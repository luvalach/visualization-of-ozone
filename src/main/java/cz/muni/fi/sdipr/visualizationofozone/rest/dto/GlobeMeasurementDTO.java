package cz.muni.fi.sdipr.visualizationofozone.rest.dto;

import java.util.Date;

public class GlobeMeasurementDTO {
	private Date dateTime;
	private long phenomenonTypeId;
	private float value;

	public GlobeMeasurementDTO(Date dateTime, long phenomenonTypeId, float value) {
		super();
		this.dateTime = dateTime;
		this.phenomenonTypeId = phenomenonTypeId;
		this.value = value;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public long getPhenomenonTypeId() {
		return phenomenonTypeId;
	}

	public void setPhenomenonTypeId(long phenomenonTypeId) {
		this.phenomenonTypeId = phenomenonTypeId;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + (int) (phenomenonTypeId ^ (phenomenonTypeId >>> 32));
		result = prime * result + Float.floatToIntBits(value);
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
		GlobeMeasurementDTO other = (GlobeMeasurementDTO) obj;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (phenomenonTypeId != other.phenomenonTypeId)
			return false;
		if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value))
			return false;
		return true;
	}
}
