package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.data.creation.TestServiceInfo;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.atomicobject.haste.framework.Step;

/** 
 *  RebuildServiceStep
 *  Rebuilds the stubs, invokes post-processing extensions,
 *  and compiles the service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 7, 2006 
 * @version $Id: RebuildServiceStep.java,v 1.5 2007-04-12 14:24:55 dervin Exp $ 
 */
public class RebuildServiceStep extends Step {
	
    private TestServiceInfo serviceInfo;
	private String introduceDir;
	
	public RebuildServiceStep(TestServiceInfo serviceInfo, String introduceDir) {
		super();
        this.serviceInfo = serviceInfo;
		this.introduceDir = introduceDir;
	}
	

	public void runStep() throws Throwable {		
		System.out.println("Running step: " + getClass().getName());
		
		System.out.println("Invoking post creation processes...");
		String cmd = CommonTools.getAntSkeletonPostCreationCommand(introduceDir, 
            serviceInfo.getName(), serviceInfo.getDir(), serviceInfo.getPackage(), 
            serviceInfo.getNamespace(), serviceInfo.getExtensions());
		Process p = CommonTools.createAndOutputProcess(cmd);
        new StreamDumpster(p.getInputStream(), System.out).start();
        new StreamDumpster(p.getErrorStream(), System.err).start();
		p.waitFor();
        
		assertTrue("Service post creation process failed", p.exitValue() == 0);

		System.out.println("Building created service...");
		cmd = CommonTools.getAntAllCommand(serviceInfo.getDir());
		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Build process failed", p.exitValue() == 0);
	}
    
    
    private static class StreamDumpster extends Thread {
        private InputStream in;
        private OutputStream out;
        
        public StreamDumpster(InputStream in, OutputStream out) {
            this.in = in;
            this.out = out;
        }
        
        
        public void run() {
            byte[] buff = new byte[1024];
            int len = -1;
            try {
                while ((len = in.read(buff)) != -1) {
                    out.write(buff, 0, len);
                    out.flush();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
