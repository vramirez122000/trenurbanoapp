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

    /**/
    my.stopTimes = function(/*string*/ targetDivId, /*object*/ params, /*function*/ success) {

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
    my.nearestStation = function(/*function*/ success, /*function*/ error) {
        if (!!navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                    jQuery.get("../app/nearestStation", {
                        lat: position.coords.latitude,
                        lng: position.coords.longitude,
                        accuracy: position.coords.accuracy
                    }, function(data) {
                        console.log(String(data));
                        success(data, position);
                    });
                },
                function(err) {
                    console.error(err.code + ": " + err.message);
                    if(error) {
                        error(err);
                    }
                },
                { enableHighAccuracy: true }
            );
        }
    };

    /* returns a JSON representation of the nearest station */
    my.stopTimesByLocation = function(/*string*/ targetDivId, /*function*/ success, /*function*/ error) {
        if (!!navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                    my.stopTimes(targetDivId, {
                        lat: position.coords.latitude,
                        lng: position.coords.longitude,
                        accuracy: position.coords.accuracy
                    }, success);
                },
                function(err) {
                    console.error(err.code + ": " + err.message);
                    if(error) {
                        error(err);
                    }
                },
                { enableHighAccuracy: true }
            );
        }
    };

    my.nearbyRoutesWithoutSchedules = function(targetDivId, params) {
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

    return my;

})(TU.SCHED || {}, jQuery);


