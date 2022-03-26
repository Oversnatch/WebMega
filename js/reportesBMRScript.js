/**
 * 
 */
var ReportesBMRFCNS = {};

ACGSiteScriptsFCNS.resultadoResize = function () {
	//debugger;
	var theHeight = window.innerHeight;
	
	//bodyPpal
	theHeight = theHeight - $("#headerPpal").outerHeight(true);
	theHeight = theHeight - $("#panelMnuIconos").outerHeight(true);
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

	$('#outputPanelProductos').height(theHeight);
	
	
	var uiDatatableHeader = $("#dataTableTabla .ui-datatable-header").outerHeight(true);
	var uiDatatableScrollableHeader = $("#dataTableTabla .ui-datatable-scrollable-header").outerHeight(true);
	var uiDatatableScrollableFooter = $("#dataTableTabla .ui-datatable-scrollable-footer").outerHeight(true);
	var uiDatatableFooter = $("#dataTableTabla .ui-datatable-footer").outerHeight(true);
	var uiDataTableTablaPaginatorBottom = $("#dataTableTabla_paginator_bottom").outerHeight(true);
	
		
	//debugger;
	if ($("#dataTableTabla .ui-datatable-scrollable-body").length > 0) {
		var barraScrollInf = 10;
		var theHeight2 = $("#outputPanelProductos").height()
				- $("#panelFiltro").height()
				- (((typeof uiDatatableHeader !== "undefined") ? parseFloat(uiDatatableHeader) : parseFloat("0"))
						+ ((typeof uiDatatableScrollableHeader !== "undefined") ? parseFloat(uiDatatableScrollableHeader) : parseFloat("0"))
						+ ((typeof uiDatatableScrollableFooter !== "undefined") ? parseFloat(uiDatatableScrollableFooter) : parseFloat("0"))
						+ ((typeof uiDatatableFooter !== "undefined") ? parseFloat(uiDatatableFooter) : parseFloat("0"))
						+ ((typeof uiDataTableTablaPaginatorBottom !== "undefined") ? parseFloat(uiDataTableTablaPaginatorBottom) : parseFloat("0"))												
						+ parseFloat(barraScrollInf))
				- (($("#botonesInferiores").length > 0) ? $("#botonesInferiores").outerHeight(true) : 0);						
						;

		$("#dataTableTabla .ui-datatable-scrollable-body").attr("style",
				"max-height: " + theHeight2 + "px;");

		$("#dataTableTabla .ui-datatable-scrollable-body").attr("style",
				"height: " + theHeight2 + "px;");
	}
}
