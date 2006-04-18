package gov.nih.nci.cagrid.introduce.portal;

import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.Properties;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.extension.ExtensionClassLoader;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.portal.discoverytools.NamespaceTypeToolsComponent;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;


public class ExtensionTools {

	public static NamespaceTypeToolsComponent getNamespaceTypeToolsComponent(String extensionName) throws Exception {
		DiscoveryExtensionDescriptionType extensionD = ExtensionsLoader.getInstance().getDiscoveryExtension(
			extensionName);
		if (extensionD != null && extensionD.getDiscoveryToolsPanelExtension() != null
			&& !extensionD.getDiscoveryToolsPanelExtension().equals("")) {
			ExtensionClassLoader cloader = new ExtensionClassLoader(new File(ExtensionsLoader.getInstance()
				.getExtensionsDir()
				+ File.separator

				+ extensionName));
			Class c = cloader.loadClass(extensionD.getDiscoveryToolsPanelExtension());
			Constructor con = c.getConstructor(new Class[]{DiscoveryExtensionDescriptionType.class});
			Object obj = con.newInstance(new Object[]{extensionD});
			return (NamespaceTypeToolsComponent) obj;
		}
		return null;
	}


	public static NamespaceTypeDiscoveryComponent getNamespaceTypeDiscoveryComponent(String extensionName)
		throws Exception {
		DiscoveryExtensionDescriptionType extensionD = ExtensionsLoader.getInstance().getDiscoveryExtension(
			extensionName);
		if (extensionD != null && extensionD.getDiscoveryPanelExtension() != null
			&& !extensionD.getDiscoveryPanelExtension().equals("")) {
			ExtensionClassLoader cloader = new ExtensionClassLoader(new File(ExtensionsLoader.getInstance()
				.getExtensionsDir()
				+ File.separator

				+ extensionName));
			Class c = cloader.loadClass(extensionD.getDiscoveryPanelExtension());
			Constructor con = c.getConstructor(new Class[]{DiscoveryExtensionDescriptionType.class});
			Object obj = con.newInstance(new Object[]{extensionD});
			return (NamespaceTypeDiscoveryComponent) obj;
		}
		return null;
	}


	/**
	 * Reads the schema and sets the SchemaElements of the NamespaceType for
	 * each.
	 * 
	 * @param namespace
	 *            the namespace to populate
	 * @param schemaContents
	 *            the schema's text contents
	 * @throws Exception
	 */
	public static void setSchemaElements(NamespaceType namespace, Document schemaContents) throws Exception {
		List elementTypes = schemaContents.getRootElement().getChildren("element",
			schemaContents.getRootElement().getNamespace());
		SchemaElementType[] schemaTypes = new SchemaElementType[elementTypes.size()];
		for (int i = 0; i < elementTypes.size(); i++) {
			Element element = (Element) elementTypes.get(i);
			SchemaElementType type = new SchemaElementType();
			type.setType(element.getAttributeValue("name"));
			schemaTypes[i] = type;
		}
		namespace.setSchemaElement(schemaTypes);
	}

}
