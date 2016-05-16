angular.module('visualizationofozone').factory('PhenomenonTypeResource', function($resource){
    var resource = $resource('rest/phenomenontypes/:PhenomenonTypeId',{PhenomenonTypeId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});