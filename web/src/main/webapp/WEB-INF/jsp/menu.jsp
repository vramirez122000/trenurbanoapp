<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="navbar navbar-default navbar-static-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <div class="navbar-brand visible-xs">
                <img src="<c:url value="/images/trenurbano_icon_banner231.png"/>"
                     class="img-responsive"
                     style="max-height: 1.2em; max-width: 184px"/>
            </div>
            <div class="btn-group visible-xs pull-right">
                <button type="button" class="btn navbar-btn btn-default"
                        onclick="location = '<c:url value="/"/>'" title="Itinerarios">
                    <i class="glyphicon glyphicon-time"></i>
                </button>
                <button type="button" class="btn navbar-btn btn-default"
                        onclick="location = '<c:url value="/app/map"/>'" title="Mapa">
                    <i class="fa fa-map"></i>
                </button>
                <button type="button" class="btn navbar-btn btn-default"
                        data-toggle="modal" data-target="#aboutUs" title="Acerca de Tren Urbano App">
                    <i class="glyphicon glyphicon-info-sign"></i>
                </button>
            </div>
        </div>

        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li id="itinerario-link">
                    <a onclick="location = '<c:url value="/"/>'"><i class="glyphicon glyphicon-time"></i>
                        Itinerario</a>
                </li>
                <li id="mapa-link">
                    <a onclick="location = '<c:url value="/app/map"/>'"><i class="fa fa-map"></i>
                        Mapa</a>
                </li>
                <li class="hidden-xs">
                    <a href="http://itunes.apple.com/us/app/tren-urbano-app/id484781635" target="_blank"><i class="fa fa-apple"></i>
                        Versi&oacute;n Apple iOS</a>
                </li>
                <li class="hidden-xs">
                    <a href="https://market.android.com/details?id=com.trenurbanoapp" target="_blank"><i class="fa fa-android"></i>
                        Versi&oacute;n Android</a>
                </li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <a data-toggle="modal" data-target="#aboutUs"><i class="glyphicon glyphicon-info-sign"></i>
                        Acerca de Tren Urbano App</a>
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
