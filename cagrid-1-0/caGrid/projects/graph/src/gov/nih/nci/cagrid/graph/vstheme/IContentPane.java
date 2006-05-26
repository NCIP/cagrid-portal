/****************************************/
/*   IContentPane.java                  */
/*                                      */
/*   Copyright (c) Forhad Ahmed 2006    */
/*                                      */
/****************************************/

package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class IContentPane extends JComponent
{
    protected JComponent component;

    public IContentPane()
    {
        super();
        this.setLayout(new BorderLayout());

        this.addComponentListener(new IContentPaneComponentListener());
        this.addMouseListener(new IContentPaneMouseListener());
    }

    public void setComponent(JComponent c)
    {
        this.add(c, 0);
        this.component = c;
    }

}

class IContentPaneComponentListener extends ComponentAdapter
{
    public void componentResized(ComponentEvent e)
    {
        IContentPane s = (IContentPane) e.getSource();

        s.validate();
    }
}

class IContentPaneMouseListener extends MouseAdapter
{
    public void mouseEntered(MouseEvent e)
    {
        IContentPane s = (IContentPane) e.getSource();

        s.setCursor(Cursor.getDefaultCursor());
    }


}
