package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.common.StreamGobbler;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.creation.DataTestCaseInfo;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  RebuildServiceStep
 *  Rebuilds the stubs, invokes post-processing extensions,
 *  and compiles the service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 7, 2006 
 * @version $Id: RebuildServiceStep.java,v 1.8 2007-06-21 16:21:15 dervin Exp $ 
 */
public class RebuildServiceStep extends Step {
	
    private DataTestCaseInfo serviceInfo;
	private String introduceDir;
	
	public RebuildServiceStep(DataTestCaseInfo serviceInfo, String introduceDir) {
		super();
        this.serviceInfo = serviceInfo;
		this.introduceDir = introduceDir;
	}
	

	public void runStep() throws Throwable {		
		System.out.println("Running step: " + getClass().getName());
		
		System.out.println("Invoking post creation processes...");
		String cmd = CommonTools.getAntSkeletonPostCreationCommand(introduceDir, 
            serviceInfo.getName(), serviceInfo.getDir(), serviceInfo.getPackageName(), 
            serviceInfo.getNamespace(), getServiceExtensions());
        System.out.println("Invoking ant:");
        System.out.println(cmd);
		Process p = CommonTools.createAndOutputProcess(cmd);
        new StreamGobbler(p.getInputStream(), StreamGobbler.TYPE_OUT, System.out).start();
        new StreamGobbler(p.getErrorStream(), StreamGobbler.TYPE_ERR, System.err).start();
		p.waitFor();
		assertTrue("Service post creation process failed", p.exitValue() == 0);

		System.out.println("Building created service...");
		cmd = CommonTools.getAntAllCommand(serviceInfo.getDir());
        System.out.println("Invoking ant:");
        System.out.println(cmd);
		p = CommonTools.createAndOutputProcess(cmd);
        new StreamGobbler(p.getInputStream(), StreamGobbler.TYPE_OUT, System.out).start();
        new StreamGobbler(p.getErrorStream(), StreamGobbler.TYPE_ERR, System.err).start();
        p.waitFor();
		assertTrue("Build process failed", p.exitValue() == 0);
	}
    
    
    private String getServiceExtensions() throws Exception {
        ServiceDescription description = (ServiceDescription) Utils.deserializeDocument(
            serviceInfo.getDir() + File.separator + IntroduceConstants.INTRODUCE_XML_FILE,
            ServiceDescription.class);
        String ext = "";
        if (description.getExtensions() != null 
            && description.getExtensions().getExtension() != null) {
            ExtensionType[] extensions = description.getExtensions().getExtension();
            for (int i = 0; i < extensions.length; i++) {
                ext += extensions[i].getName();
                if (i + 1 < extensions.length) {
                    ext += ",";
                }
            }
        }
        return ext;
    }
}
