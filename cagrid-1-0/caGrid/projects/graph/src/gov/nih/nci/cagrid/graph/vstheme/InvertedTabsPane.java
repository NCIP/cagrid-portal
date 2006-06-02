package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;



public class InvertedTabsPane extends JLayeredPane
{
	public Vector tabs = new Vector();
	public InvertedMDIPanel parentMDIPanel;
	public int currentActiveTab = -1;
	
	public static Color bgColor = new Color(247, 243, 233);
	public static Color grayColor = new Color(212, 208, 200);
	public static int spacerHeight = 3;
	public static int tabHeight = 21;
	
	
	public InvertedTabsPane()
	{
		this.addComponentListener(new InvertedTabsPaneComponentListener());
		
		
	}
	
	public InvertedTabsPane(InvertedMDIPanel parent)
	{
		this.parentMDIPanel = parent;
		this.addComponentListener(new InvertedTabsPaneComponentListener());
		
	}
	
	public void addTab(String s, ImageIcon icon)
	{
		
		InvertedMDITab tab = new InvertedMDITab(tabs.size(), icon , s, this);
		this.tabs.add(tab);
		this.add(tab);
		this.setActiveTab(tabs.size());
		
		
	}
	
	public void removeTab(int i)
	{
		if(i >= 0 && i < this.tabs.size())
		{
			InvertedMDITab tab = (InvertedMDITab) this.tabs.get(i);
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
			InvertedMDITab t = (InvertedMDITab)this.tabs.get(k);
			t.active = false;
			t.repaint();
		}
		
		this.currentActiveTab = -1;
		
	}
	
	public void resyncTabIndices()
	{
		for(int k = 0; k < this.tabs.size(); k++)
		{
			InvertedMDITab tab = (InvertedMDITab) this.tabs.get(k);
			
			tab.index = k;
		}
	}
	public void setActiveTab(int i)
	{
		if(currentActiveTab >= 0 && currentActiveTab < this.tabs.size())
		{
			InvertedMDITab tab = (InvertedMDITab) this.tabs.get(currentActiveTab);
			this.setLayer(tab, JLayeredPane.DEFAULT_LAYER.intValue());
			tab.deactivate();
			
			currentActiveTab = -1;
		}
		
		if(i < this.tabs.size() && i >= 0)
		{
			InvertedMDITab tab = (InvertedMDITab) this.tabs.get(i);
			
			this.setLayer(tab, JLayeredPane.MODAL_LAYER.intValue());
			tab.activate();
			
			currentActiveTab = i;
		}
		
		resizeTabs();
	
	}
	
	public void tabClicked(int i)
	{
		this.parentMDIPanel.setActivePage(i);
	}
	
	public int getTotalTabsPreferredWidth()
	{
		int sum = 0;
		
		for (int k = 0; k < this.tabs.size(); k++)
		{
			InvertedMDITab tab = (InvertedMDITab) this.tabs.get(k);
			
			sum += tab.getPreferredWidth();
		}
		
		return sum;
	}
	
	public void resizeTabs()
	{
		int lastX = 4;
		
		if(getTotalTabsPreferredWidth() >= getWidth())
		{
			int width = getWidth() / tabs.size();
			width--;
			
			for(int k = 0; k < tabs.size(); k++)
			{
				
				InvertedMDITab tab = (InvertedMDITab) tabs.get(k);
				
				if(tab.active)
				{
					tab.setBounds(lastX-1, InvertedTabsPane.spacerHeight, width+1, InvertedTabsPane.tabHeight);
				}
				else
				{
					tab.setBounds(lastX, InvertedTabsPane.spacerHeight+1, width, InvertedTabsPane.tabHeight);
				}
				
				lastX += width;
			}
		}
		else
		{	
			for(int k = 0; k < tabs.size(); k++)
			{
				InvertedMDITab tab = (InvertedMDITab) tabs.get(k);
				int width = tab.getPreferredWidth();
				
				if(tab.active)
				{
					tab.setBounds(lastX-1, InvertedTabsPane.spacerHeight, width+1, InvertedTabsPane.tabHeight);
				}
				else
				{
					tab.setBounds(lastX, InvertedTabsPane.spacerHeight+1, width, InvertedTabsPane.tabHeight);
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
		g.fillRect(0, 0, this.getWidth(), spacerHeight);
		
		g.setColor(Color.black);
		g.drawLine(0, spacerHeight, this.getWidth(), spacerHeight);
		
		super.paint(g);
	}
	
}

class InvertedTabsPaneComponentListener extends ComponentAdapter
{
	public void componentResized(ComponentEvent e)
	{
		InvertedTabsPane s = (InvertedTabsPane) e.getSource();
		
		s.resizeTabs();

	}
}
 