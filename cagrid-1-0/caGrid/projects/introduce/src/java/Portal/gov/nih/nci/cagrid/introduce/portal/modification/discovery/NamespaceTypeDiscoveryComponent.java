package gov.nih.nci.cagrid.introduce.portal.modification.discovery;

import java.io.File;

import javax.swing.JPanel;

import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;


public abstract class NamespaceTypeDiscoveryComponent extends JPanel {
	private DiscoveryExtensionDescriptionType descriptor;


	public NamespaceTypeDiscoveryComponent(DiscoveryExtensionDescriptionType descriptor) {
		this.descriptor = descriptor;
	}


	public DiscoveryExtensionDescriptionType getDescriptor() {
		return descriptor;
	}


	public abstract NamespaceType createNamespaceType(File schemaDestinationDir);

}
