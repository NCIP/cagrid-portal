package gov.nih.nci.cagrid.introduce.extension;

import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.Properties;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis.message.MessageElement;

public class ExtensionTools {

	public static CreationExtensionPostProcessor getCreationPostProcessor(
			String extensionName) throws Exception {
		ServiceExtensionDescriptionType extensionDesc = ExtensionsLoader
				.getInstance().getServiceExtension(extensionName);
		if (extensionDesc != null
				&& extensionDesc.getCreationPostProcessor() != null
				&& !extensionDesc.getCreationPostProcessor().equals("")) {
			Class c = Class.forName(extensionDesc.getCreationPostProcessor());
			Object obj = c.newInstance();
			return (CreationExtensionPostProcessor) obj;
		}
		return null;
	}

	public static CodegenExtensionPostProcessor getCodegenPostProcessor(
			String extensionName) throws Exception {
		ServiceExtensionDescriptionType extensionDesc = ExtensionsLoader
				.getInstance().getServiceExtension(extensionName);
		if (extensionDesc != null
				&& extensionDesc.getCodegenPostProcessor() != null
				&& !extensionDesc.getCodegenPostProcessor().equals("")) {
			Class c = Class.forName(extensionDesc.getCodegenPostProcessor());
			Object obj = c.newInstance();
			return (CodegenExtensionPostProcessor) obj;
		}
		return null;
	}

	public static CodegenExtensionPreProcessor getCodegenPreProcessor(
			String extensionName) throws Exception {
		ServiceExtensionDescriptionType extensionDesc = ExtensionsLoader
				.getInstance().getServiceExtension(extensionName);
		if (extensionDesc != null
				&& extensionDesc.getCodegenPreProcessor() != null
				&& !extensionDesc.getCodegenPreProcessor().equals("")) {
			Class c = Class.forName(extensionDesc.getCodegenPreProcessor());
			Object obj = c.newInstance();
			return (CodegenExtensionPreProcessor) obj;
		}
		return null;
	}

	public static CreationExtensionUIDialog getCreationUIDialog(
			String extensionName,
			gov.nih.nci.cagrid.introduce.info.ServiceInformation info)
			throws Exception {
		ServiceExtensionDescriptionType extensionDesc = ExtensionsLoader
				.getInstance().getServiceExtension(extensionName);
		if (extensionDesc != null
				&& extensionDesc.getCreationUIDialog() != null
				&& !extensionDesc.getCreationUIDialog().equals("")) {
			Class c = Class.forName(extensionDesc.getCreationUIDialog());
			Constructor con = c.getConstructor(new Class[] {
					ServiceExtensionDescriptionType.class,
					ServiceInformation.class });
			Object obj = con.newInstance(new Object[] { extensionDesc, info });
			return (CreationExtensionUIDialog) obj;
		}
		return null;
	}

	public static ServiceModificationUIPanel getServiceModificationUIPanel(
			String extensionName,
			gov.nih.nci.cagrid.introduce.info.ServiceInformation info)
			throws Exception {
		ServiceExtensionDescriptionType extensionDesc = ExtensionsLoader
				.getInstance().getServiceExtension(extensionName);
		if (extensionDesc != null
				&& extensionDesc.getServiceModificationUIPanel() != null
				&& !extensionDesc.getServiceModificationUIPanel().equals("")) {
			Class c = Class.forName(extensionDesc
					.getServiceModificationUIPanel());
			Constructor con = c.getConstructor(new Class[] {
					ServiceExtensionDescriptionType.class,
					ServiceInformation.class });
			Object obj = con.newInstance(new Object[] { extensionDesc, info });
			return (ServiceModificationUIPanel) obj;
		}
		return null;
	}

	public static ExtensionsPreferencesConfigurationPanel getExtensionsPreferencesConfigurationPanel(
			String extensionName) throws Exception {
		ExtensionDescription extensionDesc = ExtensionsLoader.getInstance()
				.getExtension(extensionName);
		if (extensionDesc != null
				&& extensionDesc.getExtensionPreferencesPanel() != null
				&& !extensionDesc.getExtensionPreferencesPanel().equals("")) {
			Class c = Class.forName(extensionDesc
					.getExtensionPreferencesPanel());
			Constructor con = c
					.getConstructor(new Class[] { ExtensionDescription.class });
			Object obj = con.newInstance(new Object[] { extensionDesc });
			return (ExtensionsPreferencesConfigurationPanel) obj;
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

	public static ExtensionTypeExtensionData getExtensionData(
			ServiceExtensionDescriptionType desc, ServiceInformation info) {
		String extensionName = desc.getName();
		ExtensionType[] extensions = info.getServiceDescriptor()
				.getExtensions().getExtension();
		for (int i = 0; extensions != null && i < extensions.length; i++) {
			if (extensions[i].getName().equals(extensionName)) {
				if (extensions[i].getExtensionData() == null) {
					extensions[i]
							.setExtensionData(new ExtensionTypeExtensionData());
				}
				return extensions[i].getExtensionData();
			}
		}
		return null;
	}

	public static MessageElement getExtensionDataElement(
			ExtensionTypeExtensionData extensionData, String dataElement) {
		MessageElement[] dataEntries = extensionData.get_any();
		for (int i = 0; dataEntries != null && i < dataEntries.length; i++) {
			if (dataEntries[i].getLocalName().equals(dataElement)) {
				return dataEntries[i];
			}
		}
		return null;
	}

	public static void updateExtensionDataElement(
			ExtensionTypeExtensionData data, MessageElement element) {
		MessageElement[] anys = data.get_any();
		if (anys == null) {
			anys = new MessageElement[] { element };
		} else {
			// find the existing element of the same name, if it exists
			boolean valueSet = false;
			for (int i = 0; i < anys.length; i++) {
				if (anys[i].getName().equals(element.getName())) {
					anys[i] = element;
					valueSet = true;
					break;
				}
			}
			if (!valueSet) {
				MessageElement[] newAnys = new MessageElement[anys.length + 1];
				System.arraycopy(anys, 0, newAnys, 0, anys.length);
				newAnys[newAnys.length - 1] = element;
				anys = newAnys;
			}
		}
		data.set_any(anys);
	}

	public static void removeExtensionDataElement(
			ExtensionTypeExtensionData data, String dataElementName) {
		MessageElement[] dataEntries = data.get_any();
		if (dataEntries != null) {
			List cleanedEntries = new ArrayList(dataEntries.length);
			for (int i = 0; i < dataEntries.length; i++) {
				if (!dataEntries[i].getName().equals(dataElementName)) {
					cleanedEntries.add(dataEntries[i]);
				}
			}
			dataEntries = new MessageElement[cleanedEntries.size()];
			cleanedEntries.toArray(dataEntries);
			data.set_any(dataEntries);
		}
	}
}
