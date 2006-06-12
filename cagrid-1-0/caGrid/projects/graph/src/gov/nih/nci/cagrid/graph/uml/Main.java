package gov.nih.nci.cagrid.graph.uml;

import gov.nih.nci.cagrid.graph.domainmodelapplication.DomainModelExplorer;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

// Test Driver for the UMLDiagram class

public class Main {

	public static void main(String args[]) {

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
		} catch (IllegalAccessException ex) {
		} catch (InstantiationException ex) {
		} catch (ClassNotFoundException ex) {
		}
		
		
		UMLDiagram d = new UMLDiagram();
		
	
		
		

		DomainModelExplorer e = new DomainModelExplorer();
		

		
		
		
		JFrame f = new JFrame();
		f.getContentPane().add(e);
		f.setBounds(10, 100, 900, 600);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
		
	}
}
	
	