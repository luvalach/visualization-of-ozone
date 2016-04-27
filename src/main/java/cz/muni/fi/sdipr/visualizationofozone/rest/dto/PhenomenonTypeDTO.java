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
}