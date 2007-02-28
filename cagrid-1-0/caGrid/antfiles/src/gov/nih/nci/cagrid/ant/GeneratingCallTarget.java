package gov.nih.nci.cagrid.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.CallTarget;
import org.apache.tools.ant.taskdefs.Ant.TargetElement;


/**
 * @author oster
 */
public class GeneratingCallTarget extends CallTarget {
	private String target = null;


	@Override
	public void addConfiguredTarget(TargetElement t) {
		throw new BuildException("Nested target elements are not supported!");
	}


	@Override
	public void setTarget(String target) {
		this.target = target;
		super.setTarget(target);
	}


	public String getTarget() {
		return this.target;
	}
}
