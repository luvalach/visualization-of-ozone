package cz.muni.fi.sdipr.visualizationofozone.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import cz.muni.fi.sdipr.visualizationofozone.dao.FileUpdateDao;
import cz.muni.fi.sdipr.visualizationofozone.dao.MeasurementDao;
import cz.muni.fi.sdipr.visualizationofozone.dao.StationDao;
import cz.muni.fi.sdipr.visualizationofozone.model.FileUpdate;
import cz.muni.fi.sdipr.visualizationofozone.model.Measurement;
import cz.muni.fi.sdipr.visualizationofozone.model.Station;
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.FileDTO;

@MessageDriven(name = "ReadStationQueueMDB", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/ReadStationQueue"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "maxSession", propertyValue = "2") })
public class ReadStationQueueMDB implements MessageListener {

	private final static Logger LOGGER = Logger.getLogger(ReadStationQueueMDB.class.toString());
	private static Date zeroDate;

	@EJB
	private StationDao stationDao;

	@EJB
	private MeasurementDao measurementDao;

	@EJB
	private FileParser fileParser;

	@EJB
	private FileUpdateDao fileUpdateDao;

	@Override
	public void onMessage(Message rcvMessage) {
		LOGGER.info("Message accepted :-)");
		FileDTO fileDto = null;

		try {
			fileDto = messageToFileDto(rcvMessage);
		} catch (JMSException | IllegalArgumentException e) {
			LOGGER.warning("Can't extract FileDTO from message: " + e.getMessage());
			return;
		}

		try {
			LOGGER.info("Downloading file: " + fileDto);
			downloadDataFile(fileDto);
		} catch (IOException e) {
			LOGGER.warning("Fail to download data file: " + fileDto + "; " + e.getMessage());
		} catch (Exception e) {
			LOGGER.warning("Fail to download, parse, or store data file: " + fileDto + "; " + e.getMessage());
			return;
		}
	}

	private FileDTO messageToFileDto(Message rcvMessage) throws JMSException {
		if (rcvMessage instanceof ObjectMessage) {
			ObjectMessage objectMessage = (ObjectMessage) rcvMessage;
			return rcvMessage.getBody(FileDTO.class);
		} else {
			String errrorMessage = "Message of wrong type, expectiong 'ObjectMessage' but get '"
					+ rcvMessage.getClass().getName() + "'.";
			throw new IllegalArgumentException(errrorMessage);
		}
	}

	private void downloadDataFile(FileDTO fileDto) throws IOException {
		List<Measurement> measurements = new ArrayList<>();

		try (InputStream is = fileDto.getUrl().openStream();) {
			Station station = stationDao.findByFileName(fileDto.getFileName());
			if (station == null) {
				station = new Station();
				station.setFileName(fileDto.getFileName());
				stationDao.create(station);
			}

			FileUpdate fileUpdate = fileUpdateDao.findById(fileDto.getSource().getId(), station.getId());
			if (fileUpdate == null) {
				fileUpdate = new FileUpdate();
				fileUpdate.setStationId(station.getId());
				fileUpdate.setSourceId(fileDto.getSource().getId());
				fileUpdate.setLastUpdate(this.getZeroDate());
				fileUpdate.setLastRowDate(this.getZeroDate());
				fileUpdateDao.create(fileUpdate);
			}

			fileParser.parseFile(is, station, measurements, fileDto.getSource(), fileUpdate);

			stationDao.update(station);

			fileUpdate.setLastUpdate(Calendar.getInstance().getTime());
			fileUpdateDao.update(fileUpdate);

			for (Measurement measurement : measurements) {
				measurementDao.create(measurement);
			}

			LOGGER.info("Download of file " + fileDto.getFileName() + " success. Added measuremets: "
					+ measurements.size());
		}
	}

	/**
	 * Return date of start of the epoch. Date should be older that all
	 * measurements.
	 * 
	 * @return date of start of the epoch.
	 */
	private Date getZeroDate() {
		if (ReadStationQueueMDB.zeroDate == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(0L);
			ReadStationQueueMDB.zeroDate = calendar.getTime();
		}
		return ReadStationQueueMDB.zeroDate;
	}
}
// vycisteni jms queue
// http://stackoverflow.com/questions/9529214/how-to-delete-a-message-from-the-jms-queue