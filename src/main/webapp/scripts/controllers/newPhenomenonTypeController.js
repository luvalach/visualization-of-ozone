
angular.module('visualizationofozone').controller('NewPhenomenonTypeController', function ($scope, $location, locationParser, flash, PhenomenonTypeResource , SourceResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.phenomenonType = $scope.phenomenonType || {};
    
    $scope.sourceList = SourceResource.queryAll(function(items){
        $scope.sourceSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.id
            });
        });
    });
    $scope.$watch("sourceSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.phenomenonType.source = {};
            $scope.phenomenonType.source.id = selection.value;
        }
    });
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            flash.setMessage({'type':'success','text':'The phenomenonType was created successfully.'});
            $location.path('/PhenomenonTypes');
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        PhenomenonTypeResource.save($scope.phenomenonType, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/PhenomenonTypes");
    };
});