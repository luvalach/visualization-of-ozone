package cz.muni.fi.sdipr.visualizationofozone.rest.dto;

import java.io.Serializable;
import cz.muni.fi.sdipr.visualizationofozone.model.Source;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.ArrayList;
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.NestedPhenomenonTypeDTO;
import cz.muni.fi.sdipr.visualizationofozone.model.PhenomenonType;
import java.util.Iterator;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class SourceDTO implements Serializable {

	private Long id;
	private String url;
	private String description;
	private List<NestedPhenomenonTypeDTO> phenomenonTypes = new ArrayList<NestedPhenomenonTypeDTO>();

	public SourceDTO() {
	}

	public SourceDTO(final Source entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.url = entity.getUrl();
			this.description = entity.getDescription();
			Iterator<PhenomenonType> iterPhenomenonType = entity.getPhenomenonType()
					.iterator();
			while (iterPhenomenonType.hasNext()) {
				PhenomenonType element = iterPhenomenonType.next();
				this.phenomenonTypes.add(new NestedPhenomenonTypeDTO(element));
			}
		}
	}

	public Source fromDTO(Source entity, EntityManager em) {
		if (entity == null) {
			entity = new Source();
		}
		entity.setUrl(this.url);
		entity.setDescription(this.description);
		Iterator<PhenomenonType> iterPhenomenonType = entity.getPhenomenonType()
				.iterator();
		while (iterPhenomenonType.hasNext()) {
			boolean found = false;
			PhenomenonType phenomenonType = iterPhenomenonType.next();
			Iterator<NestedPhenomenonTypeDTO> iterDtoPhenomenonType = this
					.getPhenomenonType().iterator();
			while (iterDtoPhenomenonType.hasNext()) {
				NestedPhenomenonTypeDTO dtoPhenomenonType = iterDtoPhenomenonType.next();
				if (dtoPhenomenonType.getId().equals(phenomenonType.getId())) {
					found = true;
					break;
				}
			}
			if (found == false) {
				iterPhenomenonType.remove();
			}
		}
		Iterator<NestedPhenomenonTypeDTO> iterDtoPhenomenonType = this
				.getPhenomenonType().iterator();
		while (iterDtoPhenomenonType.hasNext()) {
			boolean found = false;
			NestedPhenomenonTypeDTO dtoPhenomenonType = iterDtoPhenomenonType.next();
			iterPhenomenonType = entity.getPhenomenonType().iterator();
			while (iterPhenomenonType.hasNext()) {
				PhenomenonType phenomenonType = iterPhenomenonType.next();
				if (dtoPhenomenonType.getId().equals(phenomenonType.getId())) {
					found = true;
					break;
				}
			}
			if (found == false) {
				Iterator<PhenomenonType> resultIter = em
						.createQuery("SELECT DISTINCT m FROM PhenomenonType m",
								PhenomenonType.class).getResultList().iterator();
				while (resultIter.hasNext()) {
					PhenomenonType result = resultIter.next();
					if (result.getId().equals(dtoPhenomenonType.getId())) {
						entity.getPhenomenonType().add(result);
						break;
					}
				}
			}
		}
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

	public List<NestedPhenomenonTypeDTO> getPhenomenonType() {
		return this.phenomenonTypes;
	}

	public void setPhenomenonType(final List<NestedPhenomenonTypeDTO> phenomenonType) {
		this.phenomenonTypes = phenomenonType;
	}
}