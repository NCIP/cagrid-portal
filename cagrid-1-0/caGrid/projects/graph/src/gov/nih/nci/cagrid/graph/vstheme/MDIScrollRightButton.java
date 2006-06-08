package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class MDIScrollRightButton extends MDIUtilityButton
{
	public MDIScrollRightButton(MDIPanel parent)
	{
		super(parent);
	}
	
	public void paint(Graphics g)
	{
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
