package gov.nih.nci.cagrid.introduce.extension.example;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionPostProcessor;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class ExampleCreationPostProcessor implements CreationExtensionPostProcessor {

	public void postCreate(ServiceDescription serviceDescription, Properties serviceProperties) throws CreationExtensionException {
		// TODO Auto-generated method stub
		String fileName = serviceProperties.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + this.getClass().getName();
		File file = new File(fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			throw new CreationExtensionException(e.getMessage(),e);
		}
	}

}
