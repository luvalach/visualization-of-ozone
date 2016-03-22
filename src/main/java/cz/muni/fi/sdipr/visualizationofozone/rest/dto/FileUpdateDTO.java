package cz.muni.fi.sdipr.visualizationofozone.rest.dto;

import java.io.Serializable;
import cz.muni.fi.sdipr.visualizationofozone.model.FileUpdate;
import javax.persistence.EntityManager;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class FileUpdateDTO implements Serializable {

	private long sourceId;
	private long stationId;
	private Date lastUpdate;
	private Date lastRowDate;
	private long serialversionuid;

	public FileUpdateDTO() {
	}

	public FileUpdateDTO(final FileUpdate entity) {
		if (entity != null) {
			this.sourceId = entity.getSourceId();
			this.stationId = entity.getStationId();
			this.lastUpdate = entity.getLastUpdate();
			this.lastRowDate = entity.getLastRowDate();
			this.serialversionuid = entity.getSerialversionuid();
		}
	}

	public FileUpdate fromDTO(FileUpdate entity, EntityManager em) {
		if (entity == null) {
			entity = new FileUpdate();
		}
		entity.setStationId(this.stationId);
		entity.setLastUpdate(this.lastUpdate);
		entity.setLastRowDate(this.lastRowDate);
		entity = em.merge(entity);
		return entity;
	}

	public long getSourceId() {
		return this.sourceId;
	}

	public void setSourceId(final long sourceId) {
		this.sourceId = sourceId;
	}

	public long getStationId() {
		return this.stationId;
	}

	public void setStationId(final long stationId) {
		this.stationId = stationId;
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

	public void setSerialversionuid(final long serialversionuid) {
		this.serialversionuid = serialversionuid;
	}
}