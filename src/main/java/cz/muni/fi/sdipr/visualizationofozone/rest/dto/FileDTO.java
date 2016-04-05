package cz.muni.fi.sdipr.visualizationofozone.rest.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.xml.bind.annotation.XmlRootElement;

import cz.muni.fi.sdipr.visualizationofozone.model.File;
import cz.muni.fi.sdipr.visualizationofozone.model.Station;

@XmlRootElement
public class FileDTO implements Serializable {

	private String fileName;
	private NestedSourceDTO source;
	private Station station;
	private Date lastUpdate;
	private Date lastRowDate;
	private long serialversionuid;

	public FileDTO() {
	}

	public FileDTO(final File entity) {
		if (entity != null) {
			this.fileName = entity.getFileName();
			this.station = entity.getStation();
			this.lastUpdate = entity.getLastUpdate();
			this.lastRowDate = entity.getLastRowDate();
			this.serialversionuid = entity.getSerialversionuid();
			this.source = new NestedSourceDTO(entity.getSource());
		}
	}

	public File fromDTO(File entity, EntityManager em) {
		if (entity == null) {
			entity = new File();
		}
		entity.setStation(this.station);
		entity.setLastUpdate(this.lastUpdate);
		entity.setLastRowDate(this.lastRowDate);
		entity = em.merge(entity);
		return entity;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	public Station getStation() {
		return this.station;
	}

	public void setStation(final Station station) {
		this.station = station;
	}

	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(final Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Date getLastRowDate() {
		return this.lastRowDate;
	}

	public void setLastRowDate(final Date lastRowDate) {
		this.lastRowDate = lastRowDate;
	}

	public long getSerialversionuid() {
		return this.serialversionuid;
	}

	public NestedSourceDTO getSource() {
		return source;
	}

	public void setSource(NestedSourceDTO source) {
		this.source = source;
	}

	public void setSerialversionuid(final long serialversionuid) {
		this.serialversionuid = serialversionuid;
	}
}