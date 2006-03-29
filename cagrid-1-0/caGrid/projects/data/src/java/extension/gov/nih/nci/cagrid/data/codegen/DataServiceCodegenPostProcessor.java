package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cagrid.introduce.ServiceInformation;
import gov.nih.nci.cagrid.introduce.codegen.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.codegen.extension.CodegenExtensionPostProcessor;

/** 
 *  DataServiceCodegenPostProcessor
 *  Post-processor for dataservice code generation
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Mar 29, 2006 
 * @version $Id$ 
 */
public class DataServiceCodegenPostProcessor extends CodegenExtensionPostProcessor {

	public DataServiceCodegenPostProcessor(ServiceInformation info) {
		super(info);
	}


	public void postCodegen() throws CodegenExtensionException {
		// TODO Auto-generated method stub

	}
}
