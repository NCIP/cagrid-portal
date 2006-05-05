package gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;

import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.xml.namespace.QName;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * 
 */
public class ResourcePropertyTable extends PortalBaseTable {

	public static String NAMESPACE = "Namespace";

	public static String TYPE = "Type";

	public static String POPULATE_FROM_FILE = "Populate From File";

	public static String REGISTER = "Register";

	public static String DATA1 = "DATA1";

	private ResourcePropertiesListType metadatas;


	public ResourcePropertyTable(ResourcePropertiesListType metadatas) {
		super(createTableModel());
		this.metadatas = metadatas;
		this.setColumnSelectionAllowed(false);
		this.setRowSelectionAllowed(true);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		initialize();
	}


	public boolean isCellEditable(int row, int column) {
		return true;
	}
	
	public ResourcePropertyType getRowData(int row) throws Exception {
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		String namespace = (String) getValueAt(row, 0);
		String type = (String) getValueAt(row, 1);
		Boolean populateFromFile = (Boolean) getValueAt(row, 2);
		Boolean register = (Boolean) getValueAt(row, 3);

		ResourcePropertyType metadata = new ResourcePropertyType();

		if (namespace != null && !namespace.equals("") && type != null && !type.equals("")) {
			metadata.setQName(new QName(namespace,type));
		}
		if (populateFromFile != null && !populateFromFile.equals("")) {
			metadata.setPopulateFromFile(populateFromFile.booleanValue());
		}
		if (register != null && !register.equals("")) {
			metadata.setRegister(register.booleanValue());
		}
		
		return metadata;
	}


	public void addRow(ResourcePropertyType metadata) {
		final Vector v = new Vector();
		v.add(metadata.getQName().getNamespaceURI());
		v.add(metadata.getQName().getLocalPart());
		v.add(new Boolean(metadata.isPopulateFromFile()));
		v.add(new Boolean(metadata.isRegister()));
		v.add(v);

		((DefaultTableModel) this.getModel()).addRow(v);
		this.setRowSelectionInterval(this.getModel().getRowCount() - 1, this.getModel().getRowCount() - 1);
		paint(getGraphics());
	}
	
	public void removeSelectedRow() throws Exception {
		int row = getSelectedRow();
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		int oldSelectedRow = getSelectedRow();
		((DefaultTableModel) getModel()).removeRow(oldSelectedRow);
		if (oldSelectedRow == 0) {
			oldSelectedRow++;
		}
		if (getRowCount() > 0) {
			setRowSelectionInterval(oldSelectedRow - 1, oldSelectedRow - 1);
		}
		paint(getGraphics());
	}

	private void initialize() {
		this.getColumn(DATA1).setMaxWidth(0);
		this.getColumn(DATA1).setMinWidth(0);
		this.getColumn(DATA1).setPreferredWidth(0);
		
		for (int i = this.getRowCount() - 1; i == 0; i--) {
			this.removeRow(i);
		}

		if (metadatas !=null && metadatas.getResourceProperty() != null) {
			for (int i = 0; i < metadatas.getResourceProperty().length; i++) {
				this.addRow(metadatas.getResourceProperty(i));
			}
		}
	}

	
	public void setResourceProperties(ResourcePropertiesListType properties){
		this.metadatas = properties;
		initialize();
	}


	public static MyDefaultTableModel createTableModel() {
		MyDefaultTableModel model = new MyDefaultTableModel();

		return model;
	}


	public void singleClick() throws Exception {
		// TODO Auto-generated method stub

	}


	public void doubleClick() throws Exception {
		// TODO Auto-generated method stub

	}
	
	public static class MyDefaultTableModel extends DefaultTableModel {

		public MyDefaultTableModel() {
			super();
			addColumn(NAMESPACE);
			addColumn(TYPE);
			addColumn(POPULATE_FROM_FILE);
			addColumn(REGISTER);
			addColumn(DATA1);
		}


		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}
	}
}