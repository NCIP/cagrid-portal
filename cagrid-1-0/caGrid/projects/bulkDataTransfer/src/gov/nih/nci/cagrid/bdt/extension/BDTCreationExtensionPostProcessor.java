package gov.nih.nci.cagrid.bdt.extension;

import java.util.Properties;

import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionPostProcessor;

public class BDTCreationExtensionPostProcessor implements CreationExtensionPostProcessor {

	public void postCreate(ServiceDescription descriptor, Properties properties) throws CreationExtensionException {
		// 1. copy over all the relevent files
		// 2. add in the namespacetypes
		// 3. add in the BDT service
		// 4. add in the BDT metadata to the using service

	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
