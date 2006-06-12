/****************************************/
/*   IBorder.java                       */
/*                                      */
/*   Copyright (c) Forhad Ahmed 2006    */
/*                                      */
/****************************************/

package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComponent;


public class IBorder
{
    public static final int RAISED = 0;
    public static final int LOWERED = 1;
    public static final int PLAIN = 2;

    protected int   borderStyle = 0;
    protected Color lightColor = Color.lightGray;
    protected Color darkColor = Color.gray;
   protected int   width = 5;

    protected Component host;

    public IBorder(JComponent host)
    {
        this.host = host;
    }

    public IBorder()
    {

    }

    public IBorder( int width)
    {
        this.width = width;
    }


    public IBorder(int style, int width)
    {
        this.borderStyle = style;
        this.width = width;
    }

    public IBorder(Component host) throws NullPointerException
    {
        if(host != null)
        {
            this.host = host;
        }
        else
        {
            throw new NullPointerException("The 'host' component for the border is null");
        }
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getWidth()
    {
        return this.width;
    }



    public void paint(Graphics g)
    {
        if (this.host != null) {
            if (borderStyle == PLAIN) {
                g.setColor(lightColor);
               for (int c = 0; c < this.width; c++) {
                   g.drawLine(c, c, c, host.getHeight() - c);
                   g.drawLine(c, c, host.getWidth() - c, c);
               }

               g.setColor(lightColor);
               for (int c = 0; c < this.width; c++) {
                   g.drawLine(c, host.getHeight() - c - 1, host.getWidth() - c - 1,
                              host.getHeight() - c - 1);
                   g.drawLine(host.getWidth() - c - 1, c, host.getWidth() - c - 1,
                              host.getHeight() - c - 1);
               }

            } else if (borderStyle == LOWERED) {
                g.setColor(darkColor);
                for (int c = 0; c < this.width; c++) {
                    g.drawLine(c, c, c, host.getHeight() - c);
                    g.drawLine(c, c, host.getWidth() - c, c);
                }

                g.setColor(lightColor);
                for (int c = 0; c < this.width; c++) {
                    g.drawLine(c, host.getHeight() - c - 1, host.getWidth() - c - 1,
                               host.getHeight() - c - 1);
                    g.drawLine(host.getWidth() - c - 1, c, host.getWidth() - c - 1,
                               host.getHeight() - c - 1);
                }
            } else if (borderStyle == RAISED) {
                g.setColor(lightColor);
                for (int c = 0; c < this.width; c++) {
                    g.drawLine(c, c, c, host.getHeight() - c);
                    g.drawLine(c, c, host.getWidth() - c, c);
                }

                g.setColor(darkColor);
                for (int c = 0; c < this.width; c++) {
                    g.drawLine(c, host.getHeight() - c - 1, host.getWidth() - c - 1,
                               host.getHeight() - c - 1);
                    g.drawLine(host.getWidth() - c - 1, c, host.getWidth() - c - 1,
                               host.getHeight() - c - 1);
                }
            }
        }
    }


}
