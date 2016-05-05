package cz.muni.fi.sdipr.visualizationofozone.model;

import java.io.Serializable;
import java.util.Date;

public class MeasurementKey implements Serializable {
	private Date dateTime;
	private long stationId;
	private long phenomenonTypeId;

	public MeasurementKey() {
	}

	public MeasurementKey(Date dateTime, long stationId, long phenomenonTypeId) {
		super();
		this.dateTime = dateTime;
		this.stationId = stationId;
		this.phenomenonTypeId = phenomenonTypeId;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public long getStationId() {
		return stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public long getPhenomenonTypeId() {
		return phenomenonTypeId;
	}

	public void setPhenomenonTypeId(long phenomenonTypeId) {
		this.phenomenonTypeId = phenomenonTypeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + (int) (phenomenonTypeId ^ (phenomenonTypeId >>> 32));
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
		MeasurementKey other = (MeasurementKey) obj;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (phenomenonTypeId != other.phenomenonTypeId)
			return false;
		if (stationId != other.stationId)
			return false;
		return true;
	}

}
