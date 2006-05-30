package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class InvertedMDIPanel extends JComponent
{
	
	public Vector pages = new Vector();
	public Vector pageIcons = new Vector();
	public Vector pageTitles = new Vector();
	
	public int currentPage = -1;
	
	public MultipleComponentContainer container = new MultipleComponentContainer();
	public InvertedTabsPane tabs = new InvertedTabsPane(this);
	
	public InvertedMDIPanel()
	{
		this.add(container);
		this.add(tabs);
		
		this.addComponentListener(new InvertedMDIPanelComponentListener());
		
	}
	
	public void addPage(JComponent component, ImageIcon icon, String title)
	{
		this.pages.add(component);
		this.container.addComponent(component);
		this.pageIcons.add(icon);
		this.pageTitles.add(title);
		this.tabs.addTab(title, icon);
		
		this.setActivePage(pages.size() - 1);
		
		
		
	}
	
	public void removePage(int i)
	{
		if(i < this.pages.size() && i >= 0)
		{
			pages.remove(i);
			pageIcons.remove(i);
			pageTitles.remove(i);


			container.removeComponent(i);
			tabs.removeTab(i);
			
			
			this.setActivePage(0);
			
		}
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

class InvertedMDIPanelComponentListener extends ComponentAdapter
{
	public void componentResized(ComponentEvent e)
	{
		InvertedMDIPanel s = (InvertedMDIPanel) e.getSource();
		
		s.container.setBounds(0, 0, s.getWidth(), s.getHeight() - 27);
		s.tabs.setBounds(0, s.getHeight() - 26, s.getWidth(), 28);
		
		s.validate();
		
	}
}
