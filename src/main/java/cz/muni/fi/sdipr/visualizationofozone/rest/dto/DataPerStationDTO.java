package cz.muni.fi.sdipr.visualizationofozone.rest.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

public class DataPerStationDTO {

	@NotNull
	private Long stationId;
	@NotNull
	private List<Number[]> measurements;

	private long lastMeasurementDate;
	private float lastMeasurementValue;

	public DataPerStationDTO(Long stationId) {
		this.stationId = stationId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public List<Number[]> getMeasurements() {
		return measurements;
	}

	public void setMeasurements(List<Number[]> measurements) {
		this.measurements = measurements;
	}

	public long getLastMeasurementDate() {
		if (measurements.size() == 0)
			return -1L;
		return measurements.get(measurements.size() - 1)[0].longValue();
	}

	public float getLastMeasurementValue() {
		if (measurements.size() == 0)
			return -1f;
		return measurements.get(measurements.size() - 1)[1].floatValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((measurements == null) ? 0 : measurements.hashCode());
		result = prime * result + ((stationId == null) ? 0 : stationId.hashCode());
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
		DataPerStationDTO other = (DataPerStationDTO) obj;
		if (measurements == null) {
			if (other.measurements != null)
				return false;
		} else if (!measurements.equals(other.measurements))
			return false;
		if (stationId == null) {
			if (other.stationId != null)
				return false;
		} else if (!stationId.equals(other.stationId))
			return false;
		return true;
	}

}
