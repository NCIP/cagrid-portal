package gov.nih.nci.cagrid.graph.domainmodelapplication;



import gov.nih.nci.cagrid.graph.uml.UMLDiagram;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class DomainModelExplorer extends JComponent
{
	public DomainModel model;
	
	public DomainModelOutlines outlineMDI;
	public DomainModelUMLViews umlMDI;
	public JPanel              splitter = new JPanel();
	
	public Point splitterLastClicked;
	
	public static int preferredOutlinesWidth = 300;
	
	public boolean hideOutlines = false;
	
	public DomainModelExplorer()
	{
		this(null);
	}
	
	public DomainModelExplorer(DomainModel model)
	{

			outlineMDI = new DomainModelOutlines(model);
			umlMDI = new DomainModelUMLViews();
			
			
			this.add(outlineMDI);
			this.add(umlMDI);
			this.add(splitter);
			
			UMLDiagram d = new UMLDiagram();
			d.getViewer().setDrawingSize(1000,1000);
			umlMDI.addPage(d, null, "HI", "HI");
			
			this.splitter.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
	
			
			this.addComponentListener(new DomainModelExplorerComponentListener());
			this.splitter.addMouseListener(new DomainModelSplitterMouseListener());
			this.splitter.addMouseMotionListener(new DomainModelSplitterMouseMotionListener());
	
	
	}
	
	public void selectPackage()
	{
		
	}
	
	
	
	public void selectClass()
	{
		
	}
	
	public void selectDomainModel()
	{
		
	}
	
	protected void resizeComponents()
	{
		if(this.hideOutlines)
		{
			this.umlMDI.setBounds(0, 0, this.getWidth(), this.getHeight());
		}
		else
		{
			this.outlineMDI.setBounds(0, 0, DomainModelExplorer.preferredOutlinesWidth, this.getHeight());
			this.splitter.setBounds(DomainModelExplorer.preferredOutlinesWidth, 0, 3, this.getHeight());
			this.umlMDI.setBounds(DomainModelExplorer.preferredOutlinesWidth + this.splitter.getWidth(), 0, this.getWidth() - DomainModelExplorer.preferredOutlinesWidth - this.splitter.getWidth(), this.getHeight());
		}
	}
}

class DomainModelExplorerComponentListener extends ComponentAdapter
{
	public void componentResized(ComponentEvent e)
	{
		DomainModelExplorer s = (DomainModelExplorer) e.getSource();
		s.resizeComponents();
	}
}

class DomainModelSplitterMouseListener extends MouseAdapter
{
	public void mousePressed(MouseEvent e)
	{
		JPanel splitter = (JPanel)	e.getSource();
		DomainModelExplorer parent = (DomainModelExplorer) splitter.getParent();
		parent.splitterLastClicked = e.getPoint();
	}
}

class DomainModelSplitterMouseMotionListener extends MouseMotionAdapter
{
	public void mouseDragged(MouseEvent e)
	{
		JPanel splitter = (JPanel)	e.getSource();
		DomainModelExplorer parent = (DomainModelExplorer) splitter.getParent();
		
		splitter.setLocation(splitter.getX() + e.getX() - parent.splitterLastClicked.x, splitter.getY() );
		parent.outlineMDI.setSize(splitter.getX(), parent.outlineMDI.getHeight());
		parent.umlMDI.setBounds(splitter.getX() + splitter.getWidth(), 0, parent.getWidth() - parent.outlineMDI.getWidth() - splitter.getWidth(), parent.getHeight());
	}
}