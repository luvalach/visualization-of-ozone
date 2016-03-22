
angular.module('visualizationofozone').controller('NewMeasurementController', function ($scope, $location, locationParser, flash, MeasurementResource, StationResource, PhenomenonTypeResource ) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.measurement = $scope.measurement || {};
    
    //Select Station
    $scope.stationList = StationResource.queryAll(function(items){
        $scope.stationSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.name
            });
        });
    });
    //Select Station
    $scope.$watch("stationSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.measurement.stationId = selection.value;
        }
    });
    
    //Select Phenomenon Type
    $scope.phenomenonTypeList = PhenomenonTypeResource.queryAll(function(items){
        $scope.phenomenonTypeSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.name
            });
        });
    });
  //Select Phenomenon Type
    $scope.$watch("phenomenonTypeSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.measurement.phenomenonTypeId = selection.value;
        }
    });

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            flash.setMessage({'type':'success','text':'The measurement was created successfully.'});
            $location.path('/Measurements');
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        MeasurementResource.save($scope.measurement, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Measurements");
    };
});