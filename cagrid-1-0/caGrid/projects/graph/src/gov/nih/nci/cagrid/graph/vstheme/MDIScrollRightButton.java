package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

public class MDIScrollRightButton extends MDIUtilityButton
{
	public MDIScrollRightButton(MDIPanel parent)
	{
		super(parent);
		
		this.addMouseListener(new MDIScrollRightButtonMouseListener());
	}
	
	public void paint(Graphics g)
	{
	    super.paint(g);
		  
		g.setColor(Color.gray);
		
		if(!enabled)
		{
			g.drawLine(8, 7, 5, 4);
			g.drawLine(5, 4, 5, 10);
			g.drawLine(5, 10, 8, 7);
		}
		else
		{
			g.drawLine(8, 7, 5, 4);
			g.drawLine(5, 4, 5, 10);
			g.drawLine(5, 10, 8, 7);
			
			g.drawLine(7, 8, 7, 6);
			g.drawLine(6, 8, 6, 6);
		}
	}
}

class MDIScrollRightButtonMouseListener extends MouseAdapter
{
	public void mousePressed(MouseEvent e)
	{

		MDIScrollRightButton s = (MDIScrollRightButton) e.getSource();
		
		if(s.enabled)
		{
			s.parent.tabs.scrollHorizontally(25);
		}
		
		
	}
}
