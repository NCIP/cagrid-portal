package gov.nih.nci.cagrid.graph.domainmodelapplication;

import gov.nih.nci.cagrid.graph.uml.UMLDiagram;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import javax.swing.tree.DefaultMutableTreeNode;

public class DomainModelTreeNode extends DefaultMutableTreeNode 
{
	public DomainModel parentModel;
	
	public int type = 0;
	public String name;
	
	public static int DOMAIN = 1;
	public static int PACKAGE = 2;
	public static int CLASS = 3;
	
	public DomainModelTreeNode(DomainModel model)
	{
		this.parentModel = model;
	}
	
	

}
