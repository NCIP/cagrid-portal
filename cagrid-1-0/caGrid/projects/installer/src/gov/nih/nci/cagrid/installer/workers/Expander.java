package gov.nih.nci.cagrid.installer.workers;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.taskdefs.Expand;


public class Expander extends Expand{

	public Expander(File src, File dest) {
 	    project = new Project();
	    project.init();
	    taskType = "unzip";
	    taskName = "unzip";
	    target = new Target();
	    this.setSrc(src);
	    this.setDest(dest);
	}
	public void expand(){
		this.execute();
	}

}
