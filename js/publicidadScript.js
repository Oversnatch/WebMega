/**
 * 
 */
var PublicidadFCNS = {};

ACGSiteScriptsFCNS.resultadoResize = function () {
	var barraScrollInf = 10;
	var theHeight = $("#outputPanelProductos").height() - barraScrollInf; 
	$('#panelPublicidad').height(theHeight);
}