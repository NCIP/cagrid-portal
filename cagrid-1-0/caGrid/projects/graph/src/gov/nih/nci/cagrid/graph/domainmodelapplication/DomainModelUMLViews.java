package gov.nih.nci.cagrid.graph.domainmodelapplication;

import gov.nih.nci.cagrid.graph.vstheme.MDIPanel;

import java.awt.Color;
import java.awt.Graphics;

public class DomainModelUMLViews extends MDIPanel
{
	public DomainModelExplorer parent;
	
	public DomainModelUMLViews(DomainModelExplorer parent)
	{
		this.parent = parent;
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		g.setColor(Color.gray);
		g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
	}
	

	
}
