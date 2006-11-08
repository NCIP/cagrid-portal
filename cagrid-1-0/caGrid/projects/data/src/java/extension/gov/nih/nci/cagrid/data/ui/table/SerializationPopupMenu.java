package gov.nih.nci.cagrid.data.ui.table;

import gov.nih.nci.cagrid.data.DataServiceConstants;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;

/** 
 *  SerializationPopupMenu
 *  Popup menu shown when user selects items in the data service types table
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jun 6, 2006 
 * @version $Id$ 
 */
public class SerializationPopupMenu extends JPopupMenu {
	private JCheckBoxMenuItem defaultCheckItem = null;
	private JCheckBoxMenuItem sdkCheckItem = null;
	private JCheckBoxMenuItem customCheckItem = null;
	private ButtonGroup checkItemGroup = null;	
	private ClassElementSerializationTable classConfigTable = null;

	public SerializationPopupMenu(ClassElementSerializationTable typesTable) {
		this.classConfigTable = typesTable;
		add(getDefaultCheckItem());
		add(getSdkCheckItem());
		addSeparator();
		add(getCustomCheckItem());
	}
	
	
	public void show(Component invoker, int x, int y) {
		// figgure out which item to check
		if (isDefaultSerialization()) {
			getButtonGroup().setSelected(getDefaultCheckItem().getModel(), true);
		} else if (isSdkSerialization()) {
			getButtonGroup().setSelected(getSdkCheckItem().getModel(), true);
		} else {
			getButtonGroup().setSelected(getCustomCheckItem().getModel(), true);
		}
		super.show(invoker, x, y);
	}
	
	
	private boolean isSdkSerialization() {
		int[] selectedRows = classConfigTable.getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {
			String ser = (String) classConfigTable.getValueAt(selectedRows[i], 4);
			String des = (String) classConfigTable.getValueAt(selectedRows[i], 5);
			if (!(ser != null && des != null && 
				ser.equals(DataServiceConstants.SDK_SERIALIZER) 
				&& des.equals(DataServiceConstants.SDK_DESERIALIZER))) {
				return false;
			}
		}
		return true;
	}
	
	
	private boolean isDefaultSerialization() {
		int[] selectedRows = classConfigTable.getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {
			String ser = (String) classConfigTable.getValueAt(selectedRows[i], 4);
			String des = (String) classConfigTable.getValueAt(selectedRows[i], 5);
			if ((ser != null && ser.length() != 0) 
				|| (des != null && des.length() != 0)) {
				return false;
			}
		}
		return true;
	}
	
	
	private JCheckBoxMenuItem getDefaultCheckItem() {
		if (defaultCheckItem == null) {
			defaultCheckItem = new JCheckBoxMenuItem();
			defaultCheckItem.setText("Default Serialization");
			defaultCheckItem.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						int[] selectedRows = classConfigTable.getSelectedRows();
						for (int i = 0; i < selectedRows.length; i++) {
							classConfigTable.setValueAt("", selectedRows[i], 4);
							classConfigTable.setValueAt("", selectedRows[i], 5);
						}
					}
				}
			});
		}
		return defaultCheckItem;
	}
	
	
	private JCheckBoxMenuItem getSdkCheckItem() {
		if (sdkCheckItem == null) {
			sdkCheckItem = new JCheckBoxMenuItem();
			sdkCheckItem.setText("SDK Serialization");
			sdkCheckItem.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						int[] selectedRows = classConfigTable.getSelectedRows();
						for (int i = 0; i < selectedRows.length; i++) {
							classConfigTable.setValueAt(DataServiceConstants.SDK_SERIALIZER, selectedRows[i], 4);
							classConfigTable.setValueAt(DataServiceConstants.SDK_DESERIALIZER, selectedRows[i], 5);
						}
					}
				}
			});
		}
		return sdkCheckItem;
	}
	
	
	private JCheckBoxMenuItem getCustomCheckItem() {
		if (customCheckItem == null) {
			customCheckItem = new JCheckBoxMenuItem();
			customCheckItem.setText("Custom Serialization");
			customCheckItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (customCheckItem.isSelected()) {
						showCustomSerializationConfig();
					}
				}
			});
		}
		return customCheckItem;
	}
	
	
	private void showCustomSerializationConfig() {
		CustomSerializationDialog dialog = new CustomSerializationDialog();
		String serializer = dialog.getCustomSerializer();
		String deserializer = dialog.getCustomDeserializer();
		int[] selectedRows = classConfigTable.getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {
			classConfigTable.setValueAt(serializer, selectedRows[i], 4);
			classConfigTable.setValueAt(deserializer, selectedRows[i], 5);
		}
	}
	
	
	private ButtonGroup getButtonGroup() {
		if (checkItemGroup == null) {
			checkItemGroup = new ButtonGroup();
			checkItemGroup.add(getDefaultCheckItem());
			checkItemGroup.add(getSdkCheckItem());
			checkItemGroup.add(getCustomCheckItem());
			checkItemGroup.setSelected(getDefaultCheckItem().getModel(), true);
		}
		return checkItemGroup;
	}
}
