package gov.nih.nci.cabig.introduce;

import gov.nih.nci.cagrid.introduce.CommonTools;

import java.io.File;

import org.projectmobius.common.MobiusException;
import org.projectmobius.common.XMLUtilities;

import com.atomicobject.haste.framework.Step;

public class CreateSkeletonStep extends Step {
	
	public void runStep() throws Throwable {
		System.out.println("Creating the service skeleton");
		
		File f = new File(".");
		System.out.println(f.getAbsolutePath());

		String pathtobasedir = System.getProperty("basedir");
		System.out.println(pathtobasedir);
		if(pathtobasedir == null){
			System.out.println("pathtobasedir system property not set");
			throw new Exception("pathtobasedir system property not set");
		}
		
		String cmd = CommonTools.getAntSkeletonCreationCommand(pathtobasedir,
				TestCaseInfo.name, TestCaseInfo.dir, TestCaseInfo.packageName,
				TestCaseInfo.namespaceDomain);
		System.out.println(cmd);
		final Process p = Runtime.getRuntime().exec(cmd);

		Thread thread1 = new Thread(new Runnable() {
			public void run() {
				try {
					
					System.out.println(XMLUtilities.streamToString(p
							.getInputStream()));
				} catch (MobiusException e) {
					e.printStackTrace();
				}

			}
		});
		thread1.start();

		Thread thread2 = new Thread(new Runnable() {
			public void run() {
				try {
					System.err.println(XMLUtilities.streamToString(p
							.getErrorStream()));
				} catch (MobiusException e) {
					e.printStackTrace();
				}

			}
		});
		thread2.start();
		
		p.waitFor();
		assertEquals(0,p.exitValue());
	}

}
