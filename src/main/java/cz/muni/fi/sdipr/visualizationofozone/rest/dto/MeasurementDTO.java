package cz.muni.fi.sdipr.visualizationofozone.rest.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.xml.bind.annotation.XmlRootElement;

import cz.muni.fi.sdipr.visualizationofozone.model.Measurement;

@XmlRootElement
public class MeasurementDTO implements Serializable {

	private Date dateTime;
	private long stationId;
	private long phenomenonTypeId;
	private float value;
	private long serialversionuid;

	public MeasurementDTO() {
	}

	public MeasurementDTO(final Measurement entity) {
		if (entity != null) {
			this.dateTime = entity.getDateTime();
			this.stationId = entity.getStationId();
			this.phenomenonTypeId = entity.getPhenomenonTypeId();
			this.value = entity.getValue();
			this.serialversionuid = entity.getSerialversionuid();
		}
	}

	public Measurement fromDTO(Measurement entity, EntityManager em) {
		if (entity == null) {
			entity = new Measurement();
		}
		entity.setDateTime(this.dateTime);
		entity.setStationId(this.stationId);
		entity.setPhenomenonTypeId(this.phenomenonTypeId);
		entity.setValue(this.value);
		entity = em.merge(entity);
		return entity;
	}

	public Date getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(final Date dateTime) {
		this.dateTime = dateTime;
	}

	public long getStationId() {
		return this.stationId;
	}

	public void setStationId(final long stationId) {
		this.stationId = stationId;
	}

	public long getPhenomenonTypeId() {
		return this.phenomenonTypeId;
	}

	public void setPhenomenonTypeId(final long phenomenonTypeId) {
		this.phenomenonTypeId = phenomenonTypeId;
	}

	public float getValue() {
		return this.value;
	}

	public void setValue(final float value) {
		this.value = value;
	}

	public long getSerialversionuid() {
		return this.serialversionuid;
	}

	public void setSerialversionuid(final long serialversionuid) {
		this.serialversionuid = serialversionuid;
	}
}