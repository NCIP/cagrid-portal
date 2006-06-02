package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JScrollPane;



public class MDIClippedTabsPane extends JScrollPane
{
	public MDITabsPane tabsPane = new MDITabsPane();
	

	public MDIClippedTabsPane(MDIPanel parent)
	{
		super();
		
		this.setViewportView(tabsPane);
		
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
	}
	
	public void setActivePage(int i)
	{
		
	}
}

class MDIClippedTabsPaneComponentListener extends ComponentAdapter
{
	public void componentResized(ComponentEvent e)
	{
		MDIClippedTabsPane s = (MDIClippedTabsPane) e.getSource();
		
		if(s.tabsPane.getPreferredWidth() < s.getWidth())
		{
			s.tabsPane.setSize(s.getHeight(), s.getWidth());
		}
	}
}