package cz.muni.fi.sdipr.visualizationofozone.converter;

import javax.ejb.Singleton;

import cz.muni.fi.sdipr.visualizationofozone.model.PhenomenonType;
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.PhenomenonTypeDTO;

@Singleton
public class PhenomenonTypeConverter extends GenericConverter<PhenomenonType, PhenomenonTypeDTO> {
	public PhenomenonTypeConverter() {
		super(PhenomenonType.class, PhenomenonTypeDTO.class);
	}
}
