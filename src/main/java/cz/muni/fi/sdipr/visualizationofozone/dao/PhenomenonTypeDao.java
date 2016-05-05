package cz.muni.fi.sdipr.visualizationofozone.dao;

import javax.ejb.Stateless;

import cz.muni.fi.sdipr.visualizationofozone.model.PhenomenonType;

/**
 * DAO for PhenomenonType
 */
@Stateless
public class PhenomenonTypeDao extends GenericDao<PhenomenonType, Long> {

	public PhenomenonTypeDao() {
		super(PhenomenonType.class, Long.class);
	}
}
