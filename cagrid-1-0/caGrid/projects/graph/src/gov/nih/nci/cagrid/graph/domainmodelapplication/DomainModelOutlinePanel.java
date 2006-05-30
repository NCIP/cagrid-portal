package gov.nih.nci.cagrid.graph.domainmodelapplication;

import javax.swing.JTree;

import gov.nih.nci.cagrid.graph.vstheme.IInternalFrame;
import gov.nih.nci.cagrid.graph.vstheme.InvertedMDIPanel;

public class DomainModelOutlinePanel extends IInternalFrame
{
	InvertedMDIPanel mdi = new InvertedMDIPanel();
	
	public DomainModelOutlinePanel()
	{
		
		mdi.addPage(new JTree(), null, "Outline 1");
		mdi.addPage(new JTree(), null, "Outline 2");
		
		this.setComponent(mdi);
		
	}
	
	public void signalClose()
	{
		mdi.removePage(mdi.currentPage);
	}
}
