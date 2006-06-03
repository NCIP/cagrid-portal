package gov.nih.nci.cagrid.graph.domainmodelapplication;

import gov.nih.nci.cagrid.graph.vstheme.InternalFrame;
import gov.nih.nci.cagrid.graph.vstheme.InvertedMDIPanel;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import javax.swing.JTree;

public class DomainModelOutlines extends InternalFrame
{
	public InvertedMDIPanel mdi;
	
	public DomainModelOutlines(DomainModel model)
	{
		mdi = new InvertedMDIPanel(this);
		mdi.addPage(new JTree(), null, "Outline 1");
		mdi.addPage(new JTree(), null, "Outline 2");
	
		this.setComponent(mdi);
		
	}
	
	public void signalClose()
	{
		mdi.removePage(mdi.currentPage);
	}
}
