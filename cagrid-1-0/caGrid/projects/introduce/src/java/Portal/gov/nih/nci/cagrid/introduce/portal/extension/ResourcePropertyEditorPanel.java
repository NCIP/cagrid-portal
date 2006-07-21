package gov.nih.nci.cagrid.introduce.portal.extension;

import java.io.File;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.jdom.Document;


public abstract class ResourcePropertyEditorPanel extends JPanel {
	private Document doc;
	private File schemaFile;
	private File schemaDir;


	public ResourcePropertyEditorPanel(Document doc, File schemaFile, File schemaDir) {
		this.doc = doc;
		this.schemaFile = schemaFile;
		this.schemaDir = schemaDir;
	}
	
	public abstract boolean save();


	public Document getDoc() {
		return doc;
	}


	public void setDoc(Document doc) {
		this.doc = doc;
	}


	public File getSchemaFile() {
		return schemaFile;
	}


	public void setSchemaFile(File schemaFile) {
		this.schemaFile = schemaFile;
	}


	public File getSchemaDir() {
		return schemaDir;
	}


	public void setSchemaDir(File schemaDir) {
		this.schemaDir = schemaDir;
	}

}
