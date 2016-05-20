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
						template : '<span class="pull-right glyphicon glyphicon-move sortable-handle"></span> <zingchart id="chart-{{phenomenon.id}}" zc-json="chart" zc-render="myRender" zc-width="100%"></zingchart>',
						controller : function($scope) {
							/**
							 * Limit of stations for line chart. If limit is
							 * exceeded the bar chart is shown instead of line
							 * chart.
							 */
							const
							LINE_CHART_LIMIT = 10;

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
									"markers" : []
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
									"draggable" : true,
									"tooltip" : {
										"text" : "Click to show/hide series."
									}
								},
								"scale-y" : {
									"label" : {
										"text" : ""
									}
								},
								"scale-x" : {
									"label" : {
										"text" : "Stations",
									},
									"show-labels" : []
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
								if ($scope.data.dataPerStations.length <= LINE_CHART_LIMIT) {
									this.refreshLineSeries();
									this.refreshLineLabels();
									this.addStatisticsMarkers();
									$scope.chart = this.lineChart;
								} else {
									this.refreshBarSeries();
									this.refreshBarLabels();
									$scope.chart = this.barChart;
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
									+ $scope.phenomenon.unitShortcut;
								this.lineChart.plot['value-box']['text'] = '%v '
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

							/**
							 * Function adds statistics markers (mean and median
							 * values) to line chart. Statistics are added only
							 * if only one station are selected (one series of
							 * data). This function is called during refreshing
							 * of line chart.
							 */
							this.addStatisticsMarkers = function() {
								var series = this.lineChart['series'];
								var values = series[0]['values'];
								if (series.length == 1) {
									var markers = [];

									markers.push(this.getMeanMarker(values));
									markers.push(this.getMedianMarker(values));

									this.lineChart['scale-y']['markers'] = markers;
								}
							}

							this.getMeanMarker = function(values) {
								var meanMarker = {
									type : "line",
									range : [ statisticsUtil.getMean(values) ],
									label : {
										text : "Mean",
										"color" : "red"
									},
									"line-color" : "red",
									alpha : 0.5,
									lineWidth : 2,
									lineStyle : "dashed"
								}
								return meanMarker;
							}

							this.getMedianMarker = function(values) {
								var medianMarker = {
									type : "line",
									range : [ statisticsUtil.getMedian(values) ],
									label : {
										text : "Median",
										"color" : "blue",
										"offset-x" : "30px",
									},
									"line-color" : "blue",
									alpha : 0.5,
									lineWidth : 2,
									lineStyle : "dotted"
								}
								return medianMarker;
							}

							this.refresh();
						},
						link : function(scope, element, attr, ctrl) {
						}
					};
				});