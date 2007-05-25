package gov.nih.nci.cagrid.introduce.portal.extension;

import java.io.File;

import javax.swing.JPanel;


public abstract class ResourcePropertyEditorPanel extends JPanel {
	private String rpString;
	private File schemaFile;
	private File schemaDir;


	public ResourcePropertyEditorPanel(String doc, File schemaFile, File schemaDir) {
		this.rpString = doc;
		this.schemaFile = schemaFile;
		this.schemaDir = schemaDir;
	}


	public abstract boolean save();


	public abstract String getResultRPString();


	protected String getRPString() {
		return rpString;
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
