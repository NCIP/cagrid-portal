package net.handle.server;

/**
 * Signals that the configuration file config.dct was not found in the
 * specified Handle repository directory.
 */
public class BadConfigurationFileException extends Exception {
	/**
	 * Constructs a new exception with the specified cause
	 *
	 * @param cause
	 * 	the cause for the exception.
	 */
	public BadConfigurationFileException(Throwable cause) { super(cause); }
}

