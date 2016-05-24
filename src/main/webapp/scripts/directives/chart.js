"use strict";

angular
		.module('visualizationofozone')
		.directive(
				'chartDirective',
				function($interval, $log, statisticsUtil) {
					return {
						restrict : "EA",
						controllerAs : "chartCtrl",
						scope : {
							data : "=",
							stationList : "=",
							phenomenon : "="
						},
						template : '<div ng-hide="hide">'
								+ '<span class="pull-right glyphicon glyphicon-move sortable-handle"></span>'
								+ '<zingchart id="chart-{{phenomenon.id}}" zc-json="chart" zc-render="myRender" zc-width="100%"></zingchart>'
								+'<div ng-show="stationStatistics != null">'
								+ '<table style="width: 100%; text-align: center">'
								+ '<tr>'
								+ '<td style=" background-color: rgb({{sample.color}})">Mean: {{stationStatistics.mean}}</td>'
								+ '<td style=" background-color: rgb({{sample.color}})">Median: {{stationStatistics.median}}</td>'
								+ '<td style=" background-color: rgb({{sample.color}})">Standard deviation: {{stationStatistics.standardDeviation}}</td>'
								+ '</tr>' 
								+ '</table>'
								+ '</div>' 
								+ '</div>',
						controller : function($scope) {
							/**
							 * Limit number of stations for chart. If limit is
							 * exceeded the chart is hidden.
							 */
							const
							CHART_LIMIT = 5;

							/*
							 * Hide chart
							 */
							$scope.hide = false;
							/*
							 * Carries statistics data like mean, median and
							 * standard deviation. Statistics data will be
							 * computed only for one station.
							 */
							$scope.stationStatistics = null;

							this.lineChart = {
								type : 'line',
								title : {
									"text" : ""
								},
								"crosshair-x" : {
									"plot-label" : {
										"visible" : false
									},
									"scale-label" : {
										"backgroundColor" : "red",
										"color" : "white",
										"text" : "%scale-label"
									}
								},
								plot : {
									aspect : 'spline',
									"tooltip" : {
										"text" : "Date: %k <br/> Value: %vt",
										"placement" : "node:top"
									},
									"value-box" : {
										"visible" : "true",
										"text" : "%v",
										"type" : "min,max"
									}
								},
								"legend" : {
									"header" : {
										"text" : "Stations"
									},
									"max-items" : 5,
									"overflow" : "page",
									"highlight-plot" : true,
									"draggable" : true,
									"tooltip" : {
										"text" : "Click to show/hide series."
									}
								},
								"scale-y" : {
									"label" : {
										"text" : ""
									},
									"markers" : [],
									"zooming" : true,
								},
								"scale-x" : {
									"label" : {
										"text" : "Time",
									},
									"transform" : {
										// "all":"%m.%d.%Y",
										"type" : "date"
									},
									"max-items" : 6,
									"zooming" : true,
									"zoom-snap" : true,
								},
								"scroll-x" : {},
								height : "100%",
								width : "100%",
								series : []
							};

							this.barChart = {
								type : 'bar',
								title : {
									"text" : ""
								},
								plot : {
									aspect : 'spline',
									"tooltip" : {
										"text" : "",
										"placement" : "node:top"
									}
								},
								"legend" : {
									"header" : {
										"text" : "Stations"
									},
									"max-items" : 8,
									"overflow" : "page",
									"highlight-plot" : true,
									"draggable" : false,
									"tooltip" : {
										"text" : "Click to show/hide series."
									}
								},
								"scale-y" : {
									"label" : {
										"text" : ""
									},
									"zooming" : true,
								},
								"scale-x" : {
									"label" : {
										"text" : "Stations",
									},
									"show-labels" : [],
									"zooming" : true,
								},
								"scroll-x" : {},
								height : "100%",
								width : "100%",
								series : []
							};

							$scope.myRender = {
								output : "auto",
								events : {
									complete : function(p) {
										console.log("Chart is finished!");
									}
								}
							}

							this.getStationById = function(id) {
								for (var i = 0; i < $scope.stationList.length; i++) {
									if ($scope.stationList[i].id == id) {
										return $scope.stationList[i];
									}
								}
								return null;
							}

							this.refresh = function() {
								if ($scope.data.dataPerStations.length <= CHART_LIMIT) {
									this.refreshLineSeries();
									this.refreshLineLabels();
									this.addStatistics();
									$scope.chart = this.lineChart;
									$scope.hide = false;
								} else {
									// this.refreshBarSeries();
									// this.refreshBarLabels();
									// $scope.chart = null;
									$scope.hide = true;
								}
							}

							this.refreshLineSeries = function() {
								var dataPerStations = $scope.data.dataPerStations;
								var chartData = [];
								for (var i = 0; i < dataPerStations.length; i++) {
									var stationId = dataPerStations[i].stationId;
									var station = this
											.getStationById(stationId);

									if (station != null) {
										var newSeries = {
											text : station.name,
											values : dataPerStations[i].measurements
										}
										chartData.push(newSeries);
									} else {
										$log.error('Station with id '
												+ stationId + 'not found.');
									}
								}

								this.lineChart['series'] = chartData;
							}

							this.refreshLineLabels = function() {
								this.lineChart.title.text = $scope.phenomenon.description;
								this.lineChart.plot['tooltip']['text'] = 'Value: %vt '
										+ '[' + $scope.phenomenon.unitShortcut + ']';
								this.lineChart.plot['value-box']['text'] = '%v'
										+ $scope.phenomenon.unitShortcut;
								this.lineChart['scale-y']['label']['text'] = $scope.phenomenon.name
										+ ' ['
										+ $scope.phenomenon.unitShortcut
										+ ']';
							}

							this.refreshBarSeries = function() {
								var dataPerStations = $scope.data.dataPerStations;
								var chartData = [];
								for (var i = 0; i < dataPerStations.length; i++) {
									var stationId = dataPerStations[i].stationId;
									var station = this
											.getStationById(stationId);
									var measurements = dataPerStations[i].measurements;
									var measurementsLength = measurements.length;

									if (station == null) {
										$log.error('Station with id '
												+ stationId + 'not found.');
										continue;
									}

									if (measurementsLength > 0) {
										var lastMeasurementIndex = measurementsLength - 1;
										var newSeries = {
											text : station.name,
											values : [ measurements[lastMeasurementIndex][1] ]
										}
										chartData.push(newSeries);
									}
								}

								this.barChart['series'] = chartData;
							}

							this.refreshBarLabels = function() {
								this.barChart.title.text = $scope.phenomenon.description;
								this.barChart['scale-y']['label']['text'] = $scope.phenomenon.name
										+ ' ['
										+ $scope.phenomenon.unitShortcut
										+ ']';
							}

							this.addStatistics = function() {
								$scope.stationStatistics = null;
								var series = this.lineChart['series'];
								var datesAndValues = series[0]['values'];

								if (series.length == 1) {
									var values = statisticsUtil
											.separateValues(datesAndValues);

									var mean = statisticsUtil.getMean(values);
									var median = statisticsUtil
											.getMedian(values);
									var stdDev = statisticsUtil
											.getStandardDeviation(values);

									this.addStatisticsMarkers(mean, median,
											stdDev)

									$scope.stationStatistics = {
										mean : Math.round(mean*100,2) / 100, 
										median : Math.round(median*100,2) / 100,
										standardDeviation : Math.round(stdDev*100,2) / 100
									}
								}
							}

							/**
							 * Function adds statistics markers (mean and median
							 * values) to line chart. Statistics are added only
							 * if only one station are selected (one series of
							 * data). This function is called during refreshing
							 * of line chart.
							 */
							this.addStatisticsMarkers = function(mean, median,
									stdDev) {
								var markers = [];

								markers.push(this.getMeanMarker(mean));
								markers.push(this.getMedianMarker(median));
								markers.push(this.getDeviationMarker(mean
										+ stdDev));
								markers.push(this.getDeviationMarker(mean
										- stdDev));

								this.lineChart['scale-y']['markers'] = markers;
							}

							this.getMeanMarker = function(mean) {
								var meanMarker = {
									type : "line",
									range : [ mean ],
									label : {
										text : "Mean",
										"color" : "red"
									},
									"line-color" : "red",
									alpha : 1,
									lineWidth : 2,
									lineStyle : "dashed"
								}
								return meanMarker;
							}

							this.getMedianMarker = function(median) {
								var medianMarker = {
									type : "line",
									range : [ median ],
									label : {
										text : "Median",
										"color" : "green",
										"offset-x" : "30px",
									},
									"line-color" : "green",
									alpha : 1,
									lineWidth : 2,
									lineStyle : "dotted"
								}
								return medianMarker;
							}

							this.getDeviationMarker = function(valueOnY) {
								var deviationMarker = {
									type : "line",
									range : [ valueOnY ],
									label : {
										text : "Std. dev.",
										"color" : "red",
										"offset-x" : "70px",
									},
									"line-color" : "red",
									alpha : 1,
									lineWidth : 2,
									lineStyle : "dotted"
								}
								return deviationMarker;
							}

							this.refresh();
						},
						link : function(scope, element, attr, ctrl) {
						}
					};
				});