package gov.nih.nci.cagrid.dorian.common;



/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class AddressValidator {

	public static void validateEmail(String email) throws IllegalArgumentException {
		if (email.indexOf("@") == -1) {
			throw new IllegalArgumentException("Invalid email address specified.");
		}
	}

}
