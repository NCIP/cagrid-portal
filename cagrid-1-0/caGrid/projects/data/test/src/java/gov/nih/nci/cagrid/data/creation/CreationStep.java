package gov.nih.nci.cagrid.data.creation;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gov.nih.nci.cagrid.introduce.common.CommonTools;

import com.atomicobject.haste.framework.Step;

/** 
 *  CreationStep
 *  TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 22, 2006 
 * @version $Id$ 
 */
public class CreationStep extends Step {
    protected TestServiceInfo serviceInfo;
    protected String introduceDir;
	
	public CreationStep(TestServiceInfo serviceInfo, String introduceDir) {
		super();
        this.serviceInfo = serviceInfo;
		this.introduceDir = introduceDir;
	}
	

	public void runStep() throws Throwable {
		System.out.println("Creating service...");

		String cmd = CommonTools.getAntSkeletonCreationCommand(introduceDir, serviceInfo.getName(), 
			serviceInfo.getDir(), serviceInfo.getPackage(), serviceInfo.getNamespace(), serviceInfo.getExtensions());
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Creating new data service failed", p.exitValue() == 0);
        
        postSkeletonCreation();
		
		System.out.println("Invoking post creation processes...");
		cmd = CommonTools.getAntSkeletonPostCreationCommand(introduceDir, serviceInfo.getName(),
			serviceInfo.getDir(), serviceInfo.getPackage(), serviceInfo.getNamespace(), serviceInfo.getExtensions());
		p = CommonTools.createAndOutputProcess(cmd);
        new StreamDumpster(p.getInputStream(), System.out).start();
        new StreamDumpster(p.getErrorStream(), System.err).start();
		p.waitFor();
		assertTrue("Service post creation process failed", p.exitValue() == 0);
        
        postSkeletonPostCreation();

		System.out.println("Building created service...");
		cmd = CommonTools.getAntAllCommand(serviceInfo.getDir());
		p = CommonTools.createAndOutputProcess(cmd);
        new StreamDumpster(p.getInputStream(), System.out).start();
        new StreamDumpster(p.getErrorStream(), System.err).start();
		p.waitFor();
		assertTrue("Build process failed", p.exitValue() == 0);
	}
    
    
    protected void postSkeletonCreation() throws Throwable {
        // subclasses can hook in here to do things post-skeleton creation
    }
    
    
    protected void postSkeletonPostCreation() throws Throwable {
        // subclasses can hook in here to do things after post-creation process has run
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
