package gov.nih.nci.cagrid.common;

import java.util.Iterator;
import java.util.Map;


public class XPathUtils {

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
