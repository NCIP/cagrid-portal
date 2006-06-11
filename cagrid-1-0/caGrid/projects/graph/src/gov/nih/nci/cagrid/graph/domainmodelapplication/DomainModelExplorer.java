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
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class DomainModelExplorer extends JLayeredPane
{
	public DomainModel model;
	public Vector packages;
	
	public DomainModelOutline domainModelOutline;
	
	public DomainModelOutlines outlineMDI;
	public DomainModelUMLViews umlMDI;
	public JPanel              splitter = new JPanel();
	
	public Point splitterLastClicked;
	public boolean splitterMoving = false;
	
	public static int preferredOutlinesWidth = 300;
	
	public boolean hideOutlines = false;
	
	public boolean resizedOnce = false;
	
	public HashMap cache = new HashMap();
	

	
	
	public DomainModelExplorer()
	{
			domainModelOutline = new DomainModelOutline(this);
		
			outlineMDI = new DomainModelOutlines(this);
			umlMDI = new DomainModelUMLViews(this);
			
			
					
			this.add(outlineMDI);
			this.add(umlMDI);
			this.add(splitter);
			
			this.setLayer(splitter, JLayeredPane.MODAL_LAYER.intValue());
			
				
			this.splitter.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
	
			
			this.addComponentListener(new DomainModelExplorerComponentListener());
			this.splitter.addMouseListener(new DomainModelSplitterMouseListener());
			this.splitter.addMouseMotionListener(new DomainModelSplitterMouseMotionListener());
		
			setDomainModel(null);
	
	}
	
	public void showPage(DomainModelTreeNode node, String pageId)
	{
		if(cache.containsValue(pageId))
		{
			if(node.type == DomainModelTreeNode.CLASS)
			{
				UMLDiagram diagram = (UMLDiagram) this.umlMDI.getPageById(node.name);
				
				if(diagram != null)
				{
					diagram.scrollToShowClass(node.name);
				}
			}
			
			this.umlMDI.setActivePageByID(pageId);
		}
		else
		{
			if(node.type == DomainModelTreeNode.CLASS)
			{
				UMLDiagram d = new UMLDiagram();
				initializeUMLDiagram(d, model, node.name);
				d.scrollToShowClass(node.name);
				this.umlMDI.addPage(d, null, node.name, node.name);
			
			}
			else if(node.type == DomainModelTreeNode.PACKAGE)
			{
				UMLDiagram d = new UMLDiagram();
				initializeUMLDiagram(d, model, node.name);
				this.umlMDI.addPage(d, null, node.name, node.name);
				
			}
			else if(node.type == DomainModelTreeNode.DOMAIN)
			{
				DomainModelOverview o = new DomainModelOverview(model);
				this.umlMDI.addPage(o, null, node.name, node.name);
			
			}
		}
	}
	
	
	public void initializeUMLDiagram(UMLDiagram d, DomainModel m, String name)
	{
		d.clear();
		
		// add classes 
		for(int k = 0 ; k < this.model.getExposedUMLClassCollection().getUMLClass().length; k++)
		{
			gov.nih.nci.cagrid.metadata.common.UMLClass c =  model.getExposedUMLClassCollection().getUMLClass()[k];
			
			if(c.getPackageName().equals(name))
			{
				if(c != null)
				{
					gov.nih.nci.cagrid.graph.uml.UMLClass C = new gov.nih.nci.cagrid.graph.uml.UMLClass(c.getClassName());
					
					d.addClass(C);
				}
			}
		}
		
		// add assocs
		
		d.refresh();
	}
	
	
	public void setDomainModel(DomainModel model)
	{
		clear();
		
		if(model != null)
		{
			initModel(model);
			this.domainModelOutline.setDomainModel(this.model, this.packages);
		}
	}
	
	
	
	public void initModel(DomainModel model)
	{
		this.model = model;
		packages = new Vector();
		
		// uses O(n^2) algorithm to make a list of classes in each package in the
		// domain model... could be left for improvement later.
		
		for(int k = 0; k < model.getExposedUMLClassCollection().getUMLClass().length; k++)
		{
			gov.nih.nci.cagrid.metadata.common.UMLClass c =  model.getExposedUMLClassCollection().getUMLClass()[k];
			
			if(c != null)
			{
				String packageName = c.getPackageName();
				String className = c.getClassName();
				
				int found = -1;
				
				for(int j = 0; j < packages.size() && found != -1; j++)
				{
					MultiMapElement e = (MultiMapElement) packages.get(j);
					
					if(packageName.equals(e.head))
					{
						found = j;
					}
				}
				
				if(found == -1)
				{
					MultiMapElement e = new MultiMapElement();
					e.head = packageName;
					e.list.add(className);
					
					packages.add(e);
				}
				else
				{
					MultiMapElement e = (MultiMapElement) packages.get(found);
					e.list.add(className);
					packages.remove(found);
					packages.add(found, e);
				}
			}
		}
		
		
		// 'packages' is now a "multimap" of package-class[] elements;
		
		
		
	}
	
	
	
	public void clear()
	{
		this.model = null;
		this.packages = null;
		
		this.outlineMDI.clear();
		this.umlMDI.clear();
		
		this.domainModelOutline.setDomainModel(model, packages);
		
		
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
			if(!this.resizedOnce)
			{
				this.outlineMDI.setBounds(0, 0, DomainModelExplorer.preferredOutlinesWidth, this.getHeight());
				this.splitter.setBounds(DomainModelExplorer.preferredOutlinesWidth, 0, 3, this.getHeight());
				this.umlMDI.setBounds(DomainModelExplorer.preferredOutlinesWidth + this.splitter.getWidth(), 0, this.getWidth() - DomainModelExplorer.preferredOutlinesWidth - this.splitter.getWidth(), this.getHeight());
				this.resizedOnce = true;
			}
			else
			{
				this.outlineMDI.setBounds(0, 0, outlineMDI.getWidth(), this.getHeight());
				this.splitter.setBounds(outlineMDI.getWidth(), 0, 3, this.getHeight());
				this.umlMDI.setBounds(outlineMDI.getWidth() + this.splitter.getWidth(), 0, this.getWidth() - outlineMDI.getWidth() - this.splitter.getWidth(), this.getHeight());			
			}
		}
	}
	
	protected void requestSplitterMove(int x)
	{
		// if *PROJECTED/anticipated* move violates bounds THEN disallow move, otherwise grant
		//splitter.setBackground(Color.gray);
		if(splitter.getLocation().getX()+x-splitterLastClicked.x >= 200)
		{
			splitter.setLocation(splitter.getX() + x  - splitterLastClicked.x + 1, splitter.getY() );		
		}
		else
		{
			splitter.setLocation(200, splitter.getY());
		}
	}
	
	protected void splitterMoved()
	{
		outlineMDI.setSize(splitter.getX(), outlineMDI.getHeight());
		umlMDI.setBounds(splitter.getX() + splitter.getWidth(), 0, getWidth() - outlineMDI.getWidth() - splitter.getWidth(), getHeight());		
			
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
		parent.splitterMoving = true;
		
	}
	
	public void mouseReleased(MouseEvent e)
	{
		JPanel splitter = (JPanel)	e.getSource();
		DomainModelExplorer parent = (DomainModelExplorer) splitter.getParent();
		
		parent.splitterMoved();
		//parent.splitter.setBackground(Color.lightGray);
	}
}

class DomainModelSplitterMouseMotionListener extends MouseMotionAdapter
{
	public void mouseDragged(MouseEvent e)
	{
		JPanel splitter = (JPanel)	e.getSource();
		DomainModelExplorer parent = (DomainModelExplorer) splitter.getParent();
		
		parent.requestSplitterMove(e.getX());
		
	
	}
}