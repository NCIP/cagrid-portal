package gov.nih.nci.cagrid.introduce.codegen.services;

import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.codegen.common.SyncTool;
import gov.nih.nci.cagrid.introduce.codegen.common.SynchronizationException;
import gov.nih.nci.cagrid.introduce.codegen.services.methods.SyncMethods;
import gov.nih.nci.cagrid.introduce.codegen.services.resources.SyncResource;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.NewServerConfigTemplate;
import gov.nih.nci.cagrid.introduce.templates.ServerConfigTemplate;
import gov.nih.nci.cagrid.introduce.templates.etc.SecurityDescTemplate;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.common.XMLUtilities;

import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

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
		if (getServiceInformation().getServices() != null
				&& getServiceInformation().getServices().getService() != null) {
			for (int serviceI = 0; serviceI < getServiceInformation()
					.getServices().getService().length; serviceI++) {

				int serviceIndex = serviceI;

				try {

					SpecificServiceInformation ssi = new SpecificServiceInformation(
							getServiceInformation(), getServiceInformation()
									.getServices().getService(serviceIndex));
					
					SecurityDescTemplate secDescT = new SecurityDescTemplate();
					String secDescS = secDescT.generate(ssi);
					File secDescF = new File(getBaseDirectory()
							+ File.separator
							+ "etc"
							+ File.separator
							+ getServiceInformation().getServices().getService(
									serviceIndex).getName()
							+ "-security-desc.xml");
					FileWriter secDescFW = new FileWriter(secDescF);
					secDescFW.write(secDescS);
					secDescFW.close();

					SyncMethods methodSync = new SyncMethods(
							getBaseDirectory(), getServiceInformation(),
							getServiceInformation().getServices().getService(
									serviceIndex));
					methodSync.sync();
					SyncResource resourceSync = new SyncResource(
							getBaseDirectory(), getServiceInformation(),
							getServiceInformation().getServices().getService(
									serviceIndex));
					resourceSync.sync();

				} catch (Exception e) {
					throw new SynchronizationException(e.getMessage(), e);
				}

			}
		}
	}
	
}
