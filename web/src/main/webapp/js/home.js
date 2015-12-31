var clockIntervalId;
var currPosition;

function main() {

    if(TU.SCHED.isIOS9WebView()) {
        var $alertMsg = $('#alertMsg');
        $alertMsg.find('#alertTxt').html('Si tiene problemas con Tren Urbano App en iOS 9, ' +
            'favor de visitar <a href="http://trenurbanoapp.com" target="_blank">Tren Urbano App Web</a> y ' +
            'seleccionar la opci&oacute;n "Add to Home Screen"');
        $alertMsg.css('display', 'block');
    }

    TU.SCHED.stopTimesByLocation('#stopTimes', function(params) {
        var resultsStation = $('input#resultsStation').val();
        var stationSelect = $(".station-select:visible");
        stationSelect.val(resultsStation);

        if(gpsEnabled) {
            TU.SCHED.nearbyEtas('#nearbyEtas', params)
        }

        TU.SCHED.nearbyRoutesWithoutSchedules('#nearbyRoutesWithoutSchedules', params)

    }, function() {
        var stationSelect = $('.station-select:visible');
        var station = stationSelect.val();
        if (station != null && jQuery.trim(station) != "") {
            TU.SCHED.stopTimes('#stopTimes', {
                station: station
            });
        }
    });

    var $stationSelect = $('.station-select:visible');
    $stationSelect.change(function () {

        var $btn = $(".clock:visible button");
        var isClockActive = $btn.hasClass('active');
        var time = null;
        if (!isClockActive) {
            time = getInputTime();
        }

        var selectedStation = $(this).val();
        TU.SCHED.stopTimes('#stopTimes', {
            station: selectedStation,
            time: time
        });
    });



    clockIntervalId = startClock();

    $(".clock:visible input[type=time]").change(function(eventObj) {

        if(!eventObj.target.value) {
            return;
        }

        var station = $stationSelect.val();
        if(!station) {
            return;
        }

        var time = getInputTime();
        TU.SCHED.stopTimes('#nextArrivals', {
            station: station,
            time: time
        });
    });

    $('.itinerario-link').addClass('active');
    $(function() {
        FastClick.attach(document.body);
    });
}


function getInputTime() {
    var $timeInput = $('.clock:visible input[type=time]');
    if($timeInput.prop("type") == "time" && jQuery.trim($timeInput.val()).length > 0) {
        return $timeInput.val();
    }

    //force strict parsing
    var timeMoment = moment($timeInput.val(), 'h:mm A', true);
    var $clockGroup = $('.clock:visible');
    if(timeMoment.isValid()) {
        var time = timeMoment.format('HH:mm:ss');
        $clockGroup.removeClass("has-error");
        return time;
    }

    $clockGroup.addClass("has-error");
    return null;

}

function findNearestStation() {
    TU.SCHED.nearestStation(function (data, position) {
        currPosition = position;
        var stationSelect = $(".station-select:visible");
        stationSelect.val(data);
        stationSelect.change();

    }, function(err) {
        var $alertMsg = $('#alertMsg');
        $alertMsg.find('#alertTxt').text('Servicios de localización deshabilitados');
        $alertMsg.toggle();
    });
}

function toggleClock() {

    $(".clock:visible input[type=text]").toggle();
    var $clockTimeInput = $(".clock:visible input[type=time]");
    $clockTimeInput.toggle();

    var $clockLabel = $('.clock:visible label');
    var $btn = $(".clock:visible button");
    var isClockActive = $btn.hasClass('active');
    if (!isClockActive) {
        $btn.addClass('active');
        $clockLabel.text('Hora Actual');
        clockIntervalId = startClock();
    } else {
        $clockLabel.text('Escoja la hora (e.g. 10:45 AM)');
        clearInterval(clockIntervalId);
        $btn.removeClass('active');
        $clockTimeInput.focus();
    }
}

function startClock() {
    $(".clock:visible input[type=text]").val(moment().format("h:mm:ss A"));
    return setInterval(updateClocks, 5000);
}

function updateClocks() {
    var now = moment();
    $(".clock:visible input[type=text]").val(now.format("h:mm:ss A"));
    $('.etaDiv').each(function() {
        var $this = $(this);
        var stopTime = moment(parseInt($this.find('.stopTime').text()));
        var duration = moment.duration(stopTime.valueOf() - now.valueOf(), 'milliseconds');
        $this.find('.etaString').text((Math.abs(duration.hours()) > 0 && (duration.hours() + ' hr   ')) + duration.minutes() + ' min ' + duration.seconds() + ' s')
    });
}
