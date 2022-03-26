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
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gestion.model.Descripcion;
import com.americacg.cargavirtual.gestion.model.DescripcionContainer;
import com.americacg.cargavirtual.gestion.model.InformeStockSims;
import com.americacg.cargavirtual.gestion.model.ResultadoBase;
import com.americacg.cargavirtual.gestion.model.SIM;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;
import com.americacg.cargavirtual.web.shared.AutoCompleteClienteView;

@Named("reporteStockSimsView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteStockSimsView extends ReporteGeneral<SIM> {

	private static final long serialVersionUID = -4512644792106068569L;

	private List<SelectItem> operadores;
	private String imeiSIM;
	private Long idOperador;
	private Long idClienteFiltro;
	private List<SelectItem> proveedoresSIM;
	private Long idProveedorSIM;
	private Boolean historialSims;
	private String leyendaAdicionalEncabezado;
	private Long id_accion;
	private List<SelectItem> acciones;
	private Long idUbicacion;
	private List<SelectItem> ubicacionesSIM;
	private AutoCompleteClienteView autoCompleteCliente = new AutoCompleteClienteView();
	private SIM simSeleccionado = null;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();

		this.list = null;
		this.imeiSIM = null;
		this.idOperador = null;
		this.idClienteFiltro = null;
		this.cantRegistros = 0;
		this.idProveedorSIM = null;
		this.historialSims = false;
		this.leyendaAdicionalEncabezado = "";
		this.id_accion = null;
		this.idUbicacion = null;

		try {

			this.operadores = new ArrayList<SelectItem>();
			DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarOperadores(this.getUsuario().getIdMayorista(), null);

			for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
				if (d.getDescripcion() != null) {
					if (d.getDescripcion().getValue().length() > 0) {
						this.operadores.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
					}
				}
			}
			this.proveedoresSIM = new ArrayList<SelectItem>();
			DescripcionContainer p = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarProveedoresDeSim(this.getUsuario().getIdMayorista(), null);

			for (Descripcion d : p.getListDescripcion().getValue().getDescripcion()) {
				if (d.getDescripcion().getValue() != null) {
					if (d.getDescripcion().getValue().length() > 0) {
						this.proveedoresSIM.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
					}
				}
			}
			this.acciones = new ArrayList<SelectItem>();
			DescripcionContainer a = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarStockSimsAcciones(null, null);

			for (Descripcion d : a.getListDescripcion().getValue().getDescripcion()) {
				if (d.getDescripcion().getValue() != null) {
					if (d.getDescripcion().getValue().length() > 0) {
						this.acciones.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
					}
				}
			}
			this.ubicacionesSIM = new ArrayList<SelectItem>();
			DescripcionContainer u = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarSimsUbicacion(null, null);

			for (Descripcion d : u.getListDescripcion().getValue().getDescripcion()) {
				if (d.getDescripcion().getValue() != null) {
					if (d.getDescripcion().getValue().length() > 0) {
						this.ubicacionesSIM.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
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
							"Informe Stock SIMs. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Stock SIMs. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Stock SIMs. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteStockSimsView() {
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

	public void checkHistorialSims(ActionEvent ae) {
		this.inicializarFechas();
		this.list = null;
	}

	private void informe() {
		try {
			this.mostrarRegistros = true;
			cantRegistros = 0;
			if (historialSims) {
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

			InformeStockSims ist = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).mostrarListadoStockSims(
					this.getUsuario().getIdMayorista(), imeiSIM, idOperador, idClienteFiltro, idProveedorSIM,
					historialSims, xmlGCFechaHoraDesde, xmlGCFechaHoraHasta, id_accion, idUbicacion,
					this.getUsuario().getIdCliente());

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
				this.list = ist.getListStockSims().getValue().getSIM();

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

						sb.append((char) 34).append("ID Operador").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Operador").append((char) 34).append(csvSepCamp);

						sb.append((char) 34).append("IMEI").append((char) 34).append(csvSepCamp);

						if (!historialSims) {
							sb.append((char) 34).append("Fecha Alta").append((char) 34).append(csvSepCamp);
						}

						sb.append((char) 34).append("ID Ubicacion").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Ubicacion").append((char) 34).append(csvSepCamp);

						if (historialSims) {
							sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
						}

						if (historialSims) {
							sb.append((char) 34).append("ID Accion").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Accion").append((char) 34).append(csvSepCamp);
						}

						sb.append((char) 34).append("ID Proveedor de SIM").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Proveedor de SIM").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);

						if (!historialSims) {
							sb.append((char) 34).append("Fecha Ult. Asoc Cliente").append((char) 34).append(csvSepCamp);
						}

						sb.append((char) 34).append("Datos Adicionales").append((char) 34).append(csvSepCamp);

						if (!historialSims) {
							sb.append((char) 34).append("ID Cliente que dio de Alta").append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("Cliente que dio de Alta").append((char) 34).append(csvSepCamp);
						}

						sb.append((char) 13).append((char) 10);
					}

					// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
					SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
					// EXPORTAR EN CASO DE SER NECESARIO
					for (SIM sim : list) {

						// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {

							if (i == 0) {
								mostrarArchivoCSV = true;
							}

							sb.append((char) 34).append(sim.getIdOperador().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(sim.getDescOperador().getValue()).append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 34).append("'").append(sim.getImeiSIM().getValue()).append((char) 34)
									.append(csvSepCamp);

							if (!historialSims) {
								sb.append((char) 34).append((sim.getFechaAlta() == null) ? ""
										: ff.format(sim.getFechaAlta().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append(sim.getIdUbicacion().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(sim.getDescUbicacion().getValue()).append((char) 34)
									.append(csvSepCamp);

							if (historialSims) {
								sb.append((char) 34).append((sim.getFechaHistorial() == null) ? ""
										: ff.format(sim.getFechaHistorial().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);
							}

							if (historialSims) {
								sb.append((char) 34).append(sim.getIdAccionHistorial().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(sim.getDescAccionHistorial().getValue()).append((char) 34)
										.append(csvSepCamp);
							}

							sb.append((char) 34).append(sim.getIdProveedorDeSims().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(sim.getDescProveedorDeSims().getValue()).append((char) 34)
									.append(csvSepCamp);

							sb.append((char) 34);
							if (sim.getIdCliente() != null && sim.getIdCliente().getValue() != null) {
								sb.append(sim.getIdCliente().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);
							sb.append((char) 34);
							if (sim.getRazonSocialCliente() != null && sim.getRazonSocialCliente().getValue() != null) {
								sb.append(sim.getRazonSocialCliente().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);

							if (!historialSims) {
								sb.append((char) 34)
										.append((sim.getFechaUltAsocCliente() == null) ? ""
												: ff.format(
														sim.getFechaUltAsocCliente().toGregorianCalendar().getTime())
														.toString())
										.append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append(sim.getDatosAdicionalesSIM().getValue()).append((char) 34)
									.append(csvSepCamp);

							if (!historialSims) {
								sb.append((char) 34);
								if (sim.getIdClienteAlta() != null && sim.getIdClienteAlta().getValue() != null) {
									sb.append(sim.getIdClienteAlta().getValue());
								}
								sb.append((char) 34).append(csvSepCamp);
								sb.append((char) 34);
								if (sim.getDescClienteAlta() != null && sim.getDescClienteAlta().getValue() != null) {
									sb.append(sim.getDescClienteAlta().getValue());
								}
								sb.append((char) 34).append(csvSepCamp);
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
										+ this.getUsuario().getIdMayorista() + ")_" + "InformeHistoricoStockSims.csv"
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
							"Informe Stock Sims. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Stock Sims. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe Stock Sims. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de Stock Sims: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
	}

	public void borrarSimDePlataforma(String imeiSIMABorrar, Long idOperadorABorrar) {

		try {

			Boolean resp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).borrarEnStockSim(
					this.getUsuario().getIdMayorista(), idOperadorABorrar, imeiSIMABorrar,
					this.getUsuario().getIdCliente(), this.getUsuario().getIdUsuario(),
					this.getUsuario().getPassword());

			if (resp) {
				// Borrado OK
				this.list = null;
				this.exportToExcel = false;
				informe();
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "SIM borrada exitosamente", null));
				PrimeFaces.current().executeScript(
						"ACGSiteScriptsFCNS.cambiaFondoMsg('msgsProductos', '#212121', '#9ed5a0', '#a0d4b7', '"
								+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
										.replace("/", "\\/")
								+ "\\/javax.faces.resource\\/images\\/24x24\\/operacionOK.png')");
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo borrar el SIM", null));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error borrando el SIM", null));
		}
	}

	public List<SelectItem> getOperadores() {
		return operadores;
	}

	public void modificarDatos(ActionEvent ae) {
		try {
			if (!(this.simSeleccionado.getIdOperador() != null
					&& this.simSeleccionado.getIdOperador().getValue() != null
					&& this.simSeleccionado.getIdOperador().getValue().compareTo(0L) != 0
					&& this.simSeleccionado.getImeiSIM() != null && this.simSeleccionado.getImeiSIM().getValue() != null
					&& !"".equals(this.simSeleccionado.getImeiSIM().getValue()))) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El operador y el imei del SIM no pueden ser nulos ni vacios", null));
				return;
			}

			/*
			 * Actualizo datos adicionales
			 */
			if (this.simSeleccionado.getIdProveedorDeSims() != null
					&& this.simSeleccionado.getIdProveedorDeSims().getValue() != null
					&& this.simSeleccionado.getIdProveedorDeSims().getValue().compareTo(0L) == 0) {
				this.simSeleccionado.getIdProveedorDeSims().setValue(null);
			}

			Boolean resp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).modificarDatosAdicionalesDeSIM(
					this.simSeleccionado.getIdMayorista().getValue(), this.simSeleccionado.getImeiSIM().getValue(),
					this.simSeleccionado.getIdOperador().getValue(),
					(this.simSeleccionado.getDatosAdicionalesSIM() != null && this.simSeleccionado.getDatosAdicionalesSIM().getValue() != null ? this.simSeleccionado.getDatosAdicionalesSIM().getValue() : ""),
					this.simSeleccionado.getIdProveedorDeSims().getValue(),
					this.simSeleccionado.getIdUbicacion().getValue(), this.getUsuario().getIdCliente(),
					this.getUsuario().getIdUsuario(), this.getUsuario().getPassword());

			if (!resp) {

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Los Datos Adicionales no fueron modificados", null));
				return;
			}

			/*
			 * Asocia o Desasocia al cliente
			 */

			if ((this.autoCompleteCliente.getId() == null) || (this.autoCompleteCliente.getId().compareTo(0L) == 0)) {
				/*
				 * Desasociar cliente
				 */

				ResultadoBase rb = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).asociarSIMAcliente(
						this.getUsuario().getIdMayorista(), this.simSeleccionado.getIdOperador().getValue(),
						this.simSeleccionado.getImeiSIM().getValue(), null);

				if (rb.getError().getValue().getHayError().getValue()) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Error: |" + rb.getError().getValue().getCodigoError().getValue() + ", "
											+ rb.getError().getValue().getMsgError().getValue(),
									null));
					return;
				}

			} else {
				/*
				 * Asociar cliente
				 */
				try {

					Long idCliAsoc = null;
					try {
						idCliAsoc = this.autoCompleteCliente.getId();
					} catch (Exception e) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"El idCliente no es de tipo Long: |" + this.autoCompleteCliente.getId() + "|", null));
						return;
					}

					ResultadoBase rb = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).asociarSIMAcliente(
							this.getUsuario().getIdMayorista(), this.simSeleccionado.getIdOperador().getValue(),
							this.simSeleccionado.getImeiSIM().getValue(), idCliAsoc);

					if (rb.getError().getValue().getHayError().getValue()) {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"Error: |" + rb.getError().getValue().getCodigoError().getValue() + ", "
												+ rb.getError().getValue().getMsgError().getValue(),
										null));
						return;
					}
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS asociarSIMAcliente: |" + e.getMessage() + "|", null));
					return;
				}
			}

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Dato Adicional y actualizacion de cliente aplicados correctamente", null));
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

			InformeStockSims ist = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).mostrarListadoStockSims(
					this.getUsuario().getIdMayorista(), imeiSIM, idOperador, null, null, Boolean.FALSE,
					xmlGCFechaHoraDesde, xmlGCFechaHoraHasta, null, null, this.getUsuario().getIdCliente());

			if (ist != null) {
				for (SIM sim : ist.getListStockSims().getValue().getSIM()) {

					this.simSeleccionado.getIdProveedorDeSims().setValue(sim.getIdProveedorDeSims().getValue());
					this.simSeleccionado.getIdUbicacion().setValue(sim.getIdUbicacion().getValue());
					this.simSeleccionado.getDatosAdicionalesSIM().setValue(sim.getDatosAdicionalesSIM().getValue());

					this.autoCompleteCliente.setId(sim.getIdCliente().getValue());
					this.autoCompleteCliente.setDescripcion(sim.getRazonSocialCliente().getValue());
				}
			}

			// Vuelvo a cargar la lista de terminales, para que se actualice la grilla
			this.exportToExcel = false;
			informe();
			PrimeFaces.current().ajax().update("outputPanelTabla");
			PrimeFaces.current().executeScript("ACGSiteScriptsFCNS.resultadoResize();");

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS modificarDatosAdicionalesSIM: |" + e.getMessage() + "|", null));
		}
		return;
	}

	public Long getIdClienteFiltro() {
		return idClienteFiltro;
	}

	public void setIdClienteFiltro(Long idClienteFiltro) {
		this.idClienteFiltro = idClienteFiltro;
	}

	public void setOperadores(List<SelectItem> operadores) {
		this.operadores = operadores;
	}

	public String getImeiSIM() {
		return imeiSIM;
	}

	public void setImeiSIM(String imeiSIM) {
		this.imeiSIM = imeiSIM;
	}

	public Long getIdOperador() {
		return idOperador;
	}

	public void setIdOperador(Long idOperador) {
		this.idOperador = idOperador;
	}

	public List<SelectItem> getProveedoresSIM() {
		return proveedoresSIM;
	}

	public void setProveedoresSIM(List<SelectItem> proveedoresSIM) {
		this.proveedoresSIM = proveedoresSIM;
	}

	public Long getIdProveedorSIM() {
		return idProveedorSIM;
	}

	public void setIdProveedorSIM(Long idProveedorSIM) {
		this.idProveedorSIM = idProveedorSIM;
	}

	public Boolean getHistorialSims() {
		return historialSims;
	}

	public void setHistorialSims(Boolean historialSims) {
		this.historialSims = historialSims;
	}

	public String getLeyendaAdicionalEncabezado() {
		return leyendaAdicionalEncabezado;
	}

	public void setLeyendaAdicionalEncabezado(String leyendaAdicionalEncabezado) {
		this.leyendaAdicionalEncabezado = leyendaAdicionalEncabezado;
	}

	public List<SelectItem> getAcciones() {
		return acciones;
	}

	public void setAcciones(List<SelectItem> acciones) {
		this.acciones = acciones;
	}

	public Long getId_accion() {
		return id_accion;
	}

	public void setId_accion(Long id_accion) {
		this.id_accion = id_accion;
	}

	public Long getIdUbicacion() {
		return idUbicacion;
	}

	public void setIdUbicacion(Long idUbicacion) {
		this.idUbicacion = idUbicacion;
	}

	public List<SelectItem> getUbicacionesSIM() {
		return ubicacionesSIM;
	}

	public void setUbicacionesSIM(List<SelectItem> ubicacionesSIM) {
		this.ubicacionesSIM = ubicacionesSIM;
	}

	public AutoCompleteClienteView getAutoCompleteCliente() {
		return autoCompleteCliente;
	}

	public void setAutoCompleteCliente(AutoCompleteClienteView autoCompleteCliente) {
		this.autoCompleteCliente = autoCompleteCliente;
	}

	public SIM getSimSeleccionado() {
		return simSeleccionado;
	}

	public void setSimSeleccionado(SIM simSeleccionado) {
		this.simSeleccionado = simSeleccionado;
		if (this.simSeleccionado != null) {
			if (this.simSeleccionado.getIdCliente() != null && this.simSeleccionado.getIdCliente().getValue() != null) {
				this.autoCompleteCliente.setCampoBusqueda(this.simSeleccionado.getIdCliente().getValue().toString());
				this.autoCompleteCliente.setId(this.simSeleccionado.getIdCliente().getValue());
			} else {
				this.autoCompleteCliente.setCampoBusqueda("");
				this.autoCompleteCliente.setId(null);
			}
			if (this.simSeleccionado.getRazonSocialCliente() != null
					&& this.simSeleccionado.getRazonSocialCliente().getValue() != null) {
				this.autoCompleteCliente.setDescripcion(this.simSeleccionado.getRazonSocialCliente().getValue());
			} else {
				this.autoCompleteCliente.setDescripcion("");
			}
		}
	}

}
