
angular.module('visualizationofozone').controller('NewFileUpdateController', function ($scope, $location, locationParser, flash, FileUpdateResource ) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.fileUpdate = $scope.fileUpdate || {};
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            flash.setMessage({'type':'success','text':'The fileUpdate was created successfully.'});
            $location.path('/FileUpdates');
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        FileUpdateResource.save($scope.fileUpdate, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/FileUpdates");
    };
});