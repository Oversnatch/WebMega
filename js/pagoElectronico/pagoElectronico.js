/**
 * 
 */
	function keyupNroTarjeta(componente) {
		var tarjeta = componente.value.replaceAll(' ', '');
		
		if(tarjeta.length == 0 ){
			blanquearDatosDeTarjeta();
			return true;
		}
		/*else if(tarjeta.length == 8){
			validarBIN();
			return true;
	    }*/
		else
			return false;
	};

	function verificarLongitudBIN(bin){
		if(bin.length > 8){
			validarBIN();
			return true;
	    }else{
	    	return false;
	    }
	}
	
	function evaluarIngresoNroTarjeta(event, componente){
		//alert(componente.value);
		if(componente.value.length >= 25){
			abrirLectorDeTarjeta();
			componente.value = '';
			return false;
		}else if(event.keyCode == 13 && componente.value.length == 0){
			abrirLectorDeTarjeta();
			return false;
		}else if(event.which < 48 || event.which > 57) {
			return false;
		}
	}
	