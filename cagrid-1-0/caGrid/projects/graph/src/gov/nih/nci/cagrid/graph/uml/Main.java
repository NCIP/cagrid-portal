package gov.nih.nci.cagrid.graph.uml;

import java.util.Random;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class Main {

	public static void main(String args[]) {

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
		} catch (IllegalAccessException ex) {
		} catch (InstantiationException ex) {
		} catch (ClassNotFoundException ex) {
		}

		UMLDiagram diagram = new UMLDiagram();

		Random r = new Random(System.currentTimeMillis());

		XMLClass c1 = new XMLClass("TestClass1");
		c1.addAttribute(1, "int", "count");
		c1.refresh();

		XMLClass c2 = new XMLClass("TestClass2");
		c2.refresh();

		XMLClass c3 = new XMLClass("TestClass3");
		c3.refresh();

		diagram.addClass(c1);
		diagram.addClass(c2);
		diagram.addClass(c3);

		diagram.addAssociation(c1, c2, "c1c2", "c1c2", "1..0", "1..*");
		diagram.addAssociation(c1, c3, "c1c3", "c1c3", "1..0", "1..*");
		diagram.addAssociation(c3, c2, "c3c2", "c3c2", "1..0", "1..*");
		// diagram.addAssociation(c1, c1 , "c1c1", "c1c1", "1..0", "1..*");
		// diagram.addAssociation(c2, c2 , "c2c2", "c2c2", "1..0", "1..*");
		// diagram.addAssociation(c3, c3 , "c3c3", "c3c3", "1..0", "1..*");

		diagram.refresh();

		diagram.placeRoute();
		diagram.repositionLabelsAndArrowHeads();

		JFrame f = new JFrame();
		f.getContentPane().add(diagram);
		f.setBounds(10, 100, 900, 600);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);

	}
}
