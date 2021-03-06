package cz.muni.fi.sdipr.visualizationofozone.downloading.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import cz.muni.fi.sdipr.visualizationofozone.configuration.Configuration;
import cz.muni.fi.sdipr.visualizationofozone.model.File;
import cz.muni.fi.sdipr.visualizationofozone.model.Measurement;
import cz.muni.fi.sdipr.visualizationofozone.model.PhenomenonType;
import cz.muni.fi.sdipr.visualizationofozone.model.Station;

/**
 * FileParser extracts data from single file form AVDC server. Parser reads data
 * from input stream and extract phenomenons defined for station's source.
 * 
 * @author Lukas
 *
 */
@Stateless
public class FileParser {

	private final static Logger LOGGER = Logger.getLogger(FileParser.class.toString());

	@EJB
	Configuration config;

	public static final String LATITUDE_ATTR = "Latitude:";
	public static final String LONGITUDE_ATTR = "Longitude:";
	public static final String DOCUMENT_DATE_FORMAT = "yyyyMMdd'T'HHmmssSSS'Z'";
	public static final String CONFIG_DATE_FORMAT = "dd.MM.yyyy";

	private Station station;
	private List<Measurement> measurements;
	private SimpleDateFormat documentDateFormat;
	private BufferedReader reader;
	private File file;
	private Long updateFrom = Long.MIN_VALUE;

	public FileParser() {
		documentDateFormat = new SimpleDateFormat(DOCUMENT_DATE_FORMAT);
		documentDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	/**
	 * Entry method of FileParser. File given as input stream will be read. Only
	 * new, not-persisted, measurements will be added into list of measurements.
	 * Information about file update will be set into file entity. Station in
	 * the file record will be also updated. There is side effect used to return
	 * the data.
	 * 
	 * @param inputStream
	 *            opened input stream to file of station on AVDC.
	 * @param file
	 *            entity which holds data about last update of file.
	 * @param measurements
	 *            list of new measurements which will be filled by parser.
	 * @throws IOException
	 *             when IO exception occurs.
	 * @throws IllegalArgumentException
	 *             when parser cannot parse data from file due to bad/unexpected
	 *             file format.
	 */
	public void parseFile(InputStream inputStream, File file, List<Measurement> measurements)
			throws IOException, IllegalArgumentException {
		this.reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		this.measurements = measurements;
		this.station = file.getStation();
		this.file = file;

		configureParser();

		parseHeaderLine();

		skipIrrelevantLines();

		parseDataLines();
	}

	/**
	 * Read configuration and set 'updateFrom' property. All measurements oldest
	 * that date defined in 'updateFrom' property will be ignored.
	 */
	private void configureParser() {
		String startFromString = config.getPropertyValue(Configuration.DM_START_DOWNLOADING_FROM_PROPERTY);

		if (startFromString == null) {
			LOGGER.debug("File parser  could not find '" + Configuration.DM_START_DOWNLOADING_FROM_PROPERTY
					+ "' property. Whole measurements history will be stored.");
			return;
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(CONFIG_DATE_FORMAT);
			Date startFromDate = sdf.parse(startFromString);
			this.updateFrom = startFromDate.getTime();
		} catch (ParseException e) {
			LOGGER.error("The value of the property '" + Configuration.DM_START_DOWNLOADING_FROM_PROPERTY
					+ "' can't be cast to date. Property value must be in format " + CONFIG_DATE_FORMAT
					+ ". Whole measurements history will be stored.");
		}
	}

	/**
	 * Parse file header which contains station name, country, latitude and
	 * longitude.
	 * 
	 * @throws IOException
	 */
	private void parseHeaderLine() throws IOException {
		String line = null;

		if (reader.ready()) {
			line = reader.readLine();
		} else {
			throw new IllegalArgumentException("Can't read first line of file.");
		}

		// Load name and country
		setNameAndCountry(line);

		// Load latitude and longitude
		station.setLatitude(getAttributeValue(line, LATITUDE_ATTR));
		station.setLongitude(getAttributeValue(line, LONGITUDE_ATTR));

		station.setLastUpdate(new Date());
	}

	/**
	 * Parse name and country and set these values into station entity.
	 * 
	 * @param line
	 *            line from which name and country should be parsed
	 */
	private void setNameAndCountry(String line) {
		String[] nameAndTrash = line.split(" *[a-z,A-Z]+:", 2);

		if (nameAndTrash.length < 1) {
			throw new IllegalArgumentException("Station name not found.");
		}

		String[] nameAndCountry = nameAndTrash[0].split(",");

		station.setName(nameAndCountry[0]);
		station.setCountry(nameAndCountry.length < 2 ? "" : nameAndCountry[1]);
	}

	/**
	 * Get value of attribute from single line of text.
	 * 
	 * @param line
	 *            line of text from file
	 * @param attribute
	 *            name of attribute
	 * @return value after attribute name as float
	 */
	private float getAttributeValue(String line, String attribute) {
		int attrIndex = line.indexOf(attribute);

		if (attrIndex < 0) {
			throw new IllegalArgumentException("Attribute '" + attribute + "'not found on line '" + line + "'.");
		}

		String valueAndTrash = line.substring(attrIndex + attribute.length()).trim();
		String value = valueAndTrash.split(" ", 2)[0];
		if (value.length() < 1) {
			throw new IllegalArgumentException(
					"Value of attribute '" + attribute + "'not found on line '" + line + "'.");
		}

		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(
					"Can't convert value " + value + "' to float. Original line '" + line + "'.");
		}
	}

