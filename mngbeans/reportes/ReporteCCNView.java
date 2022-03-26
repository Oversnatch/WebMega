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
import java.util.List;
import java.util.TimeZone;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;
import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gestion.model.ArrayOfSaldoProveedor;
import com.americacg.cargavirtual.gestion.model.Banco;
import com.americacg.cargavirtual.gestion.model.BancoContainer;
import com.americacg.cargavirtual.gestion.model.ConceptoCC;
import com.americacg.cargavirtual.gestion.model.ConceptoCCcontainer;
import com.americacg.cargavirtual.gestion.model.CuentaCorrienteNuevo;
import com.americacg.cargavirtual.gestion.model.Descripcion;
import com.americacg.cargavirtual.gestion.model.DescripcionContainer;
import com.americacg.cargavirtual.gestion.model.InformeCuentaCorrienteNuevo;
import com.americacg.cargavirtual.gestion.model.SaldoProveedor;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reporteCCNView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteCCNView extends ReporteGeneral<CuentaCorrienteNuevo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2280654416077292961L;
	private Long idFiltro;
	private List<SelectItem> listaFiltro = new ArrayList<SelectItem>();

	private Float totalDebe;
	private Float totalHaber;

	private Boolean mostrarDetalles;
	private Boolean mostrarAuditoria;

	private String fechaDesdeRep;
	private String fechaHastaRep;

	private Float estadoFinal;

	private List<SelectItem> tiposCC;
	private Long idTipoCC = 1L;

	private List<SelectItem> listaConceptoCC = new ArrayList<SelectItem>();
	private Long idConceptoCC;

	private String leyendaID;

	private String nroFactura;

	private Long id_ccbanco_coment;
	private List<SelectItem> cc_banco_comentarios;

	private Boolean mostrarTotales;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		limpiarReporte(true, true, true);
		try {

			this.cc_banco_comentarios = new ArrayList<SelectItem>();
			DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.CONSULTA)
					.mostrarCCbancosComentarios(this.getUsuario().getIdMayorista(), null);

			for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
				this.cc_banco_comentarios.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
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
					LogACGHelper.escribirLog(null, "No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					e.setError("GST-TRW",
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.");					
				} else {
					LogACGHelper.escribirLog(null,
							"Informe CCN. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe CCN. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe CCN. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteCCNView() {
		super();
	}

	private void limpiarReporte(Boolean limpiarIdTipoCC, Boolean resetCombosFiltro, Boolean resetNroFactura) {

		this.list = null;
		this.cantRegistros = 0;

		if (this.idTipoCC == 4) {
			// Si selecciono la cuenta corriente de Caja, por defecto muestro el id del
			// cliente logueado.
			this.idFiltro = this.getUsuario().getIdCliente();
		} else {
			this.idFiltro = null;
		}

		this.totalDebe = 0F;
		this.totalHaber = 0F;

		this.mostrarTotales = false;

		if (resetCombosFiltro) {
			this.mostrarDetalles = false;
			this.mostrarAuditoria = false;
			this.exportToExcel = false;
		}

		this.fechaDesdeRep = null;
		this.fechaHastaRep = null;

		this.estadoFinal = 0F;

		if (limpiarIdTipoCC) {
			this.idTipoCC = 1L;
		}

		this.idConceptoCC = null;

		this.leyendaID = null;

		this.inicializarFechas();

		if (resetNroFactura || this.idTipoCC == 1 || this.idTipoCC == 3 || this.idTipoCC == 4) {
			this.nroFactura = null;
		}

		id_ccbanco_coment = null;

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

			this.list = null;
			this.mostrarRegistros = true;

			this.totalDebe = 0F;
			this.totalHaber = 0F;
			this.list = null;
			this.fechaDesdeRep = null;
			this.fechaHastaRep = null;
			this.estadoFinal = 0F;
			this.mostrarTotales = false;
			this.cantRegistros = 0;

			// Validacion de fechas
			if (!validaFechas()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en la validacion de las fechas", null));
			} else {

				// VALIDO QUE LA FECHADESDE NO SEA MENOR A 3 MESES DESDE LA FECHA ACTUAL
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.MONTH, -4); // Le resto 4 meses

				// Seteo el Ultimo día del mes a las 23:59:59
				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);

				// Asigno el HusoHorario
				TimeZone tz = TimeZone.getTimeZone(this.getUsuario().getHusoHorario());
				cal.setTimeZone(tz);

				Calendar cal2 = Calendar.getInstance();
				cal2.setTime(fechaHoraDesde);
				TimeZone tz2 = TimeZone.getTimeZone(this.getUsuario().getHusoHorario());
				cal2.setTimeZone(tz2);

				if (cal.compareTo(cal2) > 0) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Los reportes con mas de 3 meses de antiguedad no se encuentran OnLine. Por favor comuniquese con su Distribuidor",
							null));
					return;
				}
				// FIN VALIDO QUE LA FECHADESDE NO SEA MENOR A 3 MESES DESDE LA FECHA ACTUAL

				if ("P".equals(this.getUsuario().getTipoCliente())) {
					this.idTipoCC = 1L;
					this.idFiltro = this.getUsuario().getIdCliente();
				}

				GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
				gcFechaHoraDesde.setTime(fechaHoraDesde);
				XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gcFechaHoraDesde);

				GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
				gcFechaHoraHasta.setTime(fechaHoraHasta);
				XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gcFechaHoraHasta);

				InformeCuentaCorrienteNuevo i = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.informeCuentaCorrienteNuevo(this.getUsuario().getIdMayorista(), this.idTipoCC, idFiltro,
								xmlGCFechaHoraDesde, xmlGCFechaHoraHasta, this.getUsuario().getIdCliente(),
								this.idConceptoCC, nroFactura, id_ccbanco_coment);

				if (i.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							i.getError().getValue().getMsgError().getValue(), null));
				} else {
					this.list = i.getListCuentaCorrienteNuevo().getValue().getCuentaCorrienteNuevo();

					if (this.list == null) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No existe información para la consulta realizada.", null));
					} else if (this.list.isEmpty()) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No existe información para la consulta realizada.", null));
						this.list = null;
					} else {
						this.cantRegistros = list.size();

						SimpleDateFormat ff = new SimpleDateFormat("dd/MM/yyyy");
						String fd = ff.format(fechaHoraDesde).toString();
						String fh = ff.format(fechaHoraHasta).toString();
						this.fechaDesdeRep = fd;
						this.fechaHastaRep = fh;

						// Calcular Totales
						this.totalDebe = i.getDebeFinal().getValue();
						this.totalHaber = i.getHaberFinal().getValue();
						this.estadoFinal = i.getEstadoFinal().getValue();

						this.mostrarTotales = i.getMostrarTotales().getValue();

						// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {

							// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
							mostrarArchivoCSV = false;

							String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
							String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

							StringBuilder sb = new StringBuilder();

							sb.append((char) 34).append("Informe de Cuenta Corriente (").append(fechaDesdeRep)
									.append(" hasta ").append(fechaHastaRep).append(") - (").append(cantRegistros)
									.append(" Registros)").append((char) 34);
							sb.append((char) 13).append((char) 10);

							if (mostrarDetalles) {
								sb.append((char) 34).append("ID CC").append((char) 34).append(csvSepCamp);
							}
							sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Concepto CC").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Concepto CC").append((char) 34).append(csvSepCamp);
							if (idTipoCC.compareTo(1L) == 0) {
								sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);
							}
							if ((idTipoCC.compareTo(2L) == 0) || (nroFactura != null)
									|| (idTipoCC.compareTo(3L) == 0 && mostrarDetalles)
									|| (idTipoCC.compareTo(4L) == 0 && mostrarDetalles)) {
								sb.append((char) 34).append("ID Proveedor").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Proveedor").append((char) 34).append(csvSepCamp);
							}
							if (idTipoCC.compareTo(3L) == 0 || nroFactura != null) {
								sb.append((char) 34).append(nroFactura != null ? "Pagado con " : "").append("ID Banco")
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(nroFactura != null ? "Pagado con " : "").append("Banco")
										.append((char) 34).append(csvSepCamp);
							}
							if (idTipoCC.compareTo(4L) == 0 || nroFactura != null) {
								sb.append((char) 34).append(nroFactura != null ? "Pagado con " : "")
										.append("Caja para ID Cliente").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(nroFactura != null ? "Pagado con " : "")
										.append("Caja de Cliente").append((char) 34).append(csvSepCamp);
							}
							if (mostrarDetalles && idTipoCC.compareTo(1L) == 0) {
								sb.append((char) 34).append("ID Rep.Saldo").append((char) 34).append(csvSepCamp);
							}
							if (mostrarDetalles) {
								sb.append((char) 34).append("ID Dep.Adelanto").append((char) 34).append(csvSepCamp);
							}
							if (mostrarDetalles && idTipoCC.compareTo(1L) == 0) {
								sb.append((char) 34).append("ID Res.Comision").append((char) 34).append(csvSepCamp);
							}
							if (mostrarDetalles && (idTipoCC.compareTo(2L) == 0 || idTipoCC.compareTo(3L) == 0
									|| idTipoCC.compareTo(4L) == 0)) {
								sb.append((char) 34).append("ID Inc. Saldo Mayorista").append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append("ID Compra Saldo Prov").append((char) 34)
										.append(csvSepCamp);
							}
							if (mostrarDetalles && idTipoCC.compareTo(1L) == 0) {
								sb.append((char) 34).append("ID TipoMovimiento").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("TipoMovimiento").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("ID Rep.Saldo WU").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("ID TipoRepartoWU").append((char) 34).append(csvSepCamp);
							}
							if (mostrarDetalles && idTipoCC == 1) {
								sb.append((char) 34).append("TipoRepartoWU").append((char) 34).append(csvSepCamp);
							}
							if (mostrarDetalles && (idTipoCC.compareTo(2L) == 0 || idTipoCC.compareTo(3L) == 0
									|| idTipoCC.compareTo(4L) == 0)) {
								sb.append((char) 34).append("Nro. Factura").append((char) 34).append(csvSepCamp);
							}
							sb.append((char) 34).append("Debe").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Haber").append((char) 34).append(csvSepCamp);
							if (mostrarTotales) {
								sb.append((char) 34).append(nroFactura != null ? "Pend. Pago" : "Saldo")
										.append((char) 34).append(csvSepCamp);
							}
							if (idTipoCC.compareTo(3L) == 0) {
								sb.append((char) 34).append("ID Comentario").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Comentario").append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append("Motivo").append((char) 34).append(csvSepCamp);
							if (mostrarAuditoria) {
								sb.append((char) 34).append("ID Cliente Que Autorizo").append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append("Cliente Que Autorizo").append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append("ID Usuario Que Autorizo").append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append("Usuario Que Autorizo").append((char) 34)
										.append(csvSepCamp);
							}
							sb.append((char) 13).append((char) 10);

							// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

							// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
							// EXPORTAR EN CASO DE SER NECESARIO
							if (list != null) {
								for (CuentaCorrienteNuevo ic : list) {

									if (mostrarDetalles) {
										if (ic.getIdCC().getValue() != null) {
											sb.append((char) 34).append(ic.getIdCC().getValue()).append((char) 34)
													.append(csvSepCamp);
										} else {
											sb.append((char) 34).append((char) 34).append(csvSepCamp);
										}
									}
									sb.append((char) 34).append(
											sdf.format(ic.getFecha().toGregorianCalendar().getTime()).toString())
											.append((char) 34).append(csvSepCamp);

									if (ic.getIdConceptoCC().getValue() != null) {
										sb.append((char) 34).append(ic.getIdConceptoCC().getValue()).append((char) 34)
												.append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}

									sb.append((char) 34).append(ic.getDescConceptoCC().getValue()).append((char) 34)
											.append(csvSepCamp);
									if (idTipoCC.compareTo(1L) == 0) {
										sb.append((char) 34).append(ic.getIdCliente().getValue()).append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append(ic.getRazonSocial().getValue()).append((char) 34)
												.append(csvSepCamp);
									}
									if ((idTipoCC.compareTo(2L) == 0) || (nroFactura != null)
											|| (idTipoCC.compareTo(3L) == 0 && mostrarDetalles)
											|| (idTipoCC.compareTo(4L) == 0 && mostrarDetalles)) {
										sb.append((char) 34).append(ic.getIdProveedor().getValue()).append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append(ic.getProvRazonSocial().getValue())
												.append((char) 34).append(csvSepCamp);
									}
									if (idTipoCC.compareTo(3L) == 0 || nroFactura != null) {
										sb.append((char) 34).append(ic.getIdBanco().getValue()).append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append(ic.getNombreBanco().getValue()).append((char) 34)
												.append(csvSepCamp);
									}
									if (idTipoCC.compareTo(4L) == 0 || nroFactura != null) {
										sb.append((char) 34).append(ic.getIdCliente().getValue()).append((char) 34)
												.append(csvSepCamp);
										sb.append((char) 34).append(ic.getRazonSocial().getValue()).append((char) 34)
												.append(csvSepCamp);
									}
									if (mostrarDetalles && idTipoCC.compareTo(1L) == 0) {
										if (ic.getIdRepartoSaldo().getValue() != null) {
											sb.append((char) 34).append(ic.getIdRepartoSaldo().getValue())
													.append((char) 34).append(csvSepCamp);
										} else {
											sb.append((char) 34).append((char) 34).append(csvSepCamp);
										}
									}
									if (mostrarDetalles) {
										if (ic.getIdDepAdelanto().getValue() != null) {
											sb.append((char) 34).append(ic.getIdDepAdelanto().getValue())
													.append((char) 34).append(csvSepCamp);
										} else {
											sb.append((char) 34).append((char) 34).append(csvSepCamp);
										}
									}
									if (mostrarDetalles && idTipoCC.compareTo(1L) == 0) {
										if (ic.getIdResultadoComision().getValue() != null) {
											sb.append((char) 34).append(ic.getIdResultadoComision().getValue())
													.append((char) 34).append(csvSepCamp);
										} else {
											sb.append((char) 34).append((char) 34).append(csvSepCamp);
										}
									}
									if (mostrarDetalles && (idTipoCC.compareTo(2L) == 0 || idTipoCC.compareTo(3L) == 0
											|| idTipoCC.compareTo(4L) == 0)) {
										if (ic.getIdIncSaldoMayorista().getValue() != null) {
											sb.append((char) 34).append(ic.getIdIncSaldoMayorista().getValue())
													.append((char) 34).append(csvSepCamp);
										} else {
											sb.append((char) 34).append((char) 34).append(csvSepCamp);
										}
										if (ic.getIdcompraSaldoProv().getValue() != null) {
											sb.append((char) 34).append(ic.getIdcompraSaldoProv().getValue())
													.append((char) 34).append(csvSepCamp);
										} else {
											sb.append((char) 34).append((char) 34).append(csvSepCamp);
										}
									}
									if (mostrarDetalles && idTipoCC.compareTo(1L) == 0) {
										if (ic.getIdTipoMovimiento().getValue() != null) {
											sb.append((char) 34).append(ic.getIdTipoMovimiento().getValue())
													.append((char) 34).append(csvSepCamp);
										} else {
											sb.append((char) 34).append((char) 34).append(csvSepCamp);
										}
										if (ic.getDescripcionTipoMovimiento().getValue() != null
												&& !"".equals(ic.getDescripcionTipoMovimiento().getValue())) {
											sb.append((char) 34).append(ic.getDescripcionTipoMovimiento().getValue())
													.append((char) 34).append(csvSepCamp);
										} else {
											sb.append((char) 34).append((char) 34).append(csvSepCamp);
										}
										if (ic.getIdRepartoSaldoWu().getValue() != null) {
											sb.append((char) 34).append(ic.getIdRepartoSaldoWu().getValue())
													.append((char) 34).append(csvSepCamp);
										} else {
											sb.append((char) 34).append((char) 34).append(csvSepCamp);
										}

										if (ic.getIdTipoRepartoWu().getValue() != null) {
											sb.append((char) 34).append(ic.getIdTipoRepartoWu().getValue())
													.append((char) 34).append(csvSepCamp);
										} else {
											sb.append((char) 34).append((char) 34).append(csvSepCamp);
										}
									}
									if (mostrarDetalles && idTipoCC == 1) {
										if (ic.getDescripcionTipoRepartoWu().getValue() != null
												&& !"".equals(ic.getDescripcionTipoRepartoWu().getValue())) {
											sb.append((char) 34).append(ic.getDescripcionTipoRepartoWu().getValue())
													.append((char) 34).append(csvSepCamp);
										} else {
											sb.append((char) 34).append((char) 34).append(csvSepCamp);
										}
									}
									if (mostrarDetalles && (idTipoCC.compareTo(2L) == 0 || idTipoCC.compareTo(3L) == 0
											|| idTipoCC.compareTo(4L) == 0)) {
										sb.append((char) 34).append(ic.getNroFactura().getValue()).append((char) 34)
												.append(csvSepCamp);
									}
									sb.append((char) 34).append("$")
										.append(ACGFormatHelper.format(ic.getDebe().getValue(), csvSepDec))
										.append((char) 34).append(csvSepCamp);
									
									sb.append((char) 34).append("$")
										.append(ACGFormatHelper.format(ic.getHaber().getValue(), csvSepDec))
										.append((char) 34).append(csvSepCamp);
									
									if (mostrarTotales) {
										sb.append((char) 34).append("$")
											.append(ACGFormatHelper.format(ic.getSaldo().getValue(), csvSepDec))
											.append((char) 34).append(csvSepCamp);
									}
									if (idTipoCC.compareTo(3L) == 0) {
										sb.append((char) 34).append(ic.getIdCcbancoComent().getValue())
												.append((char) 34).append(csvSepCamp);
										sb.append((char) 34).append(ic.getDescripcionCCbancoComent().getValue())
												.append((char) 34).append(csvSepCamp);
									}
									sb.append((char) 34).append(ic.getMotivo().getValue()).append((char) 34)
											.append(csvSepCamp);
									if (mostrarAuditoria) {
										if (ic.getIdClienteQueAutorizo().getValue() != null) {
											sb.append((char) 34).append(ic.getIdClienteQueAutorizo().getValue())
													.append((char) 34).append(csvSepCamp);
										} else {
											sb.append((char) 34).append((char) 34).append(csvSepCamp);
										}
										if (ic.getRazonSocialClienteQueAutorizo().getValue() != null
												&& !"".equals(ic.getRazonSocialClienteQueAutorizo().getValue())) {
											sb.append((char) 34)
													.append(ic.getRazonSocialClienteQueAutorizo().getValue())
													.append((char) 34).append(csvSepCamp);
										} else {
											sb.append((char) 34).append((char) 34).append(csvSepCamp);
										}
										if (ic.getIdUsuarioQueAutorizo().getValue() != null) {
											sb.append((char) 34).append(ic.getIdUsuarioQueAutorizo().getValue())
													.append((char) 34).append(csvSepCamp);
										} else {
											sb.append((char) 34).append((char) 34).append(csvSepCamp);
										}
										if (ic.getUsuarioQueAutorizo().getValue() != null
												&& !"".equals(ic.getUsuarioQueAutorizo().getValue())) {
											sb.append((char) 34).append(ic.getUsuarioQueAutorizo().getValue())
													.append((char) 34).append(csvSepCamp);
										} else {
											sb.append((char) 34).append((char) 34).append(csvSepCamp);
										}
									}
									sb.append((char) 13).append((char) 10);
								}
							}
							if (mostrarDetalles) {
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							}
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							if (idTipoCC.compareTo(1L) == 0) {
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							}
							if ((idTipoCC.compareTo(2L) == 0) || (nroFactura != null)
									|| (idTipoCC.compareTo(3L) == 0 && mostrarDetalles)
									|| (idTipoCC.compareTo(4L) == 0 && mostrarDetalles)) {
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							}
							if (idTipoCC.compareTo(3L) == 0 || nroFactura != null) {
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							}
							if (idTipoCC.compareTo(4L) == 0 || nroFactura != null) {
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							}
							if (mostrarDetalles && idTipoCC.compareTo(1L) == 0) {
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							}
							if (mostrarDetalles) {
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							}
							if (mostrarDetalles && idTipoCC.compareTo(1L) == 0) {
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							}
							if (mostrarDetalles && (idTipoCC.compareTo(2L) == 0 || idTipoCC.compareTo(3L) == 0
									|| idTipoCC.compareTo(4L) == 0)) {
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							}
							if (mostrarDetalles && idTipoCC.compareTo(1L) == 0) {
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							}
							if (mostrarDetalles && idTipoCC == 1) {
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							}
							if (mostrarDetalles && (idTipoCC.compareTo(2L) == 0 || idTipoCC.compareTo(3L) == 0
									|| idTipoCC.compareTo(4L) == 0)) {
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							}
							sb.append((char) 34).append("$")
								.append(ACGFormatHelper.format(totalDebe, csvSepDec))
									.append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("$")
								.append(ACGFormatHelper.format(totalHaber, csvSepDec))							
									.append((char) 34).append(csvSepCamp);
							if (mostrarTotales) {
								sb.append((char) 34).append("$")
									.append(ACGFormatHelper.format(estadoFinal, csvSepDec))								
									.append((char) 34).append(csvSepCamp);
							}
							if (idTipoCC.compareTo(3L) == 0) {
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							}
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							if (mostrarAuditoria) {
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							}
							sb.append((char) 13).append((char) 10);

							FacesContext fc = FacesContext.getCurrentInstance();
							ExternalContext ec = fc.getExternalContext();

							ec.responseReset();
							ec.setResponseContentType("text/plain");
							ec.setResponseContentLength(sb.toString().length());

							sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
							ec.setResponseHeader("Content-Disposition",
									"attachment; filename=\"" + sdf.format(new Date()) + "_("
											+ this.getUsuario().getIdMayorista() + ")_" + "InformeCCN.csv" + "\"");

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
							"No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null, "No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));					
				} else {
					LogACGHelper.escribirLog(null,
							"Informe CCN. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe CCN. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe CCN. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de CCN: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;
	}

	public void cambiarTipoCC() {

		limpiarReporte(false, false, false);

		// Recargar la "listaConceptoCC" segun el "idTipoCC" seleccionado
		cargarListaConceptosCC(idTipoCC);

		// Cargo la lista de Proveedores y Bancos para mostrar como filtro
		cargaListaFiltro(idTipoCC);

		id_ccbanco_coment = null;

	}

	private void cargarListaConceptosCC(Long w_idTipoCC) {

		try {

			this.listaConceptoCC = new ArrayList<SelectItem>();
			ConceptoCCcontainer ccc = GestionServiceHelper.getGestionService(CfgTimeout.CONSULTA)
					.mostrarConceptosCuentaCorriente(this.getUsuario().getIdMayorista(), w_idTipoCC, null);
			if ((ccc != null) && (!ccc.getError().getValue().getHayError().getValue())) {
				if (!ccc.getListConceptoCC().getValue().getConceptoCC().isEmpty()) {
					for (ConceptoCC c : ccc.getListConceptoCC().getValue().getConceptoCC()) {
						this.listaConceptoCC
								.add(new SelectItem(c.getIdConceptoCC().getValue(), c.getDescConceptoCC().getValue()));
					}
				}
			}

			this.leyendaID = "";
			if (w_idTipoCC == 1) {
				// CC Clientes
				this.leyendaID = "idCliente";
			} else if (w_idTipoCC == 2) {
				// CC Proveedores
				this.leyendaID = "idProveedor";
			} else if (w_idTipoCC == 3) {
				// CC Bancos
				this.leyendaID = "idBanco";
			} else if (w_idTipoCC == 4) {
				// CC Caja
				this.leyendaID = "Caja para idCliente";
			}
		} catch (Exception e) {
			this.listaConceptoCC = new ArrayList<SelectItem>();
		}

	}

	private void cargaListaFiltro(Long w_idTipoCC) {
		try {
			if (w_idTipoCC == 1) {
				// CC Clientes
				this.listaFiltro = new ArrayList<SelectItem>();

			} else if (w_idTipoCC == 2) {
				// CC Proveedores
				this.listaFiltro = new ArrayList<SelectItem>();
				this.listaFiltro.add(new SelectItem("", "Seleccione"));
				ArrayOfSaldoProveedor lp = GestionServiceHelper.getGestionService(CfgTimeout.CONSULTA)
						.obtenerSaldoProveedores(this.getUsuario().getIdMayorista(), null, false, false);
				for (SaldoProveedor sp : lp.getSaldoProveedor()) {
					this.listaFiltro
							.add(new SelectItem(sp.getIdProveedor().getValue(), sp.getRazonSocial().getValue()));
				}

			} else if (w_idTipoCC == 3) {
				// CC Bancos
				this.listaFiltro = new ArrayList<SelectItem>();
				this.listaFiltro.add(new SelectItem("", "Seleccione"));

				BancoContainer bc = GestionServiceHelper.getGestionService(CfgTimeout.CONSULTA)
						.mostrarBanco(this.getUsuario().getIdMayorista(), null, null, false);

				if ((bc != null) && (bc.getListBanco() != null)) {
					if (bc.getListBanco().getValue() != null) {
						if (bc.getListBanco().getValue().getBanco().size() > 0) {
							for (Banco b : bc.getListBanco().getValue().getBanco()) {
								this.listaFiltro.add(
										new SelectItem(b.getIdBanco().getValue(), b.getNombreBanco().getValue() + "|" + b.getSucursal().getValue() + "|"
												+ b.getCuenta().getValue() + "|" + b.getCBU().getValue() + "|" + b.getDescripcion().getValue() + "|"));
							}
						}
					}
				}

			} else if (w_idTipoCC == 4) {
				// CC Caja
				this.listaFiltro = new ArrayList<SelectItem>();
			}

		} catch (Exception e) {
			// No hacer nada
		}

	}

	public Float getTotalDebe() {
		return totalDebe;
	}

	public void setTotalDebe(Float totalDebe) {
		this.totalDebe = totalDebe;
	}

	public Float getTotalHaber() {
		return totalHaber;
	}

	public void setTotalHaber(Float totalHaber) {
		this.totalHaber = totalHaber;
	}

	public Boolean getMostrarDetalles() {
		return mostrarDetalles;
	}

	public void setMostrarDetalles(Boolean mostrarDetalles) {
		this.mostrarDetalles = mostrarDetalles;
	}

	public Boolean getMostrarAuditoria() {
		return mostrarAuditoria;
	}

	public void setMostrarAuditoria(Boolean mostrarAuditoria) {
		this.mostrarAuditoria = mostrarAuditoria;
	}

	public String getFechaDesdeRep() {
		return fechaDesdeRep;
	}

	public void setFechaDesdeRep(String fechaDesdeRep) {
		this.fechaDesdeRep = fechaDesdeRep;
	}

	public String getFechaHastaRep() {
		return fechaHastaRep;
	}

	public void setFechaHastaRep(String fechaHastaRep) {
		this.fechaHastaRep = fechaHastaRep;
	}

	public Float getEstadoFinal() {
		return estadoFinal;
	}

	public void setEstadoFinal(Float estadoFinal) {
		this.estadoFinal = estadoFinal;
	}

	public List<SelectItem> getTiposCC() {

		this.tiposCC = new ArrayList<SelectItem>();

		if (("M".equals(this.getUsuario().getTipoCliente())) || ("D".equals(this.getUsuario().getTipoCliente()))) {
			this.tiposCC.add(new SelectItem(1L, "CC Clientes"));
		}

		if ("M".equals(this.getUsuario().getTipoCliente())) {
			this.tiposCC.add(new SelectItem(2L, "CC Proveedores"));
			this.tiposCC.add(new SelectItem(3L, "CC Bancos"));
		}

		if (("M".equals(this.getUsuario().getTipoCliente())) || ("D".equals(this.getUsuario().getTipoCliente()))) {
			this.tiposCC.add(new SelectItem(4L, "Caja"));
		}

		// La primera vez, cuando cargo el combo de tiposCC, cargo tambien la
		// listaConceptosCC para clientes (1L)
		cargarListaConceptosCC(1L);

		return tiposCC;
	}

	public void setTiposCC(List<SelectItem> tiposCC) {
		this.tiposCC = tiposCC;
	}

	public List<SelectItem> getListaConceptoCC() {
		return listaConceptoCC;
	}

	public void setListaConceptoCC(List<SelectItem> listaConceptoCC) {
		this.listaConceptoCC = listaConceptoCC;
	}

	public Long getIdFiltro() {
		return idFiltro;
	}

	public void setIdFiltro(Long idFiltro) {
		this.idFiltro = idFiltro;
	}

	public Long getIdConceptoCC() {
		return idConceptoCC;
	}

	public void setIdConceptoCC(Long idConceptoCC) {
		this.idConceptoCC = idConceptoCC;
	}

	public Long getIdTipoCC() {
		return idTipoCC;
	}

	public void setIdTipoCC(Long idTipoCC) {
		this.idTipoCC = idTipoCC;
	}

	public String getLeyendaID() {
		return leyendaID;
	}

	public void setLeyendaID(String leyendaID) {
		this.leyendaID = leyendaID;
	}

	public List<SelectItem> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<SelectItem> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

	public String getNroFactura() {
		return nroFactura;
	}

	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
	}

	public Long getId_ccbanco_coment() {
		return id_ccbanco_coment;
	}

	public void setId_ccbanco_coment(Long id_ccbanco_coment) {
		this.id_ccbanco_coment = id_ccbanco_coment;
	}

	public List<SelectItem> getCc_banco_comentarios() {
		return cc_banco_comentarios;
	}

	public void setCc_banco_comentarios(List<SelectItem> cc_banco_comentarios) {
		this.cc_banco_comentarios = cc_banco_comentarios;
	}

	public Boolean getMostrarTotales() {
		return mostrarTotales;
	}

	public void setMostrarTotales(Boolean mostrarTotales) {
		this.mostrarTotales = mostrarTotales;
	}

}
