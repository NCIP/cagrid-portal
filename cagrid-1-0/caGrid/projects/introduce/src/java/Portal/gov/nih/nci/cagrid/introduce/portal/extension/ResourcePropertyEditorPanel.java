package gov.nih.nci.cagrid.introduce.portal.extension;

import java.io.File;
import java.io.InputStream;

import javax.swing.JPanel;


public abstract class ResourcePropertyEditorPanel extends JPanel {
	private InputStream rpInputStream;
	private File schemaFile;
	private File schemaDir;


	public ResourcePropertyEditorPanel(InputStream doc, File schemaFile, File schemaDir) {
		this.rpInputStream = doc;
		this.schemaFile = schemaFile;
		this.schemaDir = schemaDir;
	}


	public abstract boolean save();


	public abstract InputStream getResultRPInputStream();


	protected InputStream getRPInputStream() {
		return rpInputStream;
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
