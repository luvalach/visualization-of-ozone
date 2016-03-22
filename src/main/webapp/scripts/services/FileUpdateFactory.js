angular.module('visualizationofozone').factory('FileUpdateResource', function($resource){
    var resource = $resource('rest/fileupdates/:FileUpdateId',{FileUpdateId:'@sourceId'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});