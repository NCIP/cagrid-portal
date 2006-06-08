package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.JScrollBar;



public class MDITabsPane extends JLayeredPane
{
	public Vector tabs = new Vector();
	public MDIClippedTabsPane parent;
	public int currentActiveTab = -1;
	
	public static Color bgColor = new Color(247, 243, 233);
	public static Color grayColor = new Color(212, 208, 200);
	public static int spacerHeight = 4;
	public static int tabHeight = 21;
	
	
	
	public MDITabsPane(MDIClippedTabsPane parent)
	{
		this.parent = parent;
	}
	
	public void addTab(String s, ImageIcon icon)
	{
		
		MDITab tab = new MDITab(tabs.size(), icon , s, this);
		this.tabs.add(tab);
		this.add(tab);
		this.setActiveTab(tabs.size());
		
		this.addComponentListener(new MDITabsPaneComponentListener());
		
	}
	
	public void removeTab(int i)
	{
		if(i >= 0 && i < this.tabs.size())
		{
			MDITab tab = (MDITab) this.tabs.get(i);
			this.tabs.remove(i);
			this.remove(tab);
			
			resyncTabIndices();
			this.resizeTabs();
			this.repaint();
			deactivateAllTabs();
			//setActiveTab(0);
			
			
		}
	}
	
	protected void deactivateAllTabs()
	{
		for(int k = 0;  k < this.tabs.size(); k ++)
		{
			MDITab t = (MDITab)this.tabs.get(k);
			t.active = false;
			t.repaint();
		}
		
		this.currentActiveTab = -1;
		
	}
	
	public void resyncTabIndices()
	{	
		for(int k = 0; k < this.tabs.size(); k++)
		{
			
			MDITab tab = (MDITab) this.tabs.get(k);
			
			tab.index = k;
			
		}
	}
	public void setActiveTab(int i)
	{
		if(currentActiveTab >= 0 && currentActiveTab < this.tabs.size())
		{
			MDITab tab = (MDITab) this.tabs.get(currentActiveTab);
			this.setLayer(tab, JLayeredPane.DEFAULT_LAYER.intValue());
			tab.deactivate();
			
			currentActiveTab = -1;
		}
		
		if(i < this.tabs.size() && i >= 0)
		{
			MDITab tab = (MDITab) this.tabs.get(i);
			
			this.setLayer(tab, JLayeredPane.MODAL_LAYER.intValue());
			tab.activate();
			
			currentActiveTab = i;
		}
		
		resizeTabs();
	
	}
	
	public void tabClicked(int i)
	{
		this.parent.tabClicked(i);
	}
	
	public int getTotalTabsPreferredWidth()
	{
		int sum = 0;
		
		for (int k = 0; k < this.tabs.size(); k++)
		{
			MDITab tab = (MDITab) this.tabs.get(k);
			
			sum += tab.getPreferredWidth();
		}
		
		return sum;
	}
	
	public void resizeTabs()
	{
		int lastX = 5;
		
		{	
			for(int k = 0; k < tabs.size(); k++)
			{
				MDITab tab = (MDITab) tabs.get(k);
				int width = tab.getPreferredWidth();
				
				if(tab.active)
				{
					tab.setBounds(lastX-1, this.getHeight()-spacerHeight- tabHeight+1, width+1, tabHeight);
				}
				else
				{
					tab.setBounds(lastX, this.getHeight()-spacerHeight - tabHeight, width, tabHeight);
				}
				
				lastX += width;
			}			
		}
		
		validate();		
	}
	
	public void paint(Graphics g)
	{
		
		
		g.setColor(bgColor);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(grayColor);
		g.fillRect(0, this.getHeight() - spacerHeight , this.getWidth(), spacerHeight);

		
		g.setColor(Color.white);
		g.drawLine(0, this.getHeight() - spacerHeight, this.getWidth(), this.getHeight() - spacerHeight);
		
		super.paint(g);
	}
	
	public int getPreferredWidth()
	{
		int sum  = 0;
		for(int k = 0; k < this.tabs.size(); k++)
		{
			MDITab t = (MDITab) this.tabs.get(k);
			
			sum += t.getPreferredWidth();
		}
		
		return sum + 10;
	}
	
}

class MDITabsPaneComponentListener extends ComponentAdapter
{
	public void componentResized(ComponentEvent e)
	{
		MDITabsPane s = (MDITabsPane) e.getSource();
		
		s.resizeTabs();

	}
}
 