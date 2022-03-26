function setNext(keyCode, id) {
	if(keyCode == 13){
		//Compatibilidad para los tab id. Los mismos estan conformados con idTab:idComponente
		if(id.indexOf(":") > 0){
			id = id.replace(":", "\\:");
		}
	
		setTimeout(function() {
			$('#' + id).focus();
		}, 1);
	}
}

function setFocusAndSelectInput(id) {
	//Compatibilidad para los tab id. Los mismos estan conformados con idTab:idComponente
	if(id.indexOf(":") > 0){
		id = id.replace(":", "\\:");
	}

	setTimeout(function() {
		$('#' + id + '_input').focus();
	}, 1);
}

function setFocusAndSelect(id) {
	//Compatibilidad para los tab id. Los mismos estan conformados con idTab:idComponente
	if(id.indexOf(":") > 0){
		id = id.replace(":", "\\:");
	}

	setTimeout(function() {
		$('#' + id).focus();
	}, 1);
}

function Solo_Numerico(variable, permCeroAizq){
    
    //Reemplazo los puntos y las comas por vacio
	variable = variable.replace(".","");
	variable = variable.replace(",","");
    
    if ((permCeroAizq == 'false') && (variable.length > 0) && (variable.substr(0,1) == '0')){
    	//Numer=parseInt(variable);
	    Numer = variable.substr(1, variable.length);
    }else{
    	Numer=variable;
    }
    if (isNaN(Numer)){
    	if (Numer.length > 0){
        	Numer = Numer.substr(0,(Numer.length -1));
        }else{
        	Numer = "";
        }
    }
    return Numer;
}

function Numerico_Decimal(variable){
	Res = "";
	if (isNaN(variable)){
		Numer=parseFloat(variable) || 0;
		if (Numer == "0"){
			Res = "";
		}else{
			Res = Numer;
		}
	}else{
		Res = variable;
	}    
    return Res;
}

function ValNumero(Control, permCeroAizq){
    Control.value=Solo_Numerico(Control.value, permCeroAizq);
}

function ValNumeroDecimal(Control){
    Control.value=Numerico_Decimal(Control.value);
}