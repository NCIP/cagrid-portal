package gov.nih.nci.cagrid.gums.common;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: GUMSInternalException.java,v 1.1 2005-09-27 18:31:18 langella Exp $
 */
public class GUMSInternalException extends Exception {
	public GUMSInternalException() {
		super();
	}


	public GUMSInternalException(String s) {
		super(s);
	}


	public GUMSInternalException(String s, Throwable t) {
		super(s, t);
	}

}