package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
	public static Color inactiveColor = new Color(247, 243, 233);
	public static Font  font = new Font("verdana", Font.PLAIN , 11);

	
	
	boolean active = false;
	
	public IInvertedTab(int i, ImageIcon icon, String title, InvertedTabsPane parentTabsPane)
	{
		this.index = i;
		this.parentTabsPane = parentTabsPane;
		this.icon = icon;
		this.title = title;
		
		this.deactivate();
		
		this.addMouseListener(new IInvertedTabMouseListener());
		
	}
	
	public int getPreferredWidth()
	{
		Graphics g = this.getGraphics();
		
		if(g != null)
		{
		
			FontMetrics fm = g.getFontMetrics();
			return fm.stringWidth(this.title) + 20;
		}
		
		return 100;
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
		//g.setFont(font);
		
		if(this.active)
		{
			g.setColor(activeColor);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setColor(Color.white);
			g.drawLine(0, 0,0, this.getHeight());
			g.setColor(Color.black);
			g.drawLine(0, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() -1 );
			g.drawLine(this.getWidth()-1, 0, this.getWidth()-1, this.getHeight()-1);
			g.setColor(Color.black);
			g.drawString(this.title, 10, 15);
			
		}
		else
		{
			g.setColor(inactiveColor);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setColor(Color.gray);
			g.drawLine(this.getWidth()-1, 5, this.getWidth() - 1, this.getHeight() - 3);
			g.setColor(Color.black);
			g.drawString(this.title , 10, 15);
		}
	
	}

}

class IInvertedTabMouseListener extends MouseAdapter
{
	public void mousePressed(MouseEvent e)
	{
		
		IInvertedTab tab = (IInvertedTab) e.getSource();
		
		tab.parentTabsPane.tabClicked(tab.index);
		
	
	}
}