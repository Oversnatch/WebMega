/**
 * 
 */
	function handleKeyDown(event) {
		if (event.key == 'F2'){
			document.getElementById('btnCONTINUAR').click();
		} else if (event.key == 'F4'){
			document.getElementById('btnLimpiarLista').click();	
		} else if (event.key == 'F7'){
			document.getElementById('btnManual').click();	
		} else if (event.key == 'F8'){
			document.getElementById('btnBarra').click();	
		} else if (event.key == 'F9'){
			document.getElementById('btnConfirmarPagos').click();	
		} else if (event.key == 'F10'){
			document.getElementById('btnMPConfirmarPagos').click();	
		} 
	};
	
	document.addEventListener('keydown', handleKeyDown);
	
	function enFocus(componente){
		document.getElementById(componente).focus();
	};

	
	var ResultadoPNetFCNS = {};
	/**
	 * 
	 */

	ACGSiteScriptsFCNS.resultadoResize = function() {
		//debugger;
			//Agregar la definicion para modificar el alto del menu lateral
		$('#contenedorTabla').height($('#outputPanelProductos').height() - (($("#msgsProductos").length > 0) ? $("#msgsProductos").outerHeight(true) : 0) - 15);
		$('#contenedorMenu').height($('#contenedorTabla').height() - $('#contenedorEstado').height() - 8);
	};