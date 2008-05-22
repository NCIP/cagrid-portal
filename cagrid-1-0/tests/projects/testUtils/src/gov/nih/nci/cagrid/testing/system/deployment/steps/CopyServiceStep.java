package gov.nih.nci.cagrid.testing.system.deployment.steps;

import gov.nih.nci.cagrid.common.ZipUtilities;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DeployServiceStep Deploys a caGrid service to the container
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 8, 2006
 * @version $Id: CopyServiceStep.java,v 1.2 2008-05-22 01:16:24 langella Exp $
 */
public class CopyServiceStep extends Step {
	private static final Log LOG = LogFactory.getLog(CopyServiceStep.class);

	private File sourceDir;
	private File destDir;
	private File serviceDirectory;

	public CopyServiceStep(File sourceDir, File destParentDir) {
		this.sourceDir = sourceDir;
		this.destDir = destParentDir;
	}

	public void runStep() throws Throwable {
		LOG.debug("Running step: " + getClass().getName());
		File zip = new File(new Date().getTime()+".zip");
		ZipUtilities.zipDirectory(sourceDir, zip);
		ZipUtilities.unzip(zip, destDir);
		zip.delete();
		serviceDirectory = new File(destDir.getAbsolutePath()+File.separator+sourceDir.getName());
	}

	public File getServiceDirectory() {
		return serviceDirectory;
	}
	
	
}
