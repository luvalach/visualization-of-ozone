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
import java.util.logging.Logger;

import javax.ejb.Stateless;

import cz.muni.fi.sdipr.visualizationofozone.model.File;
import cz.muni.fi.sdipr.visualizationofozone.model.Measurement;
import cz.muni.fi.sdipr.visualizationofozone.model.PhenomenonType;
import cz.muni.fi.sdipr.visualizationofozone.model.Station;

@Stateless
public class FileParser {

	private final static Logger LOGGER = Logger.getLogger(FileParser.class.toString());

	public static final String LATITUDE_ATTR = "Latitude:";
	public static final String LONGITUDE_ATTR = "Longitude:";
	public static final int DATE_TIME_COLUM_NO = 0;
	public static final int OYONE_COLUMN_NO = 11;

	private Station station;
	private List<Measurement> measurements;
	private SimpleDateFormat simpleDateFormat;
	private BufferedReader reader;
	private File file;

	public FileParser() {
		simpleDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmssSSS'Z'");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public void parseFile(InputStream inputStream, File file, List<Measurement> measurements)
			throws IOException, IllegalArgumentException {
		this.reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		this.measurements = measurements;
		this.station = file.getStation();
		this.file = file;

		parseHeaderLine();

		skipIrrelevantLines();

		parseDataLines();
	}

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

	private void setNameAndCountry(String line) {
		String[] nameAndTrash = line.split(" *[a-z,A-Z]+:", 2);

		if (nameAndTrash.length < 1) {
			throw new IllegalArgumentException("Station name not found.");
		}

		String[] nameAndCountry = nameAndTrash[0].split(",");

		station.setName(nameAndCountry[0]);
		station.setCountry(nameAndCountry.length < 2 ? "" : nameAndCountry[1]);
	}

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

	private void skipIrrelevantLines() throws IOException {
		while (reader.ready()) {
			String line = reader.readLine();

			if (parseLineAndFindTableHeader(line)) {
				break;
			}
		}
	}

	private boolean parseLineAndFindTableHeader(String line) {
		if (line.matches(file.getSource().getTableHeaderPattern())) {
			return true;
		}
		return false;
	}

	private void parseDataLines() throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			List<Measurement> measurementsOnRow = parseDataLine(line);
			if (measurementsOnRow != null) {
				this.measurements.addAll(measurementsOnRow);
			}
		}
	}

	private List<Measurement> parseDataLine(String line) {
		if (line.trim().length() == 0) {
			return null; // empty line
		}

		String[] splitedLine = line.trim().split(" +");

		Date dateTime = null;

		try {
			dateTime = simpleDateFormat.parse(splitedLine[0]);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Can't convert string to Date. String: " + splitedLine[0]);
		}

		if (dateTime.getTime() > this.file.getLastRowDate().getTime()) {
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
}
