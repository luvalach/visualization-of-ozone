package cz.muni.fi.sdipr.visualizationofozone.model;

import java.io.Serializable;
import java.util.Date;

public class MeasureKey implements Serializable {
	private Date dateTime;
	private Station Station;
	private MeasureType measureType;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public Station getStation() {
		return Station;
	}

	public void setStation(Station station) {
		Station = station;
	}

	public MeasureType getMeasureType() {
		return measureType;
	}

	public void setMeasureType(MeasureType measureType) {
		this.measureType = measureType;
	}

}
