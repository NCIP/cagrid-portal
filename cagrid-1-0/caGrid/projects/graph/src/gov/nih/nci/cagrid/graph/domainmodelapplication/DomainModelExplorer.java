package gov.nih.nci.cagrid.graph.domainmodelapplication;



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

			outlineMDI = new DomainModelOutlines();
			umlMDI = new DomainModelUMLViews();
			
			
			this.add(outlineMDI);
			this.add(umlMDI);
			this.add(splitter);
			
			this.setLayer(splitter, JLayeredPane.MODAL_LAYER.intValue());
			
				
			this.splitter.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
	
			
			this.addComponentListener(new DomainModelExplorerComponentListener());
			this.splitter.addMouseListener(new DomainModelSplitterMouseListener());
			this.splitter.addMouseMotionListener(new DomainModelSplitterMouseMotionListener());
	
	
	}
	
	public void showPage(DomainModelTreeNode node, String pageId)
	{
		if(cache.containsKey(pageId))
		{
			this.umlMDI.setActivePageByID(pageId);
		}
		else
		{
			
		}
	}
	
	
	
	public void setDomainModel(DomainModel model)
	{
		clear();
		
		if(model != null)
		{
			initModel(model);
		}
	}
	
	public void initModel(DomainModel model)
	{
		this.model = model;
		
		Vector packages = new Vector();
		
		for(int k = 0; k < model.getExposedUMLClassCollection().getUMLClass().length; k++)
		{
			gov.nih.nci.cagrid.metadata.common.UMLClass c =  model.getExposedUMLClassCollection().getUMLClass()[k];
			
			if(c != null)
			{
				String packageName = c.getPackageName();
				String className = c.getClassName();
				
			}
			
		
		}
		
		
	}
	
	public void clear()
	{
		this.model = null;
		
		this.outlineMDI.clear();
		this.umlMDI.clear();
		
		
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
		
		if(splitter.getLocation().getX()+x-splitterLastClicked.x >= 100)
		{
			splitter.setLocation(splitter.getX() + x  - splitterLastClicked.x + 1, splitter.getY() );		
		}
		else
		{
			splitter.setLocation(100, splitter.getY());
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