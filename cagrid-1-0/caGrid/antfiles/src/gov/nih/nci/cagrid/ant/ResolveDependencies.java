package gov.nih.nci.cagrid.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.CallTarget;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.types.FileSet;


/**
 * @author oster
 */
public class ResolveDependencies extends Task {

	private List artifactList;
	private File extDir;
	private File targetDir;


	public ResolveDependencies() {
		this.artifactList = new ArrayList();

	}


	public File getExtDir() {
		return extDir;
	}


	/**
	 * Set the base directory in which artifacts will be copied based on the
	 * types of artifacts. Ignored if targetDir is set.
	 * 
	 * @param extDir
	 */
	public void setExtDir(File extDir) {
		this.extDir = extDir;
	}


	/**
	 * Add an artifact dependency to resolve
	 * 
	 * @param art
	 */
	public void addConfiguredArtifact(Artifact art) {
		this.artifactList.add(art);
	}


	public void execute() throws BuildException {
		super.execute();
		for (int i = 0; i < artifactList.size(); i++) {
			Artifact artifact = (Artifact) artifactList.get(i);
			CallTarget antCall = artifact.getAntCall();
			if (antCall != null) {
				antCall.setProject(this.getProject());
				antCall.execute();
			}

			// configure a copy task to send the created artifacts where they
			// need to go
			Copy copyTask = new Copy();
			copyTask.setProject(this.getProject());
			copyTask.setOverwrite(true);
			// add the files from the artifact
			for (int j = 0; j < artifact.getFileSetList().size(); j++) {
				FileSet fileset = (FileSet) artifact.getFileSetList().get(j);
				copyTask.addFileset(fileset);
			}
			// figure out where to copy artifacts
			if (getTargetDir() == null) {
				if (artifact.getType().equals(Artifact.JAR_TYPE)) {
					copyTask.setTodir(new File(getExtDir().getAbsolutePath() + File.separator + "lib"));
				} else if (artifact.getType().equals(Artifact.SCHEMAS_TYPE)) {
					copyTask.setTodir(new File(getExtDir().getAbsolutePath() + File.separator + "schema"));
				} else if (artifact.getType().equals(Artifact.MAPPINGS_TYPE)) {
					copyTask.setTodir(new File(getExtDir().getAbsolutePath()));
				}

			} else {
				copyTask.setTodir(getTargetDir());
			}
			copyTask.execute();

		}
	}


	public File getTargetDir() {
		return targetDir;
	}


	/**
	 * Explicitly set the directory to copy all artifacts to. This overrides the
	 * default artifact locations that would be used, relative to the extDir.
	 * 
	 * @param targetDir
	 */
	public void setTargetDir(File targetDir) {
		this.targetDir = targetDir;
	}

}
