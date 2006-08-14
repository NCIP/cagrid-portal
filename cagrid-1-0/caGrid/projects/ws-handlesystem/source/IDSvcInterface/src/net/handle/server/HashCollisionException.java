package net.handle.server;

/**
 * Signals the (unlikely) event that a cryptographic digest collision
 * was found.
 */
class HashCollisionException extends Exception {
	/**
	 * Constructs a new exception with the specified detail message.
	 * 
	 * @param message
	 * 	the detail message, which can be retrieved by the
	 * 	{@link Throwable#getMessage()} method.
	 */
	public HashCollisionException(String message) { super(message); }
}

