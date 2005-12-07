package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.introduce.templates.ServiceClientTemplate;
import gov.nih.nci.cagrid.introduce.templates.ServiceITemplate;
import gov.nih.nci.cagrid.introduce.templates.ServiceImplTemplate;
import gov.nih.nci.cagrid.introduce.templates.ServiceProviderImplTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class SkeletonSourceCreator {

	public SkeletonSourceCreator() {
	}

	public void createSkeleton(Properties properties) {
		File baseDirectory = new File(properties.getProperty("introduce.skeleton.destination.dir"));
		baseDirectory.mkdirs();
		File srcDir = new File(baseDirectory.getAbsolutePath() + File.separator + "src");
		srcDir.mkdir();

		ServiceClientTemplate clientT = new ServiceClientTemplate();
		String clientS = clientT.generate(properties);
		File clientF = new File(srcDir.getAbsolutePath() + File.separator + properties.getProperty("introduce.skeleton.package.dir") + File.separator + "client" + File.separator + properties.getProperty("introduce.skeleton.service.name") + "Client.java");
		try {
			FileWriter clientFW = new FileWriter(clientF);
			clientFW.write(clientS);
			clientFW.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ServiceITemplate iT = new ServiceITemplate();
		String iS = iT.generate(properties);
		File iF = new File(srcDir.getAbsolutePath() + File.separator + properties.getProperty("introduce.skeleton.package.dir") + File.separator + "common" + File.separator + properties.getProperty("introduce.skeleton.service.name") + "I.java");
		try {
			FileWriter iFW = new FileWriter(iF);
			iFW.write(iS);
			iFW.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ServiceImplTemplate implT = new ServiceImplTemplate();
		String implS = implT.generate(properties);
		File implF = new File(srcDir.getAbsolutePath() + File.separator + properties.getProperty("introduce.skeleton.package.dir") + File.separator + "service" + File.separator + properties.getProperty("introduce.skeleton.service.name") + "Impl.java");
		try {
			FileWriter implFW = new FileWriter(implF);
			implFW.write(implS);
			implFW.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ServiceProviderImplTemplate providerImplT = new ServiceProviderImplTemplate();
		String providerImplS = providerImplT.generate(properties);
		File providerImplF = new File(srcDir.getAbsolutePath() + File.separator + properties.getProperty("introduce.skeleton.package.dir") + File.separator + "service" + File.separator + "globus" + File.separator + properties.getProperty("introduce.skeleton.service.name") + "ProviderImpl.java");
		try {
			FileWriter providerImplFW = new FileWriter(providerImplF);
			providerImplFW.write(providerImplS);
			providerImplFW.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SkeletonSourceCreator sc = new SkeletonSourceCreator();
	}

}
