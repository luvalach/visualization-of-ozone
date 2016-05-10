package cz.muni.fi.sdipr.visualizationofozone.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.fi.sdipr.visualizationofozone.model.Station;

@RunWith(Arquillian.class)
public class StationDaoNormalTest {

	public static final String CONFIG_DATE_FORMAT = "dd.MM.yyyy";
	public static final String STATION_LASTUPDATE = "1.1.2010";

	@PersistenceContext(unitName = "visualization-of-ozone-persistence-unit")
	private EntityManager em;

	@EJB
	private StationDao dao;

	@Resource
	UserTransaction utx;

	private TypedQuery<Station> findAllQuery;
	private Query deleteAllQuery;

	private Station s1;
	private Station s2;
	private Date date;

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "ozoneTest.war").addPackages(true, "org.apache.commons.lang3")
				.addPackages(true, "org.apache.commons.beanutils")
				.addPackages(true, "cz.muni.fi.sdipr.visualizationofozone.model")
				.addPackages(true, "cz.muni.fi.sdipr.visualizationofozone.dao")
				.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Before
	public void setUp() throws Exception {
		this.findAllQuery = em.createQuery("SELECT DISTINCT s FROM Station s", Station.class);
		this.deleteAllQuery = em.createQuery("DELETE FROM Station s");

		this.date = this.getDate();
		this.s1 = this.getStation(1);
		this.s2 = this.getStation(2);

		try {
			utx.begin();
			deleteAllQuery.executeUpdate();
			utx.commit();
		} finally {
			// Rollback transaction in case it is open due to some error
			if (utx.getStatus() == Status.STATUS_ACTIVE) {
				utx.rollback();
			}
		}
	}

	@Test
	@InSequence(1)
	public void testCreate() throws Exception {
		try {
			utx.begin();
			dao.create(s1);
			dao.create(s2);
			utx.commit();

			List<Station> results = findAllQuery.getResultList();

			assertEquals("Database should contains two objects.", 2, results.size());

		} finally {
			// Rollback transaction in case it is open due to some error
			if (utx.getStatus() == Status.STATUS_ACTIVE) {
				utx.rollback();
			}
		}
	}

	@Test
	public void testDeleteById() throws Exception {
		try {
			utx.begin();
			em.persist(s1);
			em.persist(s2);
			utx.commit();

			dao.deleteById(s1.getId());

			List<Station> results = findAllQuery.getResultList();

			assertEquals("Database should contains only one object.", 1, results.size());

		} finally {
			// Rollback transaction in case it is open due to some error
			if (utx.getStatus() == Status.STATUS_ACTIVE) {
				utx.rollback();
			}
		}
	}

	@Test
	public void testFindById() throws Exception {
		try {
			utx.begin();
			em.persist(s1);
			em.persist(s2);
			utx.commit();

			Station entity = dao.findById(s1.getId());
			assertNotNull("Entity not found.", entity);
			assertEquals("Entities IDs not match.", this.s1.getId(), entity.getId());
			assertEquals("Entities content not match.", this.s1, entity);
		} finally {
			// Rollback transaction in case it is open due to some error
			if (utx.getStatus() == Status.STATUS_ACTIVE) {
				utx.rollback();
			}
		}
	}

	@Test
	public void testFindByNameAndCountry() throws Exception {
		try {
			utx.begin();
			em.persist(s1);
			em.persist(s2);
			utx.commit();

			Station entity = dao.findByNameAndCountry(s2.getName(), s2.getCountry());
			assertNotNull("Entity not found.", entity);
			assertEquals("Entities content not match.", s2, entity);

			entity = dao.findByNameAndCountry(s2.getName(), "foo");
			assertNull("Entity found but should not be.", entity);
		} finally {
			// Rollback transaction in case it is open due to some error
			if (utx.getStatus() == Status.STATUS_ACTIVE) {
				utx.rollback();
			}
		}
	}

	@Test
	public void testUpdate() throws Exception {
		try {
			dao.create(s1);

			s1.setLongitude(20.1F);

			dao.update(s1);

			Station entity = dao.findById(s1.getId());

			assertEquals("Entities IDs not match.", 20.1F, entity.getLongitude(), 1);
		} finally {
			// Rollback transaction in case it is open due to some error
			if (utx.getStatus() == Status.STATUS_ACTIVE) {
				utx.rollback();
			}
		}
	}

	@Test
	public void testListAll() throws Exception {
		try {
			Station s3 = getStation(3);
			Station s4 = getStation(4);

			utx.begin();
			em.persist(s1);
			em.persist(s2);
			em.persist(s3);
			em.persist(s4);
			utx.commit();

			List<Station> results = dao.listAll(null, null);
			assertEquals("DAO should return 4 entities.", 4, results.size());

			results = dao.listAll(0, 1);
			assertEquals("DAO should return single entity.", 1, results.size());
			assertEquals("Entity name doesn't match.", s1, results.get(0));

			results = dao.listAll(3, 10);
			assertEquals("DAO should return single entity.", 1, results.size());
			assertEquals("Entity name doesn't match.", s4, results.get(0));
		} finally {
			// Rollback transaction in case it is open due to some error
			if (utx.getStatus() == Status.STATUS_ACTIVE) {
				utx.rollback();
			}
		}
	}

	@Test
	public void testGetAllIds() throws Exception {
		try {
			utx.begin();
			dao.create(s1);
			dao.create(s2);
			utx.commit();

			List<Long> results = dao.getAllIds();

			assertEquals("DAO should return two IDs.", 2, results.size());
			assertEquals("First ID should be 1", s1.getId(), results.get(0));
			assertEquals("First ID should be 1", s2.getId(), results.get(1));

		} finally {
			// Rollback transaction in case it is open due to some error
			if (utx.getStatus() == Status.STATUS_ACTIVE) {
				utx.rollback();
			}
		}
	}

	@Test
	public void testDeleteAll() throws Exception {
		try {
			utx.begin();
			em.persist(s1);
			em.persist(s2);
			utx.commit();

			dao.deleteAll();

			List<Station> results = findAllQuery.getResultList();

			assertEquals("Database should be empty.", 0, results.size());

		} finally {
			// Rollback transaction in case it is open due to some error
			if (utx.getStatus() == Status.STATUS_ACTIVE) {
				utx.rollback();
			}
		}
	}

	private Station getStation(int number) throws ParseException {
		Station s = new Station();
		s.setName(Integer.toString(number));
		s.setCountry(Integer.toString(number));
		s.setLastUpdate(this.getDate());
		s.setLatitude(number);
		s.setLongitude(number);
		return s;
	}

	private Date getDate() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(CONFIG_DATE_FORMAT);
		Date date = sdf.parse(STATION_LASTUPDATE);
		return date;
	}

}
