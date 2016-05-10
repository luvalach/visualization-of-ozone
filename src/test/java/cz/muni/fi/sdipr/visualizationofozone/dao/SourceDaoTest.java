package cz.muni.fi.sdipr.visualizationofozone.dao;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.runner.RunWith;

import cz.muni.fi.sdipr.visualizationofozone.model.Source;

@RunWith(Arquillian.class)
public class SourceDaoTest extends GenericDaoTest<Source, Long> {

	@EJB
	protected SourceDao dao;

	public SourceDaoTest() {
		super(Source.class, Long.class);
	}

	@Override
	protected GenericDao<Source, Long> getDao() {
		return dao;
	}

	@Override
	protected Source getEntity(int number) throws Exception {
		Source s = new Source();
		s.setUrl("URL " + number);
		s.setDescription("Description " + number);
		s.setTableHeaderPattern("Pattern " + number);
		return s;
	}

	@Override
	public void callTestUpdate() throws Exception {
		testUpdate(v -> e1.setUrl(v), () -> e1.getUrl(), "New URL");
	}
}
