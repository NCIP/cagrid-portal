package gov.nih.nci.cagrid.introduce.portal.modification.properties;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.beans.property.ServicePropertiesProperty;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;


/**
 * ServicePropertiesTable Table to render and allow modification of service
 * properties as contained in an introduce service model.
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 */
public class ServicePropertiesTable extends PortalBaseTable {

	public static String NAME = "Name";
	public static String VALUE = "Default Value";
	public static String ETC = "Path From Etc";
	public static String DATA1 = "DATA1";

	private ServiceInformation info;


	public ServicePropertiesTable(ServiceInformation info) {
		super(new MyDefaultTableModel());
		this.info = info;
		initialize();
	}
	
	public void setServiceInformation(ServiceInformation info){
		this.info = info;
		refreshView();
	}


	public boolean isCellEditable(int row, int column) {
		return false;
	}


	public void refreshView() {
		// clean out old data
		while (getRowCount() != 0) {
			((DefaultTableModel) getModel()).removeRow(0);
		}
		// add new data
		if (info.getServiceProperties() != null) {
			ServicePropertiesProperty[] allProperties = info.getServiceProperties().getProperty();
			if (allProperties != null && allProperties.length > 0) {
				for (int i = 0; i < allProperties.length; i++) {
					Vector v = new Vector(4);
					v.add(allProperties[i].getKey());
					v.add(allProperties[i].getValue());
					if (allProperties[i].getIsFromETC() != null) {
						v.add(allProperties[i].getIsFromETC());
					} else {
						v.add(new Boolean(false));
					}
					v.add(v);
					((DefaultTableModel) this.getModel()).addRow(v);
				}
			}
		}

		sort();

		repaint();
	}


	private void setSelectedRow(int row) {
		setRowSelectionInterval(row, row);
	}


	public void addRow(String key, String value, boolean isFromETC) {
		// add the property to the service model
		CommonTools.setServiceProperty(info, key, value, isFromETC);

		// add the row to the GUI
		refreshView();

		for (int i = 0; i < getRowCount(); i++) {
			String tkey = (String) getModel().getValueAt(i, 0);
			if (tkey.equals(key)) {
				// select the newly added row
				setSelectedRow(i);
			}
		}

	}


	public void modifyRow(final ServicePropertiesProperty property, int row) throws IndexOutOfBoundsException {
		if ((row < 0) || (row >= getRowCount())) {
			throw new IndexOutOfBoundsException("invalid row: " + row);
		}
		// modify the property in the service model
		info.getServiceProperties().setProperty(row, property);

		// update the gui
		refreshView();
	}


	public void modifySelectedRow(final ServicePropertiesProperty property) throws IndexOutOfBoundsException {
		modifyRow(property, getSelectedRow());
	}


	public void removeSelectedRow() throws IndexOutOfBoundsException {
		int row = getSelectedRow();
		if ((row < 0) || (row >= getRowCount())) {
			return;
		}
		int oldSelectedRow = getSelectedRow();
		// remove the row from the model
		String removeKey = info.getServiceProperties().getProperty(oldSelectedRow).getKey();
		CommonTools.removeServiceProperty(info, removeKey);

		// update GUI
		refreshView();

		// change row selection
		if (getRowCount() > 0) {
			if (oldSelectedRow == 0) {
				setSelectedRow(0);
			} else {
				setSelectedRow(oldSelectedRow - 1);
			}
		}
	}


	private void initialize() {
		setAutoCreateColumnsFromModel(false);
		this.getTableHeader().setReorderingAllowed(false);
		this.getColumn(DATA1).setMaxWidth(0);
		this.getColumn(DATA1).setMinWidth(0);
		this.getColumn(DATA1).setPreferredWidth(0);
		refreshView();
	}


	public void singleClick() throws Exception {
		// TODO Auto-generated method stub
	}


	public void doubleClick() throws Exception {
		// TODO Auto-generated method stub
	}


	public void sort() {
		DefaultTableModel model = (DefaultTableModel) getModel();
		Vector data = model.getDataVector();
		Collections.sort(data, new ColumnSorter(0, true));
		model.fireTableStructureChanged();
	}


	public class ColumnSorter implements Comparator {
		int colIndex;
		boolean ascending;


		ColumnSorter(int colIndex, boolean ascending) {
			this.colIndex = colIndex;
			this.ascending = ascending;
		}


		public int compare(Object a, Object b) {
			Vector v1 = (Vector) a;
			Vector v2 = (Vector) b;
			String o1 = (String) v1.get(colIndex);
			String o2 = (String) v2.get(colIndex);

			if (o1 == null && o2 == null) {
				return 0;
			} else if (o1 == null) {
				return 1;
			} else if (o2 == null) {
				return -1;
			} else if (o1 instanceof Comparable) {
				if (ascending) {
					return ((Comparable) o1).compareTo(o2);
				} else {
					return ((Comparable) o2).compareTo(o1);
				}
			} else {
				if (ascending) {
					return o1.toString().compareTo(o2.toString());
				} else {
					return o2.toString().compareTo(o1.toString());
				}
			}
		}
	}


	public static class MyDefaultTableModel extends DefaultTableModel {

		public MyDefaultTableModel() {
			super();
			addColumn(NAME);
			addColumn(VALUE);
			addColumn(ETC);
			addColumn(DATA1);
		}


		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}
	}
}
