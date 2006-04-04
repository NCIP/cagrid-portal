package gov.nih.nci.cagrid.introduce.portal.modification.discovery;

import java.io.File;

import javax.swing.JPanel;

import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;

public abstract class NamespaceTypeDiscoveryComponent extends JPanel {
	
	public abstract NamespaceType createNamespaceType(File schemaDestinationDir);

}
