var AdministracionUsuariosFCNS = {};

/**
 * 
 */

ACGSiteScriptsFCNS.resultadoResize = function() {
	 //Valida que exista el contenedor. Esto debido a que resultadoResize() se llama siempre desde 
	//el arbol.
	if ($('#usuariosAdminDT').length > 0){ 
		theHeight = $('#outputPanelProductos').height();
		theHeight = theHeight - 15;
		theHeight = theHeight - $('#botoneraUsuario').height();
		$("#usuariosAdminDT").css("maxHeight", theHeight + "px");
		$('#usuariosAdminDT').height(theHeight);

		theHeight = theHeight
				- $(
						'#usuariosAdminDT .ui-widget-header.ui-datatable-scrollable-header')
						.height();
		$('#usuariosAdminDT .ui-datatable-scrollable-body').css("height",
				theHeight + "px");

		PF('usuariosAdminDTWV').scrollBody.scrollTop(1);
		PF('usuariosAdminDTWV').allLoadedLiveScroll = false;
		PF('usuariosAdminDTWV').scrollOffset = 0;
		PF('usuariosAdminDTWV').clearScrollState();
		PF('usuariosAdminDTWV').adjustScrollHeight();
	}
}