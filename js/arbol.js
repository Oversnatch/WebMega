var ArbolFCNS = {}
/**
 * Saca Highlight del arbol
 */

ArbolFCNS.ocultar = function(idCliente) {
	for (ind = 0; ind <= 6; ind++) {
		if (PrimeFaces.widgets["dtWidgetVar" + ind])
			if (idCliente != ("dtWidgetVar" + ind))
				PF('dtWidgetVar' + ind).unselectAllRows();
	}

	/*
	 * var elements = $('#formArbol .ui-state-highlight');
	 * 
	 * var i; for (i = 0; i < elements.length; i++) { if (idCliente ==
	 * elements[i].getAttribute('data-rk')) { } else {
	 * elements[i].classList.remove('ui-state-highlight'); } }
	 */
}

/**
 * 
 */
ArbolFCNS.arbolResize = function(nivel) {

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
	//theHeight = theHeight - 4;
	
	theHeight = theHeight
			- parseFloat($("#bodyPpal").css("padding-top").replace("px", ""));
	theHeight = theHeight
			- parseFloat($("#bodyPpal").css("padding-bottom").replace("px", ""));
	theHeight = theHeight
			- parseFloat($("#bodyPpal").css("border-top").replace("px", ""));
	theHeight = theHeight
			- parseFloat($("#bodyPpal").css("border-bottom").replace("px", ""));
	$('#bodyPpal').height(theHeight);

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

	$("#contenedorDataArbol").css("maxHeight", theHeight + "px");
	$('#contenedorDataArbol').height(theHeight);

	if ($("#gridContArbol").length > 0) {
		// gridContArbol
		theHeight = theHeight
				- parseFloat($("#contenedorDataArbol").css("borderTopWidth")
						.replace("px", ""));
		theHeight = theHeight
				- parseFloat($("#contenedorDataArbol").css("borderBottomWidth")
						.replace("px", ""));
		theHeight = theHeight
				- parseFloat($("#contenedorDataArbol").css("padding-top")
						.replace("px", ""));
		theHeight = theHeight
				- parseFloat($("#contenedorDataArbol").css("padding-bottom")
						.replace("px", ""));
		$("#gridContArbol").css("maxHeight", theHeight + "px");
		$('#gridContArbol').height(theHeight);

		// arbolNivel1, arbolNivel2, arbolNivel3, arbolNivel4, arbolNivel5
		theHeight = theHeight - 10;

		$("#arbolNivel1").css("maxHeight", theHeight + "px");
		$('#arbolNivel1').height(theHeight);
		$("#arbolNivel2").css("maxHeight", theHeight + "px");
		$('#arbolNivel2').height(theHeight);
		$("#arbolNivel3").css("maxHeight", theHeight + "px");
		$('#arbolNivel3').height(theHeight);
		$("#arbolNivel4").css("maxHeight", theHeight + "px");
		$('#arbolNivel4').height(theHeight);
		$("#arbolNivel5").css("maxHeight", theHeight + "px");
		$('#arbolNivel5').height(theHeight);

		if (nivel == 0) {
			PF('dtWidgetVar1').scrollBody.scrollTop(1);
			PF('dtWidgetVar1').allLoadedLiveScroll = false;
			PF('dtWidgetVar1').scrollOffset = 0;
			PF('dtWidgetVar1').clearScrollState();
			PF('dtWidgetVar1').adjustScrollHeight();
		}
		
		if (nivel == 0 || 1 >= nivel) {
			PF('dtWidgetVar2').scrollBody.scrollTop(1);
			PF('dtWidgetVar2').allLoadedLiveScroll = false;
			PF('dtWidgetVar2').scrollOffset = 0;
			PF('dtWidgetVar2').clearScrollState();
			PF('dtWidgetVar2').adjustScrollHeight();
		}
		
		if (nivel == 0 || 2 >= nivel) {
			PF('dtWidgetVar3').scrollBody.scrollTop(1);
			PF('dtWidgetVar3').allLoadedLiveScroll = false;
			PF('dtWidgetVar3').scrollOffset = 0;
			PF('dtWidgetVar3').clearScrollState();
			PF('dtWidgetVar3').adjustScrollHeight();
		}
		
		if (nivel == 0 || 3 >= nivel) {
			PF('dtWidgetVar4').scrollBody.scrollTop(1);
			PF('dtWidgetVar4').allLoadedLiveScroll = false;
			PF('dtWidgetVar4').scrollOffset = 0;
			PF('dtWidgetVar4').clearScrollState();
			PF('dtWidgetVar4').adjustScrollHeight();
		}
		
		if (nivel == 0 || 4 >= nivel) {
			PF('dtWidgetVar5').scrollBody.scrollTop(1);
			PF('dtWidgetVar5').allLoadedLiveScroll = false;
			PF('dtWidgetVar5').scrollOffset = 0;
			PF('dtWidgetVar5').clearScrollState();
			PF('dtWidgetVar5').adjustScrollHeight();
		}
	} else {
		$("#resuBusCliDT").css("maxHeight", theHeight + "px");
		$('#resuBusCliDT').height(theHeight);
		
		PF('resuBusCliDTWV').scrollBody.scrollTop(1);
		PF('resuBusCliDTWV').allLoadedLiveScroll = false;
		PF('resuBusCliDTWV').scrollOffset = 0;
		PF('resuBusCliDTWV').clearScrollState();
		PF('resuBusCliDTWV').adjustScrollHeight();
	}

	if (typeof (AdministracionFCNS) !== 'undefined')
		AdministracionFCNS.productoResize();

}

