/****************************************/
/*   ICaptionMinimaxButton.java         */
/*                                      */
/*   Copyright (c) Forhad Ahmed 2006    */
/*                                      */
/****************************************/

package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class ICaptionMinimaxButton extends ICaptionButton
{
    protected int inset = 3;
    public ICaptionMinimaxButton(ICaptionBar parent)
    {
        super(parent);
        this.addMouseListener(new ICaptionMinimaxButtonMouseListener());
    }

    public void paint(Graphics g)
    {
        super.paint(g);

        g.setColor(Color.white);

        g.drawRect(inset, inset+1, this.getWidth() - 2*inset - 1, this.getHeight() - 2*inset - 2);


    }
}

class ICaptionMinimaxButtonMouseListener extends MouseAdapter
{
    public void mouseClicked(MouseEvent e)
    {
        ICaptionMinimaxButton s = (ICaptionMinimaxButton) e.getSource();

        //s.parent.signalParentFrameMaximize();
    }
}
