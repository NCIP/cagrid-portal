package gov.nih.nci.cagrid.introduce.codegen.services;

import gov.nih.nci.cagrid.introduce.codegen.common.SyncTool;
import gov.nih.nci.cagrid.introduce.codegen.common.SynchronizationException;
import gov.nih.nci.cagrid.introduce.codegen.services.methods.SyncMethods;
import gov.nih.nci.cagrid.introduce.codegen.services.resources.SyncResource;
import gov.nih.nci.cagrid.introduce.codegen.services.security.SyncSecurity;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.service.ServiceImplBaseTemplate;

import java.io.File;
import java.io.FileWriter;


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
		if ((getServiceInformation().getServices() != null)
			&& (getServiceInformation().getServices().getService() != null)) {
			for (int serviceI = 0; serviceI < getServiceInformation().getServices().getService().length; serviceI++) {

				int serviceIndex = serviceI;

				try {

					SpecificServiceInformation ssi = new SpecificServiceInformation(getServiceInformation(),
						getServiceInformation().getServices().getService(serviceIndex));

					// regenerate the services globus layer "ImplBase"
					ServiceImplBaseTemplate implBaseT = new ServiceImplBaseTemplate();
					String implBaseS = implBaseT.generate(new SpecificServiceInformation(getServiceInformation(), ssi
						.getService()));
					File implBaseF = new File(getBaseDirectory() + File.separator + "src" + File.separator
						+ CommonTools.getPackageDir(ssi.getService()) + File.separator + "service" + File.separator
						+ ssi.getService().getName() + "ImplBase.java");
					FileWriter implBaseFW = new FileWriter(implBaseF);
					implBaseFW.write(implBaseS);
					implBaseFW.close();

					// execute the source synchronization to make any necessary
					// changes to source
					// code for new/modified/removed methods
					SyncMethods methodSync = new SyncMethods(getBaseDirectory(), getServiceInformation(),
						getServiceInformation().getServices().getService(serviceIndex));
					methodSync.sync();
					SyncResource resourceSync = new SyncResource(getBaseDirectory(), getServiceInformation(),
						getServiceInformation().getServices().getService(serviceIndex));
					resourceSync.sync();

					SyncSecurity securitySync = new SyncSecurity(getBaseDirectory(), getServiceInformation(),
						getServiceInformation().getServices().getService(serviceIndex));
					securitySync.sync();

				} catch (Exception e) {
					throw new SynchronizationException(e.getMessage(), e);
				}

			}
		}
	}

}
