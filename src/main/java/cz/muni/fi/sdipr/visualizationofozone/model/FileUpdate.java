package cz.muni.fi.sdipr.visualizationofozone.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: FileUpdate
 *
 */
@Entity
@Table(name = "FileUpdates")
@IdClass(FileUpdateKey.class)
@NamedQueries({
		@NamedQuery(name = "FileUpdate.getByIdClass", query = "SELECT f FROM FileUpdate f WHERE f.sourceId = :sourceId AND f.stationId = :stationId") })
public class FileUpdate implements Serializable {

	@Id
	private long sourceId;
	@Id
	private long stationId;
	private Date lastUpdate;
	private Date lastRowDate;
	private static final long serialVersionUID = 1L;

	public long getSourceId() {
		return sourceId;
	}

	public void setSourceId(long sourceId) {
		this.sourceId = sourceId;
	}

	public long getStationId() {
		return stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Date getLastRowDate() {
		return lastRowDate;
	}

	public void setLastRowDate(Date lastRowDate) {
		this.lastRowDate = lastRowDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lastRowDate == null) ? 0 : lastRowDate.hashCode());
		result = prime * result + ((lastUpdate == null) ? 0 : lastUpdate.hashCode());
		result = prime * result + (int) (sourceId ^ (sourceId >>> 32));
		result = prime * result + (int) (stationId ^ (stationId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileUpdate other = (FileUpdate) obj;
		if (lastRowDate == null) {
			if (other.lastRowDate != null)
				return false;
		} else if (!lastRowDate.equals(other.lastRowDate))
			return false;
		if (lastUpdate == null) {
			if (other.lastUpdate != null)
				return false;
		} else if (!lastUpdate.equals(other.lastUpdate))
			return false;
		if (sourceId != other.sourceId)
			return false;
		if (stationId != other.stationId)
			return false;
		return true;
	}

}
