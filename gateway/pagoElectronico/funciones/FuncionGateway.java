package com.americacg.cargavirtual.gateway.pagoElectronico.funciones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import com.americacg.cargavirtual.gateway.pagoElectronico.enums.FuncionName;
import com.americacg.cargavirtual.gateway.pagoElectronico.enums.FuncionParam;
import com.americacg.cargavirtual.gateway.pagoElectronico.exceptions.SendRequestException;
import com.americacg.cargavirtual.gateway.pagoElectronico.exceptions.TimeoutRWException;
import com.americacg.cargavirtual.gateway.pagoElectronico.helper.CryptoHelper;
import com.americacg.cargavirtual.gateway.pagoElectronico.helper.GatewayHelper;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.ClaseBase;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.ErrorGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.HeaderInGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.ParametrosServicio;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.SeparadorTrama;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnGateway;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;

public abstract class FuncionGateway extends ClaseBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1377206285171084802L;
	private ParametrosServicio parametrosServicio;
	private HeaderInGateway headerIn;
	private DataInputFcnGateway dataIn;
	private SeparadorTrama separadorTrama;
	private Long idProceso;
	private Long idMayorista;
	private Long idCliente;
	
	private String tipoCliente;
	private FuncionName codigoFuncion;
	protected Map<FuncionParam, Object> parametros = null;

	public FuncionGateway(Map<FuncionParam, Object> parametros) throws Exception {
		this.setIdProceso((Long) parametros.get(FuncionParam.IDPROCESO));
		this.parametros = parametros;
		if(parametros.containsKey(FuncionParam.SEPARADORTRAMA))
			this.setSeparadorTrama((SeparadorTrama)parametros.get(FuncionParam.SEPARADORTRAMA));
		if(parametros.containsKey(FuncionParam.TIPOCLIENTE))
			this.tipoCliente = (String)parametros.get(FuncionParam.TIPOCLIENTE);
		
		if(parametros.containsKey(FuncionParam.IDMAYORISTA))
			this.idMayorista = (Long)parametros.get(FuncionParam.IDMAYORISTA);
		
		if(parametros.containsKey(FuncionParam.IDCLIENTE))
			this.idCliente = (Long)parametros.get(FuncionParam.IDCLIENTE);
		
		this.setHeaderIn(inicializarHeaderInGateway());		
	}

	public FuncionGateway(Map<FuncionParam, Object> parametros, FuncionName codigoFuncion) throws Exception {
		this.setIdProceso((Long) parametros.get(FuncionParam.IDPROCESO));
		
		this.setCodigoFuncion(codigoFuncion);
		this.parametros = parametros;
		if(parametros.containsKey(FuncionParam.SEPARADORTRAMA))
			this.setSeparadorTrama((SeparadorTrama)parametros.get(FuncionParam.SEPARADORTRAMA));
		if(parametros.containsKey(FuncionParam.TIPOCLIENTE))
			this.tipoCliente = (String)parametros.get(FuncionParam.TIPOCLIENTE);
		
		if(parametros.containsKey(FuncionParam.IDMAYORISTA))
			this.idMayorista = (Long)parametros.get(FuncionParam.IDMAYORISTA);
		
		if(parametros.containsKey(FuncionParam.IDCLIENTE))
			this.idCliente = (Long)parametros.get(FuncionParam.IDCLIENTE);
		
		this.setHeaderIn(inicializarHeaderInGateway());
		this.getHeaderIn().setFuncion(codigoFuncion);
	}
	
	
	private HeaderInGateway inicializarHeaderInGateway(){
		HeaderInGateway oHeader = new HeaderInGateway();
		
		oHeader.setHashSecurity(".");
		oHeader.setProveedor("ACG");
		oHeader.setVersion("V1.0");
		oHeader.setCanal("WEBINTRA");
		oHeader.setMarcaPOS("BROWSER");//
		oHeader.setNroSeriePos("999-999-999");//
		oHeader.setImeiPOS("");//
		oHeader.setImeiSIM("");//
		oHeader.setMedioConexion("L");//
		oHeader.setNroTelefonoDelSIM("");//
		oHeader.setModelo("BROWSER");//
		oHeader.setFechaCliente(new Date());//
		oHeader.setIdTransaccionCliente(GatewayHelper.generaIdTrxCliente(this.idMayorista, this.idCliente));
		oHeader.setTipoCliente(this.tipoCliente);//
		oHeader.setVersionPOS("V1.0");// id transaccion cliente
		oHeader.setTokenAuth("");// id transaccion cliente 
		
		return oHeader;
	}

	public FuncionName getCodigoFuncion() {
		return codigoFuncion;
	}

	public void setCodigoFuncion(FuncionName codigoFuncion) {
		this.codigoFuncion = codigoFuncion;
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

	public ParametrosServicio getParametrosServicio() {
		return parametrosServicio;
	}

	public void setParametroServicio(ParametrosServicio parametrosServicio) {
		this.parametrosServicio = parametrosServicio;
	}

	public Long getIdProceso() {
		return idProceso;
	}

	public void setIdProceso(Long idProceso) {
		this.idProceso = idProceso;
	}

	public ErrorGateway validar(boolean omitirDatosLG) {
		return validarInt(omitirDatosLG);
	}

	public ErrorGateway validar() {
		return validarInt(false);
	}

	private ErrorGateway validarInt(boolean omitirDatosLG) {
		ErrorGateway oErr = new ErrorGateway();
		oErr.setCodigoError("M0000");
		oErr.setHayError(false);
		oErr.setMsgError("");
		return oErr;
	}

	public final String enviarTrama(String peticion) throws Exception {
		Socket conexion = null;
		String respuesta = "";
		BufferedWriter dataToServer = null;
		// PrintWriter dataToServer = null;
		BufferedReader dataFromServer = null;
		String ip = null;
		Integer puerto = null;
		int ch = 0;
		StringBuilder oSB = null;

		conexion = new Socket();

		try {

			try {

				LogACGHelper.escribirLog(this.getIdProceso(), "Conexion contra DNS");
				
				LogACGHelper.escribirLog(this.getIdProceso(), "Fcn: |" + this.getClass().getSimpleName() + "| Conectar a: |"
						+ this.getParametrosServicio().getParametros().get("DnsGateway").getValor() + ":"
						+ this.getParametrosServicio().getParametros().get("PuertoGateway").getValor() + "|. TimeOutConexion: |"
						+ this.getParametrosServicio().getParametros().get("TimeoutConnGateway").getValor()
						+ "|. TimeOutReadSocket: |"
						+ this.getParametrosServicio().getParametros().get("TimeoutRWGateway").getValor() + "|");


				// Conecto contra la IP Principal
				conexion.bind(null);

				conexion.connect(
						new InetSocketAddress(
								(String) this.getParametrosServicio().getParametros().get("DnsGateway").getValor(),
								Integer.parseInt(
										this.getParametrosServicio().getParametros().get("PuertoGateway").getValor())),
						Integer.parseInt(
								this.getParametrosServicio().getParametros().get("TimeoutConnGateway").getValor())); // TimeOut
				// en
				// milisegundos

				conexion.setSoTimeout(
						Integer.parseInt(this.getParametrosServicio().getParametros().get("TimeoutRWGateway").getValor()));
				ip = (String) this.getParametrosServicio().getParametros().get("DnsGateway").getValor();
				puerto = Integer.parseInt(this.getParametrosServicio().getParametros().get("PuertoGateway").getValor());

			} catch (Exception e) {
				
				
				if(conexion != null) {
					conexion = null;
					conexion = new Socket();
				}

				LogACGHelper.escribirLog(this.getIdProceso(),	"Fallo la conexion contra el DNS al GatewaySUBE (Conecto contra la IP)");

				LogACGHelper.escribirLog(this.getIdProceso(), "Fcn: |" + this.getClass().getSimpleName() + "| Conectar a: |"
						+ this.getParametrosServicio().getParametros().get("IpGateway").getValor() + ":"
						+ this.getParametrosServicio().getParametros().get("PuertoGateway").getValor() + "|. TimeOutConexion: |"
						+ this.getParametrosServicio().getParametros().get("TimeoutConnGateway").getValor()
						+ "|. TimeOutReadSocket: |"
						+ this.getParametrosServicio().getParametros().get("TimeoutRWGateway").getValor() + "|");

				// Conecto contra la IP Principal
				conexion.bind(null);

				conexion.connect(
						new InetSocketAddress(
								(String) this.getParametrosServicio().getParametros().get("IpGateway").getValor(),
								Integer.parseInt(
										this.getParametrosServicio().getParametros().get("PuertoGateway").getValor())),
						Integer.parseInt(
								this.getParametrosServicio().getParametros().get("TimeoutConnGateway").getValor())); // TimeOut
				// en
				// milisegundos

				conexion.setSoTimeout(
						Integer.parseInt(this.getParametrosServicio().getParametros().get("TimeoutRWGateway").getValor()));
				ip = (String) this.getParametrosServicio().getParametros().get("IpGateway").getValor();
				puerto = Integer.parseInt(this.getParametrosServicio().getParametros().get("PuertoGateway").getValor());
			}			

		} catch (SocketTimeoutException st2) {
			if(conexion != null) {
				conexion.close();
			}
			LogACGHelper.escribirLog(this.getIdProceso(),
					"Funcion [" + this.getClass().getSimpleName() + "] enviarTrama(): |" + st2.getMessage() + "|");
			throw new TimeoutRWException("No se pudo establecer la comunicacion (GPE-TOC)");
		} catch (Exception e) {
			if(conexion != null) {
				conexion.close();
			}
			conexion = null;
			
			if("COM.AMERICACG.CARGAVIRTUAL.GATEWAY.PAGOELECTRONICO.EXCEPTIONS.TIMEOUTRWEXCEPTION".equals(e.getClass().getName().toUpperCase())){

				LogACGHelper.escribirLog(this.getIdProceso(),
						"Funcion [" + this.getClass().getSimpleName() + "] enviarTrama(): |" + e.getMessage() + "|");
				throw new TimeoutRWException("No se pudo establecer la comunicacion (GPE-TOC)");
			}else {
				LogACGHelper.escribirLog(this.getIdProceso(),
						"Funcion [" + this.getClass().getSimpleName() + "] enviarTrama(): |" + e.getMessage() + "|");
				throw new TimeoutRWException("No se pudo establecer la comunicacion (GPE-HNC)");
			}
		}
		

		try {

			LogACGHelper.escribirLog(this.getIdProceso(), "Fcn: |" + this.getClass().getSimpleName()
					+ "| Conectado contra la IP: |" + ip + "| Puerto: |" + puerto + "|");

			LogACGHelper.escribirLog(this.getIdProceso(),
					"Fcn: |" + this.getClass().getSimpleName() + "| Creo Stream de salida");

			// dataToServer = new PrintWriter(new
			// OutputStreamWriter(conexion.getOutputStream()));

			dataToServer = new BufferedWriter(new OutputStreamWriter(conexion.getOutputStream(), "UTF-8"));

			dataFromServer = new BufferedReader(new InputStreamReader(conexion.getInputStream(), "ISO-8859-1"));

			LogACGHelper.escribirLog(this.getIdProceso(),
					"Fcn: |" + this.getClass().getSimpleName() + "| Escribo en socket");

			LogACGHelper.escribirLog(this.getIdProceso(),
					"Fcn: |" + this.getClass().getSimpleName() + "| message a enviar:|" + new String(peticion) + "|");

			dataToServer.write(peticion);
			dataToServer.write("\r\n");

			dataToServer.flush();

			respuesta = "";
			LogACGHelper.escribirLog(this.getIdProceso(), "Fcn: |" + this.getClass().getSimpleName()
					+ "| Lectura inicial desde el servidor de PAGOS ELECTRONICOS |");
			// ch = dataFromServer.read();

			oSB = new StringBuilder();

			String serverResponse = null;
			while ((serverResponse = dataFromServer.readLine()) != null)
				oSB.append(serverResponse);

			// Cierro conexion
			LogACGHelper.escribirLog(this.getIdProceso(),
					"Fcn: |" + this.getClass().getSimpleName() + "| Cerrar conexion");
			conexion.close();

			LogACGHelper.escribirLog(this.getIdProceso(), "Rta Socket: |" + oSB.toString() + "|");

			return oSB.toString();

		} catch (SocketTimeoutException st) {
			LogACGHelper.escribirLog(this.getIdProceso(),
					"Funcion [" + this.getClass().getSimpleName() + "] enviarTrama(): |" + st.getMessage() + "|");
			throw new TimeoutRWException("No se pudo establecer la comunicacion (GPE-TRW)");
		} catch (Exception e) {
			LogACGHelper.escribirLog(this.getIdProceso(),
					"Funcion [" + this.getClass().getSimpleName() + "] enviarTrama(): |" + e.getMessage() + "|");

			throw new SendRequestException("Error en envio/recepcion trama PAGOS ELECTRONICOS.");
		}

	}
	
	public String obtenerHeader() {
		StringBuilder oSB = new StringBuilder();

		if(this.headerIn != null) {
			if (this.headerIn.getHashSecurity() != null)
				oSB.append(this.headerIn.getHashSecurity());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getProveedor() != null)
				oSB.append(this.headerIn.getProveedor());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getVersion() != null)
				oSB.append(this.headerIn.getVersion());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getCanal() != null)
				oSB.append(this.headerIn.getCanal());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());
			
			if (this.headerIn.getOrigen() != null)
				oSB.append(this.headerIn.getOrigen());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());
			
			if (this.headerIn.getFuncion() != null)
				oSB.append(this.headerIn.getFuncion());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getMarcaPOS() != null)
				oSB.append(this.headerIn.getMarcaPOS());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getNroSeriePos() != null)
				oSB.append(this.headerIn.getNroSeriePos());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getImeiPOS() != null)
				oSB.append(this.headerIn.getImeiPOS());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getImeiSIM() != null)
				oSB.append(this.headerIn.getImeiSIM());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getMedioConexion() != null)
				oSB.append(this.headerIn.getMedioConexion());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getNroTelefonoDelSIM() != null)
				oSB.append(this.headerIn.getNroTelefonoDelSIM());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getModelo() != null)
				oSB.append(this.headerIn.getModelo());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getIdMayorista() != null)
				oSB.append(this.headerIn.getIdMayorista());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getIdCliente() != null)
				oSB.append(this.headerIn.getIdCliente());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getIdUsuario() != null)
				oSB.append(this.headerIn.getIdUsuario());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getUsuario() != null)
				oSB.append(this.headerIn.getUsuario());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getClave() != null)
				oSB.append(this.headerIn.getClave());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getIdConfiguracionComercio() != null)
				oSB.append(this.headerIn.getIdConfiguracionComercio());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getFechaCliente() != null) {
				SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHmmss");
				oSB.append(formato.format(this.headerIn.getFechaCliente()));
			}else {
				oSB.append("");
			}
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getIdTransaccionCliente() != null)
				oSB.append(this.headerIn.getIdTransaccionCliente());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getTipoCliente() != null)
				oSB.append(this.headerIn.getTipoCliente());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getVersionPOS() != null)
				oSB.append(this.headerIn.getVersionPOS());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.headerIn.getTokenAuth() != null)
				oSB.append(this.headerIn.getTokenAuth());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());
			
		}
		
		oSB.append((char) this.getSeparadorTrama().getSeparadorHeaderData());
		
		return oSB.toString();
	}
	
	public String generarTramaConHash(String trama) {

		// ----------------------------------------------------------------------------------------------

		// GENERO HASH DE LA TRAMA
		//String claveParaHash = "2x6yPjAVMNktQ6ZS";
		String claveParaHash = "5y3fKMghPnAtW9Xv";

		String tramaih = trama.concat(claveParaHash);
		System.out.println("Calcular el Hash de: |" + tramaih + "|");

		// Calculo el hash en hexa
		MessageDigest md;
		byte[] buffer, digest;
		String hash = "";

		buffer = tramaih.getBytes();
		try {
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
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Hash Generado en Hexa: |" + hash + "|");

		StringBuilder oSB2 = new StringBuilder();
		oSB2.append((char) 2);
		oSB2.append(".");
		oSB2.append((char) 4);
		trama = trama.replace(
				oSB2.toString(),
				Character.toString((char) 2).concat(hash.toUpperCase())
						.concat(Character.toString((char) 4)));
		
		try {
			return CryptoHelper.encrypt(trama);
			//return oSB.toString();
		} catch (Exception e) {
			LogACGHelper.escribirLog(this.getIdProceso(), "No se pudo encriptar la trama. IdTransaccionCliente: |"
					+ this.getHeaderIn().getIdTransaccionCliente() + "|");

			return "";
		}
		
	}
	
	public abstract String obtenerTrama();
	
	public abstract MensajeOutboundGateway ejecutar();

	public String getCurrentLocalDateTimeStamp() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
	}

	public DataInputFcnGateway getDataIn() {
		return dataIn;
	}

	public void setDataIn(DataInputFcnGateway dataIn) {
		this.dataIn = dataIn;
	}

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public Long getIdMayorista() {
		return idMayorista;
	}

	public void setIdMayorista(Long idMayorista) {
		this.idMayorista = idMayorista;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

}
