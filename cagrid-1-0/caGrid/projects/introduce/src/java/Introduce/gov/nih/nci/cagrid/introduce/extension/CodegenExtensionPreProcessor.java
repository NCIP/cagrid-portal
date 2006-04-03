package gov.nih.nci.cagrid.introduce.extension;

import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

/**
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created 
 */
public interface CodegenExtensionPreProcessor {
	
	public void preCodegen(ServiceInformation info) throws CodegenExtensionException;

}
