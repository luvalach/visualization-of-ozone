package cz.muni.fi.sdipr.visualizationofozone.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.muni.fi.sdipr.visualizationofozone.dao.SourceDao;
import cz.muni.fi.sdipr.visualizationofozone.model.Source;
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.DownloadJobDTO;

@Stateless
public class DownloadManager {

	private final static Logger LOGGER = Logger.getLogger(DownloadManager.class.toString());

	@Inject
	private JMSContext context;

	@Resource(lookup = "java:/jms/queue/ReadStationQueue")
	private Queue queue;

	@EJB
	private SourceDao sourceDao;

	public void refreshDatabase() {
		List<Source> souces = sourceDao.listAll(null, null);

		int fileCounter = 0;
		for (Source source : souces) {
			List<String> fileNames = getRelativeLinksToFiles(source.getUrl());
			for (String fileName : fileNames) {
				DownloadJobDTO fileDto = new DownloadJobDTO(fileName, source);
				context.createProducer().send(queue, fileDto);
				fileCounter++;
			}
		}
		LOGGER.info(fileCounter + " files added to download queue.");
	}

	private List<String> getRelativeLinksToFiles(String baseUrl) {
		List<String> links = new ArrayList<>();
		Document doc;

		try {
			doc = Jsoup.connect(baseUrl).get();
			Elements linksElementsAll = doc.select("a[href~=^aura_omi_l2ovp_.*txt$]");
			Elements linksElements = doc.select("a[href~=^aura_omi_l2ovp_omdoao3_v03_mendel_999.txt$]");
			linksElements.addAll(doc.select("a[href~=^aura_omi_l2ovp_omdoao3_v03_amundsen.scott_111.txt$]"));
			linksElements.addAll(doc.select("a[href~=^aura_omi_l2ovp_omuvb_v03_amundsen.scott.txt$]"));

			int keepStations = 3000 - linksElements.size();
			for (Element link : linksElementsAll) {
				if (!linksElements.contains(link) && keepStations > 0) {
					linksElements.add(link);
					keepStations--;
				}
			}

			for (Element link : linksElements) {
				links.add(link.attr("href"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return links;
	}

}
