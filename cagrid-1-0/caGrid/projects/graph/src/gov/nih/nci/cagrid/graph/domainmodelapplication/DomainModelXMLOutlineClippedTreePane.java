package gov.nih.nci.cagrid.graph.domainmodelapplication;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

public class DomainModelXMLOutlineClippedTreePane extends JScrollPane
{
	public DomainModelXMLOutlineClippedTreePane()
	{
		super();
		
		this.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
	}
}
