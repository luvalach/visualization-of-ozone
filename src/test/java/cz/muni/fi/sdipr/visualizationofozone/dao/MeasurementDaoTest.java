package cz.muni.fi.sdipr.visualizationofozone.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.transaction.Status;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.fi.sdipr.visualizationofozone.model.Measurement;
import cz.muni.fi.sdipr.visualizationofozone.model.MeasurementKey;

@RunWith(Arquillian.class)
public class MeasurementDaoTest extends GenericDaoTest<Measurement, MeasurementKey> {

	@EJB
	protected MeasurementDao dao;

	public MeasurementDaoTest() {
		super(Measurement.class, MeasurementKey.class);
	}

	@Override
	protected GenericDao<Measurement, MeasurementKey> getDao() {
		return dao;
	}

	@Override
	protected Measurement getEntity(int number) throws Exception {
		Measurement m = new Measurement();
		m.setStationId(number);
		m.setPhenomenonTypeId(number);
		m.setDateTime(this.getDate());
		m.setValue(number);
		return m;
	}

	@Override
	public void callTestUpdate() throws Exception {
		testUpdate(v -> e1.setValue(v), () -> e1.getValue(), 99.9F);
	}

	@Test
	public void testFindByStationPhenomenonDates() throws Exception {
		try {
			Date searchFromDate = getDate("1.2.2015");
			Date searchToDate = getDate("4.2.2015");
			Date wantedDate1 = getDate("2.2.2015");
			Date wantedDate2 = getDate("3.2.2015");

			Measurement m1 = getEntity(1);

			Measurement m2 = getEntity(1);
			m2.setStationId(2);

			Measurement m3 = getEntity(1);
			m3.setStationId(2);
			m3.setDateTime(wantedDate1);

			Measurement m4 = getEntity(1);
			m4.setDateTime(wantedDate1);

			Measurement m5 = getEntity(1);
			m5.setDateTime(wantedDate2);

			Measurement m6 = getEntity(1);
			m6.setPhenomenonTypeId(2);
			m6.setDateTime(wantedDate1);

			utx.begin();
			em.persist(m1);
			em.persist(m2);
			em.persist(m3);
			em.persist(m4);
			em.persist(m5);
			em.persist(m6);
			utx.commit();

			List<Measurement> results = dao.findByStationPhenomenonDates(m4.getStationId(), m4.getPhenomenonTypeId(),
					searchFromDate, searchToDate);

			assertEquals("Bad number of results.", 2, results.size());
			assertEquals("First result doesn't match.", m4, results.get(0));
			assertEquals("Second result doesn't match.", m5, results.get(1));
		} finally {
			if (utx.getStatus() == Status.STATUS_ACTIVE) {
				utx.rollback();
			}
		}
	}
}
