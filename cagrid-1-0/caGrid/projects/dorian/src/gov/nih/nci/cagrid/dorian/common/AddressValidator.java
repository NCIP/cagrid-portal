package gov.nih.nci.cagrid.gums.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class AddressValidator {

	// precompiled patterns
	// notice double escape of special characters
	private static Pattern phonePattern = Pattern
			.compile("\\d\\d\\d-\\d\\d\\d-\\d\\d\\d\\d");

	private static Pattern zipPattern = Pattern.compile("\\d\\d\\d\\d\\d");

	public static void validatePhone(String phone)
			throws IllegalArgumentException {
		if (phone == null) {
			throw new IllegalArgumentException(
					"No phone number specified, correct format is 555-555-5555");
		}
		Matcher phoneMat = phonePattern.matcher(phone);
		if (!phoneMat.matches()) {
			throw new IllegalArgumentException(
					"Invalid phone number, correct format is 555-555-5555");
		}
	}

	public static void validateEmail(String email)
			throws IllegalArgumentException {
		if (email.indexOf("@") == -1) {
			throw new IllegalArgumentException(
					"Invalid email address specified.");
		}
	}

	public static void validateZipCode(String zip)
			throws IllegalArgumentException {
		if (zip == null) {
			throw new IllegalArgumentException(
					"No zip code specified, correct format is 55555");
		}
		Matcher zipMat = zipPattern.matcher(zip);
		if (!zipMat.matches()) {
			throw new IllegalArgumentException(
					"Invalid phone number, correct format is 55555");
		}
	}
}
