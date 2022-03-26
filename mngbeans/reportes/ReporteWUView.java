package com.americacg.cargavirtual.web.mngbeans.reportes;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import org.primefaces.PrimeFaces;
import org.primefaces.event.ToggleEvent;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gestion.model.DatosPlataforma;
import com.americacg.cargavirtual.gestion.model.Descripcion;
import com.americacg.cargavirtual.gestion.model.DescripcionContainer;
import com.americacg.cargavirtual.gestion.model.ListaDeStrings;
import com.americacg.cargavirtual.wu.model.Lote;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;
import com.americacg.cargavirtual.wu.service.EstadoLote;
import com.americacg.cargavirtual.wu.service.EstadoTransaccion;
import com.americacg.cargavirtual.wu.service.Transaccion;

@Named("reporteWUView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteWUView extends ReporteGeneral<Lote> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6702562250837864913L;

	// idUsuario es el id del usuario logueado, que se lo pase desde el faces.xml
	private Long idUsuario;

	private Long idEstadoACG;
	private List<SelectItem> wu_estados;

	private String estado_lote;
	private String estado_trx;
	private Long idLoteWU;
	private Long idTrxWU;

	private Integer tipoFiltroCliente = 3;
	private Long idClienteFiltro;

	private String codBarra1;
	private String codBarra2;
	private String descProducto;

	private List<Transaccion> ltr = new ArrayList<Transaccion>();
	private Integer cantFacturas = 0;
	private BigDecimal sumFacturas = BigDecimal.ZERO;

	private Integer cantLotes = 0;
	private BigDecimal sumLoteConfirmados = BigDecimal.ZERO;
	private Integer cantLotesConFacturasAceptadas = 0;
	private Integer cantFacturasAceptadas = 0;
	private BigDecimal sumLoteRechazadas = BigDecimal.ZERO;
	private BigDecimal sumLoteAnuladas = BigDecimal.ZERO;

	private String idAgente;
	private String idLocacion;
	private String caja;
	private String operador;

	//private Long idFacturaParaTicket;
	//private Long idClienteParaTicket;
	private String ticket;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();

		// Limpio las Listas
		this.ltr.clear();
		this.cantFacturas = 0;
		this.sumFacturas = BigDecimal.ZERO;

		this.list = null;
		this.cantLotes = 0;
		this.sumLoteConfirmados = BigDecimal.ZERO;
		this.cantLotesConFacturasAceptadas = 0;
		this.cantFacturasAceptadas = 0;
		this.sumLoteRechazadas = BigDecimal.ZERO;
		this.sumLoteAnuladas = BigDecimal.ZERO;

		// Reseteo el resto de las variables
		this.estado_lote = null;
		this.estado_trx = null;
		this.idLoteWU = null;
		this.idTrxWU = null;
		this.tipoFiltroCliente = 3;
		this.idClienteFiltro = null;
		this.codBarra1 = null;
		this.codBarra2 = null;
		this.descProducto = null;

		this.idAgente = null;
		this.idLocacion = null;
		this.caja = null;
		this.operador = null;

		//this.idFacturaParaTicket = null;
		//this.idClienteParaTicket = null;
		this.ticket = null;

		this.idEstadoACG = null;

		try {

			this.wu_estados = new ArrayList<SelectItem>();

			// La lista WU_estados, devuelve los estados de la tabla wu_estado
			// Estos estados son los de ACG
			DescripcionContainer dc = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarWUEstados(this.getUsuario().getIdMayorista(), null);

			for (Descripcion d : dc.getListDescripcion().getValue().getDescripcion()) {
				this.wu_estados.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
			}
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					e.setError("GST-TOC",
							"No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente.");
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					e.setError("GST-TRW",
							"No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente.");
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					e.setError("GST-TRW",
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.");
				} else {
					LogACGHelper.escribirLog(null,
							"Informe WU. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe WU. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe WU. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public void buscarFacturas(ToggleEvent te) {

		try {
			// Limpio la Lista de Facturas
			ltr.clear();
			cantFacturas = 0;
			sumFacturas = BigDecimal.ZERO;
			Long idLoteACG = null;

			// Busco los DatosPlataforma
			DatosPlataforma dp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarDatosPlataforma(this.getUsuario().getIdMayorista());

			if (dp == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se encontraron datosPlataforma", null));
				return;
			} else {

				String urlw = dp.getWuTipoConexionSinSSL().getValue() + "://" + dp.getWuServidorSinSSL().getValue()
						+ ":" + dp.getWuPuertoSinSSL().getValue().toString() + dp.getWuUrlSinSSL().getValue();

				com.americacg.cargavirtual.wu.service.CargaVirtualGatewayWU gatewayWU = null;

				URL url = new URL(com.americacg.cargavirtual.wu.service.CargaVirtualGatewayWU.class.getResource("."),
						urlw);

				gatewayWU = new com.americacg.cargavirtual.wu.service.CargaVirtualGatewayWU(url,
						new QName("http://service.wu.cargavirtual.americacg.com/", "CargaVirtualGatewayWU"));

				com.americacg.cargavirtual.wu.service.ICargaVirtualGatewayWU gsp;

				gsp = gatewayWU.getCargaVirtualGatewayWUPort();

				// SETEO EL TIME OUT DE CONEXION (ms)
				((BindingProvider) gsp).getRequestContext().put("javax.xml.ws.client.connectionTimeout", "6000");

				// SETEO EL TIME DE LECTURA (ms)
				((BindingProvider) gsp).getRequestContext().put("javax.xml.ws.client.receiveTimeout", "120000");

				// Configuro el Parametro set-jaxb-validation-event-handler para que no pinche
				// el WS si cambia la definicion del WSDL
				((BindingProvider) gsp).getRequestContext().put("set-jaxb-validation-event-handler", "false");

				com.americacg.cargavirtual.wu.message.solicitud.ConsultarTransaccionesACG st = new com.americacg.cargavirtual.wu.message.solicitud.ConsultarTransaccionesACG();

				st.setIdMayorista(this.getUsuario().getIdMayorista());
				st.setUsuario(this.getUsuario().getUsername());
				st.setPwd(this.getUsuario().getPassword());
				st.setIdAgente(null);
				st.setIdLocacion(null);
				st.setCaja(null);
				st.setOperador(null);
				st.setFecha(null);
				st.setFechaFrontEnd(null);
				st.setEmpresaFactura(null);
				st.setIdEstadoTrx(null);
				st.setCodBarra1(null);
				st.setCodBarra2(null);
				st.setLimit(null);

				// Cargo el numero de lote del registro de la grilla que quiero ver
				idLoteACG = ((Lote)te.getData()).getIdLoteACG();
				st.setIdLoteACG(idLoteACG);

				// Invoco el Metodo de Consultar de Transacciones (Facturas)
				com.americacg.cargavirtual.wu.message.respuesta.ConsultarTransaccionesACG rt = gsp
						.consultarTransaccionesACG(st);

				if (rt == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"La consulta de Facturas devolvio null", null));

				} else if (!"W0000".equals(rt.getErrorRb().getCodigo())) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "La consulta de Lote devolvio Error", null));

				} else if (rt.getListaTransacciones().getTransaccion().size() <= 0) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se encontraron transacciones para el lote: " + idLoteACG, null));

				} else {
					// Cargo la lista de Facturas para mostrar en la grilla
					ltr = rt.getListaTransacciones().getTransaccion();
					cantFacturas = ltr.size();

					// Sumo el importe total de todas las facturas aprobadas para el lote en
					// cuestion
					for (Iterator iterator = ltr.iterator(); iterator.hasNext();) {
						Transaccion t = (Transaccion) iterator.next();

						if (EstadoTransaccion.fromValue("ACEPTADA") == t.getEstado()) {
							sumFacturas = t.getImporte().add(sumFacturas);
						}
					}
				}
			}
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GWU-TOC).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GWU-TRW).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GWU-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GWU-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null, "Informe WU Buscar Facturas. Error ejecutando el WS de consulta: |"
							+ ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe WU Buscar Facturas. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe WU Buscar Facturas. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de Buscar Facturas: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();

		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;
	}

	public void realizarInforme() {
		this.exportToExcel = false;
		informe();
	}

	public void exportarInforme() {
		this.exportToExcel = true;
		informe();
	}

	public void informe() {

		try {

			this.mostrarRegistros = true;

			// Limpio la lista de Lotes y la Lista de Facturas
			ltr.clear();
			list = null;
			cantLotes = 0;
			sumLoteConfirmados = BigDecimal.ZERO;
			cantLotesConFacturasAceptadas = 0;
			cantFacturasAceptadas = 0;
			sumLoteRechazadas = BigDecimal.ZERO;
			sumLoteAnuladas = BigDecimal.ZERO;
			cantFacturas = 0;
			sumFacturas = BigDecimal.ZERO;

			// Fecha Desde
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fechaHoraDesde);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			fechaHoraDesde = calendar.getTime();

			// Fecha Hasta
			calendar = Calendar.getInstance();
			calendar.setTime(fechaHoraHasta);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			fechaHoraHasta = calendar.getTime();

			if (!validaFechas()) {
				return;
			}

			// Busco los DatosPlataforma
			DatosPlataforma dp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarDatosPlataforma(this.getUsuario().getIdMayorista());

			if (dp == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se encontraron datosPlataforma", null));
				return;
			} else {

				// Para Crear las Clases de WU
				// wsimport -keep -Xnocompile
				// http://americacg.dyndns.org/CargaVirtualGatewayWU/services/CargaVirtualGatewayWU?wsdl

				String urlw = dp.getWuTipoConexionSinSSL().getValue() + "://" + dp.getWuServidorSinSSL().getValue()
						+ ":" + dp.getWuPuertoSinSSL().getValue().toString() + dp.getWuUrlSinSSL().getValue();

				com.americacg.cargavirtual.wu.service.CargaVirtualGatewayWU gatewayWU = null;

				URL url = new URL(com.americacg.cargavirtual.wu.service.CargaVirtualGatewayWU.class.getResource("."),
						urlw);

				gatewayWU = new com.americacg.cargavirtual.wu.service.CargaVirtualGatewayWU(url,
						new QName("http://service.wu.cargavirtual.americacg.com/", "CargaVirtualGatewayWU"));

				com.americacg.cargavirtual.wu.service.ICargaVirtualGatewayWU gsp;

				gsp = gatewayWU.getCargaVirtualGatewayWUPort();

				// SETEO EL TIME OUT DE CONEXION (ms)
				((BindingProvider) gsp).getRequestContext().put("javax.xml.ws.client.connectionTimeout", "6000");

				// SETEO EL TIME DE LECTURA (ms)
				((BindingProvider) gsp).getRequestContext().put("javax.xml.ws.client.receiveTimeout", "120000");

				// Configuro el Parametro set-jaxb-validation-event-handler para que no pinche
				// el WS si cambia la definicion del WSDL
				((BindingProvider) gsp).getRequestContext().put("set-jaxb-validation-event-handler", "false");

				com.americacg.cargavirtual.wu.message.solicitud.ConsultarLotesACG sl = new com.americacg.cargavirtual.wu.message.solicitud.ConsultarLotesACG();

				// Cargo los valores en la clase de peticion para la consulta de Lotes
				sl.setIdMayorista(this.getUsuario().getIdMayorista());
				sl.setUsuario(this.getUsuario().getUsername());
				sl.setPwd(this.getUsuario().getPassword());

				if (!"".equals(idAgente)) {
					sl.setIdAgente(idAgente);
				} else {
					sl.setIdAgente(null);
				}

				if (!"".equals(idLocacion)) {
					sl.setIdLocacion(idLocacion);
				} else {
					sl.setIdLocacion(null);
				}

				if (!"".equals(caja)) {
					sl.setCaja(caja);
				} else {
					sl.setCaja(null);
				}

				if (!"".equals(operador)) {
					sl.setOperador(operador);
				} else {
					sl.setOperador(null);
				}

				if (!"".equals(estado_trx)) {
					sl.setEstadoTransaccion(EstadoTransaccion.fromValue(estado_trx));
				} else {
					sl.setEstadoTransaccion(null);
				}

				if (!"".equals(estado_lote)) {
					sl.setEstadoLote(EstadoLote.fromValue(estado_lote));
				} else {
					sl.setEstadoLote(null);
				}

				if (idEstadoACG != null && idEstadoACG > 0) {
					sl.setIdEstadoLote(idEstadoACG);
				}

				// idLoteACG y idFronEnd, mandarlos siempre con estos valores (cero y null)
				sl.setIdLoteACG(0);
				sl.setIdFrontEnd("");

				if (idLoteWU == null || idLoteWU <= 0) {
					sl.setIdLote(0L);
				} else {
					sl.setIdLote(idLoteWU);
				}

				if (idTrxWU != null && idTrxWU == 0) {
					sl.setIdTransaccion(null);
				} else {
					sl.setIdTransaccion(idTrxWU);
				}

				if (fechaHoraDesde != null) {
					GregorianCalendar gcd = new GregorianCalendar();
					gcd.setTimeInMillis(fechaHoraDesde.getTime());
					DatatypeFactory dfd = DatatypeFactory.newInstance();
					sl.setFechaDde(dfd.newXMLGregorianCalendar(gcd));
				}

				if (fechaHoraHasta != null) {
					GregorianCalendar gch = new GregorianCalendar();
					gch.setTimeInMillis(fechaHoraHasta.getTime());
					DatatypeFactory dfh = DatatypeFactory.newInstance();
					sl.setFechaHta(dfh.newXMLGregorianCalendar(gch));
				}

				if (!"".equals(descProducto)) {
					sl.setEmpresaFactura(descProducto);
				} else {
					sl.setEmpresaFactura(null);
				}

				if (!"".equals(codBarra1)) {
					sl.setCodBarra1(codBarra1);
				} else {
					sl.setCodBarra1(null);
				}

				if (!"".equals(codBarra2)) {
					sl.setCodBarra2(codBarra2);
				} else {
					sl.setCodBarra2(null);
				}

				// System.out.println("TipoCliente: |" + this.getUsuario().getTipoCliente() +
				// "|");

				// Prevaido las condificiones de filtro por cliente antes de Buscar la lista de
				// Clientes a consultar
				if ("P".equals(this.getUsuario().getTipoCliente())) {
					tipoFiltroCliente = 1;
					idClienteFiltro = this.getUsuario().getIdCliente();

				} else if (tipoFiltroCliente == null) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "El tipoFiltroCliente es null", null));
					return;

				} else if (tipoFiltroCliente == 3) {
					if (idClienteFiltro != null) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"El cliente del filtro debe ser vacio", null));
						return;
					} else {
						// TODO: ver si es correcto esto
						// idClienteFiltro = idCliente;
						idClienteFiltro = this.getUsuario().getIdCliente();
					}

				} else if ((tipoFiltroCliente == 1) || (tipoFiltroCliente == 2)) {
					if ((idClienteFiltro == null) || (idClienteFiltro <= 0)) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Debe cargar un cliente para filtrar", null));
						return;
					}

				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"El tipoCliente de Filtro no es valido", null));
					return;
				}

				// Buscar la lista de Clientes a consultar

				// tipoFiltroCliente
				// 1 = Valida que el idClienteLogueado sea igual al idCliente del Filtro (Se usa
				// para los Puntos de Venta)
				// 2 = Todos los subclientes del filtro
				// 3 = Todos los subclientes del cliente Loguado

				ListaDeStrings ls = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).listaDeSubClientes(
						this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), tipoFiltroCliente,
						idClienteFiltro, false);

				// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
				// TODO implementar exportToCSV
				// this.exportToCSV.setExportText("");
				// this.exportToCSV.setFileName("");
				mostrarArchivoCSV = false;

				String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
				String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

				StringBuilder sb = new StringBuilder();
				// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
				SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				if (ls == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se encontraron subclientes para consultar. La respuesta de la consulta fue null",
							null));
				} else if (ls.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							ls.getError().getValue().getMsgError().getValue(), null));
				} else if ((ls.getL() == null) || (ls.getL().getValue().getString().size() <= 0)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se encontraron subclientes para consultar", null));
				} else {

					// Asigno la lista de SubCliente a la clase ConsultarLotesACG
					sl.getListaIdCliente().addAll(ls.getL().getValue().getString());

					// Llamado al metodo de Consulta de Lote
					com.americacg.cargavirtual.wu.message.respuesta.ConsultarLotesACG cl = gsp.consultarLotesACG(sl);

					// Analizo la Respuesta
					if (cl.getListaLotes() == null) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No se encontraron lotes con el criterio de busqueda seleccionado", null));

					} else if (!"W0000".equals(cl.getErrorRb().getCodigo())) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"La Consulta de Lote devolvio Error: " + cl.getErrorRb().getCodigo(), null));

					} else if ((cl.getListaLotes().getLote() == null) || (cl.getListaLotes().getLote().size() <= 0)) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No se encontraron registros con el criterio de Busqueda Especificado", null));

					} else {
						// Asigno la Lista de Lotes a la clase list para poder mostrarla en la grilla
						list = cl.getListaLotes().getLote();
						cantLotes = list.size();

						// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {
							// Header
							sb.append((char) 34).append("Informe WU").append((char) 34).append(csvSepCamp);
							sb.append((char) 13).append((char) 10);

							sb.append((char) 34).append("ID Lote ACG").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Lote WU").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Usuario").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Usuario").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Importe por Lote").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Estado ACG").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Estado ACG").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Estado WU").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Agente").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Location").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Caja").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Operador").append((char) 34).append(csvSepCamp);

							sb.append((char) 13).append((char) 10);

						}

						for (Lote l : list) {

							if (l.getTotalesConfirmadas().getTotal().get(0).getImporte() != null) {
								sumLoteConfirmados = sumLoteConfirmados
										.add(l.getTotalesConfirmadas().getTotal().get(0).getImporte());

								if (l.getEstado() != null && (l.getEstado().compareTo(EstadoLote.CONFIRMADO) == 0
										|| l.getEstado().compareTo(EstadoLote.CONFIRMADO_CON_RECHAZOS) == 0)) {

									cantLotesConFacturasAceptadas++;

								}

								if (l.getCantidadConfirmadas() != null && l.getCantidadConfirmadas() > 0) {

									cantFacturasAceptadas = cantFacturasAceptadas + l.getCantidadConfirmadas();

								}

							}

							if (l.getTotalesRechazadas().getTotal().get(0).getImporte() != null) {
								sumLoteRechazadas = sumLoteRechazadas
										.add(l.getTotalesRechazadas().getTotal().get(0).getImporte());
							}

							if (l.getTotalesAnuladas().getTotal().get(0).getImporte() != null) {
								sumLoteAnuladas = sumLoteAnuladas
										.add(l.getTotalesAnuladas().getTotal().get(0).getImporte());
							}

							if (this.exportToExcel) {
								sb.append((char) 34).append(l.getIdLoteACG()).append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(l.getIdLote()).append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(ff.format(l.getFechaAlta()).toString()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(l.getIdCliente()).append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(l.getRazonSocial()).append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(l.getIdUsuario()).append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(l.getOperador()).append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("$")
										.append(ACGFormatHelper.format(
												l.getTotalesConfirmadas().getTotal().get(0).getImporte().floatValue(),
												csvSepDec))
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(l.getIdEstado()).append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(l.getDescripcionEstadoACG()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(l.getEstado()).append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(l.getIdAgente()).append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(l.getIdLocacion()).append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(l.getCaja()).append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(l.getOperador()).append((char) 34).append(csvSepCamp);
								sb.append((char) 13).append((char) 10);
							}
						}

						// GENERO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {

							FacesContext fc = FacesContext.getCurrentInstance();
							ExternalContext ec = fc.getExternalContext();

							ec.responseReset();
							ec.setResponseContentType("text/plain");
							ec.setResponseContentLength(sb.toString().length());

							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
							ec.setResponseHeader("Content-Disposition",
									"attachment; filename=\"" + sdf.format(new Date()) + "_("
											+ this.getUsuario().getIdMayorista() + ")_" + "InformeWU.csv" + "\"");

							OutputStream os = ec.getResponseOutputStream();
							OutputStreamWriter osw = new OutputStreamWriter(os);
							PrintWriter writer = new PrintWriter(osw);
							writer.write(sb.toString());
							writer.flush();
							writer.close();
							sb.setLength(0);

							fc.responseComplete();

						}

						PrimeFaces.current().executeScript("PF('panelFiltroWG').toggle();");
					}
				}
			}
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GWU-TOC).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GWU-TRW).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GWU-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GWU-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null,
							"Informe WU. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe WU. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe WU. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de Informe WU: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;
	}

	public void imprimirTicket(Long idClienteParaTicket, Long idFacturaParaTicket) {
		try {
			ticket = null;

			// Busco los DatosPlataforma
			DatosPlataforma dp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarDatosPlataforma(this.getUsuario().getIdMayorista());

			if (dp == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se encontraron datosPlataforma", null));
				return;
			} else {

				String urlw = dp.getWuTipoConexionSinSSL().getValue() + "://" + dp.getWuServidorSinSSL().getValue() + ":"
						+ dp.getWuPuertoSinSSL().getValue().toString() + dp.getWuUrlSinSSL().getValue();

				com.americacg.cargavirtual.wu.service.CargaVirtualGatewayWU gatewayWU = null;

				URL url = new URL(com.americacg.cargavirtual.wu.service.CargaVirtualGatewayWU.class.getResource("."),
						urlw);

				gatewayWU = new com.americacg.cargavirtual.wu.service.CargaVirtualGatewayWU(url,
						new QName("http://service.wu.cargavirtual.americacg.com/", "CargaVirtualGatewayWU"));

				com.americacg.cargavirtual.wu.service.ICargaVirtualGatewayWU gsp;

				gsp = gatewayWU.getCargaVirtualGatewayWUPort();

				// Creo la clase para enviar al metodo de solicitud de Ticket
				com.americacg.cargavirtual.wu.message.solicitud.ReimprimirTransaccion reimpTrx = new com.americacg.cargavirtual.wu.message.solicitud.ReimprimirTransaccion();

				reimpTrx.setIdMayorista(this.getUsuario().getIdMayorista());
				reimpTrx.setIdCliente(0);
				reimpTrx.setIdUsuario(0);

				reimpTrx.setUsuario(this.getUsuario().getUsername());
				reimpTrx.setPwd(this.getUsuario().getPassword());

				reimpTrx.setCaja(null);
				reimpTrx.setOperador(null);

				reimpTrx.setIdLoteACG(0);

				reimpTrx.setIdCliente(idClienteParaTicket);

				// parametro que indica que debe obtener la data de la BBDD de ACG
				reimpTrx.setOrigenACG(true);

				com.americacg.cargavirtual.wu.message.solicitud.ReimprimirTransaccion.ListaTransacciones oLst = new com.americacg.cargavirtual.wu.message.solicitud.ReimprimirTransaccion.ListaTransacciones();

				// Genero una transaccion y le asigno el numero de factura que quiero imprimir
				Transaccion t = new Transaccion();
				t.setIdTransaccion(idFacturaParaTicket);

				// A la clase de peticion de reimpresion le agrego la clase Transaccion que
				// quiero imprimir
				oLst.getTransaccion().add(t);
				reimpTrx.setListaTransacciones(oLst);

				// SETEO EL TIME OUT DE CONEXION (ms)
				((BindingProvider) gsp).getRequestContext().put("javax.xml.ws.client.connectionTimeout", "6000");

				// SETEO EL TIME DE LECTURA (ms)
				((BindingProvider) gsp).getRequestContext().put("javax.xml.ws.client.receiveTimeout", "120000");

				// Configuro el Parametro set-jaxb-validation-event-handler para que no pinche
				// el WS si cambia la definicion del WSDL
				((BindingProvider) gsp).getRequestContext().put("set-jaxb-validation-event-handler", "false");

				com.americacg.cargavirtual.wu.message.respuesta.ReimprimirTransaccion reimpTrxRta;

				// Invoco el Metodo de Reimpresion de ticket
				reimpTrxRta = gsp.reimprimirTransaccion(reimpTrx);

				// Analizo la Respuesta
				if (reimpTrxRta == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"El resultado de la consulta de Tickets es null", null));

				} else if (!"W0000".equals(reimpTrxRta.getErrorRb().getCodigo())) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"La Consulta de Ticket devolvio Error: " + reimpTrxRta.getErrorRb().getMensaje(), null));

				} else {
					if (reimpTrxRta.getLote().getCantidadAnuladas() != null
							&& reimpTrxRta.getLote().getCantidadAnuladas() > 0) {
						ticket = reimpTrxRta.getLote().getAnuladas().getTransaccion().get(0).getComprobante();
					}

					if (reimpTrxRta.getLote().getCantidadConfirmadas() != null
							&& reimpTrxRta.getLote().getCantidadConfirmadas() > 0) {
						ticket = reimpTrxRta.getLote().getConfirmadas().getTransaccion().get(0).getComprobante();
					}

					if (reimpTrxRta.getLote().getCantidadRechazadas() != null
							&& reimpTrxRta.getLote().getCantidadRechazadas() > 0) {
						ticket = reimpTrxRta.getLote().getRechazadas().getTransaccion().get(0).getComprobante();
					}

					// Hago trim de ticket
					if (ticket != null) {
						ticket = ticket.trim();
						mostrarTicket();
					}

				}
			}

		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GWU-TOC).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GWU-TRW).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GWU-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GWU-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null,
							"Informe WU Buscar Ticket. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe WU Buscar Ticket. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe WU Buscar Ticket. Error ejecutando el metodo buscarTicket: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el metodo buscarTicket", null));
		}

		//this.idFacturaParaTicket = idFacturaParaTicket;
	}
	
	public void mostrarTicket() {
		String nombreTicketCompleto = "";

		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", false);
		options.put("closable", true);
		options.put("contentWidth", this.getUsuario().getAnchoTicket() + 45);
		options.put("position", "center center");
		options.put("includeViewParams", true);
		options.put("fitViewport", true);

		List<String> paramList = new ArrayList<String>();
		// paramList.add("ENTIDAD");
		Map<String, List<String>> paramMap = new HashMap<String, List<String>>();
		// paramMap.put("ORIGEN", paramList);

		nombreTicketCompleto = "/secure/shared/tickets/ticketWU.xhtml";

		// --- Abre el dialogo para buscar articulos
		PrimeFaces.current().dialog().openDynamic(nombreTicketCompleto, options, paramMap);
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
	}
	
	public String getEstado_lote() {
		return estado_lote;
	}

	public void setEstado_lote(String estado_lote) {
		this.estado_lote = estado_lote;
	}

	public Long getIdLoteWU() {
		return idLoteWU;
	}

	public void setIdLoteWU(Long idLoteWU) {
		this.idLoteWU = idLoteWU;
	}

	public Long getIdTrxWU() {
		return idTrxWU;
	}

	public void setIdTrxWU(Long idTrxWU) {
		this.idTrxWU = idTrxWU;
	}

	public String getEstado_trx() {
		return estado_trx;
	}

	public void setEstado_trx(String estado_trx) {
		this.estado_trx = estado_trx;
	}

	public Integer getTipoFiltroCliente() {
		return tipoFiltroCliente;
	}

	public void setTipoFiltroCliente(Integer tipoFiltroCliente) {
		this.tipoFiltroCliente = tipoFiltroCliente;
	}

	public String getCodBarra1() {
		return codBarra1;
	}

	public void setCodBarra1(String codBarra1) {
		this.codBarra1 = codBarra1;
	}

	public String getCodBarra2() {
		return codBarra2;
	}

	public void setCodBarra2(String codBarra2) {
		this.codBarra2 = codBarra2;
	}

	public String getDescProducto() {
		return descProducto;
	}

	public void setDescProducto(String descProducto) {
		this.descProducto = descProducto;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public List<Transaccion> getLtr() {
		return ltr;
	}

	public void setLtr(List<Transaccion> ltr) {
		this.ltr = ltr;
	}

	public String getIdAgente() {
		return idAgente;
	}

	public void setIdAgente(String idAgente) {
		this.idAgente = idAgente;
	}

	public String getIdLocacion() {
		return idLocacion;
	}

	public void setIdLocacion(String idLocacion) {
		this.idLocacion = idLocacion;
	}

	public String getCaja() {
		return caja;
	}

	public void setCaja(String caja) {
		this.caja = caja;
	}

	public String getOperador() {
		return operador;
	}

	public void setOperador(String operador) {
		this.operador = operador;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Integer getCantFacturas() {
		return cantFacturas;
	}

	public void setCantFacturas(Integer cantFacturas) {
		this.cantFacturas = cantFacturas;
	}

	public Integer getCantLotes() {
		return cantLotes;
	}

	public void setCantLotes(Integer cantLotes) {
		this.cantLotes = cantLotes;
	}

	public BigDecimal getSumFacturas() {
		return sumFacturas;
	}

	public void setSumFacturas(BigDecimal sumFacturas) {
		this.sumFacturas = sumFacturas;
	}

	public BigDecimal getSumLoteRechazadas() {
		return sumLoteRechazadas;
	}

	public void setSumLoteRechazadas(BigDecimal sumLoteRechazadas) {
		this.sumLoteRechazadas = sumLoteRechazadas;
	}

	public BigDecimal getSumLoteAnuladas() {
		return sumLoteAnuladas;
	}

	public void setSumLoteAnuladas(BigDecimal sumLoteAnuladas) {
		this.sumLoteAnuladas = sumLoteAnuladas;
	}

	public BigDecimal getSumLoteConfirmados() {
		return sumLoteConfirmados;
	}

	public void setSumLoteConfirmados(BigDecimal sumLoteConfirmados) {
		this.sumLoteConfirmados = sumLoteConfirmados;
	}

	public Long getIdClienteFiltro() {
		return idClienteFiltro;
	}

	public void setIdClienteFiltro(Long idClienteFiltro) {
		this.idClienteFiltro = idClienteFiltro;
	}

	public List<SelectItem> getWu_estados() {
		return wu_estados;
	}

	public void setWu_estados(List<SelectItem> wu_estados) {
		this.wu_estados = wu_estados;
	}

	public Long getIdEstadoACG() {
		return idEstadoACG;
	}

	public void setIdEstadoACG(Long idEstadoACG) {
		this.idEstadoACG = idEstadoACG;
	}

	public Integer getCantFacturasAceptadas() {
		return cantFacturasAceptadas;
	}

	public void setCantFacturasAceptadas(Integer cantFacturasAceptadas) {
		this.cantFacturasAceptadas = cantFacturasAceptadas;
	}

	public Integer getCantLotesConFacturasAceptadas() {
		return cantLotesConFacturasAceptadas;
	}

	public void setCantLotesConFacturasAceptadas(Integer cantLotesConFacturasAceptadas) {
		this.cantLotesConFacturasAceptadas = cantLotesConFacturasAceptadas;
	}
}
