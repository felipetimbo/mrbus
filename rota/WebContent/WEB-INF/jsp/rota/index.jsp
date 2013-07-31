<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<html>
	<head>
		<title>MRBUs</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">  
	
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
		<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.6.2/leaflet.css" />
		<!--[if lte IE 8]>
		    <link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.6.2/leaflet.ie.css" />
		<![endif]-->
		
		<script src="http://cdn.leafletjs.com/leaflet-0.6.2/leaflet.js"></script> 
		
		<script type="text/javascript" src="<c:url value="/js/jquery-1.10.2.min.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/jquery-ui/js/jquery-ui-1.10.3.custom.js"/>"></script>
		
		<link href="<c:url value="/css/rota.css"/>" rel="stylesheet" type="text/css"/>
		<link href="<c:url value="/js/jquery-ui/css/custom-theme/jquery-ui-1.10.3.custom.css"/>" rel="stylesheet">
		
	</head>
	<body>
		<img src="<c:url value="/images/mrbus_mini_logo.png"/>" /> 
		<img id="busStopBlueImg" src="<c:url value="/images/bus-stop-blue.png"/>" style="display:none" />
		<img id="busStopRedImg" src="<c:url value="/images/bus-stop-green.png"/>" style="display:none" />
		<img id="busStopRedImg" src="<c:url value="/images/bus-stop-red.png"/>" style="display:none" />
		<form action="">
		
		<div class="ui-widget">
		  	<label for="rotasBusca">Rotas: </label>
		  		<input id="rotasBusca" name="rotasBusca" />
		  		<!-- 
		  		<label>Ida</label>
		  		<input type="radio" name="sentidoIda" value="true" checked="checked" />
				<label>Volta</label>
				<input type="radio" name="sentidoIda" value="false" />
				  -->
				<input id="exibirRotaButton" type="button" onclick="exibirRota();" value="Consultar" />
				<input id="limparTelaButton" type="button" onclick="limparTela();" value="Limpar" />
				<input id="buscarMelhorRotaButton" type="button" onclick="buscarRota();" value="Recomendar Rota" />
		</div>
			
		<div id="map"></div>
		<script src="js/rota.js"></script>
		
		<div id="dialog" title="Aguarde...">
		  <img id="busStopBlueImg" src="<c:url value="/images/loading.gif"/>" align="middle" style="padding: 0 12px 0 0;"/>
		  Buscando Melhor Rota
		</div>
		
		</form>
	</body>
</html>

