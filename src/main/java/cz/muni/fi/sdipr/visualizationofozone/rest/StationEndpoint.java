package cz.muni.fi.sdipr.visualizationofozone.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import cz.muni.fi.sdipr.visualizationofozone.converter.StationConverter;
import cz.muni.fi.sdipr.visualizationofozone.dao.StationDao;
import cz.muni.fi.sdipr.visualizationofozone.model.Station;
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.StationDTO;

/**
 * 
 */
@Stateless
@Path("/stations")
public class StationEndpoint {
	@PersistenceContext(unitName = "visualization-of-ozone-persistence-unit")
	private EntityManager em;

	@EJB
	private StationDao dao;

	@EJB
	private StationConverter converter;

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {
		Station entity = dao.findById(id);

		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		StationDTO dto = converter.entityToDto(entity);
		return Response.ok(dto).build();
	}

	@GET
	@Produces("application/json")
	public List<StationDTO> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
		final List<Station> results = dao.listAll(startPosition, maxResult);
		final List<StationDTO> resultsDaos = converter.entitiesToDtos(results);
		return resultsDaos;
	}
}
