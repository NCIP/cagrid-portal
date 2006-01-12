package gov.nih.nci.cagrid.introduce.codegen;

import java.util.HashMap;
import java.util.Map;

import gov.nih.nci.cagrid.introduce.ServiceInformation;
import gov.nih.nci.cagrid.introduce.beans.metadata.ServiceMetadataListType;
import gov.nih.nci.cagrid.introduce.beans.metadata.ServiceMetadataType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsTypeMethod;


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
		return lowerCaseFirstCharacter(baseName + ((previousNumber > 0) ? String.valueOf(previousNumber) : ""));

	}


	/**
	 * Returns the input string with the first character converted to lowercase
	 * 
	 * @param variableName
	 *            string to fix
	 * @return the input string with the first character converted to lowercase
	 */
	public static String lowerCaseFirstCharacter(String variableName) {
		return variableName.substring(0, 1).toLowerCase() + variableName.substring(1);
	}


	/**
	 * Returns the input string with the first character converted to uppercase
	 * 
	 * @param variableName
	 *            string to fix
	 * @return the input string with the first character converted to uppercase
	 */
	public static String upperCaseFirstCharacter(String variableName) {
		return variableName.substring(0, 1).toUpperCase() + variableName.substring(1);
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


	public static Map buildMasterNamespacePrefixMap(ServiceInformation info) {
		Map map = new HashMap();
		if (info.getMetadata() != null && info.getMetadata().getMetadata() != null) {
			ServiceMetadataListType metadataList = info.getMetadata();
			for (int i = 0; i < metadataList.getMetadata().length; i++) {
				ServiceMetadataType metadata = metadataList.getMetadata()[i];
				String qnameName = metadata.getQName().getLocalPart();
				String qnameNamespace = metadata.getQName().getNamespaceURI();
				String location = metadata.getLocation();

				String prefixBase = qnameName.toLowerCase().substring(0, Math.min(qnameName.length(), 4));
				int previousNumber = 0;
				String prefix = prefixBase + ((previousNumber > 0) ? String.valueOf(previousNumber) : "");
				while (map.containsValue(prefix)) {
					previousNumber++;
					prefix = prefixBase + ((previousNumber > 0) ? String.valueOf(previousNumber) : "");
				}
				// add the ns=>prefix entry
				map.put(qnameNamespace, new SchemaInformation(qnameNamespace, prefix, location));
			}
		}

		if (info.getMethods() != null) {
			if (info.getMethods().getMethod() != null) {
				for (int methodI = 0; methodI < info.getMethods().getMethod().length; methodI++) {
					MethodsTypeMethod method = info.getMethods().getMethod(methodI);
					if (method.getInputs() != null && method.getInputs().getInput() != null) {
						for (int inputI = 0; inputI < method.getInputs().getInput().length; inputI++) {
							MethodTypeInputsInput inputParam = method.getInputs().getInput(inputI);
							String qnameName = inputParam.getType();
							String qnameNamespace = inputParam.getNamespace();
							String location = inputParam.getLocation();

							String prefixBase = qnameName.toLowerCase().substring(0, Math.min(qnameName.length(), 4));
							int previousNumber = 0;
							String prefix = prefixBase + ((previousNumber > 0) ? String.valueOf(previousNumber) : "");
							while (map.containsValue(prefix)) {
								previousNumber++;
								prefix = prefixBase + ((previousNumber > 0) ? String.valueOf(previousNumber) : "");
							}
							// add the ns=>prefix entry
							map.put(qnameNamespace, new SchemaInformation(qnameNamespace, prefix, location));
						}
					}
					if (method.getOutput() != null) {
						MethodTypeOutput outputParam = method.getOutput();
						if (outputParam.getClassName() != null && !outputParam.getClassName().equals("void")) {
							String qnameName = outputParam.getType();
							String qnameNamespace = outputParam.getNamespace();
							String location = outputParam.getLocation();

							String prefixBase = qnameName.toLowerCase().substring(0, Math.min(qnameName.length(), 4));
							int previousNumber = 0;
							String prefix = prefixBase + ((previousNumber > 0) ? String.valueOf(previousNumber) : "");
							while (map.containsValue(prefix)) {
								previousNumber++;
								prefix = prefixBase + ((previousNumber > 0) ? String.valueOf(previousNumber) : "");
							}
							// add the ns=>prefix entry
							map.put(qnameNamespace, new SchemaInformation(qnameNamespace, prefix, location));
						}
					}
				}
			}
		}

		return map;
	}
}
