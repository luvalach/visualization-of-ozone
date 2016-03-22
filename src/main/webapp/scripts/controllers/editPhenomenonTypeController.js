

angular.module('visualizationofozone').controller('EditPhenomenonTypeController', function($scope, $routeParams, $location, flash, PhenomenonTypeResource , SourceResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.phenomenonType = new PhenomenonTypeResource(self.original);
            SourceResource.queryAll(function(items) {
                $scope.sourceSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.id
                    };
                    if($scope.phenomenonType.source && item.id == $scope.phenomenonType.source.id) {
                        $scope.sourceSelection = labelObject;
                        $scope.phenomenonType.source = wrappedObject;
                        self.original.source = $scope.phenomenonType.source;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The phenomenonType could not be found.'});
            $location.path("/PhenomenonTypes");
        };
        PhenomenonTypeResource.get({PhenomenonTypeId:$routeParams.PhenomenonTypeId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.phenomenonType);
    };

    $scope.save = function() {
        var successCallback = function(){
            flash.setMessage({'type':'success','text':'The phenomenonType was updated successfully.'}, true);
            $scope.get();
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        $scope.phenomenonType.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/PhenomenonTypes");
    };

    $scope.remove = function() {
        var successCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The phenomenonType was deleted.'});
            $location.path("/PhenomenonTypes");
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        }; 
        $scope.phenomenonType.$remove(successCallback, errorCallback);
    };
    
    $scope.$watch("sourceSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.phenomenonType.source = {};
            $scope.phenomenonType.source.id = selection.value;
        }
    });
    
    $scope.get();
});