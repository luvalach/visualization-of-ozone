package cz.muni.fi.sdipr.visualizationofozone.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import cz.muni.fi.sdipr.visualizationofozone.dao.ConfigPropertyDao;
import cz.muni.fi.sdipr.visualizationofozone.model.ConfigProperty;

@Singleton
public class Configuration {
	public static final String DM_TIMER_PROPERTY = "DOWNLOAD_SCHEDULER_EXPRESSION";
	public static final String DM_START_DOWNLOADING_FROM_PROPERTY = "DOWNLOAD_MEASUREMENTS_FROM_DATE";
	public static final String DM_START_DOWNLOADING_FROM_DEFAULT = "";

	@EJB
	private ConfigPropertyDao dao;

	private Map<String, String> configCache = new HashMap<String, String>();

	public String getPropertyValue(String name) {
		String value = configCache.get(name);

		if (value == null) {
			value = getPropertyValueFromDb(name);
		}

		return value;
	}

	public String getPropertyValueFromDb(String name) {
		ConfigProperty property = dao.findByName(name);

		if (property == null) {
			return null;
		}

		configCache.put(property.getName(), property.getValue());
		return property.getValue();
	}
}
