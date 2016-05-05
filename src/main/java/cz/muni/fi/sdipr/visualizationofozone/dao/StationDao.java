package cz.muni.fi.sdipr.visualizationofozone.dao;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import cz.muni.fi.sdipr.visualizationofozone.model.Station;

/**
 * DAO for Station
 */
@Stateless
public class StationDao extends GenericDao<Station, Long> {

	public StationDao() {
		super(Station.class, Long.class);
	}

	public Station findByNameAndCountry(String name, String country) {
		TypedQuery<Station> q = em.createNamedQuery("Station.findByNameAndCountry", Station.class);
		q.setParameter("name", name);
		q.setParameter("country", country);

		try {
			return q.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}
}
