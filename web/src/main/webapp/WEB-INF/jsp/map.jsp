<%--@elvariable id="gpsEnabled" type="java.lang.Boolean"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<!doctype html>
<html <c:if test="${!applicationScope['appCache.disabled']}">manifest="<c:url value="/cache.manifest"/>"</c:if>>
<head>

    <c:import url="header.jsp"/>
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/leaflet.css" />
    <link rel="stylesheet" href="<c:url value="/css/trenurbanoapp-map.css"/>">
    <link rel="stylesheet" href="<c:url value="/jspkg/leaflet-geocoder-mapzen/dist/leaflet-geocoder-mapzen.css"/>"/>


    <!-- Load geocoding plugin after Leaflet -->


    <c:if test="${applicationScope['javascript.useSource']}">
        <script src="//cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/leaflet-src.js"></script>
        <script src="<c:url value="/jspkg/Leaflet.TextPath/leaflet.textpath.js"/>"></script>
        <script src="<c:url value="/jspkg/leaflet-geocoder-mapzen/dist/leaflet-geocoder-mapzen.js"/>"></script>

        <script src="<c:url value="/js/trenurbanoapp-util.js"/>"></script>
        <script src="<c:url value="/js/trenurbanoapp-map.js"/>"></script>
    </c:if>
    <c:if test="${!applicationScope['javascript.useSource']}">
        <script src="//cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/leaflet.js"></script>
        <script src="<c:url value="/js/tumap.min.js"/>"></script>
    </c:if>
    <script type="text/javascript">
        function resizeMapDiv() {
            var mapDiv = $('#map');
            var navbarHeight = $('nav.navbar-static-top').height();
            var innerHeight = $(window).innerHeight();
            mapDiv.css("height", innerHeight - (navbarHeight + 1 || (innerHeight * 0.14) ));
        }

        $(document).ready(function () {

            resizeMapDiv();

            var mapCenter = [${applicationScope['map.center.lat']}, ${applicationScope['map.center.lng']}];
            var mapBounds = [
                [${applicationScope['map.bounds.southwest.lat']}, ${applicationScope['map.bounds.southwest.lng']}],
                [${applicationScope['map.bounds.northeast.lat']}, ${applicationScope['map.bounds.northeast.lng']}]
            ];

            var map = L.map('map', {
                center: mapCenter,
                zoom: TU.MAP.DEFAULTS.ZOOM,
                zoomControl: false
            });

            var url = 'http://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}@2x.png'; //cartodb
            L.tileLayer(url, {
                minZoom: 10,
                maxZoom: 18,
                maxBounds: mapBounds,
                attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, ' +
                '&copy; <a href="http://cartodb.com/attributions">CartoDB</a>'
            }).addTo(map);

            L.control.geocoder('search-uUAFZ_4', {
                bounds: L.latLngBounds(mapBounds).pad(1),
                latlng: mapCenter,
                autocomplete: true
            }).addTo(map);

            L.control.zoom().addTo(map);

            TU.MAP.main(map, {
                mapCenter: mapCenter,
                gpsEnabled: ${applicationScope['gps.enabled']},
                contextPath: '${pageContext.request.contextPath}',
                debug: ${applicationScope['javascript.useSource']}
            });

            $('.mapa-link').addClass('active');

            window.addEventListener('resize', resizeMapDiv, false);
            window.addEventListener('orientationchange', resizeMapDiv, false);
        });
    </script>
</head>
<body>
<c:import url="menu.jsp"/>
<div id="map" style="width: 100%; height: 460px;"></div>

<svg id="vehicle-icon-svg" version="1.1" xmlns="http://www.w3.org/2000/svg"  x="0px" y="0px"
     width="25.625px" height="41.375px" viewBox="2.148 1.966 25.625 41.375" enable-background="new 2.148 1.966 25.625 41.375"
     xml:space="preserve">
<linearGradient class="borderGradient" gradientUnits="userSpaceOnUse" x1="43.8496" y1="266.3665" x2="43.8496" y2="303.9326"
                gradientTransform="matrix(1 0 0 -1 -28.8462 306.7563)">
    <stop offset="0"></stop>
    <stop offset="1"></stop>
</linearGradient>
    <linearGradient class="fillGradient" gradientUnits="userSpaceOnUse" x1="193.6152" y1="-103.4333" x2="193.6152" y2="-84.3803">
        <stop offset="0"></stop>
        <stop offset="1"></stop>
    </linearGradient>
    <path fill="url(#fillGradient)" stroke="url(#borderGradient)" stroke-width="1.1" stroke-linecap="round"
          d="M15.047,3.003
	c-6.573,0-12.044,5.688-12.044,11.865c0,2.776,1.564,6.309,2.694,8.744l9.306,17.872l9.263-17.872
	c1.129-2.438,2.737-5.79,2.737-8.744C27.003,8.693,21.62,3.003,15.047,3.003L15.047,3.003z"></path>
    <%--
    <text id="svgRouteText" transform="matrix(1 0 0 1 0 0)" fill="#FFFFFF" stroke-width="0.2"
          style="text-anchor: middle; letter-spacing: -0.8em" x="15.19" y="21.0"
          font-family="'PT Sans Narrow Bold'" font-weight="bold" font-size="21" xml:space="preserve"></text>
    --%>
    <text class="svgRouteText" transform="matrix(1 0 0 1 0 0)" fill="#FFFFFF" stroke-width="0.2"
          style="text-anchor: middle; letter-spacing: -0.8em" x="15.19" y="21.0"
          font-family="sans-serif" font-weight="bold" font-size="21" xml:space="preserve"></text>
</svg>

<div class="modal" id="timeoutModal" tabindex="-1" role="dialog" aria-labelledby="timeoutModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="timeoutModalLabel">&iquest;Sigue ah&iacute;?</h4>
            </div>
            <div class="modal-body">Cierre esta notificaci&oacute;n o presione 'Continuar' para regresar al mapa de
                rastreo GPS.
            </div>
            <div class="modal-footer">
                <button type="button" class="btn" data-dismiss="modal">Continuar</button>
            </div>
        </div> <!-- /.modal-content -->
    </div> <!-- /.modal-dialog -->
</div>

<div id="contextMenu" class="dropdown clearfix" style="position: absolute; display:none;">
    <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="display:block;position:static;margin-bottom:5px;">
        <li><a tabindex="-1" onclick="TU.MAP.setOrigin()">Rutas desde aqu&iacute;</a></li>
        <li><a tabindex="-1" onclick="TU.MAP.setDestination()">Rutas hacia aqu&iacute;</a></li>
        <li><a tabindex="-1" onclick="TU.MAP.clearOriginDestination()">Eliminar origen y destino</a></li>
        <li><a tabindex="-1" onclick="TU.MAP.clearRoutes()">Esconder Todas las rutas</a></li>
        <%-- TODO hide this menu entry --%>
        <li style="display: ${applicationScope['javascript.useSource'] ? 'block' : 'block'}">
            <a id="latlng" tabindex="-1" onclick="TU.MAP.copyLatLngToClipboard()"></a>
        </li>
    </ul>
</div>

<div id="markerContextMenu" class="dropdown clearfix" style="position: absolute; display:none;">
    <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="display:block;position:static;margin-bottom:5px;">
        <li><a tabindex="-1" onclick="">Eliminar destino</a></li>
    </ul>
</div>


</body>
</html>