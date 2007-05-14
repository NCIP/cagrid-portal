package org.cagrid.rav;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.beans.property.ServicePropertiesProperty;
import gov.nih.nci.cagrid.introduce.common.CommonTools;


import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/*
 * Command arg issues
 * 1. We should alter service properties so we can modify the args list as is rather 
 * than having to change the method
 * 2. Specifying a list of args doesnt make it very easy to have different invocations
 * i.e poker 2 1 kyle
 *     poker kyle
 * 
 * 
 */

public class CommandArgsTable extends PortalBaseTable {

	public static String NAME = "Name";
	public static String VALUE = "Type";
	public static String DATA1 = "DATA1";

	//private ServiceInformation info;
	private Vector commandArgs;

	public CommandArgsTable(/*ServiceInformation info*/) {
		super(new MyDefaultTableModel());
		//this.info = info;
		this.commandArgs = new Vector();
		initialize();
	}


	public void setServiceInformation(/*ServiceInformation info*/) {
		//this.info = info;
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
		if (commandArgs != null){
			for (int i = 0; i < commandArgs.size(); i++) {
				Vector v = new Vector(2);
				v.add(((CommandArg)commandArgs.get(i)).getKey());
				v.add(((CommandArg)commandArgs.get(i)).getValue());
				v.add(v);
				((DefaultTableModel) this.getModel()).addRow(v);
			}
		}
		
		/*if (info.getServiceProperties() != null) {
			ServicePropertiesProperty[] allProperties = info.getServiceProperties().getProperty();
			if ((allProperties != null) && (allProperties.length > 0)) {
				for (int i = 0; i < allProperties.length; i++) {
					Vector v = new Vector(5);
					v.add(allProperties[i].getKey());
					v.add(allProperties[i].getValue());
					if (allProperties[i].getDescription() == null) {
						allProperties[i].setDescription("");
					}
					v.add(allProperties[i].getDescription());
					if (allProperties[i].getIsFromETC() != null) {
						v.add(allProperties[i].getIsFromETC());
					} else {
						v.add(new Boolean(false));
					}
					v.add(v);
					((DefaultTableModel) this.getModel()).addRow(v);
				}
			}
		}*/

		//sort();

		repaint();
	}


	private void setSelectedRow(int row) {
		setRowSelectionInterval(row, row);
	}


	public void addRow(String key, String value) {
		// add the property to the service model
		//CommonTools.setServiceProperty(info.getServiceDescriptor(), key, value, isFromETC, description);
		System.out.println("Adding Comand Args Row - " + key + "," +value);
		this.commandArgs.add(new CommandArg(key, value));
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
		//info.getServiceProperties().setProperty(row, property);

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
		String removeKey = (String) getValueAt(oldSelectedRow, 0);
		//CommonTools.removeServiceProperty(info.getServiceDescriptor(), removeKey);
		
		if (commandArgs != null){
			for (int i = 0; i < commandArgs.size(); i++) {
				if (((CommandArg)commandArgs.get(i)).getKey().equals(removeKey)){
					commandArgs.remove(i);
				}
			}			
		}
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
	
	
	// Bit of a hack will do the job for now
	class CommandArg{
		String key = null;
		String value = null;
		
		public CommandArg(){
		}
		public CommandArg(String key, String value){
			this.key = key;
			this.value = value;
		}
		
		public void addArgs(String key, String value){
			this.key = key;
			this.value = value;
		}
		
		public String getKey(){
			return key;
			
		}
		
		public String getValue(){
			return value;
			
		}
		
	}
	
	
	
}