ArbolFCNS.clearFilters = function(nivel) {
	//-----------------------------------------------------
	//Para que funcione esta funcion es necesario agregarle la clase ui-column-acg-filter al componente
	//que se utiliza como input para el filtro en el Facet
	//-----------------------------------------------------
	var tblNivel1;
	var tblNivel2;
	var tblNivel3;
	var tblNivel4;
	var tblNivel5;
	
	if (nivel == 0) {
		tblNivel1 = PF('dtWidgetVar1');
		if(tblNivel1 !== undefined)
			tblNivel1.thead.find('> tr > th.ui-filter-column > .ui-column-customfilter > .ui-column-acg-filter').val('');
	}
	
	if (nivel == 0 || 1 >= nivel){ 
		tblNivel2 = PF('dtWidgetVar2');
		if(tblNivel2 !== undefined)
			tblNivel2.thead.find('> tr > th.ui-filter-column > .ui-column-customfilter > .ui-column-acg-filter').val('');
	}
	
	if (nivel == 0 || 2 >= nivel){
		tblNivel3 = PF('dtWidgetVar3');
		if(tblNivel3 !== undefined)
			tblNivel3.thead.find('> tr > th.ui-filter-column > .ui-column-customfilter > .ui-column-acg-filter').val('');
	}
	
	if (nivel == 0 || 3 >= nivel){
		tblNivel4 = PF('dtWidgetVar4');
		if(tblNivel4 !== undefined)
			tblNivel4.thead.find('> tr > th.ui-filter-column > .ui-column-customfilter > .ui-column-acg-filter').val('');
	}
	
	if (nivel == 0 || 4 >= nivel){
		tblNivel5 = PF('dtWidgetVar5');
		if(tblNivel5 !== undefined)
			tblNivel5.thead.find('> tr > th.ui-filter-column > .ui-column-customfilter > .ui-column-acg-filter').val('');
	}
	
	PF('blockPanelGral').show();
	
	if(nivel == 0 || nivel == 5)
		if(tblNivel5 !== undefined)
			tblNivel5.filter();

	if(nivel == 0 || nivel == 4)
		if(tblNivel4 !== undefined)
			tblNivel4.filter();

	if(nivel == 0 || nivel == 3)
		if(tblNivel3 !== undefined)
			tblNivel3.filter();
	
	if(nivel == 0 || nivel == 2)
		if(tblNivel2 !== undefined)
			tblNivel2.filter();

	if(nivel == 0 || nivel == 1)
		if(tblNivel1 !== undefined)
			tblNivel1.filter();
	
	PF('blockPanelGral').hide();
}

ArbolFCNS.unselectAllRows = function() {
	for (ind = 0; ind <= 6; ind++) {
		if (PrimeFaces.widgets["dtWidgetVar" + ind])
			PF('dtWidgetVar' + ind).unselectAllRows();
	}
}

$(window).on("resize", ArbolFCNS.arbolResize);

$(document).ready(function() {
	ArbolFCNS.arbolResize(0);
});
