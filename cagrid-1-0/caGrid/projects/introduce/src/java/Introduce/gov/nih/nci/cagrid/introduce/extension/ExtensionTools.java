package gov.nih.nci.cagrid.introduce.extension;

import java.io.File;

import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescriptionType;


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

}
