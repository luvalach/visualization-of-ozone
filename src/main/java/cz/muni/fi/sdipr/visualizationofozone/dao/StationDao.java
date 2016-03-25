package cz.muni.fi.sdipr.visualizationofozone.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cz.muni.fi.sdipr.visualizationofozone.model.Station;

/**
 * DAO for Station
 */
@Stateless
public class StationDao {
	@PersistenceContext(unitName = "visualization-of-ozone-persistence-unit")
	private EntityManager em;

	public void create(Station entity) {
		em.persist(entity);
	}

	public void deleteById(Long id) {
		Station entity = em.find(Station.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public Station findById(Long id) {
		return em.find(Station.class, id);
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

	public Station update(Station entity) {
		return em.merge(entity);
	}

	public List<Station> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Station> q = em.createNamedQuery("Station.listAll", Station.class);
		if (startPosition != null) {
			q.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			q.setMaxResults(maxResult);
		}
		return q.getResultList();
	}

	public List<Long> getAllIds() {
		TypedQuery<Long> q = em.createNamedQuery("Station.getAllIds", Long.class);
		return q.getResultList();
	}

	public void deleteAll() {
		Query q = em.createNamedQuery("Station.deleteAll");
		q.executeUpdate();
	}
}
