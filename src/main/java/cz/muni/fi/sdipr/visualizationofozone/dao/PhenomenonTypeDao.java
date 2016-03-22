package cz.muni.fi.sdipr.visualizationofozone.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cz.muni.fi.sdipr.visualizationofozone.model.PhenomenonType;

/**
 * DAO for PhenomenonType
 */
@Stateless
public class PhenomenonTypeDao {
	@PersistenceContext(unitName = "visualization-of-ozone-persistence-unit")
	private EntityManager em;

	public void create(PhenomenonType entity) {
		em.persist(entity);
	}

	public void deleteById(Long id) {
		PhenomenonType entity = em.find(PhenomenonType.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public PhenomenonType findById(Long id) {
		return em.find(PhenomenonType.class, id);
	}

	public PhenomenonType update(PhenomenonType entity) {
		return em.merge(entity);
	}

	public List<PhenomenonType> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<PhenomenonType> findAllQuery = em
				.createQuery("SELECT DISTINCT m FROM PhenomenonType m ORDER BY m.id", PhenomenonType.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}
}
