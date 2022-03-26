/**
 * 
 */
var SuperPagoAgendaDestinatariosScriptsFCNS = {};

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
	
	
	var uiAgendaDestDTHeader = $("#agendaDestDT .ui-datatable-header").outerHeight(true);
	var uiAgendaDestDTScrollableHeader = $("#agendaDestDT .ui-datatable-scrollable-header").outerHeight(true);
	var uiAgendaDestDTScrollableFooter = $("#agendaDestDT .ui-datatable-scrollable-footer").outerHeight(true);
	var uiAgendaDestDTFooter = $("#agendaDestDT .ui-datatable-footer").outerHeight(true);
	var uiAgendaDestDTPaginatorBottom = $("#agendaDestDT_paginator_bottom").outerHeight(true);
	
	
		
	//debugger;
	if ($("#agendaDestDT .ui-datatable-scrollable-body").length > 0) {
		var barraScrollInf = 10;
		var theHeight2 = $("#outputPanelProductos").height()
				- $("#botoneraAgDest").outerHeight(true)
				- (((typeof uiAgendaDestDTHeader !== "undefined") ? parseFloat(uiAgendaDestDTHeader) : parseFloat("0"))
						+ ((typeof uiAgendaDestDTScrollableHeader !== "undefined") ? parseFloat(uiAgendaDestDTScrollableHeader) : parseFloat("0"))
						+ ((typeof uiAgendaDestDTScrollableFooter !== "undefined") ? parseFloat(uiAgendaDestDTScrollableFooter) : parseFloat("0"))
						+ ((typeof uiAgendaDestDTFooter !== "undefined") ? parseFloat(uiAgendaDestDTFooter) : parseFloat("0"))
						+ ((typeof uiAgendaDestDTPaginatorBottom !== "undefined") ? parseFloat(uiAgendaDestDTPaginatorBottom) : parseFloat("0"))												
						+ parseFloat(barraScrollInf));

		$("#agendaDestDT .ui-datatable-scrollable-body").attr("style",
				"max-height: " + theHeight2 + "px;");

		$("#agendaDestDT .ui-datatable-scrollable-body").attr("style",
				"height: " + theHeight2 + "px;");
	}
	
	//debugger;
	var uiAgendaDestDTItems = $("#agendaDestDTItems .ui-datatable-header").outerHeight(true);
	var uiAgendaDestDTScrollableHeader = $("#agendaDestDTItems .ui-datatable-scrollable-header").outerHeight(true);
	var uiAgendaDestDTItemsScrollableFooter = $("#agendaDestDTItems .ui-datatable-scrollable-footer").outerHeight(true);
	//var uiDataTableTablaItemsFooter = $("#dataTableTablaItems .ui-datatable-footer").outerHeight(true);
	var uiAgendaDestDTPaginatorBottom = $("#agendaDestDTItems_paginator_bottom").outerHeight(true);
	
	
		
	//debugger;
	if ($("#agendaDestDTItems .ui-datatable-scrollable-body").length > 0) {
		var barraScrollInf = 10;
		var theHeight2 = $("#outputPanelProductos").height()
				- $("#botoneraAgDest").outerHeight(true)
				- (((typeof uiAgendaDestDTItemsHeader !== "undefined") ? parseFloat(uiAgendaDestDTItemsHeader) : parseFloat("0"))
						+ ((typeof uiAgendaDestDTItemsScrollableHeader !== "undefined") ? parseFloat(uiAgendaDestDTItemsScrollableHeader) : parseFloat("0"))
						+ ((typeof uiAgendaDestDTItemsScrollableFooter !== "undefined") ? parseFloat(uiAgendaDestDTItemsScrollableFooter) : parseFloat("0"))
						//+ ((typeof uiDataTableTablaItemsFooter !== "undefined") ? parseFloat(uiDataTableTablaItemsFooter) : parseFloat("0"))
						+ ((typeof uiAgendaDestDTItemsTablaPaginatorBottom !== "undefined") ? parseFloat(uiAgendaDestDTItemsPaginatorBottom) : parseFloat("0"))												
						+ parseFloat(barraScrollInf)
						+ 70);

		$("#agendaDestDTItems .ui-datatable-scrollable-body").attr("style",
				"max-height: " + theHeight2 + "px;");

		$("#agendaDestDTItems .ui-datatable-scrollable-body").attr("style",
				"height: " + theHeight2 + "px;");
	}
	
	//debugger;
	if ($("#agendaDestDTMap").length > 0) {
		var barraScrollInf = 10;
		var theHeight2 = $("#outputPanelProductos").height()
				- $("#botoneraAgDest").outerHeight(true)
				- parseFloat(barraScrollInf);

		$("#agendaDestDTMap").attr("style",
				"max-height: " + theHeight2 + "px;");

		$("#agendaDestDTMap").attr("style",
				"height: " + theHeight2 + "px;");
	}
	
}
