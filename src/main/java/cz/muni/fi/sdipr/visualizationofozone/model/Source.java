package cz.muni.fi.sdipr.visualizationofozone.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Sources")
public class Source implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	private String url;
	private String description;
	@OneToMany(mappedBy = "source")
	private List<PhenomenonType> phenomenonType = new ArrayList<PhenomenonType>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<PhenomenonType> getPhenomenonType() {
		return phenomenonType;
	}

	public void setPhenomenonType(List<PhenomenonType> phenomenonType) {
		this.phenomenonType = phenomenonType;
	}
}