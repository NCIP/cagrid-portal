package gov.nih.nci.cagrid.security.commstyle;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: CommunicationStyleException.java,v 1.1 2005-09-27 20:09:50 langella Exp $
 */
public class CommunicationStyleException extends Exception {
	public CommunicationStyleException() {
		super();
	}


	public CommunicationStyleException(String s) {
		super(s);
	}


	public CommunicationStyleException(String s, Throwable t) {
		super(s, t);
	}

}