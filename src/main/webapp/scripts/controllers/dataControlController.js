angular.module('visualizationofozone').controller('DataControlController',
		function($scope, flash, $resource) {

			var updateDbResource = $resource('rest/datacontrols/update');
			$scope.updateDb = function() {
				var successCallback = function(data) {
					flash.setMessage({
						'type' : 'info',
						'text' : 'Database update started.'
					});
				};
				var errorCallback = function() {
					flash.setMessage({
						'type' : 'error',
						'text' : 'Fail to start database update.'
					});
				};

				updateDbResource.query(successCallback, errorCallback);
			};

			var cleanDbResource = $resource('rest/datacontrols/clean');
			$scope.cleanDb = function() {
				var successCallback = function(data) {
					flash.setMessage({
						'type' : 'info',
						'text' : 'Database successfully cleaned.'
					});
				};
				var errorCallback = function() {
					flash.setMessage({
						'type' : 'error',
						'text' : 'Fail to clean database.'
					});
				};

				cleanDbResource.query(successCallback, errorCallback);
			};
		});