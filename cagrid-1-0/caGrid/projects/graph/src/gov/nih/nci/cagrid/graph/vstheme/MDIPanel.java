package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class MDIPanel extends JComponent
{
	public MDIClippedTabsPane tabs;
	public MultipleComponentContainer container;
	
	
	public Vector pages = new Vector();
	public Vector pageIcons = new Vector();
	public Vector pageTitles = new Vector();
	public Vector pageIDs = new Vector();

	
	public int currentPage = -1;
	
	public static final int tabsHeight = 27;
	
	public MDIPanel()
	{
		this.tabs = new MDIClippedTabsPane(this);
		this.container = new MultipleComponentContainer();
		
		this.add(tabs);
		this.add(container);
		
		this.addComponentListener(new MDIPanelComponentListener());
		
	}
	
	public void addPage(JComponent component, ImageIcon icon, String title, String id)
	{
		this.pages.add(component);
		this.pageIcons.add(icon);
		this.pageTitles.add(title);
		this.pageIDs.add(id);
		
		
		this.container.addComponent(component);
		this.tabs.addTab(title, icon);
		
		this.setActivePage(pages.size() - 1);
		
		
		
	}
		
		
	
	public void removePage(int i)
	{
		if(i < this.pages.size() && i >= 0)
		{	
					
			this.pages.remove(i);
			this.pageIcons.remove(i);
			this.pageTitles.remove(i);
			this.pageIDs.remove(i);
			
			this.tabs.tabsPane.removeTab(i);
			this.tabs.repositionAndResize();
			this.container.removeComponent(i);
			
			this.setActivePage(0);
		}
	}
	
	public void removeCurrentPage()
	{
	
		removePage(currentPage);
	}
	
	public void setActivePage(int i)
	{
		if(i < this.pages.size() && i >= 0)
		{	
			currentPage = i;

			this.tabs.setActiveTab(i);
			this.container.showComponent(i);
		}
		
		this.fireResize();
		
	}
	
	public void setActivePageByID(String id)
	{
		// NOTE: if there are more than one instances
		// of the same page shown in the MDI, this
		// will activate the last one.
		
		for(int k = 0; k < this.pages.size(); k++)
		{
			if(pageIDs.get(k).equals(id))
			{
				setActivePage(k);
			}
		}
	}
	
	public JComponent getPageById(String id)
	{
		JComponent rval = null;
		for(int k = 0; k < this.pages.size(); k++)
		{
			if(pageIDs.get(k).equals(id))
			{
				rval= (JComponent) pages.elementAt(k);
			}
		}
		return rval;
	}
	
	public void setActivePageByTitle(String title)
	{
		// NOTE: if there are more than one instances
		// of the same page shown in the MDI, this
		// will activate the last one.
		
		for(int k = 0; k < this.pages.size(); k++)
		{
			if(pageTitles.get(k).equals(title))
			{
				setActivePage(k);
			}
		}		
	}
	
	public int getPageCount()
	{
		return this.pages.size();
	}
	
	public void fireResize()
	{
		this.setSize(this.getWidth() , this.getHeight() - 1);
		this.setSize(this.getWidth() , this.getHeight() + 1);
	}
	
	public void resizeChildren()
	{
		MDIPanel s = this;
		s.tabs.setBounds(1, 0, s.getWidth()-2, MDIPanel.tabsHeight);
		s.container.setBounds(1, MDIPanel.tabsHeight, s.getWidth()-2, s.getHeight()-MDIPanel.tabsHeight-1);
		s.validate();		
	}

}

class MDIPanelComponentListener extends ComponentAdapter
{
	public void componentResized(ComponentEvent e)
	{
		MDIPanel s = (MDIPanel)e.getSource();
		s.resizeChildren();
		s.tabs.repositionAndResize();
	}
}