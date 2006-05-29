package gov.nih.nci.cagrid.graph.uml;

import gov.nih.nci.cagrid.graph.domainmodelapplication.DomainModelOutlinePanel;

import java.util.Random;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

// Test Driver for the UMLDiagram class

public class Main {

	public static void main(String args[]) throws InterruptedException {

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
		} catch (IllegalAccessException ex) {
		} catch (InstantiationException ex) {
		} catch (ClassNotFoundException ex) {
		}
		
		
		
		

		UMLDiagram diagram = new UMLDiagram();

		Random r = new Random(System.currentTimeMillis());

		int numclasses = 200;
		int numassocs = 90;
		
		for(int k = 0; k < numclasses;  k++)
		{
			UMLClass c = new UMLClass("TestClass"+k);
			c.refresh();
			
			diagram.addClass(c);
		}
		
		for(int k = 0; k < numassocs; k++)
		{
			int index1 = r.nextInt() % numclasses;
			int index2 = r.nextInt() % numclasses;
			
			if(index1 < 0) index1 = -index1;
			if(index2 < 0) index2 = -index2;
			
			UMLClass c1 = (UMLClass) diagram.classes.get(index1);
			UMLClass c2 = (UMLClass) diagram.classes.get(index2);
			
			diagram.addAssociation(c1, c2, "", "", "1..0", "*..1");
		}
		
		diagram.performLayout();
		diagram.refresh();
		diagram.repositionLabelsAndArrowHeads();
		

		
		
		
		DomainModelOutlinePanel p = new DomainModelOutlinePanel();
		


		JFrame f = new JFrame();
		f.getContentPane().add(p);
		f.setBounds(10, 100, 900, 600);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setUndecorated(true);
		
		f.setVisible(true);
		

	
	}
}
