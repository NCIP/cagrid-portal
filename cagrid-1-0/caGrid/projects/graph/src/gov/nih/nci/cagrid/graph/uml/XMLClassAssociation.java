package gov.nih.nci.cagrid.graph.uml;


import gov.nih.nci.cagrid.graph.geometry.LineSegment;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;

import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.FigEdgePoly;

public class XMLClassAssociation extends FigEdgePoly
{
     public Text sourceLabel;
     public Text destinationLabel;
     public Text sourceMultiplicity;
     public Text destinationMultiplicity;

     public XMLClassAssociationArrowHead sourceArrow;
     public XMLClassAssociationArrowHead destinationArrow;

     // arrays to store label placement metrics
     protected float labelToLabelOverlaps[] = new float[10];
     protected float labelToEdgeOverlaps[] = new float[10];
     protected float labelToAnchorDistance[] = new float[10];
     protected float fitnessFunction[] = new float[10];

     protected static Font highlightFont = new Font("verdana", Font.BOLD, 13);
     protected static Font unhighlightFont = new Font("verdana", Font.PLAIN, 11);


     public XMLClassAssociation(String sl, String sm, String dl, String dm)
     {
          super();
          this.sourceLabel = new Text(this.getX(), this.getY(), sl, this);
          this.sourceMultiplicity = new Text(this.getX(), this.getY(), sm, this);
          this.destinationLabel = new Text(this.getX(), this.getY(), dl, this);
          this.destinationMultiplicity = new Text(this.getX(), this.getY(), dm, this);

          this.sourceArrow = new XMLClassAssociationArrowHead();
          this.destinationArrow = new XMLClassAssociationArrowHead();

     }

     public void highlight(Layer layer)
     {
          this.setLineColor(Color.red);

          this.sourceArrow.highlight();
          this.destinationArrow.highlight();

          this.sourceMultiplicity.setTextColor(Color.black);
          this.sourceMultiplicity.setFont(highlightFont);
          layer.bringToFront(this.sourceMultiplicity);

          this.destinationMultiplicity.setTextColor(Color.black);
          this.destinationMultiplicity.setFont(highlightFont);
          layer.bringToFront(this.destinationMultiplicity);

          this.sourceLabel.setTextColor(Color.black);
          this.sourceLabel.setFont(highlightFont);
          layer.bringToFront(this.sourceLabel);

          this.destinationLabel.setTextColor(Color.black);
          this.destinationLabel.setFont(highlightFont);
          layer.bringToFront(this.destinationLabel);

          layer.bringToFront(this);

          //layer.bringToFront(this.getSourceFigNode());
          //layer.bringToFront(this.getDestFigNode());
          layer.bringToFront(this.getSourcePortFig());
          layer.bringToFront(this.getDestPortFig());

     }

     public void fade()
     {
          this.setLineColor(Color.lightGray);

          this.sourceArrow.fade();
          this.destinationArrow.fade();

          this.sourceMultiplicity.setTextColor(Color.lightGray);
          this.sourceMultiplicity.setFont(unhighlightFont);

          this.destinationMultiplicity.setTextColor(Color.lightGray);
          this.destinationMultiplicity.setFont(unhighlightFont);

          this.sourceLabel.setTextColor(Color.lightGray);
          this.sourceLabel.setFont(unhighlightFont);

          this.destinationLabel.setTextColor(Color.lightGray);
          this.destinationLabel.setFont(unhighlightFont);
     }

     public void setNormal()
     {
          this.setLineColor(Color.black);

          this.sourceArrow.setNormal();
          this.destinationArrow.setNormal();

          this.sourceMultiplicity.setTextColor(Color.black);
          this.sourceMultiplicity.setFont(unhighlightFont);

          this.destinationMultiplicity.setTextColor(Color.black);
          this.destinationMultiplicity.setFont(unhighlightFont);

          this.sourceLabel.setTextColor(Color.black);
          this.sourceLabel.setFont(unhighlightFont);

          this.destinationLabel.setTextColor(Color.black);
          this.destinationLabel.setFont(unhighlightFont);

     }

