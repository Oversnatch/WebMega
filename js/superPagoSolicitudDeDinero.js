/**
 * 
 */
var SuperPagoSolicitudDeDineroScriptsFCNS = {};

ACGSiteScriptsFCNS.resultadoResize = function () {
	//debugger;
	var theHeight = window.innerHeight;
	
	//bodyPpal
	theHeight = theHeight - $("#headerPpal").outerHeight(true);
	theHeight = theHeight - (($("#panelMnuIconos").length > 0) ? $("#panelMnuIconos").outerHeight(true) : 0);
	theHeight = theHeight - $("#footerPpal").outerHeight(true);

	theHeight = theHeight 
			- (($("#msgsProductos").length > 0) ? $("#msgsProductos").outerHeight(true) : 0)
			- ((($("#panelProductos_header").length > 0) ? parseFloat($(
					"#panelProductos_header").outerHeight(true)) : 0)
					+ (($("#panelProductos").length > 0) ? (parseFloat($(
							"#panelProductos").css("padding-top").replace("px",
							""))
							+ parseFloat($("#panelProductos").css(
									"padding-bottom").replace("px", ""))
							+ parseFloat($("#panelProductos").css(
									"border-top-width").replace("px", "")) + parseFloat($(
							"#panelProductos").css("border-bottom-width")
							.replace("px", "")))
							: 0) + (($("#panelProductos_content").length > 0) ? (parseFloat($(
					"#panelProductos_content").css("padding-top").replace("px",
					""))
					+ parseFloat($("#panelProductos_content").css(
							"padding-bottom").replace("px", ""))
					+ parseFloat($("#panelProductos_content").css(
							"border-top-width").replace("px", "")) + parseFloat($(
					"#panelProductos_content").css("border-bottom-width")
					.replace("px", "")))
					: 0));

	var uiPanelSelUsr = $("#panelSelUsr").outerHeight(true);
	theHeight = theHeight - ((typeof uiPanelSelUsr !== "undefined") ? parseFloat(uiPanelSelUsr) : parseFloat("0"));
	
	$('#outputPanelProductos').height(theHeight);
	
	
	var uiAgendaDestDTHeader = $("#solicitudesDeDineroDT .ui-datatable-header").outerHeight(true);
	var uiAgendaDestDTScrollableHeader = $("#solicitudesDeDineroDT .ui-datatable-scrollable-header").outerHeight(true);
	var uiAgendaDestDTScrollableFooter = $("#solicitudesDeDineroDT .ui-datatable-scrollable-footer").outerHeight(true);
	var uiAgendaDestDTFooter = $("#solicitudesDeDineroDT .ui-datatable-footer").outerHeight(true);
	var uiAgendaDestDTPaginatorBottom = $("#solicitudesDeDineroDT_paginator_bottom").outerHeight(true);
	
	
		
	//debugger;
	if ($("#solicitudesDeDineroDT .ui-datatable-scrollable-body").length > 0) {
		var barraScrollInf = 10;
		var theHeight2 = $("#outputPanelProductos").height()
				- $("#botoneraSolDin").outerHeight(true)
				- (($("#gridFiltroFecha").length > 0) ? $("#gridFiltroFecha").outerHeight(true) : 0)
				- (((typeof uiAgendaDestDTHeader !== "undefined") ? parseFloat(uiAgendaDestDTHeader) : parseFloat("0"))
						+ ((typeof uiAgendaDestDTScrollableHeader !== "undefined") ? parseFloat(uiAgendaDestDTScrollableHeader) : parseFloat("0"))
						+ ((typeof uiAgendaDestDTScrollableFooter !== "undefined") ? parseFloat(uiAgendaDestDTScrollableFooter) : parseFloat("0"))
						+ ((typeof uiAgendaDestDTFooter !== "undefined") ? parseFloat(uiAgendaDestDTFooter) : parseFloat("0"))
						+ ((typeof uiAgendaDestDTPaginatorBottom !== "undefined") ? parseFloat(uiAgendaDestDTPaginatorBottom) : parseFloat("0"))												
						+ parseFloat(barraScrollInf));

		$("#solicitudesDeDineroDT .ui-datatable-scrollable-body").attr("style",
				"max-height: " + theHeight2 + "px;");

		$("#solicitudesDeDineroDT .ui-datatable-scrollable-body").attr("style",
				"height: " + theHeight2 + "px;");
	}
	
	//debugger;
	var uiAgendaDestDTItems = $("#solicitudesDeDineroDTItems .ui-datatable-header").outerHeight(true);
	var uiAgendaDestDTScrollableHeader = $("#solicitudesDeDineroDTItems .ui-datatable-scrollable-header").outerHeight(true);
	var uiAgendaDestDTItemsScrollableFooter = $("#solicitudesDeDineroDTItems .ui-datatable-scrollable-footer").outerHeight(true);
	//var uiDataTableTablaItemsFooter = $("#dataTableTablaItems .ui-datatable-footer").outerHeight(true);
	var uiAgendaDestDTPaginatorBottom = $("#solicitudesDeDineroDTItems_paginator_bottom").outerHeight(true);
	
	
		
	//debugger;
	if ($("#solicitudesDeDineroDTItems .ui-datatable-scrollable-body").length > 0) {
		var barraScrollInf = 10;
		var theHeight2 = $("#outputPanelProductos").height()
				- $("#botoneraSolDin").outerHeight(true)
				- (($("#gridFiltroFecha").length > 0) ? $("#gridFiltroFecha").outerHeight(true) : 0)
				- (((typeof uiAgendaDestDTItemsHeader !== "undefined") ? parseFloat(uiAgendaDestDTItemsHeader) : parseFloat("0"))
						+ ((typeof uiAgendaDestDTItemsScrollableHeader !== "undefined") ? parseFloat(uiAgendaDestDTItemsScrollableHeader) : parseFloat("0"))
						+ ((typeof uiAgendaDestDTItemsScrollableFooter !== "undefined") ? parseFloat(uiAgendaDestDTItemsScrollableFooter) : parseFloat("0"))
						//+ ((typeof uiDataTableTablaItemsFooter !== "undefined") ? parseFloat(uiDataTableTablaItemsFooter) : parseFloat("0"))
						+ ((typeof uiAgendaDestDTItemsTablaPaginatorBottom !== "undefined") ? parseFloat(uiAgendaDestDTItemsPaginatorBottom) : parseFloat("0"))												
						+ parseFloat(barraScrollInf)
						+ 70);

		$("#solicitudesDeDineroDTItems .ui-datatable-scrollable-body").attr("style",
				"max-height: " + theHeight2 + "px;");

		$("#solicitudesDeDineroDTItems .ui-datatable-scrollable-body").attr("style",
				"height: " + theHeight2 + "px;");
	}
	
	//debugger;
	if ($("#solicitudesDeDineroDTMap").length > 0) {
		var barraScrollInf = 10;
		var theHeight2 = $("#outputPanelProductos").height()
				- $("#botoneraSolDin").outerHeight(true)
				- parseFloat(barraScrollInf);

		$("#solicitudesDeDineroDTMap").attr("style",
				"max-height: " + theHeight2 + "px;");

		$("#solicitudesDeDineroDTMap").attr("style",
				"height: " + theHeight2 + "px;");
	}
	
}
