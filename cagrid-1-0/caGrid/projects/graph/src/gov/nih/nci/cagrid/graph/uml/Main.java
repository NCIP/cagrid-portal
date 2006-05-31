package gov.nih.nci.cagrid.graph.uml;

import gov.nih.nci.cagrid.graph.domainmodelapplication.DomainModelOutlines;

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

	
		DomainModelOutlines p = new DomainModelOutlines();
		
		JFrame f = new JFrame();
		f.getContentPane().add(diagram);
		f.setBounds(10, 100, 900, 600);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
		populateDiagram(diagram);
		diagram.refresh();
		Thread.sleep(2000);
		diagram.clear();
		populateDiagram(diagram);
		diagram.refresh();
		Thread.sleep(2000);
		diagram.clear();
		populateDiagram(diagram);
		diagram.refresh();		
		
		
		

	
	}
	
	
	public static void populateDiagram(UMLDiagram diagram) throws InterruptedException
	{
		Random r = new Random(System.currentTimeMillis());

		int numclasses = 30;
		int numassocs = 9;
		
		for(int k = 0; k < numclasses;  k++)
		{
			UMLClass c = new UMLClass("TestClass"+k);
			Thread.sleep(100);
			
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
			Thread.sleep(100);
			diagram.addAssociation(c1, c2, "", "", "1..0", "*..1");
		}
	}
}
