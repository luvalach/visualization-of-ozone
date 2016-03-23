package cz.muni.fi.sdipr.visualizationofozone.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import cz.muni.fi.sdipr.visualizationofozone.dao.MeasurementDao;
import cz.muni.fi.sdipr.visualizationofozone.dao.StationDao;
import cz.muni.fi.sdipr.visualizationofozone.model.Measurement;
import cz.muni.fi.sdipr.visualizationofozone.model.Station;
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.FileDTO;

@MessageDriven(name = "ReadStationQueueMDB", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/ReadStationQueue"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class ReadStationQueueMDB implements MessageListener {

	private final static Logger LOGGER = Logger.getLogger(ReadStationQueueMDB.class.toString());

	@EJB
	private StationDao stationDao;

	@EJB
	private MeasurementDao measurementDao;

	@EJB
	private FileParser fileParser;

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

			fileParser.parseFile(is, station, measurements, fileDto.getSource());

			stationDao.update(station);

			for (Measurement measurement : measurements) {
				measurementDao.create(measurement);
			}
			LOGGER.info("Download of file " + fileDto.getFileName() + " success. Added measuremets: "
					+ measurements.size());
		}
	}
}
// vycisteni jms queue
// http://stackoverflow.com/questions/9529214/how-to-delete-a-message-from-the-jms-queue