package cz.muni.fi.sdipr.visualizationofozone.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cz.muni.fi.sdipr.visualizationofozone.model.Measurement;
import cz.muni.fi.sdipr.visualizationofozone.model.MeasurementKey;

/**
 * DAO for Measure
 */
@Stateless
public class MeasurementDao {
	@PersistenceContext(unitName = "visualization-of-ozone-persistence-unit")
	private EntityManager em;

	public void create(Measurement entity) {
		em.persist(entity);
	}

	public boolean deleteById(MeasurementKey id) {
		Measurement entity = em.find(Measurement.class, id);
		if (entity != null) {
			em.remove(entity);
			return true;
		}
		return false;
	}

	public boolean deleteById(Date dateTime, Long measureId, Long stationId) {
		Measurement entity = findById(dateTime, measureId, stationId);
		if (entity != null) {
			em.remove(entity);
			return true;
		}
		return false;
	}

	public Measurement findById(MeasurementKey id) {
		return em.find(Measurement.class, id);
	}

	public Measurement findById(Date dateTime, Long phenomenonTypeId, Long stationId) {
		TypedQuery<Measurement> q = em.createNamedQuery("Measurement.getByIdClass", Measurement.class);
		q.setParameter("dateTime", dateTime);
		q.setParameter("phenomenonTypeId", phenomenonTypeId);
		q.setParameter("stationId", stationId);
		return q.getSingleResult();
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

	public Measurement update(Measurement entity) {
		return em.merge(entity);
	}

	public List<Measurement> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Measurement> q = em.createNamedQuery("Measurement.listAll", Measurement.class);
		if (startPosition != null) {
			q.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			q.setMaxResults(maxResult);
		}
		return q.getResultList();
	}

	public void deleteAll() {
		Query q = em.createNamedQuery("Measurement.deleteAll");
		q.executeUpdate();
	}
}
