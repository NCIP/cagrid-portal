/****************************************/
/*   IRootPane.java                     */
/*                                      */
/*   Copyright (c) Forhad Ahmed 2006    */
/*                                      */
/****************************************/

package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class IRootPane extends JComponent
{

    protected Color       color = new Color(240,240,240);

    protected ICaptionBar captionBar;
    protected int         captionBarHeight = 17;
    protected IBorder     border;
    protected int         borderWidth = 1;

    protected IContentPane contentPane;
    protected int          contentInset = 0;

    protected boolean     resizeOnDrag = false;
    protected int         resizeDirection;
    protected static final int RESIZE_RIGHT = 10;
    protected static final int RESIZE_LEFT = 20;
    protected static final int RESIZE_UP = 30;
    protected static final int RESIZE_DOWN = 40;

    protected boolean      mousePressed = false;
    protected boolean      isResizing = false;


    public IRootPane()
    {
        super();


        this.captionBar = new ICaptionBar();
        this.border = new IBorder(this);
        this.border.setWidth(borderWidth);

        this.contentPane = new IContentPane();


        this.add(captionBar);
        this.add(contentPane);

        this.addComponentListener(new IRootPaneComponentListener());
        this.addMouseListener(new IRootPaneMouseListener());
        this.addMouseMotionListener(new IRootPaneMouseMotionListener());


    }

    public IInternalFrame getIFrame()
    {
        IInternalFrame parent = (IInternalFrame) (this.getParent().getParent().getParent());

        return parent;
    }

    public void fireResized()
    {
        this.setSize(this.getWidth() - 1, this.getHeight());
        this.setSize(this.getWidth() + 1, this.getHeight());
        this.validate();
    }





    public void paint(Graphics g)
    {
        g.setColor(this.color);
        g.fillRect(0,0,this.getWidth(), this.getHeight());

        this.border.paint(g);

        super.paint(g);

    }

    public void resizeChildren()
    {
        this.captionBar.setBounds(this.borderWidth, this.borderWidth, this.getWidth() - 2 * this.borderWidth, this.captionBarHeight);
        this.contentPane.setBounds(this.borderWidth + this.contentInset, this.borderWidth + this.captionBarHeight + this.contentInset, this.getWidth() - 2 * this.borderWidth - 2 * this.contentInset , this.getHeight() - this.captionBarHeight - 2 * this.borderWidth - 2*this.contentInset);
    }

    protected void setResizeDirection(int r)
    {
        this.resizeDirection = r;
    }
}

class IRootPaneComponentListener extends ComponentAdapter
{
    public void componentResized(ComponentEvent e)
    {
        IRootPane s = (IRootPane) e.getSource();

        if(!s.isResizing)
        {
            s.resizeChildren();
        }

        s.validate();
    }
}


class IRootPaneMouseListener extends MouseAdapter
{
    public void mousePressed(MouseEvent e)
    {
        IRootPane s = (IRootPane) e.getSource();
        IInternalFrame p = s.getIFrame();

        s.mousePressed = true;

     }

    public void mouseReleased(MouseEvent e)
    {
        IRootPane s = (IRootPane) e.getSource();
        IInternalFrame p = s.getIFrame();

        s.mousePressed = false;

        s.isResizing = false;

        p.setIgnoreRepaint(false);

        s.resizeChildren();

        p.fireResized();


    }


    public void mouseEntered(MouseEvent e)
    {

    }

    public void mouseExited(MouseEvent e)
    {
        IRootPane s = (IRootPane) e.getSource();

        if(!s.mousePressed)
        {
            s.setCursor(Cursor.getDefaultCursor());
        }
    }
}
