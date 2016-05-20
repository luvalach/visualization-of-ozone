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

import cz.muni.fi.sdipr.visualizationofozone.converter.MeasurementConvertor;
import cz.muni.fi.sdipr.visualizationofozone.dao.MeasurementDao;
import cz.muni.fi.sdipr.visualizationofozone.dao.StationDao;
import cz.muni.fi.sdipr.visualizationofozone.model.Measurement;
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.DataPerPhenomenonsDTO;
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.DataPerStationDTO;
import cz.muni.fi.sdipr.visualizationofozone.rest.dto.ValidationErrorDto;

/**
 * 
 */
@Stateless
@Path("/dashboards")
public class DashboardEndpoint {

	public static final int MAX_STATATIONS = 5;
	public static final int MAX_DAYS = 366;
	public static final int MAX_DAYS_FOR_ALL_STATIONS = 3;
	public static final int MAX_PHENOMENONS = 5;

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

		ValidationErrorDto validationErrorDTOs = validateInput(stationIds, phenomenonTypeIds, fromDate, toDate);

		if (validationErrorDTOs.getFieldErrors().size() > 0) {
			return Response.status(Status.BAD_REQUEST).entity(validationErrorDTOs).build();
		}

		if (optionsAllStationsIsSelected(stationIds)) {
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

	private boolean optionsAllStationsIsSelected(List<Long> stationIds) {
		return stationIds.size() == 0 || (stationIds.size() == 1 && stationIds.get(0) == 0);
	}

	private int daysBetween(Date d1, Date d2) {
		return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}

	private ValidationErrorDto validateInput(List<Long> stationIds, List<Long> phenomenonTypeIds, Date fromDate,
			Date toDate) {
		ValidationErrorDto validationErrorDTOs = new ValidationErrorDto();

		if (stationIds.size() > MAX_STATATIONS) {
			validationErrorDTOs.addFieldError("Stations",
					"Maximum number of stations exceeded. Maximum is " + MAX_STATATIONS + ".");
		}

		if (optionsAllStationsIsSelected(stationIds)) {
			if (daysBetween(fromDate, toDate) > MAX_DAYS_FOR_ALL_STATIONS) {
				validationErrorDTOs.addFieldError("Time Span", "You can select at most " + MAX_DAYS_FOR_ALL_STATIONS
						+ " days when option 'All stations' is selected.");
			}
		} else {
			if (daysBetween(fromDate, toDate) > MAX_DAYS) {
				validationErrorDTOs.addFieldError("Time Span",
						"Maximum number of days exceeded. Maximum is " + MAX_DAYS + ".");
			}
		}

		if (phenomenonTypeIds.size() > MAX_PHENOMENONS) {
			validationErrorDTOs.addFieldError("Phenomenons",
					"Maximum number of phenomenons exceeded. Maximum is " + MAX_PHENOMENONS + ".");
		}

		return validationErrorDTOs;
	}
}
