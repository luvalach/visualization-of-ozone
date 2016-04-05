"use strict";

angular.module('visualizationofozone').directive(
		'chartDirective',
		function($interval, $log) {
			return {
				restrict : "EA",
				controllerAs : "chartCtrl",
				scope : {
					data : "=",
					stationList : "=",
					phenomenon : "="
				},
				template: '<zingchart id="chart-{{phenomenon.id}}" zc-json="chart" zc-render="myRender" zc-width="100%"></zingchart>', //<pre ng-bind="chart | json"></pre>
				controller : function($scope) {
					this.lineChart = {
							type : 'line',
							title : {
						        "text":""
						    },
							plot : {
								aspect : 'spline',
								"tooltip" : {
									"text" : "Date: %k <br/> Value: %vt",
									"placement" : "node:top"
								},
								"value-box" : {
									"visible" : "true",
									"text" : "%v DU",
									"type" : "min,max"
								}
							},
							"legend" : {
								"max-items" : 4,
								"overflow" : "page",
								"highlight-plot" : true,
								"tooltip" : {
									"text" : "Click to show/hide series."
								}
							},
							"scale-y" : {
								"label" : {
									"text" : "Total Ozone [DU]"
								}
							},
							"scale-x" : {
								"label" : {
									"text" : "Time",
								},
								"transform" : {
//									"all":"%m.%d.%Y",
									"type" : "date"
								},
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
								plot : {
									aspect : 'spline',
									"tooltip" : {
										"text" : "Station: %t <br/>Total Ozone: %vt",
										"placement" : "node:top"
									},
									"animation" : {
										"effect" : "4",
										"method" : "1",
										"sequence" : "1",
										"speed" : 500
									},
								},
								"legend" : {
									"max-items" : 8,
									"overflow" : "page",
									"highlight-plot" : true,
									"tooltip" : {
										"text" : "Click to show/hide series."
									}
								},
								"scale-y" : {
									"label" : {
										"text" : "Total Ozone [DU]"
									},
									"min-value" : 0,
									"max-value" : 500
								},
								"scale-x" : {
									"label" : {
										"text" : "Stations",
									}
								},
								"scroll-x" : {},
								height : "100%",
								width : "100%",
								series : []
							};
						
						$scope.chart = this.lineChart;

						$scope.myRender = {
							output : "auto",
							events : {
								complete : function(p) {
									console.log("Chart is finished!");
								}
							}
						}
						
						this.getStationById = function(id){
							for (var i = 0; i < $scope.stationList.length; i++) {
								if ($scope.stationList[i].id == id){
									return $scope.stationList[i];
								}
							}
							return null;
						}
						
						this.refresh = function(){
							this.refreshSeries();
							this.refreshLabels();
						}
						
						this.refreshSeries = function(){
							var dataPerStations = $scope.data.dataPerStations;
							var lineChartData = [];
							for (var i = 0; i < dataPerStations.length; i++) {
								var stationId = dataPerStations[i].stationId;
								var station = this.getStationById(stationId);
								if (station != null) {
									var newSeries = {
											text: station.name,
											values: dataPerStations[i].measurements
									}
									lineChartData.push(newSeries);
								} else {
									$log.error('Station with id ' + stationId + 'not found.')
								}
							}
							
							$scope.chart['series'] = lineChartData;
						}
						
						this.refreshLabels = function(){
							$scope.chart.title.text = $scope.phenomenon.description;
							$scope.chart.plot['value-box']['text'] = '%v '+ $scope.phenomenon.unitShortcut;
							$scope.chart['scale-y']['label']['text'] = $scope.phenomenon.name + ' [' + $scope.phenomenon.unitShortcut + ']';
						}
						
						this.refresh();
				},
				link : function(scope, element, attr, ctrl) {
				}
			};
		});