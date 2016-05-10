package cz.muni.fi.sdipr.visualizationofozone.dao;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.runner.RunWith;

import cz.muni.fi.sdipr.visualizationofozone.model.PhenomenonType;

@RunWith(Arquillian.class)
public class PhenomenonTypeDaoTest extends GenericDaoTest<PhenomenonType, Long> {

	@EJB
	protected PhenomenonTypeDao dao;

	public PhenomenonTypeDaoTest() {
		super(PhenomenonType.class, Long.class);
	}

	@Override
	protected GenericDao<PhenomenonType, Long> getDao() {
		return dao;
	}

	@Override
	protected PhenomenonType getEntity(int number) throws Exception {
		PhenomenonType s = new PhenomenonType();
		s.setName("Name " + number);
		s.setNameShortcut("NameSh " + number);
		s.setDescription("Desc " + number);
		s.setUnit("Unit " + number);
		s.setUnitShortcut("UnitSh " + number);
		s.setColumnNo(number);
		return s;
	}

	@Override
	public void callTestUpdate() throws Exception {
		testUpdate(v -> e1.setName(v), () -> e1.getName(), "New name");
	}
}
