function onKeyPressUsuario(keycode){
	if (keycode == 13){
		document.getElementById('password').focus();
		event.preventDefault();
		return false;
	}
}

function onKeyPressPassword(keycode){
	if (keycode == 13){
		document.getElementById('captcha').focus();
		event.preventDefault();
		return false;
	}
}

function onKeyPressPassword2(keycode){
	if (keycode == 13){
		//var o=document.getElementById('coordenada1');
		var o = window.getComputedStyle(document.getElementById('opTarjetaCoordenadas')).getPropertyValue('display');
		if(o != "none"){
			document.getElementById('coordenada1').focus();
			event.preventDefault();
			return false;
		}else{
			//document.getElementById('login').click();
			//event.preventDefault();
			return false;
		}
	}
}

function onKeyPressCaptcha(keycode){
	if (keycode == 13){
		//var o=document.getElementById('coordenada1');
		var o = window.getComputedStyle(document.getElementById('opTarjetaCoordenadas')).getPropertyValue('display');
		if(o != "none"){
			document.getElementById('coordenada1').focus();
			event.preventDefault();
			return false;
		}else{
			//document.getElementById('login').click();
			return false;
		}
	}
}

function onKeyPressCaptcha2(keycode){
	if (keycode == 13){
		document.getElementById('usuario').focus();
		event.preventDefault();
		return false;
	}
}

function onKeyPressCoordenada1(keycode){
	if (keycode == 13){
		document.getElementById('coordenada2').focus();
		event.preventDefault();
		return false;
	}; 
	
	if (keycode == 8 ) return true; 
	
	return /\\d/.test(String.fromCharCode(keycode));
}

function onKeyPressCoordenada2(keycode){
	if (keycode == 13){
		document.getElementById('login').click();
		return false;
	}; 
	
	if (keycode == 8 ) return true; 
	
	return /\\d/.test(String.fromCharCode(keycode));
}

function onKeyPressPasswordA(keycode){
	if (keycode == 13){
		document.getElementById('password_n').focus();
		event.preventDefault();
		return false;
	}
}
function onKeyPressPasswordN(keycode){
	if (keycode == 13){
		document.getElementById('password_n2').focus();
		event.preventDefault();
		return false;
	}
}
function onKeyPressPasswordN2(keycode){
	if (keycode == 13){
		document.getElementById('btnModificar').click();
		event.preventDefault();
		return false;
	}
}