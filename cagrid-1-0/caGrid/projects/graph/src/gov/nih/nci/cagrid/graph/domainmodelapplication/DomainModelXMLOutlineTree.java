package gov.nih.nci.cagrid.graph.domainmodelapplication;

import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import javax.swing.BorderFactory;
import javax.swing.JTree;

public class DomainModelXMLOutlineTree extends JTree
{
	
	
	public DomainModelXMLOutlineTree()
	{
		super();
		
		this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
	}
	
	public void setDomainModel(DomainModel model)
	{
		
	}

}

