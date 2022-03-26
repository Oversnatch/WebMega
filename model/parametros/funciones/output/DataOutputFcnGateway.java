package com.americacg.cargavirtual.sube.model.parametros.funciones.output;

import com.americacg.cargavirtual.sube.model.ClaseBase;
import com.americacg.cargavirtual.sube.model.parametros.SeparadorTrama;

public abstract class DataOutputFcnGateway extends ClaseBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4221230140118567033L;
	private SeparadorTrama separadorTrama;
	private Integer codResultadoTRXSUBE;
	private Integer codRetornoSUBE; 
	
	public DataOutputFcnGateway(){
		super();
	}
	
	public SeparadorTrama getSeparadorTrama() {
		return separadorTrama;
	}

	public void setSeparadorTrama(SeparadorTrama separadorTrama) {
		this.separadorTrama = separadorTrama;
	}

	public Integer getCodResultadoTRXSUBE() {
		return codResultadoTRXSUBE;
	}

	public void setCodResultadoTRXSUBE(Integer codResultadoTRXSUBE) {
		this.codResultadoTRXSUBE = codResultadoTRXSUBE;
	}

	public Integer getCodRetornoSUBE() {
		return codRetornoSUBE;
	}

	public void setCodRetornoSUBE(Integer codRetornoSUBE) {
		this.codRetornoSUBE = codRetornoSUBE;
	}

	public abstract String obtenerTramaPOS();
}
