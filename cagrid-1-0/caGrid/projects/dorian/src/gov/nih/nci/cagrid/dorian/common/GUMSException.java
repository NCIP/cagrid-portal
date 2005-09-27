package gov.nih.nci.cagrid.gums.common;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: GUMSException.java,v 1.1 2005-09-27 18:31:18 langella Exp $
 */
public class GUMSException extends Exception {
	public GUMSException() {
		super();
	}


	public GUMSException(String s) {
		super(s);
	}


	public GUMSException(String s, Throwable t) {
		super(s, t);
	}

}