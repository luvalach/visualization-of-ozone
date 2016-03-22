package cz.muni.fi.sdipr.visualizationofozone.rest.dto;

import java.io.Serializable;
import cz.muni.fi.sdipr.visualizationofozone.model.PhenomenonType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
public class NestedPhenomenonTypeDTO implements Serializable {

	private Long id;
	private String name;
	private String nameShortcut;
	private int columnNo;
	private String unit;
	private String unitShortcut;
	private String description;

	public NestedPhenomenonTypeDTO() {
	}

	public NestedPhenomenonTypeDTO(final PhenomenonType entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.name = entity.getName();
			this.nameShortcut = entity.getNameShortcut();
			this.columnNo = entity.getColumnNo();
			this.unit = entity.getUnit();
			this.unitShortcut = entity.getUnitShortcut();
			this.description = entity.getDescription();
		}
	}

	public PhenomenonType fromDTO(PhenomenonType entity, EntityManager em) {
		if (entity == null) {
			entity = new PhenomenonType();
		}
		if (this.id != null) {
			TypedQuery<PhenomenonType> findByIdQuery = em
					.createQuery(
							"SELECT DISTINCT m FROM PhenomenonType m WHERE m.id = :entityId",
							PhenomenonType.class);
			findByIdQuery.setParameter("entityId", this.id);
			try {
				entity = findByIdQuery.getSingleResult();
			} catch (javax.persistence.NoResultException nre) {
				entity = null;
			}
			return entity;
		}
		entity.setName(this.name);
		entity.setNameShortcut(this.nameShortcut);
		entity.setColumnNo(this.columnNo);
		entity.setUnit(this.unit);
		entity.setUnitShortcut(this.unitShortcut);
		entity.setDescription(this.description);
		entity = em.merge(entity);
		return entity;
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