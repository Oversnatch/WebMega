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
import com.americacg.cargavirtual.gestion.model.ArrayOfSaldoProveedor;
import com.americacg.cargavirtual.gestion.model.CabeceraProducto;
import com.americacg.cargavirtual.gestion.model.DetalleRentabilidad;
import com.americacg.cargavirtual.gestion.model.InformeRentabilidad;
import com.americacg.cargavirtual.gestion.model.SaldoProveedor;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@Named("reporteRentabilidadView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteRentabilidadView extends ReporteGeneral<DetalleRentabilidad> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6558799287411809586L;
	private InformeRentabilidad ir;
	private Float totalMonto = 0F;
	private Float totalDeMontoTotal;
	private String ivaRentabilidad = "";
	private Long idProveedor;
	private Long idProducto;
	private List<SelectItem> productos;
	private List<SelectItem> proveedores;
	private Long idClienteFiltro;

	private String fechaDesdeRep;
	private String fechaHastaRep;

	private Boolean incluirDetalleFecha = false;
	private Boolean incluirDetalleProveedor = false;
	private Boolean incluirDetalleProducto = false;
	private Boolean incluirDetalleCliente = false;

	private Boolean mostrarJerarquia = false;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();

		this.totalMonto = 0F;
		this.totalDeMontoTotal = 0F;
		this.ivaRentabilidad = "";
		this.idProveedor = null;
		this.idProducto = null;
		this.idClienteFiltro = this.getUsuario().getIdCliente();

		this.fechaDesdeRep = "";
		this.fechaHastaRep = "";

		this.incluirDetalleFecha = false;
		this.incluirDetalleProveedor = false;
		this.incluirDetalleProducto = false;
		this.incluirDetalleCliente = false;

		this.mostrarJerarquia = false;

		idClienteFiltro = this.getUsuario().getIdCliente();

		try {

			this.productos = new ArrayList<SelectItem>();
			ArrayOfCabeceraProducto l = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.mostrarCabeceraProductos(this.getUsuario().getIdMayorista(), null, false);
			for (CabeceraProducto cp : l.getCabeceraProducto()) {
				this.productos.add(new SelectItem(cp.getIdProducto().getValue(), cp.getDescProducto().getValue()));
			}
			this.proveedores = new ArrayList<SelectItem>();
			ArrayOfSaldoProveedor lp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.obtenerSaldoProveedores(this.getUsuario().getIdMayorista(), null, false, false);
			for (SaldoProveedor sp : lp.getSaldoProveedor()) {
				this.proveedores.add(new SelectItem(sp.getIdProveedor().getValue(), sp.getRazonSocial().getValue()));
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
							"Informe Rentabilidad. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Rentabilidad. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Rentabilidad. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReporteRentabilidadView() {
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

			this.list = null;
			this.mostrarRegistros = true;

			this.fechaDesdeRep = "";
			this.fechaHastaRep = "";

			if (!validaFechas()) {
				return;
			}

			totalMonto = 0F;
			totalDeMontoTotal = 0F;
			Float ivaAfacturarPorLaRentabilidad = 0F;
			ivaRentabilidad = "IVA por rentabilidad: $0";

			GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
			gcFechaHoraDesde.setTime(fechaHoraDesde);
			XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraDesde);

			GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
			gcFechaHoraHasta.setTime(fechaHoraHasta);
			XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraHasta);

			ir = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).informeRentabilidad(
					this.getUsuario().getIdMayorista(), this.getUsuario().getIdCliente(), xmlGCFechaHoraDesde,
					xmlGCFechaHoraHasta, idClienteFiltro, idProveedor, idProducto, incluirDetalleFecha,
					incluirDetalleProveedor, incluirDetalleProducto, incluirDetalleCliente);

			if (ir.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						ir.getError().getValue().getMsgError().getValue(), null));
			} else {

				SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
				fechaDesdeRep = formatoFecha.format(ir.getFechaDesde().toGregorianCalendar().getTime()).toString();
				fechaHastaRep = formatoFecha.format(ir.getFechaHasta().toGregorianCalendar().getTime()).toString();

				list = ir.getListDetRentabilidad().getValue().getDetalleRentabilidad();

				/*
				 * list = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).
				 * informeRentabilidad(this.getUsuario( ).getIdMayorista(),
				 * this.getUsuario().getIdCliente() , fechaHoraDesde, fechaHoraHasta, idCliente
				 * , idProveedor, idProducto, agrupar, detallado);
				 * 
				 */
				if (list == null || list.isEmpty()) {
					this.cantRegistros = 0;
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No existe información para la consulta realizada.", null));
				} else {
					this.cantRegistros = this.list.size();
					// Calculo sumatorias

					// System.out.println("REPORTE RENTABILIDAD");
					for (DetalleRentabilidad dr : list) {

						// System.out.println(dr.getConcepto() + ". IMPORTE:" + dr.getImporte());

						totalMonto += dr.getImporte().getValue();
						totalDeMontoTotal += dr.getMontoTotal().getValue();
					}

					// Calculo el IVA de la rentabilidad. Solo si la rentabilidad y el porcentaje de
					// IVA son mayores a Cero
					if (totalMonto > 0) {
						Float porcentajeVigenteDeIVA = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
								.porcentajeVigenteDeIVA();
						if ((porcentajeVigenteDeIVA != null) && (porcentajeVigenteDeIVA > 0)) {
							ivaAfacturarPorLaRentabilidad = totalMonto * porcentajeVigenteDeIVA / 100;

							ivaRentabilidad = "IVA por rentabilidad: $"
									+ String.format("%.2f", ivaAfacturarPorLaRentabilidad.floatValue());

						}
					}

					// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
					if (this.exportToExcel) {

						// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
						mostrarArchivoCSV = false;

						String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
						String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

						StringBuilder sb = new StringBuilder();

						sb.append((char) 34).append("Informe de Rentabilidad ")
								.append(ir.getCliente().getValue().getRazonSocial().getValue()).append("  (")
								.append(fechaDesdeRep).append(" hasta ").append(fechaHastaRep).append(")")
								.append((char) 34);
						sb.append((char) 13).append((char) 10);

						sb.append((char) 34).append("Distribuidor: ")
								.append(ir.getDistribuidor().getValue().getRazonSocial().getValue()).append((char) 34);
						sb.append((char) 13).append((char) 10);

						if (incluirDetalleFecha) {
							sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
						}

						sb.append((char) 34).append("Concepto").append((char) 34).append(csvSepCamp);

						if (mostrarJerarquia && this.getUsuario().getNivelDistribuidorSuperior() <= 1) {
							sb.append((char) 34).append("ID Distribuidor 1").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Distribuidor 1").append((char) 34).append(csvSepCamp);
						}
						if (mostrarJerarquia && this.getUsuario().getNivelDistribuidorSuperior() <= 2) {
							sb.append((char) 34).append("ID Distribuidor 2").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Distribuidor 2").append((char) 34).append(csvSepCamp);
						}
						if (mostrarJerarquia && this.getUsuario().getNivelDistribuidorSuperior() <= 3) {
							sb.append((char) 34).append("ID Distribuidor 3").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Distribuidor 3").append((char) 34).append(csvSepCamp);
						}
						if (mostrarJerarquia && this.getUsuario().getNivelDistribuidorSuperior() <= 4) {
							sb.append((char) 34).append("ID Distribuidor 4").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Distribuidor 4").append((char) 34).append(csvSepCamp);
						}
						if (mostrarJerarquia && this.getUsuario().getNivelDistribuidorSuperior() <= 5) {
							sb.append((char) 34).append("ID Distribuidor 5").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Distribuidor 5").append((char) 34).append(csvSepCamp);
						}

						if (incluirDetalleCliente) {
							sb.append((char) 34).append("ID Cliente").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Cliente").append((char) 34).append(csvSepCamp);
						}

						if (incluirDetalleProveedor) {
							sb.append((char) 34).append("ID Proveedor").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Proveedor").append((char) 34).append(csvSepCamp);
						}

						if (incluirDetalleProducto) {
							sb.append((char) 34).append("ID Producto").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("Producto").append((char) 34).append(csvSepCamp);
						}
						sb.append((char) 34).append("Monto Total Vendido/Pagado").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Comision").append((char) 34).append(csvSepCamp);
						sb.append((char) 13).append((char) 10);

						// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
						SimpleDateFormat ff = new SimpleDateFormat("dd/MM/yyyy");

						// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
						// EXPORTAR EN CASO DE SER NECESARIO
						if (list != null) {
							for (DetalleRentabilidad ic : list) {
								if (incluirDetalleFecha) {
									sb.append((char) 34)
											.append(ff.format(ic.getFecha().toGregorianCalendar().getTime()).toString())
											.append((char) 34).append(csvSepCamp);
								}
								sb.append((char) 34).append(ic.getConcepto().getValue()).append((char) 34)
										.append(csvSepCamp);
								if (mostrarJerarquia && this.getUsuario().getNivelDistribuidorSuperior() <= 1) {
									if (ic.getIdDist1().getValue() != null && ic.getIdDist1().getValue() > 0) {
										sb.append((char) 34).append(ic.getIdDist1().getValue()).append((char) 34)
												.append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}
									if (ic.getDescDist1().getValue() != null
											&& !"".equals(ic.getDescDist1().getValue())) {
										sb.append((char) 34).append(ic.getDescDist1().getValue()).append((char) 34)
												.append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}
								}
								if (mostrarJerarquia && this.getUsuario().getNivelDistribuidorSuperior() <= 2) {
									if (ic.getIdDist2().getValue() != null && ic.getIdDist2().getValue() > 0) {
										sb.append((char) 34).append(ic.getIdDist2().getValue()).append((char) 34)
												.append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}
									if (ic.getDescDist2().getValue() != null
											&& !"".equals(ic.getDescDist2().getValue())) {
										sb.append((char) 34).append(ic.getDescDist2().getValue()).append((char) 34)
												.append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}
								}
								if (mostrarJerarquia && this.getUsuario().getNivelDistribuidorSuperior() <= 3) {
									if (ic.getIdDist3().getValue() != null && ic.getIdDist3().getValue() > 0) {
										sb.append((char) 34).append(ic.getIdDist3().getValue()).append((char) 34)
												.append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}
									if (ic.getDescDist3().getValue() != null
											&& !"".equals(ic.getDescDist3().getValue())) {
										sb.append((char) 34).append(ic.getDescDist3().getValue()).append((char) 34)
												.append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}
								}
								if (mostrarJerarquia && this.getUsuario().getNivelDistribuidorSuperior() <= 4) {
									if (ic.getIdDist4().getValue() != null && ic.getIdDist4().getValue() > 0) {
										sb.append((char) 34).append(ic.getIdDist4().getValue()).append((char) 34)
												.append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}
									if (ic.getDescDist4().getValue() != null
											&& !"".equals(ic.getDescDist4().getValue())) {
										sb.append((char) 34).append(ic.getDescDist4().getValue()).append((char) 34)
												.append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}
								}
								if (mostrarJerarquia && this.getUsuario().getNivelDistribuidorSuperior() <= 5) {
									if (ic.getIdDist5().getValue() != null && ic.getIdDist5().getValue() > 0) {
										sb.append((char) 34).append(ic.getIdDist5().getValue()).append((char) 34)
												.append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}
									if (ic.getDescDist5().getValue() != null
											&& !"".equals(ic.getDescDist5().getValue())) {
										sb.append((char) 34).append(ic.getDescDist5().getValue()).append((char) 34)
												.append(csvSepCamp);
									} else {
										sb.append((char) 34).append((char) 34).append(csvSepCamp);
									}
								}
								if (incluirDetalleCliente) {
									sb.append((char) 34).append(ic.getIdCliente().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getRazonSocialCliente().getValue()).append((char) 34)
											.append(csvSepCamp);
								}

								if (incluirDetalleProveedor) {
									sb.append((char) 34).append(ic.getIdProveedor().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getDescProveedor().getValue()).append((char) 34)
											.append(csvSepCamp);
								}

								if (incluirDetalleProducto) {
									sb.append((char) 34).append(ic.getIdProducto().getValue()).append((char) 34)
											.append(csvSepCamp);
									sb.append((char) 34).append(ic.getDescProducto().getValue()).append((char) 34)
											.append(csvSepCamp);
								}
								
								sb.append((char) 34).append("$")
								.append(ACGFormatHelper.format(ic.getMontoTotal().getValue(), csvSepDec))
								.append((char) 34).append(csvSepCamp);

								sb.append((char) 34).append("$")
								.append(ACGFormatHelper.format(ic.getImporte().getValue(), csvSepDec))
								.append((char) 34).append(csvSepCamp);

								sb.append((char) 13).append((char) 10);
							}
						}

						if (incluirDetalleFecha) {
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						}
						sb.append((char) 34).append("Rentabilidad Total del Periodo").append((char) 34).append(csvSepCamp);

						if (mostrarJerarquia && this.getUsuario().getNivelDistribuidorSuperior() <= 1) {
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						}
						if (mostrarJerarquia && this.getUsuario().getNivelDistribuidorSuperior() <= 2) {
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						}
						if (mostrarJerarquia && this.getUsuario().getNivelDistribuidorSuperior() <= 3) {
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						}
						if (mostrarJerarquia && this.getUsuario().getNivelDistribuidorSuperior() <= 4) {
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						}
						if (mostrarJerarquia && this.getUsuario().getNivelDistribuidorSuperior() <= 5) {
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						}

						if (incluirDetalleCliente) {
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						}

						if (incluirDetalleProveedor) {
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						}

						if (incluirDetalleProducto) {
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
							sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
						}
						sb.append((char) 34).append("$")
							.append(ACGFormatHelper.format(totalDeMontoTotal, csvSepDec))						
								.append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("$")
							.append(ACGFormatHelper.format(totalMonto, csvSepDec))						
								.append((char) 34).append(csvSepCamp);
						sb.append((char) 13).append((char) 10);

						sb.append((char) 34).append(ivaRentabilidad).append((char) 34).append(csvSepCamp);
						sb.append((char) 13).append((char) 10);

						FacesContext fc = FacesContext.getCurrentInstance();
						ExternalContext ec = fc.getExternalContext();

						ec.responseReset();
						ec.setResponseContentType("text/plain");
						ec.setResponseContentLength(sb.toString().length());
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
						ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + sdf.format(new Date())
								+ "_(" + this.getUsuario().getIdMayorista() + ")_" + "InformeRentabilidad.csv" + "\"");

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
					LogACGHelper.escribirLog(null, "Informe Rentabilidad. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			}else {
				LogACGHelper.escribirLog(null, "Informe Rentabilidad. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Informe Rentabilidad. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");	
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Error ejecutando el WS de consulta de Rentabilidad: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;			
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

	public List<SelectItem> getProveedores() {
		return proveedores;
	}

	public void setProveedores(List<SelectItem> proveedores) {
		this.proveedores = proveedores;
	}

	public Float getTotalMonto() {
		return totalMonto;
	}

	public void setTotalMonto(Float totalMonto) {
		this.totalMonto = totalMonto;
	}

	public InformeRentabilidad getIr() {
		return ir;
	}

	public void setIr(InformeRentabilidad ir) {
		this.ir = ir;
	}

	public Long getIdClienteFiltro() {
		return idClienteFiltro;
	}

	public void setIdClienteFiltro(Long idClienteFiltro) {
		this.idClienteFiltro = idClienteFiltro;
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

	public String getIvaRentabilidad() {
		return ivaRentabilidad;
	}

	public void setIvaRentabilidad(String ivaRentabilidad) {
		this.ivaRentabilidad = ivaRentabilidad;
	}

	public Float getTotalDeMontoTotal() {
		return totalDeMontoTotal;
	}

	public void setTotalDeMontoTotal(Float totalDeMontoTotal) {
		this.totalDeMontoTotal = totalDeMontoTotal;
	}

	public Boolean getIncluirDetalleProveedor() {
		return incluirDetalleProveedor;
	}

	public void setIncluirDetalleProveedor(Boolean incluirDetalleProveedor) {
		this.incluirDetalleProveedor = incluirDetalleProveedor;
	}

	public Boolean getIncluirDetalleProducto() {
		return incluirDetalleProducto;
	}

	public void setIncluirDetalleProducto(Boolean incluirDetalleProducto) {
		this.incluirDetalleProducto = incluirDetalleProducto;
	}

	public Boolean getIncluirDetalleCliente() {
		return incluirDetalleCliente;
	}

	public void setIncluirDetalleCliente(Boolean incluirDetalleCliente) {
		this.incluirDetalleCliente = incluirDetalleCliente;
	}

	public Boolean getIncluirDetalleFecha() {
		return incluirDetalleFecha;
	}

	public void setIncluirDetalleFecha(Boolean incluirDetalleFecha) {
		this.incluirDetalleFecha = incluirDetalleFecha;
	}

	public Boolean getMostrarJerarquia() {
		return mostrarJerarquia;
	}

	public void setMostrarJerarquia(Boolean mostrarJerarquia) {
		this.mostrarJerarquia = mostrarJerarquia;
	}
}