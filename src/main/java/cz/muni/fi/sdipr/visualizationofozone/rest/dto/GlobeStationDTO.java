package cz.muni.fi.sdipr.visualizationofozone.rest.dto;

import java.util.List;

public class GlobeStationDTO {
	private long stationId;
	private List<GlobeMeasurementDTO> measurements;

	public GlobeStationDTO(long stationId, List<GlobeMeasurementDTO> measurements) {
		super();
		this.stationId = stationId;
		this.measurements = measurements;
	}

	public long getStationId() {
		return stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public List<GlobeMeasurementDTO> getMeasurements() {
		return measurements;
	}

	public void setMeasurements(List<GlobeMeasurementDTO> measurements) {
		this.measurements = measurements;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((measurements == null) ? 0 : measurements.hashCode());
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
		GlobeStationDTO other = (GlobeStationDTO) obj;
		if (measurements == null) {
			if (other.measurements != null)
				return false;
		} else if (!measurements.equals(other.measurements))
			return false;
		if (stationId != other.stationId)
			return false;
		return true;
	}
}
