package gov.nih.nci.cagrid.graph.domainmodelapplication;

import gov.nih.nci.cagrid.graph.uml.UMLDiagram;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Application 
{
	public static void main(String args[]) 
	{
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
		} catch (IllegalAccessException ex) {
		} catch (InstantiationException ex) {
		} catch (ClassNotFoundException ex) {
		}
		
		
		DomainModelExplorer e = new DomainModelExplorer();
		UMLDiagram d = new UMLDiagram();
	
		JFrame f = new JFrame();
		f.getContentPane().add(d);
		f.setBounds(10, 100, 900, 600);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
	
		

		
	}

}
