package cz.muni.fi.sdipr.visualizationofozone.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Resource;
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

import cz.muni.fi.sdipr.visualizationofozone.model.BaseEntity;

@RunWith(Arquillian.class)
public abstract class GenericDaoTest<E extends BaseEntity, PK extends Serializable> {

	public static final String CONFIG_DATE_FORMAT = "dd.MM.yyyy";
	public static final String STATION_LASTUPDATE = "1.1.2010";

	protected Class<E> entityClass;
	protected Class<PK> primaryKeyClass;

	@PersistenceContext(unitName = "visualization-of-ozone-persistence-unit")
	protected EntityManager em;

	@Resource
	protected UserTransaction utx;

	protected TypedQuery<E> findAllQuery;
	protected Query deleteAllQuery;

	protected E e1;
	protected E e2;
	protected Date date;

	public GenericDaoTest(Class<E> entityClass, Class<PK> primaryKeyClass) {
		super();
		this.entityClass = entityClass;
		this.primaryKeyClass = primaryKeyClass;
	}

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
		StringBuilder findAllQueryString = new StringBuilder("SELECT e FROM ");
		findAllQueryString.append(entityClass.getSimpleName()).append(" e");
		this.findAllQuery = em.createQuery(findAllQueryString.toString(), entityClass);

		StringBuilder deleteAllQueryString = new StringBuilder("DELETE FROM ");
		deleteAllQueryString.append(entityClass.getSimpleName()).append(" e");
		this.deleteAllQuery = em.createQuery(deleteAllQueryString.toString());

		this.date = this.getDate();
		this.e1 = this.getEntity(1);
		this.e2 = this.getEntity(2);

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
			getDao().create(e1);
			getDao().create(e2);
			utx.commit();

			List<E> results = findAllQuery.getResultList();

			assertEquals("Database should contains two entities.", 2, results.size());

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
			em.persist(e1);
			em.persist(e2);
			utx.commit();

			getDao().deleteById((PK) e1.getId());

			List<E> results = findAllQuery.getResultList();

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
			em.persist(e1);
			em.persist(e2);
			utx.commit();

			E entity = getDao().findById((PK) e1.getId());
			assertNotNull("Entity not found.", entity);
			assertEquals("Entities IDs not match.", this.e1.getId(), entity.getId());
			assertEquals("Entities content not match.", this.e1, entity);
		} finally {
			// Rollback transaction in case it is open due to some error
			if (utx.getStatus() == Status.STATUS_ACTIVE) {
				utx.rollback();
			}
		}
	}

	@Test
	public abstract <V> void callTestUpdate(Consumer<V> setterMethod, java.util.function.Supplier<V> getterMethod,
			V newValue) throws Exception;

	public <V> void testUpdate(Consumer<V> setterMethod, java.util.function.Supplier<V> getterMethod, V newValue)
			throws Exception {
		try {
			getDao().create(e1);

			setterMethod.accept(newValue);

			getDao().update(e1);

			E entity = getDao().findById((PK) e1.getId());

			assertEquals("Entities IDs not match.", newValue, getterMethod.get());
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
			E e3 = getEntity(3);
			E e4 = getEntity(4);

			utx.begin();
			em.persist(e1);
			em.persist(e2);
			em.persist(e3);
			em.persist(e4);
			utx.commit();

			List<E> results = getDao().listAll(null, null);
			assertEquals("getDao() should return 4 entities.", 4, results.size());

			results = getDao().listAll(0, 1);
			assertEquals("DAO should return single entity.", 1, results.size());
			assertEquals("Entity name doesn't match.", e1, results.get(0));

			results = getDao().listAll(3, 10);
			assertEquals("DAO should return single entity.", 1, results.size());
			assertEquals("Entity name doesn't match.", e4, results.get(0));
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
			getDao().create(e1);
			getDao().create(e2);
			utx.commit();

			List<PK> results = getDao().getAllIds();

			assertEquals("DAO should return two IDs.", 2, results.size());
			assertEquals("First ID should be 1", e1.getId(), results.get(0));
			assertEquals("First ID should be 1", e2.getId(), results.get(1));

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
			em.persist(e1);
			em.persist(e2);
			utx.commit();

			getDao().deleteAll();

			List<E> results = findAllQuery.getResultList();

			assertEquals("Database should be empty.", 0, results.size());

		} finally {
			// Rollback transaction in case it is open due to some error
			if (utx.getStatus() == Status.STATUS_ACTIVE) {
				utx.rollback();
			}
		}
	}

	protected abstract E getEntity(int number) throws Exception;

	protected abstract GenericDao<E, PK> getDao();

	protected Date getDate() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(CONFIG_DATE_FORMAT);
		Date date = sdf.parse(STATION_LASTUPDATE);
		return date;
	}

}
