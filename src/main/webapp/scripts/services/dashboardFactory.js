angular.module('visualizationofozone').factory('DashboardResource', function($resource){
    var resource = $resource('rest/dashboards/',{},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:true},'update':{method:'PUT'}});
    return resource;
});