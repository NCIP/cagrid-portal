package gov.nih.nci.cagrid.introduce.codegen.extension;

import gov.nih.nci.cagrid.introduce.ServiceInformation;

/**
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created 
 */
public abstract class CodegenExtensionPostProcessor extends CodegenExtensionProcessor {

	public CodegenExtensionPostProcessor(ServiceInformation info) {
		super(info);
		// TODO Auto-generated constructor stub
	}
	
	public abstract void postCodegen() throws CodegenExtensionException;

}
