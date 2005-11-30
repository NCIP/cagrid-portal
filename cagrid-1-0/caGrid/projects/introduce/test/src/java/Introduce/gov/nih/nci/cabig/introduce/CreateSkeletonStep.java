package gov.nih.nci.cabig.introduce;

import org.projectmobius.common.MobiusException;
import org.projectmobius.common.XMLUtilities;

import gov.nih.nci.cagrid.introduce.CommonTools;

import com.atomicobject.haste.framework.Step;

public class CreateSkeletonStep extends Step {

	public void runStep() throws Throwable {
		System.out.println("Creating the service skeleton");

		String cmd = CommonTools.getAntSkeletonCreationCommand(
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
		assertEquals(p.exitValue(),0);
	}
}
