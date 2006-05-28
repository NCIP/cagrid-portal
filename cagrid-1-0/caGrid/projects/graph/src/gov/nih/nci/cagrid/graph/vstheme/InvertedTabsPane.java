package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;



public class InvertedTabsPane extends JComponent
{
	public Vector tabs;
	public InvertedMDIPanel parentMDIPanel;
	
	public static Color bgColor = new Color(247, 243, 200);
	public static Color grayColor = new Color(212, 208, 200);
	public static int spacerHeight = 3;
	
	public void addTab(String s, ImageIcon icon)
	{
		IInvertedTab tab = new IInvertedTab(tabs.size(), icon , s, this, this.parentMDIPanel);
		this.tabs.add(tab);
		
	}
	
	public void setActiveTab(int i)
	{
		
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		g.setColor(bgColor);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(grayColor);
		g.fillRect(0, 0, this.getWidth(), spacerHeight);
		
		g.setColor(Color.black);
		g.drawLine(0, spacerHeight, this.getWidth(), spacerHeight);
		
	}
	
}

class InvertedTabsPaneComponentListener extends ComponentAdapter
{
	public void componentResized(ComponentEvent e)
	{
		InvertedTabsPane s = (InvertedTabsPane) e.getSource();
		
		for(int k = 0; k < s.tabs.size(); k++)
		{
			
		}
	}
}
 