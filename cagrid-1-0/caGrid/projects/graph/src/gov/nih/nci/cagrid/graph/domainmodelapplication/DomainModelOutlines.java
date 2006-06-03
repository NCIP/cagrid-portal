package gov.nih.nci.cagrid.graph.domainmodelapplication;

import gov.nih.nci.cagrid.graph.vstheme.InternalFrame;
import gov.nih.nci.cagrid.graph.vstheme.InvertedMDIPanel;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

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
		
		this.addFocusListener(new DomainModelOutlinesFocusListener());
	}
	
	public void signalClose()
	{
		mdi.removePage(mdi.currentPage);
	}
}


class DomainModelOutlinesFocusListener extends FocusAdapter
{
	public void focusGained(FocusEvent e)
	{
		System.out.println("lost");
	}
	
	public void focusLost(FocusEvent e)
	{
		System.out.println("gained");
	}
}