<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tu" uri="/WEB-INF/tld/tu" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: victor
  Date: 17/10/15
  Time: 16:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="etas" scope="request" type="java.util.List"/>
<c:if test="${not empty etas}">

    <h4 class="hidden-xs">Tiempo estimado de espera</h4>
    <h5 class="visible-xs">Tiempo estimado de espera</h5>

<table style="width:100%" class="table table-bordered table-striped">
    <tbody>
    <c:forEach items="${etas}" var="eta">
        <c:set var="darkColor" value="${tu:darken(eta.color)}"/>
        <tr>
            <td>
                <div class="row">
                    <div class="col-xs-10" style="font-weight:bold">
                        Ruta
                        <span class="routeLabel" style="
                                background-color: ${eta.color};
                                text-shadow: -1px 0 ${darkColor}, 0 1px ${darkColor}, 1px 0 ${darkColor}, 0 -1px ${darkColor};
                                border-color: ${darkColor}">${eta.fullName}</span>
                        <c:if test="${not empty eta.direction}">hacia <span style="text-transform: capitalize">${fn:replace(fn:toLowerCase(eta.direction), '_', ' ')}</span></c:if>
                    </div>
                    <div class="col-xs-2 text-right">
                        <div>
                            <a href="<c:url value="/app/map?route=${eta.route}"/>" title="Ver en mapa">
                                <i class="fa fa-map-o"></i>
                            </a>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-4">Espera</div>
                    <div class="col-xs-8" style="font-weight: bold">
                        <span>
                            <c:if test="${eta.eta > 3600}">
                                <fmt:formatNumber value="${eta.eta / 3600}" maxFractionDigits="0" /> hr
                            </c:if>
                            <c:if test="${eta.eta > 60}">
                                <fmt:formatNumber value="${(eta.eta % 3600) / 60}" maxFractionDigits="0" /> min
                            </c:if>
                            ${eta.eta % 60} s
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-4">Distancia</div>
                    <div class="col-xs-8" style="font-weight: bold">
                        ${eta.distance}
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-4">Vel. Promedio</div>
                    <div class="col-xs-8" style="font-weight: bold">
                        ${eta.avg_speed}
                    </div>
                </div>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</c:if>