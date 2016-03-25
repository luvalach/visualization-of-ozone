package cz.muni.fi.sdipr.visualizationofozone.rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import cz.muni.fi.sdipr.visualizationofozone.dao.FileDao;
import cz.muni.fi.sdipr.visualizationofozone.dao.MeasurementDao;
import cz.muni.fi.sdipr.visualizationofozone.dao.StationDao;
import cz.muni.fi.sdipr.visualizationofozone.impl.DownloadManager;

@Stateless
@Path("/datacontrols")
public class DataControl {

	@EJB
	private DownloadManager dm;

	@EJB
	private StationDao stationDao;

	@EJB
	private MeasurementDao measurementDao;

	@EJB
	private FileDao fileDao;

	@GET
	@Path("/update")
	public Response updateDB() {
		dm.refreshDatabase();
		return Response.noContent().build();
	}

	@GET
	@Path("/clean")
	public Response cleanDB() {
		measurementDao.deleteAll();
		fileDao.deleteAll();
		stationDao.deleteAll();
		return Response.noContent().build();
	}
}
