package com.americacg.cargavirtual.gateway.pagoElectronico.enums;

//=====================================================
// FUNCIONES HABILITADAS EN EL GATEWAY
//=====================================================
// Funcion....: TKNP
// Descripcion: Solicitud Token de Pago 
//-----------------------------------------------------
// Funcion....: EPAG
// Descripcion: Ejecucion de Pago
//-----------------------------------------------------
// Funcion....: TKNN
// Descripcion: Solicitud Token de Pago No Efectuado 
//-----------------------------------------------------
// Funcion....: EPNE
// Descripcion: Ejecucion de Pago NO Exitoso  
//-----------------------------------------------------
// Funcion....: ANPT
// Descripcion: Anulacion de Pago Total
//-----------------------------------------------------
// Funcion....: COPA
// Descripcion: Consulta de Pago
//-----------------------------------------------------
// Funcion....: LIPA
// Descripcion: Listado de Pagos
//-----------------------------------------------------
// Funcion....: GCOM
// Descripcion: Obtencion Configuracion Comercio
//-----------------------------------------------------
// Funcion....: PAGO
// Descripcion: ACG Pago electronico
//-----------------------------------------------------
// Funcion....: LIAP
// Descripcion: Listado de Anulaciones de un Pago
//-----------------------------------------------------
//Funcion....: LICP
//Descripcion: Listado de Conceptos de Pago BBDD ACG
//-----------------------------------------------------
//Funcion....: LIMP
//Descripcion: Listado de Medios de Pago BBDD ACG
//-----------------------------------------------------
//Funcion....: GPCU
//Descripcion: Obtencion de plan de cuotas
//-----------------------------------------------------
//Funcion....: LIBC
//Descripcion: Listado de Bancos
//-----------------------------------------------------
//Funcion....: LIOP 
//Descripcion: Listado de operadores
//-----------------------------------------------------
//Funcion....: LISI 
//Descripcion: Listado de Sites
//-----------------------------------------------------
//Funcion....: LEOP
//Descripcion: Listado de Estados Operador
//-----------------------------------------------------
//Funcion....: VABI
//Descripcion: Validador de BINS
//-----------------------------------------------------
//Funcion....: MCOM 
//Descripcion: Mantenimiento de comercio (Alta/Modificacion)
//-----------------------------------------------------
//Funcion....: LPOP
//Descripcion: Listado de Pagos en Operador
//-----------------------------------------------------

public enum FuncionName {
	//Mnemonico de funciones habilitadas para operar en el gateway.
	VAPP, TKNP, EPAG, TKNN, EPNE, ANPT, COPA, LIPA, GCOM, PAGO, LIAP, LICP, LIMP, GPCU, LIBC, LIOP, LISI, LEOP, VABI, MCOM, LPOP
}
