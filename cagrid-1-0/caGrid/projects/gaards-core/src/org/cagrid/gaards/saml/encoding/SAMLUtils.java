package org.cagrid.gaards.saml.encoding;

import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.io.ByteArrayInputStream;

import org.w3c.dom.Element;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class SAMLUtils {

	public static SAMLAssertion domToSAMLAssertion(Element dom)
			throws Exception {
		SAMLAssertion saml = new SAMLAssertion(dom);
		return saml;
	}

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

	public static Element samlAssertionToDOM(SAMLAssertion saml)
			throws Exception {
		return (Element)saml.toDOM();
	}
}
