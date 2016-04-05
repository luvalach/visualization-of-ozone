package cz.muni.fi.sdipr.visualizationofozone.downloading.impl;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.sdipr.visualizationofozone.model.Measurement;

public class MeasurementConvertor {
	public static List<Number[]> toChartReadableArray(List<Measurement> measurements) {
		List<Number[]> measurementsArray = new ArrayList<Number[]>();

		for (Measurement m : measurements) {
			measurementsArray.add(new Number[] { m.getDateTime().getTime(), m.getValue() });
		}
		return measurementsArray;
	}
}
