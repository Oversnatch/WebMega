if(typeof PEITransaccion === 'undefined'){	
	var PEITransaccion = class {
	  validar(transaccion, titulo) {
		  this.PEI_transaccion = transaccion;
		  this.tituloImpresion = titulo;
		  
		  if(token == ""){
			   writeToScreen("Error GCAT. Posible falla de conexion o lectura contra el GatewayPEI.");
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
		   if (typeof IdProductoPEIPlataforma == 'undefined') {
			   writeToScreen("Producto PEI no habilitado.");
			   return false;
		   }
		   
		   return true;
	   }
	
	   ejecutar() {
			try {	   
				webSocketTransaccionando = true;	 
				if((typeof webSocket === 'undefined') || (webSocket == undefined)){
					webSocket = new WebSocketTransaccion(wsUriSTRWebVersion, this.PEI_transaccion, this.tituloImpresion);
					webSocket.onopen = function(evt) { PEI_open_chequearVersion(evt, this.PEI_transaccion, this.tituloImpresion) };
					webSocket.onclose = function(evt) { PEI_close_chequearVersion(evt, this.PEI_transaccion, this.tituloImpresion) };
					webSocket.onerror = function(evt) { PEI_error_chequearVersion(evt, this.PEI_transaccion, this.tituloImpresion) };
					webSocket.onmessage = function(evt) { PEI_message_chequearVersion(evt, this.PEI_transaccion, this.tituloImpresion) };	
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