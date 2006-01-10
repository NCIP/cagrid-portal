package gov.nih.nci.cagrid.introduce.codegen;

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

}
