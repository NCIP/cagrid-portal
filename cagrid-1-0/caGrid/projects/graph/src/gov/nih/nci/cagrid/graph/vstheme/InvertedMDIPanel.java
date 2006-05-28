package gov.nih.nci.cagrid.graph.vstheme;

import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class InvertedMDIPanel extends JComponent
{
	
	public Vector pages;
	public Vector pageIcons;
	public Vector pageTitles;
	
	public MultipleComponentContainer container;
	public InvertedTabsPane tabs;
	
	public void addPage(JComponent component, ImageIcon icon, String title)
	{
		this.pages.add(component);
		this.pageIcons.add(icon);
		this.pageTitles.add(title);
		
		this.tabs.addTab(title, icon);
		
		this.setActivePage(pages.size() - 1);
		
		
		
	}
	
	public void setActivePage(int i)
	{
		this.tabs.setActiveTab(i);
		this.container.showComponent(i);

	}
	
	public void setFocus()
	{
		
	}

	public void replacePage(int i , JComponent component, ImageIcon icon, String title)
	{
		if(i < this.pages.size() && i >= 0)
		{
			this.pages.set(i, component);
			this.pageIcons.set(i, icon);
			this.pageTitles.set(i, title);
		}
	}
	
	public int getPageCount()
	{
		return this.pages.size();
	}
	
}
