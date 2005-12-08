package gov.nih.nci.cagrid.common.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;


/**
 * @author oster
 */
public class DependOnTask extends Task {

	// <attribute name="dependingProject" description="The short name of the
	// project requiring the artifact" />
	// <attribute name="providingProject" description="The short name of the
	// project providing the artifact" />
	// <attribute name="artifactType" description="The type of artifact the
	// project is providing (e.g jars)" default="jars" />
	// <attribute name="artifactTrack" description="The track of artifact the
	// project is providing (e.g main or test)" default="main" />

	private String providingProject;
	private String dependingProject;
	private String artifactTrack;
	private String artifactType;


	public DependOnTask() {
	}


	public String getArtifactTrack() {
		return artifactTrack;
	}


	public void setArtifactTrack(String artifactTrack) {
		this.artifactTrack = artifactTrack;
	}


	public String getArtifactType() {
		return artifactType;
	}


	public void setArtifactType(String artifactType) {
		this.artifactType = artifactType;
	}


	public String getDependingProject() {
		return dependingProject;
	}


	public void setDependingProject(String dependingProject) {
		this.dependingProject = dependingProject;
	}


	public String getProvidingProject() {
		return providingProject;
	}


	public void setProvidingProject(String providingProject) {
		this.providingProject = providingProject;
	}


	public void execute() throws BuildException {
		super.execute();

		//TODO: put the logic to check the types and copy to the right location
	}

}
