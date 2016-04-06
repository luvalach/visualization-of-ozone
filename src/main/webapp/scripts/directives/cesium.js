"use strict";

angular.module('visualizationofozone').directive(
		'cesiumDirective',
		function($interval, $filter, geocoordinateFilter) {
			return {
				restrict : "EA",
				controllerAs : "cesiumCtrl",
				scope : {
					stationData : "=",
					stationList : "=",
					searchStations : "=",
					searchDate : "=",
					onDoubleClick : "&",
					onClickWithCtrl : "&",
					getPhenomenonById : "&"					
				},
				controller : function($scope) {
					/*
					 * Refresh visualization of selected stations and fly to last selected station.
					 * This function sets points outline color and width.
					 */
					this.refreshSelection = function(newValue, oldValue) {						
						Array.prototype.diff = function(a) {
						    return this.filter(function(i) {return a.indexOf(i) < 0;});
						};
						
						//Stations added and removed from selection
						var added = newValue.diff(oldValue);
						var removed = oldValue.diff(newValue);
						
						//Set default outline on unselected stations
						for (var i = 0; i < removed.length; i++) {
							var entity = this.cesium.entities.getById(removed[i])
							if (typeof entity === 'undefined' ){
								continue;
							}
							entity.point.outlineColor = this.defaultOutlineColor;
							entity.point.outlineWidth = this.defaultOutlineWidth;
						}
						
						//Set selected-style outline to selected stations and fly camera to last selected station.
						for (var i = 0; i < added.length; i++) {
							var entity = this.cesium.entities.getById(added[i])
							if (typeof entity === 'undefined' ){
								continue;
							}
							entity.point.outlineColor = this.selectedOutlineColor;
							entity.point.outlineWidth = this.selectedOutlineWidth;
							
							if (i == added.length - 1){
								this.cesium.selectedEntity = entity;
								
								if (typeof entity.longitude === 'undefined' ){
									continue;
								}
								this.cesium.camera.flyTo({
									destination : Cesium.Cartesian3
									.fromDegrees(entity.longitude,
											entity.latitude, 10000000),
								});
							}
						}
					}
					
					/**
					 * Set points inner color and size. 
					 * Points of stations which data are known will be bigger and 
					 * inner color is calculated according to measurement value.
					 * 
					 * Globe should display last value in first chart for each station.
					 */
					this.refreshStationData = function(newValue, oldValue){
						var entities =  this.cesium.entities.values;
						
						//Clean all stations - set default to all points
						for (var i = 0; i < entities.length; i++) {
							var entity = entities[i];
							if (typeof entity.point === 'undefined' ){
								continue;
							}
							entity.point.color = this.defaultColor;
							entity.point.pixelSize = this.defaultPixelSize;
							entity.description = this.assemblyStationDescription(entity);
						}
						
						//Set variables for later usage or return if data aren't available
						if ($scope.stationData.length > 0){
							var phenomenonData = $scope.stationData[0];
							var phenomenonType = $scope.getPhenomenonById({id: phenomenonData.phenomenonTypeId});
							var dataPerStations = phenomenonData.dataPerStations;
						} else {
							//No data found
							return;
						}
						
						//For each station which data are available set color and size.
						for (var i = 0; i < dataPerStations.length; i++) {
							var entity = this.cesium.entities.getById(dataPerStations[i].stationId)
							if (typeof entity === 'undefined' ){
								continue;
							}
							
							var measures = dataPerStations[i].measurements;
							
							if (measures.length > 0){
								var lastMeasure = measures[measures.length - 1];
								var measurementDate = lastMeasure[0];
								var measurementValue = lastMeasure[1];
								entity.point.color = this.valueToColor(measurementValue);
								entity.point.pixelSize = this.downloadedPixelSize;
								entity.description = this.assemblyStationDescription(entity, phenomenonType, measurementDate, measurementValue);
							}
						}
					}
					
					/**
					 * Convert measurement value to Cesium.Color
					 */
					this.valueToColor = function(value){
				        var frequency = .015;
				        var i = value + 100;
				        
						//      Math.round(Math.sin(frequency*increment)*amplitude + center);
						var red   = Math.round(Math.sin(frequency*i + 0) * 127 + 128);
						var green = Math.round(Math.sin(frequency*i + 2) * 127 + 128);
						var blue  = Math.round(Math.sin(frequency*i + 4) * 127 + 128);
						
						return new Cesium.Color.fromBytes(red, green, blue, 255);
					}
					
					/**
					 * Return description as string/html. 
					 * Entity is mandatory parameter, others can be undefined.
					 */
					this.assemblyStationDescription = function(entity, phenomenonType, measurementDate, measurementValue) {
						var description = '';
						
						if (typeof entity.country !== 'undefined' ){
							description += 'Country: ' + entity.country + '</br>';
						}
						
						if (typeof measurementDate === 'undefined' ){
							var measurementDate = 'unknown'
						}
						
						description += 'Longitude: ' + geocoordinateFilter.longitude(entity.longitude) + '</br>';
						description += 'Latitude: ' + geocoordinateFilter.latitude(entity.latitude) + '</br>';
						description += 'Last update: ' + $filter('date')(entity.lastUpdate, 'yyyy-MM-dd HH:mm:ss Z') + '</br>';
						description += '</br>';
						
						//If phenomenonType is undefined then add warding message instead of measurement data.
						if (typeof phenomenonType === 'undefined'){
							description += '<b>Data for this station are unavailable, try to modify filter setting.</b>';
						} else {
							description += '<b>Displayed measurement</b></br>';
							description += 'Phenomenon: ' + phenomenonType.name + '</br>';
							description += 'Date: ' + $filter('date')(measurementDate, 'yyyy-MM-dd HH:mm:ss Z') + '</br>';
							description += 'Value: ' + measurementValue + ' [' + phenomenonType.unitShortcut + ']</br>';
						}
						
						//Hack inserting picture of Mendel station
						if (entity.name.match(/mendel/gi)){
							description += '</br>';
							description += '<img class="hidden-xs img-responsive" src="img/mendel_station1.jpeg" width="100%" alt="Ozone visualization"></img></br>';
						}
						
						return description;
					}
					
//					Sandcastle.addToolbarMenu([{
//					    text : 'Add billboard',
//					    onselect : function() {
//					        //addBillboard();
//					        //Sandcastle.highlight(addBillboard);
//					    }}]);
				},
				link : function(scope, element, attr, ctrl) {
					ctrl.defaultPixelSize = 5;
					ctrl.defaultColor = Cesium.Color.SILVER;
					ctrl.defaultOutlineWidth = 0;
					ctrl.defaultOutlineColor = Cesium.Color.WHITE;
					
					ctrl.downloadedPixelSize = 10;
					
					ctrl.selectedOutlineWidth = 4;
					ctrl.selectedOutlineColor = Cesium.Color.YELLOW;
					
					

					ctrl.cesium = new Cesium.Viewer(element[0], {
						baseLayerPicker : true,
						// Switching map source (Bing, OpenStreet etc.)
						fullscreenButton : false,
						homeButton : true,
						sceneModePicker : true,
						// Switching between map and globe
						selectionIndicator : true,
						timeline : false,
						animation : false,
						geocoder : true
					// Searbox
					});
					
					var scene = ctrl.cesium.scene;
					var handler = new Cesium.ScreenSpaceEventHandler(scene.canvas);
					
					//ctrl.cesium.fullscreenButton.viewModel.fullscreenElement = scene.canvas;
					
					ctrl.cesium.screenSpaceEventHandler.removeInputAction(Cesium.ScreenSpaceEventType.LEFT_DOUBLE_CLICK);

					handler.setInputAction(function(click) {
					    var pickedObject = scene.pick(click.position);
					    if (Cesium.defined(pickedObject) && pickedObject.id.hasOwnProperty('objectType') && pickedObject.id.objectType == 'station') {
					    	scope.$apply(function(){
					    		scope.onDoubleClick({entity: pickedObject.id});
					    	});
					    }
					}, Cesium.ScreenSpaceEventType.LEFT_DOUBLE_CLICK);
					
					handler.setInputAction(function(click) {
					    var pickedObject = scene.pick(click.position);
					    if (Cesium.defined(pickedObject) && pickedObject.id.hasOwnProperty('objectType') && pickedObject.id.objectType == 'station') {
					    	scope.$apply(function(){
					    		scope.onClickWithCtrl({entity: pickedObject.id});
					    		ctrl.cesium.selectedEntity = pickedObject.id;
					    	});
					    }
					}, Cesium.ScreenSpaceEventType.LEFT_CLICK, Cesium.KeyboardEventModifier.CTRL);
					
					scope.$watch('stationList', function(newValue, oldValue) {
						ctrl.cesium.entities.removeAll();

						var arrayLength = scope.stationList.length;

						for (var i = 0; i < arrayLength; i++) {
							var id = scope.stationList[i].id;
							var name = scope.stationList[i].name;
							var country = scope.stationList[i].country;
							var longitude = scope.stationList[i].longitude;
							var latitude = scope.stationList[i].latitude;
							var lastUpdate = scope.stationList[i].lastUpdate;
							
							if (typeof longitude === 'undefined' ){
								continue;
							}

							ctrl.cesium.entities
									.add({
										id : id,
										position : Cesium.Cartesian3
												.fromDegrees(longitude,
														latitude),
										name : name,
										country : country,
										lastUpdate : lastUpdate,
										point : {
											pixelSize : ctrl.defaultPixelSize,
											color : ctrl.defaultColor,
											outlineColor : ctrl.defaultOutlineColor,
											outlineWidth : ctrl.defaultOutlineWidth
										},
										objectType : 'station',
										longitude : longitude,
										latitude : latitude
									});
						}
					});
					
					scope.$watchCollection('searchStations', function(newValue, oldValue) {
						ctrl.refreshSelection(newValue, oldValue);
					});
					
					scope.$watchCollection('stationData', function(newValue, oldValue) {
						ctrl.refreshStationData(newValue, oldValue);
					});

//					ctrl.cesium.camera.flyTo({
//						destination : new Cesium.Cartesian3.fromDegrees(-57.88,
//								-63.8, 10000000)
//					});
				}
			};
		});