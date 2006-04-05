package gov.nih.nci.cagrid.introduce.codegen.security;

import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.etc.SecurityDescTemplate;

import java.io.File;
import java.io.FileWriter;


/**
 * SyncSecurity TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 14, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class SyncSecurity {

	private ServiceInformation info;
	private File etcDir;


	public SyncSecurity(File baseDirectory, ServiceInformation info) {
		this.info = info;
		etcDir = new File(baseDirectory.getAbsolutePath() + File.separator + "etc");
	}


	public void sync() throws Exception {
		SecurityDescTemplate secDescT = new SecurityDescTemplate();
		String secDescS = secDescT.generate(info);
		File secDescF = new File(etcDir.getAbsolutePath() + File.separator + "security-desc.xml");
		FileWriter secDescFW = new FileWriter(secDescF);
		secDescFW.write(secDescS);
		secDescFW.close();
	}

}
