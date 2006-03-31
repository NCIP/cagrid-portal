package gov.nih.nci.cagrid.introduce.extension;

import java.io.File;

import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.codegen.extension.CodegenExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.codegen.extension.CodegenExtensionPreProcessor;
import gov.nih.nci.cagrid.introduce.creator.extension.CreationExtensionPostProcessor;


public class ExtensionTools {
	private ExtensionsLoader loader;
	ExtensionClassLoader cloader;


	public ExtensionTools() {
		this.loader = new ExtensionsLoader();
	}


	public CreationExtensionPostProcessor getCreationPostProcessor(String extensionName) throws Exception {
		ExtensionDescriptionType extensionD = loader.getExtension(extensionName);
		ExtensionClassLoader cloader = new ExtensionClassLoader(new File(loader.getExtensionsDir() + File.separator
			+ extensionName));
		Class c = cloader.loadClass(extensionD.getCreationPostProcessor());
		Object obj = c.newInstance();
		return (CreationExtensionPostProcessor) obj;
	}


	public CodegenExtensionPostProcessor getCodegenPostProcessor(String extensionName) throws Exception {
		ExtensionDescriptionType extensionD = loader.getExtension(extensionName);
		ExtensionClassLoader cloader = new ExtensionClassLoader(new File(loader.getExtensionsDir() + File.separator
			+ extensionName));
		Class c = cloader.loadClass(extensionD.getCodegenPostProcessor());
		Object obj = c.newInstance();
		return (CodegenExtensionPostProcessor) obj;
	}


	public CodegenExtensionPreProcessor getCodegenPreProcessor(String extensionName) throws Exception {
		ExtensionDescriptionType extensionD = loader.getExtension(extensionName);
		ExtensionClassLoader cloader = new ExtensionClassLoader(new File(loader.getExtensionsDir() + File.separator
			+ extensionName));
		Class c = cloader.loadClass(extensionD.getCodegenPreProcessor());
		Object obj = c.newInstance();
		return (CodegenExtensionPreProcessor) obj;
	}

}
