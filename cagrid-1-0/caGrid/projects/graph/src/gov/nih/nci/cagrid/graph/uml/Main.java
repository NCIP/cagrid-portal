package gov.nih.nci.cagrid.graph.uml;

import gov.nih.nci.cagrid.graph.domainmodelapplication.DomainModelExplorer;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.globus.wsrf.encoding.DeserializationException;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.xml.sax.InputSource;

// Test Driver for the UMLDiagram class

public class Main {

	public static void main(String args[]) throws InterruptedException, FileNotFoundException, DeserializationException {

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
		} catch (IllegalAccessException ex) {
		} catch (InstantiationException ex) {
		} catch (ClassNotFoundException ex) {
		}
		
		
		UMLDiagram d = new UMLDiagram();
		
	
		
		
		FileInputStream fis = new FileInputStream(new File(System.getProperty("user.dir") + "\\domainmodel.xml"));
		
		InputSource is = new InputSource(fis);
		
		DomainModel model = (DomainModel) ObjectDeserializer.deserialize(is, DomainModel.class);
		
		DomainModelExplorer e = new DomainModelExplorer();
		
		e.setDomainModel(model);
		
		
		
		JFrame f = new JFrame();
		f.getContentPane().add(e);
		f.setBounds(10, 100, 900, 600);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
		
	}
}
	
	