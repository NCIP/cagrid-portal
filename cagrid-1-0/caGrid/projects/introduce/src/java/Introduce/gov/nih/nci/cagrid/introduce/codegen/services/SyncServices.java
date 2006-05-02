package gov.nih.nci.cagrid.introduce.codegen.services;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.codegen.common.SyncTool;
import gov.nih.nci.cagrid.introduce.codegen.common.SynchronizationException;
import gov.nih.nci.cagrid.introduce.codegen.methods.SyncMethods;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.creator.SkeletonSourceCreator;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.naming.resources.BaseDirContext;
import org.apache.ws.jaxme.js.JavaMethod;
import org.apache.ws.jaxme.js.JavaSource;
import org.apache.ws.jaxme.js.JavaSourceFactory;
import org.apache.ws.jaxme.js.util.JavaParser;


/**
 * SyncMethodsOnDeployment
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 8, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class SyncServices extends SyncTool {

	private String serviceInterface;
	private List newServices;


	public SyncServices(File baseDirectory, ServiceInformation info) {
		super(baseDirectory, info);
		newServices = new ArrayList();
	}


	public void sync() throws SynchronizationException {

		lookForUpdates();

		for (int i = 0; i < newServices.size(); i++) {
			ServiceType newService = (ServiceType) newServices.get(i);
			SkeletonSourceCreator ssc = new SkeletonSourceCreator();
			try {
				System.out.println("Adding Service for: " + newService.getName());
				ssc.createSkeleton(getBaseDirectory(), getServiceInformation(), newService);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			String cmd = CommonTools.getAntFlattenCommand(getBaseDirectory().getAbsolutePath());
			Process p = CommonTools.createAndOutputProcess(cmd);
			p.waitFor();
			if (p.exitValue() != 0) {
				throw new SynchronizationException("Service flatten wsdl exited abnormally");
			}
		} catch (Exception e) {
			throw new SynchronizationException("Error executing wsdl flatten command:" + e.getMessage(), e);
		}

		// sync each sub service
		if (getServiceInformation().getServices() != null && getServiceInformation().getServices().getService() != null) {
			for (int serviceI = 0; serviceI < getServiceInformation().getServices().getService().length; serviceI++) {
				SyncMethods methodSync = new SyncMethods(getBaseDirectory(), getServiceInformation(),
					getServiceInformation().getServices().getService(serviceI));
				methodSync.sync();
			}
		}
	}


	private void lookForUpdates() {
		if (getServiceInformation().getServices() != null && getServiceInformation().getServices().getService() != null) {
			for (int serviceI = 0; serviceI < getServiceInformation().getServices().getService().length; serviceI++) {
				File serviceDir = new File(getBaseDirectory()
					+ File.separator
					+ "src" + File.separator
					+ getServiceInformation().getIntroduceServiceProperties().getProperty(
						IntroduceConstants.INTRODUCE_SKELETON_PACKAGE_DIR) + File.separator
					+ getServiceInformation().getServices().getService(serviceI).getName().toLowerCase());
				if (!serviceDir.exists()) {
					newServices.add(getServiceInformation().getServices().getService(serviceI));
				}
			}
		}

	}

}
