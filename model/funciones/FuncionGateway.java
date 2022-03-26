package com.americacg.cargavirtual.sube.model.funciones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;

import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.sube.enums.FuncionName;
import com.americacg.cargavirtual.sube.model.ClaseBase;
import com.americacg.cargavirtual.sube.model.ErrorGateway;
import com.americacg.cargavirtual.sube.model.HeaderInGateway;
import com.americacg.cargavirtual.sube.model.MensajeOutboundGateway;
import com.americacg.cargavirtual.sube.model.parametros.SeparadorTrama;
import com.americacg.cargavirtual.sube.model.parametros.funciones.input.DataInputFcn;
import com.americacg.cargavirtual.sube.model.parametros.funciones.input.DataInputFcnGateway;

public abstract class FuncionGateway extends ClaseBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4691685146762917602L;
	protected SeparadorTrama separadorTrama; 
	protected DataInputFcnGateway dataIn;
	protected HeaderInGateway headerIn;
	
	private Long idProceso;
	private String ipGatewaySUBE;
	private String dnsGatewaySUBE;
	private Integer puertoGatewaySUBE;
	private Integer timeoutConnGatewaySUBE;
	private Integer timeoutRWGatewaySUBE;
	private String lastServerConn = "NO CONECTADO";
	private FuncionName codigoFuncionSUBE;
	
	
	
	/**
	 * 
	 */
	/*
	private static final long serialVersionUID = 1377206285171084802L;
	private WSConfig wsConfig;
	
	
	
	
	private boolean ternaValida = false;
	
*/

	public FuncionGateway(Long idProceso, HeaderInGateway headerIn) throws Exception {
		this.setIdProceso(idProceso);
		this.setHeaderIn(headerIn);
	}

	public FuncionGateway(Long idProceso, HeaderInGateway headerIn, 
			SeparadorTrama separadorTrama, FuncionName codigoFuncionSUBE, 
			String data) throws Exception {
		this.setHeaderIn(headerIn);
		this.setSeparadorTrama(separadorTrama);
		this.setIdProceso(idProceso);
		this.setCodigoFuncionSUBE(codigoFuncionSUBE);
	}

	public HeaderInGateway getHeaderIn() {
		return headerIn;
	}

	public void setHeaderIn(HeaderInGateway headerIn) {
		this.headerIn = headerIn;
	}

	/*
	public DataInputFcnGateway getDataIn() {
		return dataIn;
	}

	public void setDataIn(DataInputFcnGateway dataIn) throws Exception {
		this.dataIn = dataIn;

		// --- Metodo invocado para que parsee la data cargada
		if (!this.getDataIn().parseData()) {
			throw new ParseDataInFcnException(
					"Error en parseo de datos de entrada");
		}

	}
*/
	public SeparadorTrama getSeparadorTrama() {
		return separadorTrama;
	}

	public void setSeparadorTrama(SeparadorTrama separadorTrama) {
		this.separadorTrama = separadorTrama;
	}

	public Long getIdProceso() {
		return idProceso;
	}

	public void setIdProceso(Long idProceso) {
		this.idProceso = idProceso;
	}

	public ErrorGateway validar() {
		ErrorGateway oErr = new ErrorGateway();
		oErr.setCodigoError("M0000");
		oErr.setHayError(false);
		oErr.setMsgError("");
		try {
			

			// --- Valida credenciales del usuario ---
			if ((this.getHeaderIn().getUsuario() == null ||  this.getHeaderIn().getUsuario().isEmpty()) 
				|| (this.getHeaderIn().getClave() == null ||  this.getHeaderIn().getClave().isEmpty())){
				oErr.setHayError(true);
				oErr.setCodigoError("M0001");
				oErr.setMsgError("Usuario y/o password invalida");

				LogACGHelper.escribirLog(this.getIdProceso(),
						"Usuario y/o password invalidos.");

			}

		
		} catch (Exception ex) {
			LogACGHelper.escribirLog(this.getIdProceso(),
					"Error en validador (" + this.getClass().getSimpleName()
							+ ") Msg: |" + ex.getMessage() + "|");

			oErr.setCodigoError("M0001");
			oErr.setHayError(true);
			oErr.setMsgError("Error al autenticar usuario.");
		}

		return oErr;
	}


	protected String armarTramaHeaderExtend(){
		return null;
	}

	protected String armarTramaDataExtend(){
		return null;
	}

	public String armarTrama(Long idGPL, DataInputFcn dataInputNT) {
		StringBuilder oSB = new StringBuilder();
		
		oSB.append(this.separadorTrama.getInicioTrama());
		oSB.append(".").append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getProveedor()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getVersion()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getCanal()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getFuncion().name()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getMarcaPOS()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getNroSeriePos()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getImeiPOS()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getImeiSIM()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getMedioConexion()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getNroTelefonoDelSIM()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getModelo()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getIdMayorista()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getIdCliente()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getIdUsuario()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getUsuario()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getClave()).append(this.separadorTrama.getSeparadorTrama());
		
		
		SimpleDateFormat mdyFormat = new SimpleDateFormat("YYYYMMDDHHMMSS");
		
		oSB.append(mdyFormat.format(this.headerIn.getFechaCliente())).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getIdTransaccionCliente()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getTipoCliente()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getVersionPOS());
		
		
		if(this.armarTramaHeaderExtend() != null){
			oSB.append(this.separadorTrama.getSeparadorTrama()).append(this.armarTramaHeaderExtend());
		}
		oSB.append(this.separadorTrama.getSeparadorHeaderData());
		if(this.armarTramaDataExtend() != null){
			oSB.append(this.armarTramaDataExtend());
		}
		oSB.append(this.separadorTrama.getFinTrama());
		
		
		try {
			this.headerIn.setHashSecurity(calcularHashSecurity(
					null, "5p9qJrSCHUziM7QS", oSB.toString()));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		oSB = new StringBuilder();
		oSB.append(this.separadorTrama.getInicioTrama());
		oSB.append(this.headerIn.getHashSecurity()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getProveedor()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getVersion()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getCanal()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getFuncion().name()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getMarcaPOS()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getNroSeriePos()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getImeiPOS()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getImeiSIM()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getMedioConexion()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getNroTelefonoDelSIM()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getModelo()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getIdMayorista()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getIdCliente()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getIdUsuario()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getUsuario()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getClave()).append(this.separadorTrama.getSeparadorTrama());
		
		mdyFormat = new SimpleDateFormat("YYYYMMDDHHMMSS");
		
		
		oSB.append(mdyFormat.format(this.headerIn.getFechaCliente())).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getIdTransaccionCliente()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getTipoCliente()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getVersionPOS());

		if(this.armarTramaHeaderExtend() != null){
			oSB.append(this.separadorTrama.getSeparadorTrama()).append(this.armarTramaHeaderExtend());
		}
		oSB.append(this.separadorTrama.getSeparadorHeaderData());
		if(this.armarTramaDataExtend() != null){
			oSB.append(this.armarTramaDataExtend());
		}
		oSB.append(this.separadorTrama.getFinTrama());
		
		
		return oSB.toString();
	}

	public final MensajeOutboundGateway enviarTrama(String peticion) throws Exception {
		Socket conexion = null;

		MensajeOutboundGateway mob = null;
		BufferedWriter dataToServer = null;
		// PrintWriter dataToServer = null;
		BufferedReader dataFromServer = null;
		String ip = null;
		Integer puerto = null;
		int ch = 0;
		StringBuilder oSB = null;

		LogACGHelper.escribirLog(
				this.getIdProceso(),
				"Fcn: |" + this.getClass().getSimpleName() + "| Conectar a: |"
						+ " dnsGatewaySUBE: |" + this.dnsGatewaySUBE + "|"
						+ ",  ipGatewaySUBE: |" + this.ipGatewaySUBE + "|"
						+ ", puertoProveedor: |" + this.puertoGatewaySUBE + "|"
						+ ", timeoutConnGatewaySUBE: |" + this.timeoutConnGatewaySUBE + "|"
						+ ", timeoutRWGatewaySUBE: |" + this.timeoutRWGatewaySUBE + "|");
		
		try {

			// COMIENZO CONEXION //

			//Convierto el puerto a int
			puerto = this.puertoGatewaySUBE.intValue(); // puerto

			try {

				LogACGHelper.escribirLog(this.getIdProceso(), "Conexion contra DNS");

				conexion = crearSocket(this.getIdProceso(), this.dnsGatewaySUBE, puerto, this.timeoutConnGatewaySUBE, this.timeoutRWGatewaySUBE);
				ip = this.dnsGatewaySUBE;
				
			} catch (Exception e) {

				LogACGHelper.escribirLog(this.getIdProceso(),	"Fallo la conexion contra el DNS al GatewaySUBE (Conecto contra la IP)");

				conexion = null;
				conexion = crearSocket(this.getIdProceso(), this.ipGatewaySUBE, puerto, this.timeoutConnGatewaySUBE, this.timeoutRWGatewaySUBE);
				ip = this.ipGatewaySUBE;
			}			


		} catch (Exception e) {
			conexion = null;
			LogACGHelper.escribirLog(this.getIdProceso(), "Fcn: |"
					+ this.getClass().getSimpleName()
					+ "| Fallo la conexion al servidor al GatewaySUBE por DNS y por IP.");
			throw new Exception("Error en la conexion al servidor al GatewaySUBE por DNS y por IP.");
		}
		
		

		if (conexion == null) {
			LogACGHelper.escribirLog(this.getIdProceso(), "Funcion ["
					+ this.getClass().getSimpleName() + "] Error de conexion contra el GatewaySUBE.");
			throw new Exception(
					"Error de conexion contra el GatewaySUBE.");
		}

		try {
			LogACGHelper.escribirLog(this.getIdProceso(), "Fcn: |"
					+ this.getClass().getSimpleName()
					+ "| Conectado contra: |" + ip + "| Puerto: |"
					+ puerto + "|");

			LogACGHelper.escribirLog(this.getIdProceso(), "Fcn: |"
					+ this.getClass().getSimpleName()
					+ "| Creo Stream de salida");

			dataToServer = new BufferedWriter(new OutputStreamWriter(conexion.getOutputStream()));
			dataFromServer = new BufferedReader(new InputStreamReader(
					conexion.getInputStream(), "ISO-8859-1"));

			LogACGHelper.escribirLog(this.getIdProceso(), "Fcn: |"
					+ this.getClass().getSimpleName() + "| Escribo en socket");

			LogACGHelper.escribirLog(this.getIdProceso(), "Fcn: |"
					+ this.getClass().getSimpleName() + "| message a enviar:|"
					+ new String(peticion) + "|");
			
			dataToServer.write(peticion + "\r\n");
			dataToServer.flush();

			LogACGHelper.escribirLog(this.getIdProceso(), "Fcn: |"
					+ this.getClass().getSimpleName()
					+ "| Lectura inicial desde el servidor de SUBE |");
					
			ch = dataFromServer.read();

			oSB = new StringBuilder();

			while (ch != -1) {

				/*
				 * concateno caracteres leidos
				 */
				oSB.append((char) ch);

				/*
				 * leo el proximo caracter
				 */
				ch = dataFromServer.read();
			}

			// Cierro conexion
			LogACGHelper.escribirLog(this.getIdProceso(), "Fcn: |"
					+ this.getClass().getSimpleName() + "| Cerrar conexion");
			
			conexion.close();

			LogACGHelper.escribirLog(this.getIdProceso(), "Rta Socket: |" + oSB.toString() + "|");

			
			mob = new MensajeOutboundGateway(idProceso,  separadorTrama, oSB.toString()); 
			mob.parseData();
			return mob;

		} catch (SocketTimeoutException st) {
			LogACGHelper.escribirLog(this.getIdProceso(), "Funcion ["
					+ this.getClass().getSimpleName() + "] Timeout en envio/recepcion de msg. SUBE. Error: |"
					+ st.getMessage() + "|");
			throw new Exception(
					"Timeout en envio/recepcion de msg. SUBE");
		} catch (Exception e) {
			LogACGHelper.escribirLog(this.getIdProceso(), "Funcion ["
					+ this.getClass().getSimpleName() + "] Error en envio/recepcion trama SUBE. Error: |"
					+ e.getMessage() + "|");

			throw new Exception(
					"Error en envio/recepcion trama SUBE.");
		}

	}
	
	private String calcularHashSecurity(Long idProceso,
			String claveParaCalcularHash,
			String strOrigen) throws Exception {

		// ----------------------------------------//
		// CALCULO HASH - VALIDACION SHA1 //
		// ----------------------------------------//
		String leidoDelSocketW = "";
		String hash = "";

		if(claveParaCalcularHash == null)
			throw new Exception("Clave para hash seguridad core no configurada.");
		
		try {
			leidoDelSocketW = strOrigen.concat(claveParaCalcularHash);

			hash = generarHash(idProceso, leidoDelSocketW);
		} catch (Exception e) {
			LogACGHelper.escribirLog(idProceso,
							"Error calculando el HashBinario: |"
									+ e.getMessage() + "|");
		}

		return hash.toUpperCase();
	}
	
	private String generarHash(Long idProceso, String strOrigen){

		// ----------------------------------------//
		// CALCULO HASH - VALIDACION SHA1 //
		// ----------------------------------------//
		MessageDigest md;
		byte[] buffer, digest;
		String hash = "";

		try {
			LogACGHelper.escribirLog(idProceso, "Calculo Hash en Hexa de: |" + strOrigen + "|");

			buffer = strOrigen.getBytes();
			md = MessageDigest.getInstance("SHA1");
			md.update(buffer);
			digest = md.digest();

			for (byte aux : digest) {
				int b = aux & 0xff;
				if (Integer.toHexString(b).length() == 1) {
					hash += "0";
				}
				hash += Integer.toHexString(b);
			}

			LogACGHelper.escribirLog(idProceso, "Hash Generado en Hexa: |" + hash + "|");

		} catch (Exception e) {
			LogACGHelper.escribirLog(
							idProceso,
							"Error calculando el HashBinario: |"
									+ e.getMessage() + "|");
		}

		return hash.toUpperCase();		
	}

	
	private Socket crearSocket(Long idConector, String ip, Integer puerto
			,Integer timeOutConnectMS
			,Integer timeOutReadingMS) throws Exception{

		
		LogACGHelper.escribirLog(idConector, "Conectar a: |" + ip + ":" + puerto + "|"
				+ ", TimeOutConexion: |" + timeOutConnectMS + "|"
				+ ", TimeOutReadSocket: |" + timeOutReadingMS + "|");
		

		// Create socket en null
		Socket socket = null;

		LogACGHelper.escribirLog(idConector, "Creo el Socket sin SSL");
		socket = new Socket();
		LogACGHelper.escribirLog(idConector, "Socket sin SSL Creado");


		// Bind to a local ephemeral port
		socket.bind(null);
		LogACGHelper.escribirLog(idConector, "Puerto Bindeado");

		
		//Realizo la conexion
		socket.connect(new InetSocketAddress(ip, puerto), timeOutConnectMS);
		LogACGHelper.escribirLog(idConector, "Conectado!!! contra: |" + ip + ":" + puerto + "|");

		
		// Seteo el timeOut
		LogACGHelper.escribirLog(idConector, "Seteo el timeOut: |" + timeOutReadingMS + "| milisegundos");
		socket.setSoTimeout(timeOutReadingMS);
		
		return socket;

	}
	

	public abstract MensajeOutboundGateway ejecutar();

	public DataInputFcnGateway getDataIn() {
		return dataIn;
	}

	public void setDataIn(DataInputFcnGateway dataIn) {
		this.dataIn = dataIn;
	}

	public String getIpGatewaySUBE() {
		return ipGatewaySUBE;
	}

	public void setIpGatewaySUBE(String ipGatewaySUBE) {
		this.ipGatewaySUBE = ipGatewaySUBE;
	}

	public Integer getPuertoGatewaySUBE() {
		return puertoGatewaySUBE;
	}

	public void setPuertoGatewaySUBE(Integer puertoGatewaySUBE) {
		this.puertoGatewaySUBE = puertoGatewaySUBE;
	}

	public Integer getTimeoutConnGatewaySUBE() {
		return timeoutConnGatewaySUBE;
	}

	public void setTimeoutConnGatewaySUBE(Integer timeoutConnGatewaySUBE) {
		this.timeoutConnGatewaySUBE = timeoutConnGatewaySUBE;
	}

	public Integer getTimeoutRWGatewaySUBE() {
		return timeoutRWGatewaySUBE;
	}

	public void setTimeoutRWGatewaySUBE(Integer timeoutRWGatewaySUBE) {
		this.timeoutRWGatewaySUBE = timeoutRWGatewaySUBE;
	}

	public String getLastServerConn() {
		return lastServerConn;
	}

	public void setLastServerConn(String lastServerConn) {
		this.lastServerConn = lastServerConn;
	}

	public FuncionName getCodigoFuncionSUBE() {
		return codigoFuncionSUBE;
	}

	public void setCodigoFuncionSUBE(FuncionName codigoFuncionSUBE) {
		this.codigoFuncionSUBE = codigoFuncionSUBE;
	}

	public String getDnsGatewaySUBE() {
		return dnsGatewaySUBE;
	}

	public void setDnsGatewaySUBE(String dnsGatewaySUBE) {
		this.dnsGatewaySUBE = dnsGatewaySUBE;
	}
}
