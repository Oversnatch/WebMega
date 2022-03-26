package com.americacg.cargavirtual.web.helpers;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Named;

@Named("fechasHelper")
public class FechasHelper {

	protected Date esMenorIgualHoyDesde;
	protected Date esMenorIgualHoyHasta;
	protected Date esMayorHoyDesde;
	protected Date esMayorHoyHasta;
	
	public Date getEsMenorIgualHoyDesde() {
		Calendar cal = Calendar.getInstance();
		esMenorIgualHoyDesde = cal.getTime();
		return esMenorIgualHoyDesde;
	}

	public void setEsMenorIgualHoyDesde(Date esMenorIgualHoyDesde) {
		this.esMenorIgualHoyDesde = esMenorIgualHoyDesde;
	}
	public Date getEsMenorIgualHoyHasta() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		esMenorIgualHoyHasta = cal.getTime();
		return esMenorIgualHoyHasta;
	}

	public void setEsMenorIgualHoyHasta(Date esMenorIgualHoyHasta) {
		this.esMenorIgualHoyHasta = esMenorIgualHoyHasta;
	}
	
	public Date getEsMayorHoyDesde() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);		
		cal.set(Calendar.MILLISECOND, 0);
		esMayorHoyDesde = cal.getTime();
		return esMayorHoyDesde;
	}

	public void setEsMayorHoyDesde(Date esMayorHoyDesde) {
		this.esMayorHoyDesde = esMayorHoyDesde;
	}
	
	public Date getEsMayorHoyHasta() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);		
		cal.set(Calendar.MILLISECOND, 999);
		esMayorHoyHasta = cal.getTime();
		return esMayorHoyHasta;
	}

	public void setEsMayorHoyHasta(Date esMayorHoyHasta) {
		this.esMayorHoyHasta = esMayorHoyHasta;
	}
	
}
