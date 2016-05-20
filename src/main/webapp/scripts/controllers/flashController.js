'use strict';

angular.module('visualizationofozone').controller('FlashController', ['$scope','flash', function ($scope, flash) {
    $scope.flash = flash;
    $scope.showAlert = false;
    $scope.messages = flash.messages;

    $scope.$watch('flash.getMessages()', function(newVal) {
    	$scope.messages = flash.getMessages();
    });

    $scope.closeMessage = function(message) {
    	flash.closeMessage(message);
    }
}]);
