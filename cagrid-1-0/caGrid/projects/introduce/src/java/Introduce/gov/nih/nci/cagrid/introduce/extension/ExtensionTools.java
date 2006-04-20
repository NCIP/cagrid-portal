package gov.nih.nci.cagrid.introduce.extension;

import gov.nih.nci.cagrid.introduce.beans.extension.Properties;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.io.File;
import java.lang.reflect.Constructor;


public class ExtensionTools {

	public static CreationExtensionPostProcessor getCreationPostProcessor(String extensionName) throws Exception {
		ServiceExtensionDescriptionType extensionD = ExtensionsLoader.getInstance().getServiceExtension(extensionName);
		if (extensionD != null && extensionD.getCreationPostProcessor() != null
			&& !extensionD.getCreationPostProcessor().equals("")) {
			Class c = Class.forName(extensionD.getCreationPostProcessor());
			Object obj = c.newInstance();
			return (CreationExtensionPostProcessor) obj;
		}
		return null;
	}


	public static CodegenExtensionPostProcessor getCodegenPostProcessor(String extensionName) throws Exception {
		ServiceExtensionDescriptionType extensionD = ExtensionsLoader.getInstance().getServiceExtension(extensionName);
		if (extensionD != null && extensionD.getCodegenPostProcessor() != null
			&& !extensionD.getCodegenPostProcessor().equals("")) {
			Class c = Class.forName(extensionD.getCodegenPostProcessor());
			Object obj = c.newInstance();
			return (CodegenExtensionPostProcessor) obj;
		}
		return null;
	}


	public static CodegenExtensionPreProcessor getCodegenPreProcessor(String extensionName) throws Exception {
		ServiceExtensionDescriptionType extensionD = ExtensionsLoader.getInstance().getServiceExtension(extensionName);
		if (extensionD != null && extensionD.getCodegenPreProcessor() != null
			&& !extensionD.getCodegenPreProcessor().equals("")) {
			Class c = Class.forName(extensionD.getCodegenPreProcessor());
			Object obj = c.newInstance();
			return (CodegenExtensionPreProcessor) obj;
		}
		return null;
	}


	public static CreationExtensionUIDialog getCreationUIDialog(String extensionName,
		gov.nih.nci.cagrid.introduce.info.ServiceInformation info) throws Exception {
		ServiceExtensionDescriptionType extensionD = ExtensionsLoader.getInstance().getServiceExtension(extensionName);
		if (extensionD != null && extensionD.getCreationUIDialog() != null
			&& !extensionD.getCreationUIDialog().equals("")) {
			Class c = Class.forName(extensionD.getCreationUIDialog());
			Constructor con = c.getConstructor(new Class[]{ServiceInformation.class});
			Object obj = con.newInstance(new Object[]{info});
			return (CreationExtensionUIDialog) obj;
		}
		return null;
	}


	public static ServiceModificationUIPanel getServiceModificationUIPanel(String extensionName,
		gov.nih.nci.cagrid.introduce.info.ServiceInformation info) throws Exception {
		ServiceExtensionDescriptionType extensionD = ExtensionsLoader.getInstance().getServiceExtension(extensionName);
		if (extensionD != null && extensionD.getServiceModificationUIPanel() != null
			&& !extensionD.getServiceModificationUIPanel().equals("")) {
			Class c = Class.forName(extensionD.getServiceModificationUIPanel());
			Constructor con = c.getConstructor(new Class[]{ServiceInformation.class});
			Object obj = con.newInstance(new Object[]{info});
			return (ServiceModificationUIPanel) obj;
		}
		return null;
	}


	public static String getProperty(Properties properties, String key) {
		String value = null;
		if (properties != null && properties.getProperty() != null) {
			for (int i = 0; i < properties.getProperty().length; i++) {
				if (properties.getProperty(i).getKey().equals(key)) {
					return properties.getProperty(i).getValue();
				}
			}
		}

		return value;
	}

}
