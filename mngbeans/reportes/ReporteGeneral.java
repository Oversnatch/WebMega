package com.americacg.cargavirtual.web.mngbeans.reportes;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.springframework.security.core.context.SecurityContextHolder;
//import com.americacg.cargavirtual.web.helpers.StringHelper;
import com.americacg.cargavirtual.web.model.Usuario;
import com.americacg.cargavirtual.web.model.Error;

public class ReporteGeneral<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5744318490887462181L;
	protected Date fechaHoraDesde;
	protected Date fechaHoraHasta;
	protected Integer cantRegistros;
	protected boolean mostrarRegistros = false;

	private Integer scrollPage = 1;

	protected Boolean exportToExcel = false;

	protected Boolean mostrarArchivoCSV;

	protected List<T> list;

	protected Integer cantMaxRegistrosAmostrarPorPantalla = 10000;
	protected Integer cantMaxRegistrosAexportar = 100000;

	public ReporteGeneral() {
		inicializarFechas();
	}

	protected void inicializarFechas() {
		// inicializo Fechas

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

		exportToExcel = false;
		cantRegistros = 0;

	}

	protected boolean validaFechas() {

		boolean ok = true;

		if (fechaHoraDesde != null && fechaHoraHasta != null) {
			if (!(fechaHoraDesde.before(fechaHoraHasta) || fechaHoraDesde.equals(fechaHoraHasta))) {
				ok = false;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La fecha Desde debe ser menor o igual a la fecha Hasta", null));
			}
		}

		return ok;

	}

	public Error resetearReporte(){
		Error e = new Error();
		inicializarFechas();
		this.scrollPage = 1;
		this.list = null;
		this.mostrarRegistros = false;
		return e;
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

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public Integer getScrollPage() {
		return scrollPage;
	}

	public void setScrollPage(Integer scrollPage) {
		this.scrollPage = scrollPage;
	}

	public Integer getCantRegistros() {
		return cantRegistros;
	}

	public void setCantRegistros(Integer cantRegistros) {
		this.cantRegistros = cantRegistros;
	}

	public Integer getCantMaxRegistrosAmostrarPorPantalla() {
		return cantMaxRegistrosAmostrarPorPantalla;
	}

	public void setCantMaxRegistrosAmostrarPorPantalla(Integer cantMaxRegistrosAmostrarPorPantalla) {
		this.cantMaxRegistrosAmostrarPorPantalla = cantMaxRegistrosAmostrarPorPantalla;
	}

	public Boolean getMostrarArchivoCSV() {
		return mostrarArchivoCSV;
	}

	public void setMostrarArchivoCSV(Boolean mostrarArchivoCSV) {
		this.mostrarArchivoCSV = mostrarArchivoCSV;
	}

	public Integer getCantMaxRegistrosAexportar() {
		return cantMaxRegistrosAexportar;
	}

	public void setCantMaxRegistrosAexportar(Integer cantMaxRegistrosAexportar) {
		this.cantMaxRegistrosAexportar = cantMaxRegistrosAexportar;
	}

	public Usuario getUsuario() {
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

	public boolean ismostrarRegistros() {
		return mostrarRegistros;
	}

	public void setmostrarRegistros(boolean mostrarRegistros) {
		this.mostrarRegistros = mostrarRegistros;
	}
}
