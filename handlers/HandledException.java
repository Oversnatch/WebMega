package com.americacg.cargavirtual.web.handlers;

public class HandledException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5337499711762735131L;
	private String code;

    public HandledException(String code, String message) {
        super(message);
        this.setCode(code);
    }

    public HandledException(String code, String message, Throwable cause) {
        super(message, cause);
        this.setCode(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
