<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tu" uri="/WEB-INF/tld/tuapp" %>
<%--
  User: victor
  Date: 8/30/11
--%>
<html>
<body>

<jsp:useBean id="station" scope="request" type="java.lang.String"/>
<jsp:useBean id="stopTimes" scope="request" type="java.util.List"/>

<input type="hidden" id="resultsStation" name="resultsStation" value="${station}"/>

<c:if test="${not empty stopTimes}">
    <h4 class="hidden-xs">Rutas con itinerarios en ${stopTimes[0].stopArea}</h4>
    <h5 class="visible-xs">Rutas con itinerarios en ${stopTimes[0].stopArea}</h5>

    <table id="results" class="table table-bordered table-striped">
        <tbody>

        <c:forEach items="${stopTimes}" var="stopTime">
            <jsp:useBean id="stopTime" scope="page" type="com.trenurbanoapp.model.StopTime"/>
            <c:set var="darkColor" value="${tu:darken(stopTime.color)}"/>
            <tr>
                <td>
                    <div class="row">
                        <div class="col-xs-10">
                            <div class="row">
                                <div class="col-xs-12" style="font-weight:bold">
                                    Ruta
                                <span class="routeLabel" style="
                                        background-color: ${stopTime.color};
                                        text-shadow: -1px 0 ${darkColor}, 0 1px ${darkColor}, 1px 0 ${darkColor}, 0 -1px ${darkColor};
                                        border-color: ${darkColor}">${stopTime.routeFullName}</span>
                                    hacia ${stopTime.dest}
                                </div>
                            </div>
                            <div class="row etaDiv">
                                <div class="stopTime" style="display: none">${stopTime.stopTimeEpochMillis}</div>
                                <div class="col-xs-6">Espera</div>
                                <div class="col-xs-6 etaString" style="font-weight: bold">
                                    <c:if test="${stopTime.stopTimeEtaSeconds > 3600}">
                                        <fmt:formatNumber maxFractionDigits="0"
                                                          value="${stopTime.stopTimeEtaSeconds / 3600}"/> hr
                                    </c:if>
                                    <fmt:formatNumber maxFractionDigits="0"
                                                      value="${stopTime.stopTimeEtaSeconds / 60}"/>
                                    min ${stopTime.stopTimeEtaSeconds % 60} s
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-xs-6">Itinerario</div>
                                <div class="col-xs-6"><strong>${stopTime.stopTimeString}</strong></div>
                            </div>
                        </div>
                        <div class="col-xs-2 text-right">
                            <div>
                                <a href="<c:url value="/app/map?route=${stopTime.route}"/>" title="Ver en mapa">
                                    <i class="fa fa-map-o"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

</body>
</html>