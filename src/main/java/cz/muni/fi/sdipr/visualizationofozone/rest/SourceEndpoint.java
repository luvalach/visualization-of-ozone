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
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.SourceDTO;
import cz.muni.fi.sdipr.visualizationofozone.model.Source;

/**
 * 
 */
@Stateless
@Path("/sources")
public class SourceEndpoint {
	@PersistenceContext(unitName = "visualization-of-ozone-persistence-unit")
	private EntityManager em;

	@POST
	@Consumes("application/json")
	public Response create(SourceDTO dto) {
		Source entity = dto.fromDTO(null, em);
		em.persist(entity);
		return Response.created(
				UriBuilder.fromResource(SourceEndpoint.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		Source entity = em.find(Source.class, id);
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
		TypedQuery<Source> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT s FROM Source s LEFT JOIN FETCH s.phenomenonType WHERE s.id = :entityId ORDER BY s.id",
						Source.class);
		findByIdQuery.setParameter("entityId", id);
		Source entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		SourceDTO dto = new SourceDTO(entity);
		return Response.ok(dto).build();
	}

	@GET
	@Produces("application/json")
	public List<SourceDTO> listAll(@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		TypedQuery<Source> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT s FROM Source s LEFT JOIN FETCH s.phenomenonType ORDER BY s.id",
						Source.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<Source> searchResults = findAllQuery.getResultList();
		final List<SourceDTO> results = new ArrayList<SourceDTO>();
		for (Source searchResult : searchResults) {
			SourceDTO dto = new SourceDTO(searchResult);
			results.add(dto);
		}
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, SourceDTO dto) {
		if (dto == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (id == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (!id.equals(dto.getId())) {
			return Response.status(Status.CONFLICT).entity(dto).build();
		}
		Source entity = em.find(Source.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		entity = dto.fromDTO(entity, em);
		try {
			entity = em.merge(entity);
		} catch (OptimisticLockException e) {
			return Response.status(Status.CONFLICT).entity(e.getEntity())
					.build();
		}
		return Response.noContent().build();
	}
}
