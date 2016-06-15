'use strict';

angular.module('visualizationofozone').factory('flash',
		[ '$rootScope', function($rootScope) {
			var messages = [];

			$rootScope.$on('$routeChangeSuccess', function() {
				messages = [];
			});

			return {
				getMessages : function() {
					return messages;
				},
				closeMessage : function(message) {
					var index = messages.indexOf(message);
					
					if (index > -1) {
						messages.splice(index, 1);
					}
			    },
				cleanMessages : function() {
					for (var i = 0; i < messages.length; i++){
						if (messages[i].hasOwnProperty('durable') && messages[i].durable){
							continue;
						} else {
							messages.splice(i, 1);
							i--;
						}
					}
				},
				setMessage : function(message) {
					var messageType = message.type.toUpperCase();
					switch (message.type) {
					case "ERROR":
						message.cssClass = "danger";
						break;
					case "SUCCESS":
						message.cssClass = "success";
						break;
					case "INFO":
						message.cssClass = "info";
						break;
					case "WARNING":
						message.cssClass = "warning";
						break;
					}
					
					messages.push(message);
				}
			};
		} ]);
