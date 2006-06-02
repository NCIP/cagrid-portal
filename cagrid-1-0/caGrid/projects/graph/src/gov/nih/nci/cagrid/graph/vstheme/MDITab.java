package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class MDITab extends JComponent
{
	public int index;
	public ImageIcon icon;
	public String title;
	public MDITabsPane parentTabsPane;
	public MDIPanel parentMDIPanel;
	
	public static Color activeColor = new Color(212, 208, 200);
	public static Color inactiveColor = new Color(247, 243, 233);
	public static Font  font = new Font("tahoma", Font.PLAIN , 11);
	public static Font  bfont= new Font("tahoma", Font.BOLD, 11);
	
	
	boolean active = false;
	
	public MDITab(int i, ImageIcon icon, String title, MDITabsPane parentTabsPane)
	{
		
		this.index = i;
		this.parentTabsPane = parentTabsPane;
		this.icon = icon;
		this.title = title;
		
		this.deactivate();
		
		this.addMouseListener(new MDITabMouseListener());
		
		this.setFont(font);
		
	}
	
	public int getPreferredWidth()
	{
		Graphics g = this.getGraphics();
		
		if(g != null)
		{
		
			if(this.icon == null)
			{
				FontMetrics fm = g.getFontMetrics();
				return fm.stringWidth(this.title) + 20;
			}
			else
			{
				FontMetrics fm = g.getFontMetrics();
				return fm.stringWidth(this.title) + 20 + this.icon.getIconWidth();
			}
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
		this.setFont(bfont);
		this.repaint();
	}
	
	public void deactivate()
	{
		this.active = false;
		this.setFont(font);
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
			g.drawLine(0, 0,this.getWidth(), 0);
			
			g.setColor(Color.black);
			g.drawLine(this.getWidth(), 0, this.getWidth(), this.getHeight());
			g.setColor(Color.black);

			
		}
		else
		{
			g.setColor(inactiveColor);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setColor(Color.gray);
			g.drawLine(this.getWidth()-1, 5, this.getWidth() - 1, this.getHeight() - 3);
			g.setColor(Color.black);

			
		}
		
		if(this.icon != null)
		{
			g.drawImage(this.icon.getImage(), 1, 1, 20, 20, this);
			g.drawString(this.title, 22, 15);
		}
		else
		{
			g.drawString(this.title, 10, 15);
		}
	
	}

}

class MDITabMouseListener extends MouseAdapter
{
	public void mousePressed(MouseEvent e)
	{
		
		MDITab tab = (MDITab) e.getSource();
		
		tab.parentTabsPane.tabClicked(tab.index);
		
	
	}
}