package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.beans.metadata.MetadataListType;
import gov.nih.nci.cagrid.introduce.beans.metadata.MetadataType;

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
public class MetadataTable extends PortalBaseTable {

	public static String NAMESPACE = "Namespace";

	public static String TYPE = "Type";

	public static String POPULATE_FROM_FILE = "Populate From File";

	public static String REGISTER = "Register";

	public static String DATA1 = "DATA1";

	private MetadataListType metadatas;


	public MetadataTable(MetadataListType metadatas) {
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
	
	public MetadataType getRowData(int row) throws Exception {
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		String namespace = (String) getValueAt(row, 0);
		String type = (String) getValueAt(row, 1);
		String populateFromFile = (String) getValueAt(row, 2);
		String register = (String) getValueAt(row, 3);

		MetadataType metadata = new MetadataType();

		if (namespace != null && !namespace.equals("") && type != null && !type.equals("")) {
			metadata.setQName(new QName(namespace,type));
		}
		if (populateFromFile != null && !populateFromFile.equals("")) {
			metadata.setPopulateFromFile(Boolean.valueOf(populateFromFile)
				.booleanValue());
		}
		if (register != null && !register.equals("")) {
			metadata.setRegister(Boolean.valueOf(register).booleanValue());
		}
		
		return metadata;
	}


	public void addRow(MetadataType metadata) {
		final Vector v = new Vector();
		v.add(metadata.getQName().getNamespaceURI());
		v.add(metadata.getQName().getLocalPart());
		v.add(String.valueOf(metadata.isPopulateFromFile()));
		v.add(String.valueOf(metadata.isRegister()));
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

		if (metadatas.getMetadata() != null) {
			for (int i = 0; i < metadatas.getMetadata().length; i++) {
				this.addRow(metadatas.getMetadata(i));
			}
		}
	}


	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(NAMESPACE);
		model.addColumn(TYPE);
		model.addColumn(POPULATE_FROM_FILE);
		model.addColumn(REGISTER);
		model.addColumn(DATA1);

		return model;
	}


	public void singleClick() throws Exception {
		// TODO Auto-generated method stub

	}


	public void doubleClick() throws Exception {
		// TODO Auto-generated method stub

	}
}