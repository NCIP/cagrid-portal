package gov.nih.nci.cagrid.graph.uml;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;

import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;

import uml.classdiagram.ClassdiagramAssociationEdge;
import uml.classdiagram.ClassdiagramLayouter;
import uml.classdiagram.ClassdiagramNode;


public class UMLDiagram extends JLayeredPane {
	protected Diagram diagram;
	public    UMLViewer viewer;
	protected UMLMenuBar menubar;
	protected UMLStatusBar statusBar;

	ClassdiagramLayouter layouter = new ClassdiagramLayouter();

	protected Vector classes = new Vector();
	protected Vector assocs = new Vector();
	
	public boolean inactiveState = true;

	public UMLViewer getViewer()
	{
		return this.viewer;
	}

	public UMLDiagram() 
	{
		super();

		diagram = new Diagram();
		viewer = new UMLViewer(this);

		viewer.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		viewer.setBorder(BorderFactory.createEmptyBorder());

		this.menubar = new UMLMenuBar();
		this.statusBar = new UMLStatusBar();

		this.add(this.viewer);
		this.add(menubar);
		this.add(statusBar);

		this.addComponentListener(new UMLDiagramComponentListener());
		
		this.setPreferredSize(new Dimension(500, 500));

	}


	public void setStatusMessage(String msg) {
		this.statusBar.setMsg(msg);
	}


	public boolean addClass(UMLClass gc) {

		if (classes.contains(gc))
			return false;

		gc.refresh();
		
		gc.setVisible(false);
		this.classes.addElement(gc);
		this.diagram.add(gc);
		this.layouter.add(new ClassdiagramNode(gc));
		

		return true;

	}


	public boolean highlightClass(UMLClass c) {

		return this.viewer.highlightClass(c);

	}


	public boolean addAssociation(UMLClass gc1, UMLClass gc2, String label1, String label2, String multiplicity1,
		String multiplicity2) {
		UMLClassAssociation edge = new UMLClassAssociation(label1, multiplicity1, label2, multiplicity2);

		edge.setSourceFigNode(gc1);
		edge.setSourcePortFig(gc1);

		edge.setDestFigNode(gc2);
		edge.setDestPortFig(gc2);

		edge.setVisible(false);
		
		this.diagram.add(edge);
		this.diagram.add(edge.sourceLabel);
		this.diagram.add(edge.destinationLabel);
		this.diagram.add(edge.sourceMultiplicity);
		this.diagram.add(edge.destinationMultiplicity);

		this.diagram.add(edge.sourceArrow);
		this.diagram.add(edge.destinationArrow);

		this.assocs.addElement(edge);

		this.layouter.add(new ClassdiagramAssociationEdge(edge));

		return true;
	}
	
	public void scrollToShowClass(String name)
	{
		//
	}


	public void addFig(Fig f) 
	{
		this.diagram.add(f);

	}


	public void classDoubleClicked(UMLClass c) {

	}


	public void zoom(int percent) 
	{

	}


	public void refresh() 
	{
		this.viewer.setDiagram(this.diagram);

		for (int k = 0; k < this.classes.size(); k++) {
			UMLClass gc = (UMLClass) this.classes.get(k);

		}
		
		
		performLayout();
		repositionLabelsAndArrowHeads();
		
		this.viewer.updateDrawingSizeToIncludeAllFigs();
		
		this.inactiveState = false;
		
		
	}
	
	public void clear()
	{
		this.inactiveState = true;
		this.layouter = new ClassdiagramLayouter();
		
		for(int k = 0; k < this.classes.size(); k++)
		{
			UMLClass gc = (UMLClass) this.classes.get(k);
			this.diagram.remove(gc);
		}
		
		for(int k = 0; k < this.assocs.size(); k++)
		{
			UMLClassAssociation edge = (UMLClassAssociation) this.assocs.get(k);
			this.diagram.remove(edge);
			this.diagram.remove(edge.sourceArrow);
			this.diagram.remove(edge.destinationArrow);
			this.diagram.remove(edge.sourceLabel);
			this.diagram.remove(edge.destinationLabel);
			this.diagram.remove(edge.sourceMultiplicity);
			this.diagram.remove(edge.destinationMultiplicity);
		}
		
		this.classes = new Vector();
		this.assocs = new Vector();
	}


	public void performLayout() {

		layouter.layout();
	
	}


	protected void repositionLabelsAndArrowHeads() {
		
		UMLClassAssociation edge = null;
	
		for (int c = 0; c < this.assocs.size(); c++) {
				edge = (UMLClassAssociation) this.assocs.elementAt(c);
	
				edge.repositionLabelsAndArrowHeads();
	
				this.repaint();
	
		}
		
	}

}

class UMLDiagramComponentListener extends ComponentAdapter {
	public void componentResized(ComponentEvent e) {
		UMLDiagram s = (UMLDiagram) e.getSource();

		// s.menubar.setBounds(0, 0, s.getWidth(), 25);
		// s.viewer.setBounds(0, 26, s.getWidth(), s.getHeight()-26-22);
		// s.statusBar.setBounds(0, s.getHeight()- 20, s.getWidth(), 20);
		// s.validate();

		s.viewer.setBounds(0, 0, s.getWidth(), s.getHeight());
		s.validate();
	}
}
