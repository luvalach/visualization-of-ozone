angular
		.module('visualizationofozone')
		.controller(
				'DashboardController',
				function($scope, $http, $filter, $window, StationResource,
						DashboardResource, PhenomenonTypeResource, flash) {

					$scope.search = {
						stations : [],
						date : {
							startDate : moment().subtract(1, 'year'),
							endDate : moment()
						}
					};
					/*
					 * data fetched from server (stations and measures)
					 */
					$scope.searchResults = [];
					$scope.wideMode = false;
					$scope.panels = {
						open1 : true, // filter
						open2 : true, // chart
						open3 : true // globe
					};
					$scope.allStationsItem = {
						id : 0,
						name : 'All stations',
						country : ''
					};
					/* all stations without measurements */
					$scope.stationList = [ $scope.allStationsItem ];
					$scope.search.stations = []; // Default filter setting

					$scope.defaultPhenomenonSelection = {
						0 : true
					};
					$scope.phenomenonTypeList = [ {
						id : 1,
						name : "Ozone"
					}, {
						id : 2,
						name : "UV Index"
					} ];
					$scope.search.phenomenonTypes = {};

					$scope.datePicker = {
						opts : {
							ranges : {
								'Today' : [ moment(), moment() ],
								'Yesterday' : [ moment().subtract(1, 'days'),
										moment().subtract(1, 'days') ],
								'Last 7 Days' : [ moment().subtract(6, 'days'),
										moment() ],
								'Last 30 Days' : [
										moment().subtract(29, 'days'), moment() ],
								'This Month' : [ moment().startOf('month'),
										moment().endOf('month') ],
								'Last Month' : [ moment().subtract(1, 'month'),
										moment() ],
								'This Year' : [ moment().startOf('year'),
										moment().endOf('year') ],
								'Last Year' : [ moment().subtract(1, 'year'),
										moment() ]
							},
							locale : {
								"format" : "DD.MM.YYYY",
								"separator" : " - ",
								"applyLabel" : "Apply",
								"cancelLabel" : "Cancel",
								"fromLabel" : "From",
								"toLabel" : "To",
								"customRangeLabel" : "Custom",
								"firstDay" : 1
							},
						}
					};

					$scope.sortableOptions = {
						handle : '>> .sortable-handle'
					};

					$scope.refreshListOfStations = function() {
						StationResource.queryAll(function(items) {
							$scope.stationList = items;
							$scope.stationList.push($scope.allStationsItem);
							$scope.initialSearch();
						});
					};

					$scope.refreshListOfPhenomenonTypes = function() {
						PhenomenonTypeResource.queryAll(function(items) {
							$scope.phenomenonTypeList = items;
							$scope.initialSearch();
						});
					};

					$scope.performSearch = function() {
						var successCallback = function(data) {
							$scope.searchResults = data;
						}
						var errorCallback = function(response) {
							if (response && response.data) {
								var fieldErrors = response.data.fieldErrors
										|| [];

								for (i = 0; i < fieldErrors.length; i++) {
									flash.setMessage({
										'type' : fieldErrors[i].type,
										'message' : fieldErrors[i].message,
										'field' : fieldErrors[i].field
									});
								}
							} else {
								flash.setMessage({
									'type' : 'error',
									'text' : 'Could not download data.'
								});
							}
						};

						flash.cleanMessages();

						DashboardResource
								.query(
										{
											stationIds : $scope.search.stations,
											phenomenonTypeIds : $scope
													.selectionToArrayOfIds($scope.search.phenomenonTypes),
											startDate : $scope.search.date.startDate,
											endDate : $scope.search.date.endDate
										}, successCallback, errorCallback);
					};

					$scope.selectionToArrayOfIds = function(selection) {
						var array = []; // array of IDs of selected items
						for ( var key in selection) {
							if (selection[key] == true) {
								array.push(key);
							}
						}
						return array;
					};

					$scope.unselectStationById = function(id) {
						for (var i = 0; i < $scope.search.stations.length; i++) {
							if ($scope.search.stations[i] == id) {
								$scope.search.stations.splice(i, 1);
								break;
							}
						}
					};

					$scope.stationSelectionChanged = function($item, $model) {
						if ($model == $scope.allStationsItem.id) {
							$scope.search.stations = [ $scope.allStationsItem.id ];
						} else {
							$scope
									.unselectStationById($scope.allStationsItem.id);
						}
					};

					$scope.onDoubleClick = function(entity) {
						$scope.search.stations = [ entity.id ];
						$scope.performSearch();
					};

					$scope.onClickWithCtrl = function(entity) {
						$scope.unselectStationById($scope.allStationsItem.id);

						for (var i = 0; i < $scope.search.stations.length; i++) {
							if ($scope.search.stations[i] == entity.id) {
								return;
							}
						}
						var newArray = [ entity.id ];
						newArray.push.apply(newArray, $scope.search.stations);
						$scope.search.stations = newArray;
					};

					$scope.setMendelAsSelected = function() {
						for (var i = 0; i < $scope.stationList.length; i++) {
							if ($scope.stationList[i].name == "Mendel") {
								$scope.search.stations = [ $scope.stationList[i].id ];
								return true;
							}
						}
						return false;
					}

					$scope.setOzoneAsSelected = function() {
						for (var i = 0; i < $scope.phenomenonTypeList.length; i++) {
							if ($scope.phenomenonTypeList[i].name == "Ozone") {
								$scope.search.phenomenonTypes = {};
								$scope.search.phenomenonTypes[$scope.phenomenonTypeList[i].id] = true;
								return true;
							}
						}
						return false;
					}

					// $scope.getStationById = function(id){
					// for (var i = 0; i < $scope.stationList.length; i++) {
					// if ($scope.stationList[i].id == id){
					// return $scope.stationList[i];
					// }
					// }
					// return null;
					// }

					$scope.getPhenomenonById = function(id) {
						for (var i = 0; i < $scope.phenomenonTypeList.length; i++) {
							if ($scope.phenomenonTypeList[i].id == id) {
								return $scope.phenomenonTypeList[i];
							}
						}
						return null;
					}

					$scope.switchChart = function() {
						if ($scope.searchResults.length > 10) {
							$scope.chart = $scope.barChart;
						} else {
							$scope.chart = $scope.lineChart;
							$scope.panels.open2 = true;
						}
					}

					/**
					 * This method should be called after stations and
					 * phenomenons lists are downloaded. If station or
					 * phenomenon aren't selected by user the method try to set
					 * default values for first search. Whenever both default
					 * station and phenomenon are set then the performSearch()
					 * is called.
					 */
					$scope.initialSearch = function() {
						var stationFound = true;
						var phenomenonFound = true;

						if ($scope.search.stations.length == 0) {
							stationFound = $scope.setMendelAsSelected();
						}
						if ($scope
								.selectionToArrayOfIds($scope.search.phenomenonTypes).length == 0) {
							phenomenonFound = $scope.setOzoneAsSelected();
						}
						if (stationFound && phenomenonFound) {
							$scope.performSearch();
						}
					}

					$scope.$watch(function() {
						return $window.innerWidth;
					}, function(value) {
						$scope.wideMode = value > 750;
					});

					$scope.refreshListOfStations();
					$scope.refreshListOfPhenomenonTypes();
				});