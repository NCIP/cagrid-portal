package gov.nih.nci.cagrid.introduce.portal.extension;

import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ResourcePropertyEditorExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.discoverytools.NamespaceTypeToolsComponent;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;


public class ExtensionTools {
	public static CreationExtensionUIDialog getCreationUIDialog(String extensionName,
		gov.nih.nci.cagrid.introduce.info.ServiceInformation info) throws Exception {
		ServiceExtensionDescriptionType extensionDesc = ExtensionsLoader.getInstance().getServiceExtension(
			extensionName);
		if (extensionDesc != null && extensionDesc.getCreationUIDialog() != null
			&& !extensionDesc.getCreationUIDialog().equals("")) {
			Class c = Class.forName(extensionDesc.getCreationUIDialog());
			Constructor con = c.getConstructor(new Class[]{ServiceExtensionDescriptionType.class,
					ServiceInformation.class});
			Object obj = con.newInstance(new Object[]{extensionDesc, info});
			return (CreationExtensionUIDialog) obj;
		}
		return null;
	}


	public static ServiceModificationUIPanel getServiceModificationUIPanel(String extensionName,
		gov.nih.nci.cagrid.introduce.info.ServiceInformation info) throws Exception {
		ServiceExtensionDescriptionType extensionDesc = ExtensionsLoader.getInstance().getServiceExtension(
			extensionName);
		if (extensionDesc != null && extensionDesc.getServiceModificationUIPanel() != null
			&& !extensionDesc.getServiceModificationUIPanel().equals("")) {
			Class c = Class.forName(extensionDesc.getServiceModificationUIPanel());
			Constructor con = c.getConstructor(new Class[]{ServiceExtensionDescriptionType.class,
					ServiceInformation.class});
			Object obj = con.newInstance(new Object[]{extensionDesc, info});
			return (ServiceModificationUIPanel) obj;
		}
		return null;
	}


	public static ResourcePropertyEditorPanel getMetadataEditorComponent(String extensionName, Document doc,
		File schemaFile, File schemaDir) throws Exception {
		ResourcePropertyEditorExtensionDescriptionType extensionDesc = ExtensionsLoader.getInstance()
			.getResourcePropertyEditorExtension(extensionName);
		if (extensionDesc != null && extensionDesc.getResourcePropertyEditorPanel() != null
			&& !extensionDesc.getResourcePropertyEditorPanel().equals("")) {
			Class c = Class.forName(extensionDesc.getResourcePropertyEditorPanel());
			Constructor con = c.getConstructor(new Class[]{Document.class, File.class, File.class});
			con.newInstance(new Object[]{doc, schemaFile, schemaDir});
			Object obj = c.newInstance();
			return (ResourcePropertyEditorPanel) obj;
		}
		return null;
	}


	public static ExtensionsPreferencesConfigurationPanel getExtensionsPreferencesConfigurationPanel(
		String extensionName) throws Exception {
		ExtensionDescription extensionDesc = ExtensionsLoader.getInstance().getExtension(extensionName);
		if (extensionDesc != null && extensionDesc.getExtensionPreferencesPanel() != null
			&& !extensionDesc.getExtensionPreferencesPanel().equals("")) {
			Class c = Class.forName(extensionDesc.getExtensionPreferencesPanel());
			Constructor con = c.getConstructor(new Class[]{ExtensionDescription.class});
			Object obj = con.newInstance(new Object[]{extensionDesc});
			return (ExtensionsPreferencesConfigurationPanel) obj;
		}
		return null;
	}


	public static NamespaceTypeToolsComponent getNamespaceTypeToolsComponent(String extensionName) throws Exception {
		DiscoveryExtensionDescriptionType extensionD = ExtensionsLoader.getInstance().getDiscoveryExtension(
			extensionName);
		if (extensionD != null && extensionD.getDiscoveryToolsPanelExtension() != null
			&& !extensionD.getDiscoveryToolsPanelExtension().equals("")) {
			Class c = Class.forName(extensionD.getDiscoveryToolsPanelExtension());
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
			Class c = Class.forName(extensionD.getDiscoveryPanelExtension());
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


	public static ResourcePropertyEditorExtensionDescriptionType getResourcePropertyEditorExtensionDescriptor(
		javax.xml.namespace.QName qname) {
		ResourcePropertyEditorExtensionDescriptionType mde = null;
		List metadataExtensions = ExtensionsLoader.getInstance().getResourcePropertyEditorExtensions();
		for (int i = 0; i < metadataExtensions.size(); i++) {
			ResourcePropertyEditorExtensionDescriptionType tmde = (ResourcePropertyEditorExtensionDescriptionType) metadataExtensions
				.get(i);
			if (tmde.getQname().equals(qname)) {
				mde = tmde;
				break;
			}
		}
		return mde;
	}

}
