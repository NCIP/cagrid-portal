package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;

public class MultipleComponentContainer extends JLayeredPane
{
	public Vector components = new Vector();
	public int currentShownComponent = -1;
	
	public MultipleComponentContainer()
	{
		//this.setLayout(new BorderLayout());
		this.addComponentListener(new MultipleComponentContainerComponentListener());
	}
	
	public void addComponent(JComponent component)
	{
		this.add(component);
		this.components.add(component);
		
		this.validate();
		this.repaint();
	}
	
	public void removeComponent(int i)
	{
		if(i < this.components.size() && i >= 0)
		{
			JComponent c = (JComponent) this.components.get(i);
			
			this.remove(c);
			this.components.remove(i);

			if(i == currentShownComponent)
			{
				currentShownComponent = -1;
			}
			
			this.validate();
			this.repaint();
		}
		
	}
	
	public void showComponent(int i)
	{
		System.out.println(currentShownComponent);
		
		if(currentShownComponent >=0 && currentShownComponent < components.size())
		{
			JComponent c = (JComponent) this.components.get(currentShownComponent);
			this.setLayer(c, JLayeredPane.DEFAULT_LAYER.intValue());
			currentShownComponent = -1;
			

		
		}
		if(i >= 0 && i < this.components.size())
		{
			JComponent c = (JComponent) this.components.get(i);
			this.setLayer(c, JLayeredPane.MODAL_LAYER.intValue());
			currentShownComponent = i;	

		}
	
		this.validate();
		this.repaint();
		
	}
}

class MultipleComponentContainerComponentListener extends ComponentAdapter
{
	public void componentResized(ComponentEvent e)
	{
		MultipleComponentContainer s = (MultipleComponentContainer) e.getSource();
		
		for(int k = 0; k < s.components.size(); k++)
		{
			JComponent c = (JComponent) s.components.get(k);
			
			c.setBounds(0, 0, s.getWidth(), s.getHeight());
		}
	}
}