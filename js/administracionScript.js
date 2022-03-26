var AdministracionFCNS = {};
/**
 * 
 */
AdministracionFCNS.cambiaFondoMsg = function (idComponente, colorLetra, colorFondo, colorBorde,
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

AdministracionFCNS.productoResize = function () {
	//debugger;
	var panel = $('#outputPanelProductos');
	
	if (typeof (panel) !== 'undefined') {
		var theHeight = $("#bodyPpal").height()
				- (($("#panelSelUsr").length > 0) ? $("#panelSelUsr").outerHeight(true) : 0)
				- (($("#mnuIconos").length > 0) ? $("#mnuIconos").outerHeight(true) : 0)
				- ((($("#panelProductos_header").length > 0) ? parseFloat($(
						"#panelProductos_header").outerHeight(true)) : 0)
						+ (($("#panelProductos").length > 0) ? (parseFloat($(
								"#panelProductos").css("padding-top").replace(
								"px", ""))
								+ parseFloat($("#panelProductos").css(
										"padding-bottom").replace("px", ""))
								+ parseFloat($("#panelProductos").css(
										"border-top-width").replace("px", "")) + parseFloat($(
								"#panelProductos").css("border-bottom-width")
								.replace("px", "")))
								: 0) + (($("#panelProductos_content").length > 0) ? (parseFloat($(
						"#panelProductos_content").css("padding-top").replace(
						"px", ""))
						+ parseFloat($("#panelProductos_content").css(
								"padding-bottom").replace("px", ""))
						+ parseFloat($("#panelProductos_content").css(
								"border-top-width").replace("px", "")) + parseFloat($(
						"#panelProductos_content").css("border-bottom-width")
						.replace("px", "")))
						: 0));

		$('#outputPanelProductos').height(theHeight);

		AdministracionFCNS.resultadoResize();
	}
}

AdministracionFCNS.cierraPanelProducto = function () {
	if ($("#panelProductos").length > 0)
		PF('panelProductosWV').close();
}

AdministracionFCNS.resultadoResize = function () {

}

window.addEventListener("resize", ACGSiteScriptsFCNS.resultadoResize);

$(document).ready(function() {
	ACGSiteScriptsFCNS.resultadoResize();
});

AdministracionFCNS.deshabilitaSeleccionMenu = function () {
	$('#admTabMenu .ui-state-active').removeClass('ui-state-active');
}