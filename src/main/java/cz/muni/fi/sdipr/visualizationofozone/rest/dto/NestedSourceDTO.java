package cz.muni.fi.sdipr.visualizationofozone.rest.dto;

import java.io.Serializable;

public class NestedSourceDTO implements Serializable {

	private Long id;

	public NestedSourceDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}