package gov.nih.nci.cagrid.introduce.portal;

import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.Properties;
import gov.nih.nci.cagrid.introduce.extension.ExtensionClassLoader;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.portal.discoverytools.NamespaceTypeToolsComponent;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.io.File;
import java.lang.reflect.Constructor;


public class ExtensionTools {
	
	public static NamespaceTypeToolsComponent getNamespaceTypeToolsComponent(String extensionName) throws Exception {
		DiscoveryExtensionDescriptionType extensionD = ExtensionsLoader.getInstance().getDiscoveryExtension(extensionName);
		if (extensionD != null && extensionD.getDiscoveryToolsPanelExtension() != null
			&& !extensionD.getDiscoveryToolsPanelExtension().equals("")) {
			ExtensionClassLoader cloader = new ExtensionClassLoader(new File(ExtensionsLoader.getInstance().getExtensionsDir() + File.separator

			+ extensionName));
			Class c = cloader.loadClass(extensionD.getDiscoveryToolsPanelExtension());
			Constructor con = c.getConstructor(new Class[]{DiscoveryExtensionDescriptionType.class});
			Object obj = con.newInstance(new Object[]{extensionD});
			return (NamespaceTypeToolsComponent) obj;
		}
		return null;
	}
	
	public static NamespaceTypeDiscoveryComponent getNamespaceTypeDiscoveryComponent(String extensionName) throws Exception {
		DiscoveryExtensionDescriptionType extensionD = ExtensionsLoader.getInstance().getDiscoveryExtension(extensionName);
		if (extensionD != null && extensionD.getDiscoveryPanelExtension() != null
			&& !extensionD.getDiscoveryPanelExtension().equals("")) {
			ExtensionClassLoader cloader = new ExtensionClassLoader(new File(ExtensionsLoader.getInstance().getExtensionsDir() + File.separator

			+ extensionName));
			Class c = cloader.loadClass(extensionD.getDiscoveryPanelExtension());
			Constructor con = c.getConstructor(new Class[]{DiscoveryExtensionDescriptionType.class});
			Object obj = con.newInstance(new Object[]{extensionD});
			return (NamespaceTypeDiscoveryComponent) obj;
		}
		return null;
	}


	public static String getProperty(Properties properties, String key) {
		String value = null;
		if (properties.getProperty() != null) {
			for (int i = 0; i < properties.getProperty().length; i++) {
				if (properties.getProperty(i).getKey().equals(key)) {
					return properties.getProperty(i).getValue();
				}
			}
		}

		return value;
	}

}
