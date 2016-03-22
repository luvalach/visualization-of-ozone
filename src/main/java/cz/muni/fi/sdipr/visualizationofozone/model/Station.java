package cz.muni.fi.sdipr.visualizationofozone.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Stations")
@NamedQueries({ @NamedQuery(name = "Station.deleteAll", query = "DELETE FROM Station"),
		@NamedQuery(name = "Station.getAllIds", query = "SELECT DISTINCT s.id FROM Station s ORDER BY s.id"),
		@NamedQuery(name = "Station.findByFileName", query = "SELECT s FROM Station s WHERE s.fileName = :fileName") })
public class Station implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stations_id_seq")
	@SequenceGenerator(name = "stations_id_seq", sequenceName = "stations_id_seq", allocationSize = 100)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	private String name;
	private String country;
	@Column(unique = true)
	private String fileName;
	private float latitude;
	private float longitude;
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}