package gov.nih.nci.cagrid.data.creation;

import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.util.Properties;

/** 
 *  BDTFeatureCreator
 *  Provides implementation and methods for BDT query
 * 
 * @author David Ervin
 * 
 * @created Mar 9, 2007 3:45:48 PM
 * @version $Id: BDTFeatureCreator.java,v 1.1 2007-03-10 16:31:36 dervin Exp $ 
 */
public class BDTFeatureCreator extends FeatureCreator {

	public BDTFeatureCreator(ServiceInformation info, ServiceType mainService, Properties serviceProps) {
		super(info, mainService, serviceProps);
	}


	public void addFeature() throws CreationExtensionException {
		ensureBdtExtensionAdded();
		addBdtQueryMethod();
	}
	
	
	private void addBdtQueryMethod() throws CreationExtensionException {
		
	}
	
	
	/**
	 * Verifies the BDT extension is installed and if it needs to be included
	 * in this service, adds it.
	 * 
	 * @throws CreationExtensionException
	 */
	private void ensureBdtExtensionAdded() throws CreationExtensionException {
		ExtensionDescription bdtDescription = ExtensionsLoader.getInstance().getExtension("bdt");
		if (bdtDescription == null) {
			throw new CreationExtensionException("BDT Extension is not installed!");
		}
		
		boolean bdtExtensionUsed = false;
		ExtensionType[] usedExtensions = getServiceInformation().getExtensions().getExtension();
		for (int i = 0; i < usedExtensions.length; i++) {
			if (usedExtensions[i].getName().equals("bdt")) {
				bdtExtensionUsed = true;
				break;
			}
		}
		
		if (!bdtExtensionUsed) {
			ExtensionType bdtExtension = new ExtensionType();
			bdtExtension.setName(bdtDescription.getServiceExtensionDescription().getName());
			bdtExtension.setExtensionType(bdtDescription.getExtensionType());
			bdtExtension.setVersion(bdtDescription.getVersion());
			ExtensionType[] moreExtensions = new ExtensionType[usedExtensions.length + 1];
			moreExtensions[0] = bdtExtension;
			System.arraycopy(usedExtensions, 0, moreExtensions, 1, usedExtensions.length);
			getServiceInformation().getExtensions().setExtension(moreExtensions);
		}
	}
}
