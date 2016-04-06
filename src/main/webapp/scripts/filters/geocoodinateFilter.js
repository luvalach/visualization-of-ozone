'use strict';

angular.module('visualizationofozone').filter('geocoordinate', function() {
	this.latitude = function(input) {
		return this._toGeolocate(input, 'N', 'S');
	};
	
	this.longitude = function(input) {
		return this._toGeolocate(input, 'E', 'W');
	};
	
	this._toGeolocate = function(input, positive, negative) {
		var polarity = input >= 0 ? positive : negative;
		var absoluteValue = Math.abs(input).toString();
		var splited = absoluteValue.split('.');
		var grades = splited[0] + '° '

		if (splited.length == 2) {
			var minutesAndSeconds = ('0.' + splited[1]) * 60;
		} else {
			var minutesAndSeconds = 0;
		}

		var splitedMinAndSec = minutesAndSeconds.toString().split('.');
		var minutes = splitedMinAndSec[0] + '\' ';

		if (splitedMinAndSec.length == 2) {
			var seconds = Math.round(('0.' + splitedMinAndSec[1]) * 60) + '\'\' ';
		} else {
			var seconds = '';
		}

		return grades + minutes + seconds + polarity;
	};
	
	return this;
});