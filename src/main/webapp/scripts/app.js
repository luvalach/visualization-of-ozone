'use strict';

angular.module('visualizationofozone',['ngRoute','ngResource'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/',{templateUrl:'views/landing.html',controller:'LandingPageController'})
      .when('/Files',{templateUrl:'views/File/search.html',controller:'SearchFileController'})
      .when('/Files/new',{templateUrl:'views/File/detail.html',controller:'NewFileController'})
      .when('/Files/edit/:FileId',{templateUrl:'views/File/detail.html',controller:'EditFileController'})
      .when('/Measurements',{templateUrl:'views/Measurement/search.html',controller:'SearchMeasurementController'})
      .when('/Measurements/new',{templateUrl:'views/Measurement/detail.html',controller:'NewMeasurementController'})
      .when('/Measurements/edit/:StationId/:PhenomenonTypeId/:DateTime',{templateUrl:'views/Measurement/detail.html',controller:'EditMeasurementController'})
      .when('/PhenomenonTypes',{templateUrl:'views/PhenomenonType/search.html',controller:'SearchPhenomenonTypeController'})
      .when('/PhenomenonTypes/new',{templateUrl:'views/PhenomenonType/detail.html',controller:'NewPhenomenonTypeController'})
      .when('/PhenomenonTypes/edit/:PhenomenonTypeId',{templateUrl:'views/PhenomenonType/detail.html',controller:'EditPhenomenonTypeController'})
      .when('/Sources',{templateUrl:'views/Source/search.html',controller:'SearchSourceController'})
      .when('/Sources/new',{templateUrl:'views/Source/detail.html',controller:'NewSourceController'})
      .when('/Sources/edit/:SourceId',{templateUrl:'views/Source/detail.html',controller:'EditSourceController'})
      .when('/Stations',{templateUrl:'views/Station/search.html',controller:'SearchStationController'})
      .when('/Stations/new',{templateUrl:'views/Station/detail.html',controller:'NewStationController'})
      .when('/Stations/edit/:StationId',{templateUrl:'views/Station/detail.html',controller:'EditStationController'})
      .when('/DataControls',{templateUrl:'views/DataControl/dataControl.html',controller:'DataControlController'})
      .otherwise({
        redirectTo: '/'
      });
  }])
  .controller('LandingPageController', function LandingPageController() {
  })
  .controller('NavController', function NavController($scope, $location) {
    $scope.matchesRoute = function(route) {
        var path = $location.path();
        return (path === ("/" + route) || path.indexOf("/" + route + "/") == 0);
    };
  });
