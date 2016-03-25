angular.module('visualizationofozone').factory('FileResource', function($resource){
    var resource = $resource('rest/files/:FileId',{FileId:'@fileName'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});