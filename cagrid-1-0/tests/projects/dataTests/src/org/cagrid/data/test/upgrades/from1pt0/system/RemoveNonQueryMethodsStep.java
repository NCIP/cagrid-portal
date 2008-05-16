package org.cagrid.data.test.upgrades.from1pt0.system;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;

/** 
 *  RemoveNonQueryMethodsStep
 *  Removes any methods from the service which are not the query method
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 21, 2007 
 * @version $Id: RemoveNonQueryMethodsStep.java,v 1.1 2008-05-16 19:25:25 dervin Exp $ 
 */
public class RemoveNonQueryMethodsStep extends Step {
	private String serviceDir;
	
	public RemoveNonQueryMethodsStep(String serviceDir) {
		this.serviceDir = serviceDir;
	}
	

	public void runStep() throws Throwable {
		ServiceDescription serviceDesc = (ServiceDescription) Utils.deserializeDocument(
			serviceDir + File.separator + "introduce.xml", ServiceDescription.class);
		ServiceType mainService = serviceDesc.getServices().getService(0);
		MethodsType methods = mainService.getMethods();
		MethodType[] currentMethods = methods.getMethod();
		for (int i = 0; i < currentMethods.length; i++) {
			if (currentMethods[i].getName().equals(DataServiceConstants.QUERY_METHOD_NAME)) {
				methods.setMethod(new MethodType[] {currentMethods[i]});
				break;
			}
		}
		Utils.serializeDocument(serviceDir + File.separator + "introduce.xml", serviceDesc, IntroduceConstants.INTRODUCE_SKELETON_QNAME);
	}
}
