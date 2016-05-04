package cz.muni.fi.sdipr.visualizationofozone.rest.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PhenomenonTypeDTO implements Serializable {

	private Long id;
	private String name;
	private String nameShortcut;
	private NestedSourceDTO source;
	private int columnNo;
	private String unit;
	private String unitShortcut;
	private String description;

	public PhenomenonTypeDTO() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getNameShortcut() {
		return this.nameShortcut;
	}

	public void setNameShortcut(final String nameShortcut) {
		this.nameShortcut = nameShortcut;
	}

	public NestedSourceDTO getSource() {
		return this.source;
	}

	public void setSource(final NestedSourceDTO source) {
		this.source = source;
	}

	public int getColumnNo() {
		return this.columnNo;
	}

	public void setColumnNo(final int columnNo) {
		this.columnNo = columnNo;
	}

	public String getUnit() {
		return this.unit;
	}

	public void setUnit(final String unit) {
		this.unit = unit;
	}

	public String getUnitShortcut() {
		return this.unitShortcut;
	}

	public void setUnitShortcut(final String unitShortcut) {
		this.unitShortcut = unitShortcut;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + columnNo;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nameShortcut == null) ? 0 : nameShortcut.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		result = prime * result + ((unitShortcut == null) ? 0 : unitShortcut.hashCode());
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
		PhenomenonTypeDTO other = (PhenomenonTypeDTO) obj;
		if (columnNo != other.columnNo)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nameShortcut == null) {
			if (other.nameShortcut != null)
				return false;
		} else if (!nameShortcut.equals(other.nameShortcut))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		if (unitShortcut == null) {
			if (other.unitShortcut != null)
				return false;
		} else if (!unitShortcut.equals(other.unitShortcut))
			return false;
		return true;
	}
}