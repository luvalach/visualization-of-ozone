/**
 * AngularJS default filter with the following expression:
 * "person in people | filter: {name: $select.search, age: $select.search}"
 * performs a AND between 'name: $select.search' and 'age: $select.search'.
 * We want to perform a OR.
 */
angular.module('visualizationofozone').filter('selectPropsFilter', function() {
  return function(items, props) {
    var out = [];

    if (angular.isArray(items)) {
      for (var i = 0; i < items.length; i++){
    	var item = items[i];
        var itemMatches = false;

        var keys = Object.keys(props);
        for (var k = 0; k < keys.length; k++) {
          var prop = keys[k];
          var text = props[prop].toLowerCase();
          
          if(text == ''){
        	  return items;
          }
          
          if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
            itemMatches = true;
            break;
          }
        }

        if (itemMatches && out.length < 20) {
          out.push(item);
        }
      };
    } else {
      // Let the output be the input untouched
      out = items;
    }

    return out;
  };
});