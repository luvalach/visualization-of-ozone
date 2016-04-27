package cz.muni.fi.sdipr.visualizationofozone.converter;

import javax.ejb.Singleton;

import cz.muni.fi.sdipr.visualizationofozone.model.Station;
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.StationDTO;

@Singleton
public class StationConverter extends GenericConverter<Station, StationDTO> {

	public StationConverter() {
		super(Station.class, StationDTO.class);
	}
}
