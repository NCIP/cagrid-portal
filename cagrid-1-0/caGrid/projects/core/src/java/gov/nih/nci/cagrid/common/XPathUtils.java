package gov.nih.nci.cagrid.common;

import java.util.Iterator;
import java.util.Map;


public class XPathUtils {

	/**
	 * This utilty takes an xpath that uses namespace prefixes (such as
	 * /a:B/a:C) and converts it to one without prefixes, by using the
	 * appropriate operators instead (such as
	 * /*[namespace-uri()='http://DOMAIN.COM/SCHEMA' and
	 * local-name()='B']/*[namespace-uri()='http://DOMAIN.COM/SCHEMA' and
	 * local-name()='C']). THe only conceivable use for this funciton is to
	 * write sane xpath and convert it to the insane xpath globus index service
	 * supports.
	 * 
	 * @param prefixedXpath
	 *            An xpath [optionally] using namespace prefixes in nodetests
	 * @param namespaces
	 *            A Map<String,String> keyed on namespace prefixes to resolve
	 *            in the xpath, where the value is the actual namespace that
	 *            should be used.
	 * @return a converted, conformant, xpath
	 */
	public static String translateXPath(String prefixedXpath, Map namespaces) {
		// don't process an empty Xpath, or one with not ns prefixes
		if (prefixedXpath == null || prefixedXpath.trim().length() == 0 || prefixedXpath.indexOf(":") < 0) {
			return prefixedXpath;
		} else if (namespaces == null || namespaces.keySet().size() == 0) {
			throw new IllegalArgumentException(
				"You specified an XPath with prefixes, yet didn't define any prefix mappings.");
		}

		// process all the replacements based on prefixes
		Iterator iterator = namespaces.keySet().iterator();
		while (iterator.hasNext()) {
			String prefix = (String) iterator.next();
			String ns = (String) namespaces.get(prefix);

			// replace the a:A$ stuff
			prefixedXpath = prefixedXpath.replaceAll(prefix + ":([a-zA-Z])?[\\s]*$", "*[namespace-uri()='" + ns
				+ "' and local-name()='$1']");

			// replace the a:A/ stuff
			prefixedXpath = prefixedXpath.replaceAll(prefix + ":([a-zA-Z])?/", "*[namespace-uri()='" + ns
				+ "' and local-name()='$1']/");

			// replace the /a:A[* stuff
			prefixedXpath = prefixedXpath.replaceAll(prefix + ":([a-zA-Z])?\\[", "*[namespace-uri()='" + ns
				+ "' and local-name()='$1' and ");

		}

		return prefixedXpath;
	}
}
