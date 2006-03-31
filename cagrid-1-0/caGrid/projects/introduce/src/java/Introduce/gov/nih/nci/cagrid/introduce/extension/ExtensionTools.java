package gov.nih.nci.cagrid.introduce.extension;

import gov.nih.nci.cagrid.introduce.codegen.extension.CodegenExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.codegen.extension.CodegenExtensionPreProcessor;
import gov.nih.nci.cagrid.introduce.creator.extension.CreationExtensionPostProcessor;

public class ExtensionTools {

	public static CreationExtensionPostProcessor getCreationPostProcessor(String extensionName){
		return null;
	}

	public static CodegenExtensionPostProcessor getCodegenPostProcessor(String extensionName){
		return null;
	}

	public static CodegenExtensionPreProcessor getCodegenPreProcessor(String extensionName){
		return null;
	}

}
