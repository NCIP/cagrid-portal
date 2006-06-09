

package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.*;
import java.awt.event.*;



import javax.swing.JComponent;

public class MDIUtilityButton extends JComponent
{
    protected MDIPanel parent;
    protected boolean     isPressed = false;
    public Color borderLightColor = Color.lightGray;
    public Color borderDarkColor = Color.gray;
    
    public boolean enabled = true;

    public MDIUtilityButton(MDIPanel parent)
    {
        super();

        this.parent = parent;

        this.addMouseListener(new MDIUtilityButtonMouseListener());
    }

    public void paint(Graphics g)
    {
        g.setColor(getCurrentParentColor());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());



    }
    
    public void disable()
    {
    	if(enabled)
    	{
	    	this.enabled= false;
	    	this.borderLightColor = getCurrentParentColor();
	    	this.borderDarkColor = getCurrentParentColor();
	    	
	    	this.repaint();
    	}
    }
    
    public void enable()
    {
    	if(!enabled)
    	{
	    	this.enabled = true;
	    	borderLightColor = Color.lightGray;
	        borderDarkColor = Color.gray;   
	        
	        this.repaint();
    	}
    }

    protected Color getCurrentParentColor()
    {
    	return parent.tabs.tabsPane.bgColor;
    }

    protected void drawBordersForMouseEnter()
    {
        Graphics g = this.getGraphics();

        if(!this.isPressed)
        {
            g.setColor(borderLightColor);
            g.drawLine(0, 0, 0, this.getHeight() - 1);
            g.drawLine(0, 0, this.getWidth() - 1, 0);

            g.setColor(borderDarkColor);
            g.drawLine(0, this.getHeight() - 1, this.getWidth() - 1,
                       this.getHeight() - 1);
            g.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1,
                       this.getHeight() - 1);
        }
        else
        {
        	g.setColor(borderDarkColor);
            g.drawLine(0, 0, 0, this.getHeight() - 1);
            g.drawLine(0, 0, this.getWidth() - 1, 0);

            g.setColor(borderLightColor);
            g.drawLine(0, this.getHeight() - 1, this.getWidth() - 1,
                       this.getHeight() - 1);
            g.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1,
                       this.getHeight() - 1);

        }


    }

    protected void drawBordersForMouseExit()
    {
        Graphics g = this.getGraphics();

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
        if(enabled) g.copyArea(1, 1, this.getWidth() , this.getHeight() , 1, 1);

        g.setColor(borderDarkColor);
        g.drawLine(0, 0, 0, this.getHeight()-1);
        g.drawLine(0, 0, this.getWidth()-1, 0);

        g.setColor(borderLightColor);
        g.drawLine(0, this.getHeight()-1, this.getWidth()-1, this.getHeight()-1);
        g.drawLine(this.getWidth()-1, 0, this.getWidth()-1, this.getHeight()-1);

        this.isPressed = true;
    }

    protected void drawBordersForMouseRelease()
    {
        Graphics g = this.getGraphics();
        if(enabled) g.copyArea(1, 1, this.getWidth() , this.getHeight() , -1, -1);
        this.drawBordersForMouseExit();
        this.isPressed = false;
        this.repaint();
    }



}


class MDIUtilityButtonMouseListener extends MouseAdapter
{
    public void mouseEntered(MouseEvent e)
    {
        MDIUtilityButton s = (MDIUtilityButton) e.getSource();

        s.drawBordersForMouseEnter();
    }

    public void mouseExited(MouseEvent e)
    {
        MDIUtilityButton s = (MDIUtilityButton) e.getSource();

        s.drawBordersForMouseExit();

    }

    public void mousePressed(MouseEvent e)
    {
        MDIUtilityButton s = (MDIUtilityButton) e.getSource();

        s.drawBordersForMousePress();

    }

    public void mouseReleased(MouseEvent e)
    {
        MDIUtilityButton s = (MDIUtilityButton) e.getSource();

        s.drawBordersForMouseRelease();

    }


}
