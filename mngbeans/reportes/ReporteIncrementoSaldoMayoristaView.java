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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;

import org.springframework.context.annotation.Scope;

import com.americacg.cargavirtual.gestion.model.ArrayOfCabeceraProducto;
import com.americacg.cargavirtual.gestion.model.ArrayOfInformeIncrementoSaldoMayorista;
import com.americacg.cargavirtual.gestion.model.ArrayOfSaldoProveedor;
import com.americacg.cargavirtual.gestion.model.CabeceraProducto;
import com.americacg.cargavirtual.gestion.model.InformeIncrementoSaldoMayorista;
import com.americacg.cargavirtual.gestion.model.SaldoProveedor;
import com.americacg.cargavirtual.gestion.model.TipoMovimiento;
import com.americacg.cargavirtual.gestion.model.TipoMovimientoContainer;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.ACGFormatHelper;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.model.Error;

@Named("reporteIncrementoSaldoMayoristaView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class ReporteIncrementoSaldoMayoristaView extends ReporteGeneral<InformeIncrementoSaldoMayorista> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7824423279780592L;

	private Long idCliente;
	private Long idIncSaldoMayorista;
	private Float sumaInc = 0F;

	private Long idProducto;
	private Long idProveedor;
	private List<SelectItem> productos;
	private List<SelectItem> proveedores;

	private Long idTipoMovimiento;
	private List<SelectItem> tipoMovimientos;

	@Override
	public Error resetearReporte() {
		Error err = super.resetearReporte();
		this.idCliente = null;
		this.idIncSaldoMayorista = null;
		this.sumaInc = 0F;

		this.proveedores = null;
		this.productos = null;

		this.idProducto = null;
		this.idProveedor = null;

		this.tipoMovimientos = null;
		this.idTipoMovimiento = null;
		this.cantRegistros = 0;
		return err;
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

			sumaInc = 0F;

			if (!validaFechas()) {
				return;
			}

			XMLGregorianCalendar xmlGCFechaHoraDesde;
			try {
				GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
				gcFechaHoraDesde.setTime(fechaHoraDesde);
				xmlGCFechaHoraDesde = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaHoraDesde);

			} catch (DatatypeConfigurationException e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fecha Desde no valida.", null));
				return;
			}

			XMLGregorianCalendar xmlGCFechaHoraHasta;
			try {
				GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
				gcFechaHoraHasta.setTime(fechaHoraHasta);
				xmlGCFechaHoraHasta = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaHoraHasta);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fecha Hasta no valida.", null));
				return;
			}

			ArrayOfInformeIncrementoSaldoMayorista l = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.informeIncrementoSaldoMayorista(this.getUsuario().getIdMayorista(), xmlGCFechaHoraDesde,
							xmlGCFechaHoraHasta, idCliente, idIncSaldoMayorista, idTipoMovimiento, idProveedor,
							idProducto);

			if (l == null || l.getInformeIncrementoSaldoMayorista() == null
					|| l.getInformeIncrementoSaldoMayorista().isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No se ha encontrado ningun registro con ese criterio.", null));
			} else {

				this.list = l.getInformeIncrementoSaldoMayorista();

				cantRegistros = list.size();

				// GENERO Y LIMPIO LAS VARIABLES PARA LA EXPORTACION
				// TODO implementar exportToCSV
				// this.exportToCSV.setExportText("");
				// this.exportToCSV.setFileName("");
				mostrarArchivoCSV = false;

				String csvSepCamp = this.getUsuario().getCsvSeparadorCampo();
				String csvSepDec = this.getUsuario().getCsvSeparadorDecimales();

				StringBuilder sb = new StringBuilder();

				// CREO HEADER DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
				if (this.exportToExcel) {
					// Header

					sb.append((char) 34).append("Informe Incremento Saldo Mayorista (").append(cantRegistros)
							.append(" Registros)").append((char) 34).append(csvSepCamp);
					sb.append((char) 13).append((char) 10);
					sb.append((char) 34).append("Id").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Fecha").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("ID Mayorista").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Mayorista").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("ID Usuario").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Usuario").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Saldo Inicial").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Incremento").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Saldo Final").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("ID Tipo Mov.").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Tipo Mov.").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("ID Proveedor").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Proveedor").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("ID Producto").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Producto").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("Coment.").append((char) 34).append(csvSepCamp);
					sb.append((char) 13).append((char) 10);

				}

				// DEFINO FORMATO DE FECHA PARA MOSTRAR EN EL REPORTE QUE SE EXPORTA
				SimpleDateFormat ff = new SimpleDateFormat("yyyy/MM/dd HH:mm");

				// RECORRO LA LISTA QUE HACER LAS SUMATORIAS Y PARA GENERAR EL REPORTE A
				// EXPORTAR EN CASO DE SER NECESARIO
				for (InformeIncrementoSaldoMayorista it : list) {
					sumaInc += it.getIncremento().getValue();

					// CREO ARCHIVO DE EXPORTACION SI ES NECESARIO PARA EXPORTAR A CSV
					if (this.exportToExcel) {

						if (!mostrarArchivoCSV) {
							mostrarArchivoCSV = true;
						}

						sb.append((char) 34).append(it.getIdIncSaldoMayorista().getValue()).append((char) 34)
								.append(csvSepCamp);
						sb.append((char) 34).append(ff.format(it.getFecha().toGregorianCalendar().getTime()).toString())
								.append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append(it.getIdMayorista().getValue()).append((char) 34)
								.append(csvSepCamp);
						sb.append((char) 34).append(it.getRazonSocial().getValue()).append((char) 34)
								.append(csvSepCamp);
						sb.append((char) 34).append(it.getIdUsuario().getValue()).append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append(it.getUsuario().getValue()).append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("$")
								.append(ACGFormatHelper.format(it.getCreditoInicial().getValue(), csvSepDec))
								.append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("$")
								.append(ACGFormatHelper.format(it.getIncremento().getValue(), csvSepDec))
								.append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append("$")
								.append(ACGFormatHelper.format(it.getCreditoFinal().getValue(), csvSepDec))
								.append((char) 34).append(csvSepCamp);
						sb.append((char) 34);
						if (it.getIdTipoMovimiento() != null && it.getIdTipoMovimiento().getValue() != null) {
							sb.append(it.getIdTipoMovimiento().getValue());
						}
						sb.append((char) 34).append(csvSepCamp);
						sb.append((char) 34);
						if (it.getDescTipoMovimiento() != null && it.getDescTipoMovimiento().getValue() != null) {
							sb.append(it.getDescTipoMovimiento().getValue());
						}
						sb.append((char) 34).append(csvSepCamp);
						sb.append((char) 34);
						if (it.getIdProveedor() != null && it.getIdProveedor().getValue() != null) {
							sb.append(it.getIdProveedor().getValue());
						}
						sb.append((char) 34).append(csvSepCamp);
						sb.append((char) 34);
						if (it.getDescProveedor() != null && it.getDescProveedor().getValue() != null) {
							sb.append(it.getDescProveedor().getValue());
						}
						sb.append((char) 34).append(csvSepCamp);
						sb.append((char) 34);
						if (it.getIdProducto() != null && it.getIdProducto().getValue() != null) {
							sb.append(it.getIdProducto().getValue());
						}
						sb.append((char) 34).append(csvSepCamp);
						sb.append((char) 34);
						if (it.getDescProducto() != null && it.getDescProducto().getValue() != null) {
							sb.append(it.getDescProducto().getValue());
						}
						sb.append((char) 34).append(csvSepCamp);
						sb.append((char) 34).append(it.getComentario().getValue()).append((char) 34).append(csvSepCamp);

						sb.append((char) 13).append((char) 10);

					}
				}

				if (this.exportToExcel) {
					// Header

					sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("$").append(ACGFormatHelper.format(this.sumaInc, csvSepDec))
							.append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
					sb.append((char) 34).append("").append((char) 34).append(csvSepCamp);
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
									+ this.getUsuario().getIdMayorista() + ")_" + "InformeIncrementoSaldoMay.csv"
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
							"Informe Transacciones. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
				}
			} else {
				LogACGHelper.escribirLog(null,
						"Informe Transacciones. Error ejecutando el WS de consulta: |" + ste.getMessage() + "|");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error ejecutando el WS de consulta: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		} catch (Exception e) {
			LogACGHelper.escribirLog(null,
					"Informe Transacciones. Excepcion ejecutando el WS de consulta: |" + e.getMessage() + "|");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error ejecutando el WS de Informe Transacciones: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
			this.list = null;
		}
		return;

	}

	public Float getSumaInc() {
		return sumaInc;
	}

	public void setSumaInc(Float sumaInc) {
		this.sumaInc = sumaInc;
	}

	public Long getIdIncSaldoMayorista() {
		return idIncSaldoMayorista;
	}

	public void setIdIncSaldoMayorista(Long idIncSaldoMayorista) {
		this.idIncSaldoMayorista = idIncSaldoMayorista;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	// ----

	public List<SelectItem> getProductos() {
		if (this.productos == null) {
			this.productos = new ArrayList<SelectItem>();
			ArrayOfCabeceraProducto l = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarCabeceraProductos(this.getUsuario().getIdMayorista(), null, false);
			if (l != null && l.getCabeceraProducto() != null) {
				for (CabeceraProducto cp : l.getCabeceraProducto()) {
					this.productos.add(new SelectItem(cp.getIdProducto().getValue(), cp.getDescProducto().getValue()));
				}
			}
		}
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
		if (this.proveedores == null) {
			this.proveedores = new ArrayList<SelectItem>();
			ArrayOfSaldoProveedor lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.obtenerSaldoProveedores(this.getUsuario().getIdMayorista(), null, false, false);
			if (lp != null && lp.getSaldoProveedor() != null) {
				for (SaldoProveedor sp : lp.getSaldoProveedor()) {
					this.proveedores
							.add(new SelectItem(sp.getIdProveedor().getValue(), sp.getRazonSocial().getValue()));
				}
			}
		}
		return proveedores;
	}

	public void setProveedores(List<SelectItem> proveedores) {
		this.proveedores = proveedores;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public Long getIdTipoMovimiento() {
		return idTipoMovimiento;
	}

	public void setIdTipoMovimiento(Long idTipoMovimiento) {
		this.idTipoMovimiento = idTipoMovimiento;
	}

	public List<SelectItem> getTipoMovimientos() {
		if (this.tipoMovimientos == null) {
			this.tipoMovimientos = new ArrayList<SelectItem>();

			TipoMovimientoContainer tm = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT)
					.mostrarTipoMovimiento(this.getUsuario().getIdMayorista(), null);
			if (tm != null && tm.getListTipoMovimiento() != null && tm.getListTipoMovimiento().getValue() != null
					&& tm.getListTipoMovimiento().getValue().getTipoMovimiento() != null) {
				for (TipoMovimiento t : tm.getListTipoMovimiento().getValue().getTipoMovimiento()) {
					this.tipoMovimientos
							.add(new SelectItem(t.getIdTipoMovimiento().getValue(), t.getDescripcion().getValue()));
				}
			}
		}
		return tipoMovimientos;
	}

	public void setTipoMovimientos(List<SelectItem> tipoMovimientos) {
		this.tipoMovimientos = tipoMovimientos;
	}

}
