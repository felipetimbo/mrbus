<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
		<title>Rota</title>
		<meta charset="utf-8" />
	
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
		<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.6.2/leaflet.css" />
		<!--[if lte IE 8]>
		    <link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.6.2/leaflet.ie.css" />
		<![endif]-->
		
		<script src="http://cdn.leafletjs.com/leaflet-0.6.2/leaflet.js"></script> 
		
		<script type="text/javascript" src="<c:url value="/js/jquery-1.10.2.min.js"/>"></script>
		<link href="<c:url value="/css/rota.css"/>" rel="stylesheet" type="text/css"/>
		
	</head>
	<body>
		<img src="<c:url value="/images/mrbus_mini_logo.png"/>" /> 
		<img id="busStopBlueImg" src="<c:url value="/images/bus-stop-blue.png"/>" style="display:none" />
		<img id="busStopRedImg" src="<c:url value="/images/bus-stop-green.png"/>" style="display:none" />
		<form action="">
			<div id="map"></div>
			<script src="js/rota.js"></script>
			
		</form>
	</body>
</html>

