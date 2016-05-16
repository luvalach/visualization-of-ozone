package cz.muni.fi.sdipr.visualizationofozone.downloading.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;

import org.jboss.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.muni.fi.sdipr.visualizationofozone.dao.SourceDao;
import cz.muni.fi.sdipr.visualizationofozone.downloading.dto.DownloadJobDTO;
import cz.muni.fi.sdipr.visualizationofozone.model.Source;

/**
 * This class should iterate over all Sources read source's URL and get link to
 * all files on source's page. Then add all discovered files into the download
 * queue.
 * 
 * @author Lukas Valach
 *
 */
@Stateless
public class DownloadManager {

	private final static Logger LOGGER = Logger.getLogger(DownloadManager.class.toString());

	private final static String FILE_NAME_PATTERN = "a[href~=^aura_omi_l2ovp_.*txt$]";

	@Inject
	private JMSContext context;

	@Resource(lookup = "java:/jms/queue/ReadStationQueue")
	private Queue queue;

	@EJB
	private SourceDao sourceDao;

	/**
	 * This method starts process of database refreshment. All sources will be
	 * checked and all discovered files will be added do download queue.
	 */
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

	/**
	 * This method download webpage defined in source's URL address and extract
	 * links to all files.
	 * 
	 * @param baseUrl
	 * @return
	 */
	private List<String> getRelativeLinksToFiles(String baseUrl) {
		List<String> links = new ArrayList<>();
		Document doc;

		try {
			doc = Jsoup.connect(baseUrl).get();
			Elements linksElements = doc.select(FILE_NAME_PATTERN);

			for (Element link : linksElements) {
				links.add(link.attr("href"));
			}
		} catch (IOException e) {
			LOGGER.error("Failed to connect to '" + baseUrl + "', can't get list of links to files.", e);
		}
		return links;
	}

	/**
	 * Alternative for {@link #getRelativeLinksToFiles(String baseUrl)} method.
	 * This method is intended only for testing. This method should return only
	 * few predefined links.
	 * 
	 * @param baseUrl
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<String> getRelativeLinksToFilesDebugMode(String baseUrl) {
		int numberOfLinks = 3;
		List<String> links = new ArrayList<>();
		Document doc;

		try {
			doc = Jsoup.connect(baseUrl).get();
			Elements linksElementsAll = doc.select(FILE_NAME_PATTERN);
			Elements linksElements = doc.select("a[href~=^aura_omi_l2ovp_omdoao3_v03_mendel_999.txt$]");
			linksElements.addAll(doc.select("a[href~=^aura_omi_l2ovp_omdoao3_v03_amundsen.scott_111.txt$]"));
			linksElements.addAll(doc.select("a[href~=^aura_omi_l2ovp_omuvb_v03_amundsen.scott.txt$]"));

			int keepStations = numberOfLinks - linksElements.size();
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
			LOGGER.error("Failed to connect to '" + baseUrl + "', can't get list of links to files.", e);
		}
		return links;
	}

}