     public void repositionLabelsAndArrowHeads()
     {
          XMLClass source = (XMLClass) this._sourceFigNode;
          XMLClass destination = (XMLClass) this._destFigNode;

          LineSegment s_top = new LineSegment(source.getX(), source.getY(), source.getX() + source.getWidth() , source.getY());
          LineSegment s_left= new LineSegment(source.getX(), source.getY(), source.getX(), source.getY() + source.getHeight());
          LineSegment s_rite= new LineSegment(source.getX() + source.getWidth(), source.getY(), source.getX() + source.getWidth(), source.getY()+source.getHeight());
          LineSegment s_bot = new LineSegment(source.getX(), source.getY() + source.getHeight(), source.getX() + source.getWidth(), source.getHeight() + source.getY());

          LineSegment d_top = new LineSegment(destination.getX(), destination.getY(), destination.getX() + destination.getWidth() , destination.getY());
          LineSegment d_left= new LineSegment(destination.getX(), destination.getY(), destination.getX(), destination.getY() + destination.getHeight());
          LineSegment d_rite= new LineSegment(destination.getX() + destination.getWidth(), destination.getY(), destination.getX() + destination.getWidth(), destination.getY()+destination.getHeight());
          LineSegment d_bot = new LineSegment(destination.getX(), destination.getY() + destination.getHeight(), destination.getX() + destination.getWidth(), destination.getHeight() + destination.getY());

          LineSegment s_edge= new LineSegment(this.getPoint(0).x, this.getPoint(0).y, this.getPoint(1).x, this.getPoint(1).y);
          LineSegment d_edge= new LineSegment(this.getPoint(this.getPoints().length-1).x, this.getPoint(this.getPoints().length-1).y, this.getPoint(this.getPoints().length-2).x, this.getPoint(this.getPoints().length-2).y);



          if(s_top.getIntersection(s_edge)!= null)
          {
               this.sourceMultiplicity.setLocation(s_top.getIntersection(s_edge).getPoint());
               this.sourceMultiplicity.setLocation(this.sourceMultiplicity.getX(), this.sourceMultiplicity.getY() - this.sourceMultiplicity.getHeight() - 5);

               Point away = null;

               if(s_edge.p1.y <= s_top.getIntersection(s_edge).y) away = s_edge.p1;
               else away = s_edge.p2;

               this.sourceArrow.setDirection(away,   s_top.getIntersection(s_edge).getPoint());
               this.sourceArrow.redraw();


          }
          else if(s_left.getIntersection(s_edge) != null)
          {
               this.sourceMultiplicity.setLocation(s_left.getIntersection(s_edge).getPoint());
               Point center = source.getCenter();
               this.sourceMultiplicity.setLocation(this.sourceMultiplicity.getX() - this.sourceMultiplicity.getWidth() - 5, this.sourceMultiplicity.getY());

               Point away = null;
               if(s_edge.p1.x <= s_left.getIntersection(s_edge).x) away = s_edge.p1;
               else away = s_edge.p2;

               this.sourceArrow.setDirection( away ,  s_left.getIntersection(s_edge).getPoint());
               this.sourceArrow.redraw();



          }
          else if(s_rite.getIntersection(s_edge) != null)
          {
               this.sourceMultiplicity.setLocation(s_rite.getIntersection(s_edge).getPoint());
               Point center = source.getCenter();
               this.sourceMultiplicity.setLocation(this.sourceMultiplicity.getX() + 5, this.sourceMultiplicity.getY());

               Point away = null;
               if(s_edge.p1.x >= s_rite.getIntersection(s_edge).x) away = s_edge.p1;
               else away = s_edge.p2;

               this.sourceArrow.setDirection( away,  s_rite.getIntersection(s_edge).getPoint());
               this.sourceArrow.redraw();





          }
          else if(s_bot.getIntersection(s_edge) != null)
          {
               this.sourceMultiplicity.setLocation(s_bot.getIntersection(s_edge).getPoint());
               Point center = source.getCenter();
               this.sourceMultiplicity.setLocation(this.sourceMultiplicity.getX(), this.sourceMultiplicity.getY() + 5);

               Point away = null;
               if(s_edge.p1.y >= s_bot.getIntersection(s_edge).x) away = s_edge.p1;
               else away = s_edge.p2;

               this.sourceArrow.setDirection( away,  s_bot.getIntersection(s_edge).getPoint());
               this.sourceArrow.redraw();
          }
          else
          {
               // no intersection



          }


          if(d_top.getIntersection(d_edge)!= null)
          {
               this.destinationMultiplicity.setLocation(d_top.getIntersection(d_edge).getPoint());
               this.destinationMultiplicity.setLocation(this.destinationMultiplicity.getX(), this.destinationMultiplicity.getY() - this.destinationMultiplicity.getHeight() - 5);

               Point away = null;

               if(d_edge.p1.y <= d_top.getIntersection(d_edge).y) away = d_edge.p1;
               else away = d_edge.p2;

               this.destinationArrow.setDirection(away,   d_top.getIntersection(d_edge).getPoint());
               this.destinationArrow.redraw();


          }
          else if(d_left.getIntersection(d_edge) != null)
          {
               this.destinationMultiplicity.setLocation(d_left.getIntersection(d_edge).getPoint());
               Point center = source.getCenter();
               this.destinationMultiplicity.setLocation(this.destinationMultiplicity.getX() - this.destinationMultiplicity.getWidth() - 5, this.destinationMultiplicity.getY());

               Point away = null;
               if(d_edge.p1.x <= d_left.getIntersection(d_edge).x) away = d_edge.p1;
               else away = d_edge.p2;

               this.destinationArrow.setDirection( away ,  d_left.getIntersection(d_edge).getPoint());
               this.destinationArrow.redraw();



          }
          else if(d_rite.getIntersection(d_edge) != null)
          {
               this.destinationMultiplicity.setLocation(d_rite.getIntersection(d_edge).getPoint());
               Point center = source.getCenter();
               this.destinationMultiplicity.setLocation(this.destinationMultiplicity.getX() + 5, this.destinationMultiplicity.getY());

               Point away = null;
               if(d_edge.p1.x >= d_rite.getIntersection(d_edge).x) away = d_edge.p1;
               else away = d_edge.p2;

               this.destinationArrow.setDirection( away,  d_rite.getIntersection(d_edge).getPoint());
               this.destinationArrow.redraw();





          }
          else if(d_bot.getIntersection(d_edge) != null)
          {
               this.destinationMultiplicity.setLocation(d_bot.getIntersection(d_edge).getPoint());
               Point center = source.getCenter();
               this.destinationMultiplicity.setLocation(this.destinationMultiplicity.getX(), this.destinationMultiplicity.getY() + 5);

               Point away = null;
               if(d_edge.p1.y >= d_bot.getIntersection(d_edge).x) away = d_edge.p1;
               else away = d_edge.p2;

               this.destinationArrow.setDirection( away,  d_bot.getIntersection(d_edge).getPoint());
               this.destinationArrow.redraw();
          }
          else
          {
               // no intersection



          }

      }

      public float intersectsRectangle(Rectangle r)
      {
           float rval = 0;

           LineSegment temp = new LineSegment(0, 0, 1, 1);

           for(int c = 0; c < this.getPoints().length-1; c++)
           {
                temp.p1 = this.getPoint(c);
                temp.p2 = this.getPoint(c+1);

                rval += temp.cutsRectangle(r);
           }

           return rval;
      }
}
