package gov.nih.nci.cagrid.gums.common;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
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