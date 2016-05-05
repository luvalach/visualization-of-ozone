package cz.muni.fi.sdipr.visualizationofozone.dao;

import javax.ejb.Stateless;

import cz.muni.fi.sdipr.visualizationofozone.model.ConfigProperty;

/**
 * DAO for ConfigProperty
 */
@Stateless
public class ConfigPropertyDao extends GenericDao<ConfigProperty, String> {

	public ConfigPropertyDao() {
		super(ConfigProperty.class, String.class);
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
}
