package gov.nih.nci.cagrid.graph.vstheme;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;

public class MDIPanel extends JComponent
{
	public MDIClippedTabsPane tabs;
	public MultipleComponentContainer components;
	
	

}

class MDIPanelComponentListener extends ComponentAdapter
{
	public void componentResized(ComponentEvent e)
	{
		MDIPanel s = (MDIPanel)e.getSource();
		s.tabs.setBounds(0, 0, s.getWidth(), 25);
		s.components.setBounds(0, 25, s.getWidth(), s.getHeight()-25);
	}
}