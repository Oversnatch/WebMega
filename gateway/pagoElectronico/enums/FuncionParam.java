package com.americacg.cargavirtual.gateway.pagoElectronico.enums;

//===============================================================================
//PARAMETROS DISPONIBLES PARA LAS FUNCIONES DEL GATEWAY
//===============================================================================
//Parametro..: IDPROCESO
//Descripcion: Contiene el Id del proceso en ejecucion
//Obligatorio: SI
//-------------------------------------------------------------------------------
//Parametro..: VERSIONGATEWAY
//Descripcion: Contiene la version del gateway
//Obligatorio: SI
//-------------------------------------------------------------------------------
//Parametro..: HEADERINGATEWAY
//Descripcion: Contiene los datos tipo Header que se invocaron al gateway
//Obligatorio: SI
//-------------------------------------------------------------------------------
//Parametro..: SESSIONFACTORY
//Descripcion: Contiene la session factory para la BBDD en el caso de requerir
//             utilizar acceso a datos
//Obligatorio: NO
//-------------------------------------------------------------------------------
//Parametro..: WSCOREGESTIONCFG
//Descripcion: Contiene datos de config para el acceso a ACG Web Service Framework
//Obligatorio: NO
//-------------------------------------------------------------------------------
//Parametro..: SEPARADORTRAMA
//Descripcion: Contiene los separadores de trama utilizados por las funciones
//Obligatorio: SI
//-------------------------------------------------------------------------------
//Parametro..: DATAIN
//Descripcion: Contiene el DataIn de una funcion. 
//Obligatorio: NO
//-------------------------------------------------------------------------------
//Parametro..: DEBUG
//Descripcion: Contiene el valor del parametro que indica si escribe detalle de  
//             las clases en el log.
//Obligatorio: SI
//-------------------------------------------------------------------------------
//Parametro..: IDCONFIGURACIONCOMERCIO
//Descripcion: Utilizado para identificar la configuracion del comercio que ejecuta
//             la transaccion. Este dato viene 
//Obligatorio:  
//-------------------------------------------------------------------------------
//Parametro..: FACTORIESBBDD
//Descripcion: Para el caso de habilitar el resguardo en memoria de los factories
//             de conexiones de BBDD. Se configura en el POM
//Obligatorio: SI
//-------------------------------------------------------------------------------
//Parametro..: PARAMETRODAO 
//Descripcion: Contiene los parametros para generar la conexion a la BBDD que
//             administra los parametros de la BBDD asociada a cada mayorista.
//			   Se configura en el POM
//Obligatorio: SI
//-------------------------------------------------------------------------------
//Parametro..: CLAVEPARACALCULARHASH 
//Descripcion: Clave para el hash interno del header. El hash quedo de versiones
//             anteriores pero igualmente sirve como medida adicional de seguridad.
//             Se configura en el Properties del proyecto
//Obligatorio: SI
//-------------------------------------------------------------------------------
//Parametro..: TIPOCLIENTE
//Descripcion: Tipo de cliente logueado
//Obligatorio: SI
//-------------------------------------------------------------------------------
//Parametro..: DATAIN
//Descripcion: Contiene el DataIn de una funcion. 
//Obligatorio: NO
//-------------------------------------------------------------------------------
//Parametro..: 
//Descripcion: 
//Obligatorio:  
//-------------------------------------------------------------------------------
public enum FuncionParam {
	VERSIONGATEWAY, IDPROCESO, SEPARADORTRAMA, DEBUG, HEADERINGATEWAY, SOCKETDATA, OMITIRSEPARADORES, DATAIN, TIPOCLIENTE, IDMAYORISTA, IDCLIENTE
	
	/*CLAVEPARACALCULARHASH, PROVEEDORGATEWAY, SESSIONFACTORY,
	WSCOREGESTIONCFG, FACTORIESBBDD, , IDCONFIGURACIONCOMERCIO, PARAMETRODAO */
}
