"use strict";

angular.module('visualizationofozone').directive(
		'cesiumDirective',
		function($interval) {
			return {
				restrict : "EA",
				controllerAs : "cesiumCtrl",
				scope : {
					stationList : "=",
					searchStations : "=",
					searchDate : "=",
					onDoubleClick : "&",
					onClickWithCtrl : "&",
					stationData : "="
				},
				controller : function($scope) {
					this.refreshSelection = function(newValue, oldValue) {						
						Array.prototype.diff = function(a) {
						    return this.filter(function(i) {return a.indexOf(i) < 0;});
						};
						
						var added = newValue.diff(oldValue);
						var removed = oldValue.diff(newValue);
						
						for (var i = 0; i < removed.length; i++) {
							var entity = this.cesium.entities.getById(removed[i])
							if (typeof entity === 'undefined' ){
								continue;
							}
							entity.point.outlineColor = this.defaultOutlineColor;
							entity.point.outlineWidth = this.defaultOutlineWidth;
						}
						
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
					
					this.refreshStationData = function(newValue, oldValue){
						var stationData = $scope.stationData;
						var entities =  this.cesium.entities.values;

						for (var i = 0; i < entities.length; i++) {
							var entity = entities[i];
							if (typeof entity.point === 'undefined' ){
								continue;
							}
							entity.point.color = this.defaultColor;
							entity.point.pixelSize = this.defaultPixelSize;
						}
						
						for (var i = 0; i < stationData.length; i++) {
							var entity = this.cesium.entities.getById(stationData[i].id)
							if (typeof entity === 'undefined' ){
								continue;
							}
							
							var measures = stationData[i].measures;
							
							if (measures.length > 0){
								var lastMeasure = stationData[i].measures[measures.length - 1];
								var ozone = lastMeasure[1];
								entity.point.color = this.valueToColor(ozone);
								entity.point.pixelSize = this.downloadedPixelSize;
							}
						}
					}
					
					this.valueToColor = function(value){
				        var frequency = .015;
				        var i = value + 100;
				        
						//      Math.round(Math.sin(frequency*increment)*amplitude + center);
						var red   = Math.round(Math.sin(frequency*i + 0) * 127 + 128);
						var green = Math.round(Math.sin(frequency*i + 2) * 127 + 128);
						var blue  = Math.round(Math.sin(frequency*i + 4) * 127 + 128);
						
						return new Cesium.Color.fromBytes(red, green, blue, 255);
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
					ctrl.defaultOutlineWidth = 2;
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
							var longitude = scope.stationList[i].longitude;
							var latitude = scope.stationList[i].latitude;
							
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
										description : '<p>Longitude: '
												+ longitude + '<br/>Latitude: '
												+ latitude + '</p>',
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