package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;



public class MDIClippedTabsPane extends JLayeredPane
{
	public MDITabsPane tabsPane;
	public MDITabsPane utilityBox;
	public MDIPanel parent;
	public MDICloseButton closeButton;
	public MDIScrollLeftButton scrollLeftButton ;
	public MDIScrollRightButton scrollRightButton ;
	public Point point = new Point(0,0);
	
	public int current  = 0;
	
	public static int utilityBoxWidth = 50;
	

	public MDIClippedTabsPane(MDIPanel parent)
	{
		super();
		
	

		this.parent = parent;
		tabsPane = new MDITabsPane(this);
		utilityBox = new MDITabsPane(this);
		closeButton = new MDICloseButton(parent);
		scrollRightButton = new MDIScrollRightButton(parent);
		scrollLeftButton = new MDIScrollLeftButton(parent);
		
		this.add(tabsPane);
		this.add(utilityBox);
		
		this.add(closeButton);
		this.add(scrollRightButton);
		this.add(scrollLeftButton);
		
		this.setLayer(utilityBox, JLayeredPane.MODAL_LAYER.intValue());
		this.setLayer(closeButton, JLayeredPane.POPUP_LAYER.intValue());
		this.setLayer(scrollRightButton, JLayeredPane.POPUP_LAYER.intValue());
		this.setLayer(scrollLeftButton, JLayeredPane.POPUP_LAYER.intValue());

		
		this.addComponentListener(new MDIClippedTabsPaneComponentListener());
		
		
	}
	
	public void addTab(String title, ImageIcon icon)
	{
		this.tabsPane.addTab(title, icon);	
	}
	
	public void tabClicked(int i)
	{
		parent.setActivePage(i);
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
	
	public void scrollToPosition(int x)
	{
	
		
		if(this.tabsPane.getPreferredWidth() > this.getMWidth())
		{

			if(x >= 0 && x < this.tabsPane.getWidth() - this.getMWidth())
			{
				this.current = x; 
				this.repositionAndResize();
			}
			else
			{
				if(x < this.tabsPane.getWidth() - this.getMWidth() && x < 0)
				{
					this.current = 0; 
					this.repositionAndResize();
						
				}
				else if(x >= this.tabsPane.getWidth() - this.getMWidth() && x >= 0)
				{
					this.current = this.tabsPane.getWidth() - this.getMWidth(); 
					this.repositionAndResize();		
				}
				else
				{
					System.out.println("MDIClippedTabsPane: should not be here");
				}
			}
		}
	}
	
	public void scrollHorizontally(int x)
	{
		scrollToPosition(current + x);
	}
	
	public int getMWidth()
	{
		return getWidth() - utilityBoxWidth;
	}
	
	public void disableScrolling()
	{
		this.scrollLeftButton.disable();
		this.scrollRightButton.disable();
	}
	
	public void enableScrolling()
	{
		this.scrollLeftButton.enable();
		this.scrollRightButton.enable();
	}
	
	
	
	public void repositionAndResize()
	{
		
		MDIClippedTabsPane s = this;
		
		if(s.tabsPane.getPreferredWidth() < s.getMWidth())
		{
			
			s.tabsPane.setBounds(0, 0, s.getMWidth(), s.getHeight());
			s.disableScrolling();
		}
		else
		{	
			if(getMWidth() <= tabsPane.getPreferredWidth() - current)
			{
				s.tabsPane.setBounds(-s.current, 0, s.tabsPane.getPreferredWidth(), s.getHeight());
				
				s.enableScrolling();
				
				if(s.current == 0)
				{
					s.scrollLeftButton.disable();
				}
				
				if(getMWidth() >= tabsPane.getPreferredWidth() - current	)
				{
					s.scrollRightButton.disable();
				}
			}
			else
			{
				this.scrollToPosition(s.tabsPane.getPreferredWidth() - this.getMWidth());
				
				s.enableScrolling();
				
				if(s.current == 0)
				{
					s.scrollLeftButton.disable();
				}
				
				if(getMWidth() >= tabsPane.getPreferredWidth() - current	)
				{
					s.scrollRightButton.disable();
				}				
			}
		}
		
		s.utilityBox.setBounds(this.getWidth()- utilityBoxWidth, 0, utilityBoxWidth, this.getHeight());
		
		s.closeButton.setBounds(this.getWidth()- 19, 5, 15, 15);
		s.scrollRightButton.setBounds(this.getWidth()- 35, 5, 15, 15);
		s.scrollLeftButton.setBounds(this.getWidth() - 50, 5, 15, 15);
		
		
		s.validate();		
	}
}

class MDIClippedTabsPaneComponentListener extends ComponentAdapter
{
	public void componentResized(ComponentEvent e)
	{
		MDIClippedTabsPane s = (MDIClippedTabsPane) e.getSource();
		
		s.repositionAndResize();
	
	}
}