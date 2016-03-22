package cz.muni.fi.sdipr.visualizationofozone.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import cz.muni.fi.sdipr.visualizationofozone.model.Source;

/**
 *  DAO for Source
 */
@Stateless
public class SourceDao {
	@PersistenceContext(unitName = "visualization-of-ozone-persistence-unit")
	private EntityManager em;

	public void create(Source entity) {
		em.persist(entity);
	}

	public void deleteById(Long id) {
		Source entity = em.find(Source.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public Source findById(Long id) {
		return em.find(Source.class, id);
	}

	public Source update(Source entity) {
		return em.merge(entity);
	}

	public List<Source> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Source> findAllQuery = em.createQuery(
				"SELECT DISTINCT s FROM Source s ORDER BY s.id", Source.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}
}
