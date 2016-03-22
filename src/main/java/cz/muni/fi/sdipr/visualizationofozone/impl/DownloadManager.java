package cz.muni.fi.sdipr.visualizationofozone.impl;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;

@Stateless
public class DownloadManager {

	private final static Logger LOGGER = Logger.getLogger(DownloadManager.class.toString());

	@Inject
	private JMSContext context;

	@Resource(lookup = "java:/jms/queue/ReadStationQueue")
	private Queue queue;

	public void refreshDatabase() {

		int MSG_COUNT = 3;
		for (int i = 0; i < MSG_COUNT; i++) {
			String text = "This is message " + (i + 1);
			context.createProducer().send(queue, text);
		}
		LOGGER.info("Messages has been send.");
	}

}
