package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cagrid.data.common.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

/** 
 *  DataServiceCodegenPostProcessor
 *  Post-processor for dataservice code generation
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Mar 29, 2006 
 * @version $Id$ 
 */
public class DataServiceCodegenPostProcessor implements CodegenExtensionPostProcessor {

	public void postCodegen(ServiceInformation info) throws CodegenExtensionException {
		// TODO: Find the query method, if user hasn't done something to
		// it, and drop in CQL implementation
		MethodType queryMethod = getQueryMethod(info);
		if (queryMethod != null) {
			System.out.println("Found the CQL Query method!");
			// drop in CQL impl
		} else {
			System.out.println("No CQL Query method found, sorry");
		}
	}
	
	
	private MethodType getQueryMethod(ServiceInformation info) {
		MethodType queryMethod = null;
		MethodType[] methods = info.getMethods().getMethod();
		for (int i = 0; i < methods.length; i++) {
			MethodType currentMethod = methods[i];
			if (currentMethod.getName().equals("query")) {
				MethodTypeInputsInput[] inputParams = currentMethod.getInputs().getInput();
				// should only be one input parameter
				if (inputParams.length == 1) {
					String paramName = inputParams[0].getName();
					String paramUri = inputParams[0].getQName().getNamespaceURI();
					String paramType = inputParams[0].getQName().getLocalPart();
					// does the parameter have the right name and type?
					if (paramName.equals("cqlQuery") && paramUri.equals(DataServiceConstants.CQL_QUERY_URI) && paramType.equals(DataServiceConstants.CQL_QUERY_TYPE)) {
						String returnUri = currentMethod.getOutput().getQName().getNamespaceURI();
						String returnType = currentMethod.getOutput().getQName().getLocalPart();
						if (returnUri.equals(DataServiceConstants.CQL_RESULT_SET_URI) && returnType.equals(DataServiceConstants.CQL_RESULT_SET_TYPE)) {
							// found the method!
							queryMethod = currentMethod;
							break;
						}
					}
				}
			}
		}
		return queryMethod;
	}
}
