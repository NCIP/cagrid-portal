/**
 * 
 */
package org.cagrid.rav;

import java.io.File;
import java.io.IOException;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.ggf.schemas.jsdl._2005._11.jsdl.Application_Type;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptions;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
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
public class ApplicationServiceCodeGenPreProcessor implements
		CodegenExtensionPreProcessor {
	
	private static final Logger logger = Logger
			.getLogger(ApplicationServiceCodeGenPreProcessor.class);

	Application_Type appTypeData = null;

	String methodName = null;

	String appDescritption = null;

	String appVersion = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPreProcessor#preCodegen(gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType,
	 *      gov.nih.nci.cagrid.introduce.common.ServiceInformation)
	 */
	public void preCodegen(ServiceExtensionDescriptionType desc,
			ServiceInformation info) throws CodegenExtensionException {
		MethodsType methods = info.getServices().getService(0).getMethods();
		ExtensionTypeExtensionData data = ExtensionTools.getExtensionData(desc,
				info);

		try {
			this.appTypeData = ExtensionDataUtils.getExtensionData(data);
			this.methodName = this.appTypeData.getApplicationName();
			this.appDescritption = this.appTypeData.getDescription();
			this.appVersion = this.appTypeData.getApplicationVersion();
			for (int i = 0; i < methods.getMethod().length; i++) {
				logger.info("Method name:" + methods.getMethod()[i].getName());
				if (methods.getMethod()[i].getName().equals(this.methodName)) {
					logger.info("Method already added");
					return;
				}
			}

			MethodType method = new MethodType();
			method.setName(this.methodName);
			MethodTypeOutput output = new MethodTypeOutput();
			output.setQName(new QName("http://www.w3.org/2001/XMLSchema",
					"boolean"));
			method.setOutput(output);

			// create a new input param
			MethodTypeInputsInput input1 = new MethodTypeInputsInput();
			input1.setQName(new QName("http://www.w3.org/2001/XMLSchema",
					"string"));
			input1.setName("foo");
			input1.setIsArray(false);
			// create a new input param
			MethodTypeInputsInput input2 = new MethodTypeInputsInput();
			input2.setQName(new QName("http://www.w3.org/2001/XMLSchema",
					"integer"));
			input2.setName("bar");
			input2.setIsArray(false);
			MethodTypeInputsInput[] newInputs = new MethodTypeInputsInput[2];
			newInputs[0] = input1;
			newInputs[1] = input2;
			MethodTypeInputs inputs = new MethodTypeInputs();
			inputs.setInput(newInputs);
			method.setInputs(inputs);
			/*MethodTypeExceptionsException[] exceptionsArray = new MethodTypeExceptionsException[1];
	        MethodTypeExceptionsException exception = new MethodTypeExceptionsException();
	        exception.setName("java.io.IOException");
	        exceptionsArray[0] = exception;
	        MethodTypeExceptions exceptions = new MethodTypeExceptions();
	        exceptions.setException(exceptionsArray);
	        method.setExceptions(exceptions);*/

			CommonTools.addMethod(info.getServices().getService()[0], method);
			logger.info(appTypeData.getApplicationName()
					+ appTypeData.getApplicationVersion());
			editServiceImpl(info);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void editServiceImpl(ServiceInformation info) throws CodegenExtensionException {
		//info.getServices().getService()[0].
		String serviceName = info.getServices().getService()[0].getName();
		String basePackage = info.getServices().getService()[0].getPackageName();
		// full name of the service impl class
		String fullClassName = basePackage + ".service." + serviceName + "Impl";
		// file name of the service impl java source
		String sourceFileName = info.getBaseDirectory().getAbsolutePath() + File.separator + "src" + File.separator 
			+ fullClassName.replace('.', File.separatorChar) + ".java";
		// read the source file in
		StringBuffer source = null;
		try {
			source = Utils.fileToStringBuffer(new File(sourceFileName));
			logger.info(sourceFileName);
			logger.info(source.toString());
		} catch (IOException ex) {
			throw new CodegenExtensionException("Error reading service source file: " + ex.getMessage(), ex);
		}
		
	}

}
