if(typeof SUBETransaccion === 'undefined'){	
	var SUBETransaccion = class {
	  validar(transaccion, titulo) {
		  this.SUBE_transaccion = transaccion;
		  this.tituloImpresion = titulo;
		  
		  if(token == ""){
			   writeToScreen("Error GCAT. Posible falla de conexion o lectura contra el GatewaySUBE.");
			   return false;
		   }
		   if(tipoCliente == null){
			   writeToScreen("No se ha especifado el tipo de cliente.");
			   return false;
		   }
		   if(!(tipoCliente == "P")){
			   writeToScreen("Opcion no disponible para el tipo de usuario.");
			   return false;
		   }
		   if (typeof IdProductoSUBEPlataforma == 'undefined') {
			   writeToScreen("Producto SUBE no habilitado.");
			   return false;
		   }
		   
		   return true;
	   }
	
	   ejecutar() {
			try {	   
				webSocketTransaccionando = true;	 
				if((typeof webSocket === 'undefined') || (webSocket == undefined)){
					webSocket = new WebSocketTransaccion(wsUriSTRWebVersion, this.SUBE_transaccion, this.tituloImpresion);
					webSocket.onopen = function(evt) { SUBE_open_chequearVersion(evt, this.SUBE_transaccion, this.tituloImpresion) };
					webSocket.onclose = function(evt) { SUBE_close_chequearVersion(evt, this.SUBE_transaccion, this.tituloImpresion) };
					webSocket.onerror = function(evt) { SUBE_error_chequearVersion(evt, this.SUBE_transaccion, this.tituloImpresion) };
					webSocket.onmessage = function(evt) { SUBE_message_chequearVersion(evt, this.SUBE_transaccion, this.tituloImpresion) };	
				}else{
				  	writeToScreen("Existe una instancia anterior en proceso.");
				}			
			}catch(err) {
				webSocketTransaccionando = false;
				webSocket = undefined;
				writeToScreen(SERVICIO_NO_DISPONIBLE);
				controlarBotones("", false);
			}
	   }
	}
}