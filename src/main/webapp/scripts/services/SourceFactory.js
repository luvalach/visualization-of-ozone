angular.module('visualizationofozone').factory('SourceResource', function($resource){
    var resource = $resource('rest/sources/:SourceId',{SourceId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});