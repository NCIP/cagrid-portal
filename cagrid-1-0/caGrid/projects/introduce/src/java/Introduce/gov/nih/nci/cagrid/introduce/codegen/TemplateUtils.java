package gov.nih.nci.cagrid.introduce.codegen;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ServiceInformation;
import gov.nih.nci.cagrid.introduce.beans.metadata.MetadataListType;
import gov.nih.nci.cagrid.introduce.beans.metadata.MetadataType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.common.MalformedNamespaceException;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.XMLUtilities;


/**
 * Templating Utility Functions
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 8, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class TemplateUtils {

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
	public static String getResourcePropertyVariableName(MetadataListType metadataList, int index) {
		String baseName = metadataList.getMetadata(index).getQName().getLocalPart();

		int previousNumber = 0;
		for (int i = 0; (i < index && i < metadataList.getMetadata().length); i++) {
			MetadataType metadata = metadataList.getMetadata()[i];
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
	public static Map buildQNameNamespacePrefixMap(MetadataListType metadataList) {
		Map map = new HashMap();
		for (int i = 0; i < metadataList.getMetadata().length; i++) {
			MetadataType metadata = metadataList.getMetadata()[i];
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


	public static Map buildMasterNamespaceSchemaInformationMap(ServiceInformation info) {
		Map map = new HashMap();
		if (info.getMetadata() != null && info.getMetadata().getMetadata() != null) {
			MetadataListType metadataList = info.getMetadata();
			for (int i = 0; i < metadataList.getMetadata().length; i++) {
				MetadataType metadata = metadataList.getMetadata()[i];
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
				map.put(qnameNamespace, new SchemaInformation(metadata.getPackageName(), qnameNamespace, prefix,
					location));
			}
		}

		if (info.getMethods() != null) {
			if (info.getMethods().getMethod() != null) {
				for (int methodI = 0; methodI < info.getMethods().getMethod().length; methodI++) {
					MethodType method = info.getMethods().getMethod(methodI);
					if (method.getInputs() != null && method.getInputs().getInput() != null) {
						for (int inputI = 0; inputI < method.getInputs().getInput().length; inputI++) {
							MethodTypeInputsInput inputParam = method.getInputs().getInput(inputI);
							String qnameName = inputParam.getType();
							String qnameNamespace = inputParam.getNamespace();
							if (!qnameNamespace.equals(IntroduceConstants.W3CNAMESPACE)) {
								String location = inputParam.getLocation();

								String prefixBase = qnameName.toLowerCase().substring(0,
									Math.min(qnameName.length(), 4));
								int previousNumber = 0;
								String prefix = prefixBase
									+ ((previousNumber > 0) ? String.valueOf(previousNumber) : "");
								while (map.containsValue(prefix)) {
									previousNumber++;
									prefix = prefixBase + ((previousNumber > 0) ? String.valueOf(previousNumber) : "");
								}
								// add the ns=>prefix entry
								map.put(qnameNamespace, new SchemaInformation(inputParam.getPackageName(),
									qnameNamespace, prefix, location));
							} else {
								inputParam.setPackageName("");
								map.put(qnameNamespace, new SchemaInformation("",
									qnameNamespace, "xs", ""));
							}
						}
					}
					if (method.getOutput() != null) {
						MethodTypeOutput outputParam = method.getOutput();
						if ((outputParam.getClassName()==null) || (outputParam.getClassName() != null && !outputParam.getClassName().equals("void"))) {
							String qnameName = outputParam.getType();
							String qnameNamespace = outputParam.getNamespace();
							String location = outputParam.getLocation();

							if (!qnameNamespace.equals(IntroduceConstants.W3CNAMESPACE)) {

								String prefixBase = qnameName.toLowerCase().substring(0,
									Math.min(qnameName.length(), 4));
								int previousNumber = 0;
								String prefix = prefixBase
									+ ((previousNumber > 0) ? String.valueOf(previousNumber) : "");
								while (map.containsValue(prefix)) {
									previousNumber++;
									prefix = prefixBase + ((previousNumber > 0) ? String.valueOf(previousNumber) : "");
								}
								// add the ns=>prefix entry
								map.put(qnameNamespace, new SchemaInformation(outputParam.getPackageName(),
									qnameNamespace, prefix, location));
							} else {
								outputParam.setPackageName("");
								map.put(qnameNamespace, new SchemaInformation("",
									qnameNamespace, "xs", ""));
							}
						}
					}
				}
			}
		}

		return map;
	}


	public static void walkSchemasGetNamespaces(File schemaDir, String fileName, Set namespaces){
		try {
			System.out.println("Looking at schema " + fileName);
			Document schema = XMLUtilities.fileNameToDocument(fileName);
			List importEls = schema.getRootElement().getChildren("import",schema.getRootElement().getNamespace(IntroduceConstants.W3CNAMESPACE));
			for(int i=0;i<importEls.size();i++){
				org.jdom.Element importEl = (org.jdom.Element)importEls.get(i);
				String namespace = importEl.getAttributeValue("namespace");
				namespaces.add(namespace);
				System.out.println("adding namepace " + namespace);
				String location = importEl.getAttributeValue("location");
				walkSchemasGetNamespaces(schemaDir, schemaDir + File.separator + location,namespaces);
			}
		} catch (MobiusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}


	public static  String getPackageName(Namespace namespace) {
		StringTokenizer tokenizer = new StringTokenizer(namespace.getDomain(), ".", true);
		StringBuffer packageNameBuf = new StringBuffer();
		while (tokenizer.hasMoreElements()) {
			packageNameBuf.insert(0, tokenizer.nextToken());
		}
		return packageNameBuf.toString();
	}
}
