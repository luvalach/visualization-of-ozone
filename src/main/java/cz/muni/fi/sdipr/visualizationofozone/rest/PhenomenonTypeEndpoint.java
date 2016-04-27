package cz.muni.fi.sdipr.visualizationofozone.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import cz.muni.fi.sdipr.visualizationofozone.dao.PhenomenonTypeDao;
import cz.muni.fi.sdipr.visualizationofozone.model.PhenomenonType;
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.PhenomenonTypeDTO;

/**
 * 
 */
@Stateless
@Path("/phenomenontypes")
public class PhenomenonTypeEndpoint {

	@EJB
	private PhenomenonTypeDao dao;

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {
		PhenomenonType entity = dao.findById(id);

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
		final List<PhenomenonType> results = dao.listAll(startPosition, maxResult);
		final List<PhenomenonTypeDTO> resultsDtos = new ArrayList<PhenomenonTypeDTO>();

		for (PhenomenonType searchResult : results) {
			PhenomenonTypeDTO dto = new PhenomenonTypeDTO(searchResult);
			resultsDtos.add(dto);
		}

		return resultsDtos;
	}
}
