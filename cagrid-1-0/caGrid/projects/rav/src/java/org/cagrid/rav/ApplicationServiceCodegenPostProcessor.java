/**
 * 
 */
package org.cagrid.rav;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.ggf.schemas.jsdl._2005._11.jsdl.Application_Type;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.codegen.services.methods.SyncHelper;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;

/**
 * @author madduri
 *
 */
public class ApplicationServiceCodegenPostProcessor implements CodegenExtensionPostProcessor {

	private static final Logger logger = Logger.getLogger(ApplicationServiceCodegenPostProcessor.class);
	
	private static final String RUNTIME_EXEC_CODE = "Runtime.getRuntime().exec(";
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPostProcessor#postCodegen(gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType, gov.nih.nci.cagrid.introduce.common.ServiceInformation)
	 */
	public void postCodegen(ServiceExtensionDescriptionType desc, ServiceInformation info) throws CodegenExtensionException {
		editServiceImpl(desc, info);
		
	}
	private void editServiceImpl(ServiceExtensionDescriptionType desc, ServiceInformation info) throws CodegenExtensionException {
		ExtensionTypeExtensionData data = ExtensionTools.getExtensionData(desc,
				info);
		Application_Type appTypeData = null;
		try {
			appTypeData = ExtensionDataUtils.getExtensionData(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MethodsType methods = info.getServices().getService(0).getMethods();
		String methodSignatureStart = null;
		String methodName = appTypeData.getApplicationName();
		int i = 0;
		for (i = 0; i < methods.getMethod().length; i++) {
			if (methods.getMethod()[i].getName().equals(methodName)) {
				break;
			}
		}
		MethodType ourMethod = methods.getMethod()[i];
		if (ourMethod.isIsImported() && (ourMethod.getImportInformation().getFromIntroduce() != null)
				&& !ourMethod.getImportInformation().getFromIntroduce().booleanValue()) {
			methodSignatureStart = SyncHelper.createBoxedSignatureStringFromMethod(ourMethod) + " " 
			+ SyncHelper.createClientExceptions(ourMethod, info);
		} else {
			methodSignatureStart = SyncHelper.createUnBoxedSignatureStringFromMethod(ourMethod, info) + " "
			+ SyncHelper.createExceptions(ourMethod, info);
		}
		System.out.println("Searching for method with signature:");
		System.out.println("\t" + methodSignatureStart);

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
			int startOfMethod = SyncHelper.startOfSignature(source, methodSignatureStart);
            int endOfMethod = SyncHelper.bracketMatch(source, startOfMethod);
            if (startOfMethod == -1 || endOfMethod == -1) {
                System.err.println("WARNING: Unable to locate method in Impl : " + methodName);
               return;
            }
            String serviceMethod = source.substring(startOfMethod, endOfMethod);
            System.out.println("serviceMethod: " + serviceMethod + " " + startOfMethod + " " + endOfMethod + " " + source.length());
            serviceMethod = serviceMethod.substring(0, serviceMethod.indexOf("}"));
            serviceMethod = serviceMethod + this.RUNTIME_EXEC_CODE + "\" " + methodName + "\");}";
            String temp = this.RUNTIME_EXEC_CODE + "\" " + methodName + "\"); " + "\n" + "return true; \n}";
          //  source.replace(startOfMethod, endOfMethod, "");
            StringBuffer tempBuffer = new StringBuffer(); 
            tempBuffer.append(methodSignatureStart).append("{").append("\n").append(temp);
           // source.insert(source.lastIndexOf("}") -1, tempBuffer, 0 , tempBuffer.length());
            System.out.println(source.toString());
		} catch (IOException ex) {
			throw new CodegenExtensionException("Error reading service source file: " + ex.getMessage(), ex);
		}
		
		try {
			Utils.stringBufferToFile(source, sourceFileName);
		} catch (IOException ex) {
			throw new CodegenExtensionException("Error writing service source file: " + ex.getMessage(), ex);
		}
	}

}
