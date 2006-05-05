package gov.nih.nci.cagrid.introduce.portal.modification.properties;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.beans.property.ServiceProperties;
import gov.nih.nci.cagrid.introduce.beans.property.ServicePropertiesProperty;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 */
public class ServicePropertiesTable extends PortalBaseTable {

	public static String NAME = "Name";
	public static String VALUE = "Default Value";
	public static String DATA1 = "DATA1";

	private ServiceProperties properties;


	public ServicePropertiesTable(ServiceProperties properties) {
		super(createTableModel());
		this.properties = properties;
		initialize();
	}


	public boolean isCellEditable(int row, int column) {
		return true;
	}


	public void addRow(final ServicePropertiesProperty property) {
		final Vector v = new Vector();
		v.add(property.getKey());
		v.add(property.getValue());
		v.add(v);

		((DefaultTableModel) this.getModel()).addRow(v);
		this.setRowSelectionInterval(this.getModel().getRowCount() - 1, this.getModel().getRowCount() - 1);
	}


	public void modifyRow(final ServicePropertiesProperty property, int row) throws Exception {
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		Vector v = (Vector) getValueAt(row, 2);
		v.set(0, property.getKey());
		v.set(1, property.getValue());
		v.set(2, v);
	}


	public void modifySelectedRow(final ServicePropertiesProperty property) throws Exception {
		modifyRow(property,getSelectedRow());
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


	public ServicePropertiesProperty getRowData(int row) throws Exception {
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		ServicePropertiesProperty property = new ServicePropertiesProperty();

		String key = ((String) getValueAt(row, 0));
		String value = ((String) getValueAt(row, 1));

		property.setKey(key);
		property.setValue(value);

		return property;
	}


	public ServicePropertiesProperty getSelectedRowData() throws Exception {
		return getRowData(getSelectedRow());
	}


	private void initialize() {
		this.getColumn(DATA1).setMaxWidth(0);
		this.getColumn(DATA1).setMinWidth(0);
		this.getColumn(DATA1).setPreferredWidth(0);
		if (this.properties != null && this.properties.getProperty() != null) {
			for (int i = 0; i < this.properties.getProperty().length; i++) {
				addRow(this.properties.getProperty(i));
			}
		}
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
			addColumn(NAME);
			addColumn(VALUE);
			addColumn(DATA1);
		}


		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}
	}
}
