package gov.nih.nci.cagrid.introduce.codegen;

import java.util.HashMap;
import java.util.Map;

import gov.nih.nci.cagrid.introduce.beans.metadata.ServiceMetadataListType;
import gov.nih.nci.cagrid.introduce.beans.metadata.ServiceMetadataType;


public class MetadataTemplateUtils {

	/**
	 * Define a unique name for use as a variable for the metadata at the
	 * specified index given the scope of the ServiceMetadataListType.
	 * 
	 * @param metadataList
	 *            the list of metadata
	 * @param index
	 *            the index into the metadata list of the targeted metadata item
	 * @return the variable name to use
	 */
	public static String getResourcePropertyVariableName(ServiceMetadataListType metadataList, int index) {
		String baseName = metadataList.getMetadata(index).getQName().getLocalPart();

		int previousNumber = 0;
		for (int i = 0; (i < index && i < metadataList.getMetadata().length); i++) {
			ServiceMetadataType metadata = metadataList.getMetadata()[i];
			if (metadata.getQName().getLocalPart().equalsIgnoreCase(baseName)) {
				// the qname local parts are the same for multiple qnames
				// resolve the issue by appending a number
				previousNumber++;
			}
		}

		// return the orginal name, if it is unique, otherwise append a number
		return baseName + ((previousNumber > 0) ? String.valueOf(previousNumber) : "");

	}


	/**
	 * Build a map of of namespace->prefix definitions for the namespaces of all
	 * of the QNames in the list
	 * 
	 * @param metadataList
	 *            the list of metadata
	 * @return Map of namespace->prefix definitions for the namespaces of all of
	 *         the QNames in the list
	 */
	public static Map buildQNameNamespacePrefixMap(ServiceMetadataListType metadataList) {
		Map map = new HashMap();
		for (int i = 0; i < metadataList.getMetadata().length; i++) {
			ServiceMetadataType metadata = metadataList.getMetadata()[i];
			String qnameName = metadata.getQName().getLocalPart();
			String qnameNamespace = metadata.getQName().getNamespaceURI();

			String prefixBase = qnameName.toLowerCase().substring(0, Math.min(qnameName.length(), 4));
			int previousNumber = 0;
			String prefix = prefixBase + ((previousNumber > 0) ? String.valueOf(previousNumber) : "");
			while (map.containsValue(prefix)) {
				previousNumber++;
				prefix = prefixBase + ((previousNumber > 0) ? String.valueOf(previousNumber) : "");
			}
			// add the ns=>prefix entry
			map.put(qnameNamespace, prefix);
		}

		return map;
	}
}
