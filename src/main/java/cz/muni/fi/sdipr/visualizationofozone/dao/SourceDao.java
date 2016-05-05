package cz.muni.fi.sdipr.visualizationofozone.dao;

import javax.ejb.Stateless;

import cz.muni.fi.sdipr.visualizationofozone.model.Source;

/**
 * DAO for Source
 */
@Stateless
public class SourceDao extends GenericDao<Source, Long> {

	public SourceDao() {
		super(Source.class, Long.class);
	}
}
