'use strict';

angular.module('visualizationofozone',['ngRoute','ngResource', 'ui.bootstrap', 'zingchart-angularjs', 'ui.select', 'ngSanitize', 'daterangepicker', 'ui.sortable'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/Dashboard',{templateUrl:'views/Dashboard/dashboard.html',controller:'DashboardController'})
      .otherwise({
        redirectTo: '/Dashboard'
      });
  }])
  .controller('NavController', function NavController($scope, $location) {
    $scope.matchesRoute = function(route) {
        var path = $location.path();
        return (path === ("/" + route) || path.indexOf("/" + route + "/") == 0);
    };
  });
