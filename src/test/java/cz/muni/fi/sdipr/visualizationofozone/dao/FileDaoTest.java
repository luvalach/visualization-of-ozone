package cz.muni.fi.sdipr.visualizationofozone.dao;

import static org.junit.Assert.assertEquals;

import javax.ejb.EJB;
import javax.transaction.Status;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.fi.sdipr.visualizationofozone.model.File;

@RunWith(Arquillian.class)
public class FileDaoTest extends GenericDaoTest<File, String> {

	@EJB
	protected FileDao dao;

	public FileDaoTest() {
		super(File.class, String.class);
	}

	@Override
	protected GenericDao<File, String> getDao() {
		return dao;
	}

	@Override
	protected File getEntity(int number) throws Exception {
		File f = new File();
		f.setFileName("File " + number);
		f.setLastRowDate(getDate());
		f.setLastUpdate(getDate());
		return f;
	}

	@Override
	public void callTestUpdate() throws Exception {
		testUpdate(v -> e1.setLastRowDate(v), () -> e1.getLastRowDate(), getDate("5.4.2016"));
	}

	@Test
	public void testFindByName() throws Exception {
		try {
			e1.setFileName("Another name");

			utx.begin();
			em.persist(e1);
			em.persist(e2);
			utx.commit();

			File result = dao.findByFileName(e1.getFileName());

			assertEquals("Database should return file with given name.", e1, result);
		} finally {
			// Rollback transaction in case it is open due to some error
			if (utx.getStatus() == Status.STATUS_ACTIVE) {
				utx.rollback();
			}
		}
	}
}
