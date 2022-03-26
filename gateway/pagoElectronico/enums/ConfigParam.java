package com.americacg.cargavirtual.gateway.pagoElectronico.enums;

//===============================================================================
//PARAMETROS DISPONIBLES PARA LAS CONFIGURACIONES DEL GATEWAY
//===============================================================================
//Parametro..: INICIO_TRAMA
//Descripcion: Caracter para inicio de trama.
//Obligatorio: 
//-------------------------------------------------------------------------------
//Parametro..: SEPARADOR_TRAMA
//Descripcion: Caracter para separ campos de la trama.
//Obligatorio: 
//-------------------------------------------------------------------------------
//Parametro..: FIN_TRAMA
//Descripcion: Caracter para fin de trama.
//Obligatorio: 
//-------------------------------------------------------------------------------
//Parametro..: SEPARADOR_HEADER_DATA
//Descripcion: Caracter para separar el header del data de la trama.
//Obligatorio: 
//-------------------------------------------------------------------------------
//Parametro..: INICIO_VECTOR
//Descripcion: Caracter para identificar el inicio de un vector.
//Obligatorio: 
//-------------------------------------------------------------------------------
//Parametro..: FIN_VECTOR
//Descripcion: Caracter para identificar el fin de un vector.
//Obligatorio: 
//-------------------------------------------------------------------------------
//Parametro..: SEPARADOR_ITEM
//Descripcion: Caracter para separar items de un vector.
//Obligatorio: 
//-------------------------------------------------------------------------------
//Parametro..: INICIO_TICKET
//Descripcion: Caracter para identificar el inicio de un ticket.
//Obligatorio: 
//-------------------------------------------------------------------------------
//Parametro..: FIN_TICKET
//Descripcion: Caracter para identificar el fin de un ticket.
//Obligatorio: 
//-------------------------------------------------------------------------------
public enum ConfigParam {
	INICIO_TRAMA,SEPARADOR_TRAMA,FIN_TRAMA,SEPARADOR_HEADER_DATA,INICIO_VECTOR,FIN_VECTOR,SEPARADOR_ITEM,INICIO_TICKET,FIN_TICKET;
}
