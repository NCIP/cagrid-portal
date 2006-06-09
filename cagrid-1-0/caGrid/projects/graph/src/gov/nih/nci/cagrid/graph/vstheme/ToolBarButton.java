package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class ToolBarButton extends JPanel
{
	public JComponent parent;
	public boolean isPressed;
	
	public ToolBarButton(JComponent parent)
	{
		this.parent = parent;
		this.addMouseListener(new ToolBarButtonMouseListener());
	}

    public void paint(Graphics g)
    {
        g.setColor(getCurrentParentColor());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());



    }

    protected Color getCurrentParentColor()
    {
    	return this.parent.getBackground();
    }

    protected void drawBordersForMouseEnter()
    {
        Graphics g = this.getGraphics();
        
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        

        if(!this.isPressed)
        {
            g.setColor(Color.lightGray);
            g.drawLine(0, 0, 0, this.getHeight() - 1);
            g.drawLine(0, 0, this.getWidth() - 1, 0);

            g.setColor(Color.lightGray);
            g.drawLine(0, this.getHeight() - 1, this.getWidth() - 1,
                       this.getHeight() - 1);
            g.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1,
                       this.getHeight() - 1);
        }
        else
        {
            g.setColor(Color.lightGray);
            g.drawLine(0, 0, 0, this.getHeight() - 1);
            g.drawLine(0, 0, this.getWidth() - 1, 0);

            g.setColor(Color.lightGray);
            g.drawLine(0, this.getHeight() - 1, this.getWidth() - 1,
                       this.getHeight() - 1);
            g.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1,
                       this.getHeight() - 1);

        }


    }

    protected void drawBordersForMouseExit()
    {
        Graphics g = this.getGraphics();
        
        
        g.setColor(getCurrentParentColor());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        g.setColor(this.getCurrentParentColor());
        g.drawLine(0, 0, 0, this.getHeight()-1);
        g.drawLine(0, 0, this.getWidth()-1, 0);

        g.setColor(this.getCurrentParentColor());
        g.drawLine(0, this.getHeight()-1, this.getWidth()-1, this.getHeight()-1);
        g.drawLine(this.getWidth()-1, 0, this.getWidth()-1, this.getHeight()-1);



    }

    protected void drawBordersForMousePress()
    {
        Graphics g = this.getGraphics();
        g.copyArea(1, 1, this.getWidth() , this.getHeight() , 1, 1);

        g.setColor(Color.lightGray);
        g.drawLine(0, 0, 0, this.getHeight()-1);
        g.drawLine(0, 0, this.getWidth()-1, 0);

        g.setColor(Color.lightGray);
        g.drawLine(0, this.getHeight()-1, this.getWidth()-1, this.getHeight()-1);
        g.drawLine(this.getWidth()-1, 0, this.getWidth()-1, this.getHeight()-1);

        this.isPressed = true;
    }

    protected void drawBordersForMouseRelease()
    {
        Graphics g = this.getGraphics();
        g.copyArea(1, 1, this.getWidth() , this.getHeight() , -1, -1);
        this.drawBordersForMouseExit();
        this.isPressed = false;
        this.repaint();
    }


	
	
}

class ToolBarButtonMouseListener extends MouseAdapter
{
    public void mouseEntered(MouseEvent e)
    {
        ToolBarButton s = (ToolBarButton) e.getSource();

        s.drawBordersForMouseEnter();
    }

    public void mouseExited(MouseEvent e)
    {
    	ToolBarButton s = (ToolBarButton) e.getSource();

        s.drawBordersForMouseExit();

    }

    public void mousePressed(MouseEvent e)
    {
    	ToolBarButton s = (ToolBarButton) e.getSource();

        s.drawBordersForMousePress();

    }

    public void mouseReleased(MouseEvent e)
    {
    	ToolBarButton s = (ToolBarButton) e.getSource();

        s.drawBordersForMouseRelease();

    }	
}
