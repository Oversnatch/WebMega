/* Initialize para mapa de un solo punto */

function initialize(address, titulo, tipo, geo_latitud, geo_longitud, tipoGeoLoc, imagenDelLocal) {

	geocoder = new google.maps.Geocoder();

	var mapOptions = {
		zoom : 8
	}

	map_s = new google.maps.Map(document.getElementById("map_canvas_s"),
			mapOptions);
	
	icono = 'http://maps.google.com/mapfiles/ms/icons/red-dot.png';
	switch (tipo) {
	case "M":
		icono = 'http://maps.google.com/mapfiles/ms/icons/green-dot.png'
		break;
	case "D":
		icono = 'http://maps.google.com/mapfiles/ms/icons/yellow-dot.png'
		break;
	case "P":
		icono = 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'
		break;
	case "4":
		icono = 'http://maps.google.com/mapfiles/ms/icons/purple-dot.png'
		break;
	case "5":
		icono = 'http://maps.google.com/mapfiles/ms/icons/red-dot.png'
		break;
	default:
		icono = 'http://maps.google.com/mapfiles/ms/icons/red-dot.png';
	}
	
	codeAddress(address, map_s, titulo, icono, geo_latitud, geo_longitud, tipoGeoLoc, imagenDelLocal);

	// refresco del mapa
	setTimeout(function() {
		var centro = map_s.getCenter();
		google.maps.event.trigger(map_s, "resize");
		map_s.setCenter(centro);
	}, 500);

}

/* Initialize para mapa con multiples puntos */
function initializeMapaCompleto(provinciaPais, tipoGeoLoc) {
	
	var myLatlng = new google.maps.LatLng(-35.039094, -64.126264);
	var mapOptions = {
		zoom : 4,
		center : myLatlng
	}
	var map_m = new google.maps.Map(document.getElementById('map-canvas'),
			mapOptions);
	var puntosLength = puntos.length;
	var i = 0;
	
	var geocoder = new google.maps.Geocoder();
	geocoder.geocode({
		'address' : provinciaPais
	}, function(results, status) {
		if (status == google.maps.GeocoderStatus.OK) {
			myLatlng = results[0].geometry.location;
			map_m.setCenter(myLatlng);
		}
	});
	
	for (i = 0; i < puntosLength; i++) {
		icono = 'http://maps.google.com/mapfiles/ms/icons/red-dot.png';
		switch (puntos[i][2]) {
		case "M":
			icono = 'http://maps.google.com/mapfiles/ms/icons/green-dot.png'
			break;
		case "D":
			icono = 'http://maps.google.com/mapfiles/ms/icons/yellow-dot.png'
			break;
		case "P":
			icono = 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'
			break;
		case "4":
			icono = 'http://maps.google.com/mapfiles/ms/icons/purple-dot.png'
			break;
		case "5":
			icono = 'http://maps.google.com/mapfiles/ms/icons/red-dot.png'
			break;
		default:
			icono = 'http://maps.google.com/mapfiles/ms/icons/red-dot.png';
		}
		codeAddressMultiple (puntos[i][0], map_m, puntos[i][1], icono, puntos[i][3], puntos[i][4], tipoGeoLoc, puntos[i][5]);  
	}
	// refresco del mapa
	setTimeout(function() {
		var centro = map_m.getCenter();
		google.maps.event.trigger(map_m, "resize");
		map_m.setCenter(centro);
	}, 2000);
}

// Agregar evento click con contenido html
function attachSecretMessage(marker, html) {
	var infowindow = new google.maps.InfoWindow({
		content : html
	});
	google.maps.event.addListener(marker, 'click', function() {
		infowindow.open(marker.get('map'), marker);
	});
}

// codificar direccion a lat y lon
function codeAddress(address, map, titulo, icono, geo_latitud, geo_longitud, tipoGeoLoc, imagenDelLocal) {
	
	if (tipoGeoLoc == 2){
		map.setCenter(new google.maps.LatLng(geo_latitud, geo_longitud));
		var marker = new google.maps.Marker({
			map : map,
			position : new google.maps.LatLng(geo_latitud, geo_longitud),
			title : titulo,
			icon : icono
		});
		attachSecretMessage(marker, "<b>Razon Social: </b>" + titulo
				+ " <br /> " + "<b>Direccion: </b>" + address
				+ " <br /> " + "<div style=\"text-align:center;\"> <img src=" + imagenDelLocal + " alt=\"" + "La imagen no esta disponible" + "\" style=\"max-width:640px;\"> </div>");
		
	}else{
	
		geocoder.geocode({
			'address' : address
		}, function(results, status) {
			if (status == google.maps.GeocoderStatus.OK) {
				map.setCenter(results[0].geometry.location);
				var marker = new google.maps.Marker({
					map : map,
					position : results[0].geometry.location,
					title : titulo,				
					icon : icono
				});
				attachSecretMessage(marker, "<b>Razon Social: </b>" + titulo 
						+ " <br /> " + "<b>Direccion: </b>" + address 
						+ " <br /> " + "<div style=\"text-align:center;\"> <img src=" + imagenDelLocal + " alt=\"" + "La imagen no esta disponible" + "\" style=\"max-width:640px;\"> </div>");
			} else {
				alert("No se pudo instanciar geocoder por la siguiente razon: "
						+ status);
			}
		});
	}
}

//codificar direccion a lat y lon
function codeAddressMultiple(address, map, titulo, icono, geo_latitud, geo_longitud, tipoGeoLoc, imagenDelLocal) {

	if (tipoGeoLoc == 2){
		var marker = new google.maps.Marker({
			map : map,
			position : new google.maps.LatLng(geo_latitud, geo_longitud),
			title : titulo,
			icon : icono
		});
		attachSecretMessage(marker, "<b>Razon Social: </b>" + titulo
				+ " <br /> " + "<b>Direccion: </b>" + address
				+ " <br /> " + "<div style=\"text-align:center;\"> <img src=" + imagenDelLocal + " alt=\"" + "La imagen no esta disponible" + "\" style=\"max-width:640px;\"> </div>");
		
	}else{
		
		var geocoder = new google.maps.Geocoder();	
		geocoder.geocode({
			'address' : address
		}, function(results, status) {
			if (status == google.maps.GeocoderStatus.OK) {
				var marker = new google.maps.Marker({
					map : map,
					position : results[0].geometry.location,
					title : titulo,
					icon : icono
				});
				attachSecretMessage(marker, "<b>Razon Social: </b>" + titulo
						+ " <br /> " + "<b>Direccion: </b>" + address
						+ " <br /> " + "<div style=\"text-align:center;\"> <img src=" + imagenDelLocal + " alt=\"" + "La imagen no esta disponible" + "\" style=\"max-width:640px;\"> </div>");
			}
		});
	}
}