
angular.module('visualizationofozone').controller('NewFileController', function ($scope, $location, locationParser, flash, FileResource ) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.file = $scope.file || {};
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            flash.setMessage({'type':'success','text':'The file was created successfully.'});
            $location.path('/Files');
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        FileResource.save($scope.file, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Files");
    };
});