/**
 * Created by victor on 5/29/14.
 */
var TU = TU || {};
TU.UTIL = (function(my, $) {

    my.shadeColor = function(color, percent) {
        var num = parseInt(color.slice(1),16);
        var amt = Math.round(2.55 * percent);
        var R = (num >> 16) + amt;
        var B = (num >> 8 & 0x00FF) + amt;
        var G = (num & 0x0000FF) + amt;
        return "#" + (0x1000000 + (R<255?R<1?0:R:255)*0x10000 + (B<255?B<1?0:B:255)*0x100 + (G<255?G<1?0:G:255))
            .toString(16).slice(1);
    };

    my.leftPadUpToFour = function(num, size) {
        var s = "0000" + num;
        return s.substr(s.length-size);
    };

    my.intervalInMinutes = function(currentTimestamp, olderTimestamp) {
        var safeCurrTimestamp = currentTimestamp || new Date().getTime();
        return ((safeCurrTimestamp - olderTimestamp) / 60000).toFixed(0);
    };

    return my;

})(TU.UTIL || {}, jQuery);