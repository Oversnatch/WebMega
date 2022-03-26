/**
 * 
 */
if(typeof WebSocketTransaccion === 'undefined'){	
	var WebSocketTransaccion = class extends WebSocket {
	   constructor(url, transaccion, tituloImpresion) {
	     super(url);
	     this.SUBE_transaccion = transaccion;
	     this.tituloImpresion = tituloImpresion;
	   }
	}
}