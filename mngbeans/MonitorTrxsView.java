package com.americacg.cargavirtual.web.mngbeans;

import java.io.Serializable;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;
import org.springframework.context.annotation.Scope;
import com.americacg.cargavirtual.gestion.model.ArrayOfResultadoMonitoreoTransacciones;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.americacg.cargavirtual.web.helpers.GestionServiceHelper;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.web.shared.BasePage;


@Named("monitorTrxsView")
@Scope(org.springframework.web.context.WebApplicationContext.SCOPE_SESSION)
public class MonitorTrxsView extends BasePage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8684605733098146184L;

	private ArrayOfResultadoMonitoreoTransacciones listMonitor;
	
	private Integer tipoFecha;
	private Date fechaHoraDesde;
	private Date fechaHoraHasta;
	
	private String fechaMostrar;
	
	private String tipoTRX;
	private List<SelectItem> productos;
	private Long idProducto;
	private List<SelectItem> proveedores;
	private Long idProveedor;
	private Boolean agruparProv;
	private Integer estadoMon;
	private Boolean agruparProd;
	private Boolean agruparTipoTRX;

	private Integer scrollPage = 1;
	private Integer cantRegistrosDevueltosMon = 0;
	
	public void inicializarMonitoreo(){

		this.tipoFecha = 0;
		this.fechaHoraDesde = new Date(new Date().getTime() - (600 * 1000));  //Fecha Desde es igual a la fecha actual menos 600 segundos (10 minutos)
		this.fechaHoraHasta = new Date();
		this.tipoTRX = "";
		this.idProducto = null;
		this.idProveedor = null;
		if ("M".equals(this.getUsuario().getTipoCliente())){
			this.agruparProv = false;
			this.agruparProd = false;
		}else{
			if (("D".equals(this.getUsuario().getTipoCliente())) || ("P".equals(this.getUsuario().getTipoCliente()))){
				this.agruparProv = true;
				this.agruparProd = false;
			}
		}
		this.estadoMon = null;
		
		this.agruparTipoTRX = true;
		
		this.listMonitor = null;
		this.scrollPage = 1;
		this.cantRegistrosDevueltosMon = 0;

		this.monitoreoTransacciones();
	}
	
	public void monitoreoTransacciones(){
		//System.out.println("monitoreoTransacciones");
		try {
			if (null != this.getUsuario().getMostrarMonitorTRXs()){
				if (1 == this.getUsuario().getMostrarMonitorTRXs()){
				
					//System.out.println("PASO POR monitoreoTransacciones");
					
					this.listMonitor = null;
					scrollPage = 1;
					cantRegistrosDevueltosMon = 0;
					
					this.fechaMostrar = "";
					
					GregorianCalendar gcFechaHoraDesde = new GregorianCalendar();
					gcFechaHoraDesde.setTime(fechaHoraDesde);
					XMLGregorianCalendar xmlGCFechaHoraDesde = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaHoraDesde);
							
					GregorianCalendar gcFechaHoraHasta = new GregorianCalendar();
					gcFechaHoraHasta.setTime(fechaHoraHasta);
					XMLGregorianCalendar xmlGCFechaHoraHasta = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcFechaHoraHasta);
					
					this.listMonitor = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).monitoreoTransacciones(this.getUsuario().getIdMayorista(), this.getUsuario().getTipoCliente()
							, tipoFecha, xmlGCFechaHoraDesde, xmlGCFechaHoraHasta
							, tipoTRX, idProveedor, idProducto, estadoMon, agruparProv, agruparProd, agruparTipoTRX);
		
					if (listMonitor == null){
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "La consulta de Monitoreo devolvio null", null));	
					}else{
						if (listMonitor.getResultadoMonitoreoTransacciones().size() == 0){
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "La consulta de Monitoreo devolvio cero registros", null));
						}else{
							cantRegistrosDevueltosMon = listMonitor.getResultadoMonitoreoTransacciones().size();
							//Fecha para mostrar en el encabezado del informe
							SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm");
							
							if (("M".equals(this.getUsuario().getTipoCliente())) || ("D".equals(this.getUsuario().getTipoCliente()))){
								if (tipoFecha == 0){
									String fechaMos = formato.format(listMonitor.getResultadoMonitoreoTransacciones().get(0).getFecha());
									fechaMostrar = "Monitoreo de Transacciones (Ultimo Monitoreo de las " + fechaMos + ")";
								}else{
									String fechaDesMos = formato.format(fechaHoraDesde);
									String fechaHasMos = formato.format(fechaHoraHasta);
									
									fechaMostrar = "Monitoreo de Transacciones (desde " + fechaDesMos + " hasta " + fechaHasMos + ")";
								}
							}else{
								fechaMostrar = "Monitoreo de Productos";
							}
								
							//Fin para mostrar en el encabezado del informe
						}
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
					LogACGHelper.escribirLog(null, "Error ejecutando el Monitoreo de Transacciones: |" + ste.getMessage() + "|");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el Monitoreo de Transacciones: |" + ste.getMessage() + "|", null));
				}
			}else {
				LogACGHelper.escribirLog(null, "Error ejecutando el Monitoreo de Transacciones: |" + ste.getMessage() + "|");				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ejecutando el Monitoreo de Transacciones: |" + ste.getMessage() + "|", null));
			}
			FacesContext.getCurrentInstance().validationFailed();
		} catch (Exception e) {
			LogACGHelper.escribirLog(null, "Error ejecutando el Monitoreo de Transacciones: |" + e.getMessage() + "|");	
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Error ejecutando el Monitoreo de Transacciones: |" + e.getMessage() + "|", null));
			FacesContext.getCurrentInstance().validationFailed();
		}
		return;			
	}	
	
	//-----------------------------------------------------------------------------------------------------
	// GETTERS AND SETTERS
	//-----------------------------------------------------------------------------------------------------
	
	public ArrayOfResultadoMonitoreoTransacciones getListMonitor() {
		if(listMonitor == null) {
			inicializarMonitoreo();
		}
		return listMonitor;
	}

	public void setListMonitor(ArrayOfResultadoMonitoreoTransacciones listMonitor) {
		this.listMonitor = listMonitor;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public Long getIdProveedor() {
		return idProveedor;
	}


	public void setIdProveedor(Long idProveedor) {
		this.idProveedor = idProveedor;
	}

	public List<SelectItem> getProveedores() {
		//TODO: VER SI HAY QUE MIGRARLO AL INICIALIZADOR
				/*
		if (this.proveedores == null) {
			this.proveedores = new ArrayList<SelectItem>();
			ArrayOfSaldoProveedor lp = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).obtenerSaldoProveedores(this.getUsuario().getIdMayorista(), null, false, false);
			for (SaldoProveedor sp : lp.getSaldoProveedor()) {
				this.proveedores.add(new SelectItem(sp.getIdProveedor().getValue(), sp.getRazonSocial().getValue()));
			}
		}
		*/
		return proveedores;
	}

	public void setProveedores(List<SelectItem> proveedores) {
		this.proveedores = proveedores;
	}

	public List<SelectItem> getProductos() {
		//TODO: VER SI HAY QUE MIGRARLO AL INICIALIZADOR
		/*
		if (this.productos == null) {
			this.productos = new ArrayList<SelectItem>();
			ArrayOfCabeceraProducto l = GestionServiceHelper.getGestionService(CfgTimeout.DEFAULT).mostrarCabeceraProductos(this.getUsuario().getIdMayorista(), null, false);
			for (CabeceraProducto cp : l.getCabeceraProducto()) {
				this.productos.add(new SelectItem(cp.getIdProducto().getValue(), cp.getDescProducto().getValue()));
			}
		}
		*/
		return productos;
	}

	public void setProductos(List<SelectItem> productos) {
		this.productos = productos;
	}


	public Date getFechaHoraDesde() {
		return fechaHoraDesde;
	}


	public void setFechaHoraDesde(Date fechaHoraDesde) {
		this.fechaHoraDesde = fechaHoraDesde;
	}


	public Date getFechaHoraHasta() {
		return fechaHoraHasta;
	}


	public void setFechaHoraHasta(Date fechaHoraHasta) {
		this.fechaHoraHasta = fechaHoraHasta;
	}


	public String getTipoTRX() {
		return tipoTRX;
	}


	public void setTipoTRX(String tipoTRX) {
		this.tipoTRX = tipoTRX;
	}

	public Integer getEstadoMon() {
		return estadoMon;
	}

	public void setEstadoMon(Integer estadoMon) {
		this.estadoMon = estadoMon;
	}

	public Boolean getAgruparProv() {
		return agruparProv;
	}

	public void setAgruparProv(Boolean agruparProv) {
		this.agruparProv = agruparProv;
	}

	public String getFechaMostrar() {
		return fechaMostrar;
	}

	public void setFechaMostrar(String fechaMostrar) {
		this.fechaMostrar = fechaMostrar;
	}

	public Integer getTipoFecha() {
		return tipoFecha;
	}

	public void setTipoFecha(Integer tipoFecha) {
		this.tipoFecha = tipoFecha;
	}

	public Boolean getAgruparProd() {
		return agruparProd;
	}

	public void setAgruparProd(Boolean agruparProd) {
		this.agruparProd = agruparProd;
	}

	public Boolean getAgruparTipoTRX() {
		return agruparTipoTRX;
	}

	public void setAgruparTipoTRX(Boolean agruparTipoTRX) {
		this.agruparTipoTRX = agruparTipoTRX;
	}

	public Integer getScrollPage() {
		return scrollPage;
	}

	public void setScrollPage(Integer scrollPage) {
		this.scrollPage = scrollPage;
	}

	public Integer getCantRegistrosDevueltosMon() {
		return cantRegistrosDevueltosMon;
	}

	public void setCantRegistrosDevueltosMon(Integer cantRegistrosDevueltosMon) {
		this.cantRegistrosDevueltosMon = cantRegistrosDevueltosMon;
	}
}
