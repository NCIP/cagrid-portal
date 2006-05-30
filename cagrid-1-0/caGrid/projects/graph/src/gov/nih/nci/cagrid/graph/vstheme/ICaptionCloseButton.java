/****************************************/
/*   ICaptionCloseButton.java           */
/*                                      */
/*   Copyright (c) Forhad Ahmed 2006    */
/*                                      */
/****************************************/

package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class ICaptionCloseButton extends ICaptionButton
{
    protected int inset = 3;
    public ICaptionCloseButton(ICaptionBar parent)
    {
        super(parent);
        this.addMouseListener(new ICaptionCloseButtonMouseListener());
    }

    public void paint(Graphics g)
    {
        super.paint(g);

        g.setColor(Color.white);

        g.drawLine(inset, inset, this.getWidth() - inset - 1, this.getHeight() - inset - 1);
        g.drawLine(inset, this.getHeight() - inset - 1, this.getWidth() - inset - 1, inset);

    }
}

class ICaptionCloseButtonMouseListener extends MouseAdapter
{
    public void mouseClicked(MouseEvent e)
    {
        ICaptionCloseButton s = (ICaptionCloseButton) e.getSource();
        ICaptionBar bar = (ICaptionBar) s.getParent();
        
        bar.signalClose();
    }
}
