package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.common.portal.PortalUtils;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.portal.PortalResourceManager;
import org.projectmobius.portal.PortalTable;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: MethodsTable.java,v 1.2 2005-11-15 14:52:01 hastings Exp $
 */
public class MethodsTable extends PortalTable {

	public static String NAME = "Name";
	public static String DATA = "Data";

	private Document methodsDocument;
	private File methodsDirectory;
	private Properties serviceProperties;
	private MethodsTable me;


	public MethodsTable(Document methodsDocument, File methodsDirectory, Properties serviceProperties) {
		super(createTableModel());
		this.methodsDocument = methodsDocument;
		this.setRowSelectionAllowed(true);
		this.methodsDirectory = methodsDirectory;
		this.serviceProperties = serviceProperties;
		me = this;
		initialize();
	}
	
	

	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
	
	private void initialize() {
		this.getColumn(DATA).setMaxWidth(0);
		this.getColumn(DATA).setMinWidth(0);
		this.getColumn(DATA).setPreferredWidth(0);
		
		List methods = methodsDocument.getRootElement().getChildren("method",
			methodsDocument.getRootElement().getNamespace());
		for (int i = 0; i < methods.size(); i++) {
			Element method = (Element) methods.get(i);
			this.addRow(method);

		}
		
		addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (e.getClickCount() == 2) {
					try {
						int row = getSelectedRow();
						if((row<0)||(row>=getRowCount())){
							PortalUtils.showErrorMessage("Please select a method to modify.");
							return;
						}
						
						Element method = (Element) getValueAt(getSelectedRow(), 1);
						PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(
							new MethodViewer(method, new File(methodsDirectory.getAbsolutePath() + File.separator
								+ "schema" + File.separator + "cagrid" + File.separator
								+ serviceProperties.getProperty("introduce.skeleton.service.name")), me,getSelectedRow()));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
	}


	public void addRow(final Element method) {
		final Vector v = new Vector();
		v.add(method.getAttributeValue("name"));
		v.add(method);

		((DefaultTableModel) this.getModel()).addRow(v);
		 this.setRowSelectionInterval(this.getModel().getRowCount()-1, this.getModel().getRowCount()-1);
		
	}
	
	public void changeMethodName(int row, String newName){
		setValueAt(newName,row,0);
	}
	
	public String getMethodName(int row){
		return (String)getValueAt(row,0);
	}



	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(NAME);
		model.addColumn(DATA);
		return model;
	}
}