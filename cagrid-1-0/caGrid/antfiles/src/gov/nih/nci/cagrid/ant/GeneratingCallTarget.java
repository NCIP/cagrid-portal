package gov.nih.nci.cagrid.ant;

import java.util.Enumeration;

import net.sf.antcontrib.logic.ProjectDelegate;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Ant;
import org.apache.tools.ant.taskdefs.Property;


/**
 * Copied from antcontrib's AntCallBack to save our ResolveDependencies-like
 * properties (instead of a static list)
 * 
 * @author oster
 */
public class GeneratingCallTarget extends Ant {
	private String target = null;
	private ProjectDelegate fakeProject = null;


	public void setProject(Project realProject) {
		this.fakeProject = new ProjectDelegate(realProject);
		super.setProject(this.fakeProject);
		setAntfile(realProject.getProperty("ant.file"));
	}


	/**
	 * Do the execution.
	 * 
	 * @exception BuildException
	 *                Description of the Exception
	 */
	public void execute() throws BuildException {
		super.execute();

		// copy over any properties this task set from recursive calls
		Enumeration keys = this.fakeProject.getSubproject().getProperties().keys();
		while (keys.hasMoreElements()) {
			String propKey = (String) keys.nextElement();
			if (propKey.startsWith(ResolveDependencies.ANTCALL_PROPERTY_PREFIX)) {
				// System.out.println("Saving property:" + propKey);
				this.getProject().setProperty(propKey, "true");
			}
		}

	}


	public Property createParam() {
		return super.createProperty();
	}


	public void addConfiguredTarget(TargetElement t) {
		throw new BuildException("Nested target elements are not supported!");
	}


	public void setTarget(String target) {
		this.target = target;
		super.setTarget(target);
	}


	public String getTarget() {
		return this.target;
	}
}
