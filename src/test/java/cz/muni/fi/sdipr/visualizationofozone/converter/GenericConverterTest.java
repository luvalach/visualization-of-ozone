package cz.muni.fi.sdipr.visualizationofozone.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public abstract class GenericConverterTest<T1, T2> {

	private T1 origEntity;
	private T2 origDto;

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "ozoneTest.war").addPackages(true, "org.dozer")
				.addPackages(true, "org.apache.commons.lang3").addPackages(true, "org.apache.commons.beanutils")
				.addPackages(true, "cz.muni.fi.sdipr.visualizationofozone.converter")
				.addPackages(true, "cz.muni.fi.sdipr.visualizationofozone.model")
				.addPackages(true, "cz.muni.fi.sdipr.visualizationofozone.rest.dto")
				.addAsResource("dozerBeanMapping.xml", "dozerBeanMapping.xml")
				.addAsResource("beanmapping.xsd", "beanmapping.xsd")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Before
	public void setUp() throws Exception {
		this.origEntity = initEntity();
		this.origDto = initDto();
	}

	protected abstract T1 initEntity() throws Exception;

	protected abstract T2 initDto() throws Exception;

	protected abstract GenericConverter<T1, T2> getConverter();

	@Test
	public void testEntityToDto() {
		T2 dto = getConverter().entityToDto(origEntity);
		assertEquals(origDto, dto);
	}

	@Test
	public void testDtoToEntity() {
		T1 entity = getConverter().dtoToEntity(origDto);
		assertEquals(origEntity, entity);
	}

	@Test
	public void testEntitiesToDtos() {
		List<T1> entities = new ArrayList<>();
		entities.add(origEntity);
		entities.add(origEntity);
		entities.add(origEntity);

		List<T2> dtos = getConverter().entitiesToDtos(entities);
		assertNotNull(dtos);
		assertEquals("Converter returns bad number of entities.", entities.size(), dtos.size());
	}

	@Test
	public void testDtosToEntities() {
		List<T2> dtos = new ArrayList<>();
		dtos.add(origDto);
		dtos.add(origDto);
		dtos.add(origDto);

		List<T1> entities = getConverter().dtosToEntities(dtos);
		assertNotNull(entities);
		assertEquals("Converter returns bad number of dtos.", dtos.size(), entities.size());
	}

}
