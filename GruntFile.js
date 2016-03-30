module.exports = function( grunt ){

   // tell grunt to load jshint task plugin.
   grunt.loadNpmTasks('grunt-contrib-jshint');

   // configure tasks
   grunt.initConfig({
      jshint: {
          files: [
             'GruntFile.js'
             //'src/main/webapp/**/*.js'
          ],
          options: {
             ignores: [
                'src/main/webapp/scripts/vendor/**/*.js',
                'src/main/webapp/bower_components/**/*.js'
             ]
          }
      }
      // more plugin configs go here.
   });

   grunt.registerTask('default',['jshint']);

};