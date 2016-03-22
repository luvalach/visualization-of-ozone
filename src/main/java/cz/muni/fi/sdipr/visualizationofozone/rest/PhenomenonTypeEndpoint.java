package cz.muni.fi.sdipr.visualizationofozone.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import cz.muni.fi.sdipr.visualizationofozone.model.PhenomenonType;
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.PhenomenonTypeDTO;

/**
 * 
 */
@Stateless
@Path("/phenomenontypes")
public class PhenomenonTypeEndpoint {
	@PersistenceContext(unitName = "visualization-of-ozone-persistence-unit")
	private EntityManager em;

	@POST
	@Consumes("application/json")
	public Response create(PhenomenonTypeDTO dto) {
		PhenomenonType entity = dto.fromDTO(null, em);
		em.persist(entity);
		return Response.created(
				UriBuilder.fromResource(PhenomenonTypeEndpoint.class).path(String.valueOf(entity.getId())).build())
				.build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		PhenomenonType entity = em.find(PhenomenonType.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		em.remove(entity);
		return Response.noContent().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {
		TypedQuery<PhenomenonType> findByIdQuery = em.createQuery(
				"SELECT DISTINCT m FROM PhenomenonType m LEFT JOIN FETCH m.source WHERE m.id = :entityId ORDER BY m.id",
				PhenomenonType.class);
		findByIdQuery.setParameter("entityId", id);
		PhenomenonType entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		PhenomenonTypeDTO dto = new PhenomenonTypeDTO(entity);
		return Response.ok(dto).build();
	}

	@GET
	@Produces("application/json")
	public List<PhenomenonTypeDTO> listAll(@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		TypedQuery<PhenomenonType> findAllQuery = em.createQuery(
				"SELECT DISTINCT m FROM PhenomenonType m LEFT JOIN FETCH m.source ORDER BY m.id", PhenomenonType.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<PhenomenonType> searchResults = findAllQuery.getResultList();
		final List<PhenomenonTypeDTO> results = new ArrayList<PhenomenonTypeDTO>();
		for (PhenomenonType searchResult : searchResults) {
			PhenomenonTypeDTO dto = new PhenomenonTypeDTO(searchResult);
			results.add(dto);
		}
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, PhenomenonTypeDTO dto) {
		if (dto == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (id == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (!id.equals(dto.getId())) {
			return Response.status(Status.CONFLICT).entity(dto).build();
		}
		PhenomenonType entity = em.find(PhenomenonType.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		entity = dto.fromDTO(entity, em);
		try {
			entity = em.merge(entity);
		} catch (OptimisticLockException e) {
			return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
		}
		return Response.noContent().build();
	}
}
