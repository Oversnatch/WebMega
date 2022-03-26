package com.americacg.cargavirtual.sube.model.parametros;

public class SeparadorTrama {
	private char inicioTrama;
	private char finTrama;
	private char separadorTrama;
	private char separadorHeaderData;

	public SeparadorTrama(){}

	public SeparadorTrama(char inicioTrama, char finTrama, char separadorTrama,
			char separadorHeaderData) {
		super();
		this.inicioTrama = inicioTrama;
		this.finTrama = finTrama;
		this.separadorTrama = separadorTrama;
		this.separadorHeaderData = separadorHeaderData;
	}

	public char getInicioTrama() {
		return inicioTrama;
	}

	public void setInicioTrama(char inicioTrama) {
		this.inicioTrama = inicioTrama;
	}

	public char getFinTrama() {
		return finTrama;
	}

	public void setFinTrama(char finTrama) {
		this.finTrama = finTrama;
	}

	public char getSeparadorTrama() {
		return separadorTrama;
	}

	public void setSeparadorTrama(char separadorTrama) {
		this.separadorTrama = separadorTrama;
	}

	public char getSeparadorHeaderData() {
		return separadorHeaderData;
	}

	public void setSeparadorHeaderData(char separadorHeaderData) {
		this.separadorHeaderData = separadorHeaderData;
	};
}
