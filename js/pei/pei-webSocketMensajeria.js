/*
 * SISTEMA DE MENSAJERIA
 */

function mensajeriaConnect(){
	if(debug == "true"){
		debugger;
	}
	
	if((webSocketMensajeria == undefined ) || (typeof webSocketMensajeria === 'undefined')){
	//if(webSocketMensajeria == undefined){
		webSocketMensajeria = new WebSocket(wsUriMSG);
		//webSocketMensajeria.onopen = function(evt) { SUBE_open_mensajeria(evt) };
		//webSocketMensajeria.onclose = function(evt) { SUBE_AT_close_chequearVersion(evt) };
		webSocketMensajeria.onerror = function(evt) { PEI_error_mensajeria(evt) };
		webSocketMensajeria.onmessage = function(evt) { PEI_message_mensajeria(evt) };
	}
}

function PEI_error_mensajeria(evt){
	if(debug == "true"){
		debugger;
	}	
	if(typeof evt.data === 'undefined'){
		//writeToScreen(SERVICIO_NO_DISPONIBLE);	
	}else{
		writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);	
	}
	if((webSocketMensajeria != undefined ) || (typeof webSocketMensajeria !== 'undefined')){
	//if(webSocketMensajeria != undefined){
		webSocketMensajeria.close();
		webSocketMensajeria = undefined;
	}
}

function PEI_message_mensajeria(evt){
	if(debug == "true"){
		debugger;
	}	
	var myObj = JSON.parse(evt.data);
	if(myObj.codigo == "MSG"){
		if(myObj.tipo == "C"){
			writeToScreenClear(myObj.mensaje);	
		}else{
			writeToScreen(myObj.mensaje);	
		}
	}else if(myObj.codigo == -2){
		writeToScreen(myObj.mensaje);
	}else{
		writeToScreen("No se pudo interpretar la respuesta");
	}
}