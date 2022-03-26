/**
 * 
 */

// var wsUri = "ws://localhost:81/chat/";
var wsUri = "ws://localhost:81/sube/";
var wsUriMSG = "ws://localhost:81/MSGSube/";
var wsUriSTRWebVersion = "ws://localhost:81/STRWebVersion/";
var wsUriSTRPortUpdate = "ws://localhost:81/STRWebPortUpdate/";
var output;
var tipoCliente = "P";
var webSocket = undefined;
var webSocketTransaccionando = false;
var webSocketMensajeria = undefined;

var SERVICIO_NO_DISPONIBLE = 'El servicio no esta disponible por favor inicie STRweb.<br />En caso de no tenerlo instalado, para descargarlo haga click <a href="'
		+ strWebURL
		+ "?"
		+ Math.random()
		+ '">aquí</a>. <br />Una vez iniciado haga click en  <br /> <button onclick="init();" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only textoFontSizeCustom"><span class="ui-button-text ui-c">Reiniciar Proceso</span></button>';
var STRWEB_DESACTUALIZADO = 'Por favor actualizar STRweb para poder procesar las transacciones.<br />Recuerde cerrar primero la instancia anterior. Luego para volver a descargarlo haga click <a href="'
		+ strWebURL + "?" + Math.random() + '">aquí</a>. ';

var b_ejecutar_submenu = false;

/*
 * INICIALIZACION DE PANTALLA
 */

function init() {
	webSocketMensajeria = undefined;
	output = document.getElementById("output");

	webSocketArranqueInicial();
}

function cleanScreen() {
	output.innerHTML = '';
}

function writeToConsoleLog(message) {
	if (debug == "true") {
		console.log(message);
	}
}

function controlarBotones(sBoton, bDeshabilitado) {
	if (sBoton != "") {
		boton = document.getElementById(sBoton);
	}
	if ((boton != null) || ("arranqueInicial" == sBoton)) {
		if (boton != null) {
			if (bDeshabilitado) {
				boton.style.fontWeight = "bold";
			} else {
				boton.style.fontWeight = "normal";
			}
			boton.disabled = bDeshabilitado;
		}

	}
	pb = document.getElementById("progressSube");
	if (pb != null) {
		if (bDeshabilitado) {
			pb.style.display = "block";
		} else {
			pb.style.display = "none";
		}
	}
	deshabilitarBoton("sube_oper_carga", bDeshabilitado);
	deshabilitarBoton("sube_oper_anular", bDeshabilitado);
	deshabilitarBoton("sube_oper_reimp", bDeshabilitado);
	deshabilitarBoton("sube_oper_saldo_tarjeta", bDeshabilitado);
	deshabilitarBoton("sube_oper_saldo_lg", bDeshabilitado);
	deshabilitarBoton("sube_oper_debitar_tarjeta", bDeshabilitado);
	deshabilitarBoton("sube_oper_consultar_ultima_carga", bDeshabilitado);
	b_ejecutar_submenu = bDeshabilitado;

	if (PrimeFaces.widgets['blockPanelGral'] && !bDeshabilitado) {
		PF('blockPanelGral').hide();
	}
	// document.getElementById("sidebar-left").disabled = bDeshabilitado;

}

function deshabilitarBoton(sBoton, bDeshabilitado) {
	if (sBoton != "") {
		botonAux = document.getElementById(sBoton);
		if (botonAux != null) {
			botonAux.disabled = bDeshabilitado;
		}
	}
}

function deshabilitarMenuItem(sMenuItem, bDeshabilitado) {
	if (sBoton != "") {
		botonAux = document.getElementById(sBoton);
		if (botonAux != null) {
			botonAux.disabled = bDeshabilitado;
		}
	}
}

