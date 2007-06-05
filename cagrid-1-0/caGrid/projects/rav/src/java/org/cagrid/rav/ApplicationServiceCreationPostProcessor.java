package org.cagrid.rav;
import java.io.File;
import java.io.IOException;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.ggf.schemas.jsdl._2005._11.jsdl.Application_Type;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptions;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.resource.*;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.beans.service.*;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import org.apache.axis.message.MessageElement;

/**
 * @author madduri
 * 
 */


public class ApplicationServiceCreationPostProcessor implements
CreationExtensionPostProcessor {
	
	private static final Logger logger = Logger
			.getLogger(ApplicationServiceCodeGenPreProcessor.class);

	public void postCreate(ServiceExtensionDescriptionType desc,
			ServiceInformation info) {
		
		/* Trying to add new service context to get a resource */
		ServiceType[]sertype = new ServiceType [2];
		sertype[0] = info.getServices().getService()[0];
		sertype[1] = new ServiceType();
		sertype[1].setDescription("Hopefully the resource");
				
		sertype[1].setName(sertype[0].getName() + "ResultResource");
		sertype[1].setNamespace(info.getServices().getService()[0].getNamespace() + "/Context");
		sertype[1].setPackageName(info.getServices().getService()[0].getPackageName() + ".context");
		sertype[1].setResourceFrameworkType(IntroduceConstants.INTRODUCE_LIFETIME_RESOURCE);
		
		ServicesType st = new ServicesType();
		st.setService(sertype);
		info.setServices(st);
		
	}
}
