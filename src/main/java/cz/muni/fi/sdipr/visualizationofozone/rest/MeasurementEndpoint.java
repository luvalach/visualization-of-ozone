package cz.muni.fi.sdipr.visualizationofozone.rest;

import java.util.ArrayList;
import java.util.Date;
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

import cz.muni.fi.sdipr.visualizationofozone.dao.MeasurementDao;
import cz.muni.fi.sdipr.visualizationofozone.model.Measurement;
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.MeasurementDTO;

/**
 * 
 */
@Stateless
@Path("/measurements")
public class MeasurementEndpoint {
	@PersistenceContext(unitName = "visualization-of-ozone-persistence-unit")
	private EntityManager em;

	@EJB
	MeasurementDao measurementDao;

	@POST
	@Path("/{stationId:[0-9][0-9]*}/{phenomenonTypeId:[0-9][0-9]*}/{dateTime:.*}")
	@Consumes("application/json")
	public Response create(MeasurementDTO dto) {
		Measurement entity = dto.fromDTO(null, em);
		em.persist(entity);
		return Response.created(
				UriBuilder.fromResource(MeasurementEndpoint.class).path(String.valueOf(entity.getDateTime())).build())
				.build();
	}

	@DELETE
	@Path("/{stationId:[0-9][0-9]*}/{phenomenonTypeId:[0-9][0-9]*}/{dateTime:.*}")
	public Response deleteById(@PathParam("dateTime") Date dateTime,
			@PathParam("phenomenonTypeId") Long phenomenonTypeId, @PathParam("stationId") Long stationId) {
		if (!measurementDao.deleteById(dateTime, phenomenonTypeId, stationId)) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.noContent().build();
	}

	@GET
	@Path("/{stationId:[0-9][0-9]*}/{phenomenonTypeId:[0-9][0-9]*}/{dateTime:.*}")
	@Produces("application/json")
	public Response findById(@PathParam("dateTime") Date dateTime, @PathParam("phenomenonTypeId") Long phenomenonTypeId,
			@PathParam("stationId") Long stationId) {
		Measurement entity;
		try {
			entity = measurementDao.findById(dateTime, phenomenonTypeId, stationId);
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		MeasurementDTO dto = new MeasurementDTO(entity);
		return Response.ok(dto).build();
	}

	@GET
	@Produces("application/json")
	public List<MeasurementDTO> listAll(@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		final List<Measurement> searchResults = measurementDao.listAll(startPosition, maxResult);
		final List<MeasurementDTO> results = new ArrayList<MeasurementDTO>();
		for (Measurement searchResult : searchResults) {
			MeasurementDTO dto = new MeasurementDTO(searchResult);
			results.add(dto);
		}
		return results;
	}

	@PUT
	@Path("/{stationId:[0-9][0-9]*}/{phenomenonTypeId:[0-9][0-9]*}/{dateTime:.*}")
	@Consumes("application/json")
	public Response update(@PathParam("dateTime") Date dateTime, @PathParam("phenomenonTypeId") Long phenomenonTypeId,
			@PathParam("stationId") Long stationId, MeasurementDTO dto) {
		if (dto == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (stationId == null || phenomenonTypeId == null || dateTime == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		Measurement entity = measurementDao.findById(dateTime, phenomenonTypeId, stationId);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		entity = dto.fromDTO(entity, em);
		try {
			// TODO: Myslim, ze se tady 2x dela merge
			entity = measurementDao.update(entity);
		} catch (OptimisticLockException e) {
			return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
		}
		return Response.noContent().build();
	}
}
