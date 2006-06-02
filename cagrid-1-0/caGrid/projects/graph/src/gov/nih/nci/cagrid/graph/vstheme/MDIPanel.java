package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;

public class MDIPanel extends JComponent
{
	public MDIClippedTabsPane tabs;
	public MultipleComponentContainer components;
	
	public static final int tabsHeight = 25;
	
	public MDIPanel()
	{
		this.add(tabs);
		this.add(components);
		
	}
	
	public void addPage()
	{
		
	}
	
	public void removePage(int i)
	{
		
	}
	
	public void setActivePage(int i)
	{
		
	}

}

class MDIPanelComponentListener extends ComponentAdapter
{
	public void componentResized(ComponentEvent e)
	{
		MDIPanel s = (MDIPanel)e.getSource();
		s.tabs.setBounds(0, 0, s.getWidth(), MDIPanel.tabsHeight);
		s.components.setBounds(0, MDIPanel.tabsHeight, s.getWidth(), s.getHeight()-MDIPanel.tabsHeight);
	}
}