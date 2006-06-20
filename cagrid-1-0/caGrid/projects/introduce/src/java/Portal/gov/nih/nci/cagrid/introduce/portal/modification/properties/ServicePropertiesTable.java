package gov.nih.nci.cagrid.introduce.portal.modification.properties;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.beans.property.ServicePropertiesProperty;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;


/**
 * ServicePropertiesTable
 * Table to render and allow modification of service properties
 * as contained in an introduce service model.
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 */
public class ServicePropertiesTable extends PortalBaseTable {

	public static String NAME = "Name";
	public static String VALUE = "Default Value";
	public static String DATA1 = "DATA1";

	private ServiceInformation info;

	public ServicePropertiesTable(ServiceInformation info) {
		super(new MyDefaultTableModel());
		this.info = info;
		initialize();
	}


	public boolean isCellEditable(int row, int column) {
		return true;
	}
	
	
	public void refreshView() {
		// clean out old data
		while (getRowCount() != 0) {
			((DefaultTableModel) getModel()).removeRow(0);
		}
		// add new data
		if (info.getServiceProperties() != null) {
			ServicePropertiesProperty[] allProperties = info.getServiceProperties().getProperty();
			if (allProperties != null && allProperties.length != 0) {
				for (int i = 0; i < allProperties.length; i++) {
					Vector v = new Vector(3);
					v.add(allProperties[i].getKey());
					v.add(allProperties[i].getValue());
					v.add(v);
					((DefaultTableModel) this.getModel()).addRow(v);
				}
			}
		}
		repaint();
	}
	
	
	private void setSelectedRow(int row) {
		setRowSelectionInterval(row, row);
	}


	public void addRow(String key, String value) {
		// add the property to the service model
		CommonTools.setServiceProperty(info, key, value);
				
		// add the row to the GUI
		refreshView();
		
		// select the newly added row
		setSelectedRow(info.getServiceProperties().getProperty().length - 1);
	}


	public void modifyRow(final ServicePropertiesProperty property, int row) 
		throws IndexOutOfBoundsException {
		if ((row < 0) || (row >= getRowCount())) {
			throw new IndexOutOfBoundsException("invalid row: " + row);
		}
		// modify the property in the service model
		info.getServiceProperties().setProperty(row, property);
		
		// update the gui
		refreshView();
	}


	public void modifySelectedRow(final ServicePropertiesProperty property) 
		throws IndexOutOfBoundsException {
		modifyRow(property, getSelectedRow());
	}
	

	public void removeSelectedRow() throws IndexOutOfBoundsException {
		int row = getSelectedRow();
		if ((row < 0) || (row >= getRowCount())) {
			throw new IndexOutOfBoundsException("invalid row: " + row);
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
		this.getColumn(DATA1).setMaxWidth(0);
		this.getColumn(DATA1).setMinWidth(0);
		this.getColumn(DATA1).setPreferredWidth(0);
		refreshView();
		// handle modifications to the table model
		((DefaultTableModel) getModel()).addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					int row = e.getFirstRow();
					String key = (String) getValueAt(row, 0);
					String val = (String) getValueAt(row, 1);
					ServicePropertiesProperty modified = new ServicePropertiesProperty(key, val);
					modifyRow(modified, row);
				}
			}
		});
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
