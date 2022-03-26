/**
 * 
 */
var gcsiScriptsFCNS = {};

gcsiScriptsFCNS.handleKeyDown = function(event) {
	if (event.key == 'F2') {
		$('#btnCONTINUAR').click();
	} else if (event.key == 'F4') {
		$('#btnLimpiarLista').click();
	} else if (event.key == 'F7') {
		$('#btnManual').click();
	} else if (event.key == 'F8') {
		$('#btnBarra').click();
	} else if (event.key == 'F9') {
		$('#btnConfirmarPagos').click();
	} else if (event.key == 'F10') {
		$('#btnMPConfirmarPagos').click();
	}
};

ACGSiteScriptsFCNS.resultadoResize = function() {
	// debugger;
	// Agregar la definicion para modificar el alto del menu lateral
	$('#contenedorTabla').height(
			$('#outputPanelProductos').height()
					- (($("#msgsProductos").length > 0) ? $("#msgsProductos")
							.outerHeight(true) : 0) - 15);
	$('#contenedorMenu').height(
			$('#contenedorTabla').height() - $('#contenedorEstado').height()
					- 8);
};

gcsiScriptsFCNS.procesarSinFacGIRE = function(){
	var camposGIRE = {
		  objetos: []
	};

	$('.camposGIRE').each(function(i, obj) {
		var pagoSinFact = {};
		pagoSinFact.etiqueta = $('.etiquetaGIRE', obj).text();
		pagoSinFact.nombre = $('.nombreCampoGIRE', obj).attr("valor");
		pagoSinFact.tipo = $('.nombreCampoGIRE', obj).attr("tipo");
		
		if($('.nombreCampoGIRE', obj).attr("tipo") == "COMBO_BOX_ESTATICO" || $('.nombreCampoGIRE', obj).attr("tipo") == "COMBO_BOX_DINAMICO")
			pagoSinFact.valor = $('.valorCampoGIRE option:selected', obj).val();
		else
			pagoSinFact.valor = $('.valorCampoGIRE', obj).val();
		
		camposGIRE.objetos.push(pagoSinFact);
	});
	
	var jsonCompleto = JSON.stringify(camposGIRE.objetos);
	
	callScript([{name:'parametro', value:'cob'}, {name:'tipo', value:'proccobman'}, {name:'valor', value: jsonCompleto}, {name:'texto', value:''}]);	
}

document.addEventListener('keydown', gcsiScriptsFCNS.handleKeyDown);
