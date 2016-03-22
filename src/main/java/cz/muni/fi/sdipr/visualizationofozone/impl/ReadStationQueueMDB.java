package cz.muni.fi.sdipr.visualizationofozone.impl;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(name = "ReadStationQueueMDB", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/ReadStationQueue"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class ReadStationQueueMDB implements MessageListener {

	private final static Logger LOGGER = Logger.getLogger(ReadStationQueueMDB.class.toString());

	@Override
	public void onMessage(Message rcvMessage) {
		LOGGER.info("Message accepted :-)");

		TextMessage msg = null;
		try {
			if (rcvMessage instanceof TextMessage) {
				msg = (TextMessage) rcvMessage;
				LOGGER.info("Received Message from queue: " + msg.getText());
			} else {
				LOGGER.warning("Message of wrong type: " + rcvMessage.getClass().getName());
			}
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

}
