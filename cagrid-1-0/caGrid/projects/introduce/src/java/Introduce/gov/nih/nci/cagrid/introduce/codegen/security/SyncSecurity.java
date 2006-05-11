package gov.nih.nci.cagrid.introduce.codegen.security;

import gov.nih.nci.cagrid.introduce.codegen.common.SyncTool;
import gov.nih.nci.cagrid.introduce.codegen.common.SynchronizationException;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.etc.SecurityDescTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * SyncSecurity
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 14, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class SyncSecurity extends SyncTool {
	private File etcDir;


	public SyncSecurity(File baseDirectory, ServiceInformation info) {
		super(baseDirectory, info);
		etcDir = new File(baseDirectory.getAbsolutePath() + File.separator + "etc");
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.introduce.codegen.security.SyncTool#sync()
	 */
	public void sync() throws SynchronizationException {
		if (getServiceInformation().getServices() != null && getServiceInformation().getServices().getService() != null) {
			for (int serviceI = 0; serviceI < getServiceInformation().getServices().getService().length; serviceI++) {
				try {
					SpecificServiceInformation ssi = new SpecificServiceInformation(getServiceInformation(),getServiceInformation().getServices().getService(serviceI));
					SecurityDescTemplate secDescT = new SecurityDescTemplate();
					String secDescS = secDescT.generate(ssi);
					File secDescF = new File(etcDir.getAbsolutePath() + File.separator + getServiceInformation().getServices().getService(serviceI).getName() + "-security-desc.xml");
					FileWriter secDescFW = new FileWriter(secDescF);
					secDescFW.write(secDescS);
					secDescFW.close();
				} catch (IOException e) {
					throw new SynchronizationException("Error writting security descriptor:" + e.getMessage(), e);
				}
			}
		}
	}

}
