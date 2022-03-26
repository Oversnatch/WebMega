var ResultadoBusquedaClienteFCNS = {}
/**
 * 
 */

AdministracionFCNS.resultadoResize = function() {

	var theHeight = window.innerHeight;
	var theWidth = $("#bodyPpal").width();
	
	if (1024 > window.innerWidth) { 
		/* Horizontal Scrollbar! */
		theHeight = theHeight - 17;
	} 
	
	// bodyPpal
	theHeight = theHeight - $("#headerPpal").outerHeight(true);
	theHeight = theHeight - $("#footerPpal").outerHeight(true);

	//5 pixeles de pading superior
	//fuerzo a 4 pixeles por diferencias de calculos
	theHeight = theHeight - 4;
	
	theHeight = theHeight
			- parseFloat($("#bodyPpal").css("padding-top").replace("px", ""));
	theHeight = theHeight
			- parseFloat($("#bodyPpal").css("padding-bottom").replace("px", ""));
	theHeight = theHeight
			- parseFloat($("#bodyPpal").css("border-top").replace("px", ""));
	theHeight = theHeight
			- parseFloat($("#bodyPpal").css("border-bottom").replace("px", ""));
	$('#bodyPpal').height(theHeight);

	// contSelUsr

	// contSelUsr_content
	theHeight = theHeight - $("#panelSelUsr_header").outerHeight(true);
	theHeight = theHeight
			- $("#panelSelUsr_content").css("borderBottomWidth").replace("px",
					"");

	// contenedorDataArbol
	theHeight = theHeight
			- parseFloat($("#panelSelUsr_content").css("padding-top").replace(
					"px", ""));
	theHeight = theHeight
			- parseFloat($("#panelSelUsr_content").css("padding-bottom")
					.replace("px", ""));

	$("#contSelUsr").css("maxHeight", theHeight + "px");
	$("#contSelUsr").height(theHeight);

	// arbolNivel1, arbolNivel2, arbolNivel3, arbolNivel4, arbolNivel5
	theHeight = theHeight - 10;
	$("#resuBusCliDT").css("maxHeight", theHeight + "px");
	$('#resuBusCliDT').height(theHeight);
}

$(window).on("resize", AdministracionFCNS.resultadoResize);

$(document).ready(function() {
	AdministracionFCNS.resultadoResize();
});
