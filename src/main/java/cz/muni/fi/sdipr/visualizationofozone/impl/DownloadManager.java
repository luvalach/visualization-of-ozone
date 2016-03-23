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
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.FileDTO;

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
				FileDTO fileDto = new FileDTO(fileName, source);
				context.createProducer().send(queue, fileDto);
				fileCounter++;
			}
		}
		LOGGER.info(fileCounter + " files added to download queue.");
	}

	// @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	// private void persistMeasures(int y) {
	// Measure2 m = null;
	// for (int j = 1; j < 1000; j++) {
	// int i = j + 1000 * y;
	// m = new Measure2();
	// m.setValue(i);
	// Date d = new Date();
	// Calendar c = Calendar.getInstance();
	// c.add(Calendar.DAY_OF_YEAR, i % 1000);
	// c.add(Calendar.SECOND, i % 100000);
	//
	// m.setDateTime(c.getTime());
	// m.setStationId(i % 700);
	// m.setPhenomenonTypeId(i % 150);
	// try {
	// em.persist(m);
	// } catch (Exception ex) {
	// System.out.println(i + " Exception: " + ex.getMessage());
	// System.out.println("date " + m.getDateTime());
	// System.out.println("stationId " + m.getStationId());
	// System.out.println("typeId " + m.getPhenomenonTypeId());
	// System.out.println("value " + m.getValue());
	// }
	// }
	// em.flush();
	// if (m == null) {
	// return;
	// }
	// // System.out.println("y " + y);
	// LOGGER.log(Level.INFO, "y " + y);
	// // System.out.println("date " + m.getDateTime());
	// // System.out.println("stationId " + m.getStationId());
	// // System.out.println("typeId " + m.getPhenomenonTypeId());
	// // System.out.println("value " + m.getValue());
	// }

	// @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	// private void persistStation(Station s) {
	// stationDao.deleteByFileName(s.getFileName());
	// stationDao.create(s);
	// }

	// private Map<String, URL> getFileNamesAndUrls(Source source) {
	// List<String> fileNames = getRelativeLinksToFiles(source.getUrl());
	// Map<String, Source> urls = new TreeMap<>();
	//
	// for (String link : fileNames) {
	// try {
	// urls.put(link, new URL(baseUrl + "/" + link));
	// } catch (MalformedURLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// return urls;
	// }

	private List<String> getRelativeLinksToFiles(String baseUrl) {
		List<String> links = new ArrayList<>();
		Document doc;

		try {
			doc = Jsoup.connect(baseUrl).get();
			// The 'a' element with href attribute
			// Elements linksElements =
			Elements linksElementsAll = doc.select("a[href~=^aura_omi_l2ovp_omdoao3_v03.*txt$]");
			Elements linksElements = doc.select("a[href~=^aura_omi_l2ovp_omdoao3_v03_mendel_999.txt$]");
			linksElements.addAll(doc.select("a[href~=^aura_omi_l2ovp_omdoao3_v03_amundsen.scott_111.txt$]"));
			linksElements.addAll(doc.select("a[href~=^aura_omi_l2ovp_omdoao3_v03_aarhus_034.txt$]"));

			int keepStations = 20 - linksElements.size();
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
