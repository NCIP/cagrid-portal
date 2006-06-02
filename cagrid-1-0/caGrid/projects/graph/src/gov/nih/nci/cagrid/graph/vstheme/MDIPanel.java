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
	
	public static final int tabsHeight = 25;
	
	public MDIPanel()
	{
		this.add(tabs);
		this.add(container);
		
		this.addComponentListener(new MDIPanelComponentListener());
		
	}
	
	public void addPage(JComponent component, ImageIcon icon, String title, String id)
	{
		this.pages.add(component);
		this.container.addComponent(component);
		this.pageIcons.add(icon);
		this.pageTitles.add(title);
		this.pageIDs.add(id);
		this.tabs.addTab(title, icon);
		
		this.setActivePage(pages.size() - 1);
	}
		
		
	
	public void removePage(int i)
	{
		
	}
	
	public void removeCurrentPage()
	{
		
		
	}
	
	public void setActivePage(int i)
	{
		if(i < this.pages.size() && i >= 0)
		{	
			currentPage = i;

			this.tabs.setActiveTab(i);
			this.container.showComponent(i);
		}
		
	}
	
	public void setActivePageByID(String id)
	{
		

	}
	
	public void setActivePageByTitle(String title)
	{
		
	}
	
	public int getPageCount()
	{
		return this.pages.size();
	}

}

class MDIPanelComponentListener extends ComponentAdapter
{
	public void componentResized(ComponentEvent e)
	{
		MDIPanel s = (MDIPanel)e.getSource();
		s.tabs.setBounds(0, 0, s.getWidth(), MDIPanel.tabsHeight);
		s.container.setBounds(0, MDIPanel.tabsHeight, s.getWidth(), s.getHeight()-MDIPanel.tabsHeight);
	}
}