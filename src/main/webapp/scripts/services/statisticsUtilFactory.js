'use strict';

angular.module('visualizationofozone')
		.factory('statisticsUtil', statisticsUtil);

function statisticsUtil() {
	var util = {};

	util.getMean = function(valuesArray) {
		var sum = 0;

		for (var i = 0; i < valuesArray.length; i++) {
			sum += valuesArray[i][1];
		}

		return sum / valuesArray.length;
	}

	util.getMedian = function(valuesArray) {
		var median = 0;
		var numbers = [];

		for (var i = 0; i < valuesArray.length; i++) {
			numbers.push(valuesArray[i][1]);
		}

		var numsLen = numbers.length;
		numbers.sort(function(a, b){return a-b});

		if (numsLen % 2 === 0) { // is even
			// average of two middle numbers
			return (numbers[numsLen / 2 - 1] + numbers[numsLen / 2]) / 2;
		} else { // is odd
			// middle number only
			return numbers[(numsLen - 1) / 2];
		}
	}

	return util;
}
