package gov.nih.nci.cagrid.sdkinstall;

import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.sdkinstall.description.InstallationDescription;

import java.io.File;

/** 
 *  Version321BuildInvoker
 *  Invokes the build process for SDK version 3.2.1, assumes it's already configured
 * 
 * @author David Ervin
 * 
 * @created Jun 18, 2007 10:38:41 AM
 * @version $Id: Version321BuildInvoker.java,v 1.2 2007-06-18 20:52:34 dervin Exp $ 
 */
public class Version321BuildInvoker extends BuildInvoker {
    public static final String BUILD_COMMAND = "build-system";

    public Version321BuildInvoker(InstallationDescription description, File sdkDir) {
        super(description, sdkDir);
    }


    public void invokeBuildProcess() throws BuildInvocationException {
        String antCommand = null;
        try {
            antCommand = CommonTools.getAntCommand(BUILD_COMMAND, getSdkDir().getAbsolutePath());
        } catch (Exception ex) {
            throw new BuildInvocationException("Error generating ant command: " + ex.getMessage(), ex);
        }
        
        // exec the ant process
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec(antCommand);
        } catch (Exception ex) {
            throw new BuildInvocationException("Error creating ant build process: " + ex.getMessage(), ex);
        }
        new StreamRedirector(proc.getInputStream(), System.out).start();
        new StreamRedirector(proc.getErrorStream(), System.err).start();
        int exitCode = 0;
        InterruptedException iex = null;
        try {
            exitCode = proc.waitFor();
        } catch (InterruptedException ex) {
            iex = ex;
        }
        if (iex != null || exitCode != 0) {
            throw new BuildInvocationException(
                "Error executing build process: " + (iex == null ? 
                    " exit status " + exitCode : iex.getMessage()), iex);
        }
    }
}
