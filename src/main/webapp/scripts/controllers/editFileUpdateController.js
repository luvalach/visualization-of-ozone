

angular.module('visualizationofozone').controller('EditFileUpdateController', function($scope, $routeParams, $location, flash, FileUpdateResource ) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.fileUpdate = new FileUpdateResource(self.original);
        };
        var errorCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The fileUpdate could not be found.'});
            $location.path("/FileUpdates");
        };
        FileUpdateResource.get({FileUpdateId:$routeParams.FileUpdateId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.fileUpdate);
    };

    $scope.save = function() {
        var successCallback = function(){
            flash.setMessage({'type':'success','text':'The fileUpdate was updated successfully.'}, true);
            $scope.get();
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        $scope.fileUpdate.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/FileUpdates");
    };

    $scope.remove = function() {
        var successCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The fileUpdate was deleted.'});
            $location.path("/FileUpdates");
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        }; 
        $scope.fileUpdate.$remove(successCallback, errorCallback);
    };
    
    
    $scope.get();
});