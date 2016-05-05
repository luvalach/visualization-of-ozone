package cz.muni.fi.sdipr.visualizationofozone.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: Measure
 *
 */
@Entity
@Table(name = "Measurements")
@IdClass(MeasurementKey.class)
@NamedQueries({
		@NamedQuery(name = "Measurement.getByIdClass", query = "SELECT m FROM Measurement m WHERE m.dateTime = :dateTime AND m.phenomenonTypeId = :phenomenonTypeId AND m.stationId = :stationId"),
		@NamedQuery(name = "Measurement.findByDates", query = "SELECT DISTINCT m FROM Measurement m WHERE m.dateTime BETWEEN :fromDate AND :toDate ORDER BY m.stationId, m.phenomenonTypeId, m.dateTime"),
		@NamedQuery(name = "Measurement.findByStationPhenomenonDates", query = "SELECT DISTINCT m FROM Measurement m WHERE m.stationId = :stationId AND m.phenomenonTypeId = :phenomenonTypeId AND(m.dateTime BETWEEN :fromDate AND :toDate) ORDER BY m.dateTime") })
public class Measurement implements BaseEntity<MeasurementKey> {

	@Id
	private Date dateTime;
	@Id
	private long stationId;
	@Id
	private long phenomenonTypeId;
	private float value;
	private static final long serialVersionUID = 1L;

	@Override
	public MeasurementKey getId() {
		return new MeasurementKey(this.dateTime, this.stationId, this.phenomenonTypeId);
	}

	@Override
	public void setId(MeasurementKey id) {
		this.dateTime = id.getDateTime();
		this.stationId = id.getStationId();
		this.phenomenonTypeId = id.getPhenomenonTypeId();
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

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + (int) (phenomenonTypeId ^ (phenomenonTypeId >>> 32));
		result = prime * result + (int) (stationId ^ (stationId >>> 32));
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
		Measurement other = (Measurement) obj;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (phenomenonTypeId != other.phenomenonTypeId)
			return false;
		if (stationId != other.stationId)
			return false;
		if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value))
			return false;
		return true;
	}
}
