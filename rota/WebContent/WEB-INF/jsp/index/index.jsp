<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
		<title>Leaflet Example</title>
		<meta charset="utf-8" />
	
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
		<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.6.2/leaflet.css" />
			
			<!--[if lte IE 8]>
	    		<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.6.2/leaflet.ie.css" />
			<![endif]-->
		
		
		<script src="http://cdn.leafletjs.com/leaflet-0.6.2/leaflet.js"></script>
		
		<link href="<c:url value="/css/rota.css"/>" rel="stylesheet" type="text/css"/>
	</head>
	<body>
		<div id="map"></div>
		<script src="js/rota.js"></script>
	</body>
</html>

