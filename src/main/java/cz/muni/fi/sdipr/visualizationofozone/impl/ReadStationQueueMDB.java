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

import cz.muni.fi.sdipr.visualizationofozone.dao.FileDao;
import cz.muni.fi.sdipr.visualizationofozone.dao.MeasurementDao;
import cz.muni.fi.sdipr.visualizationofozone.dao.StationDao;
import cz.muni.fi.sdipr.visualizationofozone.model.File;
import cz.muni.fi.sdipr.visualizationofozone.model.Measurement;
import cz.muni.fi.sdipr.visualizationofozone.model.Station;
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.DownloadJobDTO;

@MessageDriven(name = "ReadStationQueueMDB", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/ReadStationQueue"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "maxSession", propertyValue = "2"),
		@ActivationConfigProperty(propertyName = "transactionTimeout", propertyValue = "900000") })
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
	private FileDao fileDao;

	@Override
	public void onMessage(Message rcvMessage) {
		DownloadJobDTO downloadJob = null;

		try {
			downloadJob = messageToFileDto(rcvMessage);
		} catch (JMSException | IllegalArgumentException e) {
			LOGGER.warning("Can't extract DownloadJobDTO from message: " + e.getMessage());
			return;
		}

		try {
			LOGGER.info("Downloading file: " + downloadJob);
			downloadDataFile(downloadJob);
		} catch (IOException e) {
			LOGGER.warning("Fail to download data file: " + downloadJob + "; " + e.getMessage());
		} catch (Exception e) {
			LOGGER.warning("Fail to download, parse, or store data file: " + downloadJob + "; " + e.getMessage());
			return;
		}
	}

	private DownloadJobDTO messageToFileDto(Message rcvMessage) throws JMSException {
		if (rcvMessage instanceof ObjectMessage) {
			ObjectMessage objectMessage = (ObjectMessage) rcvMessage;
			return rcvMessage.getBody(DownloadJobDTO.class);
		} else {
			String errrorMessage = "Message of wrong type, expectiong 'ObjectMessage' but get '"
					+ rcvMessage.getClass().getName() + "'.";
			throw new IllegalArgumentException(errrorMessage);
		}
	}

	private void downloadDataFile(DownloadJobDTO downloadJob) throws IOException {
		List<Measurement> measurements = new ArrayList<>();

		try (InputStream is = downloadJob.getUrl().openStream();) {
			// Station tmpStation;
			File file = fileDao.findByFileName(downloadJob.getFileName());

			if (file == null) {
				file = new File();
				file.setFileName(downloadJob.getFileName());
				file.setStation(new Station());
				file.setSource(downloadJob.getSource());
				file.setLastUpdate(this.getZeroDate());
				file.setLastRowDate(this.getZeroDate());

				// fileDao.create(file);

				// tmpStation = new Station();
			} else {
				// tmpStation = file.getStation();
			}
			fileParser.parseFile(is, file, measurements);

			storeData(file, measurements);

			LOGGER.info("Download of file " + downloadJob.getFileName() + " success. Added measuremets: "
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

	private void storeData(File file, List<Measurement> measurements) {
		file.setLastUpdate(Calendar.getInstance().getTime());

		if (file.getStation().getId() == null) {
			Station existingStation = stationDao.findByNameAndCountry(file.getStation().getName(),
					file.getStation().getCountry());
			if (existingStation != null) {
				file.getStation().setId(existingStation.getId());
			}
		}

		file = fileDao.update(file);

		for (Measurement measurement : measurements) {
			measurement.setStationId(file.getStation().getId());
			measurementDao.create(measurement);
		}
	}
}
// vycisteni jms queue
// http://stackoverflow.com/questions/9529214/how-to-delete-a-message-from-the-jms-queue