/*requires jQuery, LeafletJS */
var TU = TU || {};
TU.EDITOR = (function (my, $, Leaf) {
    "use strict";

    my.DEFAULTS = {
        CENTER: [18.430189, -66.060061],
        ZOOM: 13,
        BOUNDS: [[18.352687, -66.179752], [18.477284, -65.928097]]
    };


    my.main = function (options) {
        var map = Leaf.map(options.mapDiv, {
            center: TU.EDITOR.DEFAULTS.CENTER,
            zoom: TU.EDITOR.DEFAULTS.ZOOM
        });

        var url = ' http://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}@2x.png'; //cartodb
        L.tileLayer(url, {
            drawControl: true,
            minZoom: 10,
            maxZoom: 18,
            maxBounds: TU.EDITOR.DEFAULTS.BOUNDS,
            attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, ' +
            '&copy; <a href="http://cartodb.com/attributions">CartoDB</a>',
        }).addTo(map);

        // Initialise the draw control and pass it the FeatureGroup of editable layers
        $.ajax(options.contextRoot + '/rest/subroutes/' + getParam('route'), {
            contentType: 'application/json; charset=UTF-8',
            dataType: 'json',
            success: function (subroutesGeojson) {
                var subroutes = L.geoJson(subroutesGeojson, {
                    style: function (feature) {
                        return {color: feature.properties.color};
                    },
                    onEachFeature: function (feature, layer) {
                        layer.bindPopup(feature.properties.name + ' a ' + feature.properties.dest);
                    }
                }).addTo(map);

                subroutes.addTo(map);

                var drawControl = new L.Control.Draw({
                    edit: { featureGroup: subroutes }
                });
                map.addControl(drawControl);

                map.on('draw:edited', function (e) {
                    console.log(JSON.stringify(e));
                })

            }
        });

    };

    function getParam(name) {
        var paramKeyValPair = (new RegExp('[?&]' + encodeURIComponent(name) + '=([^&]*)')).exec(location.search);
        if (paramKeyValPair) {
            var decoded = decodeURIComponent(paramKeyValPair[1]);
            if (decoded.indexOf('/') == (decoded.length - 1)) {
                decoded = decoded.substring(0, decoded.length - 1);
            }
            return decoded;
        }
    }

    return my;

}(TU.EDITOR || {}, jQuery, L));
