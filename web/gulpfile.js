var gulp = require('gulp'),
    util = require('gulp-util'),
    jshint = require('gulp-jshint'),
    uglify = require('gulp-uglify'),
    concat = require('gulp-concat'),
    addsrc = require('gulp-add-src');

gulp.task('default', function () {

    var srcDir = 'src/main/webapp/js';
    var depsDir = 'src/main/webapp/jspkg';

    gulp.src([
        srcDir + '/trenurbanoapp-util.js',
        srcDir + '/trenurbanoapp-schedule.js'
    ])
        .pipe(jshint({multistr: true}))
        .pipe(jshint.reporter('default'))
        .pipe(addsrc([
            depsDir + '/fastclick/lib/fastclick.js',
            depsDir + '/momentjs/moment.js'
        ]))
        .pipe(concat('tusched.min.js'))
        .pipe(uglify())
        .pipe(gulp.dest(srcDir));


    var map = gulp.src([
        srcDir + '/trenurbanoapp-util.js',
        srcDir + '/trenurbanoapp-map.js'
    ])
        .pipe(jshint({multistr: true}))
        .pipe(jshint.reporter('default'))
        .pipe(addsrc([
            depsDir + '/fastclick/lib/fastclick.js',
            depsDir + '/momentjs/moment.js',
            depsDir + '/leaflet-geocoder-mapzen/dist/leaflet-geocoder-mapzen.js',
            depsDir + '/Leaflet.TextPath/leaflet.textpath.js'
        ]))
        .pipe(concat('tumap.min.js'))
        .pipe(uglify())
        .pipe(gulp.dest(srcDir));
});
