<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<nav class="navbar navbar-default navbar-static-top">
    <div class="container">

        <div class="navbar-header pull-left">
            <span class="navbar-brand visible-xs">
                <img src="<c:url value="/images/trenurbano_icon_banner231.png"/>"
                     class="img-responsive"
                     style="max-height: 1.2em; max-width: 184px"/>
            </span>

            <div class="pull-right">
                <div class="btn-group visible-xs">
                    <button class="btn btn-sm btn-default navbar-btn itinerario-link" onclick="location = '<c:url value="/"/>'" title="<spring:message code="menu.schedule"/>">
                        <i class="glyphicon glyphicon-time"></i>
                    </button>
                    <button class="btn btn-sm btn-default navbar-btn mapa-link" onclick="location = '<c:url value="/app/map"/>'" title="<spring:message code="menu.map"/>">
                        <i class="fa fa-map"></i>
                    </button>
                    <button class="btn btn-sm btn-default navbar-btn" data-toggle="modal" data-target="#aboutUs">
                        <i class="glyphicon glyphicon-info-sign"></i>
                    </button>
                    <c:set var="locale" value="${cookie['org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE'].value}"/>
                    <c:if test="${param['lang'] == 'en' || (empty param['lang'] && fn:startsWith(locale,'en'))}">
                        <button class="btn btn-sm btn-default navbar-btn" onclick="location = '<c:url value="?lang=es"/>'">Es</button>
                    </c:if>
                    <c:if test="${param['lang'] == 'es' || (empty param['lang'] && !fn:startsWith(locale,'en'))}">
                        <button class="btn btn-sm btn-default navbar-btn" onclick="location = '<c:url value="?lang=en"/>'">En</button>
                    </c:if>
                </div>
            </div>

        </div>

        <div>
            <ul class="nav navbar-nav hidden-xs">
                <li class="itinerario-link">
                    <a onclick="location = '<c:url value="/"/>'"><i class="glyphicon glyphicon-time"></i>
                        <spring:message code="menu.schedule"/></a>
                </li>
                <li class="mapa-link">
                    <a onclick="location = '<c:url value="/app/map"/>'"><i class="fa fa-map"></i>
                        <spring:message code="menu.map"/></a>
                </li>
                <li>
                    <a href="http://itunes.apple.com/us/app/tren-urbano-app/id484781635" target="_blank"><i class="fa fa-apple"></i>
                        <spring:message code="menu.apple"/></a>
                </li>
                <li>
                    <a href="https://market.android.com/details?id=com.trenurbanoapp" target="_blank"><i class="fa fa-android"></i>
                        <spring:message code="menu.android"/></a>
                </li>
                <li>
                    <c:if test="${param['lang'] == 'en' || (empty param['lang'] && fn:startsWith(locale,'en'))}">
                        <a onclick="location = '<c:url value="?lang=es"/>'">Español</a>
                    </c:if>
                    <c:if test="${param['lang'] == 'es' || (empty param['lang'] && !fn:startsWith(locale,'en'))}">
                        <a onclick="location = '<c:url value="?lang=en"/>'">English</a>
                    </c:if>
                </li>
            </ul>

            <ul class="nav navbar-nav navbar-right hidden-xs">
                <li>
                    <a data-toggle="modal" data-target="#aboutUs">
                        <i class="glyphicon glyphicon-info-sign"></i>
                        <spring:message code="menu.about"/>
                    </a>
                </li>
            </ul>
        </div>
    </div>

</nav>

<div class="modal" id="aboutUs" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">Acerca de Tren Urbano App</h4>
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
<!-- /.modal -->
<script type="text/javascript">
    $("#aboutUs .modal-body").load("<c:url value="/html/about_us.html"/>");
</script>
