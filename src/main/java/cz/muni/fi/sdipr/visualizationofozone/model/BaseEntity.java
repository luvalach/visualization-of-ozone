package cz.muni.fi.sdipr.visualizationofozone.model;

import java.io.Serializable;

public interface BaseEntity<PK extends Serializable> extends Serializable {
	public PK getId();

	public void setId(PK id);
}
