

angular.module('visualizationofozone').controller('EditSourceController', function($scope, $routeParams, $location, flash, SourceResource , PhenomenonTypeResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.source = new SourceResource(self.original);
            PhenomenonTypeResource.queryAll(function(items) {
                $scope.phenomenonTypeSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.id
                    };
                    if($scope.source.phenomenonType){
                        $.each($scope.source.phenomenonType, function(idx, element) {
                            if(item.id == element.id) {
                                $scope.phenomenonTypeSelection.push(labelObject);
                                $scope.source.phenomenonType.push(wrappedObject);
                            }
                        });
                        self.original.phenomenonType = $scope.source.phenomenonType;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The source could not be found.'});
            $location.path("/Sources");
        };
        SourceResource.get({SourceId:$routeParams.SourceId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.source);
    };

    $scope.save = function() {
        var successCallback = function(){
            flash.setMessage({'type':'success','text':'The source was updated successfully.'}, true);
            $scope.get();
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        $scope.source.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Sources");
    };

    $scope.remove = function() {
        var successCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The source was deleted.'});
            $location.path("/Sources");
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        }; 
        $scope.source.$remove(successCallback, errorCallback);
    };
    
    $scope.phenomenonTypeSelection = $scope.phenomenonTypeSelection || [];
    $scope.$watch("phenomenonTypeSelection", function(selection) {
        if (typeof selection != 'undefined' && $scope.source) {
            $scope.source.phenomenonType = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.source.phenomenonType.push(collectionItem);
            });
        }
    });
    
    $scope.get();
});