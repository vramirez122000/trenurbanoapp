module.exports = function(grunt) {

    var srcDir = 'src/main/webapp/js';

    // Project configuration.
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        srcDir: 'src/main/webapp/js',
        depsDir: 'src/main/webapp/jspkg',
        jshint: {
            files: [
                'Gruntfile.js',
                '<%= srcDir %>/trenurbanoapp-schedule.js',
                '<%= srcDir %>/trenurbanoapp-map.js',
                '<%= srcDir %>/trenurbanoapp-util.js'
            ],
            options: {
                multistr: true
            }
        },
        concat: {
            schedule: {
                src: [
                    '<%= depsDir %>/fastclick/lib/fastclick.js',
                    '<%= depsDir %>/momentjs/moment.js',
                    '<%= srcDir %>/trenurbanoapp-util.js',
                    '<%= srcDir %>/trenurbanoapp-schedule.js'
                ],
                dest: '<%= srcDir %>/trenurbanoapp-schedule.concat.js'
            },
            map: {
                src: [
                    '<%= srcDir %>/trenurbanoapp-util.js',
                    '<%= srcDir %>/trenurbanoapp-map.js'
                ],
                dest: '<%= srcDir %>/trenurbanoapp-map.concat.js'
            }

        },
        uglify: {
            build: {
                files: [{
                    src: '<%= srcDir %>/trenurbanoapp-schedule.concat.js',
                    dest: '<%= srcDir %>/tusched.min.js'
                },{
                    src: '<%= srcDir %>/trenurbanoapp-map.concat.js',
                    dest: '<%= srcDir %>/tumap.min.js'
                }]
            }
        }
    });

    // Load the plugin that provides the "uglify" task.
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');

    // Default task(s).
    grunt.registerTask('default', ['jshint','concat','uglify']);

};