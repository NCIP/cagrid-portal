package gov.nih.nci.cagrid.introduce.creator;

import gov.nih.nci.cagrid.common.portal.PortalUtils;

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

		String service = properties
				.getProperty("introduce.skeleton.service.name");
		if (!service.matches("[A-Z]++[A-Za-z0-9\\_\\$]*")) {
			System.err
					.println("Service Name can only contain [A-Z]++[A-Za-z0-9\\_\\$]*");
			return;
		}
		if (service.substring(0, 1).toLowerCase().equals(
				service.substring(0, 1))) {
			System.err
					.println("Service Name cannnot start with lower case letters.");
			return;
		}
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