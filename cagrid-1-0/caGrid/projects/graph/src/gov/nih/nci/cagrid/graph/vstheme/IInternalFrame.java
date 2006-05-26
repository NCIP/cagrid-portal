/****************************************/
/*   IInternalFrame.java                */
/*                                      */
/*   Copyright (c) Forhad Ahmed 2006    */
/*                                      */
/****************************************/

package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;



public class IInternalFrame extends JComponent
{

    protected IRootPane   rootPane;



    public IInternalFrame()
    {
        super();
        init("");
    }

    public IInternalFrame(String title)
    {
        super();
        init(title);
    }

    protected void init(String title)
    {


        this.rootPane = new IRootPane();

        this.add(this.rootPane);

        this.addComponentListener(new IFrameComponentListener());
        this.addMouseMotionListener(new IFrameMouseMotionListener());

        this.setSize(600, 500);

        this.validate();
        this.setTitle(title);
        this.setVisible(true);

    }




    public void setComponent(JComponent c)
    {
        c.setDoubleBuffered(true);
        c.setBorder(BorderFactory.createEmptyBorder());
        this.rootPane.contentPane.setComponent(c);
        this.validate();

    }

    public JComponent getComponent()
    {
        return this.rootPane.contentPane.component;
    }


    public void setContentInset(int n)
    {
        this.rootPane.contentInset = n;
        this.rootPane.fireResized();
        this.validate();

    }

    public int getContentInset()
    {
        return this.rootPane.contentInset;

    }




    public void setTitle(String title)
    {
        this.rootPane.captionBar.setTitle(title);
    }


    public String getTitle()
    {
        return this.rootPane.captionBar.getTitle();
    }

    public void setCaptionBarHeight(int h)
    {
        this.rootPane.captionBarHeight = h;
    }

    public void setCaptionBarFocusColor(Color c)
    {

    }

    public Color getCaptionBarFocusColor()
    {
        return this.rootPane.captionBar.getFocusColor();
    }

    public void setCaptionBarNoFocusColor(Color c)
    {

    }

    public Color getCaptionBarNoFocusColor()
    {
        return this.rootPane.captionBar.getNoFocusColor();
    }


    public void setRootPaneColor(Color c)
    {
        this.rootPane.color = c;
    }

    public void setBorder(IBorder b)
    {
        this.rootPane.border = b;
        this.rootPane.border.host = this;
        this.rootPane.borderWidth = b.getWidth();
        this.rootPane.fireResized();
    }





    public void fireResized()
    {
        this.setSize(this.getWidth() - 1, this.getHeight());
        this.setSize(this.getWidth() + 1, this.getHeight());
        this.validate();
    }


    public void paint(Graphics g)
    {

        super.paint(g);

    }

}


class IFrameComponentListener extends ComponentAdapter
{
    public void componentResized(ComponentEvent e)
    {
        IInternalFrame s = (IInternalFrame) e.getSource();

        s.rootPane.setBounds(0, 0, s.getWidth(), s.getHeight());

    }

}


class IFrameMouseMotionListener extends MouseMotionAdapter
{
    public void mouseMoved(MouseEvent e)
    {
         IInternalFrame s = (IInternalFrame) e.getSource();
    }

    public void mouseDragged(MouseEvent e)
    {
         IInternalFrame s = (IInternalFrame) e.getSource();
    }
}
