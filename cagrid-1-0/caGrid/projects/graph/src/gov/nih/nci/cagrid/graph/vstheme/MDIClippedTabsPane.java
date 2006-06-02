package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;



public class MDIClippedTabsPane extends JScrollPane
{
	public MDITabsPane tabsPane;
	public MDIPanel parent;
	public MDICloseButton closeButton;
	public MDIScrollLeftButton scrollLeftButton = new MDIScrollLeftButton(this);
	public MDIScrollRightButton scrollRightButton = new MDIScrollRightButton(this);
	public Point point = new Point(0,0);
	
	

	public MDIClippedTabsPane(MDIPanel parent)
	{
		super();
		this.parent = parent;
		tabsPane = new MDITabsPane(this);
		closeButton = new MDICloseButton(parent);
		
		this.setViewportView(tabsPane);
		
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
	}
	
	public void addTab(String title, ImageIcon icon)
	{
		this.tabsPane.addTab(title, icon);	
	}
	
	public void setActiveTab(int i)
	{
		this.tabsPane.setActiveTab(i);
		this.scrollToShowTab(i);
	}
	
	public void scrollToShowTab(int i)
	{
		if(i < this.tabsPane.tabs.size() && i >= 0)
		{
			// if tab is not present in the current view, scroll
			// the view to show tab
			
			// if tab is present in current view
			// then do nothing
		}
	}
	
	public void scrollHorizontally(int x)
	{
		int currentX = this.getViewport().getViewPosition().x;
		
		// requesting a left scroll
		if(x < 0)
		{
			if(-x < currentX)
			{
				point.x = this.getViewport().getViewPosition().x + x;
				point.y = this.getViewport().getViewPosition().y;	
				// grant the scroll
				this.getViewport().setViewPosition(point);
			}
			else
			{
				point.x = 0;
				point.y = this.getViewport().getViewPosition().y;	
				// grant a limited scroll
				this.getViewport().setViewPosition(point);				
			}
			this.getViewport().setViewPosition(point);
		}
		// requesting a right scroll
		else
		{
			point.x = this.getViewport().getViewPosition().x + x;
			point.y = this.getViewport().getViewPosition().y;
			this.getViewport().setViewPosition(point);
		}
	}
	
	public void disableScrolling()
	{
		this.scrollLeftButton.setVisible(false);
		this.scrollRightButton.setVisible(false);
	}
	
	public void enableScrolling()
	{
		this.scrollLeftButton.setVisible(true);
		this.scrollRightButton.setVisible(true);		
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
			s.disableScrolling();
		}
		else
		{
			s.tabsPane.setSize(s.getHeight(), s.tabsPane.getPreferredWidth());
			s.enableScrolling();
		}
	}
}