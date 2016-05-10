package cz.muni.fi.sdipr.visualizationofozone.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.ejb.EJB;
import javax.transaction.Status;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.fi.sdipr.visualizationofozone.model.Station;

@RunWith(Arquillian.class)
public class StationDaoTest extends GenericDaoTest<Station, Long> {

	@EJB
	protected StationDao dao;

	public StationDaoTest() {
		super(Station.class, Long.class);
	}

	@Override
	protected GenericDao<Station, Long> getDao() {
		return dao;
	}

	@Override
	protected Station getEntity(int number) throws Exception {
		Station s = new Station();
		s.setName(Integer.toString(number));
		s.setCountry(Integer.toString(number));
		s.setLastUpdate(this.getDate());
		s.setLatitude(number);
		s.setLongitude(number);
		return s;
	}

	@Override
	public void callTestUpdate() throws Exception {
		testUpdate(v -> e1.setCountry(v), () -> e1.getCountry(), "New updated country.");
	}

	@Test
	public void testFindByNameAndCountry() throws Exception {
		try {
			utx.begin();
			em.persist(e1);
			em.persist(e2);
			utx.commit();

			Station entity = dao.findByNameAndCountry(e2.getName(), e2.getCountry());
			assertNotNull("Entity not found.", entity);
			assertEquals("Entities content not match.", e2, entity);

			entity = dao.findByNameAndCountry(e2.getName(), "foo");
			assertNull("Entity found but should not be.", entity);
		} finally {
			if (utx.getStatus() == Status.STATUS_ACTIVE) {
				utx.rollback();
			}
		}
	}
}
