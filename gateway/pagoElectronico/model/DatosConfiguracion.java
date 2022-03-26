package com.americacg.cargavirtual.gateway.pagoElectronico.model;

public class DatosConfiguracion {

	private String pathlog;
	private String pathlogWin;
	private String conciliacionPath;
	private String conciliacionPathWin;
	private String logFileName;
	private String logFileNameScheduler;
	private Boolean bloqueoClaves = false;
	private Integer maxIntentosLoginFallidosConsec = 5;

	public String getPathlog() {
		return pathlog;
	}

	public void setPathlog(String pathlog) {
		this.pathlog = pathlog;
	}

	public String getPathlogWin() {
		return pathlogWin;
	}

	public void setPathlogWin(String pathlogWin) {
		this.pathlogWin = pathlogWin;
	}

	public String getConciliacionPath() {
		return conciliacionPath;
	}

	public void setConciliacionPath(String conciliacionPath) {
		this.conciliacionPath = conciliacionPath;
	}

	public String getConciliacionPathWin() {
		return conciliacionPathWin;
	}

	public void setConciliacionPathWin(String conciliacionPathWin) {
		this.conciliacionPathWin = conciliacionPathWin;
	}

	public Integer getMaxIntentosLoginFallidosConsec() {
		return maxIntentosLoginFallidosConsec;
	}

	public void setMaxIntentosLoginFallidosConsec(
			Integer maxIntentosLoginFallidosConsec) {
		this.maxIntentosLoginFallidosConsec = maxIntentosLoginFallidosConsec;
	}

	public String getLogFileName() {
		return logFileName;
	}

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}

	public String getLogFileNameScheduler() {
		return logFileNameScheduler;
	}

	public void setLogFileNameScheduler(String logFileNameScheduler) {
		this.logFileNameScheduler = logFileNameScheduler;
	}

	public Boolean getBloqueoClaves() {
		return bloqueoClaves;
	}

	public void setBloqueoClaves(Boolean bloqueoClaves) {
		this.bloqueoClaves = bloqueoClaves;
	}

}
