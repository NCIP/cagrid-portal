package gov.nih.nci.cagrid.data.system;

import java.io.File;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import com.atomicobject.haste.framework.Step;

/** 
 *  EnableValidationStep
 *  Step to enable query validation on the data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 9, 2006 
 * @version $Id: EnableValidationStep.java,v 1.1 2006-11-09 15:27:52 dervin Exp $ 
 */
public class EnableValidationStep extends Step {
	
	private String serviceBaseDir;
	
	public EnableValidationStep(String serviceBaseDir) {
		this.serviceBaseDir = serviceBaseDir;
	}
	

	public void runStep() throws Throwable {
		System.out.println("Running step: " + getClass().getName());
		String serviceModelFilename = serviceBaseDir + File.separator + IntroduceConstants.INTRODUCE_XML_FILE;
		ServiceDescription desc = (ServiceDescription) Utils.deserializeDocument(
			serviceModelFilename, ServiceDescription.class);
		CommonTools.setServiceProperty(desc, 
			DataServiceConstants.VALIDATE_CQL_FLAG, String.valueOf(true), false);
		/*
		 * Turn this back on when we get a domain model for the Bookstore
		CommonTools.setServiceProperty(desc, 
			DataServiceConstants.VALIDATE_DOMAIN_MODEL_FLAG, String.valueOf(true), false);
		*/
		Utils.serializeDocument(serviceModelFilename, desc, IntroduceConstants.INTRODUCE_SKELETON_QNAME);
	}
}
