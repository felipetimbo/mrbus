/**
 * Arquivo js principal da aplicacao
 */

/* Variaveis globais */

var map = L.map('map').setView([-3.76149, -38.52287], 12);

L.tileLayer('http://{s}.tile.cloudmade.com/BC9A493B41014CAABB98F0471D759707/997/256/{z}/{x}/{y}.png', {
	maxZoom: 18,
	attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery � <a href="http://cloudmade.com">CloudMade</a>'
}).addTo(map);

/**
 * Ao entrar na pagina de selecao de melhor rota
 * */
$(document).ready(function(){
	buscarNomeTodasRotas();
});

var rotasDisponiveis;

var paradaIcon = L.icon({
    iconUrl: 'images/bus-stop-green.png',

    iconSize:     [30, 30], // size of the icon
    iconAnchor:   [0, 30], // point of the icon which will correspond to marker's location
    popupAnchor:  [11, -30] // point from which the popup should open relative to the iconAnchor
});

var paradaSelecionadaIcon = L.icon({
	iconUrl: 'images/bus-stop-blue.png',

	iconSize:     [30, 30], // size of the icon
    iconAnchor:   [0, 30], // point of the icon which will correspond to marker's location
    popupAnchor:  [11, -30] // point from which the popup should open relative to the iconAnchor
});

var estiloRuas = {
    "color": "#0000ff",
    "weight": 5,
    "opacity": 0.65
};

var layerGroupParadas;
var paradasLayer = [];

var featuresSelecionados = [];
var layerGroupParadasSelecionadas;
var paradasSelecionadasLayer = [];

var nomesTodasRotas = []; // nomes de todas as linhas de onibus
var ruaLayer = L.geoJson().addTo(map);
var ruaFeature;

function caracteristicasPonto(feature, layer) {
	featuresSelecionados.push(feature);
	var popupContent = "<p><b>Id:</b> " + feature.properties.id + "<br />" +
						"<b>Latidude:</b> " + layer._latlng.lat + "<br />" +
						"<b>Longitude:</b> " + layer._latlng.lng + "<br />" +
						"<b>Proximo a:</b> " + feature.properties.next_to + "<br />" +
						"<b>Linhas da parada:</b> " + feature.properties.qtd_linhas + " linha(s) <br />" +
						feature.properties.linhas_parada + "</p><br />" + 
						"<input id='adicionarDestinoButton' type='button' class='btnAdd' onclick='adicionarParada("+ feature.properties.id +");' value='Adicionar' />";

	if (feature.properties && feature.properties.popupContent) {
		popupContent += feature.properties.popupContent;
	}

	layer.bindPopup(popupContent);
}

function caracteristicasPontoSelecionado(feature, layer) {
	featuresSelecionados.push(feature);
	var popupContent = "<p><b>Proximo a:</b> " + feature.properties.next_to + "<br />" +
						"<b>Linhas da parada:</b> " + feature.properties.qtd_linhas + " linha(s) <br />" +
						feature.properties.linhas_parada + "</p><br />";

	if (feature.properties && feature.properties.popupContent) {
		popupContent += feature.properties.popupContent;
	}

	layer.bindPopup(popupContent);
}

function onMapClick(e) {

	if(typeof(layerGroupParadas) != "undefined"){
		layerGroupParadas.clearLayers();
		paradasLayer = [];
	}
	
	var circle = L.circle([e.latlng.lat, e.latlng.lng], 330, {
		color: 'green',
		fillColor: '#f9f513',
		fillOpacity: 0.5
	});
	
	paradasLayer.push(circle);
	
	buscarParadasAdjacentes(e.latlng.lat, e.latlng.lng);
}

map.on('click', onMapClick);

var geojsonMarkerOptions = {
	    radius: 4,
	    fillColor: "#0000ff",
	    color: "#000",
	    weight: 1,
	    opacity: 1,
	    fillOpacity: 0.8
	};

function buscarParadasAdjacentes(lat, lng){
	
		$.ajax({
			  url: 'rota/buscarParadasAdjacentes',
			  cache: false, 
			  data: {latitude: lat, longitude: lng},
			  success: function(data){
				  plotarParadaNoMapa(data);
				  layerGroupParadas = L.layerGroup(paradasLayer).addTo(map);
			}
				
		});
}

function buscarTodasParadas(){

	$.ajax({
		  url: 'rota/buscarTodasParadas',
		  cache: false, 
		  success: function(data){
			  plotarParadaNoMapa(data);
		}
			
	});
}


