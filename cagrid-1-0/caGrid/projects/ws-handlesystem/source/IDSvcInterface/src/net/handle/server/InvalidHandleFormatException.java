package net.handle.server;

/**
 * Signals that a resource has been registered, but that the storage format
 * was discovered to be invalid.  This could indicate corruption of the
 * repository.
 */
public class InvalidHandleFormatException extends Exception {
	/**
	 * Constructs a new exception with no detail message.
	 */
	public InvalidHandleFormatException() { super(); }
	/**
	 * Constructs a new exception with the specified detail message.
	 * 
	 * @param message
	 * 	the detail message, which can be retrieved by the
	 * 	{@link Throwable#getMessage()} method.
	 */
	public InvalidHandleFormatException(String message) { super(message); }
}

