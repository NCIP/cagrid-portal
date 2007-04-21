/**
 * 
 */
package org.cagrid.rav;

import org.apache.log4j.Logger;
import org.ggf.schemas.jsdl._2005._11.jsdl.Application_Type;

import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPreProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
/**
 * @author madduri
 *
 */
public class ApplicationServiceCodeGenPreProcessor implements CodegenExtensionPreProcessor  {
	private static final Logger logger = Logger.getLogger(ApplicationServiceCodeGenPreProcessor.class);
	
	Application_Type appTypeData = null;
	
	String methodName = null;
	String appDescritption = null;
	String appVersion = null;
	

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPreProcessor#preCodegen(gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType, gov.nih.nci.cagrid.introduce.common.ServiceInformation)
	 */
	public void preCodegen(ServiceExtensionDescriptionType desc, ServiceInformation info) throws CodegenExtensionException {
		logger.info("in precodegen");
		MethodsType methods = info.getServices().getService(0).getMethods();
		for (int i=0;i<methods.getMethod().length;i++){
			logger.info("Method name:" +methods.getMethod()[i].getName());
		}
		MethodsType newMethods = new MethodsType();
		MethodType[] newMethodArray = new MethodType[1];
		
		//info.getServices().getService(0).setMethods(newMethods );
		CommonTools.addMethod(info.getServices().getService()[0], newMethodArray[0]);
		ExtensionTypeExtensionData data = ExtensionTools.getExtensionData(desc, info);
		try {
			this.appTypeData = ExtensionDataUtils.getExtensionData(data);
			this.methodName = this.appTypeData.getApplicationName();
			this.appDescritption = this.appTypeData.getDescription();
			this.appVersion = this.appTypeData.getApplicationVersion();
			newMethodArray[0] = new MethodType("String", "String",this.appDescritption, 
					null, null, "java.lang.String", null, false, false, null, appDescritption, null, appDescritption, null);
			newMethodArray[0].setName(this.methodName);
			newMethods.setMethod(newMethodArray);
			logger.info(appTypeData.getApplicationName() + appTypeData.getApplicationVersion());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