function buscarNomeTodasRotas(){

	$.ajax({
		  url: 'rota/buscarNomeTodasRotas',
		  cache: false, 
		  success: function(data){
			  for (var x=0; x < data.nomesTodasRotas.length; x++) {
				  var nomeRota = data.nomesTodasRotas[x];
				  
				  nomesTodasRotas.push(nomeRota);
			  }
			  $( "#rotasBusca" ).autocomplete({
			      source: nomesTodasRotas
			    });
			  
		}
			
	});
}

function exibirRota() {
	
	var nomeDaRota = $("#rotasBusca")[0].value;
//	var sentido = $("input[name='sentidoIda']:checked").val();
	
	$.ajax({
		  url: 'rota/exibirRota',
		  cache: false, 
		  data: {nomeDaRota: nomeDaRota},
		  success: function(data){
			  var rotaSelecionada = data.rotaSelecionada;
			  
			  geo = new Object();
		      geo = eval("("+rotaSelecionada.rota+")");
		    
		      //troca as coordenadas, BUG do GeoJson
		      for(var i=0; i < geo.coordinates.length; i++){
		    	  coordenadas = geo.coordinates[i];
		    	  
		    	  var aux = coordenadas[0];
		    	  coordenadas[0] = coordenadas[1];
		    	  coordenadas[1] = aux;
		      }
		      
		      if(typeof(ruaFeature) != "undefined"){
		    	  ruaLayer.clearLayers();
			  }
		      
		      ruaFeature = {
			    "type": "Feature",
			    "properties": {
			    	"id": rotaSelecionada.codigo,
			        "nome": rotaSelecionada.nome,
			        "terminais": rotaSelecionada.terminais
			    },
			    "geometry": geo
			  };
		      
		      ruaLayer.addData(ruaFeature);
		      
//		    var elementLayer = L.geoJson(geojsonFeature, {
//		    	style: estiloRuas
//		    });
//		    
//		    if(typeof(layerGroupRuas) != "undefined"){
//		    	layerGroupRuas.clearLayers();
//			}
//		    
//		    ruaLayer = [];
//		    ruaLayer.push(elementLayer);
//			 
//			layerGroupRuas = L.layerGroup(ruaLayer).addTo(map);   
		      
		},error: function(){
		    alert('error!');
	    }
			
	});

}

function plotarParadaNoMapa(data){
	 for (var x=0; x < data.paradas.length; x++) {
		  var parada = data.paradas[x];
		  
		  geo = new Object();
		  geo = eval("("+parada.localizacao+")");
		  
		  var geojsonFeature = {
				    "type": "Feature",
				    "properties": {
				    	"id": parada.id.toString(),
				        "next_to": parada.pertoDe,
				        "qtd_linhas": parada.qtdLinhas,
				        "linhas_parada": parada.linhasParada
				    },
				    "geometry": geo
				    
				};
		  
		  var elementLayer = L.geoJson(geojsonFeature, {
				 pointToLayer: function (feature, latlng) {
					 	var coord = [latlng.lat, latlng.lng]; 
					    var lnglat = L.GeoJSON.coordsToLatLng(coord, false);
				        return L.marker(lnglat, {icon: paradaIcon}, geojsonMarkerOptions);
				    },
				    onEachFeature: caracteristicasPonto
			});
		  
		  paradasLayer.push(elementLayer);
	  }
}

function adicionarParada(id){
	
	var featureSelecionado = new Object();
	
	for(var i=0; i<featuresSelecionados.length; i++){
		var feature = featuresSelecionados[i];
		if(feature.properties.id == id){
			featureSelecionado = feature;
			break;
		}
	}
	
	layerGroupParadas.clearLayers();
	paradasLayer = [];
	
	 var elementLayer = L.geoJson(featureSelecionado, {
		 pointToLayer: function (feature, latlng) {
			 	var coord = [latlng.lat, latlng.lng]; 
			    var lnglat = L.GeoJSON.coordsToLatLng(coord, false);
		        return L.marker(lnglat, {icon: paradaSelecionadaIcon}, geojsonMarkerOptions);
		    },
		    onEachFeature: caracteristicasPontoSelecionado
	});
	 
	 if(typeof(layerGroupParadasSelecionadas) != "undefined"){
		 layerGroupParadasSelecionadas.clearLayers();
	 }
	 
	 paradasSelecionadasLayer.push(elementLayer);
	 
	 layerGroupParadasSelecionadas = L.layerGroup(paradasSelecionadasLayer).addTo(map);
	
}



