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

    <div class="panel panel-default">

    <table id="results" class="table table-striped">
        <tbody>

        <c:forEach items="${stopTimes}" var="stopTime" varStatus="varStat">
            <jsp:useBean id="stopTime" scope="page" type="com.trenurbanoapp.model.StopTime"/>
            <c:set var="darkColor" value="${tu:darken(stopTime.color)}"/>
            <c:set var="plusMinus" value="${stopTime.errorMinutes > 0 ? ' &plusmn; '.concat(stopTime.errorMinutes).concat(' min') : ''}"/>
            <tr id="schedRow${varStat.index}">
                <td>
                    <div class="row">
                        <div class="col-xs-9">
                            <div class="row">
                                <div class="col-xs-12" style="font-weight:bold">
                                    <span class="routeLabel" style="
                                            background-color: ${stopTime.color};
                                            text-shadow: -1px 0 ${darkColor}, 0 1px ${darkColor}, 1px 0 ${darkColor}, 0 -1px ${darkColor};
                                            border-color: ${darkColor}">${stopTime.routeCode}</span>
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
                        <div class="col-xs-3 text-right" style="vertical-align: middle">
                               <div class="btn-group-vertical btn-group-sm">
                                   <a class="btn btn-sm btn-default" href="<c:url value="/app/map?route=${stopTime.route}"/>" title="Ver en mapa">
                                       <i class="fa fa-map-o"></i><span class="hidden-xs"> <spring:message code="menu.map"/></span>
                                   </a>
                                   <c:if test="${stopTime.errorMinutes == 0}">
                                       <a class="btn btn-sm btn-default"
                                          onclick="$('#instructions').html($('#schedRow${varStat.index}').find('.report-instructions').html())"
                                          title="Repórtalo" data-toggle="modal" data-target="#report">
                                           <i class="fa fa-exclamation-circle"></i><span class="hidden-xs"> <spring:message code="schedule.delayed"/></span>
                                       </a>
                                   </c:if>

                               </div>
                        </div>
                    </div>
                    <div class="hidden report-instructions">
                        <spring:message code="route"/> <span class="routeLabel" style="
                            background-color: ${stopTime.color};
                            text-shadow: -1px 0 ${darkColor}, 0 1px ${darkColor}, 1px 0 ${darkColor}, 0 -1px ${darkColor};
                            border-color: ${darkColor}">${stopTime.routeFullName}</span>
                        <spring:message code="to"/> ${stopTime.dest}, partiendo de ${stopTime.stopArea} a las
                            ${stopTime.stopTimeString}, está retrasado.
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>

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
                <h4 class="modal-title" id="myModalLabel"><spring:message code="schedule.reportDialog.header"/> </h4>
            </div>
            <div class="modal-body">
                <p>Llame a <a class="" href="tel:${applicationScope['report.phone']}"><i class="fa fa-phone"></i> ${applicationScope['report.phone']}</a> e indique
                    que <span id="instructions"></span></p>
            </div>
            <div class="modal-footer">
                <a class="btn btn-info" href="tel:${applicationScope['report.phone']}"><i class="fa fa-phone"></i> ${applicationScope['report.phone']}</a>
                <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

</body>
</html>