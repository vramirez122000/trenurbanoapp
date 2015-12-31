<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tu" uri="/WEB-INF/tld/tuapp" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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
    <h4 class="hidden-xs"><spring:message code="routes.with.timetables"/> ${stopTimes[0].stopArea}</h4>
    <h5 class="visible-xs"><spring:message code="routes.with.timetables"/> ${stopTimes[0].stopArea}</h5>

    <table id="results" class="table table-bordered table-striped">
        <tbody>

        <c:forEach items="${stopTimes}" var="stopTime">
            <jsp:useBean id="stopTime" scope="page" type="com.trenurbanoapp.model.StopTime"/>
            <c:set var="darkColor" value="${tu:darken(stopTime.color)}"/>
            <c:set var="plusMinus" value="${stopTime.errorMinutes > 0 ? ' &plusmn; '.concat(stopTime.errorMinutes).concat(' min') : ''}"/>
            <tr>
                <td>
                    <div class="row">
                        <div class="col-xs-9">
                            <div class="row">
                                <div class="col-xs-12" style="font-weight:bold">
                                    <span class="routeLabel" style="
                                            background-color: ${stopTime.color};
                                            text-shadow: -1px 0 ${darkColor}, 0 1px ${darkColor}, 1px 0 ${darkColor}, 0 -1px ${darkColor};
                                            border-color: ${darkColor}">${stopTime.routeFullName}</span>
                                        <spring:message code="to"/> ${stopTime.dest}
                                </div>
                            </div>
                            <div class="row etaDiv">
                                <div class="stopTime" style="display: none">${stopTime.stopTimeEpochMillis}</div>
                                <div class="col-xs-5"><spring:message code="waitTime"/></div>
                                <div class="col-xs-7" style="font-weight: bold">
                                <span class="etaString">
                                        ${stopTime.stopTimeEtaString}
                                </span>${plusMinus}
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-xs-5"><spring:message code="${stopTime.errorMinutes > 0 ? 'estimate' : 'stopTime' }"/></div>
                                <div class="col-xs-7">
                                    <strong>${stopTime.stopTimeString}${plusMinus}</strong>
                                </div>
                            </div>
                            <div class="row hidden-xs">
                                <div class="col-xs-5"><spring:message code="scheduleType"/></div>
                                <div class="col-xs-7">
                                    <strong>${stopTime.scheduleType.description}</strong>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-3 visible-xs text-right">
                               <div class="btn-group-vertical btn-group-sm">
                                   <c:if test="${stopTime.errorMinutes == 0}">
                                       <a class="btn btn-sm btn-default" href="tel:311" title="Repórtalo">
                                           <i class="fa fa-exclamation-circle"></i>
                                       </a>
                                   </c:if>
                                   <a class="btn btn-sm btn-default" href="<c:url value="/app/map?route=${stopTime.route}"/>" title="Ver en mapa">
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

<c:if test="${empty stopTimes && not empty station}">
    <div class="alert alert-warning">
        <span id="alertTxt">No se encontraron viajes en la parada seleccionada</span>
    </div>
</c:if>

<div class="modal" id="report" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">Repórtalo</h4>
            </div>
            <div class="modal-body"></div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

</body>
</html>