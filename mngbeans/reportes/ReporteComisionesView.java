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
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;

import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;

import com.americacg.cargavirtual.gestion.model.ArrayOfCabeceraProducto;
import com.americacg.cargavirtual.gestion.model.ArrayOfInformeComisiones;
import com.americacg.cargavirtual.gestion.model.ArrayOfSaldoProveedor;
import com.americacg.cargavirtual.gestion.model.CabeceraProducto;
import com.americacg.cargavirtual.gestion.model.Descripcion;
import com.americacg.cargavirtual.gestion.model.DescripcionContainer;
import com.americacg.cargavirtual.gestion.model.InformeComisiones;
import com.americacg.cargavirtual.gestion.model.SaldoProveedor;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@Named("reporteComisionesView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteComisionesView extends ReporteGeneral<InformeComisiones> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1792029116724669836L;

	private Long idClienteInferior;

	private Integer tipoFiltroCliente;

	private Float totalMonto = 0F;
	private Float totalComisiones = 0F;
	private Float totalComisionesConIVA = 0F;
	private Long totalTrx = 0L;
	private Long idProveedor;
	private Long idProducto;
	private List<SelectItem> productos;
	private List<SelectItem> proveedores;
	private Boolean agrupar;

	private Long idEstadoComision;
	private List<SelectItem> estadosComisiones;

	private Boolean infHistorico;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.totalComisiones = 0F;
		this.totalComisionesConIVA = 0F;
		this.totalMonto = 0F;
		this.totalTrx = 0L;
		this.idClienteInferior = null;
		this.idProveedor = null;
		this.idProducto = null;
		this.agrupar = false;
		this.tipoFiltroCliente = 3;
		this.cantRegistros = 0;
		this.idEstadoComision = -1L;
		this.infHistorico = null;

		try {
			this.estadosComisiones = new ArrayList<SelectItem>();
			DescripcionContainer cd = GestionServiceHelper.getGestionService(CfgTimeout.CONSULTA)
					.mostrarEstadosComisiones(this.getUsuario().getIdMayorista(), null);

			for (Descripcion d : cd.getListDescripcion().getValue().getDescripcion()) {
				this.estadosComisiones.add(new SelectItem(d.getId().getValue(), d.getDescripcion().getValue()));
			}
			this.proveedores = new ArrayList<SelectItem>();
			ArrayOfSaldoProveedor lp = GestionServiceHelper.getGestionService(CfgTimeout.CONSULTA)
					.obtenerSaldoProveedores(this.getUsuario().getIdMayorista(), null, false, false);
			for (SaldoProveedor sp : lp.getSaldoProveedor()) {
				this.proveedores.add(new SelectItem(sp.getIdProveedor().getValue(), sp.getRazonSocial().getValue()));
			}
			this.productos = new ArrayList<SelectItem>();
			ArrayOfCabeceraProducto l = GestionServiceHelper.getGestionService(CfgTimeout.CONSULTA)
					.mostrarCabeceraProductos(this.getUsuario().getIdMayorista(), null, false);
			for (CabeceraProducto cp : l.getCabeceraProducto()) {
				this.productos.add(new SelectItem(cp.getIdProducto().getValue(), cp.getDescProducto().getValue()));
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
							"Informe Comisiones. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Comisiones. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Comisiones. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteComisionesView() {
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
			this.cantRegistros = 0;
			this.totalMonto = 0F;
			this.totalComisiones = 0F;
			this.totalComisionesConIVA = 0F;
			this.totalTrx = 0L;

			this.mostrarRegistros = true;

			// Limpio la tabla
			list = null;

			// Indica la cantidad de registros que voy a usar en el limit de la query a la
			// base de datos
			Integer cantRegistrosFiltro = null;
			if (exportToExcel) {
				cantRegistrosFiltro = cantMaxRegistrosAexportar;
			} else {
				cantRegistrosFiltro = cantMaxRegistrosAmostrarPorPantalla;
			}

			if (!validaFechas()) {
				return;
			}

			if (this.getUsuario().getTipoCliente() != null) {
				if ("P".equals(this.getUsuario().getTipoCliente())) {
					// Para el caso del Punto de venta donde no tengo filtro de cliente
					// System.out.println("Inicializo tipoFiltroCliente porque estoy en punto de
					// venta");
					tipoFiltroCliente = 1;
					idClienteInferior = this.getUsuario().getIdCliente();
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El tipo de Cliente no es valido para la consulta", null));
				return;
			}

			// TODO validar filtros...
			Boolean filtroOK = false;
			if (tipoFiltroCliente == 1) { // Solo cliente
				if (idClienteInferior != null) {
					filtroOK = true;
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"El idCliente en el filtro no puede ser vacio", null));
				}

			} else {
				if (tipoFiltroCliente == 2) { // Todos los subclientes de
					if (idClienteInferior != null) {
						filtroOK = true;
					} else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"El idCliente en el filtro no puede ser vacio", null));
					}
				} else {
					if (tipoFiltroCliente == 3) { // Todos los clientes que pertenecen al cliente logueado
						if (idClienteInferior == null) {
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



				if (idEstadoComision == -1) {
					// Null indica que se deben buscar todas las comisiones (Pendientes, Aprobadadas
					// y Rechazadas)
					idEstadoComision = null;
				}

				GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
				gcFechaHoraDesde.setTime(fechaHoraDesde);
				XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gcFechaHoraDesde);

				GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
				gcFechaHoraHasta.setTime(fechaHoraHasta);
				XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gcFechaHoraHasta);

				ArrayOfInformeComisiones laux = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
						.informeComisiones(this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(),
								xmlGCFechaHoraDesde, xmlGCFechaHoraHasta,
								this.getUsuario().getNivelDistribuidorSuperior(), this.idClienteInferior, idProveedor,
								idProducto, agrupar, tipoFiltroCliente, idEstadoComision, infHistorico,
								cantRegistrosFiltro);

				// TODO: el arrayList no tiene mas el error, que hacemos!!!!!!!!
				/*
				 * if (laux.getInformeComisiones().get cantRegistrosFiltro.get
				 * .getInformeComisiones() .getInformeComisiones().get
				 * .get(index).getError().getValue().getHayError().getValue()){
				 * FacesContext.getCurrentInstance().addMessage(null, new
				 * FacesMessage(FacesMessage.SEVERITY_ERROR,
				 * laux.getError().getValue().getMsgError().getValue(), null)); }else{
				 */
				list = laux.getInformeComisiones();

				// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
				mostrarArchivoCSV = false;

				String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
				String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

				StringBuilder sb = new StringBuilder();

				if (list == null || list.isEmpty()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No existe información para la consulta realizada.", null));
				} else {
					cantRegistros = list.size();

					// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
					if (this.exportToExcel) {
						if (!agrupar) {
							sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
						}

						if (agrupar) {
							sb.append((char) 34).append("Fecha Desde").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Fecha Hasta").append((char) 34).append(csvSepCamp);
						}

						sb.append((char) 34).append("ID Cliente Dist").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Cliente Dist").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Cond. IVA").append((char) 34).append(csvSepCamp);

						if ("M".equals(this.getUsuario().getTipoCliente())) {
							sb.append((char) 34).append("ID Proveedor").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Proveedor").append((char) 34).append(csvSepCamp);
						}

						sb.append((char) 34).append("ID Producto").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Producto").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Cant Trx").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Total").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("F.Pago Com").append((char) 34).append(csvSepCamp);

						if (!"P".equals(this.getUsuario().getTipoCliente())) {
							sb.append((char) 34).append("% / imp C.s/IVA").append((char) 34).append(csvSepCamp);
						}

						sb.append((char) 34).append("$ Comis.s/IVA").append((char) 34).append(csvSepCamp);

						if ((!"P".equals(this.getUsuario().getTipoCliente()))
								&& (this.getUsuario().getInfComMostrarComConIVA())) {
							sb.append((char) 34).append("% / imp C.c/IVA").append((char) 34).append(csvSepCamp);
						}

						if (this.getUsuario().getInfComMostrarComConIVA()) {
							sb.append((char) 34).append("$ Comis.c/IVA").append((char) 34).append(csvSepCamp);
						}

						if (!agrupar) {
							sb.append((char) 34).append("ID Estado").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Estado").append((char) 34);
						}

						sb.append((char) 13).append((char) 10);
					}

					// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
					SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd");

					// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
					// EXPORTAR EN CASO DE SER NECESARIO
					Integer i = 0;
					for (InformeComisiones ic : list) {

						totalMonto += ic.getMontoTotal().getValue();
						totalTrx += ic.getCantidadTransacciones().getValue();
						totalComisiones += ic.getComisionCalculada().getValue();
						totalComisionesConIVA += ic.getComisionCalculadaConIVA().getValue();

						// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {

							if (i == 0) {
								mostrarArchivoCSV = true;
							}

							if (!agrupar) {
								sb.append((char) 34)
										.append(ff.format(ic.getFecha().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);
							}

							if (agrupar) {
								sb.append((char) 34).append(
										ff.format(ic.getFechaDesde().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(
										ff.format(ic.getFechaHasta().toGregorianCalendar().getTime()).toString())
										.append((char) 34).append(csvSepCamp);

							}

							sb.append((char) 34).append(ic.getIdClienteClienteDist().getValue()).append((char) 34)
									.append(csvSepCamp);
							
							sb.append((char) 34);
							if(ic.getRazonSocialClienteDist() != null && ic.getRazonSocialClienteDist().getValue() != null) {
								sb.append( ic.getRazonSocialClienteDist().getValue());
							}
							sb.append((char) 34).append(csvSepCamp);
							
							sb.append((char) 34).append(ic.getIdCliente().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ic.getRazonSocial().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ic.getDescripcionTipoIVA().getValue()).append((char) 34)
									.append(csvSepCamp);

							if ("M".equals(this.getUsuario().getTipoCliente())) {
								sb.append((char) 34).append(ic.getIdProveedor().getValue()).append((char) 34)
										.append(csvSepCamp);
								
								sb.append((char) 34);
								if(ic.getDescProveedor() != null && ic.getDescProveedor().getValue() != null) {
									sb.append(ic.getDescProveedor().getValue());
								}
								sb.append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append(ic.getIdProducto().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ic.getDescProducto().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(ic.getCantidadTransacciones().getValue()).append((char) 34)
									.append(csvSepCamp);
							
							
							sb.append((char) 34).append("$")
							.append(ACGFormatHelper.format(ic.getMontoTotal().getValue(), csvSepDec))
							.append((char) 34).append(csvSepCamp);

							sb.append((char) 34).append(ic.getFormaPagoComision().getValue()).append((char) 34)
									.append(csvSepCamp);

							if (!"P".equals(this.getUsuario().getTipoCliente())) {
								sb.append((char) 34)
								.append(ACGFormatHelper.format(ic.getPorcentajeComision().getValue(), csvSepDec))
								.append((char) 34).append(csvSepCamp);
							}

							sb.append((char) 34).append("$")
							.append(ACGFormatHelper.format(ic.getComisionCalculada().getValue(), csvSepDec))
							.append((char) 34).append(csvSepCamp);
							
							if ((!"P".equals(this.getUsuario().getTipoCliente()))
									&& (this.getUsuario().getInfComMostrarComConIVA())) {
								sb.append((char) 34)
								.append(ACGFormatHelper.format(ic.getPorcentajeComisionConIVA().getValue(), csvSepDec))
								.append((char) 34).append(csvSepCamp);
							}

							if (this.getUsuario().getInfComMostrarComConIVA()) {
								sb.append((char) 34).append("$")
								.append(ACGFormatHelper.format(ic.getComisionCalculadaConIVA().getValue(), csvSepDec))
								.append((char) 34).append(csvSepCamp);
							}

							if (!agrupar) {
								sb.append((char) 34)
								.append(ic.getComisionAcreditada().getValue())
								.append((char) 34).append(csvSepCamp);
								sb.append((char) 34).append(ic.getDescEstadoComision().getValue()).append((char) 34);
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
						ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + sdf.format(new Date())
								+ "_(" + this.getUsuario().getIdMayorista() + ")_" + "InformeComisiones.csv" + "\"");

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
			// TODO: el arrayList no tiene mas el error, que hacemos!!!!!!!!
			// }
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
					LogACGHelper.escribirLog(null, "Informe Comisiones. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			}else {
				LogACGHelper.escribirLog(null, "Informe Comisiones. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Informe Comisiones. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");	
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Error ejecutando el WS de consulta de Comisiones: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;			
	}	

	public Long getIdClienteInferior() {
		return idClienteInferior;
	}

	public void setIdClienteInferior(Long idClienteInferior) {
		this.idClienteInferior = idClienteInferior;
	}

	public Float getTotalComisiones() {
		return totalComisiones;
	}

	public void setTotalComisiones(Float totalComisiones) {
		this.totalComisiones = totalComisiones;
	}

	public Long getTotalTrx() {
		return totalTrx;
	}

	public void setTotalTrx(Long totalTrx) {
		this.totalTrx = totalTrx;
	}

	public Float getTotalMonto() {
		return totalMonto;
	}

	public void setTotalMonto(Float totalMonto) {
		this.totalMonto = totalMonto;
	}

	public Float getTotalComisionesConIVA() {
		return totalComisionesConIVA;
	}

	public void setTotalComisionesConIVA(Float totalComisionesConIVA) {
		this.totalComisionesConIVA = totalComisionesConIVA;
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

	public Long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Long idProveedor) {
		this.idProveedor = idProveedor;
	}

	public Boolean getAgrupar() {
		return agrupar;
	}

	public void setAgrupar(Boolean agrupar) {
		this.agrupar = agrupar;
	}

	public List<SelectItem> getProveedores() {
		return proveedores;
	}

	public void setProveedores(List<SelectItem> proveedores) {
		this.proveedores = proveedores;
	}

	public Integer getTipoFiltroCliente() {
		return tipoFiltroCliente;
	}

	public void setTipoFiltroCliente(Integer tipoFiltroCliente) {
		this.tipoFiltroCliente = tipoFiltroCliente;
	}

	public Long getIdEstadoComision() {
		return idEstadoComision;
	}

	public void setIdEstadoComision(Long idEstadoComision) {
		this.idEstadoComision = idEstadoComision;
	}

	public List<SelectItem> getEstadosComisiones() {
		return estadosComisiones;
	}

	public void setEstadosComisiones(List<SelectItem> estadosComisiones) {
		this.estadosComisiones = estadosComisiones;
	}

	public Boolean getInfHistorico() {
		return infHistorico;
	}

	public void setInfHistorico(Boolean infHistorico) {
		this.infHistorico = infHistorico;
	}

}
