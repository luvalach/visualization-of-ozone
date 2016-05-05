package cz.muni.fi.sdipr.visualizationofozone.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "Sources")
public class Source implements BaseEntity<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(nullable = false)
	@NotBlank
	private String url;
	@NotBlank
	private String tableHeaderPattern;
	private String description;
	@OneToMany(mappedBy = "source")
	private List<PhenomenonType> phenomenonType;

	@Override
	public Long getId() {
		return id;
	}

	@Override
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

	public String getTableHeaderPattern() {
		return tableHeaderPattern;
	}

	public void setTableHeaderPattern(String tableHeaderPattern) {
		this.tableHeaderPattern = tableHeaderPattern;
	}
}