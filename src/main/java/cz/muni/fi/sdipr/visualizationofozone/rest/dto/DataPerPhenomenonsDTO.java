package cz.muni.fi.sdipr.visualizationofozone.rest.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DataPerPhenomenonsDTO implements Serializable {

	@NotNull
	private Long phenomenonTypeId;
	@NotNull
	private List<DataPerStationDTO> dataPerStations;

	public DataPerPhenomenonsDTO(Long phenomenonTypeId, List<DataPerStationDTO> stationData) {
		this.phenomenonTypeId = phenomenonTypeId;
		this.dataPerStations = stationData;
	}

	public Long getPhenomenonTypeId() {
		return phenomenonTypeId;
	}

	public void setPhenomenonTypeId(Long phenomenonTypeId) {
		phenomenonTypeId = phenomenonTypeId;
	}

	public List<DataPerStationDTO> getDataPerStations() {
		return dataPerStations;
	}

	public void setDataPerStations(List<DataPerStationDTO> stationData) {
		this.dataPerStations = stationData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((phenomenonTypeId == null) ? 0 : phenomenonTypeId.hashCode());
		result = prime * result + ((dataPerStations == null) ? 0 : dataPerStations.hashCode());
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
		DataPerPhenomenonsDTO other = (DataPerPhenomenonsDTO) obj;
		if (phenomenonTypeId == null) {
			if (other.phenomenonTypeId != null)
				return false;
		} else if (!phenomenonTypeId.equals(other.phenomenonTypeId))
			return false;
		if (dataPerStations == null) {
			if (other.dataPerStations != null)
				return false;
		} else if (!dataPerStations.equals(other.dataPerStations))
			return false;
		return true;
	}

}