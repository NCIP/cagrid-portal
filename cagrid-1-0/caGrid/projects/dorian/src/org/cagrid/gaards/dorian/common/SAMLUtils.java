package org.cagrid.gaards.dorian.common;

import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.io.ByteArrayInputStream;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class SAMLUtils {



	public static SAMLAssertion stringToSAMLAssertion(String str)
			throws Exception {
		SAMLAssertion saml = new SAMLAssertion(new ByteArrayInputStream(str
				.getBytes()));
		return saml;
	}

	public static String samlAssertionToString(SAMLAssertion saml)
			throws Exception {
		String xml = saml.toString();
		return xml;
	}
}
