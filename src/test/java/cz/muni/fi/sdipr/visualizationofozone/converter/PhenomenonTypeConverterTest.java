package cz.muni.fi.sdipr.visualizationofozone.converter;

import java.text.ParseException;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.runner.RunWith;

import cz.muni.fi.sdipr.visualizationofozone.model.PhenomenonType;
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.PhenomenonTypeDTO;

@RunWith(Arquillian.class)
public class PhenomenonTypeConverterTest extends GenericConverterTest<PhenomenonType, PhenomenonTypeDTO> {

	public static final Long STATION_ID = 3L;
	public static final String STATION_NAME = "Phenomenon name";
	public static final String STATION_NAME_SHOTRCUT = "Phenomenon name shortcut";
	public static final String STATION_DESCRIPTION = "Phenomenon description";
	public static final String STATION_UNIT = "Phenomenon unit";
	public static final String STATION_UNIT_SHORTCUT = "Phenomenon unit shortcut";

	@EJB
	private PhenomenonTypeConverter converter;

	@Override
	protected PhenomenonType initEntity() throws ParseException {
		PhenomenonType entity = new PhenomenonType();
		entity.setId(STATION_ID);
		entity.setName(STATION_NAME);
		entity.setNameShortcut(STATION_NAME_SHOTRCUT);
		entity.setDescription(STATION_DESCRIPTION);
		entity.setUnit(STATION_UNIT);
		entity.setUnitShortcut(STATION_UNIT_SHORTCUT);

		return entity;
	}

	@Override
	protected PhenomenonTypeDTO initDto() throws ParseException {
		PhenomenonTypeDTO dto = new PhenomenonTypeDTO();
		dto.setId(STATION_ID);
		dto.setName(STATION_NAME);
		dto.setNameShortcut(STATION_NAME_SHOTRCUT);
		dto.setDescription(STATION_DESCRIPTION);
		dto.setUnit(STATION_UNIT);
		dto.setUnitShortcut(STATION_UNIT_SHORTCUT);

		return dto;
	}

	@Override
	protected PhenomenonTypeConverter getConverter() {
		return converter;
	}

}
