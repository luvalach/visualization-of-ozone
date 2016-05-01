package cz.muni.fi.sdipr.visualizationofozone.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cz.muni.fi.sdipr.visualizationofozone.model.ConfigProperty;

/**
 * DAO for ConfigProperty
 */
@Stateless
public class ConfigPropertyDao {
	@PersistenceContext(unitName = "visualization-of-ozone-persistence-unit")
	private EntityManager em;

	public void create(ConfigProperty entity) {
		em.persist(entity);
	}

	public void deleteByName(String Name) {
		ConfigProperty entity = em.find(ConfigProperty.class, Name);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public ConfigProperty findByName(String name) {
		return em.find(ConfigProperty.class, name);
	}

	public ConfigProperty update(ConfigProperty entity) {
		return em.merge(entity);
	}

	public List<ConfigProperty> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<ConfigProperty> findAllQuery = em.createNamedQuery("ConfigProperty.listAll", ConfigProperty.class);

		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}
}
