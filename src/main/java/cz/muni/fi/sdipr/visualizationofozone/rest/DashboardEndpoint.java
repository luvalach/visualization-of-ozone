package cz.muni.fi.sdipr.visualizationofozone.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

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
import cz.muni.fi.sdipr.visualizationofozone.downloading.impl.MeasurementConvertor;
import cz.muni.fi.sdipr.visualizationofozone.model.Measurement;

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

		Map<Long, Map<Long, List<Number[]>>> phenomenonsAndStations = new TreeMap<Long, Map<Long, List<Number[]>>>();

		for (Long phenomenonTypeId : phenomenonTypeIds) {
			Map<Long, List<Number[]>> stationsAndMeasurements = new TreeMap<Long, List<Number[]>>();

			for (Long stationId : stationIds) {
				List<Measurement> measurements = measurementDao.findByStationPhenomenonDates(stationId,
						phenomenonTypeId, fromDate, toDate);
				stationsAndMeasurements.put(stationId, MeasurementConvertor.toChartReadableArray(measurements));
			}

			phenomenonsAndStations.put(phenomenonTypeId, stationsAndMeasurements);
		}

		return Response.ok(phenomenonsAndStations).build();
	}
}
