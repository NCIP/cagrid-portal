package gov.nih.nci.cagrid.introduce.codegen.extension;

import gov.nih.nci.cagrid.introduce.ServiceInformation;

/**
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created 
 */
public interface CodegenExtensionPostProcessor {
	
	public void postCodegen(ServiceInformation info) throws CodegenExtensionException;

}
