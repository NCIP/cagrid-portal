package gov.nih.nci.cagrid.introduce.extension;

import gov.nih.nci.cagrid.introduce.ServiceInformation;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescriptionType;

import java.io.File;
import java.lang.reflect.Constructor;


public class ExtensionTools {
	private ExtensionsLoader loader;
	ExtensionClassLoader cloader;


	public ExtensionTools() {
		this.loader = new ExtensionsLoader();
	}


	public CreationExtensionPostProcessor getCreationPostProcessor(String extensionName) throws Exception {
		ExtensionDescriptionType extensionD = loader.getExtension(extensionName);
		if (extensionD != null && extensionD.getCreationPostProcessor() != null
			&& !extensionD.getCreationPostProcessor().equals("")) {
			ExtensionClassLoader cloader = new ExtensionClassLoader(new File(loader.getExtensionsDir() + File.separator
				+ extensionName));
			Class c = cloader.loadClass(extensionD.getCreationPostProcessor());
			Object obj = c.newInstance();
			return (CreationExtensionPostProcessor) obj;
		}
		return null;
	}


	public CodegenExtensionPostProcessor getCodegenPostProcessor(String extensionName) throws Exception {
		ExtensionDescriptionType extensionD = loader.getExtension(extensionName);
		if (extensionD != null && extensionD.getCodegenPostProcessor() != null
			&& !extensionD.getCodegenPostProcessor().equals("")) {
			ExtensionClassLoader cloader = new ExtensionClassLoader(new File(loader.getExtensionsDir() + File.separator

			+ extensionName));
			Class c = cloader.loadClass(extensionD.getCodegenPostProcessor());
			Object obj = c.newInstance();
			return (CodegenExtensionPostProcessor) obj;
		}
		return null;
	}


	public CodegenExtensionPreProcessor getCodegenPreProcessor(String extensionName) throws Exception {
		ExtensionDescriptionType extensionD = loader.getExtension(extensionName);
		if (extensionD != null && extensionD.getCodegenPreProcessor() != null
			&& !extensionD.getCodegenPreProcessor().equals("")) {
			ExtensionClassLoader cloader = new ExtensionClassLoader(new File(loader.getExtensionsDir() + File.separator

			+ extensionName));
			Class c = cloader.loadClass(extensionD.getCodegenPreProcessor());
			Object obj = c.newInstance();
			return (CodegenExtensionPreProcessor) obj;
		}
		return null;
	}
	
	public CreationExtensionUIDialog getCreationUIDialog(String extensionName, gov.nih.nci.cagrid.introduce.ServiceInformation info) throws Exception {
		ExtensionDescriptionType extensionD = loader.getExtension(extensionName);
		if (extensionD != null && extensionD.getCreationUIDialog() != null
			&& !extensionD.getCreationUIDialog().equals("")) {
			ExtensionClassLoader cloader = new ExtensionClassLoader(new File(loader.getExtensionsDir() + File.separator

			+ extensionName));
			Class c = cloader.loadClass(extensionD.getCreationUIDialog());
			Constructor con = c.getConstructor(new Class[] {ServiceInformation.class});
			Object obj = con.newInstance(new Object[] {info});
			return (CreationExtensionUIDialog) obj;
		}
		return null;
	}
	
	public ServiceModificationUIPanel getServiceModificationUIPanel(String extensionName, gov.nih.nci.cagrid.introduce.ServiceInformation info) throws Exception {
		ExtensionDescriptionType extensionD = loader.getExtension(extensionName);
		if (extensionD != null && extensionD.getServiceModificationUIPanel() != null
			&& !extensionD.getServiceModificationUIPanel().equals("")) {
			ExtensionClassLoader cloader = new ExtensionClassLoader(new File(loader.getExtensionsDir() + File.separator

			+ extensionName));
			Class c = cloader.loadClass(extensionD.getServiceModificationUIPanel());
			Constructor con = c.getConstructor(new Class[] {ServiceInformation.class});
			Object obj = con.newInstance(new Object[] {info});
			return (ServiceModificationUIPanel) obj;
		}
		return null;
	}

}
