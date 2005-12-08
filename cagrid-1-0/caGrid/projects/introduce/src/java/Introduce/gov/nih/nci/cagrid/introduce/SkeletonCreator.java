package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.introduce.creator.SkeletonBaseCreator;
import gov.nih.nci.cagrid.introduce.creator.SkeletonDocsCreator;
import gov.nih.nci.cagrid.introduce.creator.SkeletonEtcCreator;
import gov.nih.nci.cagrid.introduce.creator.SkeletonSchemaCreator;
import gov.nih.nci.cagrid.introduce.creator.SkeletonSourceCreator;

import java.io.File;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class SkeletonCreator extends Task {

	public SkeletonCreator() {
	}

	public void execute() throws BuildException {
		super.execute();
		Properties properties = new Properties();
		properties.putAll(this.getProject().getProperties());
		SkeletonBaseCreator sbc = new SkeletonBaseCreator();
		SkeletonSourceCreator ssc = new SkeletonSourceCreator();
		SkeletonSchemaCreator sscc = new SkeletonSchemaCreator();
		SkeletonEtcCreator sec = new SkeletonEtcCreator();
		SkeletonDocsCreator sdc = new SkeletonDocsCreator();

		// Create the overall skeleton
		File baseDirectory = new File(properties
				.getProperty("introduce.skeleton.destination.dir"));
		baseDirectory.mkdirs();

		// Generate the source
		try {
			sbc.createSkeleton(properties);
			ssc.createSkeleton(properties);
			sscc.createSkeleton(properties);
			sec.createSkeleton(properties);
			sdc.createSkeleton(properties);
		} catch (Exception e) {
			BuildException be = new BuildException(e.getMessage());
			be.setStackTrace(e.getStackTrace());
			throw be;
		}
	}
}