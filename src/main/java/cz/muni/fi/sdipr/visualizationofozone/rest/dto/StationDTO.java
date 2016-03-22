package cz.muni.fi.sdipr.visualizationofozone.rest.dto;

import java.io.Serializable;
import cz.muni.fi.sdipr.visualizationofozone.model.Station;
import javax.persistence.EntityManager;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class StationDTO implements Serializable {

	private Long id;
	private String name;
	private String country;
	private String fileName;
	private float latitude;
	private float longitude;
	private Date lastUpdate;

	public StationDTO() {
	}

	public StationDTO(final Station entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.name = entity.getName();
			this.country = entity.getCountry();
			this.fileName = entity.getFileName();
			this.latitude = entity.getLatitude();
			this.longitude = entity.getLongitude();
			this.lastUpdate = entity.getLastUpdate();
		}
	}

	public Station fromDTO(Station entity, EntityManager em) {
		if (entity == null) {
			entity = new Station();
		}
		entity.setName(this.name);
		entity.setCountry(this.country);
		entity.setFileName(this.fileName);
		entity.setLatitude(this.latitude);
		entity.setLongitude(this.longitude);
		entity.setLastUpdate(this.lastUpdate);
		entity = em.merge(entity);
		return entity;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(final String country) {
		this.country = country;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	public float getLatitude() {
		return this.latitude;
	}

	public void setLatitude(final float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return this.longitude;
	}

	public void setLongitude(final float longitude) {
		this.longitude = longitude;
	}

	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(final Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}