package net.handle.server;

/**
 * Signals that the class specified in the configuration file is invalid.
 */
public class InvalidSubclassException extends Exception {
	/**
	 * Constructs a new exception with the specified cause
	 *
	 * @param cause
	 * 	the cause for the exception.
	 */
	public InvalidSubclassException(Throwable cause) { super(cause); }
}

