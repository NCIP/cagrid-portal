package gov.nih.nci.cagrid.graph.domainmodelapplication;

import gov.nih.nci.cagrid.graph.vstheme.InternalFrame;
import gov.nih.nci.cagrid.graph.vstheme.InvertedMDIPanel;
import gov.nih.nci.cagrid.graph.vstheme.PageClosedListener;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class DomainModelOutlines extends InternalFrame
{
	public DomainModelExplorer parent;
	public InvertedMDIPanel mdi;
	
	public DomainModelOutlines(DomainModelExplorer parent)
	{
		this.parent = parent;
		mdi = new InvertedMDIPanel(this);
		
		ImageIcon domainModelImage = new ImageIcon(System.getProperty("user.dir") + "\\resource\\domainmodel.png");
		
		mdi.addPage(parent.domainModelOutline, domainModelImage, "Domain Model");
		
		this.setComponent(mdi);
		
		this.addFocusListener(new DomainModelOutlinesFocusListener());
		
		mdi.setPageClosedListener(new OutlinesPageClosedListener());
	}
	
	public void signalClose()
	{
		mdi.removePage(mdi.currentPage);
	}
	
	public void setModel(DomainModel model)
	{
		
	}
	
	public void clear()
	{
		
	}
	
	
}



class DomainModelOutlinesFocusListener extends FocusAdapter
{
	public void focusGained(FocusEvent e)
	{
		
	}
	
	public void focusLost(FocusEvent e)
	{
		
	}
}

class OutlinesPageClosedListener extends PageClosedListener
{
	public void pageClosed(InvertedMDIPanel source , JComponent removed, int left)
	{
		
		DomainModelOutlines s = (DomainModelOutlines) source.parent;
		if(left == 0)
		{
			s.parent.hideOutlines();
		}
	}
}