/* requires jquery, momentjs */
var TU = TU || {};
TU.SCHED = (function(my, $) {
    "use strict";

    var _errorTemplate =
        '<div>\
    <span class="ui-icon ui-icon-alert" style="float:left; margin: 0 7px 20px 0;"></span>\
    <h1>Error en Aplicaci&oacute;n:</h1>\
    <p id="ajaxErrorStatus" style="margin-left:22px;"></p>\
    <p id="ajaxErrorUrl" style="margin-left:22px;"></p>\
    <p id="ajaxErrorHttpStatus" style="margin-left:22px;"></p>\
    <p id="ajaxErrorStackTrace"></p>\
</div>';

    var _progressImg =
        '<div style="height: 3em; text-align: center">' +
            '<img id="progressImg" src="../images/progress.gif" alt="progress image"/>' +
            '</div>';

    /* return HTML Table of stop times*/
    my.getStopTimes = function(/*string*/ targetDivId, /*object*/ params, /*function*/ success) {

        var targetDiv = $(targetDivId);

        $.ajax({
            url: '../app/stopTimes',
            cache: false,
            data: params,
            beforeSend: function() {
                targetDiv.html(_progressImg);
            },
            success: function(data) {
                targetDiv.html(data);
                if(success) {
                    success(params);
                }
            },
            error: function(xhr) {
                targetDiv.html(_errorTemplate);
                var stackTrace = "";
                $("pre", xhr.responseXML).each(function() {
                    stackTrace += this;
                });
                targetDiv.find("#ajaxErrorStackTrace").html(stackTrace);
            },
            complete: function() {
                $("#progressImg").hide();
            }
        });
    };

    /* returns a JSON representation of the nearest station */
    my.getGeolocation = function(/*function*/ success, /*function*/ error) {
        if (!navigator.geolocation) {
            console.warn('Geolocation not available');
            return;
        }

        navigator.geolocation.getCurrentPosition(function (position) {
                var location = {
                    lat: position.coords.latitude,
                    lng: position.coords.longitude,
                    accuracy: position.coords.accuracy
                };

                console.log(JSON.stringify(position));
                success(location);
            },
            function (err) {
                console.error(err.code + ": " + err.message);
                if (error) {
                    error(err);
                }
            },
            {enableHighAccuracy: true}
        );

    };

    /* returns a JSON representation of the nearest station */
    my.getNearestStopArea = function(location, success) {
        jQuery.getJSON('../app/nearestStation', location, function (data) {
            //console.log(String(data));
            success(data);
        });
    };

    /* returns a JSON representation of the nearest station */
    my.getStopAreasByDistance = function(location, success) {
        jQuery.getJSON('../app/stopAreasByDistance', location, function (data) {
            //console.log(String(data));
            success(data);
        });
    };

    my.getNearbyRoutesWithoutSchedules = function(targetDivId, params) {
        var targetDiv = $(targetDivId);
        $.ajax({
            url: '../app/nearbyRoutesWithoutSchedules',
            cache: false,
            data: params,
            beforeSend: function() {
                targetDiv.html(_progressImg);
            },
            success: function(data) {
                targetDiv.html(data);
            },
            error: function(xhr) {
                targetDiv.html(_errorTemplate);
                var stackTrace = "";
                $("pre", xhr.responseXML).each(function() {
                    stackTrace += this;
                });
                targetDiv.find("#ajaxErrorStackTrace").html(stackTrace);
            },
            complete: function() {
                $("#progressImg").hide();
            }
        });
    };

    my.getNearbyEtas = function(targetDivId, params) {
        var targetDiv = $(targetDivId);
        $.ajax({
            url: '../app/nearbyEtas',
            cache: false,
            data: params,
            beforeSend: function() {
                targetDiv.html(_progressImg);
            },
            success: function(data) {
                targetDiv.html(data);
            },
            error: function(xhr) {
                targetDiv.html(_errorTemplate);
                var stackTrace = "";
                $("pre", xhr.responseXML).each(function() {
                    stackTrace += this;
                });
                targetDiv.find("#ajaxErrorStackTrace").html(stackTrace);
            },
            complete: function() {
                $("#progressImg").hide();
            }
        });
    };

    var safariPattern = /safari/i;
    var iosMajorVersPattern = /(iphone|ipod|ipad); CPU OS (\d+)_/i;

    my.isIOS9WebView = function() {
        var standalone = 'standalone' in window.navigator && window.navigator.standalone;
        if(standalone) {
            return false;
        }

        var userAgent = window.navigator.userAgent;
        var iosMajorVers = iosMajorVersPattern.exec(userAgent);
        if(!iosMajorVers) {
            return false;
        }

        if(Number.valueOf(iosMajorVers) < 9) {
            return false;
        }

        return !safariPattern.test(userAgent);

    };

    return my;

})(TU.SCHED || {}, jQuery);


