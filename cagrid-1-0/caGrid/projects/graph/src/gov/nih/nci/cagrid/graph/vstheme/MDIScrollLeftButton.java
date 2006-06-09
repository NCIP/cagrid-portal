package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MDIScrollLeftButton extends MDIUtilityButton
{
	
	public MDIScrollLeftButton(MDIPanel parent)
	{
		super(parent);
		
		this.addMouseListener(new MDIScrollLeftButtonMouseListener());
	}
	
	public void paint(Graphics g)
	{
	    super.paint(g);
		  
		g.setColor(Color.gray);
		
		if(!enabled)
		{
			g.drawLine(5, 7, 8, 4);
			g.drawLine(8, 4, 8, 10);
			g.drawLine(8, 10, 5, 7);
		}
		else
		{
			g.drawLine(5, 7, 8, 4);
			g.drawLine(8, 4, 8, 10);
			g.drawLine(8, 10, 5, 7);
			
			g.drawLine(7, 9, 7, 5);
			g.drawLine(6, 8, 6, 6);
		}
	}
}

class MDIScrollLeftButtonMouseListener extends MouseAdapter
{
	public void mousePressed(MouseEvent e) 
	{
		MDIScrollLeftButton s = (MDIScrollLeftButton) e.getSource();
		
		if(s.enabled)
		{
			s.parent.tabs.scrollHorizontally(-25);
			
			
		}
		
		
		
		
	}
}