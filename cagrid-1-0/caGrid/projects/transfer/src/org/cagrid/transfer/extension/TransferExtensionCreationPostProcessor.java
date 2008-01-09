package org.cagrid.transfer.extension;

import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionPostProcessor;


public class TransferExtensionCreationPostProcessor implements CreationExtensionPostProcessor {

    public void postCreate(ServiceExtensionDescriptionType desc, ServiceInformation info)
        throws CreationExtensionException {
        //edit the JDNI so that the service can get a handle to the Transfer Service
        
        //copy in the transfer jars
        
        //copy in the caGrid transfer schema and add the namespace types for it

    }

}
