package cz.muni.fi.sdipr.visualizationofozone.model;

import java.io.Serializable;

public class FileUpdateKey implements Serializable {
	private long sourceId;
	private long stationId;

	public FileUpdateKey() {
	}

	public FileUpdateKey(long source, long station) {
		super();
		this.sourceId = source;
		this.stationId = station;
	}

	public long getSource() {
		return sourceId;
	}

	public void setSource(long source) {
		this.sourceId = source;
	}

	public long getStation() {
		return stationId;
	}

	public void setStation(long station) {
		this.stationId = station;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		FileUpdateKey other = (FileUpdateKey) obj;
		if (sourceId != other.sourceId)
			return false;
		if (stationId != other.stationId)
			return false;
		return true;
	}

}
