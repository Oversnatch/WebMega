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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.xml.ws.WebServiceException;
import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gestion.model.ArrayOfCabeceraProducto;
import com.americacg.cargavirtual.gestion.model.ArrayOfSaldoProveedor;
import com.americacg.cargavirtual.gestion.model.CabeceraProducto;
import com.americacg.cargavirtual.gestion.model.SaldoProveedor;
import com.americacg.cargavirtual.gestion.model.StockPinItem;
import com.americacg.cargavirtual.gestion.model.StockPines;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reportePINView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReportePINView extends ReporteGeneral<StockPinItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1154850124834084310L;

	private StockPines listp;

	private Long idProveedor;
	private Long idProducto;
	private String estado = "L";
	private Float importe;

	private Float importeTotal = 0F;
	private Long cantidadTotal = 0L;

	private List<SelectItem> productos;
	private List<SelectItem> proveedores;

	@Override
	public Error resetearReporte() {
		Error e = super.resetearReporte();
		listp = null;
		this.idProveedor = null;
		this.idProducto = null;
		this.importeTotal = 0F;
		this.cantidadTotal = 0L;
		this.estado = "L";
		this.importe = null;
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
							"Informe PIN. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					e.setError("GST-OTR",
							"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe PIN. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				e.setError("GST-OTR",
						"No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception ex) {
			LogACGHelper.escribirLog(null,
					"Informe PIN. Excepcion ejecutando el WS de consulta: |" + ex.getMessage() + "|");
			e.setError("GST-OTR", "No se pudo establecer la comunicación (GST-OTR).\n Por favor intente nuevamente.");
		}
		return e;
	}

	public ReportePINView() {
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

		this.list = null;
		this.mostrarRegistros = true;
		importeTotal = 0F;
		cantidadTotal = 0L;
		cantRegistros = 0;

		try {
			listp = GestionServiceHelper.getGestionService(CfgTimeout.REPORTE)
					.devolverStockPines(this.getUsuario().getIdMayorista(), idProveedor, idProducto, estado, importe);

			if (listp == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Por favor reintente", null));
				return;
			}

			if (listp.getError().getValue().getHayError().getValue()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Error en lectura de lectura de Stock de Pines: |"
										+ listp.getError().getValue().getCodigoError().getValue() + "|"
										+ listp.getError().getValue().getMsgError().getValue() + "|",
								null));
				return;
			}

			if (listp.getListStockPines() == null || listp.getListStockPines().getValue().getStockPinItem().isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se ha encontrado ningun registro con ese criterio.", null));
			} else {
				list = listp.getListStockPines().getValue().getStockPinItem();
				cantRegistros = list.size() ;
				// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
				// TODO implementar exportToCSV
				// this.exportToCSV.setExportText("");
				// this.exportToCSV.setFileName("");
				mostrarArchivoCSV = false;
	
				String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
				String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();
	
				StringBuilder sb = new StringBuilder();
				// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
				SimpleDateFormat ff = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


				//CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
				if (this.exportToExcel) {
					// Header
					sb.append((char) 34).append("Informe de Stock de Pines").append((char) 34).append(csvSepCamp);
					sb.append((char) 13).append((char) 10);
					
					sb.append((char) 34).append("ID Proveedor").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Proveedor").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("ID Producto").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Producto").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Importe").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Cantidad").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Estado").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Desc Estado").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Fecha Ultimo Cargado").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Fecha Ultimo Vendido").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Fecha Primero en Vencer").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Fecha Ultimo en Vencer").append((char) 34).append(csvSepCamp);
					sb.append((char) 13).append((char) 10);
				}
			
				// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
				// EXPORTAR EN CASO DE SER NECESARIO
				for (StockPinItem spi : this.list) {
					importeTotal += spi.getImporte().getValue() * spi.getCantidad().getValue();
					cantidadTotal += spi.getCantidad().getValue();

					// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
					if (this.exportToExcel) {

						if (!mostrarArchivoCSV) {
							mostrarArchivoCSV = true;
						}

						sb.append((char) 34).append(spi.getIdProveedor().getValue()).append((char) 34)
							.append(csvSepCamp);
						sb.append((char) 34).append(spi.getDescProveedor().getValue()).append((char) 34)
							.append(csvSepCamp);
						sb.append((char) 34).append(spi.getIdProducto().getValue()).append((char) 34)
							.append(csvSepCamp);
						sb.append((char) 34).append(spi.getDescProducto().getValue()).append((char) 34)
							.append(csvSepCamp);
						sb.append((char) 34).append("$")
							.append(ACGFormatHelper.format(spi.getImporte().getValue(), csvSepDec))
							.append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append(spi.getCantidad().getValue())
							.append((char) 34).append(csvSepCamp);		
						sb.append((char) 34).append(spi.getEstado().getValue()).append((char) 34)
							.append(csvSepCamp);
						sb.append((char) 34).append(spi.getDescEstado().getValue()).append((char) 34)
							.append(csvSepCamp);	
						
						sb.append((char) 34);
						if(spi.getFechaUltimoCargado() != null) {
							sb.append(
								ff.format(spi.getFechaUltimoCargado().toGregorianCalendar().getTime()).toString());
						}
						sb.append((char) 34).append(csvSepCamp);

						sb.append((char) 34);
						if(spi.getFechaUltimoVendido() != null) {
							sb.append(
								ff.format(spi.getFechaUltimoVendido().toGregorianCalendar().getTime()).toString());
						}
						sb.append((char) 34).append(csvSepCamp);

						sb.append((char) 34);
						if(spi.getFechaPrimeroEnVencer() != null) {
							sb.append(
								ff.format(spi.getFechaPrimeroEnVencer().toGregorianCalendar().getTime()).toString());
						}
						sb.append((char) 34).append(csvSepCamp);
						
						sb.append((char) 34);
						if(spi.getFechaUltimoEnVencer() != null) {
							sb.append(
								ff.format(spi.getFechaUltimoEnVencer().toGregorianCalendar().getTime()).toString());
						}
						sb.append((char) 34).append(csvSepCamp);

						sb.append((char) 13).append((char) 10);

					}

				}

				//CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
				if (this.exportToExcel) {
					// Footer
					sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("$").append(ACGFormatHelper.format(this.importeTotal, csvSepDec)).append((char) 34).append(csvSepCamp);					
					sb.append((char) 34).append(this.cantidadTotal).append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("-").append((char) 34).append(csvSepCamp);
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
									+ this.getUsuario().getIdMayorista() + ")_" + "InformePines.csv"
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
							"Informe PIN. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe PIN. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe PIN. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de consulta de PIN: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		PrimeFaces.current().executeScript("PF('blockPanelGral').hide()");
		return;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public Float getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(Float importeTotal) {
		this.importeTotal = importeTotal;
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

	public Float getImporte() {
		return importe;
	}

	public void setImporte(Float importe) {
		this.importe = importe;
	}

	public Long getCantidadTotal() {
		return cantidadTotal;
	}

	public void setCantidadTotal(Long cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}

	public StockPines getListp() {
		return listp;
	}

	public void setListp(StockPines listp) {
		this.listp = listp;
	}

}
