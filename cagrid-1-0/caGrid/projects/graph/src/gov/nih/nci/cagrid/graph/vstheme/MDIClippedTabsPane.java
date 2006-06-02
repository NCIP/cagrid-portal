package gov.nih.nci.cagrid.graph.vstheme;

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