package cz.muni.fi.sdipr.visualizationofozone.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
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

import cz.muni.fi.sdipr.visualizationofozone.dao.FileUpdateDao;
import cz.muni.fi.sdipr.visualizationofozone.model.FileUpdate;
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.FileUpdateDTO;

@Stateless
@Path("/fileupdates")
public class FileUpdateEndpoint {
	@PersistenceContext(unitName = "visualization-of-ozone-persistence-unit")
	private EntityManager em;

	@EJB
	FileUpdateDao fileUpdateDao;

	@POST
	@Consumes("application/json")
	public Response create(FileUpdateDTO dto) {
		FileUpdate entity = dto.fromDTO(null, em);
		em.persist(entity);
		return Response.created(
				UriBuilder.fromResource(FileUpdateEndpoint.class).path(String.valueOf(entity.getSourceId())).build())
				.build();
	}

	@DELETE
	@Path("/{sourceId:[0-9][0-9]*}/{stationId:[0-9][0-9]*}")
	public Response deleteById(@PathParam("sourceId") Long sourceId, @PathParam("stationId") Long stationId) {
		if (!fileUpdateDao.deleteById(sourceId, stationId)) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.noContent().build();
	}

	@GET
	@Path("/{sourceId:[0-9][0-9]*}/{stationId:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("sourceId") Long sourceId, @PathParam("stationId") Long stationId) {
		FileUpdate entity;
		try {
			entity = fileUpdateDao.findById(sourceId, stationId);
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		FileUpdateDTO dto = new FileUpdateDTO(entity);
		return Response.ok(dto).build();
	}

	@GET
	@Produces("application/json")
	public List<FileUpdateDTO> listAll(@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		final List<FileUpdate> searchResults = fileUpdateDao.listAll(startPosition, maxResult);
		final List<FileUpdateDTO> results = new ArrayList<FileUpdateDTO>();
		for (FileUpdate searchResult : searchResults) {
			FileUpdateDTO dto = new FileUpdateDTO(searchResult);
			results.add(dto);
		}
		return results;
	}

	@PUT
	@Path("/{sourceId:[0-9][0-9]*}/{stationId:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("sourceId") Long sourceId, @PathParam("stationId") Long stationId,
			FileUpdateDTO dto) {
		if (dto == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (sourceId == null || stationId == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (!sourceId.equals(dto.getSourceId())) {
			return Response.status(Status.CONFLICT).entity(dto).build();
		}
		if (!stationId.equals(dto.getStationId())) {
			return Response.status(Status.CONFLICT).entity(dto).build();
		}
		FileUpdate entity = fileUpdateDao.findById(sourceId, stationId);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		entity = dto.fromDTO(entity, em);
		try {
			entity = fileUpdateDao.update(entity);
		} catch (OptimisticLockException e) {
			return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
		}
		return Response.noContent().build();
	}
}
