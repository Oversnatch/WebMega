/**
 * 
 */
if(typeof WebSocketTransaccion === 'undefined'){	
	var WebSocketTransaccion = class extends WebSocket {
	   constructor(url, transaccion, tituloImpresion) {
	     super(url);
	     this.PEI_transaccion = transaccion;
	     this.tituloImpresion = tituloImpresion;
	   }
	}
}