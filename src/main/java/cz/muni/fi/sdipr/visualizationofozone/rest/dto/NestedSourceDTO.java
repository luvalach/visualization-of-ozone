package cz.muni.fi.sdipr.visualizationofozone.rest.dto;

import java.io.Serializable;
import cz.muni.fi.sdipr.visualizationofozone.model.Source;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
public class NestedSourceDTO implements Serializable {

	private Long id;
	private String url;
	private String description;

	public NestedSourceDTO() {
	}

	public NestedSourceDTO(final Source entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.url = entity.getUrl();
			this.description = entity.getDescription();
		}
	}

	public Source fromDTO(Source entity, EntityManager em) {
		if (entity == null) {
			entity = new Source();
		}
		if (this.id != null) {
			TypedQuery<Source> findByIdQuery = em.createQuery(
					"SELECT DISTINCT s FROM Source s WHERE s.id = :entityId",
					Source.class);
			findByIdQuery.setParameter("entityId", this.id);
			try {
				entity = findByIdQuery.getSingleResult();
			} catch (javax.persistence.NoResultException nre) {
				entity = null;
			}
			return entity;
		}
		entity.setUrl(this.url);
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

	public String getUrl() {
		return this.url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}
}