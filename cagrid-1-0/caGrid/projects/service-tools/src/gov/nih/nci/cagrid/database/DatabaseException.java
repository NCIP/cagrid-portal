package gov.nih.nci.cagrid.database;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: DatabaseException.java,v 1.1 2007-09-12 19:52:28 langella Exp $
 */
public class DatabaseException extends Exception {

	public DatabaseException() {
		super();
	}


	public DatabaseException(String message) {
		super(message);
	}


	public DatabaseException(Throwable cause) {
		super(cause);
	}


	public DatabaseException(String message, Throwable cause) {
		super(message, cause);
	}

}
