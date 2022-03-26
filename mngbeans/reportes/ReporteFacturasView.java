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
import java.util.Iterator;
import java.util.List;
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
import com.americacg.cargavirtual.gestion.model.CuentaCorrienteNuevo;
import com.americacg.cargavirtual.gestion.model.Factura;
import com.americacg.cargavirtual.gestion.model.FacturaList;
import com.americacg.cargavirtual.gestion.model.InformeCuentaCorrienteNuevo;
import com.americacg.cargavirtual.gestion.model.SaldoProveedor;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reporteFacturasView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteFacturasView extends ReporteGeneral<Factura> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5610476376109269242L;
	private List<SelectItem> proveedores;
	private Long idProveedor;
	private Float sumaPendPago = 0F;
	private String nroFactura;
	private Integer estadoFacturas = 2;

	private Long idProvDet;
	private String nroFactDet;

	private Float sumaPendPagoDetalle = 0F;

	private Integer cantFacturas = 0;

	private List<CuentaCorrienteNuevo> l;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		this.idProveedor = null;
		this.sumaPendPago = 0F;
		this.nroFactura = "";
		this.estadoFacturas = 2;
		this.cantFacturas = 0;
		try {

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
							"Informe Facturas. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Facturas. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe Facturas. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
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
			this.cantRegistros = 0;
			
			sumaPendPago = 0F;
			list = null;
			if (!validaFechas()) {
				return;
			}

			FacturaList fl = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.listaDeFacturas(this.getUsuario().getIdMayorista(), idProveedor, nroFactura, estadoFacturas);

			if (fl.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error: " + fl.getError().getValue().getMsgError().getValue(), null));
			} else {

				list = fl.getListFact().getValue().getFactura();
				
				if ((list != null) && (!list.isEmpty()) && (list.size() > 0)) {
					this.cantRegistros = list.size();
					this.cantFacturas = list.size();
					
					// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
					// TODO implementar exportToCSV
					// this.exportToCSV.setExportText("");
					// this.exportToCSV.setFileName("");
					this.mostrarArchivoCSV = false;

					String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
					String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();
		
					StringBuilder sb = new StringBuilder();
					// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
					SimpleDateFormat ff = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


					//CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
					if (this.exportToExcel) {
						// Header
						sb.append((char) 34).append("Informe Facturas. Cant: ").append(this.cantFacturas).append((char) 34).append(csvSepCamp);
						sb.append((char) 13).append((char) 10);
						
						sb.append((char) 34).append("ID Proveedor").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Proveedor").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Nro. Factura").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("Valor Pendiente de Pago").append((char) 34).append(csvSepCamp);
						sb.append((char) 13).append((char) 10);

					}
				
					// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
					// EXPORTAR EN CASO DE SER NECESARIO
					for (Factura it : list) {

						sumaPendPago += it.getValorPendienteDePago().getValue();
						
						// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
						if (this.exportToExcel) {

							if (!mostrarArchivoCSV) {
								mostrarArchivoCSV = true;
							}

							sb.append((char) 34).append(it.getIdProveedor().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(it.getRazonSocialProveedor().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append(it.getNroFactura().getValue()).append((char) 34)
									.append(csvSepCamp);
							sb.append((char) 34).append("$")
							.append(ACGFormatHelper.format(it.getValorPendienteDePago().getValue(), csvSepDec))
							.append((char) 34).append(csvSepCamp);
							sb.append((char) 13).append((char) 10);

						}

					}

					if (this.exportToExcel) {
						// Footer
						sb.append((char) 34).append("Total").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("$")
						.append(ACGFormatHelper.format(this.sumaPendPago, csvSepDec))
						.append((char) 34).append(csvSepCamp);
						sb.append((char) 13).append((char) 10);
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
										+ this.getUsuario().getIdMayorista() + ")_" + "InformeFacturas.csv"
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

					PrimeFaces.current().executeScript("PF('panelFiltroWG').toggle();");
					

				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No se ha encontrado ningun registro con ese criterio.", null));
				}

			}

		} catch (WebServiceException ste) {
			cantFacturas = 0;
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
					LogACGHelper.escribirLog(null, "Informe Facturas. Error ejecutando el WS de consulta: |"
							+ ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Facturas. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			cantFacturas = 0;
			LogACGHelper.escribirLog(null,
					"Informe Facturas. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de Facturas: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;
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

	public String getNroFactura() {
		return nroFactura;
	}

	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
	}

	public Float getSumaPendPago() {
		return sumaPendPago;
	}

	public void setSumaPendPago(Float sumaPendPago) {
		this.sumaPendPago = sumaPendPago;
	}

	public Long getIdProvDet() {
		return idProvDet;
	}

	public void setIdProvDet(Long idProvDet) {
		this.idProvDet = idProvDet;
	}

	public String getNroFactDet() {
		return nroFactDet;
	}

	public void setNroFactDet(String nroFactDet) {

		sumaPendPagoDetalle = 0F;
		Float sumDebeDet = 0F;
		Float sumHaberDet = 0F;
		l = null;

		try {
			// TODO: ver el idcliente si es recibido desde otro lugar
			// InformeCuentaCorrienteNuevo i =
			// GestionServiceHelper.getGestionService(CfgTimeout.REPORTE).informeCuentaCorrienteNuevo(
			// this.getUsuario().getIdMayorista(), 2L, this.idProvDet, new Date(), new
			// Date(), idCliente, null,
			// nroFactDet, null);
			GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
			gcFechaHoraDesde.setTime(new Date());
			XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraDesde);

			GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
			gcFechaHoraHasta.setTime(new Date());
			XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gcFechaHoraHasta);

			InformeCuentaCorrienteNuevo i = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.informeCuentaCorrienteNuevo(this.getUsuario().getIdMayorista(), 2L, this.idProvDet,
							xmlGCFechaHoraDesde, xmlGCFechaHoraHasta, this.getUsuario().getIdCliente(), null,
							nroFactDet, null);

			if (!i.getError().getValue().getHayError().getValue()) {

				if (i.getListCuentaCorrienteNuevo() != null) {

					if ((i.getListCuentaCorrienteNuevo() != null)
							&& (!i.getListCuentaCorrienteNuevo().getValue().getCuentaCorrienteNuevo().isEmpty())
							&& (i.getListCuentaCorrienteNuevo().getValue().getCuentaCorrienteNuevo().size() > 0)) {

						l = i.getListCuentaCorrienteNuevo().getValue().getCuentaCorrienteNuevo();

						// Elimina el encabezado que muestra el saldo anterior de la CC
						if (l.get(0).getIdCC() == null) {
							l.remove(0);
						}

						for (Iterator iterator = l.iterator(); iterator.hasNext();) {
							CuentaCorrienteNuevo ccn = (CuentaCorrienteNuevo) iterator.next();

							sumDebeDet = sumDebeDet + ccn.getDebe().getValue();
							sumHaberDet = sumHaberDet + ccn.getHaber().getValue();

						}

						sumaPendPagoDetalle = sumDebeDet - sumHaberDet;
					}

				}

			}
		} catch (Exception e) {
			l = null;
			sumaPendPagoDetalle = 0F;
		}

		this.nroFactDet = nroFactDet;
	}

	public List<CuentaCorrienteNuevo> getL() {
		return l;
	}

	public void setL(List<CuentaCorrienteNuevo> l) {
		this.l = l;
	}

	public Float getSumaPendPagoDetalle() {
		return sumaPendPagoDetalle;
	}

	public void setSumaPendPagoDetalle(Float sumaPendPagoDetalle) {
		this.sumaPendPagoDetalle = sumaPendPagoDetalle;
	}

	public Integer getEstadoFacturas() {
		return estadoFacturas;
	}

	public void setEstadoFacturas(Integer estadoFacturas) {
		this.estadoFacturas = estadoFacturas;
	}

	public Integer getCantFacturas() {
		return cantFacturas;
	}

	public void setCantFacturas(Integer cantFacturas) {
		this.cantFacturas = cantFacturas;
	}

}