function webSocketTurnosAbrir(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("sube_turnos_abrir", true);
		mensaje = "webSocketTurnosAbrir";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocketUri;

		var output;
		var tipoCliente;

		const objSUBETransaccion = new SUBETransaccion();
		if (objSUBETransaccion.validar("SUBE_abrirTurno", "")) {
			objSUBETransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("sube_turnos_abrir", false);
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function webSocketAdministracionActualizarTablas(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("sube_adm_actt", true);
		mensaje = "webSocketAdministracionActualizarTablas";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objSUBETransaccion = new SUBETransaccion();
		if (objSUBETransaccion.validar("SUBE_actualizarTablas", "")) {
			objSUBETransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("sube_adm_actt", false);
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function webSocketAdministracionAltaLG(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		cleanScreen();
		if ((tipoCliente == "M") || (tipoCliente == "D")) {
			writeToScreen("Ingrese el ID de Cliente a dar de alta");
			writeToScreen("<input type='text' id='subeIdCliente' value='0' style='text-align: right; width: 200px;font-size: large;' class='textoVta'  onkeyup='return ValNumero(this, false);' maxlength='9'></input>");
			writeToScreen("<button type='button' onclick='webSocketAdministracionAltaLGOk();return false;' class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only textoFontSizeCustom'><span class='ui-button-text ui-c'>Alta de LG</span></button>");
		} else {
			webSocketAdministracionAltaLGOk();
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function webSocketAdministracionAltaLGOk(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("sube_adm_aalg", true);
		mensaje = "webSocketAdministraiconAltaLG";

		subeIdCliente = 0;
		if ((tipoCliente == "M") || (tipoCliente == "D")) {
			subeIdCliente = document.getElementById("subeIdCliente");
		}

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objSUBETransaccion = new SUBETransaccion();
		if (objSUBETransaccion.validar("SUBE_altaLG", "")) {
			objSUBETransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("sube_adm_aalg", false);
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function webSocketArranqueInicial() {
	mensajeriaConnect();
	controlarBotones("arranqueInicial", true);
	mensaje = "webSocketArranqueInicial";

	cleanScreen();
	writeToConsoleLog(mensaje);

	var webSocket;
	var webSocketUri;

	var output;
	var tipoCliente;

	const objSUBETransaccion = new SUBETransaccion();
	if (objSUBETransaccion.validar("SUBE_arranqueInicial", "")) {
		objSUBETransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("arranqueInicial", false);
	}
}

function webSocketAdministracionInicializar(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("sube_adm_inic", true);
		mensaje = "webSocketAdministracionInicializar";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objSUBETransaccion = new SUBETransaccion();
		if (objSUBETransaccion.validar("SUBE_inicializar", "")) {
			objSUBETransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("sube_adm_inic", false);
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function webSocketAdministracionDesvincularLG(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		cleanScreen();
		writeToScreen("<button type='button' onclick='webSocketAdministracionDesvincularLGOk();return false;' class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only textoFontSizeCustom'><span class='ui-button-text ui-c'>Desvincular LG</span></button>");
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function webSocketAdministracionDesvincularLGOk(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("sube_adm_deslg", true);
		mensaje = "webSocketAdministracionDesvincularLG";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objSUBETransaccion = new SUBETransaccion();
		if (objSUBETransaccion.validar("SUBE_desvincularLG", "")) {
			objSUBETransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("sube_adm_deslg", false);
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function webSocketAdministracionEjecComandoRem(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("sube_adm_ecre", true);
		mensaje = "webSocketAdministracionEjecComandoRem";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objSUBETransaccion = new SUBETransaccion();
		if (objSUBETransaccion.validar("SUBE_ejecutarComandoRemoto", "")) {
			objSUBETransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("sube_adm_ecre", false);
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}

}

function webSocketAdministracionVersionDll(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("sube_adm_vdll", true);
		mensaje = "webSocketAdministracionVersionDll";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objSUBETransaccion = new SUBETransaccion();
		if (objSUBETransaccion.validar("SUBE_obtenerVersion", "")) {
			objSUBETransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("sube_adm_vdll", false);
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function webSocketAdministracionReiniciarSubeLG(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("sube_adm_rslg", true);
		mensaje = "webSocketAdministracionReiniciarSubeLG";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objSUBETransaccion = new SUBETransaccion();
		if (objSUBETransaccion.validar("SUBE_reiniciar", "")) {
			objSUBETransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("sube_adm_rslg", false);
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function webSocketAnularCarga(evt) {
	mensajeriaConnect();
	controlarBotones("sube_oper_anular", true);
	mensaje = "webSocketAnularCarga";

	cleanScreen();
	writeToConsoleLog(mensaje);

	var webSocket;
	var webSocketUri;

	var output;
	var tipoCliente;

	// var r = confirm("Esta seguro de anular la carga?");

	// if (r == true) {
	PF('blockPanelGral').show();
	const objSUBETransaccion = new SUBETransaccion();
	if (objSUBETransaccion.validar("SUBE_anulacionRecarga", "")) {
		objSUBETransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("sube_oper_anular", false);
	}
	/*
	 * } else{ webSocketTransaccionando = false; controlarBotones("", false); }
	 */
}

function webSocketConsultarUltimaCarga(evt) {
	mensajeriaConnect();
	controlarBotones("sube_oper_consultar_ultima_carga", true);
	mensaje = "webSocketConsultarUltimaCarga";

	cleanScreen();
	writeToConsoleLog(mensaje);

	var webSocket;
	var webSocketUri;

	var output;
	var tipoCliente;

	const objSUBETransaccion = new SUBETransaccion();
	if (objSUBETransaccion.validar("SUBE_consultarUltimaCarga", "")) {
		objSUBETransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("sube_oper_consultar_ultima_carga", false);
	}
}

function webSocketCargarTarjeta(evt) {
	cleanScreen();
	writeToScreen("Ingrese el importe a cargar ");
	writeToScreen("<input id='subeImporteCarga' style='text-align: right; width: 200px;font-size: large;' class='textoVta' oninput='validarInput(this, importeSubeSinDecimal, importeCargaTerminadoEnCero)' maxlength='9' type='text' placeholder='" + (importeSubeSinDecimal ? '0' : '0.00') + "' required></input>");
	writeToScreen("<button type='button' onclick='if(validarInputConfirmacion(document.getElementById(\"subeImporteCarga\").value, importeSubeSinDecimal, importeCargaTerminadoEnCero)){PF(\"confirmaCargaDialogWV\").show();}else{document.getElementById(\"subeImporteCarga\").focus();document.getElementById(\"subeImporteCarga\").setSelectionRange(document.getElementById(\"subeImporteCarga\").value.length, document.getElementById(\"subeImporteCarga\").value.length);}'  class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only textoFontSizeCustom'><span class='ui-button-text ui-c'>Realizar Carga</span></button>");
}

function validarInput(objInput, sinDecimal, terminadoEnCero) {
	if (objInput.value != ''){
		let p = objInput.selectionStart;
		console.log(objInput.value);
		if(sinDecimal){
			if (objInput.value.match(/^[0-9]+$/)){
				objInput.value = objInput.value;
				valorPrevio = objInput.value;
			}else{
				objInput.value = valorPrevio;
			}
		}else{
			if (objInput.value.match(/^\d+\.?\d{0,2}$/)){
				objInput.value = objInput.value;
				valorPrevio = objInput.value;
			}else{
				objInput.value = valorPrevio;
			}
		}
		objInput.setSelectionRange(p, p);
	}
	
}

function validarInputConfirmacion(objInput, sinDecimal, terminadoEnCero) {
	if(sinDecimal){
		if(objInput.match(/[.,]+/)){
			return false;
		}else{
			if(terminadoEnCero){
				if(objInput.match(/^\d+0/)){
					return true;
				}else{
					alert("Solo se permiten valores multiplos de 10. Por ejemplo: 10, 20, 30, etc.");
					return false;
				}
			}else{
				return true;
			}
		}
	}else{
		if(!objInput.includes(".")){
			objInput = parseFloat(objInput).toFixed(2);
		}
		if (objInput.match(/^\d+\.\d{0,2}$/)){
			if(terminadoEnCero){
				if(objInput.match(/^\d+0.00/)){
					return true;
				}else{
					alert("Solo se permiten valores multiplos de 10. Por ejemplo: 10.00, 20.00, 30.00, etc.");
					return false;
				}
			}else{
				return true;
			}
		}else{
			alert("Solo se permiten valores con decimales y hasta 2 digitos decimales.");
			return false;
		}
	}
}

function webSocketValidarImporteCarga(evt) {
	mensajeriaConnect();
	controlarBotones("sube_oper_carga", true);
	mensaje = "webSocketCargarTarjeta";

	importeSube = Math
			.floor(document.getElementById("subeImporteCarga").value * 100);

	cleanScreen();
	writeToConsoleLog(mensaje);

	var webSocket;
	var webSocketUri;

	var output;
	var tipoCliente;

	const objSUBETransaccion = new SUBETransaccion();
	if (objSUBETransaccion.validar("SUBE_validarImporteCarga", "")) {
		objSUBETransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("sube_oper_carga", false);
	}
}

function webSocketCargarTarjetaButton(evt) {
	mensajeriaConnect();
	controlarBotones("sube_oper_carga", true);
	mensaje = "webSocketCargarTarjeta";

	cleanScreen();
	writeToConsoleLog(mensaje);

	var webSocket;
	var webSocketUri;

	var output;
	var tipoCliente;

	const objSUBETransaccion = new SUBETransaccion();
	if (objSUBETransaccion.validar("SUBE_cargarTarjeta", "")) {
		objSUBETransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("sube_oper_carga", false);
	}
}

function webSocketSUBECargasDiferidas(evt) {
	mensajeriaConnect();
	controlarBotones("sube_oper_carga_diferida", true);
	mensaje = "webSocketCargasDiferidas";

	cleanScreen();
	writeToConsoleLog(mensaje);

	var webSocket;
	var webSocketUri;

	var output;
	var tipoCliente;

	const objSUBETransaccion = new SUBETransaccion();
	if (objSUBETransaccion.validar("SUBE_cargarTarjetaDiferida", "")) {
		objSUBETransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("sube_oper_carga_diferida", false);
	}
}

function webSocketTurnosCerrar(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("sube_turnos_cerrar", true);
		mensaje = "webSocketTurnosCerrar";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objSUBETransaccion = new SUBETransaccion();
		if (objSUBETransaccion.validar("SUBE_cerrarTurno", "")) {
			objSUBETransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("sube_turnos_cerrar", false);
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function webSocketConsultarSaldoLG(evt) {
	mensajeriaConnect();
	controlarBotones("sube_oper_saldo_lg", true);
	mensaje = "webSocketConsultarSaldoLG";

	cleanScreen();
	writeToConsoleLog(mensaje);

	var webSocket;
	var webSocketUri;

	var output;
	var tipoCliente;

	const objSUBETransaccion = new SUBETransaccion();
	if (objSUBETransaccion.validar("SUBE_consultarSaldoLG", "")) {
		objSUBETransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("sube_oper_saldo_lg", false);
	}
}

function webSocketConsultarSaldoTarjeta(evt) {
	mensajeriaConnect();
	controlarBotones("sube_oper_saldo_tarjeta", true);
	mensaje = "webSocketConsultarSaldoTarjeta";

	cleanScreen();
	writeToConsoleLog(mensaje);

	var webSocket;
	var webSocketUri;

	var output;
	var tipoCliente;

	const objSUBETransaccion = new SUBETransaccion();
	if (objSUBETransaccion.validar("SUBE_consultarSaldoTC", "")) {
		objSUBETransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("sube_oper_saldo_tarjeta", false);
	}
}

function webSocketTurnosInformarLotes(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("sube_turnos_lotes", true);
		mensaje = "webSocketTurnosInformarLotes";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objSUBETransaccion = new SUBETransaccion();
		if (objSUBETransaccion.validar("SUBE_informarLotes", "")) {
			objSUBETransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("sube_turnos_lotes", false);
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function webSocketReimprimirUltimoTicket(evt) {
	cleanScreen();
	writeToScreen("<button type='button' onclick='PF(\"blockPanelGral\").show();webSocketReimprimirUltimoTicketOK(\"ReimpresionUltimoTicket\");return false;'  class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only textoFontSizeCustom'><span class='ui-button-text ui-c'>IMPRIMIR</span></button>");
}

function webSocketReimprimirUltimoTicketOK(titulo) {

	mensajeriaConnect();
	controlarBotones("sube_oper_reimp", true);
	mensaje = "webSocketReimprimirUltimoTicket";

	cleanScreen();
	writeToConsoleLog(mensaje);

	var webSocket;
	var webSocketUri;

	var output;
	var tipoCliente;

	const objSUBETransaccion = new SUBETransaccion();
	if (objSUBETransaccion.validar("SUBE_imprimeTicket", titulo)) {
		objSUBETransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("sube_oper_reimp", false);
	}
}

function webSocketDebitarSaldoTarjeta(evt) {
	cleanScreen();
	writeToScreen("Ingrese el importe a debitar");
	writeToScreen("<input id='subeImporteCarga' style='text-align: right; width: 200px;font-size: large;' class='textoVta' oninput='validarInput(this, importeSubeSinDecimal, importeCargaTerminadoEnCero)' maxlength='9' type='text' placeholder='" + (importeSubeSinDecimal ? '0' : '0.00') + "' required></input>");
	writeToScreen("<button type='button' onclick='if(validarInputConfirmacion(document.getElementById(\"subeImporteCarga\").value, importeSubeSinDecimal, importeCargaTerminadoEnCero)){PF(\"blockPanelGral\").show();webSocketDebitarSaldoTarjetaButton();return false;}else{document.getElementById(\"subeImporteCarga\").focus();document.getElementById(\"subeImporteCarga\").setSelectionRange(document.getElementById(\"subeImporteCarga\").value.length, document.getElementById(\"subeImporteCarga\").value.length);}'  class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only textoFontSizeCustom'><span class='ui-button-text ui-c'>Debitar Saldo</span></button>");
}

function webSocketDebitarSaldoTarjetaButton(evt) {
	mensajeriaConnect();
	controlarBotones("sube_oper_debitar_tarjeta", true);
	mensaje = "webSocketDebitarSaldoTarjeta";

	importeSube = Math
			.floor(document.getElementById("subeImporteCarga").value * 100);

	cleanScreen();
	writeToConsoleLog(mensaje);

	var webSocket;
	var webSocketUri;

	var output;
	var tipoCliente;

	const objSUBETransaccion = new SUBETransaccion();
	if (objSUBETransaccion.validar("SUBE_debitarSaldoTarjeta", "")) {
		objSUBETransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("sube_oper_debitar_tarjeta", false);
	}
}

function webSocketAdministracionIdentificar(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("sube_adm_iden", true);
		mensaje = "webSocketAdministracionIdentificar";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objSUBETransaccion = new SUBETransaccion();
		if (objSUBETransaccion.validar("SUBE_identificar", "")) {
			objSUBETransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("sube_adm_iden", false);
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function writeToScreen(message) {
	var pre = document.createElement("p");
	pre.style.wordWrap = "break-word";
	pre.style.fontSize = "large";
	pre.innerHTML = message;
	this.output.appendChild(pre);
}
function writeToScreenClear(message) {
	cleanScreen();
	var pre = document.createElement("p");
	pre.style.wordWrap = "break-word";
	pre.style.fontSize = "large";
	pre.innerHTML = message;
	this.output.appendChild(pre);
}

function actualizarHeader(myObj) {

	inicializado = document.getElementById("inicializado");
	if (myObj.iLGInicializado == 1) {
		inicializado.innerHTML = INICIALIZADO_OK;
	} else if (myObj.iLGInicializado == 0) {
		inicializado.innerHTML = INICIALIZADO_NO_OK;
	} else {
		inicializado.innerHTML = INICIALIZADO_RAYA;
	}

	turno = document.getElementById("turno");
	if (myObj.cEstadoTurno == "A") {
		turno.innerHTML = TURNO_ABIERTO;
	} else if (myObj.cEstadoTurno == "C") {
		turno.innerHTML = TURNO_CERRADO;
	} else {
		turno.innerHTML = TURNO_RAYA;
	}

	lote = document.getElementById("lote");
	if (myObj.cLotesDisp == 0) {
		lote.innerHTML = LOTE_0;
	} else if (myObj.cLotesDisp == 1) {
		lote.innerHTML = LOTE_1;
	} else if (myObj.cLotesDisp == 2) {
		lote.innerHTML = LOTE_2;
	} else if (myObj.cLotesDisp == 3) {
		lote.innerHTML = LOTE_3;
	} else {
		lote.innerHTML = LOTE_RAYA;
	}

	if (myObj.iHabilitaCargaDecimal == 0) {
		importeSubeSinDecimal = true;
	} else {
		importeSubeSinDecimal = false;
	}

	if (myObj.importeCargaTerminadoEnCero == 1) {
		importeCargaTerminadoEnCero = true;
	} else {
		importeCargaTerminadoEnCero = false;
	}
}

(function() {
	// your page initialization code here
	// the DOM will be available here
	window.addEventListener('load', init, false);
})();

/*
 * Funciones SUBE
 */

/*
 * chequearVersion
 */
function SUBE_open_chequearVersion(evt, SUBE_transaccion, tituloImpresion) {
	webSocketTransaccionando = true;
	// writeToScreen("CONECTADO");
	webSocket.send('');
}

function SUBE_close_chequearVersion(evt, SUBE_transaccion, tituloImpresion) {
	if (webSocketTransaccionando) {
		if (SUBE_transaccion == "SUBE_arranqueInicial") {
			controlarBotones("arranqueInicial", true);
		} else {
			controlarBotones("", true);
		}
	}
	// writeToScreen("DESCONECTADO");
}

function SUBE_error_chequearVersion(evt, SUBE_transaccion, tituloImpresion) {
	webSocketTransaccionando = false;
	if (typeof evt.data === 'undefined') {
		writeToScreen(SERVICIO_NO_DISPONIBLE);
		webSocket.close();
		webSocket = undefined;
	} else {
		writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
	}
	if (SUBE_transaccion == "SUBE_arranqueInicial") {
		controlarBotones("arranqueInicial", false);
	} else {
		controlarBotones("", false);
	}
}

function SUBE_message_chequearVersion(evt, SUBE_transaccion, tituloImpresion) {
	writeToConsoleLog(evt.data);
	var myObj = JSON.parse(evt.data);
	if (myObj.codigo == -2) {
		writeToScreen(myObj.mensaje);
		webSocketTransaccionando = false;
		webSocket.close();
		webSocket = undefined;
		if (SUBE_transaccion == "SUBE_arranqueInicial") {
			controlarBotones("arranqueInicial", false);
		} else {
			controlarBotones("", false);
		}
	} else if (myObj.codigo == -1) {
		writeToScreen(myObj.mensaje);
		webSocketTransaccionando = false;
		webSocket.close();
		webSocket = undefined;
		if (SUBE_transaccion == "SUBE_arranqueInicial") {
			controlarBotones("arranqueInicial", false);
		} else {
			controlarBotones("", false);
		}
	} else if (myObj.codigo == 0) {
		try {
			webSocket.close();
			webSocket = undefined;
			if (myObj.mensaje >= strWeb_ult_version_de_jnlp) {
				if ((typeof webSocket === 'undefined')
						|| (webSocket == undefined)) {
					webSocket = new WebSocketTransaccion(wsUri,
							SUBE_transaccion, tituloImpresion);
					webSocket.onopen = function(evt) {
						SUBE_open_transaccion(evt, SUBE_transaccion,
								tituloImpresion)
					};
					webSocket.onclose = function(evt) {
						SUBE_close_transaccion(evt, SUBE_transaccion,
								tituloImpresion)
					};
					webSocket.onerror = function(evt) {
						SUBE_error_transaccion(evt, SUBE_transaccion,
								tituloImpresion)
					};
					webSocket.onmessage = function(evt) {
						SUBE_message_transaccion(evt, SUBE_transaccion,
								tituloImpresion)
					};
				} else {
					writeToScreen("Existe una instancia anterior en proceso.");
				}
			} else {
				writeToScreen(STRWEB_DESACTUALIZADO);
				webSocketTransaccionando = false;
				if (SUBE_transaccion == "SUBE_arranqueInicial") {
					controlarBotones("arranqueInicial", false);
				} else {
					controlarBotones("", false);
				}
			}
		} catch (err) {
			writeToScreen(SERVICIO_NO_DISPONIBLE);
			webSocketTransaccionando = false;
			webSocket.close();
			webSocket = undefined;
			if (SUBE_transaccion == "SUBE_arranqueInicial") {
				controlarBotones("arranqueInicial", false);
			} else {
				controlarBotones("", false);
			}
		}
	}
}

/*
 * portUpdate
 */
function SUBE_open_portUpdate(evt, SUBE_transaccion, tituloImpresion) {
	webSocketTransaccionando = true;
	// writeToScreen("CONECTADO");
	webSocket.send('');
}

function SUBE_close_portUpdate(evt, SUBE_transaccion, tituloImpresion) {
	if (webSocketTransaccionando) {
		if (SUBE_transaccion == "SUBE_arranqueInicial") {
			controlarBotones("arranqueInicial", true);
		} else {
			controlarBotones("", true);
		}
	}
	// writeToScreen("DESCONECTADO");
}

function SUBE_error_portUpdate(evt, SUBE_transaccion, tituloImpresion) {
	webSocketTransaccionando = false;
	if (typeof evt.data === 'undefined') {
		writeToScreen(SERVICIO_NO_DISPONIBLE);
		webSocket.close();
		webSocket = undefined;
	} else {
		writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
	}
	if (SUBE_transaccion == "SUBE_arranqueInicial") {
		controlarBotones("arranqueInicial", false);
	} else {
		controlarBotones("", false);
	}
}

function SUBE_message_portUpdate(evt, SUBE_transaccion, tituloImpresion) {
	writeToConsoleLog(evt.data);
	var myObj = JSON.parse(evt.data);
	if (myObj.codigo == -2) {
		writeToScreen(myObj.mensaje);
		webSocketTransaccionando = false;
		webSocket.close();
		webSocket = undefined;
		if (SUBE_transaccion == "SUBE_arranqueInicial") {
			controlarBotones("arranqueInicial", false);
		} else {
			controlarBotones("", false);
		}
	} else if (myObj.codigo == -1) {
		writeToScreen(myObj.mensaje);
		webSocketTransaccionando = false;
		webSocket.close();
		webSocket = undefined;
		if (SUBE_transaccion == "SUBE_arranqueInicial") {
			controlarBotones("arranqueInicial", false);
		} else {
			controlarBotones("", false);
		}
	} else if (myObj.codigo == 0) {
		try {
			webSocket.close();
			webSocket = undefined;

			if ((typeof webSocket === 'undefined') || (webSocket == undefined)) {
				// if(webSocket == undefined){

				/* TODO: regenerar token */

				webSocket = new WebSocketTransaccion(wsUri, SUBE_transaccion,
						tituloImpresion);
				webSocket.onopen = function(evt) {
					SUBE_open_transaccion(evt, SUBE_transaccion,
							tituloImpresion)
				};
				webSocket.onclose = function(evt) {
					SUBE_close_transaccion(evt, SUBE_transaccion,
							tituloImpresion)
				};
				webSocket.onerror = function(evt) {
					SUBE_error_transaccion(evt, SUBE_transaccion,
							tituloImpresion)
				};
				webSocket.onmessage = function(evt) {
					SUBE_message_transaccion(evt, SUBE_transaccion,
							tituloImpresion)
				};
			} else {
				writeToScreen("Existe una instancia anterior en proceso.");
			}

		} catch (err) {
			writeToScreen(SERVICIO_NO_DISPONIBLE);
			webSocketTransaccionando = false;
			webSocket.close();
			webSocket = undefined;
			if (SUBE_transaccion == "SUBE_arranqueInicial") {
				controlarBotones("arranqueInicial", false);
			} else {
				controlarBotones("", false);
			}
		}
	}
}

/*
 * SUBE_transaccion
 */

function SUBE_open_transaccion(evt, SUBE_transaccion, tituloImpresion) {
	// writeToScreen("CONECTADO");
	if (SUBE_transaccion == "SUBE_altaLG") {
		if ((tipoCliente == "M") || (tipoCliente == "D")) {
			webSocket.send('{"funcion": "' + SUBE_transaccion
					+ '" , "token": "' + token + '", "idClienteParaAltaLG": "'
					+ subeIdCliente + '", "tituloImpresion": "'
					+ tituloImpresion + '", "forzarInicializarSube": "'
					+ forzarInicializarSube + '"}');
		} else {
			webSocket.send('{"funcion": "' + SUBE_transaccion
					+ '" , "token": "' + token + '", "tituloImpresion": "'
					+ tituloImpresion + '", "forzarInicializarSube": "'
					+ forzarInicializarSube + '"}');
		}
	} else {
		webSocket.send('{"funcion": "' + SUBE_transaccion + '" , "token": "'
				+ token + '", "importe": "' + importeSube
				+ '", "forzarInicializarSube": "' + forzarInicializarSube
				+ '"}');
	}
	// webSocket.send('{"funcion": "SUBE_test2"}');
}

function SUBE_close_transaccion(evt, SUBE_transaccion, tituloImpresion) {
	if (webSocketTransaccionando) {
		if (SUBE_transaccion == "SUBE_arranqueInicial") {
			controlarBotones("arranqueInicial", true);
		} else {
			controlarBotones("", true);
		}
	}
}

function SUBE_error_transaccion(evt, SUBE_transaccion, tituloImpresion) {
	webSocketTransaccionando = false;
	if (typeof evt.data === 'undefined') {
		writeToScreen(SERVICIO_NO_DISPONIBLE);
		webSocket.close();
		webSocket = undefined;
	} else {
		writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
	}
	if (SUBE_transaccion == "SUBE_arranqueInicial") {
		controlarBotones("arranqueInicial", false);
	} else {
		controlarBotones("", false);
	}
}

function SUBE_message_transaccion(evt, SUBE_transaccion, tituloImpresion) {
	writeToConsoleLog(evt.data);
	var myObj = JSON.parse(evt.data);
	if (myObj.codigo == -3) {
		try {
			webSocket.close();
			webSocket = undefined;
			if ((typeof webSocket === 'undefined') || (webSocket == undefined)) {
				webSocket = new WebSocketTransaccion(wsUriSTRPortUpdate,
						SUBE_transaccion, tituloImpresion);
				webSocket.onopen = function(evt) {
					SUBE_open_portUpdate(evt, SUBE_transaccion, tituloImpresion)
				};
				webSocket.onclose = function(evt) {
					SUBE_close_portUpdate(evt, SUBE_transaccion,
							tituloImpresion)
				};
				webSocket.onerror = function(evt) {
					SUBE_error_portUpdate(evt, SUBE_transaccion,
							tituloImpresion)
				};
				webSocket.onmessage = function(evt) {
					SUBE_message_portUpdate(evt, SUBE_transaccion,
							tituloImpresion)
				};
			} else {
				writeToScreen("Existe una instancia anterior en proceso.");
			}
		} catch (err) {
			webSocketTransaccionando = false;
			webSocket = undefined;
			writeToScreen(SERVICIO_NO_DISPONIBLE);
			controlarBotones("", false);
		}
	} else if (myObj.codigo == -2) {
		writeToScreen(myObj.mensaje);
		webSocketTransaccionando = false;
		webSocket.close();
		webSocket = undefined;
		if (SUBE_transaccion == "SUBE_validarImporteCarga") {
			controlarBotones("arranqueInicial", false);
		} else if (SUBE_transaccion == "SUBE_arranqueInicial") {
			controlarBotones("arranqueInicial", false);
		} else {
			controlarBotones("", false);
		}
		actualizarHeader(myObj);
	} else if (myObj.codigo == -1) {
		// writeToScreen(myObj.mensaje);
		webSocketTransaccionando = false;
		webSocket.close();
		webSocket = undefined;

		if (SUBE_transaccion == "SUBE_arranqueInicial") {
			controlarBotones("arranqueInicial", false);
		} else if (SUBE_transaccion == "SUBE_reiniciar") {
			writeToScreenClear("Proceso de reiniciado no terminado correctamente.");
			myObj.iLGInicializado = -1;
			myObj.cEstadoTurno = "";
			myObj.cLotesDisp = -1;
			controlarBotones("", false);
		} else {
			controlarBotones("", false);
		}
		actualizarHeader(myObj);
	} else if (myObj.codigo == 0) {

		controlarBotones("", false);

		if (SUBE_transaccion == "SUBE_consultarSaldoTC") {
			writeToScreen("<button type='button' onclick='webSocketReimprimirUltimoTicketOK(\"ConsultaSaldoTC\");return false;' class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only textoFontSizeCustom'><span class='ui-button-text ui-c'>IMPRIMIR</span></button>");
		} else if (SUBE_transaccion == "SUBE_consultarSaldoLG") {
			writeToScreen("<button type='button' onclick='webSocketReimprimirUltimoTicketOK(\"ConsultaSaldoLG\");return false;' class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only textoFontSizeCustom'><span class='ui-button-text ui-c'>IMPRIMIR</span></button>");
		}
		webSocketTransaccionando = false;
		webSocket.close();
		webSocket = undefined;

		if (SUBE_transaccion == "SUBE_validarImporteCarga") {
			webSocketCargarTarjetaButton();
		} else if (SUBE_transaccion == "SUBE_arranqueInicial") {
			inicializadoOK(false);
			forzarInicializarSube = false;
			controlarBotones("arranqueInicial", false);
		} else if (SUBE_transaccion == "SUBE_reiniciar") {
			writeToScreenClear("Proceso de reiniciado correcto.");
			myObj.iLGInicializado = -1;
			myObj.cEstadoTurno = "";
			myObj.cLotesDisp = -1;
			controlarBotones("", false);
		} else if (SUBE_transaccion == "SUBE_obtenerVersion") {
			writeToScreenClear(myObj.mensaje.split('|').join('<br />'));
			controlarBotones("", false);
		} else {
			controlarBotones("", false);
		}
		actualizarHeader(myObj);
	} else if (myObj.codigo >= 1) {
		writeToConsoleLog(myObj.mensaje);
		webSocketTransaccionando = false;
		webSocket.close();
		webSocket = undefined;
		if (SUBE_transaccion == "SUBE_validarImporteCarga") {

			/*
			 * var r = confirm("IMPORTE A CARGAR $" + Number(importeSube /
			 * 100).toFixed(2) + " CONFIRMA OPERACION?");
			 * 
			 * if (r == true) { webSocketCargarTarjetaButton(); }else{
			 * controlarBotones("", false); }
			 */

			$("#importeExcedidoSUBE").text(
					"IMPORTE A CARGAR $" + Number(importeSube / 100).toFixed(2)
							+ " CONFIRMA OPERACION?");

			PF('confirmaCargaExcedidaDialogWV').show();
		} else if (SUBE_transaccion == "SUBE_arranqueInicial") {
			controlarBotones("arranqueInicial", false);
		} else {
			controlarBotones("", false);
		}
		actualizarHeader(myObj);
	} else {
		writeToScreen("No se pudo interpretar la respuesta");
		webSocketTransaccionando = false;
		webSocket.close();
		webSocket = undefined;
		if (SUBE_transaccion == "SUBE_arranqueInicial") {
			controlarBotones("arranqueInicial", false);
		} else {
			controlarBotones("", false);
		}
		actualizarHeader(myObj);
	}
}