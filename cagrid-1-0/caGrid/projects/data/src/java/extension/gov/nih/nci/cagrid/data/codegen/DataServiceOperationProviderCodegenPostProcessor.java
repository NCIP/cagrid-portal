package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.CadsrInformation;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;


/**
 * DataServiceCodegenPostProcessor Post-processor for dataservice code
 * generation
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @created Mar 29, 2006
 * @version $Id$
 */
public class DataServiceOperationProviderCodegenPostProcessor extends BaseCodegenPostProcessorExtension {

	public void postCodegen(ServiceExtensionDescriptionType desc, ServiceInformation info)
		throws CodegenExtensionException {
		// add the necessary jars to the eclipse .classpath
		modifyEclipseClasspath(desc, info);

		// create the XSD with the group of allowable return types for the
		// service
		ResultTypeGeneratorInformation typeInfo = new ResultTypeGeneratorInformation();
		typeInfo.setServiceInfo(info);
		CadsrInformation cadsrInfo = null;
		try {
			ExtensionTypeExtensionData data = ExtensionTools.getExtensionData(desc, info);
			cadsrInfo = ExtensionDataUtils.getExtensionData(data).getCadsrInformation();
		} catch (Exception ex) {
			throw new CodegenExtensionException("Error getting extension data: " + ex.getMessage());
		}
		typeInfo.setCadsrInfo(cadsrInfo);
		CQLResultTypesGenerator.generateCQLResultTypesXSD(typeInfo);
	}

}
