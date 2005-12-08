package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.introduce.templates.DeployPropertiesTemplate;
import gov.nih.nci.cagrid.introduce.templates.JNDIConfigTemplate;
import gov.nih.nci.cagrid.introduce.templates.NamespaceMappingsTemplate;
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
		File serverConfigF = new File(baseDirectory.getAbsolutePath() + File.separator + "server-config.xml");
		FileWriter serverConfigFW = new FileWriter(serverConfigF);
		serverConfigFW.write(serverConfigS);
		serverConfigFW.close();
		
		JNDIConfigTemplate jndiConfigT = new JNDIConfigTemplate();
		String jndiConfigS = jndiConfigT.generate(properties);
		File jndiConfigF = new File(baseDirectory.getAbsolutePath() + File.separator + "jndi-config.xml");
		FileWriter jndiConfigFW = new FileWriter(jndiConfigF);
		jndiConfigFW.write(jndiConfigS);
		jndiConfigFW.close();
		
		DeployPropertiesTemplate deployPropertiesT = new DeployPropertiesTemplate();
		String deployPropertiesS = deployPropertiesT.generate(properties);
		File deployPropertiesF = new File(baseDirectory.getAbsolutePath() + File.separator + "deploy.properties");
		FileWriter deployPropertiesFW = new FileWriter(deployPropertiesF);
		deployPropertiesFW.write(deployPropertiesS);
		deployPropertiesFW.close();
		
		NamespaceMappingsTemplate namespaceMappingsT = new NamespaceMappingsTemplate();
		String namespaceMappingsS = namespaceMappingsT.generate(properties);
		File namespaceMappingsF = new File(baseDirectory.getAbsolutePath() + File.separator + "namespace2package.mappings");
		FileWriter namespaceMappingsFW = new FileWriter(namespaceMappingsF);
		namespaceMappingsFW.write(namespaceMappingsS);
		namespaceMappingsFW.close();
	}

}