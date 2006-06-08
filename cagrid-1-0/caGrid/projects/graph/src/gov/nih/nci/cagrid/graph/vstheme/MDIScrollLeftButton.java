package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.Color;
import java.awt.Graphics;

public class MDIScrollLeftButton extends MDIUtilityButton
{
	
	public MDIScrollLeftButton(MDIPanel parent)
	{
		super(parent);
	}
	
	public void paint(Graphics g)
	{
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
