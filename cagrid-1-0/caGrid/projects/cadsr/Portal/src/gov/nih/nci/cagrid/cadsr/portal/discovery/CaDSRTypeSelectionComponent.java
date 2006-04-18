package gov.nih.nci.cagrid.cadsr.portal.discovery;

import java.io.File;

import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;


public class CaDSRTypeSelectionComponent extends NamespaceTypeDiscoveryComponent {

	public CaDSRTypeSelectionComponent(DiscoveryExtensionDescriptionType desc) {
		super(desc);
	}


	public NamespaceType createNamespaceType(File storeToLocation) {
		return null;
	}

}
