package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class IInvertedTab extends JComponent
{
	public int index;
	public ImageIcon icon;
	public String title;
	public InvertedTabsPane parentTabsPane;
	public InvertedMDIPanel parentMDIPanel;
	
	public static Color activeColor = new Color(212, 208, 200);
	public static Color inactiveColor = new Color(247, 243, 200);
	public static Font  font = new Font("verdana"	, 12, Font.PLAIN);

	
	
	boolean active = false;
	
	public IInvertedTab(int i, ImageIcon icon, String title, InvertedTabsPane parentTabsPane, InvertedMDIPanel parentMDIPanel)
	{
		this.index = i;
		this.parentTabsPane = parentTabsPane;
		this.parentMDIPanel = parentMDIPanel;
		this.icon = icon;
		this.title = title;
		
		this.deactivate();
	}
	
	public void setIcon(ImageIcon icon)
	{
		this.icon = icon;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	
	public void activate()
	{
		this.active = true;
		this.repaint();
	}
	
	public void deactivate()
	{
		this.active = false;
		this.repaint();
	}
	
	public void paint(Graphics g)
	{
		g.setFont(font);
		
		if(this.active)
		{
			g.setColor(activeColor);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setColor(Color.white);
			g.drawLine(0, 0,0, this.getHeight());
			g.setColor(Color.black);
			g.drawLine(0, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() -1 );
			g.drawLine(this.getWidth()-1, 0, this.getWidth()-1, this.getHeight()-1);
			
		}
		else
		{
			g.setColor(inactiveColor);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setColor(Color.gray);
			g.drawLine(this.getWidth()-1, 2, this.getWidth() - 1, this.getHeight() - 3);
		}
		
		g.drawString(this.title, 10, 10);
		
	}

}
