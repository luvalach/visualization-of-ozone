package cz.muni.fi.sdipr.visualizationofozone.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import cz.muni.fi.sdipr.visualizationofozone.model.Station;

/**
 *  DAO for Station
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

	public Station update(Station entity) {
		return em.merge(entity);
	}

	public List<Station> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Station> findAllQuery = em
				.createQuery("SELECT DISTINCT s FROM Station s ORDER BY s.id",
						Station.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}
}
