package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;

public class MultipleComponentContainer extends JLayeredPane
{
	public Vector components = new Vector();
	public int currentShownComponent = -1;
	
	public MultipleComponentContainer()
	{
		this.setLayout(new BorderLayout());
	}
	
	public void addComponent(JComponent component)
	{
		this.add(component);
		this.components.add(component);
	}
	
	public void removeComponent(int i)
	{
		if(i < this.components.size() && i >= 0)
		{
			JComponent c = (JComponent) this.components.get(i);
			
			this.remove(c);
			this.components.remove(i);
		
		}
		
	}
	
	public void showComponent(int i)
	{
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
		
	}
}
