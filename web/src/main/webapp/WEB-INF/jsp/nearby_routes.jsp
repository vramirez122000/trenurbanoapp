<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tu" uri="/WEB-INF/tld/tu" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: victor
  Date: 17/10/15
  Time: 16:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="routes" scope="request" type="java.util.List"/>
<c:if test="${not empty routes}">

    <h4 class="hidden-xs"><spring:message code="schedule.nearbyRoutes"/></h4>
    <h5 class="visible-xs"><spring:message code="schedule.nearbyRoutes"/></h5>

<div class="panel panel-default">

<table style="width:100%" class="table table-bordered table-striped">
    <tbody>
    <c:forEach items="${routes}" var="route">
        <c:set var="darkColor" value="${tu:darken(route.color)}"/>
        <tr>
            <td>
                <div class="row">
                    <div class="col-xs-9" style="font-weight:bold">
                        <span class="routeLabel" style="
                                background-color: ${route.color};
                                text-shadow: -1px 0 ${darkColor}, 0 1px ${darkColor}, 1px 0 ${darkColor}, 0 -1px ${darkColor};
                                border-color: ${darkColor}">${route.fullName}</span>
                        <c:if test="${not empty route.direction}"><spring:message code="to"/> <span style="text-transform: capitalize">${fn:replace(route.direction, '_', ' ')}</span></c:if>
                    </div>
                    <div class="col-xs-3 text-right">
                        <div class="btn-group-vertical btn-group-sm">
                            <a class="btn btn-sm btn-default" href="<c:url value="/app/map?route=${route.id}"/>" title="Ver en mapa">
                                <i class="fa fa-map-o"></i><span class="hidden-xs"> Mapa</span>
                            </a>
                        </div>
                    </div>
                </div>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</div>

</c:if>