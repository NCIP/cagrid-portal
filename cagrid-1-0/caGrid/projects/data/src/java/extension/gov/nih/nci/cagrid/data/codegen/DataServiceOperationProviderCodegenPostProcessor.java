package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;


/**
 * DataServiceCodegenPostProcessor 
 * Post-processor for dataservice code generation
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @created Mar 29, 2006
 * @version $Id$
 */
public class DataServiceOperationProviderCodegenPostProcessor extends BaseCodegenPostProcessorExtension {
	
	public void postCodegen(ServiceExtensionDescriptionType desc, ServiceInformation info)
		throws CodegenExtensionException {
		modifyEclipseClasspath(desc, info);
	}
}
