<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: victor
  Date: 7/11/15
  Time: 13:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/leaflet.css"/>
    <link rel="stylesheet" href="jspkg/Leaflet.draw/dist/leaflet.draw.css"/>
    <script type="text/javascript" src="//code.jquery.com/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/leaflet-src.js"></script>
    <script type="text/javascript" src="jspkg/Leaflet.draw/dist/leaflet.draw-src.js"></script>
    <script type="text/javascript" src="jspkg/Leaflet.TextPath/leaflet.textpath.js"></script>
    <script type="text/javascript" src="editor.js"></script>
    <script type="text/javascript">
        var config = {
            contextRoot: '<c:url value="/"/>',
            mapDiv: 'map'
        };
    </script>
</head>

<body onload="TU.EDITOR.main(config)" style="margin: 0">
<div id="map" style="width: 100%; height: 100%;"></div>
</body>
</html>
