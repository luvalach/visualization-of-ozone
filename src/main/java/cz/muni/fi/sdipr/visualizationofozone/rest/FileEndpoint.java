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
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.FileDTO;
import cz.muni.fi.sdipr.visualizationofozone.model.File;

/**
 * 
 */
@Stateless
@Path("/files")
public class FileEndpoint {
	@PersistenceContext(unitName = "visualization-of-ozone-persistence-unit")
	private EntityManager em;

	@POST
	@Consumes("application/json")
	public Response create(FileDTO dto) {
		File entity = dto.fromDTO(null, em);
		em.persist(entity);
		return Response.created(
				UriBuilder.fromResource(FileEndpoint.class)
						.path(String.valueOf(entity.getFileName())).build())
				.build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") String id) {
		File entity = em.find(File.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		em.remove(entity);
		return Response.noContent().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") String id) {
		TypedQuery<File> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT f FROM File f WHERE f.fileName = :entityId ORDER BY f.fileName",
						File.class);
		findByIdQuery.setParameter("entityId", id);
		File entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		FileDTO dto = new FileDTO(entity);
		return Response.ok(dto).build();
	}

	@GET
	@Produces("application/json")
	public List<FileDTO> listAll(@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		TypedQuery<File> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT f FROM File f ORDER BY f.fileName",
						File.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<File> searchResults = findAllQuery.getResultList();
		final List<FileDTO> results = new ArrayList<FileDTO>();
		for (File searchResult : searchResults) {
			FileDTO dto = new FileDTO(searchResult);
			results.add(dto);
		}
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") String id, FileDTO dto) {
		if (dto == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (id == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (!id.equals(dto.getFileName())) {
			return Response.status(Status.CONFLICT).entity(dto).build();
		}
		File entity = em.find(File.class, id);
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
