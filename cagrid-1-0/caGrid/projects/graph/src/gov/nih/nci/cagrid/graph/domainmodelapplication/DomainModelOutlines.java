package gov.nih.nci.cagrid.graph.domainmodelapplication;

import javax.swing.JTree;

import gov.nih.nci.cagrid.graph.vstheme.InternalFrame;
import gov.nih.nci.cagrid.graph.vstheme.InvertedMDIPanel;

public class DomainModelOutlines extends InternalFrame
{
	public InvertedMDIPanel mdi;
	
	public DomainModelOutlines()
	{
		mdi = new InvertedMDIPanel(this);
		
		mdi.addPage(new JTree(), null, "Outline 1");
		mdi.addPage(new JTree(), null, "Outline 2");
		mdi.addPage(new JTree(), null, "Outline 3");
		mdi.addPage(new JTree(), null, "Outline 4");		
		this.setComponent(mdi);
		
	}
	
	public void signalClose()
	{
		mdi.removePage(mdi.currentPage);
	}
}
