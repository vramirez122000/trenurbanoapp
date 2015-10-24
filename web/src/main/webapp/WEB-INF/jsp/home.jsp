<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--
  User: victor
  Date: 8/30/11
--%>
<!doctype html>
<jsp:useBean id="now" class="java.util.Date" scope="request"/>
<html <c:if test="${!applicationScope['appCache.disabled']}">manifest="<c:url value="/cache.manifest"/>"</c:if>>
<head>
    <c:import url="header.jsp"/>
</head>
<body>

<c:import url="menu.jsp"/>

<div class="container">

    <div id="alertMsg" class="alert alert-warning" style="display: ${not empty applicationScope['alert.message']? 'block':'none'}">
        <span id="alertTxt">${applicationScope['alert.message']}</span>
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
    </div>

    <div class="jumbotron well hidden-xs">

    <div class="container">
        <div class="col-xs-12">
            <h1>
                <img alt="TrenUrbanoApp.com banner"
                     src="<c:url value="/images/trenurbano_icon_banner468.png"/>"/>
            </h1>

            <p>Escoja la estaci&oacute;n y le mostramos los tiempos de llegada de los pr&oacute;ximos
                trenes en ambas
                direcciones.</p>
        </div>

        <div class="col-xs-6">
            <div class="form-group">
                <label for="station" class="control-label">Escoja la estación</label>

                <div class="input-group">
                    <select id="station" name="station" class="form-control station-select">
                        <option value="">-- Escoja la Estaci&oacute;n --</option>
                        <jsp:useBean id="stations" scope="request" type="java.util.List"/>
                        <c:forEach items="${stations}" var="varStation">
                            <jsp:useBean id="varStation" scope="page" type="com.trenurbanoapp.model.IdDesc"/>
                            <option value="${varStation.id}"
                                    <c:if test="${sessionScope.station == varStation.id}">selected="selected"</c:if>>
                                ${varStation.desc}
                            </option>
                        </c:forEach>
                    </select>
                <span class="input-group-btn">
                    <button type="button" class="btn btn-info" onclick="$('.station-select:visible').change()"
                            title="Refresca el itinerario para la estaci&oacute;n seleccionada">
                        <i class="glyphicon glyphicon-refresh"></i>
                    </button>
                    <button type="button" class="btn btn-info" onclick="findNearestStation()"
                            title="Encuentra la estaci&oacute;n mas cercana con GPS">
                        <i class="fa fa-location-arrow"></i>
                    </button>
                </span>
                </div>
            </div>
        </div>
        <div class="col-xs-6">
            <div class="form-group clock">
                <label for="time-lg-text" class="control-label">Hora Actual</label>
                <div class="input-group">
                    <input type="text" class="form-control" id="time-lg-text" readonly="readonly" />
                    <input type="time" class="form-control" id="time-lg-time" style="display: none"/>
                    <span class="input-group-btn">
                        <button type="button" class="btn btn-info active" onclick="toggleClock()">
                            <span class="glyphicon glyphicon-time"></span>
                        </button>
                    </span>
                </div>
            </div>
        </div>
    </div>
    </div>

    <div class="visible-xs">
        <div class="form-group">
            <label for="station-mobile" class="control-label">Escoja la estación</label>
            <div class="input-group">
                <select id="station-mobile" name="station" class="form-control station-select">
                    <option value="">-- Escoja la Estaci&oacute;n --</option>
                    <c:forEach items="${stations}" var="varStation">
                        <option value="${varStation.id}"
                                <c:if test="${sessionScope.station == varStation.id}">selected="selected"</c:if>>
                                ${varStation.desc}
                        </option>
                    </c:forEach>
                </select>
                <span class="input-group-btn">
                    <button type="button" class="btn btn-info" onclick="$('.station-select:visible').change()">
                        <i class="glyphicon glyphicon-refresh"></i>
                    </button>
                    <button type="button" class="btn btn-info" onclick="findNearestStation()">
                        <i class="fa fa-location-arrow"></i>
                    </button>
                </span>
            </div>
        </div>
        <div class="form-group clock">
            <label for="time-xs-text" class="control-label">Hora Actual</label>
            <div class="input-group clock">
                <input type="text" class="form-control" id="time-xs-text" readonly="readonly" />
                <input type="time" class="form-control" id="time-xs-time" style="display: none"/>
                <span class="input-group-btn">
                    <button type="button" class="btn btn-info active" onclick="toggleClock()">
                        <span class="glyphicon glyphicon-time"></span>
                    </button>
                </span>
            </div>
        </div>
    </div>

    <div id="stopTimes"></div>
    <div id="nearbyRoutesWithoutSchedules"></div>

</div>

<c:if test="${applicationScope['javascript.useSource']}">
    <script type="text/javascript" src="<c:url value="/js/trenurbanoapp-schedule.concat.js"/>"></script>
</c:if>
<c:if test="${!applicationScope['javascript.useSource']}">
    <script type="text/javascript" src="<c:url value="/js/tusched.min.js"/>"></script>
</c:if>

<script type="text/javascript" src="<c:url value="/js/home.js"/>"></script>
<script>
    var gpsEnabled = ${applicationScope['gps.enabled']};
    var alertMessage = '${applicationScope['alert.message']}';
    $(document).ready(main);
</script>

</body>

</html>