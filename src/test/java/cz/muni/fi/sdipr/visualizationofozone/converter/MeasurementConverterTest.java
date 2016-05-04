package cz.muni.fi.sdipr.visualizationofozone.converter;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cz.muni.fi.sdipr.visualizationofozone.model.Measurement;

public class MeasurementConverterTest {

	public static final float VALUE_1 = 10.0F;
	public static final float VALUE_2 = 7.0F;
	public static final long DATE_1 = 123456789L;
	public static final long DATE_2 = 987654321L;

	MeasurementConvertor converter = new MeasurementConvertor();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testEntityToDto() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(DATE_1);
		Date date1 = c.getTime();

		c.setTimeInMillis(DATE_2);
		Date date2 = c.getTime();

		Measurement m1 = new Measurement();
		m1.setPhenomenonTypeId(2L);
		m1.setStationId(10L);
		m1.setDateTime(date1);
		m1.setValue(VALUE_1);

		Measurement m2 = new Measurement();
		m2.setPhenomenonTypeId(2L);
		m2.setStationId(10L);
		m2.setDateTime(date2);
		m2.setValue(VALUE_2);

		List<Measurement> measurements = new ArrayList<>();
		measurements.add(m1);
		measurements.add(m2);

		List<Number[]> result = converter.toChartReadableArray(measurements);

		assertEquals("Bad number of elements in result list.", 2, result.size());
		assertEquals("Both elements has to be array of two element but first isn't.", 2, result.get(0).length);
		assertEquals("Both elements has to be array of two element but second isn't. ", 2, result.get(0).length);
		assertEquals("Bad date value.", DATE_1, result.get(0)[0]);
		assertEquals("Bad date value.", VALUE_1, result.get(0)[1]);
		assertEquals("Bad date value.", DATE_2, result.get(1)[0]);
		assertEquals("Bad date value.", VALUE_2, result.get(1)[1]);
	}
}
