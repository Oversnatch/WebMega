package com.americacg.cargavirtual.gateway.pagoElectronico.model;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import com.americacg.cargavirtual.gateway.pagoElectronico.enums.FuncionName;
import com.americacg.cargavirtual.gateway.pagoElectronico.enums.FuncionParam;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionANPT;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionCOPA;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionGCOM;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionLIAP;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionLIPA;
import com.americacg.cargavirtual.gateway.pagoElectronico.funciones.FuncionPAGO;
import com.americacg.cargavirtual.gateway.pagoElectronico.helper.CryptoHelper;
import com.americacg.cargavirtual.gateway.pagoElectronico.helper.GatewayHelper;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.SeparadorTrama;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;

public class MensajeInboundGateway extends ClaseBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8695458945440088998L;
	private static final long TOTAL_CAMPOS_HEADER = 20;
	private HeaderInGateway headerIn = null;
	private String dataSocket = null;
	private SeparadorTrama separadorTrama;
	private ErrorGateway errorGateway;
	private Integer campoAParsear = 0;
	private String versionGateway = "";
	private boolean omitirSeparadores = false;

	public MensajeInboundGateway(Map<FuncionParam, Object> parametros) {
		this.dataSocket = (String)parametros.get(FuncionParam.DATAIN);
		this.separadorTrama = (SeparadorTrama)parametros.get(FuncionParam.SEPARADORTRAMA);
		this.headerIn = new HeaderInGateway();
		this.setIdProceso((Long)parametros.get(FuncionParam.IDPROCESO));
		this.versionGateway = (String)parametros.get(FuncionParam.VERSIONGATEWAY);
		this.omitirSeparadores = (boolean)parametros.get(FuncionParam.OMITIRSEPARADORES);
	}

	public HeaderInGateway getHeaderIn() {
		return headerIn;
	}

	public void setHeaderIn(HeaderInGateway headerIn) {
		this.headerIn = headerIn;
	}

	public SeparadorTrama getSeparadorTrama() {
		return separadorTrama;
	}

	public void setSeparadorTrama(SeparadorTrama separadorTrama) {
		this.separadorTrama = separadorTrama;
	}

	public String getDataSocket() {
		return dataSocket;
	}

	public void setDataSocket(String dataSocket) {
		this.dataSocket = dataSocket;
	}

	public ErrorGateway getErrorGateway() {
		return errorGateway;
	}

	public void setErrorGateway(ErrorGateway errorGateway) {
		this.errorGateway = errorGateway;
	}

	public Integer getCampoAParsear() {
		Integer campTmp = campoAParsear;
		campoAParsear++;
		return campTmp;
	}

	public Integer getCampoActual() {
		return this.campoAParsear;
	}

	public Integer getCampoParseado() {
		return this.campoAParsear - 1;
	}

	// ------------------------------------------------------------------------------------------------------
	// DEFINICION DE METODOS
	// ------------------------------------------------------------------------------------------------------

	public FuncionGateway getEjecutor(Map<FuncionParam, Object> parametros) throws Exception {
		String[] dataSocketSplit;
		String[] camposHeader;
		String fcn;
		String prove;
		Integer indIniTrama;
		Integer indFinTrama;
		String fechaCliente;
		SimpleDateFormat sdf;
		String hashSecurityCalculado;
		//TransaccionDao oTrxDao = null;
		//TipoTransaccionDao oTipoTrxDao = null;
		//SqlSession sqlSessionMayorista = null;
		//Transaccion oTRX = null;
		//TipoTransaccion oTipoTrx = null;
		//String claveVersionEjecutable = "";
		//PagoElectronicoSqlSession sessionFactory = null;
		//Map<Long, BuildSqlSessionFactory> factoriesBBDD = null;
		//ParametroDao datosConnParametro = null;
		//WSConfig wsCoreGestionCfg = null; 
		String claveParaCalcularHash = "";

		/*
		if(!parametros.containsKey(FuncionParam.FACTORIESBBDD))
			throw new Exception("FACTORIESBBDD no informado.");
		
		factoriesBBDD = (Map<Long, BuildSqlSessionFactory>)parametros.get(FuncionParam.FACTORIESBBDD);

		if(!parametros.containsKey(FuncionParam.PARAMETRODAO))
			throw new Exception("PARAMETRODAO no informado.");

		datosConnParametro = (ParametroDao)parametros.get(FuncionParam.PARAMETRODAO);

		if(!parametros.containsKey(FuncionParam.CLAVEPARACALCULARHASH))
			throw new Exception("CLAVEPARACALCULARHASH no informado.");

		claveParaCalcularHash = (String)parametros.get(FuncionParam.CLAVEPARACALCULARHASH);

		wsCoreGestionCfg = (WSConfig)parametros.get(FuncionParam.WSCOREGESTIONCFG); 
*/
		dataSocket = (String)parametros.get(FuncionParam.DATAIN);

		this.errorGateway = new ErrorGateway();
		errorGateway.setCodigoError("M0000");
		errorGateway.setHayError(false);
		errorGateway.setMsgError("");

		// Ejemplo : ☻ACG♦V2016122701♦XXXX♦BBBB↔CCCCCCCCCCCCCCCCC♥<<
		// DATA >>CCCCCCCCCCCCCCCCC
		if (dataSocket == null || (dataSocket != null && dataSocket.isEmpty())) {
			errorGateway.setError("M0001", "La peticion recibida del POS es nula o vacia.");
			LogACGHelper.escribirLog(this.getIdProceso(), "La peticion recibida del POS es nula o vacia.");
			return null;
		}

		// ------------------------------------------------------------------------------
		// Funcion para desencriptar la trama
		// ------------------------------------------------------------------------------
		dataSocket = CryptoHelper.decryptHEX(dataSocket);
		// ------------------------------------------------------------------------------

		if (dataSocket.equals("VER-APP")) {
			this.getHeaderIn().setFuncion(FuncionName.VAPP);
			return obtenerEjecutor(null);
		}

		indIniTrama = dataSocket.indexOf(getSeparadorTrama().getInicioTrama(), 0);
		indFinTrama = dataSocket.indexOf(getSeparadorTrama().getFinTrama(), 0);

		if (indIniTrama < 0 || indFinTrama <= 0) {
			errorGateway.setError("M0001", "Trama de Peticion sin caracteres de inicio y/o fin.");
			LogACGHelper.escribirLog(this.getIdProceso(), "Trama de Peticion sin caracteres de inicio y/o fin.");
			return null;
		}

		// Hace split del header y data
		dataSocketSplit = dataSocket.substring(indIniTrama + 1, indFinTrama)
				.split(Character.toString(getSeparadorTrama().getSeparadorHeaderData()), -1);

		if (dataSocketSplit == null || (dataSocketSplit != null && dataSocketSplit.length <= 0)) {
			errorGateway.setError("M0001", "Header invalido.");
			LogACGHelper.escribirLog(this.getIdProceso(), "Header invalido.");
			return null;
		}

		camposHeader = dataSocketSplit[0].split(Character.toString(getSeparadorTrama().getSeparadorTrama()), -1);

		/*
		 * ------------------------- Datos HEADER ------------------------- 0
		 * Hashsecurity 1 Proveedor 2 Version 3 Canal 4 Funcion 5 Marcapos 6 Nroseriepos
		 * 7 ImeiPOS 8 ImeiSIM 9 MedioConexion 10 NroTelefonoDelSIM 11 Modelo 12
		 * Idmayorista 13 Idcliente 14 Idusuario 15 Usuario 16 Clave 17 Fechacliente 18
		 * idTransaccionCliente
		 * 
		 * -------------------------
		 */

		if (camposHeader == null || camposHeader.length < TOTAL_CAMPOS_HEADER) {
			errorGateway.setError("M0001", "Cantidad de campos de header invalido.");
			LogACGHelper.escribirLog(this.getIdProceso(), "Cantidad de campos de header invalido.");
			return null;
		} else
			LogACGHelper.escribirLog(this.getIdProceso(), "Total campos Header: |" + camposHeader.length + "|");

		// ------------------------------------------------------------------------------------------
		// ARMA HEADER
		// ------------------------------------------------------------------------------------------
		// Valida HashSecurity
		this.getHeaderIn().setHashSecurity(camposHeader[getCampoAParsear()].toUpperCase());

		if (this.getHeaderIn().getHashSecurity() == null || (this.getHeaderIn().getHashSecurity() != null
				&& this.getHeaderIn().getHashSecurity().trim().isEmpty())) {
			errorGateway.setError("M0001", "Campo HashSecurity no informado.");
			LogACGHelper.escribirLog(this.getIdProceso(), "Campo HashSecurity no informado.");

			return null;
		} else {
			hashSecurityCalculado = GatewayHelper.calcularHashSecurity(this.getIdProceso(), this.getSeparadorTrama(),
					claveParaCalcularHash, dataSocket, this.getHeaderIn().getHashSecurity());

			if (!hashSecurityCalculado.equals(this.getHeaderIn().getHashSecurity())) {
				errorGateway.setError("M0001", "Trama desechada por fallos de seguridad.");

				LogACGHelper.escribirLog(this.getIdProceso(), "Trama desechada por fallos de seguridad.");
				LogACGHelper.escribirLog(this.getIdProceso(),
						"El Hash binario recibido en la trama no coincide con el Hash binario calculado");

				return null;
			} else
				LogACGHelper.escribirLog(this.getIdProceso(), "HashSecurity: |" + hashSecurityCalculado + "|");

		}

		// Valida Proveedor
		prove = camposHeader[getCampoAParsear()];

		if (prove == null || (prove != null && !prove.trim().equals(this.getHeaderIn().getProveedor()))) {
			errorGateway.setError("M0001", "Campo Proveedor no informado.");
			LogACGHelper.escribirLog(this.getIdProceso(), "Campo Proveedor no informado.");
			return null;
		} else
			LogACGHelper.escribirLog(this.getIdProceso(), "Proveedor: |" + prove + "|");

		this.getHeaderIn().setProveedor(prove);

		// Valida Version
		this.getHeaderIn().setVersion(camposHeader[getCampoAParsear()]);
		if (this.getHeaderIn().getVersion() == null || (this.getHeaderIn().getVersion() != null
				&& this.getHeaderIn().getVersion().trim().isEmpty())) {
			errorGateway.setError("M0001", "Campo Version no informado.");
			LogACGHelper.escribirLog(this.getIdProceso(), "Campo Version no informado.");

			return null;
		} else
			LogACGHelper.escribirLog(this.getIdProceso(), "Version: |" + this.getHeaderIn().getVersion() + "|");

		// Valida Canal
		this.getHeaderIn().setCanal(camposHeader[getCampoAParsear()]);

		if (this.getHeaderIn().getCanal() == null
				|| (this.getHeaderIn().getCanal() != null && this.getHeaderIn().getCanal().trim().isEmpty())) {
			errorGateway.setError("M0001", "Campo Canal no informado.");
			LogACGHelper.escribirLog(this.getIdProceso(), "Campo Canal no informado.");
			return null;
		} else
			LogACGHelper.escribirLog(this.getIdProceso(), "Canal: |" + this.getHeaderIn().getCanal() + "|");

		/*
		// Valida Origen
		this.getHeaderIn().setOrigen(camposHeader[getCampoAParsear()]);

		if (this.getHeaderIn().getOrigen() == null
				|| (this.getHeaderIn().getOrigen() != null && this.getHeaderIn().getOrigen().trim().isEmpty())) {
			errorGateway.setError("M0001", "Campo Origen no informado.");
			LogACGHelper.escribirLog(this.getIdProceso(), "Campo Origen no informado.");
			return null;
		} else
			LogACGHelper.escribirLog(this.getIdProceso(), "Origen: |" + this.getHeaderIn().getOrigen() + "|");
		*/
		
		// Valida Funcion
		fcn = camposHeader[getCampoAParsear()];

		if (fcn == null || (fcn != null && fcn.trim().isEmpty())) {
			errorGateway.setError("M0001", "Campo Funcion no informado o invalido.");
			LogACGHelper.escribirLog(this.getIdProceso(), "Campo Funcion no informado.");
			return null;
		} else {
			fcn = fcn.trim();

			for (FuncionName oFN : FuncionName.values()) {
				if (fcn.equalsIgnoreCase(oFN.name())) {
					this.getHeaderIn().setFuncion(oFN);
					break;
				}
			}

			if (this.getHeaderIn().getFuncion() == null) {
				errorGateway.setError("M0001", "Funcion invalida o inexistente");
				LogACGHelper.escribirLog(this.getIdProceso(), "Funcion invalida o inexistente |" + fcn + "|");
				return null;
			} else
				LogACGHelper.escribirLog(this.getIdProceso(), "Funcion: |" + this.getHeaderIn().getFuncion() + "|");
		}

		// Valida Marca POS
		this.getHeaderIn().setMarcaPOS(camposHeader[getCampoAParsear()]);

		if (this.getHeaderIn().getMarcaPOS() == null || (this.getHeaderIn().getMarcaPOS() != null
				&& this.getHeaderIn().getMarcaPOS().trim().isEmpty())) {
			errorGateway.setError("M0001", "Campo Marca no informado.");
			LogACGHelper.escribirLog(this.getIdProceso(), "Campo Marca no informado.");
			return null;
		} else
			LogACGHelper.escribirLog(this.getIdProceso(), "Marca POS: |" + this.getHeaderIn().getMarcaPOS() + "|");

		// Valida NRO SERIE POS
		this.getHeaderIn().setNroSeriePos(camposHeader[getCampoAParsear()]);

		if (this.getHeaderIn().getNroSeriePos() == null || (this.getHeaderIn().getNroSeriePos() != null
				&& this.getHeaderIn().getNroSeriePos().trim().isEmpty())) {
			errorGateway.setError("M0001", "Campo nro. serie no informado.");
			LogACGHelper.escribirLog(this.getIdProceso(), "Campo nro. serie no informado.");
			return null;
		} else
			LogACGHelper.escribirLog(this.getIdProceso(),
					"NroSeriePos: |" + this.getHeaderIn().getNroSeriePos() + "|");

		// Valida IMEI POS
		this.getHeaderIn().setImeiPOS(camposHeader[getCampoAParsear()]);

		LogACGHelper.escribirLog(this.getIdProceso(), "ImeiPos: |" + this.getHeaderIn().getImeiPOS() + "|");

		/*
		 * if (this.getHeaderIn().getImeiPOS() == null ||
		 * (this.getHeaderIn().getImeiPOS() != null &&
		 * this.getHeaderIn().getImeiPOS().trim().isEmpty())){
		 * errorGateway.setError("M0001", "Campo IMEIPOS no informado.");
		 * LogACGHelper.escribirLog(this.getIdProceso(), "Campo IMEIPOS no informado.");
		 * return null; }
		 */

		// Valida IMEI SIM
		this.getHeaderIn().setImeiSIM(camposHeader[getCampoAParsear()]);
		LogACGHelper.escribirLog(this.getIdProceso(), "ImeiSim: |" + this.getHeaderIn().getImeiSIM() + "|");

		/*
		 * if (this.getHeaderIn().getImeiSIM() == null ||
		 * (this.getHeaderIn().getImeiSIM() != null &&
		 * this.getHeaderIn().getImeiSIM().trim().isEmpty())){
		 * errorGateway.setError("M0001", "Campo IMEISIM no informado.");
		 * LogACGHelper.escribirLog(this.getIdProceso(), "Campo IMEISIM no informado.");
		 * return null; }
		 */

		// Valida Medio Conexion
		this.getHeaderIn().setMedioConexion(camposHeader[getCampoAParsear()]);

		if (this.getHeaderIn().getMedioConexion() == null || (this.getHeaderIn().getMedioConexion() != null
				&& this.getHeaderIn().getMedioConexion().trim().isEmpty())) {
			errorGateway.setError("M0001", "Campo MedioConexion no informado.");
			LogACGHelper.escribirLog(this.getIdProceso(), "Campo MedioConexion no informado.");
			return null;
		} else
			LogACGHelper.escribirLog(this.getIdProceso(),
					"MedioConexion: |" + this.getHeaderIn().getMedioConexion() + "|");

		// Valida Nro. Tel. SIM
		this.getHeaderIn().setNroTelefonoDelSIM(camposHeader[getCampoAParsear()]);
		LogACGHelper.escribirLog(this.getIdProceso(),
				"NroTelefonoDelSIM: |" + this.getHeaderIn().getNroTelefonoDelSIM() + "|");

		/*
		 * if (this.getHeaderIn().getNroTelefonoDelSIM() == null ||
		 * (this.getHeaderIn().getNroTelefonoDelSIM() != null &&
		 * this.getHeaderIn().getNroTelefonoDelSIM().trim().isEmpty())){
		 * errorGateway.setError("M0001", "Campo NroTelefonoDelSIM no informado.");
		 * LogACGHelper.escribirLog(this.getIdProceso(),
		 * "Campo NroTelefonoDelSIM no informado."); return null; }
		 */

		// Valida Modelo
		this.getHeaderIn().setModelo(camposHeader[getCampoAParsear()]);

		if (this.getHeaderIn().getModelo() == null
				|| (this.getHeaderIn().getModelo() != null && this.getHeaderIn().getModelo().trim().isEmpty())) {
			errorGateway.setError("M0001", "Campo modelo no informado.");
			LogACGHelper.escribirLog(this.getIdProceso(), "Campo modelo no informado.");
			return null;
		} else
			LogACGHelper.escribirLog(this.getIdProceso(), "Modelo: |" + this.getHeaderIn().getModelo() + "|");

		// Valida idmayorista
		getCampoAParsear();

		if (camposHeader[getCampoParseado()] == null
				|| (camposHeader[getCampoParseado()] != null && camposHeader[getCampoParseado()].isEmpty())) {
			errorGateway.setError("M0001", "Campo idMayorista no informado.");
			LogACGHelper.escribirLog(this.getIdProceso(), "Campo idMayorista no informado.");
			return null;
		} else if (camposHeader[getCampoParseado()] != null && !camposHeader[getCampoParseado()].isEmpty()) {
			try {
				this.getHeaderIn().setIdMayorista(Long.parseLong(camposHeader[getCampoParseado()]));
			} catch (Exception e) {
				errorGateway.setError("M0001", "Campo idMayorista invalido.");
				LogACGHelper.escribirLog(this.getIdProceso(), "Campo idMayorista invalido.");
				return null;
			}

			LogACGHelper.escribirLog(this.getIdProceso(),
					"Mayorista: |" + this.getHeaderIn().getIdMayorista() + "|");
		}

		// Valida idcliente
		getCampoAParsear();

		if (camposHeader[getCampoParseado()] == null
				|| (camposHeader[getCampoParseado()] != null && camposHeader[getCampoParseado()].trim().isEmpty())) {
			errorGateway.setError("M0001", "Campo idCliente no informado.");
			LogACGHelper.escribirLog(this.getIdProceso(), "Campo idCliente no informado.");
			return null;
		} else if (camposHeader[getCampoParseado()] != null && !camposHeader[getCampoParseado()].isEmpty()) {
			try {
				this.getHeaderIn().setIdCliente(Long.parseLong(camposHeader[getCampoParseado()]));
			} catch (Exception e) {
				errorGateway.setError("M0001", "Campo idCliente invalido.");
				LogACGHelper.escribirLog(this.getIdProceso(), "Campo idCliente invalido.");
				return null;
			}

			LogACGHelper.escribirLog(this.getIdProceso(), "Id Cliente: |" + this.getHeaderIn().getIdCliente() + "|");
		}

		// idUsuario
		getCampoAParsear();
		if (camposHeader[getCampoParseado()] != null && !camposHeader[getCampoParseado()].isEmpty()) {
			try {
				this.getHeaderIn().setIdUsuario(Long.parseLong(camposHeader[getCampoParseado()]));
			} catch (Exception e) {
				errorGateway.setError("M0001", "Campo idUsuario invalido.");
				LogACGHelper.escribirLog(this.getIdProceso(), "Campo idUsuario invalido.");
				return null;
			}

			LogACGHelper.escribirLog(this.getIdProceso(), "Id Usuario: |" + this.getHeaderIn().getIdUsuario() + "|");
		}

		// Valida usuario
		this.getHeaderIn().setUsuario(camposHeader[getCampoAParsear()]);

		if (this.getHeaderIn().getUsuario() == null || (this.getHeaderIn().getUsuario() != null
				&& this.getHeaderIn().getUsuario().trim().isEmpty())) {
			errorGateway.setError("M0001", "Campo usuario no informado.");
			LogACGHelper.escribirLog(this.getIdProceso(), "Campo usuario no informado.");
			return null;
		} else
			LogACGHelper.escribirLog(this.getIdProceso(), "Usuario: |" + this.getHeaderIn().getUsuario() + "|");

		// Valida clave
		this.getHeaderIn().setClave(camposHeader[getCampoAParsear()]);

		if (this.getHeaderIn().getClave() == null
				|| (this.getHeaderIn().getClave() != null && this.getHeaderIn().getClave().trim().isEmpty())) {
			errorGateway.setError("M0001", "Campo clave no informado.");
			LogACGHelper.escribirLog(this.getIdProceso(), "Campo clave no informado.");
			return null;
		} else
			LogACGHelper.escribirLog(this.getIdProceso(), "Clave: |" + this.getHeaderIn().getClave() + "|");

		// Valida fechaCliente
		getCampoAParsear();

		if (camposHeader[getCampoParseado()] == null
				|| (camposHeader[getCampoParseado()] != null && camposHeader[getCampoParseado()].trim().isEmpty())) {
			errorGateway.setError("M0001", "Campo fechaCliente no informado.");
			LogACGHelper.escribirLog(this.getIdProceso(), "Campo fechaCliente no informado.");
			return null;
		} else if (camposHeader[getCampoParseado()] != null && !camposHeader[getCampoParseado()].isEmpty()) {
			try {
				if (camposHeader[getCampoParseado()].length() < 14) {
					errorGateway.setError("M0001", "Campo fechaCliente invalido.");
					LogACGHelper.escribirLog(this.getIdProceso(), "Campo fechaCliente invalido.");
					return null;
				} else {
					// Convertir AAAAMMDDHHMMSS -->
					// AAAA-MM-DDTHH:MM:SS.000-03:00
					// "yyyyMMddHHmmss"
					// "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
					// 2001-07-04T12:08:56.235-07:00
					fechaCliente = camposHeader[getCampoParseado()].substring(0, 4) + "-" + // AÃ‘O
							camposHeader[getCampoParseado()].substring(4, 6) + "-" + // MES
							camposHeader[getCampoParseado()].substring(6, 8) + "T" + // DIA
							camposHeader[getCampoParseado()].substring(8, 10) + ":" + // HORA
							camposHeader[getCampoParseado()].substring(10, 12) + ":" + // MINUTOS
							camposHeader[getCampoParseado()].substring(12, 14) + // SEGUNDOS
							".000-03:00";

					sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

					this.getHeaderIn().setFechaCliente(sdf.parse(fechaCliente));

					LogACGHelper.escribirLog(this.getIdProceso(), "FechaCliente: |" + fechaCliente + "|");
				}
			} catch (Exception e) {
				errorGateway.setError("M0001", "Campo fechaCliente invalido.");
				LogACGHelper.escribirLog(this.getIdProceso(), "Campo fechaCliente invalido.");
				return null;
			}
		}

		// Valida TipoCliente
		this.getHeaderIn().setTipoCliente(camposHeader[getCampoAParsear()]);

		// Version POS
		this.getHeaderIn().setVersionPOS(camposHeader[getCampoAParsear()]);

			return obtenerEjecutor(null);
	}

	private FuncionGateway obtenerEjecutor(String data) throws Exception {
		Map<FuncionParam, Object> parametros = new HashMap<FuncionParam, Object>(0);
		FuncionGateway oFcn = null;
		
		switch (this.getHeaderIn().getFuncion()) {

		/*
		case VAPP: {
			parametros.clear();
			parametros.put(FuncionParam.IDPROCESO, this.getIdProceso());
			parametros.put(FuncionParam.VERSIONGATEWAY, versionGateway);
			parametros.put(FuncionParam.HEADERINGATEWAY, this.getHeaderIn());
			parametros.put(FuncionParam.DEBUG, this.isDebug());
			
			oFcn = new FuncionVAPP(parametros);
			return oFcn;
		}
		*/
		case GCOM: {
			parametros.clear();
			parametros.put(FuncionParam.IDPROCESO, this.getIdProceso());
			parametros.put(FuncionParam.VERSIONGATEWAY, versionGateway);
			parametros.put(FuncionParam.HEADERINGATEWAY, this.getHeaderIn());
			//parametros.put(FuncionParam.SESSIONFACTORY, sessionFactory);
			//parametros.put(FuncionParam.WSCOREGESTIONCFG, wsCoreGestionCfg);
			parametros.put(FuncionParam.SEPARADORTRAMA, this.getSeparadorTrama());
			parametros.put(FuncionParam.DATAIN, data);
			parametros.put(FuncionParam.DEBUG, this.isDebug());
			
			oFcn = new FuncionGCOM(parametros);
			return oFcn;
		}
/*
		case TKNP: {
			parametros.clear();
			parametros.put(FuncionParam.IDPROCESO, this.getIdProceso());
			parametros.put(FuncionParam.VERSIONGATEWAY, versionGateway);
			parametros.put(FuncionParam.HEADERINGATEWAY, this.getHeaderIn());
			//parametros.put(FuncionParam.SESSIONFACTORY, sessionFactory);
			//parametros.put(FuncionParam.WSCOREGESTIONCFG, wsCoreGestionCfg);
			parametros.put(FuncionParam.SEPARADORTRAMA, this.getSeparadorTrama());
			parametros.put(FuncionParam.DATAIN, data);
			parametros.put(FuncionParam.DEBUG, this.isDebug());
			
			oFcn = new FuncionTKNP(parametros);
			return oFcn;
		}

		case EPAG: {
			parametros.clear();
			parametros.put(FuncionParam.IDPROCESO, this.getIdProceso());
			parametros.put(FuncionParam.VERSIONGATEWAY, versionGateway);
			parametros.put(FuncionParam.HEADERINGATEWAY, this.getHeaderIn());
			//parametros.put(FuncionParam.SESSIONFACTORY, sessionFactory);
			//parametros.put(FuncionParam.WSCOREGESTIONCFG, wsCoreGestionCfg);
			parametros.put(FuncionParam.SEPARADORTRAMA, this.getSeparadorTrama());
			parametros.put(FuncionParam.DATAIN, data);
			parametros.put(FuncionParam.DEBUG, this.isDebug());
			
			oFcn = new FuncionEPAG(parametros);
			return oFcn;
		}
*/
		case PAGO: {
			parametros.clear();
			parametros.put(FuncionParam.IDPROCESO, this.getIdProceso());
			parametros.put(FuncionParam.VERSIONGATEWAY, versionGateway);
			parametros.put(FuncionParam.HEADERINGATEWAY, this.getHeaderIn());
			//parametros.put(FuncionParam.SESSIONFACTORY, sessionFactory);
			//parametros.put(FuncionParam.WSCOREGESTIONCFG, wsCoreGestionCfg);
			parametros.put(FuncionParam.SEPARADORTRAMA, this.getSeparadorTrama());
			parametros.put(FuncionParam.DATAIN, data);
			parametros.put(FuncionParam.DEBUG, this.isDebug());
			
			oFcn = new FuncionPAGO(parametros);
			return oFcn;
		}

		case ANPT: {
			parametros.clear();
			parametros.put(FuncionParam.IDPROCESO, this.getIdProceso());
			parametros.put(FuncionParam.VERSIONGATEWAY, versionGateway);
			parametros.put(FuncionParam.HEADERINGATEWAY, this.getHeaderIn());
			//parametros.put(FuncionParam.SESSIONFACTORY, sessionFactory);
			//parametros.put(FuncionParam.WSCOREGESTIONCFG, wsCoreGestionCfg);
			parametros.put(FuncionParam.SEPARADORTRAMA, this.getSeparadorTrama());
			parametros.put(FuncionParam.DATAIN, data);
			parametros.put(FuncionParam.DEBUG, this.isDebug());
			
			oFcn = new FuncionANPT(parametros);
			return oFcn;
		}

		case COPA: {
			parametros.clear();
			parametros.put(FuncionParam.IDPROCESO, this.getIdProceso());
			parametros.put(FuncionParam.VERSIONGATEWAY, versionGateway);
			parametros.put(FuncionParam.HEADERINGATEWAY, this.getHeaderIn());
			//parametros.put(FuncionParam.SESSIONFACTORY, sessionFactory);
			//parametros.put(FuncionParam.WSCOREGESTIONCFG, wsCoreGestionCfg);
			parametros.put(FuncionParam.SEPARADORTRAMA, this.getSeparadorTrama());
			parametros.put(FuncionParam.DATAIN, data);
			parametros.put(FuncionParam.DEBUG, this.isDebug());
			
			oFcn = new FuncionCOPA(parametros);
			return oFcn;
		}

		case LIPA: {
			parametros.clear();
			parametros.put(FuncionParam.IDPROCESO, this.getIdProceso());
			parametros.put(FuncionParam.VERSIONGATEWAY, versionGateway);
			parametros.put(FuncionParam.HEADERINGATEWAY, this.getHeaderIn());
			//parametros.put(FuncionParam.SESSIONFACTORY, sessionFactory);
			//parametros.put(FuncionParam.WSCOREGESTIONCFG, wsCoreGestionCfg);
			parametros.put(FuncionParam.SEPARADORTRAMA, this.getSeparadorTrama());
			parametros.put(FuncionParam.DATAIN, data);
			parametros.put(FuncionParam.DEBUG, this.isDebug());
			
			oFcn = new FuncionLIPA(parametros);
			return oFcn;
		}

		case LIAP: {
			parametros.clear();
			parametros.put(FuncionParam.IDPROCESO, this.getIdProceso());
			parametros.put(FuncionParam.VERSIONGATEWAY, versionGateway);
			parametros.put(FuncionParam.HEADERINGATEWAY, this.getHeaderIn());
			//parametros.put(FuncionParam.SESSIONFACTORY, sessionFactory);
			//parametros.put(FuncionParam.WSCOREGESTIONCFG, wsCoreGestionCfg);
			parametros.put(FuncionParam.SEPARADORTRAMA, this.getSeparadorTrama());
			parametros.put(FuncionParam.DATAIN, data);
			parametros.put(FuncionParam.DEBUG, this.isDebug());
			
			oFcn = new FuncionLIAP(parametros);
			return oFcn;
		}

		default:
			throw new Exception("Funcion invalida.");
		}
	}

	// ------------------------------------------------------------------------------------------------------
	// DEFINICION DE METODOS
	// ------------------------------------------------------------------------------------------------------
	public String getTramaParaSocket() {
		StringBuilder oSB = new StringBuilder();
		//SimpleDateFormat trxDateFmt;
		//trxDateFmt = new SimpleDateFormat("yyyyMMddHHmmss");

		if (!omitirSeparadores) {
			oSB.append(this.getSeparadorTrama().getInicioTrama());
			oSB.append(this.getHeaderIn().getIdTransaccionCliente());
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());
			oSB.append(this.getSeparadorTrama().getSeparadorHeaderData());
			
		}

		if (dataSocket != null) {

		} else {
			oSB.append("");
		}

		if (!omitirSeparadores)
			oSB.append(this.getSeparadorTrama().getFinTrama());

		try {
			return CryptoHelper.encrypt(oSB.toString());

		} catch (Exception e) {
			LogACGHelper.escribirLog(this.getIdProceso(), "No se pudo encriptar la trama. IdTransaccionCliente: |"
					+ this.getHeaderIn().getIdTransaccionCliente() + "|");

			return "";
		}
	}
	
}
