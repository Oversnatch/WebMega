package com.americacg.cargavirtual.web.mngbeans.reportes;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gestion.model.Descripcion;
import com.americacg.cargavirtual.gestion.model.DescripcionContainer;
import com.americacg.cargavirtual.gestion.model.InformeSubeTernas;
import com.americacg.cargavirtual.gestion.model.ObjectFactory;
import com.americacg.cargavirtual.gestion.model.ParametrosList;
import com.americacg.cargavirtual.gestion.model.ResultadoBase;
import com.americacg.cargavirtual.gestion.model.SubeListaModelosLG;
import com.americacg.cargavirtual.gestion.model.SubeModeloLG;
import com.americacg.cargavirtual.gestion.model.SubeTerna;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;
import com.americacg.cargavirtual.web.shared.AutoCompleteClienteView;

@Named("reporteSubeLgsView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteSubeLgsView extends ReporteGeneral<SubeTerna> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6478662311450427542L;
	private List<SelectItem> proveedoresLg;
	private Long idProveedorLg;

	private List<SelectItem> modelosLg;
	private Long idModeloLg;

	private List<SelectItem> idsEstadosTerna;
	private Long idEstadoTerna;

	private List<SelectItem> idsUbicacionesLg;
	private Long idUbicacionLg;

	private List<SelectItem> comandosRemotosLG;

	private Long idTerna;
	private String lgSerie;
	private String samId;
	private String samPin;
	private String posId;
	private String idExterno;
	private String lgCommPort;
	private Float limiteCredito;
	private Float valorPurse;
	private Float samSaldo;
	private String idUbicacionNacion;
	private Integer cantMaxTrxsPorTurno;
	private Float importeMaxTrxsPorTurno;
	private String datosAdicionales;
	private Long idClienteFiltro;
	// private String imeiADesasociar;
	// private String marcaPOSaDesasociar;

	private AutoCompleteClienteView autoCompleteCliente = new AutoCompleteClienteView();

	private List<SelectItem> m_idsEstadosTerna;

	private List<SelectItem> m_modelosLg;

	private Boolean historialLGS;
	private String leyendaAdicionalEncabezado;

	private Long id_accion;
	private List<SelectItem> acciones;

	private Long idUbicacion;
	private List<SelectItem> ubicacionesPOS;

	private Long idUbicacionTermMod;

	private String imeiSIM;

	private Boolean agrJerarquia;

	private Float sumaDeSaldoDeSams;
	private Float sumaDeLimiteDeCreditoDeLGS;

	private Boolean permitirModificarComandoRemoto;

	private Long idDistribuidorRed;
	private List<SelectItem> listDistribuidorRed;

	private SubeTerna subeTernaSeleccionado = null;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();

		this.list = null;
		this.idClienteFiltro = null;
		this.cantRegistros = 0;
		this.historialLGS = false;
		this.leyendaAdicionalEncabezado = "";
		this.id_accion = null;
		this.idUbicacion = null;
		this.imeiSIM = null;
		this.agrJerarquia = null;

		this.idProveedorLg = null;
		this.idModeloLg = null;

		this.idEstadoTerna = null;
		this.idUbicacionLg = null;

		this.idTerna = null;
		this.lgSerie = null;
		this.samId = null;
		this.samPin = null;
		this.posId = null;
		this.idExterno = null;
		this.lgCommPort = null;
		this.limiteCredito = null;
		this.valorPurse = null;
		this.samSaldo = null;
		this.idUbicacionNacion = null;
		this.cantMaxTrxsPorTurno = null;
		this.importeMaxTrxsPorTurno = null;
		this.datosAdicionales = null;

		this.sumaDeSaldoDeSams = 0F;
		this.sumaDeLimiteDeCreditoDeLGS = 0F;

		this.idDistribuidorRed = null;

		try {

			this.idsEstadosTerna = new ArrayList<SelectItem>();
			DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarEstadoDeTernaSube(this.getUsuario().getIdMayorista(), null, null);

			if (cd != null && cd.getListDescripcion() != null && cd.getListDescripcion().getValue() != null) {
				for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {

					if (d.getDescripcion() != null) {
						if (d.getDescripcion().getValue().length() > 0) {

							this.idsEstadosTerna
									.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
						}
					}

				}
			}
			this.acciones = new ArrayList<SelectItem>();
			DescripcionContainer a = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarAccionesParaHistorialDeTernasDeSube(this.getUsuario().getIdMayorista(), null);

			if (a != null && a.getListDescripcion() != null && a.getListDescripcion().getValue() != null) {
				for (Descripcion d : a.getListDescripcion().getValue().getDescripcion()) {
					if (d.getDescripcion() != null) {
						if (d.getDescripcion().getValue().length() > 0) {
							this.acciones.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
						}
					}
				}
			}

			this.ubicacionesPOS = new ArrayList<SelectItem>();
			DescripcionContainer u = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarTerminalesUbicacion(null, null);

			if (u != null && u.getListDescripcion() != null && u.getListDescripcion().getValue() != null) {
				for (Descripcion d : u.getListDescripcion().getValue().getDescripcion()) {
					if (d.getDescripcion() != null) {
						if (d.getDescripcion().getValue().length() > 0) {
							this.ubicacionesPOS
									.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
						}
					}
				}
			}

			this.proveedoresLg = new ArrayList<SelectItem>();
			DescripcionContainer p = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarProveedorLGdeSube(this.getUsuario().getIdMayorista(), null);

			if (p != null && p.getListDescripcion() != null && p.getListDescripcion().getValue() != null) {
				for (Descripcion d : p.getListDescripcion().getValue().getDescripcion()) {

					if (d.getDescripcion() != null) {
						if (d.getDescripcion().getValue().length() > 0) {
							this.proveedoresLg.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
						}
					}

				}
			}

			this.idsUbicacionesLg = new ArrayList<SelectItem>();
			DescripcionContainer ulg = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarUbicacionesDeTernaSube(this.getUsuario().getIdMayorista(), null);

			if (ulg != null && ulg.getListDescripcion() != null && ulg.getListDescripcion().getValue() != null) {
				for (Descripcion d : ulg.getListDescripcion().getValue().getDescripcion()) {

					if (d.getDescripcion() != null) {
						if (d.getDescripcion().getValue().length() > 0) {
							this.idsUbicacionesLg
									.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
						}
					}

				}
			}

			this.m_idsEstadosTerna = new ArrayList<SelectItem>();
			DescripcionContainer et = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarEstadoDeTernaSube(this.getUsuario().getIdMayorista(), null, 0);

			if (et != null && et.getListDescripcion() != null && et.getListDescripcion().getValue() != null) {
				for (Descripcion d : et.getListDescripcion().getValue().getDescripcion()) {

					if (d.getDescripcion() != null) {
						if (d.getDescripcion().getValue().length() > 0) {

							this.m_idsEstadosTerna
									.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
						}
					}

				}
			}

			this.comandosRemotosLG = new ArrayList<SelectItem>();
			DescripcionContainer cr = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarSubeComandosRemotos(this.getUsuario().getIdMayorista(), null);

			if (cr != null && cr.getListDescripcion() != null && cr.getListDescripcion().getValue() != null) {
				for (Descripcion d : cr.getListDescripcion().getValue().getDescripcion()) {

					if (d.getDescripcion() != null) {
						if (d.getDescripcion().getValue().length() > 0) {
							this.comandosRemotosLG
									.add(new SelectItem(d.getAlias().getValue(), d.getDescripcion().getValue()));
						}
					}

				}
			}

			this.listDistribuidorRed = new ArrayList<SelectItem>();
			DescripcionContainer dr = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarSubeDistribuidorRed(this.getUsuario().getIdMayorista());

			if (dr != null && dr.getListDescripcion() != null && dr.getListDescripcion().getValue() != null) {
				for (Descripcion d : dr.getListDescripcion().getValue().getDescripcion()) {

					if (d.getDescripcion() != null) {
						if (d.getDescripcion().getValue().length() > 0) {
							this.listDistribuidorRed
									.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
						}
					}

				}
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
							"Informe LGs SUBE. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe LGs SUBE. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe LGs SUBE. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteSubeLgsView() {
		super();
	}

	private void resetearFechas() {
		// Fecha Desde
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 00);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		this.fechaHoraDesde = calendar.getTime();

		// Fecha Hasta
		calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		this.fechaHoraHasta = calendar.getTime();

	}

	public void realizarInforme() {
		this.exportToExcel = false;
		informe();
	}

	public void exportarInforme() {
		this.exportToExcel = true;
		informe();
	}

	public void checkHistorialLGS(ActionEvent ae) {
		resetearFechas();
		this.list = null;
	}

	private void informe() {
		try {

			this.mostrarRegistros = true;
			cantRegistros = 0;

			this.permitirModificarComandoRemoto = false;

			this.sumaDeSaldoDeSams = 0F;
			this.sumaDeLimiteDeCreditoDeLGS = 0F;

			if (historialLGS) {
				leyendaAdicionalEncabezado = "historico de";
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
			} else {
				leyendaAdicionalEncabezado = "";
				// Fecha Desde
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.set(Calendar.HOUR_OF_DAY, 00);
				calendar.set(Calendar.MINUTE, 00);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				this.fechaHoraDesde = calendar.getTime();

				// Fecha Hasta
				calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				calendar.set(Calendar.MILLISECOND, 999);
				this.fechaHoraHasta = calendar.getTime();
			}

			// InformeStockTerminales ist =
			// GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).mostrarListadoStockTerminales(this.getUsuario().getIdMayorista(),
			// imei, marcaPOS, idClienteFiltro
			// , historialLGS, this.fechaHoraDesde, this.fechaHoraHasta, id_accion,
			// idUbicacion, imeiSIM);

			GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
			gcFechaHoraDesde.setTime(fechaHoraDesde);
			XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraDesde);

			GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
			gcFechaHoraHasta.setTime(fechaHoraHasta);
			XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraHasta);

			InformeSubeTernas ist = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).mostrarSubeTernas(
					this.getUsuario().getIdMayorista(), idTerna, lgSerie, samId, samPin, posId, idEstadoTerna,
					idClienteFiltro, idUbicacionNacion, idUbicacionLg, idProveedorLg, historialLGS, xmlGCFechaHoraDesde,
					xmlGCFechaHoraHasta, id_accion, null, null, null, null, null, null, idModeloLg, idExterno, null,
					null, null, this.getUsuario().getIdCliente(), idDistribuidorRed);

			// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
			// this.exportToCSV.setExportText("");
			// this.exportToCSV.setFileName("");
			mostrarArchivoCSV = false;

			String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
			String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

			StringBuilder sb = new StringBuilder();

			if (ist.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						ist.getError().getValue().getMsgError().getValue(), null));
				this.list = null;
			} else {
				this.list = ist.getListSubeTernas().getValue().getSubeTerna();

				if (this.list == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se ha encontrado ningun registro con ese criterio.", null));
					this.list = null;
				} else if (this.list.isEmpty()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se ha encontrado ningun registro con ese criterio.", null));
					this.list = null;
				} else {
					cantRegistros = this.list.size();

					// Verifico si este usuario puede modificar los comandos remotos

					ParametrosList p = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).mostrarParametros(
							this.getUsuario().getIdMayorista(), "S",
							"usuariosHabilitadosParaMOdifComandosRemotosDesdeLaWeb");

					if (p != null && p.getError().getValue().getHayError().getValue() == false
							&& p.getListParametros().getValue().getParametro().size() == 1) {
						String listaUsuariosHabilitados = p.getListParametros().getValue().getParametro().get(0)
								.getValor().getValue();

						String[] usuarios = listaUsuariosHabilitados.split(",", -1);

						for (int i = 0; i < usuarios.length; i++) {
							String usuario = usuarios[i];

							if (usuario != null && usuario.equals(this.getUsuario().getUsername())) {
								this.permitirModificarComandoRemoto = true;
							}
						}
					}

					if (cantRegistros > 0 && !this.historialLGS) {
						// RECORRO LA LISTA Y HAGO LAS SUMAS

						for (Iterator iterator = list.iterator(); iterator.hasNext();) {
							SubeTerna st = (SubeTerna) iterator.next();

							sumaDeSaldoDeSams = sumaDeSaldoDeSams + st.getSamSaldo().getValue();
							sumaDeLimiteDeCreditoDeLGS = sumaDeLimiteDeCreditoDeLGS + st.getLimiteCredito().getValue();
						}
					}

					Integer i = 0;

					// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
					if (this.exportToExcel) {
						// Header

						if (historialLGS) {
							sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
						}

						sb.append((char) 34).append("ID Terna").append((char) 34).append(csvSepCamp);

						sb.append((char) 34).append("ID Distribuidor Red").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Distribuidor Red").append((char) 34).append(csvSepCamp);

						sb.append((char) 34).append("ID Proveedor LG").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Proveedor LG").append((char) 34).append(csvSepCamp);

						sb.append((char) 34).append("ID Modelo LG").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Modelo LG").append((char) 34).append(csvSepCamp);

						sb.append((char) 34).append("Nro. Serie").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Sam ID").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Sam Pin").append((char) 34).append(csvSepCamp);

						sb.append((char) 34).append("POS ID").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Externo").append((char) 34).append(csvSepCamp);

						sb.append((char) 34).append("ID Ubicacion Nacion").append((char) 34).append(csvSepCamp);

						sb.append((char) 34).append("Identificacion Terminal SUBE").append((char) 34)
								.append(csvSepCamp);

						sb.append((char) 34).append("Atrib. Social Habilitado").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Carga Dif. Habilitada").append((char) 34).append(csvSepCamp);

						sb.append((char) 34).append("Loguear Carga de Tarjeta").append((char) 34).append(csvSepCamp);

						if (!historialLGS) {
							sb.append((char) 34).append("Limite Credito").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Valor Purse").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Max Trxs. por Turno").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Max Imp. por Turno").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Saldo del SAM").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Fecha Ult. Act. Saldo SAM").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Fecha Alta").append((char) 34).append(csvSepCamp);
						}

						sb.append((char) 34).append("ID Ubicacion").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Ubicacion").append((char) 34).append(csvSepCamp);

						sb.append((char) 34).append("ID Estado").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Estado").append((char) 34).append(csvSepCamp);

						if (!historialLGS) {
							sb.append((char) 34).append("Fecha Ult. Cambio Estado").append((char) 34)
									.append(csvSepCamp);
						}

						sb.append((char) 34).append("Asoc. a ID Cliente").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Asoc. a Cliente").append((char) 34).append(csvSepCamp);

						if (!historialLGS) {
							sb.append((char) 34).append("Fecha Ult. Asoc Cliente").append((char) 34).append(csvSepCamp);
						}

						if (agrJerarquia) {
							sb.append((char) 34).append("ID Distribuidor 1").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Distribuidor 1").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Distribuidor 2").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Distribuidor 2").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Distribuidor 3").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Distribuidor 3").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Distribuidor 4").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Distribuidor 4").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Distribuidor 5").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Distribuidor 5").append((char) 34).append(csvSepCamp);
						}

						sb.append((char) 34).append("ultima Version Firmware Informado").append((char) 34)
								.append(csvSepCamp);
						sb.append((char) 34).append("Fecha ult. actualizacion Firmware").append((char) 34)
								.append(csvSepCamp);
						sb.append((char) 34).append("ultima Version Iloader Informado").append((char) 34)
								.append(csvSepCamp);
						sb.append((char) 34).append("Fecha ult. actualizacion ActIloader").append((char) 34)
								.append(csvSepCamp);

						sb.append((char) 34).append("Datos Adicionales").append((char) 34).append(csvSepCamp);

						if (!historialLGS) {
							sb.append((char) 34).append("ID Cliente que dio de Alta").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Cliente que dio de Alta").append((char) 34).append(csvSepCamp);
						}

						if (historialLGS) {
							sb.append((char) 34).append("ID Accion Realizada").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Accion Realizada").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("ID Cliente Que Modifico").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Cliente Que Modifico").append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("ID Usuario Que Modifico").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Usuario Que Modifico").append((char) 34).append(csvSepCamp);
						}

						sb.append((char) 34).append("ID Distribuidor Red").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Distribuidor Red").append((char) 34).append(csvSepCamp);

						sb.append((char) 13).append((char) 10);
					}

					// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
					SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
					// EXPORTAR EN CASO DE SER NECESARIO
					for (SubeTerna t : list) {

						// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {

							if (i == 0) {
								mostrarArchivoCSV = true;
							}

							if (historialLGS) {
								sb.append((char) 34).append((t.getFechaHistorial() == null) ? ""
										: ff.format(t.getFechaHistorial().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append(t.getIdTerna().getValue()).append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("'").append(t.getIdDistribuidorRed().getValue())
									.append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("'").append(t.getDistribuidorRedDescripcion().getValue())
									.append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("'").append(t.getIdProveedorLg().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("'").append(t.getDescProveedorLg().getValue()).append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 34).append("'").append(t.getIdModeloLg().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("'").append(t.getDescModeloLG().getValue()).append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 34).append("'").append(t.getLgSerie().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("'").append(t.getSamId().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("'").append(t.getSamPin().getValue()).append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 34).append("'").append(t.getPosId().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("'").append(t.getIdExterno().getValue()).append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 34).append("'").append(t.getIdUbicacionNacion().getValue())
									.append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("'").append(t.getIdentificacionTerminalSUBE().getValue())
									.append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("'")
									.append(t.getAtribSocialHabilitar().getValue() == 1 ? "SI" : "NO").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("'")
									.append(t.getCargaDiferidaHabilitar().getValue() == 1 ? "SI" : "NO")
									.append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append("'").append(t.getLoguearCargaDeTarjetaEnBD().getValue() == 1
									? "SI (No Excluyente)"
									: (t.getLoguearCargaDeTarjetaEnBD().getValue() == 2 ? "SI (Obligatoria)" : "NO"))
									.append((char) 34).append(csvSepCamp);

							if (!historialLGS) {
								sb.append((char) 34).append(t.getLimiteCredito().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(t.getValorPurse().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(t.getCantMaxTrxsPorTurno().getValue()).append((char) 34)
										.append(csvSepCamp);

								sb.append((char) 34).append("$").append(
										ACGFormatHelper.format(t.getImporteMaxTrxsPorTurno().getValue(), csvSepDec))
										.append((char) 34).append(csvSepCamp);

								sb.append((char) 34).append("$")
										.append(ACGFormatHelper.format(t.getSamSaldo().getValue(), csvSepDec))
										.append((char) 34).append(csvSepCamp);

								sb.append((char) 34)
										.append((t.getFechaUltActSaldoSam() == null) ? ""
												: ff.format(t.getFechaUltActSaldoSam().toGregorianCalendar().getTime())
														.toString())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append((t.getFechaAltaTerna() == null) ? ""
										: ff.format(t.getFechaAltaTerna().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append(t.getIdUbicacionLg().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(t.getDescUbicacionLg().getValue()).append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 34).append(t.getIdEstadoTerna().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(t.getDescEstadoTerna().getValue()).append((char) 34)
									.append(csvSepCamp);

							if (!historialLGS) {
								sb.append((char) 34)
										.append((t.getFechaUltActIdEstado() == null) ? ""
												: ff.format(t.getFechaUltActIdEstado().toGregorianCalendar().getTime())
														.toString())
										.append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append(t.getIdCliente().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(t.getRazonSocialCliente().getValue()).append((char) 34)
									.append(csvSepCamp);

							if (!historialLGS) {
								sb.append((char) 34)
										.append((t.getFechaUltAsocCliente() == null) ? ""
												: ff.format(t.getFechaUltAsocCliente().toGregorianCalendar().getTime())
														.toString())
										.append((char) 34).append(csvSepCamp);
							}

							if (agrJerarquia) {
								sb.append((char) 34).append(
										(t.getIdDistribuidor1() == null || t.getIdDistribuidor1().getValue() == 0) ? ""
												: t.getIdDistribuidor1().getValue())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(t.getRazonSocialDist1().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(
										(t.getIdDistribuidor2() == null || t.getIdDistribuidor2().getValue() == 0) ? ""
												: t.getIdDistribuidor2().getValue())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(t.getRazonSocialDist2().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(
										(t.getIdDistribuidor3() == null || t.getIdDistribuidor3().getValue() == 0) ? ""
												: t.getIdDistribuidor3().getValue())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(t.getRazonSocialDist3().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(
										(t.getIdDistribuidor4() == null || t.getIdDistribuidor4().getValue() == 0) ? ""
												: t.getIdDistribuidor4().getValue())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(t.getRazonSocialDist4().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(
										(t.getIdDistribuidor5() == null || t.getIdDistribuidor5().getValue() == 0) ? ""
												: t.getIdDistribuidor5().getValue())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(t.getRazonSocialDist5().getValue()).append((char) 34)
										.append(csvSepCamp);
							}

							sb.append((char) 34).append(t.getUltVersFirmwareInformado().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append((t.getFechaUltActFirmware() == null) ? ""
									: ff.format(t.getFechaUltActFirmware().toGregorianCalendar().getTime()).toString())
									.append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append(t.getUltVersIloaderInformado().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append((t.getFechaUltActIloader() == null) ? ""
									: ff.format(t.getFechaUltActIloader().toGregorianCalendar().getTime()).toString())
									.append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append(t.getDatosAdicionales().getValue()).append((char) 34)
									.append(csvSepCamp);

							if (!historialLGS) {
								sb.append((char) 34).append(t.getIdClienteAlta().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(t.getDescClienteAlta().getValue()).append((char) 34)
										.append(csvSepCamp);
							}

							if (historialLGS) {
								sb.append((char) 34).append(t.getIdAccionHistorial().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(t.getDescAccionHistorial().getValue()).append((char) 34)
										.append(csvSepCamp);

								sb.append((char) 34).append(t.getIdClienteAltaModifBorrado().getValue())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(t.getRazonSocialClienteAltaModifBorrado().getValue())
										.append((char) 34).append(csvSepCamp);

								sb.append((char) 34).append(t.getIdUsuAltaModifBorrado().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(t.getUsuarioAltaModifBorrado().getValue()).append((char) 34)
										.append(csvSepCamp);

							}

							sb.append((char) 34).append(t.getIdDistribuidorRed().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(t.getDistribuidorRedDescripcion().getValue()).append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 13).append((char) 10);

						}
						i++;

					}

					// GENERO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
					if (this.exportToExcel) {

						FacesContext fc = FacesContext.getCurrentInstance();
						ExternalContext ec = fc.getExternalContext();

						ec.responseReset();
						ec.setResponseContentType("text/plain");
						ec.setResponseContentLength(sb.toString().length());

						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
						ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + sdf.format(new Date())
								+ "_(" + this.getUsuario().getIdMayorista() + ")_" + "InformeLGSdeSUBE.csv" + "\"");

						OutputStream os = ec.getResponseOutputStream();
						OutputStreamWriter osw = new OutputStreamWriter(os);
						PrintWriter writer = new PrintWriter(osw);
						writer.write(sb.toString());
						writer.flush();
						writer.close();
						sb.setLength(0);

						fc.responseComplete();
					}

				}
			}
		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null,
							"Informe LGs SUBE. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe LGs SUBE. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe LGs SUBE. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de Informe LGs SUBE: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
	}

	public void borrarTernaSube(Long idTernaAborrar) {

		try {

			ResultadoBase resp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).borrarTernaSube(
					this.getUsuario().getIdMayorista(), idTernaAborrar, this.getUsuario().getIdCliente(),
					this.getUsuario().getIdUsuario(), this.getUsuario().getPassword());

			if (resp == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La respuesta del metodo borrarTernaSube devolvio null", null));
			} else {

				if (resp.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							resp.getError().getValue().getMsgError().getValue(), null));
				} else {

					// Borrado OK
					this.list = null;
					this.exportToExcel = false;
					informe();

					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "LG borrado exitosamente", null));
					PrimeFaces.current().executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#9ed5a0', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/operacionOK.png')");
				}

			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error borrando el LG", null));
		}
	}

	public void asignarComandoRemoto(SelectEvent event) {

		try {

			Long idTernaParaComRem = (Long) event.getComponent().getAttributes().get("idTerna");
			String valorComandoRemotoAasignar = (String) event.getObject();

			ResultadoBase resp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.subeTernaAsignarComandoRemoto(this.getUsuario().getIdMayorista(), idTernaParaComRem,
							valorComandoRemotoAasignar);

			if (resp == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La respuesta del metodo subeTernaAsignarComandoRemoto devolvio null", null));
			} else {

				if (resp.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							resp.getError().getValue().getMsgError().getValue(), null));

				} else {

					// Asignado OK
					this.list = null;
					this.exportToExcel = false;
					informe();
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Comando remoto asignado exitosamente", null));
					PrimeFaces.current().executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#9ed5a0', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/operacionOK.png')");
				}

			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Asignando Comando Remoto al LG", null));
		}

	}

	public void modificarDatos() {

		try {

			Long idCliAsoc = null;

			// Verifico si Actualizo cliente
			if ((this.autoCompleteCliente.getId() == null)
					|| ("".equals(this.autoCompleteCliente.getId().toString()))) {
				// Desasociar cliente
				idCliAsoc = null;

			} else {
				// Asociar cliente

				idCliAsoc = this.autoCompleteCliente.getId();

			}

			// Hago la modificacion

			ResultadoBase resp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).modificarTernaSube(
					this.subeTernaSeleccionado.getIdTerna().getValue(),
					this.subeTernaSeleccionado.getIdMayorista().getValue(),
					this.subeTernaSeleccionado.getLgSerie().getValue(),
					this.subeTernaSeleccionado.getSamId().getValue(), this.subeTernaSeleccionado.getSamPin().getValue(),
					this.subeTernaSeleccionado.getPosId().getValue(),
					this.subeTernaSeleccionado.getLgCommPort().getValue(),
					this.subeTernaSeleccionado.getLimiteCredito().getValue(),
					this.subeTernaSeleccionado.getValorPurse().getValue(),
					this.subeTernaSeleccionado.getCantMaxTrxsPorTurno().getValue(),
					this.subeTernaSeleccionado.getImporteMaxTrxsPorTurno().getValue(),
					this.subeTernaSeleccionado.getIdEstadoTerna().getValue(),
					this.subeTernaSeleccionado.getIdUbicacionNacion().getValue(),
					this.subeTernaSeleccionado.getIdUbicacionLg().getValue(),
					this.subeTernaSeleccionado.getIdProveedorLg().getValue(),
					this.subeTernaSeleccionado.getDatosAdicionales().getValue(), idCliAsoc,
					this.getUsuario().getIdCliente(), this.getUsuario().getIdUsuario(),
					this.subeTernaSeleccionado.getIdModeloLg().getValue(),
					this.subeTernaSeleccionado.getIdExterno().getValue(),
					this.subeTernaSeleccionado.getAtribSocialHabilitar().getValue(),
					this.subeTernaSeleccionado.getCargaDiferidaHabilitar().getValue(),
					this.subeTernaSeleccionado.getLoguearCargaDeTarjetaEnBD().getValue(),
					this.getUsuario().getPassword(), this.subeTernaSeleccionado.getIdDistribuidorRed().getValue());

			if (resp.getError().getValue().getHayError().getValue()) {

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						resp.getError().getValue().getMsgError().getValue(), null));
				return;

			} else {

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Dato Adicional y clientes actualizados Correctamente", null));
				PrimeFaces.current().executeScript(
						"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#9ed5a0', '#a0d4b7', '"
								+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
										.replace("/", "\\/")
								+ "\\/javax.faces.resource\\/images\\/24x24\\/operacionOK.png')");
			}

			this.subeTernaSeleccionado.getIdentificacionTerminalSUBE()
					.setValue(GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
							.generaIdentificacionTerminalSUBE(this.subeTernaSeleccionado.getIdTerna().getValue()));

			// Vuelvo a cargar la lista de terminales, para que se actualice la grilla
			this.exportToExcel = false;
			informe();

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de Modidicacion de datos de LG: |" + e.getMessage() + "|", null));

		}
		return;
	}

	public Long getIdClienteFiltro() {
		return idClienteFiltro;
	}

	public void setIdClienteFiltro(Long idClienteFiltro) {
		this.idClienteFiltro = idClienteFiltro;
	}

	/*
	 * public String getMarcaPOSaDesasociar() { return marcaPOSaDesasociar; }
	 * 
	 * public void setMarcaPOSaDesasociar(String marcaPOSaDesasociar) {
	 * this.marcaPOSaDesasociar = marcaPOSaDesasociar; }
	 * 
	 * public String getImeiADesasociar() { return imeiADesasociar; }
	 * 
	 * public void setImeiADesasociar(String imeiADesasociar) { this.imeiADesasociar
	 * = imeiADesasociar; }
	 */
	public String getLeyendaAdicionalEncabezado() {
		return leyendaAdicionalEncabezado;
	}

	public void setLeyendaAdicionalEncabezado(String leyendaAdicionalEncabezado) {
		this.leyendaAdicionalEncabezado = leyendaAdicionalEncabezado;
	}

	public Long getId_accion() {
		return id_accion;
	}

	public void setId_accion(Long id_accion) {
		this.id_accion = id_accion;
	}

	public List<SelectItem> getAcciones() {
		return acciones;
	}

	public void setAcciones(List<SelectItem> acciones) {
		this.acciones = acciones;
	}

	public Long getIdUbicacion() {
		return idUbicacion;
	}

	public void setIdUbicacion(Long idUbicacion) {
		this.idUbicacion = idUbicacion;
	}

	public List<SelectItem> getUbicacionesPOS() {
		return ubicacionesPOS;
	}

	public void setUbicacionesPOS(List<SelectItem> ubicacionesPOS) {
		this.ubicacionesPOS = ubicacionesPOS;
	}

	public Long getIdUbicacionTermMod() {
		return idUbicacionTermMod;
	}

	public void setIdUbicacionTermMod(Long idUbicacionTermMod) {
		this.idUbicacionTermMod = idUbicacionTermMod;
	}

	public String getImeiSIM() {
		return imeiSIM;
	}

	public void setImeiSIM(String imeiSIM) {
		this.imeiSIM = imeiSIM;
	}

	public Boolean getAgrJerarquia() {
		return agrJerarquia;
	}

	public void setAgrJerarquia(Boolean agrJerarquia) {
		this.agrJerarquia = agrJerarquia;
	}

	public Long getIdProveedorLg() {
		return idProveedorLg;
	}

	public void setIdProveedorLg(Long idProveedorLg) {
		this.idProveedorLg = idProveedorLg;
	}

	public List<SelectItem> getProveedoresLg() {
		return proveedoresLg;
	}

	public void setProveedoresLg(List<SelectItem> proveedoresLg) {
		this.proveedoresLg = proveedoresLg;
	}

	public List<SelectItem> getIdsEstadosTerna() {
		return idsEstadosTerna;
	}

	public void setIdsEstadosTerna(List<SelectItem> idsEstadosTerna) {
		this.idsEstadosTerna = idsEstadosTerna;
	}

	public Long getIdEstadoTerna() {
		return idEstadoTerna;
	}

	public void setIdEstadoTerna(Long idEstadoTerna) {
		this.idEstadoTerna = idEstadoTerna;
	}

	public List<SelectItem> getIdsUbicacionesLg() {
		return idsUbicacionesLg;
	}

	public void setIdsUbicacionesLg(List<SelectItem> idsUbicacionesLg) {
		this.idsUbicacionesLg = idsUbicacionesLg;
	}

	public Long getIdUbicacionLg() {
		return idUbicacionLg;
	}

	public void setIdUbicacionLg(Long idUbicacionLg) {
		this.idUbicacionLg = idUbicacionLg;
	}

	public String getLgSerie() {
		return lgSerie;
	}

	public void setLgSerie(String lgSerie) {
		this.lgSerie = lgSerie;
	}

	public String getSamId() {
		return samId;
	}

	public void setSamId(String samId) {
		this.samId = samId;
	}

	public String getSamPin() {
		return samPin;
	}

	public void setSamPin(String samPin) {
		this.samPin = samPin;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public String getLgCommPort() {
		return lgCommPort;
	}

	public void setLgCommPort(String lgCommPort) {
		this.lgCommPort = lgCommPort;
	}

	public Float getLimiteCredito() {
		return limiteCredito;
	}

	public void setLimiteCredito(Float limiteCredito) {
		this.limiteCredito = limiteCredito;
	}

	public Float getValorPurse() {
		return valorPurse;
	}

	public void setValorPurse(Float valorPurse) {
		this.valorPurse = valorPurse;
	}

	public Float getSamSaldo() {
		return samSaldo;
	}

	public void setSamSaldo(Float samSaldo) {
		this.samSaldo = samSaldo;
	}

	public String getIdUbicacionNacion() {
		return idUbicacionNacion;
	}

	public void setIdUbicacionNacion(String idUbicacionNacion) {
		this.idUbicacionNacion = idUbicacionNacion;
	}

	public Integer getCantMaxTrxsPorTurno() {
		return cantMaxTrxsPorTurno;
	}

	public void setCantMaxTrxsPorTurno(Integer cantMaxTrxsPorTurno) {
		this.cantMaxTrxsPorTurno = cantMaxTrxsPorTurno;
	}

	public Float getImporteMaxTrxsPorTurno() {
		return importeMaxTrxsPorTurno;
	}

	public void setImporteMaxTrxsPorTurno(Float importeMaxTrxsPorTurno) {
		this.importeMaxTrxsPorTurno = importeMaxTrxsPorTurno;
	}

	public String getDatosAdicionales() {
		return datosAdicionales;
	}

	public void setDatosAdicionales(String datosAdicionales) {
		this.datosAdicionales = datosAdicionales;
	}

	public Boolean getHistorialLGS() {
		return historialLGS;
	}

	public void setHistorialLGS(Boolean historialLGS) {
		this.historialLGS = historialLGS;
	}

	public Long getIdTerna() {
		return idTerna;
	}

	public void setIdTerna(Long idTerna) {
		this.idTerna = idTerna;
	}

	public Float getSumaDeSaldoDeSams() {
		return sumaDeSaldoDeSams;
	}

	public void setSumaDeSaldoDeSams(Float sumaDeSaldoDeSams) {
		this.sumaDeSaldoDeSams = sumaDeSaldoDeSams;
	}

	public Float getSumaDeLimiteDeCreditoDeLGS() {
		return sumaDeLimiteDeCreditoDeLGS;
	}

	public void setSumaDeLimiteDeCreditoDeLGS(Float sumaDeLimiteDeCreditoDeLGS) {
		this.sumaDeLimiteDeCreditoDeLGS = sumaDeLimiteDeCreditoDeLGS;
	}

	public String getIdExterno() {
		return idExterno;
	}

	public void setIdExterno(String idExterno) {
		this.idExterno = idExterno;
	}

	public List<SelectItem> getModelosLg() {
		try {
			this.modelosLg = new ArrayList<SelectItem>();

			if (idProveedorLg != null) {
				SubeListaModelosLG slm = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.mostrarModelosLGdeSube(this.getUsuario().getIdMayorista(), idProveedorLg, null);

				if (slm != null && !slm.getError().getValue().getHayError().getValue()) {
					for (SubeModeloLG d : slm.getListModelosLG().getValue().getSubeModeloLG()) {
						this.modelosLg.add(new SelectItem(d.getIdModelo().getValue(), d.getDescModelo().getValue()));
					}
				}
			}

		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TOC).\n No se reconsultaron los modelos. Por favor intente nuevamente.",
							null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TRW).\n No se reconsultaron los modelos. Por favor intente nuevamente.",
							null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null,
							"Informe LGs SUBE. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-OTR).\n No se reconsultaron los modelos. Por favor intente nuevamente.",
							null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe LGs SUBE. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se pudo establecer la comunicación (GST-OTR).\n No se reconsultaron los modelos. Por favor intente nuevamente.",
						null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe LGs SUBE. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"No se pudo establecer la comunicación (GST-OTR).\n No se reconsultaron los modelos. Por favor intente nuevamente.",
					null));
			FacesContext.getCurrentInstance().validationFailed();

		}
		return modelosLg;
	}

	public void setModelosLg(List<SelectItem> modelosLg) {
		this.modelosLg = modelosLg;
	}

	public Long getIdModeloLg() {
		return idModeloLg;
	}

	public void setIdModeloLg(Long idModeloLg) {
		this.idModeloLg = idModeloLg;
	}

	public List<SelectItem> getM_modelosLg() {
		ObjectFactory objectFactory = new ObjectFactory();
		try {

			this.m_modelosLg = new ArrayList<SelectItem>();

			if (this.subeTernaSeleccionado != null && this.subeTernaSeleccionado.getIdProveedorLg() != null
					&& this.subeTernaSeleccionado.getIdProveedorLg().getValue() != null) {
				SubeListaModelosLG slm = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.mostrarModelosLGdeSube(this.getUsuario().getIdMayorista(),
								this.subeTernaSeleccionado.getIdProveedorLg().getValue(), null);

				if (slm != null && !slm.getError().getValue().getHayError().getValue()) {
					for (SubeModeloLG d : slm.getListModelosLG().getValue().getSubeModeloLG()) {
						this.m_modelosLg.add(new SelectItem(d.getIdModelo().getValue(), d.getDescModelo().getValue()));
					}
				}
			}

			if (this.m_modelosLg != null) {
				if (this.m_modelosLg.size() > 0) {

					if (this.subeTernaSeleccionado != null && (this.subeTernaSeleccionado.getIdProveedorLg() == null
							|| this.subeTernaSeleccionado.getIdProveedorLg().getValue() == null)) {

						if (this.subeTernaSeleccionado.getIdProveedorLg() == null) {
							this.subeTernaSeleccionado.setIdProveedorLg(objectFactory
									.createSubeTernaIdProveedorLg((Long) this.m_modelosLg.get(0).getValue()));
						} else {
							this.subeTernaSeleccionado.getIdProveedorLg()
									.setValue((Long) this.m_modelosLg.get(0).getValue());
						}
					}
				}

			}

		} catch (WebServiceException ste) {
			if (ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TOC).\n No se reconsultaron los m_modelos. Por favor intente nuevamente.",
							null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TRW).\n No se reconsultaron los m_modelos. Por favor intente nuevamente.",
							null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null,
							"No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));
				} else {
					LogACGHelper.escribirLog(null,
							"Informe LGs SUBE. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-OTR).\n No se reconsultaron los m_modelos. Por favor intente nuevamente.",
							null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe LGs SUBE. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se pudo establecer la comunicación (GST-OTR).\n No se reconsultaron los m_modelos. Por favor intente nuevamente.",
						null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe LGs SUBE. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"No se pudo establecer la comunicación (GST-OTR).\n No se reconsultaron los m_modelos. Por favor intente nuevamente.",
					null));
			FacesContext.getCurrentInstance().validationFailed();

		}
		return m_modelosLg;
	}

	public void setM_modelosLg(List<SelectItem> m_modelosLg) {
		this.m_modelosLg = m_modelosLg;
	}

	public List<SelectItem> getM_idsEstadosTerna() {
		return m_idsEstadosTerna;
	}

	public void setM_idsEstadosTerna(List<SelectItem> m_idsEstadosTerna) {
		this.m_idsEstadosTerna = m_idsEstadosTerna;
	}

	public List<SelectItem> getComandosRemotosLG() {
		return comandosRemotosLG;
	}

	public void setComandosRemotosLG(List<SelectItem> comandosRemotosLG) {
		this.comandosRemotosLG = comandosRemotosLG;
	}

	public Boolean getPermitirModificarComandoRemoto() {
		return permitirModificarComandoRemoto;
	}

	public void setPermitirModificarComandoRemoto(Boolean permitirModificarComandoRemoto) {
		this.permitirModificarComandoRemoto = permitirModificarComandoRemoto;
	}

	public Long getIdDistribuidorRed() {
		return idDistribuidorRed;
	}

	public void setIdDistribuidorRed(Long idDistribuidorRed) {
		this.idDistribuidorRed = idDistribuidorRed;
	}

	public List<SelectItem> getListDistribuidorRed() {
		return listDistribuidorRed;
	}

	public void setListDistribuidorRed(List<SelectItem> listDistribuidorRed) {
		this.listDistribuidorRed = listDistribuidorRed;
	}

	public AutoCompleteClienteView getAutoCompleteCliente() {
		return autoCompleteCliente;
	}

	public void setAutoCompleteCliente(AutoCompleteClienteView autoCompleteCliente) {
		this.autoCompleteCliente = autoCompleteCliente;
	}

	public SubeTerna getSubeTernaSeleccionado() {
		return subeTernaSeleccionado;
	}

	public void setSubeTernaSeleccionado(SubeTerna subeTernaSeleccionado) {
		this.subeTernaSeleccionado = subeTernaSeleccionado;
		if (this.subeTernaSeleccionado != null) {
			if (this.subeTernaSeleccionado.getIdCliente() != null
					&& this.subeTernaSeleccionado.getIdCliente().getValue() != null) {
				this.autoCompleteCliente
						.setCampoBusqueda(this.subeTernaSeleccionado.getIdCliente().getValue().toString());
				this.autoCompleteCliente.setId(this.subeTernaSeleccionado.getIdCliente().getValue());
			}
			if (this.subeTernaSeleccionado.getRazonSocialCliente() != null
					&& this.subeTernaSeleccionado.getRazonSocialCliente().getValue() != null) {
				this.autoCompleteCliente.setDescripcion(this.subeTernaSeleccionado.getRazonSocialCliente().getValue());
			}
		}
	}

}