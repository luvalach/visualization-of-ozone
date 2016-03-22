
angular.module('visualizationofozone').controller('NewStationController', function ($scope, $location, locationParser, flash, StationResource ) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.station = $scope.station || {};
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            flash.setMessage({'type':'success','text':'The station was created successfully.'});
            $location.path('/Stations');
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        StationResource.save($scope.station, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Stations");
    };
});