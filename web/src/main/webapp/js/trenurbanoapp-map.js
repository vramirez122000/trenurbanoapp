/*requires jQuery, LeafletJS, Leaflet.TextPath */
var TU = TU || {};
TU.MAP = (function(my, $, Leaf) {
    "use strict";

    //Constants

    my.DEFAULTS = {
        CENTER: [18.430189, -66.060061],
        ZOOM: 13,
        BOUNDS: [[18.352687, -66.179752],[18.477284, -65.928097]]
    };

    //private fields
    var VehicleIcon = Leaf.Icon.extend({
        options: {
            iconSize: [21, 33],
            iconAnchor: [11, 33],
            popupAnchor: [1, -27],
            shadowSize: [41, 41],
            shadowAnchor: [12, 41]
        }
    });

    var StopMarker = Leaf.CircleMarker.extend({
        options: {
            radius: +4,
            fillColor: "#eeeeee",
            color: "#333333",
            fillOpacity: 1,
            clickable: true
        }
    });

    var SNAPSHOTS_AGE_MAX = 120000;
    var vehicleLayers = {};
    var routeMarkerIcons = {};
    var colors = {};
    var routeLayers = {};
    var activeRoutes = [];
    var stopMarkers = {};
    var ageInMillis = 0;
    var layerControl = Leaf.control.layers(null, {});
    var stopsLayer = Leaf.featureGroup();
    var contextPath = '';
    var map = null;
    var SESSION_TIMEOUT_IN_SECONDS = 300;
    var sessionIdleTimeInSeconds = 0;
    var snapshotIntervalId;
    var gpsEnabled = false;
    var debug = true;
    var route = TU.UTIL.getParam('route');
    var locationMarker = Leaf.marker(my.DEFAULTS.CENTER, {
        icon: markerIcon('/images/dude')
    });
    var originMarker = Leaf.marker(my.DEFAULTS.CENTER, {
        icon: markerIcon('/images/A')
    });
    var destMarker = Leaf.marker(my.DEFAULTS.CENTER, {
        icon: markerIcon('/images/B')
    });

    var contextMenuLatLng;

    my.getContextMenuLatLng = function() {
        return contextMenuLatLng;
    };

    my.getLocationMarker = function() {
        return locationMarker;
    };

    my.getMap = function() {
        return map;
    };

    my.zoomToVehicle = function(assetId) {
        var latLngs = [];
        var map = TU.MAP.getMap();
        if(map.hasLayer(locationMarker)) {
            latLngs.push(locationMarker.getLatLng());
        }

        var vehicleMarker;
        if(vehicleLayers[assetId] && vehicleLayers[assetId].marker) {
            vehicleMarker = vehicleLayers[assetId].marker;
            latLngs.push(vehicleMarker.getLatLng());
        }

        if(latLngs.length == 1) {
            map.panTo(latLngs[0]);
            return;
        }

        map.fitBounds(Leaf.latLngBounds(latLngs));
        if(vehicleMarker) {
            vehicleMarker.openPopup();
        }
    };

    my.main = function(mapObj, options) {

        if(typeof mapObj === 'undefined') {
            throw 'Map is undefined';
        }
        map = mapObj;
        layerControl.addTo(map);
        stopsLayer.addTo(map);

        contextPath = typeof options.contextPath !== 'undefined' && options.contextPath || contextPath;
        gpsEnabled = typeof options.gpsEnabled !== 'undefined' && options.gpsEnabled || gpsEnabled;
        debug = typeof options.debug !== 'undefined' && options.debug || debug;

        if(gpsEnabled) {
            getVehicleSnapshots(map, contextPath);
            var intervalFunction = function () {
                if (sessionIdleTimeInSeconds >= SESSION_TIMEOUT_IN_SECONDS) {
                    clearInterval(snapshotIntervalId);
                    $('#timeoutModal').modal({show: true});
                }
                getVehicleSnapshots(map, contextPath);
                sessionIdleTimeInSeconds += 5;
            };
            $('#timeoutModal').on('hide.bs.modal', function () {
                sessionIdleTimeInSeconds = 0;
                getVehicleSnapshots(map, contextPath);
                snapshotIntervalId = setInterval(intervalFunction, 5000);
            });
            snapshotIntervalId = setInterval(intervalFunction, 5000);
        }

        getAllRoutes();

        //load stops
        map.on('overlayadd', onOverlayAdd);
        map.on('overlayremove', onOverlayRemove);

        map.on('locationfound', function (e) {
            var radius = e.accuracy / 2;
            if (radius < 500) {
                locationMarker.setLatLng(e.latlng)
                    .bindPopup("Usted est&aacute; aqu&iacute;")
                    .addTo(map);
                Leaf.circle(e.latlng, radius, {
                    fill: false,
                    opacity: 0.5
                }).addTo(map);

                if(!gpsEnabled && !route) {
                    getNearbyRouteNames(e.latlng);
                }
            }
        });

        map.on('locationerror', function () {
            safeLog("location error");
            map.setView(my.DEFAULTS.CENTER, my.DEFAULTS.ZOOM);
        });

        map.on('zoomend', function() {
            sessionIdleTimeInSeconds = 0;
            var activeRouteId;
            if(map.getZoom() < 14) {
                /*for (var i = 0; i < activeRoutes.length; i++) {
                    activeRouteId = activeRoutes[i];
                    routeLayers[activeRouteId].setText(null);
                }*/
                map.removeLayer(stopsLayer);
            } else {
                /*for (var j = 0; j < activeRoutes.length; j++) {
                    activeRouteId = activeRoutes[j];
                    routeLayers[activeRouteId].setText('   \u27a4   ',  {
                        repeat: true,
                        attributes: {fill: colors[activeRouteId]}
                    });
                }*/

                if (!map.hasLayer(stopsLayer)) {
                    map.addLayer(stopsLayer);
                }
            }
        });

        map.on('contextmenu', function(e) {
            $('#contextMenu').css({
                display: "block",
                left: e.originalEvent.pageX,
                top: e.originalEvent.pageY
            });
            contextMenuLatLng = e.latlng;
        });

        map.on('click', function() {
            $('#contextMenu').hide();
        });

    };

    my.setOrigin = function() {
        if(!contextMenuLatLng) {
            return;
        }
        originMarker.setLatLng(contextMenuLatLng);
        if(!map.hasLayer(originMarker)) {
            originMarker.addTo(map);
        }
        if(map.hasLayer(destMarker)) {
            getOriginDestinationRouteNames(contextMenuLatLng, destMarker.getLatLng());
        } else {
            getNearbyRouteNames(contextMenuLatLng);
        }
        $('#contextMenu').hide();
    };

    my.setDestination = function() {
        if(!contextMenuLatLng) {
            return;
        }
        destMarker.setLatLng(contextMenuLatLng);
        if(!map.hasLayer(destMarker)) {
            destMarker.addTo(map);
        }
        if(map.hasLayer(originMarker)) {
            getOriginDestinationRouteNames(originMarker.getLatLng(), contextMenuLatLng);
        }
        $('#contextMenu').hide();
    };

    function onOverlayAdd(event) {
        var routeName = $(event.name).text();
        activeRoutes.push(routeName);
        updateStops();
    }

    function onOverlayRemove(event) {
        var routeName = $(event.name).text();
        var index = activeRoutes.indexOf(routeName);
        if (index > -1) {
            activeRoutes.splice(index, 1);
        }
        updateStops();
    }

    function markerIcon(path) {
        return new VehicleIcon({
            iconUrl: contextPath + path + ".png",
            iconRetinaUrl: contextPath + path + ".png",
            shadowUrl: contextPath + "/images/marker-shadow.png"
        });
    }

    function getPaddedBounds() {
        return map.getBounds().pad((map.getZoom() - 10) * 5 / 100);
    }

    function safeLog(message) {
        if (typeof console !== "undefined" && typeof console.log !== "undefined") {
            console.log(message);
        }
    }

    function getAllRoutes() {
        $.ajax(contextPath + '/app/routes', {
            contentType: 'application/json; charset=UTF-8',
            dataType: 'json',
            success: function (geojsonRoutes) {
                for (var i = 0; i < geojsonRoutes.features.length; i++) {
                    var data = geojsonRoutes.features[i];
                    colors[data.id] = data.properties.color;
                    routeMarkerIcons[data.id] = markerIcon('/app/icon/' + data.id);
                    var geoJsonLayer = Leaf.geoJson(data, {
                        style: routeStyle,
                        onEachFeature: routeBindPopup
                    });
                    routeLayers[data.id] = geoJsonLayer;
                    var darkColor = TU.UTIL.shadeColor(data.properties.color, -25);
                    var routeLabel = '<span class="routeLabel" style="' +
                        'background-color: ' + data.properties.color +
                        '; text-shadow: -1px 0 ' + darkColor + ', 0 1px ' + darkColor + ', 1px 0 ' + darkColor + ', 0 -1px ' + darkColor +
                        '; border-color: ' + darkColor + '">' + data.properties.fullName + '</span>';
                    layerControl.addOverlay(geoJsonLayer, routeLabel);
                    if(route == data.id) {
                        map.addLayer(geoJsonLayer);
                    }
                }
                routeMarkerIcons.unknown = markerIcon('/images/unknown');
                map.locate({setView: true, maxZoom: 14, enableHighAccuracy: true});
            }
        });
    }

    function routeStyle(feature) {
        return {
            weight: 6,
            opacity: 0.5,
            color: feature.properties.color
        };
    }

    function routeBindPopup(feature, layer) {
        var popupHtml = 'Ruta ' + feature.properties.fullName;
        layer.bindPopup(popupHtml);
        layer.on("click", function() {
            layer.bringToFront();
            stopsLayer.bringToFront();
        });
    }

    function getVehicleSnapshots() {
        $.ajax(contextPath + '/app/mapper', {
            contentType: 'application/json; charset=UTF-8',
            dataType: 'json',
            timeout: 5000,
            success: function (vehicleSnapshots) {
                var currentTimestamp = new Date().getTime();
                ageInMillis = currentTimestamp - vehicleSnapshots.timeStamp;
                map.attributionControl.setPrefix(getAttributionPrefixText(ageInMillis));
                if (ageInMillis > SNAPSHOTS_AGE_MAX) {
                    removeVehicleLayers(map, vehicleLayers);
                    return;
                }
                for (var i = 0; i < vehicleSnapshots.d.length; i++) {
                    var vehicleSnapshot = vehicleSnapshots.d[i];
                    if(vehicleSnapshot.assetId == 1029) {
                        console.log('');
                    }
                    if (vehicleSnapshot.trail.length < 1) {
                        continue;
                    }

                    var markerLatLng = new Leaf.LatLng(vehicleSnapshot.trail[0].lat, vehicleSnapshot.trail[0].lng);
                    var vehicleLayer = vehicleLayers[vehicleSnapshot.assetId];
                    var vehicleWithinBounds = getPaddedBounds(map).contains(markerLatLng);
                    if(!vehicleLayer && (!vehicleWithinBounds || !isVehicleValidForDisplay(vehicleSnapshot))) {
                        continue;
                    }

                    // estela del vehiculo
                    var trailLatLngs = new Array(vehicleSnapshot.trail.length);
                    for (var j = 0; j < vehicleSnapshot.trail.length; j++) {
                        var trailNode = vehicleSnapshot.trail[j];
                        trailLatLngs[j] = new Leaf.LatLng(trailNode.lat, trailNode.lng);
                    }

                    // informacion del vehiculo a desplegarse
                    var popupContent = createPopupContent(vehicleSnapshot, currentTimestamp);

                    if (vehicleLayer) {

                        // actualizar layer existente para el vehiculo
                        var trailHasChanged = false;
                        if(trailLatLngs.length != vehicleLayer.trail.length) {
                            trailHasChanged = true;
                        } else {
                            for (var k = 0; k < trailLatLngs.length; k++) {
                                if(!trailLatLngs[k].equals(vehicleLayer.trail[k])) {
                                    trailHasChanged = true;
                                    break;
                                }
                            }
                        }

                        if(trailHasChanged && vehicleWithinBounds) {
                            vehicleLayer.marker.setLatLng(markerLatLng);
                            vehicleLayer.trail.setLatLngs(trailLatLngs);
                        }

                        var newRoute = vehicleSnapshot.inRoute ? vehicleSnapshot.route : null;
                        if (newRoute != vehicleLayer.route) {
                            vehicleLayer.route = newRoute;
                            updateVehicleRoutePopup(map, vehicleLayer);
                        }

                        if (isVehicleValidForDisplay(vehicleSnapshot, currentTimestamp)) {
                            if (!map.hasLayer(vehicleLayer.marker)) {
                                map.addLayer(vehicleLayer.marker);
                            }

                            if (!map.hasLayer(vehicleLayer.trail)) {
                                map.addLayer(vehicleLayer.trail);
                            }
                        } else {
                            map.removeLayer(vehicleLayer.marker);
                            map.removeLayer(vehicleLayer.trail);
                        }

                    } else {
                        // crear nuevo layer para este vehiculo
                        vehicleLayer = {};
                        vehicleLayer.marker = Leaf.marker(markerLatLng);
                        vehicleLayer.trail = Leaf.polyline(trailLatLngs, { color: 'black', weight: 4, opacity: 0.7 });
                        vehicleLayer.route = vehicleSnapshot.inRoute ? vehicleSnapshot.route : null; //keep the latest route in mem
                        updateVehicleRoutePopup(map, vehicleLayer);

                        if (isVehicleValidForDisplay(vehicleSnapshot)) {
                            // vehiculo esta en servicio, desplegar en el mapa
                            vehicleLayer.marker.addTo(map);
                            vehicleLayer.trail.addTo(map);
                        }

                        vehicleLayers[vehicleSnapshot.assetId] = vehicleLayer;
                    }

                    vehicleLayer.marker.bindPopup(popupContent);
                    /*if(vehicleSnapshot.route == '50') {
                        safeLog('50');
                    }*/
                    var routeMarkerKey = (vehicleSnapshot.inRoute || (vehicleSnapshot.props && vehicleSnapshot.props.withinOrigin === 'true')) ? vehicleSnapshot.route : "unknown";
                    if (routeMarkerKey && routeMarkerIcons[routeMarkerKey]) {
                        //se conoce la ruta, colocar icono
                        vehicleLayer.marker.setIcon(routeMarkerIcons[routeMarkerKey]);
                    }
                }
            },
            error: function (jqXHR, textStatus) {
                safeLog(textStatus);
                ageInMillis += 5000;
                map.attributionControl.setPrefix(getAttributionPrefixText(ageInMillis));
                if(ageInMillis > SNAPSHOTS_AGE_MAX) {
                    removeVehicleLayers(map, vehicleLayers);
                }
            }
        });
    }

    function getNearbyRouteNames(latLng) {
        $.ajax(contextPath + '/app/nearbyRouteNames', {
            contentType: 'application/json; charset=UTF-8',
            dataType: 'json',
            method: 'POST',
            data: JSON.stringify(latLng),
            success: function (nearbyRouteNames) {
                updateRoutes(nearbyRouteNames);
            }
        });
    }

    function getOriginDestinationRouteNames(originLatLng, destLatLng) {
        $.ajax(contextPath + '/app/originDestinationRouteNames', {
            contentType: 'application/json; charset=UTF-8',
            dataType: 'json',
            method: 'POST',
            data: JSON.stringify([originLatLng, destLatLng]),
            success: function (nearbyRouteNames) {
                updateRoutes(nearbyRouteNames);
            }
        });
    }

    function updateRoutes(nearbyRouteNames) {
        map.off('overlayremove');
        var i, routeLayer;
        var removeFromActive = [];
        for (i = 0; i < activeRoutes.length; i++) {
            var activeRoute = activeRoutes[i];
            if(nearbyRouteNames.indexOf(activeRoute) == -1) {
                removeFromActive.push(activeRoute);
                routeLayer = routeLayers[activeRoute];
                if(routeLayer) {
                    map.removeLayer(routeLayer);
                }
            }
        }
        for (i = 0; i < removeFromActive.length; i++) {
            activeRoutes.splice(activeRoutes.indexOf(removeFromActive[i], 1));
        }
        map.on('overlayremove', onOverlayRemove);

        map.off('overlayadd');
        for (i = 0; i < nearbyRouteNames.length; i++) {
            var nearbyRouteName = nearbyRouteNames[i];
            if(activeRoutes.indexOf(nearbyRouteName) == -1) {
                activeRoutes.push(nearbyRouteName);
                routeLayer = routeLayers[nearbyRouteName];
                if (routeLayer) {
                    routeLayer.addTo(map).bringToBack();
                }
            }
        }
        updateStops();
        //load stops
        map.on('overlayadd', onOverlayAdd);
    }

    function updateStops() {
        if(activeRoutes.length === 0) {
            stopsLayer.clearLayers();
            return;
        }
        var bounds = getPaddedBounds();
        $.ajax(contextPath + '/app/stopsByRouteNames', {
            contentType: 'application/json; charset=UTF-8',
            dataType: 'json',
            method: 'POST',
            data: JSON.stringify({
                routeNames: activeRoutes,
                bounds: {
                    southwest: bounds.getSouthWest(),
                    northeast: bounds.getNorthEast()
                }
            }),
            success: function (stopsGeoJson) {
                stopsLayer.clearLayers();
                for (var i = 0; i < stopsGeoJson.features.length; i++) {
                    var feature = stopsGeoJson.features[i];
                    var routesByStop = feature.properties.routes;

                    var markerColors = {
                        fillColor: '#eeeeee',
                        color: '#333333'
                    };
                    if(routesByStop.length == 1) {
                        markerColors.fillColor = colors[routesByStop];
                        markerColors.color = TU.UTIL.shadeColor(colors[routesByStop], -35);
                    }

                    var circleMarker;
                    if (stopMarkers[feature.id]) {
                        circleMarker = stopMarkers[feature.id];
                        circleMarker.setStyle(markerColors);
                    } else {
                        var coords = feature.geometry.coordinates;
                        circleMarker = new StopMarker([coords[1], coords[0]], markerColors);
                        var popupText = 'Parada ' + (feature.properties.amaId && TU.UTIL.leftPadUpToFour(feature.properties.amaId, 4) || '') +
                            '<br/>Rutas: ' + (feature.properties.allRoutes || []).join(',');
                        if (debug) {
                            popupText += '<br/>ID: ' + feature.id + '<br/>';
                        }
                        circleMarker.bindPopup(popupText);
                        stopMarkers[feature.id] = circleMarker;
                    }
                    stopsLayer.addLayer(circleMarker);
                }
            }
        });
    }

    function getAttributionPrefixText() {
        var ageInDecimalSeconds = (ageInMillis / 1000).toFixed(1);
        return "GPS NO incluye M1, M3, ME. Ultima Lectura " + ageInDecimalSeconds + "s";
    }

    function updateVehicleRoutePopup(map, vehicleLayer) {
        var route = vehicleLayer.route;
        if (!route) {
            vehicleLayer.marker.off("popupopen");
            vehicleLayer.marker.off("popupclose");
            return;
        }
        vehicleLayer.marker.on("popupopen", function () {
            if(!routeLayers[route]) {
                return;
            }
            routeLayers[route].addTo(map).bringToBack();
        });
        vehicleLayer.marker.on("popupclose", function () {
            if(!routeLayers[route]) {
                return;
            }
            map.removeLayer(routeLayers[route]);
        });
    }

    function removeVehicleLayers(map) {
        for (var key in vehicleLayers) {
            if (!vehicleLayers.hasOwnProperty(key)) {
                //The current property is not a direct property of p
                continue;
            }
            //Do your logic with the property here
            var layer = vehicleLayers[key];
            map.removeLayer(layer.marker);
            map.removeLayer(layer.trail);
        }
    }

    function isVehicleValidForDisplay(vehicleSnapshot, currentTimestamp) {

        if (vehicleSnapshot.status == 'NOT_REPORTING') {
            return false;
        }

        //not valid if still for more than 1 hour
        var timeSinceChangeInMinutes = TU.UTIL.intervalInMinutes(currentTimestamp, vehicleSnapshot.positionChange);
        if(timeSinceChangeInMinutes > 60) {
            return false;
        }

        return vehicleSnapshot.withinServiceArea;
    }

    function createPopupContent(vehicleSnapshot, currentTimestamp) {
        var popupContent = 'Veh&iacute;culo: ' + (vehicleSnapshot.vehicle || '') +
            '<br/>' + (!vehicleSnapshot.inRoute && '&Uacute;ltima ' || '') + 'Ruta: ' + (vehicleSnapshot.route || 'Desconocida');

        if (vehicleSnapshot.inRoute && vehicleSnapshot.direction) {
            popupContent += "<br/>Hacia: " + vehicleSnapshot.direction;
        }

        if (vehicleSnapshot.positionChange) {
            var positionChangeMin = TU.UTIL.intervalInMinutes(currentTimestamp, vehicleSnapshot.positionChange);
            if(positionChangeMin > 1) {
                popupContent += "<br/>Sin moverse hace " + positionChangeMin + " min";
            }
        }

        if (debug || vehicleSnapshot.withinServiceArea && !vehicleSnapshot.inRoute && vehicleSnapshot.possibleRoutes) {
            popupContent += "<br/>Rutas Posibles: " + vehicleSnapshot.possibleRoutes;
        }

        if (vehicleSnapshot.status == 'STOPPED') {
            popupContent += "<br/>" + vehicleSnapshot.status == 'STOPPED' && 'Veh&iacute;culo Apagado' || '';
        }

        if(debug) {
            popupContent += '<br/>Asset ID: ' + vehicleSnapshot.assetId;
            popupContent += '<br/>Velocidad: ' + Math.round(vehicleSnapshot.props.avgSpeed * 1000) / 1000 + ' m/s';
            popupContent += '<br/>En origen: ' + vehicleSnapshot.props.withinOrigin;
            popupContent += '<br/>En ruta: ' + vehicleSnapshot.inRoute;
            popupContent += '<br/>En servicio: ' + vehicleSnapshot.withinServiceArea;
        }
        return popupContent;
    }


    return my;

}(TU.MAP || {}, jQuery, L));