	/**
	 * Skips all rows until it finds a table header.
	 * 
	 * @throws IOException
	 */
	private void skipIrrelevantLines() throws IOException {
		while (reader.ready()) {
			String line = reader.readLine();

			if (parseLineAndFindTableHeader(line)) {
				break;
			}
		}
	}

	/**
	 * Check if actual line is a table header. The table header pattern defined
	 * in Source is used.
	 * 
	 * @param line
	 *            line of text form file
	 * @return true if line is table header, otherwise return false
	 */
	private boolean parseLineAndFindTableHeader(String line) {
		if (line.matches(file.getSource().getTableHeaderPattern())) {
			return true;
		}
		return false;
	}

	/**
	 * Read and parse data lines one by one
	 * 
	 * @throws IOException
	 */
	private void parseDataLines() throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			List<Measurement> measurementsOnRow = parseDataLine(line);
			if (measurementsOnRow != null) {
				this.measurements.addAll(measurementsOnRow);
			}
		}
	}

	/**
	 * Parse single data line. Only records measured after last update of file
	 * will be taken into account. Value of all phenomenons defined for source
	 * of file will be added into list.
	 * 
	 * @param line
	 *            line of single measurement
	 * @return list of new measurements
	 */
	private List<Measurement> parseDataLine(String line) {
		if (line.trim().length() == 0) {
			return null; // empty line
		}

		String[] splitedLine = line.trim().split(" +");

		Date dateTime = null;

		try {
			dateTime = documentDateFormat.parse(splitedLine[0]);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Can't convert string to Date. String: " + splitedLine[0]);
		}

		if (measureShouldBeStored(dateTime)) {
			List<Measurement> measurementsOnRow = new ArrayList<Measurement>();

			for (PhenomenonType phenomenon : file.getSource().getPhenomenonType()) {
				Measurement measurement = new Measurement();
				measurement.setDateTime(dateTime);
				measurement.setPhenomenonTypeId(phenomenon.getId());

				int phenomenonColumnNo = phenomenon.getColumnNo();
				if (phenomenonColumnNo < splitedLine.length) {
					measurement.setValue(Float.parseFloat(splitedLine[phenomenon.getColumnNo()]));
				} else {
					throw new IllegalArgumentException("Can't get value from columnt " + phenomenonColumnNo
							+ " from the line: '" + line + "' in the file: " + file.getFileName());
				}

				measurementsOnRow.add(measurement);
			}

			file.setLastRowDate(dateTime);
			return measurementsOnRow;
		} else {
			return null;
		}

	}

	/**
	 * Decides whether the measurement with given dateTime should be added to
	 * measurements result list (and then stored in the database). Only
	 * measurements new from last file update and newer than "updateFrom"
	 * property should be stored.
	 * 
	 * @param measurementDateTime
	 *            date and time of act of measurement
	 * @return true if given dateTime is newer than last file update and newer
	 *         than date configured by
	 *         {@value cz.muni.fi.sdipr.visualizationofozone.configuration.Configuration#DM_START_DOWNLOADING_FROM_PROPERTY}
	 *         parameter.
	 */
	private boolean measureShouldBeStored(Date measurementDateTime) {
		return measurementDateTime.getTime() > this.file.getLastRowDate().getTime()
				&& measurementDateTime.getTime() >= this.updateFrom;
	}
}
