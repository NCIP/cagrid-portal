package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MDICloseButton extends MDIUtilityButton
{
	protected int inset = 4;
	
	public MDICloseButton(MDIPanel parent)
	{
		super(parent);
		this.addMouseListener(new MDICloseButtonMouseListener());
		
	}
	
	public void paint(Graphics g)
	{
	       super.paint(g);

	       g.setColor(Color.gray);

	       g.drawLine(inset, inset, this.getWidth() - inset - 1, this.getHeight() - inset - 1);
	       g.drawLine(inset+1, inset, this.getWidth() - inset - 1 + 1, this.getHeight() - inset - 1);

	       g.drawLine(inset, this.getHeight() - inset - 1, this.getWidth() - inset - 1, inset);
	       g.drawLine(inset+1, this.getHeight() - inset - 1, this.getWidth() - inset - 1 + 1, inset);
	       
	}
	
	public void signalClose()
	{
		parent.removeCurrentPage();
	}
}

class MDICloseButtonMouseListener extends MouseAdapter
{
	public void mouseClicked(MouseEvent e)
	{
		MDICloseButton s = (MDICloseButton) e.getSource();
		
		s.signalClose();
	}
}