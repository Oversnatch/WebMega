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
import com.americacg.cargavirtual.gestion.model.InformeStockTerminales;
import com.americacg.cargavirtual.gestion.model.ResultadoBase;
import com.americacg.cargavirtual.gestion.model.Terminal;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;
import com.americacg.cargavirtual.web.shared.AutoCompleteClienteView;

@Named("reporteStockTerminalesView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteStockTerminalesView extends ReporteGeneral<Terminal> {

	private static final long serialVersionUID = 2534013287248848442L;
	private List<SelectItem> marcasPOS;
	private String imei;
	private String marcaPOS;
	private Long idClienteFiltro;
	private AutoCompleteClienteView autoCompleteCliente = new AutoCompleteClienteView();
	private Boolean historialTerms;
	private String leyendaAdicionalEncabezado;
	private Long id_accion;
	private List<SelectItem> acciones;
	private Long idUbicacion;
	private List<SelectItem> ubicacionesPOS;
	private String imeiSIM;
	private Boolean agrJerarquia;
	private Terminal terminalSeleccionado = null;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();

		this.list = null;
		this.imei = null;
		this.marcaPOS = null;
		this.idClienteFiltro = null;
		this.cantRegistros = 0;
		this.historialTerms = false;
		this.leyendaAdicionalEncabezado = "";
		this.id_accion = null;
		this.idUbicacion = null;
		this.imeiSIM = null;
		this.agrJerarquia = null;

		try {

			this.marcasPOS = new ArrayList<SelectItem>();
			DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarTipoTerminales(this.getUsuario().getIdMayorista());

			for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
				if (d.getAlias() != null) {
					if (d.getAlias().getValue().length() > 0) {
						this.marcasPOS.add(new SelectItem(d.getAlias().getValue(), d.getAlias().getValue()));
					}
				}
			}
			this.acciones = new ArrayList<SelectItem>();
			DescripcionContainer a = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarStockTermAcciones(null, null);

			for (Descripcion d : a.getListDescripcion().getValue().getDescripcion()) {
				if (d.getDescripcion().getValue() != null) {
					if (d.getDescripcion().getValue().length() > 0) {
						this.acciones.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
					}
				}
			}
			this.ubicacionesPOS = new ArrayList<SelectItem>();
			DescripcionContainer u = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarTerminalesUbicacion(null, null);

			for (Descripcion d : u.getListDescripcion().getValue().getDescripcion()) {
				if (d.getDescripcion() != null) {
					if (d.getDescripcion().getValue().length() > 0) {
						this.ubicacionesPOS.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
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
							"Informe Stock Terminales. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Stock Terminales. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Stock Terminales. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteStockTerminalesView() {
		super();
	}

	public void realizarInforme() {
		this.exportToExcel = false;
		informe();
	}

	public void exportarInforme() {
		this.exportToExcel = true;
		informe();
	}

	public void checkHistorialTerms(ActionEvent ae) {
		inicializarFechas();
		this.list = null;
	}

	private void informe() {
		try {
			this.mostrarRegistros = true;
			cantRegistros = 0;

			if (historialTerms) {
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

			GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
			gcFechaHoraDesde.setTime(fechaHoraDesde);
			XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraDesde);

			GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
			gcFechaHoraHasta.setTime(fechaHoraHasta);
			XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraHasta);

			InformeStockTerminales ist = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarListadoStockTerminales(this.getUsuario().getIdMayorista(), imei, marcaPOS, idClienteFiltro,
							historialTerms, xmlGCFechaHoraDesde, xmlGCFechaHoraHasta, id_accion, idUbicacion, imeiSIM,
							this.getUsuario().getIdCliente());

			// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
			mostrarArchivoCSV = false;

			String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
			String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

			StringBuilder sb = new StringBuilder();

			if (ist.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						ist.getError().getValue().getMsgError().getValue(), null));
				this.list = null;
			} else {
				this.list = ist.getListStockTerminales().getValue().getTerminal();

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

					Integer i = 0;

					// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
					if (this.exportToExcel) {
						// Header
						sb.append((char) 34).append("Marca").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("IMEI/SN").append((char) 34).append(csvSepCamp);

						if (!historialTerms) {
							sb.append((char) 34).append("Fecha Alta").append((char) 34).append(csvSepCamp);
						}

						sb.append((char) 34).append("ID Ubicacion").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Ubicacion").append((char) 34).append(csvSepCamp);

						if (historialTerms) {
							sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
						}

						if (historialTerms) {
							sb.append((char) 34).append("ID Accion").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Accion").append((char) 34).append(csvSepCamp);
						}

						sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);

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

						if (!historialTerms) {
							sb.append((char) 34).append("Fecha Ult. Asoc Cliente").append((char) 34).append(csvSepCamp);
						}

						sb.append((char) 34).append("IMEI SIM").append((char) 34).append(csvSepCamp);

						sb.append((char) 34).append("Validar IMEI SIM").append((char) 34).append(csvSepCamp);

						sb.append((char) 34).append("Datos Adicionales").append((char) 34).append(csvSepCamp);

						if (!historialTerms) {
							sb.append((char) 34).append("ID Cliente que dio de Alta").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Cliente que dio de Alta").append((char) 34).append(csvSepCamp);
						}

						if (!historialTerms) {
							sb.append((char) 34).append("Ult. Modelo Logueado").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Ult. Version Logueada").append((char) 34).append(csvSepCamp);
						}
						if (!historialTerms) {
							sb.append((char) 34).append("Ult. Acceso").append((char) 34).append(csvSepCamp);
						}

						sb.append((char) 13).append((char) 10);
					}

					// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
					SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
					// EXPORTAR EN CASO DE SER NECESARIO
					for (Terminal t : list) {

						// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {

							if (i == 0) {
								mostrarArchivoCSV = true;
							}

							sb.append((char) 34).append(t.getMarca().getValue()).append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("'").append(t.getImei().getValue()).append((char) 34)
									.append(csvSepCamp);

							if (!historialTerms) {
								sb.append((char) 34).append((t.getFechaAlta() == null) ? ""
										: ff.format(t.getFechaAlta().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append(t.getIdUbicacion().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(t.getDescUbicacion().getValue()).append((char) 34)
									.append(csvSepCamp);

							if (historialTerms) {
								sb.append((char) 34).append((t.getFechaHistorial() == null) ? ""
										: ff.format(t.getFechaHistorial().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);
							}

							if (historialTerms) {
								sb.append((char) 34).append(t.getIdAccionHistorial().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(t.getDescAccionHistorial().getValue()).append((char) 34)
										.append(csvSepCamp);
							}

							sb.append((char) 34);
							if (t.getIdCliente() != null && t.getIdCliente().getValue() != null) {
								sb.append(t.getIdCliente().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);
							sb.append((char) 34);
							if (t.getRazonSocialCliente() != null && t.getRazonSocialCliente().getValue() != null) {
								sb.append(t.getRazonSocialCliente().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);

							if (agrJerarquia) {
								sb.append((char) 34)
										.append((t.getIdDist1() == null || t.getIdDist1().getValue() == 0) ? ""
												: t.getIdDist1().getValue())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(t.getDescDist1().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34)
										.append((t.getIdDist2() == null || t.getIdDist2().getValue() == 0) ? ""
												: t.getIdDist2().getValue())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(t.getDescDist2().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34)
										.append((t.getIdDist3() == null || t.getIdDist3().getValue() == 0) ? ""
												: t.getIdDist3().getValue())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(t.getDescDist3().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34)
										.append((t.getIdDist4() == null || t.getIdDist4().getValue() == 0) ? ""
												: t.getIdDist4().getValue())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(t.getDescDist4().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34)
										.append((t.getIdDist5() == null || t.getIdDist5().getValue() == 0) ? ""
												: t.getIdDist5().getValue())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(t.getDescDist5().getValue()).append((char) 34)
										.append(csvSepCamp);
							}

							if (!historialTerms) {
								sb.append((char) 34)
										.append((t.getFechaUltAsocCliente() == null) ? ""
												: ff.format(t.getFechaUltAsocCliente().toGregorianCalendar().getTime())
														.toString())
										.append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append("'").append(t.getImeiSIM().getValue()).append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 34).append(t.getValidarImeiTerminalImeiSIM().getValue() == 0 ? "NO" : "SI")
									.append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append(t.getDatosAdicionales().getValue()).append((char) 34)
									.append(csvSepCamp);

							if (!historialTerms) {
								sb.append((char) 34).append(t.getIdClienteAlta().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(t.getDescClienteAlta().getValue()).append((char) 34)
										.append(csvSepCamp);
							}

							if (!historialTerms) {
								sb.append((char) 34);
								if (t.getUltModeloLogueado() != null && t.getUltModeloLogueado().getValue() != null) {
									sb.append(t.getUltModeloLogueado().getValue());
								}
								sb.append((char) 34).append(csvSepCamp);
								sb.append((char) 34);
								if (t.getUltVersionLogueada() != null && t.getUltVersionLogueada().getValue() != null) {
									sb.append(t.getUltVersionLogueada().getValue());
								}
								sb.append((char) 34).append(csvSepCamp);
							}

							if (!historialTerms) {
								sb.append((char) 34).append((t.getFechaUltimoLogin() == null) ? ""
										: ff.format(t.getFechaUltimoLogin().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);
							}

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
						ec.setResponseHeader("Content-Disposition",
								"attachment; filename=\"" + sdf.format(new Date()) + "_("
										+ this.getUsuario().getIdMayorista() + ")_" + "InformeStockTerminales.csv"
										+ "\"");

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
							"Informe Stock Terminales. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Stock Terminales. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe Stock Terminales. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de Stock Terminales: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");

		return;
	}

	public void borrarTerminalDePlataforma(String marcaPOSaBorrar, String imeiABorrar) {
		try {

			Boolean resp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).borrarEnStockTerminal(
					this.getUsuario().getIdMayorista(), marcaPOSaBorrar, imeiABorrar, this.getUsuario().getIdCliente(),
					this.getUsuario().getIdUsuario(), this.getUsuario().getPassword());

			if (resp) {
				// Borrado OK
				this.list = null;
				this.exportToExcel = false;
				informe();
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Terminal borrada exitosamente", null));
				PrimeFaces.current().executeScript(
						"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#9ed5a0', '#a0d4b7', '"
								+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
										.replace("/", "\\/")
								+ "\\/javax.faces.resource\\/images\\/24x24\\/operacionOK.png')");
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo borrar la terminal", null));

			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error borrando la terminal", null));
		}
	}

	public void modificarDatos(ActionEvent ae) {

		try {

			/*
			 * Actualizacion de datos adicionales
			 */
			ResultadoBase resp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.modificarDatosAdicionalesDeTerminal(this.terminalSeleccionado.getIdMayorista().getValue(),
							this.terminalSeleccionado.getImei().getValue(),
							this.terminalSeleccionado.getMarca().getValue(),
							(this.terminalSeleccionado.getDatosAdicionales() == null
									|| this.terminalSeleccionado.getDatosAdicionales().getValue() == null ? ""
											: this.terminalSeleccionado.getDatosAdicionales().getValue()),
							this.terminalSeleccionado.getIdUbicacion().getValue(),
							this.terminalSeleccionado.getImeiSIM().getValue(),
							this.terminalSeleccionado.getValidarImeiTerminalImeiSIM().getValue(),
							this.getUsuario().getIdCliente(), this.getUsuario().getIdUsuario(),
							this.getUsuario().getPassword());

			if (resp.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						resp.getError().getValue().getMsgError().getValue(), null));
				return;
			}

			/*
			 * Actualizar cliente
			 */
			if (this.autoCompleteCliente.getId() == null || this.autoCompleteCliente.getId().compareTo(0L) == 0) {
				/*
				 * Desasocial cliente
				 */
				ResultadoBase rb = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).asociarTerminalAcliente(
						this.getUsuario().getIdMayorista(), this.terminalSeleccionado.getMarca().getValue(),
						this.terminalSeleccionado.getImei().getValue(), null);

				if (rb.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Error: Desasociar - |" + rb.getError().getValue().getCodigoError().getValue()
											+ ", " + rb.getError().getValue().getMsgError().getValue(),
									null));

					return;
				}

			} else {
				/*
				 * Asociar cliente
				 */
				Long idCliAsoc = null;
				idCliAsoc = this.autoCompleteCliente.getId();

				ResultadoBase rb = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).asociarTerminalAcliente(
						this.getUsuario().getIdMayorista(), this.terminalSeleccionado.getMarca().getValue(),
						this.terminalSeleccionado.getImei().getValue(), idCliAsoc);

				if (rb.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Error: Asociar - |" + rb.getError().getValue().getCodigoError().getValue() + ", "
											+ rb.getError().getValue().getMsgError().getValue(),
									null));

					return;
				}

			}

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Dato Adicional y clientes actualizados Correctamente", null));
			PrimeFaces.current()
					.executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#9ed5a0', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/operacionOK.png')");

			GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
			gcFechaHoraDesde.setTime(new Date());
			XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraDesde);

			GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
			gcFechaHoraHasta.setTime(new Date());
			XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraHasta);

			InformeStockTerminales iet = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarListadoStockTerminales(this.getUsuario().getIdMayorista(),
							this.terminalSeleccionado.getImei().getValue(),
							this.terminalSeleccionado.getMarca().getValue(), null, Boolean.FALSE, xmlGCFechaHoraDesde,
							xmlGCFechaHoraHasta, // null,
							null, null, null, this.getUsuario().getIdCliente());

			if (iet != null) {
				for (Terminal terminal : iet.getListStockTerminales().getValue().getTerminal()) {

					this.terminalSeleccionado.getIdUbicacion().setValue(terminal.getIdUbicacion().getValue());
					this.terminalSeleccionado.getImei().setValue(terminal.getImeiSIM().getValue());
					this.terminalSeleccionado.getDatosAdicionales().setValue(terminal.getDatosAdicionales().getValue());
					this.terminalSeleccionado.getValidarImeiTerminalImeiSIM()
							.setValue(terminal.getValidarImeiTerminalImeiSIM().getValue());

					if (terminal.getIdCliente() != null && terminal.getIdCliente().getValue() != null) {
						this.autoCompleteCliente.setCampoBusqueda(terminal.getIdCliente().getValue().toString());

						this.autoCompleteCliente.setId(terminal.getIdCliente().getValue());
						this.terminalSeleccionado.getIdCliente().setValue(terminal.getIdCliente().getValue());
					} else {
						this.autoCompleteCliente.setCampoBusqueda("");
						this.autoCompleteCliente.setId(null);
						this.terminalSeleccionado.getIdCliente().setValue(null);
					}
					if (this.terminalSeleccionado.getRazonSocialCliente() != null
							&& this.terminalSeleccionado.getRazonSocialCliente().getValue() != null) {
						this.autoCompleteCliente
								.setDescripcion(this.terminalSeleccionado.getRazonSocialCliente().getValue());
						this.terminalSeleccionado.getRazonSocialCliente()
								.setValue(this.terminalSeleccionado.getRazonSocialCliente().getValue());
					} else {
						this.autoCompleteCliente.setDescripcion("");
						this.terminalSeleccionado.getRazonSocialCliente().setValue("");
					}

				}
			}

			// Vuelvo a cargar la lista de terminales, para que se actualice la grilla
			this.exportToExcel = false;
			informe();
			PrimeFaces.current().ajax().update("outputPanelTabla");
			PrimeFaces.current().executeScript("ACGSiteScriptsFCNS.resultadoResize();");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS modificarDatosAdicionalesDeTerminales: |" + e.getMessage() + "|", null));

		}
		return;
	}

	public void modActSoft(SelectEvent event) {

		try {

			Long ct_idMayorista = (Long) event.getComponent().getAttributes().get("as_idMayorista");
			String ct_imei = (String) event.getComponent().getAttributes().get("as_imei");
			String ct_marca = (String) event.getComponent().getAttributes().get("as_marca");
			Integer incluirEnActualizacionDeSoft = Integer.parseInt((String) event.getObject());

			ResultadoBase resp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.modificarSTincActSoft(ct_idMayorista, ct_imei, ct_marca, incluirEnActualizacionDeSoft);

			if (resp == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La respuesta del metodo modificarSTincActSoft devolvio null", null));

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
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Valor modificado correctamente", null));
					PrimeFaces.current().executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#9ed5a0', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/operacionOK.png')");

				}

			}

		} catch (Exception e) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Modificando valor", null));

		}

	}

	public void modDepurar(SelectEvent event) {

		try {

			Long ct_idMayorista = (Long) event.getComponent().getAttributes().get("d_idMayorista");
			String ct_imei = (String) event.getComponent().getAttributes().get("d_imei");
			String ct_marca = (String) event.getComponent().getAttributes().get("d_marca");
			Integer depurar = Integer.parseInt((String) event.getObject());

			ResultadoBase resp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.modificarSTdepurar(ct_idMayorista, ct_imei, ct_marca, depurar);

			if (resp == null) {

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La respuesta del metodo modificarSTdepurar devolvio null", null));
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
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Valor modificado correctamente", null));
					PrimeFaces.current().executeScript(
							"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#9ed5a0', '#a0d4b7', '"
									+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
											.replace("/", "\\/")
									+ "\\/javax.faces.resource\\/images\\/24x24\\/operacionOK.png')");

				}

			}

		} catch (Exception e) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Modificando valor", null));

		}

	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public List<SelectItem> getMarcasPOS() {
		return marcasPOS;
	}

	public void setMarcasPOS(List<SelectItem> marcasPOS) {
		this.marcasPOS = marcasPOS;
	}

	public String getMarcaPOS() {
		return marcaPOS;
	}

	public void setMarcaPOS(String marcaPOS) {
		this.marcaPOS = marcaPOS;
	}

	public Long getIdClienteFiltro() {
		return idClienteFiltro;
	}

	public void setIdClienteFiltro(Long idClienteFiltro) {
		this.idClienteFiltro = idClienteFiltro;
	}

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

	public Boolean getHistorialTerms() {
		return historialTerms;
	}

	public void setHistorialTerms(Boolean historialTerms) {
		this.historialTerms = historialTerms;
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

	public Terminal getTerminalSeleccionado() {
		return terminalSeleccionado;
	}

	public void setTerminalSeleccionado(Terminal terminalSeleccionado) {
		this.terminalSeleccionado = terminalSeleccionado;
		if (this.terminalSeleccionado != null) {
			if (this.terminalSeleccionado.getIdCliente() != null
					&& this.terminalSeleccionado.getIdCliente().getValue() != null) {
				this.autoCompleteCliente
						.setCampoBusqueda(this.terminalSeleccionado.getIdCliente().getValue().toString());
				this.autoCompleteCliente.setId(this.terminalSeleccionado.getIdCliente().getValue());
			} else {
				this.autoCompleteCliente.setCampoBusqueda("");
				this.autoCompleteCliente.setId(null);
			}
			if (this.terminalSeleccionado.getRazonSocialCliente() != null
					&& this.terminalSeleccionado.getRazonSocialCliente().getValue() != null) {
				this.autoCompleteCliente.setDescripcion(this.terminalSeleccionado.getRazonSocialCliente().getValue());
			} else {
				this.autoCompleteCliente.setDescripcion("");
			}
		}
	}

	public AutoCompleteClienteView getAutoCompleteCliente() {
		return autoCompleteCliente;
	}

	public void setAutoCompleteCliente(AutoCompleteClienteView autoCompleteCliente) {
		this.autoCompleteCliente = autoCompleteCliente;
	}

}
