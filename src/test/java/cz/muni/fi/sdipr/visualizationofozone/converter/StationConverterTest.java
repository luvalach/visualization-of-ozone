package cz.muni.fi.sdipr.visualizationofozone.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.runner.RunWith;

import cz.muni.fi.sdipr.visualizationofozone.model.Station;
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.StationDTO;

@RunWith(Arquillian.class)
public class StationConverterTest extends GenericConverterTest<Station, StationDTO> {

	public static final String CONFIG_DATE_FORMAT = "dd.MM.yyyy";
	public static final Long STATION_ID = 37L;
	public static final String STATION_NAME = "Station name";
	public static final String STATION_COUNTRY = "Station country";
	public static final String STATION_LASTUPDATE = "1.1.2010";
	public static final long STATION_LAT = 2;
	public static final long STATION_LON = 4;

	@EJB
	private StationConverter converter;

	private Station stationEntity;
	private StationDTO stationDto;
	private Date lastUpdate;

	@Override
	protected Station initEntity() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(CONFIG_DATE_FORMAT);
		lastUpdate = sdf.parse(STATION_LASTUPDATE);

		Station stationEntity = new Station();
		stationEntity.setId(STATION_ID);
		stationEntity.setName(STATION_NAME);
		stationEntity.setCountry(STATION_COUNTRY);
		stationEntity.setLastUpdate(lastUpdate);
		stationEntity.setLatitude(STATION_LAT);
		stationEntity.setLongitude(STATION_LON);

		return stationEntity;
	}

	@Override
	protected StationDTO initDto() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(CONFIG_DATE_FORMAT);
		lastUpdate = sdf.parse(STATION_LASTUPDATE);

		StationDTO stationDto = new StationDTO();
		stationDto.setId(STATION_ID);
		stationDto.setName(STATION_NAME);
		stationDto.setCountry(STATION_COUNTRY);
		stationDto.setLastUpdate(lastUpdate);
		stationDto.setLatitude(STATION_LAT);
		stationDto.setLongitude(STATION_LON);

		return stationDto;
	}

	@Override
	protected GenericConverter<Station, StationDTO> getConverter() {
		return converter;
	}

}
