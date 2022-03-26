/**
 * 
 */

var wsUri = "ws://localhost:81/pei/";
var wsUriMSG = "ws://localhost:81/MSGPei/";
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
		+ '">aquí</a>. <br />Una vez iniciado haga click en  <br /> <button onclick="init();" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"><span class="ui-button-text ui-c">Reiniciar Proceso</span></button>';
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
	pb = document.getElementById("progressPei");
	if (pb != null) {
		if (bDeshabilitado) {
			pb.style.display = "block";
		} else {
			pb.style.display = "none";
		}
	}
	deshabilitarBoton("pei_oper_carga", bDeshabilitado);
	deshabilitarBoton("pei_oper_anular", bDeshabilitado);
	deshabilitarBoton("pei_oper_reimp", bDeshabilitado);
	deshabilitarBoton("pei_oper_saldo_tarjeta", bDeshabilitado);
	deshabilitarBoton("pei_oper_saldo_lg", bDeshabilitado);
	deshabilitarBoton("pei_oper_debitar_tarjeta", bDeshabilitado);
	deshabilitarBoton("pei_oper_consultar_ultima_carga", bDeshabilitado);
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

function webSocketPEIAbrirPinpad(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("pei_abrirPinpad", true);
		mensaje = "webSocketTurnosAbrir";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocketUri;

		var output;
		var tipoCliente;

		const objPEITransaccion = new PEITransaccion();
		if (objPEITransaccion.validar("PEI_abrirPinpad", "")) {
			objPEITransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("pei_abrirPinpad", false);
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function webSocketAdministracionActualizarTablas(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("pei_adm_actt", true);
		mensaje = "webSocketAdministracionActualizarTablas";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objPEITransaccion = new PEITransaccion();
		if (objPEITransaccion.validar("PEI_actualizarTablas", "")) {
			objPEITransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("pei_adm_actt", false);
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
			writeToScreen("<input type='text' id='peiIdCliente' value='0' style='text-align: right; width: 200px;font-size: large;' class='textoVta'  onkeyup='return ValNumero(this, false);' maxlength='9'></input>");
			writeToScreen("<button type='button' onclick='webSocketAdministracionAltaLGOk();return false;' class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only'><span class='ui-button-text ui-c'>Alta de LG</span></button>");
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
		controlarBotones("pei_adm_aalg", true);
		mensaje = "webSocketAdministraiconAltaLG";

		peiIdCliente = 0;
		if ((tipoCliente == "M") || (tipoCliente == "D")) {
			peiIdCliente = document.getElementById("peiIdCliente");
		}

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objPEITransaccion = new PEITransaccion();
		if (objPEITransaccion.validar("PEI_altaLG", "")) {
			objPEITransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("pei_adm_aalg", false);
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

	const objPEITransaccion = new PEITransaccion();
	if (objPEITransaccion.validar("PEI_arranqueInicial", "")) {
		objPEITransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("arranqueInicial", false);
	}
}

function webSocketAdministracionInicializar(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("pei_adm_inic", true);
		mensaje = "webSocketAdministracionInicializar";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objPEITransaccion = new PEITransaccion();
		if (objPEITransaccion.validar("PEI_inicializar", "")) {
			objPEITransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("pei_adm_inic", false);
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function webSocketAdministracionDesvincularLG(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		cleanScreen();
		writeToScreen("<button type='button' onclick='webSocketAdministracionDesvincularLGOk();return false;' class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only'><span class='ui-button-text ui-c'>Desvincular LG</span></button>");
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function webSocketAdministracionDesvincularLGOk(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("pei_adm_deslg", true);
		mensaje = "webSocketAdministracionDesvincularLG";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objPEITransaccion = new PEITransaccion();
		if (objPEITransaccion.validar("PEI_desvincularLG", "")) {
			objPEITransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("pei_adm_deslg", false);
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function webSocketAdministracionEjecComandoRem(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("pei_adm_ecre", true);
		mensaje = "webSocketAdministracionEjecComandoRem";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objPEITransaccion = new PEITransaccion();
		if (objPEITransaccion.validar("PEI_ejecutarComandoRemoto", "")) {
			objPEITransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("pei_adm_ecre", false);
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}

}

function webSocketAdministracionVersionDll(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("pei_adm_vdll", true);
		mensaje = "webSocketAdministracionVersionDll";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objPEITransaccion = new PEITransaccion();
		if (objPEITransaccion.validar("PEI_obtenerVersion", "")) {
			objPEITransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("pei_adm_vdll", false);
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function webSocketAdministracionReiniciarPeiLG(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("pei_adm_rslg", true);
		mensaje = "webSocketAdministracionReiniciarPeiLG";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objPEITransaccion = new PEITransaccion();
		if (objPEITransaccion.validar("PEI_reiniciar", "")) {
			objPEITransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("pei_adm_rslg", false);
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function webSocketAnularCarga(evt) {
	mensajeriaConnect();
	controlarBotones("pei_oper_anular", true);
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
	const objPEITransaccion = new PEITransaccion();
	if (objPEITransaccion.validar("PEI_anulacionRecarga", "")) {
		objPEITransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("pei_oper_anular", false);
	}
	/*
	 * } else{ webSocketTransaccionando = false; controlarBotones("", false); }
	 */
}

function webSocketPEILeerPinEncriptado(evt) {
	mensajeriaConnect();
	controlarBotones("pei_leerPinEncriptado", true);
	mensaje = "webSocketPEILeerPinEncriptado";

	cleanScreen();
	writeToConsoleLog(mensaje);

	var webSocket;
	var webSocketUri;

	var output;
	var tipoCliente;

	const objPEITransaccion = new PEITransaccion();
	if (objPEITransaccion.validar("PEI_leerPinEncriptado", "")) {
		objPEITransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("pei_leerPinEncriptado", false);
	}
}

function webSocketPEICancelaPin(evt) {
	mensajeriaConnect();
	controlarBotones("pei_cancelaPin", true);
	mensaje = "webSocketPEICancelaPin";

	cleanScreen();
	writeToConsoleLog(mensaje);

	var webSocket;
	var webSocketUri;

	var output;
	var tipoCliente;

	const objPEITransaccion = new PEITransaccion();
	if (objPEITransaccion.validar("PEI_cancelaPin", "")) {
		objPEITransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("PEI_cancelaPin", false);
	}
}


function webSocketPEICa88888ncelaPin(evt) {
	cleanScreen();
	if (importePeiSinDecimal) {
		writeToScreen("Ingrese el importe a cargar ");
		writeToScreen("<input type='text' id='peiImporteCarga' value='0' style='text-align: right; width: 200px;font-size: large;' class='textoVta'  onkeyup='return ValNumero(this, false);' maxlength='9'></input>");
	} else {
		writeToScreen("Ingrese el importe a cargar ");
		writeToScreen("<input type='text' id='peiImporteCarga' value='0.00' style='text-align: right; width: 200px;font-size: large;' class='textoVta' onkeyup='return ValNumeroDecimal(this);' maxlength='9'></input>");
	}
	/*
	 * writeToScreen("<button type='button' onclick='return
	 * confirmarAccionCargarTarjeta();' >Realizar Carga</button>");
	 */
	writeToScreen("<button type='button' onclick='PF(\"confirmaCargaDialogWV\").show();'  class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only'><span class='ui-button-text ui-c'>Realizar Carga</span></button>");
}

/*
 * function confirmarAccionCargarTarjeta(){ var r = confirm("Esta seguro de
 * realizar la carga?");
 * 
 * if (r == true) { PF('blockPanelGral').show(); webSocketValidarImporteCarga(); }
 * else { } return false; }
 */

function webSocketValidarImporteCarga(evt) {
	mensajeriaConnect();
	controlarBotones("pei_oper_carga", true);
	mensaje = "webSocketCargarTarjeta";

	importePei = Math
			.floor(document.getElementById("peiImporteCarga").value * 100);

	cleanScreen();
	writeToConsoleLog(mensaje);

	var webSocket;
	var webSocketUri;

	var output;
	var tipoCliente;

	const objPEITransaccion = new PEITransaccion();
	if (objPEITransaccion.validar("PEI_validarImporteCarga", "")) {
		objPEITransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("pei_oper_carga", false);
	}
}

function webSocketCargarTarjetaButton(evt) {
	mensajeriaConnect();
	controlarBotones("pei_oper_carga", true);
	mensaje = "webSocketCargarTarjeta";

	cleanScreen();
	writeToConsoleLog(mensaje);

	var webSocket;
	var webSocketUri;

	var output;
	var tipoCliente;

	const objPEITransaccion = new PEITransaccion();
	if (objPEITransaccion.validar("PEI_cargarTarjeta", "")) {
		objPEITransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("pei_oper_carga", false);
	}
}

function webSocketPEICargasDiferidas(evt) {
	mensajeriaConnect();
	controlarBotones("pei_oper_carga_diferida", true);
	mensaje = "webSocketCargasDiferidas";

	cleanScreen();
	writeToConsoleLog(mensaje);

	var webSocket;
	var webSocketUri;

	var output;
	var tipoCliente;

	const objPEITransaccion = new PEITransaccion();
	if (objPEITransaccion.validar("PEI_cargarTarjetaDiferida", "")) {
		objPEITransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("pei_oper_carga_diferida", false);
	}
}

function webSocketPEICerrarPinpad(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("pei_cerrarPinpad", true);
		mensaje = "webSocketCerrarPinpad";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objPEITransaccion = new PEITransaccion();
		if (objPEITransaccion.validar("PEI_cerrarPinpad", "")) {
			objPEITransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("pei_cerrarPinpad", false);
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function webSocketLeerTarjetaEncriptada(evt) {
	mensajeriaConnect();
	controlarBotones("pei_leerTarjetaEncriptada", true);
	mensaje = "webSocketConsultarSaldoLG";

	cleanScreen();
	writeToConsoleLog(mensaje);

	var webSocket;
	var webSocketUri;

	var output;
	var tipoCliente;

	const objPEITransaccion = new PEITransaccion();
	if (objPEITransaccion.validar("PEI_consultarSaldoLG", "")) {
		objPEITransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("pei_oper_saldo_lg", false);
	}
}

function webSocketConsultarSaldoTarjeta(evt) {
	mensajeriaConnect();
	controlarBotones("pei_oper_saldo_tarjeta", true);
	mensaje = "webSocketConsultarSaldoTarjeta";

	cleanScreen();
	writeToConsoleLog(mensaje);

	var webSocket;
	var webSocketUri;

	var output;
	var tipoCliente;

	const objPEITransaccion = new PEITransaccion();
	if (objPEITransaccion.validar("PEI_consultarSaldoTC", "")) {
		objPEITransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("pei_oper_saldo_tarjeta", false);
	}
}

function webSocketTurnosInformarLotes(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("pei_turnos_lotes", true);
		mensaje = "webSocketTurnosInformarLotes";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objPEITransaccion = new PEITransaccion();
		if (objPEITransaccion.validar("PEI_informarLotes", "")) {
			objPEITransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("pei_turnos_lotes", false);
		}
	} else {
		writeToScreen("Transaccion en proceso. Reintente luego de que finalice dicha transaccion.");
	}
}

function webSocketReimprimirUltimoTicket(evt) {
	cleanScreen();
	writeToScreen("<button type='button' onclick='PF('blockPanelGral').show();webSocketReimprimirUltimoTicketOK(\"ReimpresionUltimoTicket\");return false;'  class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only'><span class='ui-button-text ui-c'>IMPRIMIR</span></button>");
}

function webSocketReimprimirUltimoTicketOK(titulo) {

	mensajeriaConnect();
	controlarBotones("pei_oper_reimp", true);
	mensaje = "webSocketReimprimirUltimoTicket";

	cleanScreen();
	writeToConsoleLog(mensaje);

	var webSocket;
	var webSocketUri;

	var output;
	var tipoCliente;

	const objPEITransaccion = new PEITransaccion();
	if (objPEITransaccion.validar("PEI_imprimeTicket", titulo)) {
		objPEITransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("pei_oper_reimp", false);
	}
}

function webSocketDebitarSaldoTarjeta(evt) {
	cleanScreen();
	if (importePeiSinDecimal) {
		writeToScreen("Ingrese el importe a debitar");
		writeToScreen("<input type='text' id='peiImporteCarga' value='0'  style='text-align: right; width: 200px;font-size: large;' class='textoVta' onkeyup='return ValNumero(this, false);'  maxlength='9'></input>");
	} else {
		writeToScreen("Ingrese el importe a debitar");
		writeToScreen("<input type='text' id='peiImporteCarga' value='0.00'  style='text-align: right; width: 200px;font-size: large;' class='textoVta' onkeyup='return ValNumeroDecimal(this);'  maxlength='9'></input>");
	}

	writeToScreen("<button type='button' onclick='PF('blockPanelGral').show();webSocketDebitarSaldoTarjetaButton();return false;' class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only'><span class='ui-button-text ui-c'>Debitar Saldo</span></button>");
}

function webSocketDebitarSaldoTarjetaButton(evt) {
	mensajeriaConnect();
	controlarBotones("pei_oper_debitar_tarjeta", true);
	mensaje = "webSocketDebitarSaldoTarjeta";

	importePei = Math
			.floor(document.getElementById("peiImporteCarga").value * 100);

	cleanScreen();
	writeToConsoleLog(mensaje);

	var webSocket;
	var webSocketUri;

	var output;
	var tipoCliente;

	const objPEITransaccion = new PEITransaccion();
	if (objPEITransaccion.validar("PEI_debitarSaldoTarjeta", "")) {
		objPEITransaccion.ejecutar();
	} else {
		webSocketTransaccionando = false;
		controlarBotones("pei_oper_debitar_tarjeta", false);
	}
}

function webSocketAdministracionIdentificar(evt) {
	if (!b_ejecutar_submenu) {
		mensajeriaConnect();
		controlarBotones("pei_adm_iden", true);
		mensaje = "webSocketAdministracionIdentificar";

		cleanScreen();
		writeToConsoleLog(mensaje);

		var webSocket;
		var webSocketUri;

		var output;
		var tipoCliente;

		const objPEITransaccion = new PEITransaccion();
		if (objPEITransaccion.validar("PEI_identificar", "")) {
			objPEITransaccion.ejecutar();
		} else {
			webSocketTransaccionando = false;
			controlarBotones("pei_adm_iden", false);
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
		importePeiSinDecimal = true;
	} else {
		importePeiSinDecimal = false;
	}
}

(function() {
	// your page initialization code here
	// the DOM will be available here
	window.addEventListener('load', init, false);
})();

/*
 * Funciones PEI
 */

/*
 * chequearVersion
 */
function PEI_open_chequearVersion(evt, PEI_transaccion, tituloImpresion) {
	webSocketTransaccionando = true;
	// writeToScreen("CONECTADO");
	webSocket.send('');
}

function PEI_close_chequearVersion(evt, PEI_transaccion, tituloImpresion) {
	if (webSocketTransaccionando) {
		if (PEI_transaccion == "PEI_arranqueInicial") {
			controlarBotones("arranqueInicial", true);
		} else {
			controlarBotones("", true);
		}
	}
	// writeToScreen("DESCONECTADO");
}

function PEI_error_chequearVersion(evt, PEI_transaccion, tituloImpresion) {
	webSocketTransaccionando = false;
	if (typeof evt.data === 'undefined') {
		writeToScreen(SERVICIO_NO_DISPONIBLE);
		webSocket.close();
		webSocket = undefined;
	} else {
		writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
	}
	if (PEI_transaccion == "PEI_arranqueInicial") {
		controlarBotones("arranqueInicial", false);
	} else {
		controlarBotones("", false);
	}
}

function PEI_message_chequearVersion(evt, PEI_transaccion, tituloImpresion) {
	writeToConsoleLog(evt.data);
	var myObj = JSON.parse(evt.data);
	if (myObj.codigo == -2) {
		writeToScreen(myObj.mensaje);
		webSocketTransaccionando = false;
		webSocket.close();
		webSocket = undefined;
		if (PEI_transaccion == "PEI_arranqueInicial") {
			controlarBotones("arranqueInicial", false);
		} else {
			controlarBotones("", false);
		}
	} else if (myObj.codigo == -1) {
		writeToScreen(myObj.mensaje);
		webSocketTransaccionando = false;
		webSocket.close();
		webSocket = undefined;
		if (PEI_transaccion == "PEI_arranqueInicial") {
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
							PEI_transaccion, tituloImpresion);
					webSocket.onopen = function(evt) {
						PEI_open_transaccion(evt, PEI_transaccion,
								tituloImpresion)
					};
					webSocket.onclose = function(evt) {
						PEI_close_transaccion(evt, PEI_transaccion,
								tituloImpresion)
					};
					webSocket.onerror = function(evt) {
						PEI_error_transaccion(evt, PEI_transaccion,
								tituloImpresion)
					};
					webSocket.onmessage = function(evt) {
						PEI_message_transaccion(evt, PEI_transaccion,
								tituloImpresion)
					};
				} else {
					writeToScreen("Existe una instancia anterior en proceso.");
				}
			} else {
				writeToScreen(STRWEB_DESACTUALIZADO);
				webSocketTransaccionando = false;
				if (PEI_transaccion == "PEI_arranqueInicial") {
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
			if (PEI_transaccion == "PEI_arranqueInicial") {
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
function PEI_open_portUpdate(evt, PEI_transaccion, tituloImpresion) {
	webSocketTransaccionando = true;
	// writeToScreen("CONECTADO");
	webSocket.send('');
}

function PEI_close_portUpdate(evt, PEI_transaccion, tituloImpresion) {
	if (webSocketTransaccionando) {
		if (PEI_transaccion == "PEI_arranqueInicial") {
			controlarBotones("arranqueInicial", true);
		} else {
			controlarBotones("", true);
		}
	}
	// writeToScreen("DESCONECTADO");
}

function PEI_error_portUpdate(evt, PEI_transaccion, tituloImpresion) {
	webSocketTransaccionando = false;
	if (typeof evt.data === 'undefined') {
		writeToScreen(SERVICIO_NO_DISPONIBLE);
		webSocket.close();
		webSocket = undefined;
	} else {
		writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
	}
	if (PEI_transaccion == "PEI_arranqueInicial") {
		controlarBotones("arranqueInicial", false);
	} else {
		controlarBotones("", false);
	}
}

function PEI_message_portUpdate(evt, PEI_transaccion, tituloImpresion) {
	writeToConsoleLog(evt.data);
	var myObj = JSON.parse(evt.data);
	if (myObj.codigo == -2) {
		writeToScreen(myObj.mensaje);
		webSocketTransaccionando = false;
		webSocket.close();
		webSocket = undefined;
		if (PEI_transaccion == "PEI_arranqueInicial") {
			controlarBotones("arranqueInicial", false);
		} else {
			controlarBotones("", false);
		}
	} else if (myObj.codigo == -1) {
		writeToScreen(myObj.mensaje);
		webSocketTransaccionando = false;
		webSocket.close();
		webSocket = undefined;
		if (PEI_transaccion == "PEI_arranqueInicial") {
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

				webSocket = new WebSocketTransaccion(wsUri, PEI_transaccion,
						tituloImpresion);
				webSocket.onopen = function(evt) {
					PEI_open_transaccion(evt, PEI_transaccion,
							tituloImpresion)
				};
				webSocket.onclose = function(evt) {
					PEI_close_transaccion(evt, PEI_transaccion,
							tituloImpresion)
				};
				webSocket.onerror = function(evt) {
					PEI_error_transaccion(evt, PEI_transaccion,
							tituloImpresion)
				};
				webSocket.onmessage = function(evt) {
					PEI_message_transaccion(evt, PEI_transaccion,
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
			if (PEI_transaccion == "PEI_arranqueInicial") {
				controlarBotones("arranqueInicial", false);
			} else {
				controlarBotones("", false);
			}
		}
	}
}

/*
 * PEI_transaccion
 */

function PEI_open_transaccion(evt, PEI_transaccion, tituloImpresion) {
	// writeToScreen("CONECTADO");
	if (PEI_transaccion == "PEI_altaLG") {
		if ((tipoCliente == "M") || (tipoCliente == "D")) {
			webSocket.send('{"funcion": "' + PEI_transaccion
					+ '" , "token": "' + token + '", "idClienteParaAltaLG": "'
					+ peiIdCliente + '", "tituloImpresion": "'
					+ tituloImpresion + '", "forzarInicializarPei": "'
					+ forzarInicializarPei + '"}');
		} else {
			webSocket.send('{"funcion": "' + PEI_transaccion
					+ '" , "token": "' + token + '", "tituloImpresion": "'
					+ tituloImpresion + '", "forzarInicializarPei": "'
					+ forzarInicializarPei + '"}');
		}
	} else {
		webSocket.send('{"funcion": "' + PEI_transaccion + '" , "token": "'
				+ token + '", "importe": "' + importePei
				+ '", "forzarInicializarPei": "' + forzarInicializarPei
				+ '"}');
	}
	// webSocket.send('{"funcion": "PEI_test2"}');
}

function PEI_close_transaccion(evt, PEI_transaccion, tituloImpresion) {
	if (webSocketTransaccionando) {
		if (PEI_transaccion == "PEI_arranqueInicial") {
			controlarBotones("arranqueInicial", true);
		} else {
			controlarBotones("", true);
		}
	}
}

function PEI_error_transaccion(evt, PEI_transaccion, tituloImpresion) {
	webSocketTransaccionando = false;
	if (typeof evt.data === 'undefined') {
		writeToScreen(SERVICIO_NO_DISPONIBLE);
		webSocket.close();
		webSocket = undefined;
	} else {
		writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
	}
	if (PEI_transaccion == "PEI_arranqueInicial") {
		controlarBotones("arranqueInicial", false);
	} else {
		controlarBotones("", false);
	}
}

function PEI_message_transaccion(evt, PEI_transaccion, tituloImpresion) {
	writeToConsoleLog(evt.data);
	var myObj = JSON.parse(evt.data);
	if (myObj.codigo == -3) {
		try {
			webSocket.close();
			webSocket = undefined;
			if ((typeof webSocket === 'undefined') || (webSocket == undefined)) {
				webSocket = new WebSocketTransaccion(wsUriSTRPortUpdate,
						PEI_transaccion, tituloImpresion);
				webSocket.onopen = function(evt) {
					PEI_open_portUpdate(evt, PEI_transaccion, tituloImpresion)
				};
				webSocket.onclose = function(evt) {
					PEI_close_portUpdate(evt, PEI_transaccion,
							tituloImpresion)
				};
				webSocket.onerror = function(evt) {
					PEI_error_portUpdate(evt, PEI_transaccion,
							tituloImpresion)
				};
				webSocket.onmessage = function(evt) {
					PEI_message_portUpdate(evt, PEI_transaccion,
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
		if (PEI_transaccion == "PEI_validarImporteCarga") {
			controlarBotones("arranqueInicial", false);
		} else if (PEI_transaccion == "PEI_arranqueInicial") {
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

		if (PEI_transaccion == "PEI_arranqueInicial") {
			controlarBotones("arranqueInicial", false);
		} else if (PEI_transaccion == "PEI_reiniciar") {
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

		if (PEI_transaccion == "PEI_consultarSaldoTC") {
			writeToScreen("<button type='button' onclick='webSocketReimprimirUltimoTicketOK(\"ConsultaSaldoTC\");return false;' class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only'><span class='ui-button-text ui-c'>IMPRIMIR</span></button>");
		} else if (PEI_transaccion == "PEI_consultarSaldoLG") {
			writeToScreen("<button type='button' onclick='webSocketReimprimirUltimoTicketOK(\"ConsultaSaldoLG\");return false;' class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only'><span class='ui-button-text ui-c'>IMPRIMIR</span></button>");
		}
		webSocketTransaccionando = false;
		webSocket.close();
		webSocket = undefined;

		if (PEI_transaccion == "PEI_validarImporteCarga") {
			webSocketCargarTarjetaButton();
		} else if (PEI_transaccion == "PEI_arranqueInicial") {
			inicializadoOK(false);
			forzarInicializarPei = false;
			controlarBotones("arranqueInicial", false);
		} else if (PEI_transaccion == "PEI_reiniciar") {
			writeToScreenClear("Proceso de reiniciado correcto.");
			myObj.iLGInicializado = -1;
			myObj.cEstadoTurno = "";
			myObj.cLotesDisp = -1;
			controlarBotones("", false);
		} else if (PEI_transaccion == "PEI_obtenerVersion") {
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
		if (PEI_transaccion == "PEI_validarImporteCarga") {

			/*
			 * var r = confirm("IMPORTE A CARGAR $" + Number(importePei /
			 * 100).toFixed(2) + " CONFIRMA OPERACION?");
			 * 
			 * if (r == true) { webSocketCargarTarjetaButton(); }else{
			 * controlarBotones("", false); }
			 */

			$("#importeExcedidoPEI").text(
					"IMPORTE A CARGAR $" + Number(importePei / 100).toFixed(2)
							+ " CONFIRMA OPERACION?");

			PF('confirmaCargaExcedidaDialogWV').show();
		} else if (PEI_transaccion == "PEI_arranqueInicial") {
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
		if (PEI_transaccion == "PEI_arranqueInicial") {
			controlarBotones("arranqueInicial", false);
		} else {
			controlarBotones("", false);
		}
		actualizarHeader(myObj);
	}
}