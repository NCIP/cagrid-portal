package gov.nih.nci.cagrid.introduce.codegen.services;

import gov.nih.nci.cagrid.introduce.codegen.common.SyncTool;
import gov.nih.nci.cagrid.introduce.codegen.common.SynchronizationException;
import gov.nih.nci.cagrid.introduce.codegen.methods.SyncMethods;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.io.File;


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


	public SyncServices(File baseDirectory, ServiceInformation info) {
		super(baseDirectory, info);
	}


	public void sync() throws SynchronizationException {

		// sync each sub service
		if (getServiceInformation().getServices() != null && getServiceInformation().getServices().getService() != null) {
			for (int serviceI = 0; serviceI < getServiceInformation().getServices().getService().length; serviceI++) {
				SyncMethods methodSync = new SyncMethods(getBaseDirectory(), getServiceInformation(),
					getServiceInformation().getServices().getService(serviceI));
				methodSync.sync();
			}
		}
	}


}
