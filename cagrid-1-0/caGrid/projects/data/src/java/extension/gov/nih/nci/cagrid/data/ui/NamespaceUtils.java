package gov.nih.nci.cagrid.data.ui;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.projectmobius.client.gme.ImportInfo;
import org.projectmobius.common.MalformedNamespaceException;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.common.gme.NoSuchSchemaException;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.portal.PortalResourceManager;
import org.projectmobius.protocol.gme.SchemaNode;

/** 
 *  NamespaceUtils
 *  Some utilities for making namespace manipulation and discovery easier
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jul 21, 2006 
 * @version $Id$ 
 */
public class NamespaceUtils {

	public static NamespaceType getServiceNamespaceType(ServiceInformation info, String namespace) {
		NamespaceType[] serviceNamespaces = info.getNamespaces().getNamespace();
		for (int i = 0; i < serviceNamespaces.length; i++) {
			if (serviceNamespaces[i].getNamespace().equals(namespace)) {
				return serviceNamespaces[i];
			}
		}
		return null;
	}
	
	
	public static String createNamespaceString(Project project, UMLPackageMetadata pack) {
		return createNamespaceString(project.getShortName(), project.getVersion(), pack.getName());
	}
	
	
	public static String createNamespaceString(String projectShortName, String projectVersion, String packName) {
		if (projectVersion.indexOf(".") == -1) {
			projectVersion += ".0";
		}
		return "gme://" + projectShortName + ".caBIG/" + projectVersion + "/" + packName;
	}
	
	
	public static NamespaceType createNamespaceFromUmlPackage(Project project, 
		UMLPackageMetadata pack, XMLDataModelService gmeHandle, File schemaDir) throws Exception {
		String namespaceString = null;
		if (project != null) {
			namespaceString = createNamespaceString(project, pack);
			NamespaceType nsType = new NamespaceType();
			Namespace namespace = null;
			// get the namespace, either from the generated string, 
			// or let the user clean it up if it won't parse
			do {
				try {
					namespace = new Namespace(namespaceString);
				} catch (MalformedNamespaceException ex) {
					// show error message
					String[] error = {
						namespaceString,
						"could not be parsed as a namespace:",
						ex.getMessage(),
						"Specify a corrected namespace."
					};
					JOptionPane.showMessageDialog(PortalResourceManager.getInstance().getGridPortal(), 
						error, "Malformed Namespace", JOptionPane.ERROR_MESSAGE);
					// ask for a correct one
					namespaceString = JOptionPane.showInputDialog(PortalResourceManager.getInstance().getGridPortal(),
						"Specify Corrected Namespace", namespaceString);
					if (namespaceString == null) {
						// user canceled correcting namespace
						throw new MalformedNamespaceException(ex.getMessage(), ex);
					}
				}
			} while (namespace == null);
			
			// get administered namespace domains of the GME
			List namespaceDomainList = gmeHandle.getNamespaceDomainList();
			// verify the namespace domain is in the gme's list
			if (!namespaceDomainList.contains(namespace.getDomain())) {
				// prompt for alternate
				String alternativeDomain = (String) JOptionPane.showInputDialog(
					PortalResourceManager.getInstance().getGridPortal(),
					"The GME does not appear to contain schemas under the specified domain.\n"
					+ "Select an alternative domain, or cancel if no viable option is available.\n"
					+ "\nExpected domain: " + namespace.getDomain(), "Schema Location Error",
					JOptionPane.ERROR_MESSAGE, null, namespaceDomainList.toArray(), null);
				
				if (alternativeDomain != null) {
					namespace = new Namespace(namespace.getProtocol() + "://" + alternativeDomain + "/"
						+ namespace.getName());
				} else {
					return null;
				}
			}
			
			// get the schema contents for the namespace
			String schemaContents = null;
			try {
				schemaContents = getSchema(gmeHandle, namespace);
			} catch (NoSuchSchemaException e) {
				// prompt for alternate
				List schemas = gmeHandle.getSchemaListForNamespaceDomain(namespace.getDomain());
				Namespace alternativeSchema = (Namespace) JOptionPane.showInputDialog(
					PortalResourceManager.getInstance().getGridPortal(),
					"Unable to locate schema for the selected caDSR package.\n"
					+ "This package may not have a published Schema."
					+ "\nSelect an alternative Schema, or cancel.\n\nExpected schema: " + namespace.getName(),
					"Schema Location Error", JOptionPane.ERROR_MESSAGE, null, schemas.toArray(), null);
				
				if (alternativeSchema != null) {
					namespace = alternativeSchema;
				} else {
					return null;
				}
				schemaContents = getSchema(gmeHandle, namespace);
			}
			
			// set the package name
			String packageName = CommonTools.getPackageName(namespace);
			nsType.setPackageName(packageName);
			
			// set the raw namespace
			nsType.setNamespace(namespace.getRaw());
			
			// get the file system name for the namespace
			ImportInfo ii = new ImportInfo(namespace);
			nsType.setLocation("./" + ii.getFileName());
			
			// popualte the schema elements
			gov.nih.nci.cagrid.introduce.portal.extension.ExtensionTools.setSchemaElements(
				nsType, XMLUtilities.stringToDocument(schemaContents));
			// write the schema and its imports to the filesystem
			gmeHandle.cacheSchema(namespace, schemaDir);
			// TODO: cacheSchema returns a List of the files it stored.
			// Those should be cateloged somewhere so they can be cleaned up if need be
			return nsType;
		}
		return null;
	}
	
	
	/**
	 * Makes a map from class name to a element name for that class.
	 * Classes for which no type can be found are mapped to <code>null</code>
	 * @param classes
	 * @param nsType
	 * @return
	 * 		Map from class name to element name
	 */
	public static Map mapClassesToElementNames(UMLClassMetadata[] classes, NamespaceType nsType) {
		Map mapping = new HashMap();
		for (int i = 0; i < classes.length; i++) {
			UMLClassMetadata currentClass = classes[i];
			SchemaElementType mappedType = null;
			for (int j = 0; j < nsType.getSchemaElement().length; j++) {
				SchemaElementType type = nsType.getSchemaElement(j);
				if (type.getType().equals(currentClass.getName())) {
					mappedType = type;
					break;
				}
			}
			mapping.put(currentClass.getName(), mappedType == null ? null : mappedType.getType());
		}
		return mapping;
	}
	
	
	public static SchemaElementType getElementByName(NamespaceType nsType, String typeName) {
		if (nsType.getSchemaElement() != null) {
			for (int i = 0; i < nsType.getSchemaElement().length; i++) {
				if (nsType.getSchemaElement(i).getType().equals(typeName)) {
					return nsType.getSchemaElement(i);
				}
			}
		}
		return null;
	}
	
	
	private static String getSchema(XMLDataModelService gmeHandle, Namespace namespace) throws Exception {
		SchemaNode schema = gmeHandle.getSchema(namespace, false);
		return schema.getSchemaContents();
	}
}
