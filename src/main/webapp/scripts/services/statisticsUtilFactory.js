'use strict';

angular.module('visualizationofozone')
		.factory('statisticsUtil', statisticsUtil);

function statisticsUtil() {
	var util = {};

	util.getMean = function(values) {
		var sum = 0;

		for (var i = 0; i < values.length; i++) {
			sum += values[i];
		}

		return sum / values.length;
	}

	util.getMedian = function(values) {
		var median = 0;
		var numbers = values;

		var numsLen = numbers.length;
		numbers.sort(function(a, b) {
			return a - b
		});

		if (numsLen % 2 === 0) { // is even
			// average of two middle numbers
			return (numbers[numsLen / 2 - 1] + numbers[numsLen / 2]) / 2;
		} else { // is odd
			// middle number only
			return numbers[(numsLen - 1) / 2];
		}
	}

	util.getStandardDeviation = function(values, mean) {
		var avg = mean || util.getMean(values);

		var squareDiffs = values.map(function(value) {
			var diff = value - avg;
			var sqrDiff = diff * diff;
			return sqrDiff;
		});

		var avgSquareDiff = util.getMean(squareDiffs);

		var stdDev = Math.sqrt(avgSquareDiff);
		return stdDev;
	}

	/**
	 * Pull out measurements values from array of pair date-value.
	 * Return single dimension array.
	 */
	util.separateValues = function(arrayOfDateValuePairs) {
		var numbers = [];

		for (var i = 0; i < arrayOfDateValuePairs.length; i++) {
			numbers.push(arrayOfDateValuePairs[i][1]);
		}
		return numbers;
	}

	return util;
}
