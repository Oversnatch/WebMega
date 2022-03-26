package com.americacg.cargavirtual.web.mngbeans.reportes;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.xml.ws.WebServiceException;

import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;

import com.americacg.cargavirtual.gestion.model.ArrayOfCabeceraProducto;
import com.americacg.cargavirtual.gestion.model.ArrayOfInformeComisionesVigentes;
import com.americacg.cargavirtual.gestion.model.CabeceraProducto;
import com.americacg.cargavirtual.gestion.model.InformeComisionesVigentes;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

@Named("reporteComisionesVigentesView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteComisionesVigentesView extends ReporteGeneral<InformeComisionesVigentes> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3235171093649655097L;
	private Long idClienteFiltro;
	private Long idUsuario;
	private Integer tipoFiltroCliente;
	private Boolean incluirSubclientes;
	private Integer nivelJerarquia = 0;
	private Long idProducto;

	private List<SelectItem> nivelesJerarquia;
	private List<SelectItem> productos;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.idClienteFiltro = null;
		this.idProducto = null;
		this.tipoFiltroCliente = 3;
		this.idUsuario = null;
		this.cantRegistros = 0;
		this.nivelJerarquia = 0;
		this.incluirSubclientes = false;

		try {

			this.productos = new ArrayList<SelectItem>();
			ArrayOfCabeceraProducto l = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarCabeceraProductos(this.getUsuario().getIdMayorista(), null, false);
			for (CabeceraProducto cp : l.getCabeceraProducto()) {
				this.productos.add(new SelectItem(cp.getIdProducto().getValue(), cp.getDescProducto().getValue()));
			}
			this.nivelesJerarquia = new ArrayList<SelectItem>();

			int ini = 0;

			if (this.getUsuario().getNivelDistribuidorSuperior() == null) {
				ini = 0;
			} else {
				ini = this.getUsuario().getNivelDistribuidorSuperior();
			}

			Boolean primerIngreso = true;

			for (int i = ini; i < 6; i++) {

				if (primerIngreso) {
					this.nivelesJerarquia.add(new SelectItem(0, i + ""));
				} else {
					this.nivelesJerarquia.add(new SelectItem(i, i + ""));
				}

				primerIngreso = false;

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
					LogACGHelper.escribirLog(null, "Informe Comisiones Vigentes. Error ejecutando el WS de consulta: |"
							+ ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Comisiones Vigentes. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Comisiones Vigentes. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteComisionesVigentesView() {
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

	public void informe() {
		try {

			this.mostrarRegistros = true;
			this.cantRegistros = 0;
			
			// Limpio la tabla
			list = null;

			if (this.getUsuario().getTipoCliente() != null) {
				if ("P".equals(this.getUsuario().getTipoCliente())) {
					// Para el caso del Punto de venta donde no tengo filtro de cliente
					// System.out.println("Inicializo tipoFiltroCliente porque estoy en punto de
					// venta");
					tipoFiltroCliente = 1;
					idClienteFiltro = this.getUsuario().getIdCliente();
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El tipo de Cliente no es valido para la consulta", null));
				return;
			}

			// TODO validar filtros...
			Boolean filtroOK = false;
			if (tipoFiltroCliente == 1) { // Solo cliente
				if (idClienteFiltro != null) {
					filtroOK = true;
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"El idCliente en el filtro no puede ser vacio", null));
				}

			} else {
				if (tipoFiltroCliente == 2) { // Todos los subclientes de
					if (idClienteFiltro != null) {
						filtroOK = true;
					} else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"El idCliente en el filtro no puede ser vacio", null));
					}
				} else {
					if (tipoFiltroCliente == 3) { // Todos los clientes que pertenecen al cliente logueado
						if (idClienteFiltro == null) {
							filtroOK = true;
						} else {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_ERROR, "El idCliente en el filtro debe ser vacio", null));
						}
					} else {
						// tipo de Filtro Incorrecto
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"El tipo de filtro seleccionado es incorrecto", null));
					}
				}
			}

			if (filtroOK) {

				// Indica la cantidad de registros que voy a usar en el limit de la query a la
				// base de datos
				Integer cantRegistrosFiltro = null;
				if (exportToExcel) {
					cantRegistrosFiltro = cantMaxRegistrosAexportar;
				} else {
					cantRegistrosFiltro = cantMaxRegistrosAmostrarPorPantalla;
				}

				ArrayOfInformeComisionesVigentes listaux = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.informeComisionesVigentes(this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
								this.getUsuario().getIdDistribuidor(), tipoFiltroCliente, idClienteFiltro,
								nivelJerarquia, this.getUsuario().getTipoCliente(), idProducto, cantRegistrosFiltro,
								incluirSubclientes);

				// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
				mostrarArchivoCSV = false;

				String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
				String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

				StringBuilder sb = new StringBuilder();

				if (listaux == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"La respuesta del informe de comisiones vigentes es null", null));
					// TODO: NO SE OBTIENE EL ERROR ACA, ver que se hace
					/*
					 * }else if (listaux.getError().getValue().getHayError().getValue()){ //TODO Ver
					 * porque no entra en esta condicion. No reconoce el Error
					 * FacesContext.getCurrentInstance().addMessage(null, new
					 * FacesMessage(FacesMessage.SEVERITY_ERROR,
					 * listaux.getError().getValue().getMsgError().getValue(), null));
					 */
				} else {
					if (listaux.getInformeComisionesVigentes().isEmpty()) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"No existe información para la consulta realizada.", null));
					} else {
						list = listaux.getInformeComisionesVigentes();
						cantRegistros = list.size();
						Integer i = 0;

						// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {
							if (i == 0) {
								mostrarArchivoCSV = true;
							}
							// Header

							sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);
							if (nivelJerarquia >= 1 && this.getUsuario().getNivelDistribuidorSuperior() < 1) {
								sb.append((char) 34).append("ID Distribuidor 1").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Distribuidor 1").append((char) 34).append(csvSepCamp);
							}

							if (nivelJerarquia >= 2 && this.getUsuario().getNivelDistribuidorSuperior() < 2) {
								sb.append((char) 34).append("ID Distribuidor 2").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Distribuidor 2").append((char) 34).append(csvSepCamp);
							}

							if (nivelJerarquia >= 3 && this.getUsuario().getNivelDistribuidorSuperior() < 3) {
								sb.append((char) 34).append("ID Distribuidor 3").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Distribuidor 3").append((char) 34).append(csvSepCamp);
							}

							if (nivelJerarquia >= 4 && this.getUsuario().getNivelDistribuidorSuperior() < 4) {
								sb.append((char) 34).append("ID Distribuidor 4").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Distribuidor 4").append((char) 34).append(csvSepCamp);
							}

							if (nivelJerarquia >= 5 && this.getUsuario().getNivelDistribuidorSuperior() < 5) {
								sb.append((char) 34).append("ID Distribuidor 5").append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append("Distribuidor 5").append((char) 34).append(csvSepCamp);
							}
							sb.append((char) 34).append("Cond. IVA Cliente").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Producto").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Producto").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("ID Lista").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Lista").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Fecha Desde").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("F. Pago Com").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Comis. s/IVA").append((char) 34).append(csvSepCamp);

							sb.append((char) 13).append((char) 10);

							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

							for (InformeComisionesVigentes it : list) {
								// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV

								sb.append((char) 34).append(it.getIdCliente().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getRazonSocial().getValue()).append((char) 34)
										.append(csvSepCamp);
								if (nivelJerarquia >= 1 && this.getUsuario().getNivelDistribuidorSuperior() < 1) {
									sb.append((char) 34)
											.append((it.getIdDistribuidor1().getValue() != null
													&& it.getIdDistribuidor1().getValue() != 0)
															? it.getIdDistribuidor1().getValue()
															: "")
											.append((char) 34).append(csvSepCamp);
									sb.append((char) 34)
											.append((it.getDistribuidor1().getValue() != null)
													? it.getDistribuidor1().getValue()
													: "")
											.append((char) 34).append(csvSepCamp);
								}

								if (nivelJerarquia >= 2 && this.getUsuario().getNivelDistribuidorSuperior() < 2) {
									sb.append((char) 34)
											.append((it.getIdDistribuidor2().getValue() != null
													&& it.getIdDistribuidor2().getValue() != 0)
															? it.getIdDistribuidor2().getValue()
															: "")
											.append((char) 34).append(csvSepCamp);
									sb.append((char) 34)
											.append((it.getDistribuidor2().getValue() != null)
													? it.getDistribuidor2().getValue()
													: "")
											.append((char) 34).append(csvSepCamp);
								}

								if (nivelJerarquia >= 3 && this.getUsuario().getNivelDistribuidorSuperior() < 3) {
									sb.append((char) 34)
											.append((it.getIdDistribuidor3().getValue() != null
													&& it.getIdDistribuidor3().getValue() != 0)
															? it.getIdDistribuidor3().getValue()
															: "")
											.append((char) 34).append(csvSepCamp);
									sb.append((char) 34)
											.append((it.getDistribuidor3().getValue() != null)
													? it.getDistribuidor3().getValue()
													: "")
											.append((char) 34).append(csvSepCamp);
								}

								if (nivelJerarquia >= 4 && this.getUsuario().getNivelDistribuidorSuperior() < 4) {
									sb.append((char) 34)
											.append((it.getIdDistribuidor4().getValue() != null
													&& it.getIdDistribuidor4().getValue() != 0)
															? it.getIdDistribuidor4().getValue()
															: "")
											.append((char) 34).append(csvSepCamp);
									sb.append((char) 34)
											.append((it.getDistribuidor4().getValue() != null)
													? it.getDistribuidor4().getValue()
													: "")
											.append((char) 34).append(csvSepCamp);
								}

								if (nivelJerarquia >= 5 && this.getUsuario().getNivelDistribuidorSuperior() < 5) {
									sb.append((char) 34)
											.append((it.getIdDistribuidor5().getValue() != null
													&& it.getIdDistribuidor5().getValue() != 0)
															? it.getIdDistribuidor5().getValue()
															: "")
											.append((char) 34).append(csvSepCamp);
									sb.append((char) 34)
											.append((it.getDistribuidor5().getValue() != null)
													? it.getDistribuidor5().getValue()
													: "")
											.append((char) 34).append(csvSepCamp);
								}
								sb.append((char) 34).append(it.getDescripcion().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getIdProducto().getValue()).append((char) 34)
										.append(csvSepCamp);
								sb.append((char) 34).append(it.getDescProducto().getValue()).append((char) 34)
										.append(csvSepCamp);
								if (it.getIdLista().getValue() != null && it.getIdLista().getValue() > 0) {
									sb.append((char) 34).append(it.getIdLista().getValue()).append((char) 34)
											.append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}
								if (it.getLista().getValue() != null && !"".equals(it.getLista().getValue())) {
									sb.append((char) 34).append(it.getLista().getValue()).append((char) 34)
											.append(csvSepCamp);
								} else {
									sb.append((char) 34).append((char) 34).append(csvSepCamp);
								}
								sb.append((char) 34)
										.append((it.getFechaDesde().toString() == null
												|| "".equals(it.getFechaDesde().toString()))
														? ""
														: format.format(
																it.getFechaDesde().toGregorianCalendar().getTime()))
										.append((char) 34).append(csvSepCamp);
								
								sb.append((char) 34)
								.append(it.getFormaPagoComision().getValue()
										.replace(".", csvSepDec).replace(",", csvSepDec) )
								.append((char) 34).append(csvSepCamp);
								
								
								sb.append((char) 34)
								.append(ACGFormatHelper.format(it.getPorcentajecomision().getValue(), csvSepDec))
								.append((char) 34).append(csvSepCamp);

								sb.append((char) 13).append((char) 10);
							}

							FacesContext fc = FacesContext.getCurrentInstance();
							ExternalContext ec = fc.getExternalContext();

							ec.responseReset();
							ec.setResponseContentType("text/plain");
							ec.setResponseContentLength(sb.toString().length());
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
							ec.setResponseHeader("Content-Disposition",
									"attachment; filename=\"" + sdf.format(new Date()) + "_("
											+ this.getUsuario().getIdMayorista() + ")_"
											+ "InformeComisionesVigentes.csv" + "\"");

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
			if(ste != null && ste.getCause() != null && ste.getCause().getClass() != null) {
				if (ste.getCause().getClass().equals(ConnectException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo establecer la comunicación (GST-TOC).\n Por favor intente nuevamente.", null));
				}else if (ste.getCause().getClass().equals(SocketTimeoutException.class)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo establecer la comunicación (GST-TRW).\n Por favor intente nuevamente.", null));
				} else if (ste.getCause().getClass().equals(UnknownHostException.class)) {
					LogACGHelper.escribirLog(null, "No se pudo establecer la comunicación (GST-HNC): |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se pudo establecer la comunicación (GST-HNC).\n Por favor intente nuevamente.", null));					
				}else {
					LogACGHelper.escribirLog(null, "Informe Comisiones Vigentes. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			}else {
				LogACGHelper.escribirLog(null, "Informe Comisiones Vigentes. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Informe Comsiones Vigentes. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");	
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Error ejecutando el WS de consulta de Comisiones Vigentes: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;			
	}	

	public void limpiarIncluirSubclientes(ActionEvent actionEvent) {
		try {
			this.incluirSubclientes = false;
			this.idClienteFiltro = null;
		} catch (Exception e) {
		}
	}

	public Long getIdClienteFiltro() {
		return idClienteFiltro;
	}

	public void setIdClienteFiltro(Long idClienteFiltro) {
		this.idClienteFiltro = idClienteFiltro;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public List<SelectItem> getProductos() {
		return productos;
	}

	public void setProductos(List<SelectItem> productos) {
		this.productos = productos;
	}

	public Integer getTipoFiltroCliente() {
		return tipoFiltroCliente;
	}

	public void setTipoFiltroCliente(Integer tipoFiltroCliente) {
		this.tipoFiltroCliente = tipoFiltroCliente;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public List<SelectItem> getNivelesJerarquia() {
		return nivelesJerarquia;
	}

	public void setNivelesJerarquia(List<SelectItem> nivelesJerarquia) {
		this.nivelesJerarquia = nivelesJerarquia;
	}

	public Integer getNivelJerarquia() {
		return nivelJerarquia;
	}

	public void setNivelJerarquia(Integer nivelJerarquia) {
		this.nivelJerarquia = nivelJerarquia;
	}

	public Boolean getIncluirSubclientes() {
		return incluirSubclientes;
	}

	public void setIncluirSubclientes(Boolean incluirSubclientes) {
		this.incluirSubclientes = incluirSubclientes;
	}
}
