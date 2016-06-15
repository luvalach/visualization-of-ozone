'use strict';

angular.module('visualizationofozone').factory('unsupportedBrowserDetector',
		[ '$rootScope', function($rootScope) {
			return {
				isBrowserDeprecated: function() {
				    var ua = window.navigator.userAgent;

				    var msie = ua.indexOf('MSIE ');
				    if (msie > 0) {
				        // IE 10 or older => return version number
				    	return true;
				    }

				    var trident = ua.indexOf('Trident/');
				    if (trident > 0) {
				        // IE 11 => return version number
				    	return true;
				    }

				    var edge = ua.indexOf('Edge/');
				    if (edge > 0) {
				       // Edge (IE 12+) => return version number
				    	return true;
				    }

				    // other browser
				    return false;
				}
			};
		} ]);
