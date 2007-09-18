package org.cagrid.gme.common.exceptions;

public class InitializationException extends Exception {

	public InitializationException() {
	}


	public InitializationException(String message) {
		super(message);
	}


	public InitializationException(Throwable cause) {
		super(cause);
	}


	public InitializationException(String message, Throwable cause) {
		super(message, cause);
	}

}
