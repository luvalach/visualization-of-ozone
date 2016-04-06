package cz.muni.fi.sdipr.visualizationofozone.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import cz.muni.fi.sdipr.visualizationofozone.dao.MeasurementDao;
import cz.muni.fi.sdipr.visualizationofozone.dao.StationDao;
import cz.muni.fi.sdipr.visualizationofozone.model.Measurement;
import cz.muni.fi.sdipr.visualizationofozone.rest.convertor.MeasurementConvertor;
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.DataPerPhenomenonsDTO;
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.DataPerStationDTO;

/**
 * 
 */
@Stateless
@Path("/dashboards")
public class DashboardEndpoint {

	@EJB
	private StationDao stationDao;

	@EJB
	private MeasurementDao measurementDao;

	private SimpleDateFormat simpleDateFormat;

	public DashboardEndpoint() {
		super();
		simpleDateFormat = new SimpleDateFormat("'\"'yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z\"'");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	@GET
	@Path("/")
	@Produces("application/json")
	public Response findDataForCharts(@QueryParam("stationIds") List<Long> stationIds,
			@QueryParam("phenomenonTypeIds") List<Long> phenomenonTypeIds, @QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate) {
		Date fromDate = null;
		Date toDate = null;

		try {
			fromDate = simpleDateFormat.parse(startDate);
			toDate = simpleDateFormat.parse(endDate);
		} catch (ParseException e) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		if (stationIds.size() == 0 || (stationIds.size() == 1 && stationIds.get(0) == 0)) {
			List<Long> allStationIds = stationDao.getAllIds();
			stationIds = allStationIds;
		}

		List<DataPerPhenomenonsDTO> dataPerPhenomenonsDTOs = new ArrayList<>();

		for (Long phenomenonTypeId : phenomenonTypeIds) {
			List<DataPerStationDTO> dataPerStationDTOs = new ArrayList<>();

			for (Long stationId : stationIds) {
				DataPerStationDTO dataPerStationDTO = new DataPerStationDTO(stationId);
				List<Measurement> measurements = measurementDao.findByStationPhenomenonDates(stationId,
						phenomenonTypeId, fromDate, toDate);
				dataPerStationDTO.setMeasurements(MeasurementConvertor.toChartReadableArray(measurements));
				dataPerStationDTOs.add(dataPerStationDTO);
			}

			DataPerPhenomenonsDTO dataPerPhenomenonsDTO = new DataPerPhenomenonsDTO(phenomenonTypeId,
					dataPerStationDTOs);
			dataPerPhenomenonsDTOs.add(dataPerPhenomenonsDTO);
		}

		return Response.ok(dataPerPhenomenonsDTOs).build();
	}
}
