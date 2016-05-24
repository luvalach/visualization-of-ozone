package cz.muni.fi.sdipr.visualizationofozone.converter;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.sdipr.visualizationofozone.model.Measurement;

/**
 * Measurement converter converts list of measurements to form needed by
 * ZingChart component. All values far below zero will be considered as
 * measurement error and will be replaced by zero.
 * 
 * @author Lukas
 *
 */
public class MeasurementConvertor {
	public static List<Number[]> toChartReadableArray(List<Measurement> measurements) {
		List<Number[]> measurementsArray = new ArrayList<Number[]>();

		for (Measurement m : measurements) {
			Float value = m.getValue() < -1000000000 ? 0 : m.getValue();
			measurementsArray.add(new Number[] { m.getDateTime().getTime(), value });
		}
		return measurementsArray;
	}
}
