/****************************************/
/*   ICaptionButton.java                */
/*                                      */
/*   Copyright (c) Forhad Ahmed 2006    */
/*                                      */
/****************************************/

package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

public class ICaptionButton extends JComponent
{
    protected ICaptionBar parent;
    protected boolean     isPressed = false;

    public ICaptionButton(ICaptionBar parent)
    {
        super();

        this.parent = parent;

        this.addMouseListener(new ICaptionButtonMouseListener());
    }

    public void paint(Graphics g)
    {
        g.setColor(getCurrentParentColor());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());



    }

    protected Color getCurrentParentColor()
    {
        if (this.parent.hasFocus) {
            return (this.parent.focusColor);
        } else {
            return (this.parent.noFocusColor);
        }
    }

    protected void drawBordersForMouseEnter()
    {
        Graphics g = this.getGraphics();

        if(!this.isPressed)
        {
            g.setColor(Color.white);
            g.drawLine(0, 0, 0, this.getHeight() - 1);
            g.drawLine(0, 0, this.getWidth() - 1, 0);

            g.setColor(Color.black);
            g.drawLine(0, this.getHeight() - 1, this.getWidth() - 1,
                       this.getHeight() - 1);
            g.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1,
                       this.getHeight() - 1);
        }
        else
        {
            g.setColor(Color.black);
            g.drawLine(0, 0, 0, this.getHeight() - 1);
            g.drawLine(0, 0, this.getWidth() - 1, 0);

            g.setColor(Color.white);
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
        g.copyArea(1, 1, this.getWidth() , this.getHeight() , 1, 1);

        g.setColor(Color.black);
        g.drawLine(0, 0, 0, this.getHeight()-1);
        g.drawLine(0, 0, this.getWidth()-1, 0);

        g.setColor(Color.white);
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


class ICaptionButtonMouseListener extends MouseAdapter
{
    public void mouseEntered(MouseEvent e)
    {
        ICaptionButton s = (ICaptionButton) e.getSource();

        s.drawBordersForMouseEnter();
    }

    public void mouseExited(MouseEvent e)
    {
        ICaptionButton s = (ICaptionButton) e.getSource();

        s.drawBordersForMouseExit();

    }

    public void mousePressed(MouseEvent e)
    {
        ICaptionButton s = (ICaptionButton) e.getSource();

        s.drawBordersForMousePress();

    }

    public void mouseReleased(MouseEvent e)
    {
        ICaptionButton s = (ICaptionButton) e.getSource();

        s.drawBordersForMouseRelease();

    }


}
