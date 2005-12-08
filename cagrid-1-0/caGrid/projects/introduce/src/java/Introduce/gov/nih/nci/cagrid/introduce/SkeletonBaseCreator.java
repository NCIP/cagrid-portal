package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.introduce.templates.JNDIConfigTemplate;
import gov.nih.nci.cagrid.introduce.templates.ServerConfigTemplate;

import java.io.File;
import java.io.FileWriter;
import java.util.Properties;


public class SkeletonBaseCreator {

	public SkeletonBaseCreator() {
	}


	public void createSkeleton(Properties properties) throws Exception {
		File baseDirectory = new File(properties.getProperty("introduce.skeleton.destination.dir"));
		
		ServerConfigTemplate serverConfigT = new ServerConfigTemplate();
		String serverConfigS = serverConfigT.generate(properties);
		File serverConfigF = new File(baseDirectory.getAbsolutePath() + File.separator + "server-conifig.xml");
		FileWriter serverConfigFW = new FileWriter(serverConfigF);
		serverConfigFW.write(serverConfigS);
		serverConfigFW.close();
		
		JNDIConfigTemplate jndiConfigT = new JNDIConfigTemplate();
		String jndiConfigS = jndiConfigT.generate(properties);
		File jndiConfigF = new File(baseDirectory.getAbsolutePath() + File.separator + "jndi-conifig.xml");
		FileWriter jndiConfigFW = new FileWriter(jndiConfigF);
		jndiConfigFW.write(jndiConfigS);
		jndiConfigFW.close();
	}

}