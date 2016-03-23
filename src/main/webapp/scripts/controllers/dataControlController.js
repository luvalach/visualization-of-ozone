angular.module('visualizationofozone').controller('DataControlController',
		function($scope, flash, $resource, $route) {

			var updateDbResource = $resource('rest/datacontrols/update');
			$scope.updateDb = function() {
				var successCallback = function(data) {
					flash.setMessage({
						'type' : 'info',
						'text' : 'Database update started.'
					});
					$route.reload();
				};
				var errorCallback = function() {
					flash.setMessage({
						'type' : 'error',
						'text' : 'Fail to start database update.'
					});
					$route.reload();
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
					$route.reload();
				};
				var errorCallback = function() {
					flash.setMessage({
						'type' : 'error',
						'text' : 'Fail to clean database.'
					});
					$route.reload();
				};

				cleanDbResource.query(successCallback, errorCallback);
			};
		});