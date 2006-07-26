package gov.nih.nci.cagrid.introduce.portal.steps;

import com.atomicobject.haste.framework.Step;


public abstract class BaseStep extends Step {

	private String baseDir;

	public BaseStep() throws Exception {
		baseDir = System.getProperty("basedir");
		if (baseDir == null) {
			System.err.println("basedir system property not set");
			throw new Exception("basedir system property not set");
		}
		
	}

	public String getBaseDir() {
		return baseDir;
	}


	public abstract void runStep() throws Throwable;

}
