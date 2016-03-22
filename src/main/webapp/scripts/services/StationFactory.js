angular.module('visualizationofozone').factory('StationResource', function($resource){
    var resource = $resource('rest/stations/:StationId',{StationId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});