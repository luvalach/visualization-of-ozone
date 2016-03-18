package cz.muni.fi.sdipr.visualizationofozone.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: Measure
 *
 */
@Entity
@Table(name = "Measures")
@IdClass(MeasureKey.class)
public class Measure implements Serializable {

	@Id
	private Date dateTime;
	@Id
	private Station Station;
	@Id
	private MeasureType measureType;
	private float value;
	private static final long serialVersionUID = 1L;

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

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
