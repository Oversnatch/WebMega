var PuntoDeVentaFCNS = {};

/**
 * 
 */
PuntoDeVentaFCNS.cambiaFondoMsg = function (idComponente, colorLetra, colorFondo, colorBorde,
		imagen) {
	$("#" + idComponente + " .ui-messages-info").css("color", colorLetra);
	$("#" + idComponente + " .ui-messages-info")
			.css("border-color", colorBorde);
	$("#" + idComponente + " .ui-messages-info").css("background-color",
			colorFondo);
	$("#" + idComponente + " .ui-messages-info-icon").attr("style", "background: url(" + imagen + ".xhtml) no-repeat !important;");
	$("#" + idComponente + " .ui-messages-info-icon").addClass('no-after');
	$("#" + idComponente + " .ui-messages-info ui-corner-all").addClass('ui-messages');
	$("#" + idComponente + " .ui-messages-info ui-corner-all").removeClass('ui-messages-error');

	
	$("#" + idComponente + " .ui-messages-error").css("color", colorLetra);
	$("#" + idComponente + " .ui-messages-error").css("border-color", colorBorde);
	$("#" + idComponente + " .ui-messages-error").css("background-color", colorFondo);
	$("#" + idComponente + " .ui-messages-error-icon").attr("style", "background: url(" + imagen + ".xhtml) no-repeat !important;");
	$("#" + idComponente + " .ui-messages-error-icon").addClass('no-after');	
	$("#" + idComponente + " .ui-messages-error ui-corner-all").addClass('ui-messages');
	$("#" + idComponente + " .ui-messages-error ui-corner-all").removeClass('ui-messages-error');

	
	
	$("#" + idComponente + " .ui-messages-warn").css("color", colorLetra);
	$("#" + idComponente + " .ui-messages-warn").css("border-color", colorBorde);
	$("#" + idComponente + " .ui-messages-warn").css("background-color", colorFondo);
	$("#" + idComponente + " .ui-messages-warn-icon").attr("style", "background: url(" + imagen + ".xhtml) no-repeat !important;");
	$("#" + idComponente + " .ui-messages-warn-icon").addClass('no-after');
	$("#" + idComponente + " .ui-messages-warn ui-corner-all").addClass('ui-messages');	
	$("#" + idComponente + " .ui-messages-warn ui-corner-all").removeClass('ui-messages-error');
	
	
	$("#" + idComponente + " .ui-messages-fatal").css("color", colorLetra);
	$("#" + idComponente + " .ui-messages-fatal").css("border-color", colorBorde);
	$("#" + idComponente + " .ui-messages-fatal").css("background-color", colorFondo);
	$("#" + idComponente + " .ui-messages-fatal-icon").attr("style", "background: url(" + imagen + ".xhtml) no-repeat !important;");
	$("#" + idComponente + " .ui-messages-fatal-icon").addClass('no-after');
	$("#" + idComponente + " .ui-messages-error ui-corner-all").addClass('ui-messages');
	$("#" + idComponente + " .ui-messages-error ui-corner-all").removeClass('ui-messages-error');
	
	
}

PuntoDeVentaFCNS.monitorPVResize = function () {
	//debugger;
	var theHeight = window.innerHeight;
	
	//bodyPpal
	theHeight = theHeight - $("#headerPpal").outerHeight(true);
	theHeight = theHeight - $("#footerPpal").outerHeight(true);
	
	theHeight = theHeight 
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
	
	if ($("#tablaMonitorTRXSPV .ui-datatable-scrollable-body").length > 0) {
		var barraScrollInf = 15;
		var theHeight2 = $("#outputPanelProductos").height()
				- ($("#tablaMonitorTRXSPV .ui-datatable-header").outerHeight(true)
						+ $("#tablaMonitorTRXSPV .ui-datatable-scrollable-header")
								.outerHeight(true)
						+ $("#tablaMonitorTRXSPV .ui-datatable-scrollable-footer-box").outerHeight(
								true)
						+ $("#tablaMonitorTRXSPV_paginator_bottom").outerHeight(
								true) + barraScrollInf);

		$("#tablaMonitorTRXSPV .ui-datatable-scrollable-body").attr("style",
				"max-height: " + theHeight2 + "px;");

		$("#tablaMonitorTRXSPV .ui-datatable-scrollable-body").attr("style",
				"height: " + theHeight2 + "px;");
	}
}

PuntoDeVentaFCNS.productoResize = function () {
	//debugger;
	var theHeight = window.innerHeight;
	
	//bodyPpal
	theHeight = theHeight - $("#headerPpal").outerHeight(true);
	theHeight = theHeight - $("#footerPpal").outerHeight(true);
	
	theHeight = theHeight 
			- (($("#msgsProductos").length > 0) ? $("#msgsProductos").outerHeight(true) : 0)
			- (($("#mnuIconos").length > 0) ? $("#mnuIconos").outerHeight(true) : 0)
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

	//theHeight = theHeight - 50;
	
	$('#outputPanelProductos').height(theHeight);
	
	PuntoDeVentaFCNS.resultadoResize();
}

PuntoDeVentaFCNS.puntoVentaResize = function () {
	
	var theHeight = window.innerHeight;

	if (1024 > window.innerWidth) { 
		/* Horizontal Scrollbar! */
		theHeight = theHeight - 17;
	} 
	
	//bodyPpal
	theHeight = theHeight - $("#headerPpal").outerHeight(true);
	theHeight = theHeight - $("#footerPpal").outerHeight(true);
	
	//5 pixeles de pading superior
	//fuerzo a 4 pixeles por diferencias de calculos
	theHeight = theHeight - 4;
	
	$('#bodyPpal').height(theHeight);
}


PuntoDeVentaFCNS.cierraPanelProducto = function () {
	if ($("#panelProductos").length > 0)
		PF('panelProductosWV').close();
}

PuntoDeVentaFCNS.refreshMonPV = function () {
	if ($("#panelGroupMonPV").length > 0)
		refreshMonProductosOnlinePV();
}

PuntoDeVentaFCNS.resultadoResize = function () {

}

window.addEventListener("resize", PuntoDeVentaFCNS.puntoVentaResize);
window.addEventListener("resize", PuntoDeVentaFCNS.productoResize);
window.addEventListener("resize", PuntoDeVentaFCNS.monitorPVResize);

$(document).ready(function() {
	PuntoDeVentaFCNS.puntoVentaResize();
	PuntoDeVentaFCNS.productoResize();
	PuntoDeVentaFCNS.monitorPVResize();
});


PuntoDeVentaFCNS.log = function () {
}