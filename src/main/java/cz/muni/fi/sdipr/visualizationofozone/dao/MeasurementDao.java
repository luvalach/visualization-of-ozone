package cz.muni.fi.sdipr.visualizationofozone.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import cz.muni.fi.sdipr.visualizationofozone.model.Measurement;
import cz.muni.fi.sdipr.visualizationofozone.model.MeasurementKey;

/**
 * DAO for Measurement
 */
@Stateless
public class MeasurementDao extends GenericDao<Measurement, MeasurementKey> {

	public MeasurementDao() {
		super(Measurement.class, MeasurementKey.class);
	}

	public boolean deleteById(Date dateTime, Long stationId, Long phenomenonTypeId) {
		return deleteById(new MeasurementKey(dateTime, stationId, phenomenonTypeId));
	}

	public Measurement findById(Date dateTime, Long phenomenonTypeId, Long stationId) {
		return findById(new MeasurementKey(dateTime, stationId, phenomenonTypeId));
	}

	public List<Measurement> findByStationPhenomenonDates(Long stationId, Long phenomenonTypeId, Date fromDate,
			Date toDate) {
		TypedQuery<Measurement> q = em.createNamedQuery("Measurement.findByStationPhenomenonDates", Measurement.class);
		q.setParameter("stationId", stationId);
		q.setParameter("phenomenonTypeId", phenomenonTypeId);
		q.setParameter("fromDate", fromDate);
		q.setParameter("toDate", toDate);
		return q.getResultList();
	}

	public List<Measurement> findByDates(Date fromDate, Date toDate) {
		TypedQuery<Measurement> q = em.createNamedQuery("Measurement.findByDates", Measurement.class);
		q.setParameter("fromDate", fromDate);
		q.setParameter("toDate", toDate);
		return q.getResultList();
	}
}
