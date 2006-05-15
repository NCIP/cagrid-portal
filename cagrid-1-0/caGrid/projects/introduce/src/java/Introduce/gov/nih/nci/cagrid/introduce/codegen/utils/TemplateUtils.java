package gov.nih.nci.cagrid.introduce.codegen.utils;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.info.NamespaceInformation;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Document;
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
	public static String getResourcePropertyVariableName(ResourcePropertiesListType metadataList, int index) {
		String baseName = metadataList.getResourceProperty(index).getQName().getLocalPart();

		int previousNumber = 0;
		for (int i = 0; (i < index && i < metadataList.getResourceProperty().length); i++) {
			ResourcePropertyType metadata = metadataList.getResourceProperty()[i];
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


	public static Map buildMasterNamespaceInformationMap(ServiceDescription desc) {
		Map map = new HashMap();
		int namespaceCount = 0;
		if (desc.getNamespaces() != null && desc.getNamespaces().getNamespace() != null) {
			for (int i = 0; i < desc.getNamespaces().getNamespace().length; i++) {
				NamespaceType ntype = desc.getNamespaces().getNamespace(i);
				// add the ns=>prefix entry
				if (!map.containsKey(ntype.getNamespace())) {
					if (ntype.getNamespace().equals(IntroduceConstants.W3CNAMESPACE)) {
						map.put(ntype.getNamespace(), new NamespaceInformation(ntype, IntroduceConstants.W3CNAMESPACE_PREFIX));
					} else {
						map.put(ntype.getNamespace(), new NamespaceInformation(ntype, "ns" + namespaceCount++));
					}
				}
			}
		}

		return map;
	}


	/**
	 * Walks a schema tree, following imports and placing namespaces in the namespaces set
	 * @param schemaFile
	 * 		The <i><b>FULLY QUALIFIED</i></b> file name of an XML schema
	 * @param namespaces
	 * 		The set of namespaces to populate
	 * @throws Exception
	 */
	public static void walkSchemasGetNamespaces(String schemaFile, Set namespaces) throws Exception {
		System.out.println("Looking at schema " + schemaFile);
		Document schema = XMLUtilities.fileNameToDocument(schemaFile);
		List importEls = schema.getRootElement().getChildren("import",
			schema.getRootElement().getNamespace(IntroduceConstants.W3CNAMESPACE));
		for (int i = 0; i < importEls.size(); i++) {
			org.jdom.Element importEl = (org.jdom.Element) importEls.get(i);
			String namespace = importEl.getAttributeValue("namespace");
			if (namespaces.add(namespace)) {
				System.out.println("adding namepace " + namespace);
			}
			String location = importEl.getAttributeValue("schemaLocation");
			File currentPath = new File(schemaFile).getCanonicalFile().getParentFile();
			if (!schemaFile.equals(currentPath.getCanonicalPath() + File.separator + location)) {
				File importedSchema = new File(currentPath + File.separator + location);
				walkSchemasGetNamespaces(importedSchema.getCanonicalPath(), namespaces);
			} else {
				System.err.println("WARNING: Schema is importing itself. " + schemaFile);
			}
		}
	}
}
