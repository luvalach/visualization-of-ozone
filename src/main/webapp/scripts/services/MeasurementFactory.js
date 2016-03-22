angular.module('visualizationofozone').factory('MeasurementResource', function($resource){
    var resource = $resource('rest/measurements/:StationId/:PhenomenonTypeId/:DateTime',{StationId:'@stationId',PhenomenonTypeId:'@phenomenonTypeId',DateTime:'@dateTime'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});