
angular.module('visualizationofozone').controller('NewSourceController', function ($scope, $location, locationParser, flash, SourceResource , PhenomenonTypeResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.source = $scope.source || {};
    
    $scope.phenomenonTypeList = PhenomenonTypeResource.queryAll(function(items){
        $scope.phenomenonTypeSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.id
            });
        });
    });
    $scope.$watch("phenomenonTypeSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.source.phenomenonType = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.source.phenomenonType.push(collectionItem);
            });
        }
    });


    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            flash.setMessage({'type':'success','text':'The source was created successfully.'});
            $location.path('/Sources');
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        SourceResource.save($scope.source, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Sources");
    };
});