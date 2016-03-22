

angular.module('visualizationofozone').controller('EditMeasurementController', function($scope, $routeParams, $location, flash, MeasurementResource, StationResource, PhenomenonTypeResource ) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.updateMode = true;
    
    $scope.get = function() {
        var successCallback = function(data){
        	try{
        		// Try to convert date in millisecond to Date object
        		var dateTime = new Date(parseInt(data.dateTime));
            	data.dateTime = dateTime;
        	} catch (err){
        		//Can't convert date or data object hasn't dateTime field.
        	}
        	
            self.original = data;
            $scope.measurement = new MeasurementResource(self.original);
            StationResource.queryAll(function(items) {
                $scope.stationSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.name
                    };
                    if($scope.measurement.stationId && item.id == $scope.measurement.stationId) {
                        $scope.stationSelection = labelObject;
                        $scope.measurement.stationId = wrappedObject;
                        self.original.stationId = $scope.measurement.stationId;
                    }
                    return labelObject;
                });
            });
            PhenomenonTypeResource.queryAll(function(items) {
                $scope.phenomenonTypeSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.name
                    };
                    if($scope.measurement.phenomenonTypeId && item.id == $scope.measurement.phenomenonTypeId) {
                        $scope.phenomenonTypeSelection = labelObject;
                        $scope.measurement.phenomenonTypeId = wrappedObject;
                        self.original.phenomenonTypeId = $scope.measurement.phenomenonTypeId;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The measurement could not be found.'});
            $location.path("/Measurements");
        };
        
        var dateTime = new Date(parseInt($routeParams.DateTime));
        MeasurementResource.get({StationId:$routeParams.StationId,PhenomenonTypeId:$routeParams.PhenomenonTypeId,DateTime:dateTime}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.measurement);
    };

    $scope.save = function() {
        var successCallback = function(){
            flash.setMessage({'type':'success','text':'The measurement was updated successfully.'}, true);
            $scope.get();
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        $scope.measurement.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Measurements");
    };

    $scope.remove = function() {
        var successCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The measurement was deleted.'});
            $location.path("/Measurements");
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        }; 
        $scope.measurement.$remove(successCallback, errorCallback);
    };
    
    
    $scope.get();
});