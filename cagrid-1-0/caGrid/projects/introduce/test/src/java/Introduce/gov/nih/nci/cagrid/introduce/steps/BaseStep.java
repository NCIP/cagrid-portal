package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;

import com.atomicobject.haste.framework.Step;

public abstract class BaseStep extends Step {

	private String baseDir;
	private String serviceDir;
	private boolean build;

	public BaseStep(String serviceDir, boolean build) throws Exception {
		baseDir = System.getProperty("basedir");
		if (baseDir == null) {
			System.err.println("basedir system property not set");
			throw new Exception("basedir system property not set");
		}
		this.serviceDir = serviceDir;
		this.build = build;
	}
	
	public String getBaseDir(){
		return baseDir;
	}

	public abstract void runStep() throws Throwable;

	public void buildStep() throws Throwable {
		String cmd = CommonTools.getAntAllCommand(new File (baseDir + File.separator + serviceDir).getAbsolutePath());

		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals("Checking build status", 0, p.exitValue());
	}

}
