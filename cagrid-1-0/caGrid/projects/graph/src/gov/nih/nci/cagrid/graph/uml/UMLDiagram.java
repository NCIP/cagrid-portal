package gov.nih.nci.cagrid.graph.uml;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;

import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;

import uml.classdiagram.ClassdiagramAssociationEdge;
import uml.classdiagram.ClassdiagramLayouter;
import uml.classdiagram.ClassdiagramNode;

public class UMLDiagram extends JComponent
{
     protected Diagram   diagram;
     protected UMLViewer viewer;
     protected UMLMenuBar   menubar;
     protected UMLStatusBar statusBar;

     ClassdiagramLayouter layouter = new ClassdiagramLayouter();

     protected Vector classes = new Vector();
     protected Vector assocs  = new Vector();


     public UMLDiagram()
     {
          super();

          diagram = new Diagram();
          viewer = new UMLViewer(this);


          //viewer.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
          viewer.setBorder(BorderFactory.createLoweredBevelBorder());

          this.menubar = new UMLMenuBar();
          this.statusBar = new UMLStatusBar();



          this.add(this.viewer);
          this.add(menubar);
          this.add(statusBar);

          this.addComponentListener(new UMLDiagramComponentListener());


     }

     public void addClass(XMLClass gc)
     {

          this.diagram.add(gc);
          this.classes.addElement(gc);
          this.layouter.add(new ClassdiagramNode((FigNode) gc));

     }

     public void addAssociation(XMLClass gc1, XMLClass gc2, String label1, String label2, String multiplicity1, String multiplicity2)
     {
          XMLClassAssociation edge = new XMLClassAssociation( label1, multiplicity1, label2, multiplicity2 );

          edge.setSourceFigNode(gc1);
          edge.setSourcePortFig(gc1);

          edge.setDestFigNode(gc2);
          edge.setDestPortFig(gc2);

          this.diagram.add(edge);

          this.diagram.add(edge.sourceLabel);
          this.diagram.add(edge.destinationLabel);
          this.diagram.add(edge.sourceMultiplicity);
          this.diagram.add(edge.destinationMultiplicity);

          this.diagram.add(edge.sourceArrow);
          this.diagram.add(edge.destinationArrow);

          this.assocs.addElement(edge);

          this.layouter.add(new ClassdiagramAssociationEdge(edge));

     }

     public void addFig(Fig f)
     {
          this.diagram.add(f);

     }

     public void classDoubleClicked(XMLClass c)
     {


     }




     public void zoom(int percent)
     {

     }

     public void refresh()
     {
          this.viewer.setDiagram(this.diagram);

          for(int k = 0; k < this.classes.size(); k++)
          {
               this.diagram.getLayer().bringToFront((XMLClass)this.classes.elementAt(k));
          }

     }

     public void placeRoute()
     {


              layouter.layout();


     }

     protected void repositionLabelsAndArrowHeads()
     {
          XMLClassAssociation edge = null;

          for(int c = 0; c < this.assocs.size(); c++)
          {
               edge = (XMLClassAssociation) this.assocs.elementAt(c);

               edge.repositionLabelsAndArrowHeads();

               this.repaint();

          }

     }

     class PagerButton extends JButton implements MouseListener
     {
          protected UMLDiagram diagram;

          protected boolean _pressed = true;

          public PagerButton(UMLDiagram d)
          {
               this.diagram = d;
               this.addMouseListener(this);
               this.setBackground(Color.white);
               //this.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
               this.setFocusable(false);



          }

          public void paint(Graphics g)
          {
               super.paint(g);

               // draw the arrow and the light/dark colors

               if(!_pressed)
               {
                    g.setColor(Color.gray);
                    g.fillRect(1, 1, this.getWidth() - 2, this.getHeight() - 2);

                    g.setColor(Color.black);
                    g.drawLine(4, 4, 10, 10);
                    g.drawLine(10, 10, 10, 4);
                    g.drawLine(10, 10, 4, 10);

               }
               else {
                    // draw the little magnifying glass

                    g.drawLine(9, 9, 12, 12);
                    g.drawLine(10, 9, 12, 11);
                    g.drawLine(9, 10, 11, 12);
                    g.drawArc(3, 3, 6, 6, 0, 360);

               }


          }

          public void mousePressed(MouseEvent e)
          {

               if(e.getButton() == e.BUTTON1 || e.getButton() == e.BUTTON2 || e.getButton() == e.BUTTON3)
               {

                    if (!_pressed) {
                         _pressed = true;
                         this.diagram.viewer.pager.updateScroller();
                         this.diagram.viewer.pager.setVisible(false);

                    }
                    else {
                         JLayeredPane parent = (JLayeredPane) diagram.getParent().getParent();

                         // this is a bad hack but it works... must find underlying problem later
                         this.diagram.viewer.setSize(this.diagram.viewer.getWidth() - 1, this.diagram.viewer.getHeight());
                         this.diagram.viewer.setSize(this.diagram.viewer.getWidth() + 1, this.diagram.viewer.getHeight());
                         // end of bad hack

                         this.diagram.viewer.pager.setBounds(parent.getWidth() - 200 - this.getWidth() - 5,
                                                             parent.getHeight() - 200 - this.getHeight() - 5, 200, 200);

                         if (parent.getComponentCount() == 1) {
                              parent.add(this.diagram.viewer.pager, JLayeredPane.POPUP_LAYER);
                         }
                         this.diagram.viewer.pager.updateScroller();
                         this.diagram.viewer.pager.setVisible(true);
                         _pressed = false;
                    }
               }

          }



          public void mouseReleased(MouseEvent e)
          {

          }

          public void mouseEntered(MouseEvent e)
          {

          }

          public void mouseExited(MouseEvent e)
          {

          }

          public void mouseClicked(MouseEvent e)
          {

          }

     }
}

class UMLDiagramComponentListener extends ComponentAdapter
{
     public void componentResized(ComponentEvent e)
     {
          UMLDiagram s = (UMLDiagram) e.getSource();

          s.menubar.setBounds(0, 0, s.getWidth(), 25);
          s.viewer.setBounds(0, 26, s.getWidth(), s.getHeight()-26-20);
          s.statusBar.setBounds(0, s.getHeight()-19, s.getWidth(), 20);
          s.validate();
     }
}
