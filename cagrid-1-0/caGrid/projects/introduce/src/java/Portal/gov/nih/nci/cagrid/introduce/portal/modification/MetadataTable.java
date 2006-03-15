package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.beans.metadata.MetadataListType;
import gov.nih.nci.cagrid.introduce.beans.metadata.MetadataType;

import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.xml.namespace.QName;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: InputParametersTable.java,v 1.1 2005/06/29 19:31:01 hastings
 *          Exp $
 */
public class MetadataTable extends PortalBaseTable {

	public static String PACKAGENAME = "Package Name";

	public static String CLASSNAME = "Classname";

	public static String NAMESPACE = "Namespace";

	public static String TYPE = "Type";

	public static String LOCATION = "Location";

	public static String POPULATE_FROM_FILE = "Populate From File";

	public static String REGISTER = "Register";

	public static String QNAME_NS = "Qname Namespace";

	public static String QNAME_NAME = "Qname Name";

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
		String packageName = (String) getValueAt(row, 0);
		String className = (String) getValueAt(row, 1);
		String namespace = (String) getValueAt(row, 2);
		String type = (String) getValueAt(row, 3);
		String location = (String) getValueAt(row, 4);
		String populateFromFile = (String) getValueAt(row, 5);
		String register = (String) getValueAt(row, 6);
		String qnameNS = (String) getValueAt(row, 7);
		String qnameName = (String) getValueAt(row, 8);

		MetadataType metadata = new MetadataType();
		if (packageName != null && !packageName.equals("")) {
			metadata.setPackageName(packageName);
		}
		if (className != null && !className.equals("")) {
			metadata.setClassName(className);
		}
		if (namespace != null && !namespace.equals("")) {
			metadata.setNamespace(namespace);
		}
		if (type != null && !type.equals("")) {
			metadata.setType(type);
		}
		if (location != null && !location.equals("")) {
			metadata.setLocation(location);
		}
		if (populateFromFile != null && !populateFromFile.equals("")) {
			metadata.setPopulateFromFile(Boolean.valueOf(populateFromFile)
				.booleanValue());
		}
		if (register != null && !register.equals("")) {
			metadata.setRegister(Boolean.valueOf(register).booleanValue());
		}
		if (qnameNS != null && !qnameNS.equals("") && qnameName != null
			&& !qnameName.equals("")) {
			QName qn = new QName(qnameNS, qnameName);
			metadata.setQName(qn);
		}
		
		return metadata;
	}


	public void addRow(MetadataType metadata) {
		final Vector v = new Vector();
		v.add(metadata.getPackageName());
		v.add(metadata.getClassName());
		v.add(metadata.getNamespace());
		v.add(metadata.getType());
		v.add(metadata.getLocation());
		v.add(String.valueOf(metadata.isPopulateFromFile()));
		v.add(String.valueOf(metadata.isRegister()));
		if (metadata.getQName() != null) {
			v.add(metadata.getQName().getNamespaceURI());
			v.add(metadata.getQName().getLocalPart());
		} else {
			v.add("");
			v.add("");
		}

		v.add(v);

		((DefaultTableModel) this.getModel()).addRow(v);
		this.setRowSelectionInterval(this.getModel().getRowCount() - 1, this.getModel().getRowCount() - 1);
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
		model.addColumn(PACKAGENAME);
		model.addColumn(CLASSNAME);
		model.addColumn(NAMESPACE);
		model.addColumn(TYPE);
		model.addColumn(LOCATION);
		model.addColumn(POPULATE_FROM_FILE);
		model.addColumn(REGISTER);
		model.addColumn(QNAME_NS);
		model.addColumn(QNAME_NAME);
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