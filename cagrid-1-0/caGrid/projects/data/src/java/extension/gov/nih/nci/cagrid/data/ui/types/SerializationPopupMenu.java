package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cagrid.data.common.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;

import java.awt.Component;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
	
	private DataServiceTypesTable typesTable = null;
	private SchemaElementType[] schemaTypes = null;

	public SerializationPopupMenu(DataServiceTypesTable typesTable) {
		this.typesTable = typesTable;
		add(getDefaultCheckItem());
		add(getSdkCheckItem());
		addSeparator();
		add(getCustomCheckItem());
	}
	
	
	public void show(Component invoker, int x, int y, SchemaElementType[] selectedTypes) {
		this.schemaTypes = selectedTypes;
		// figgure out which item to check
		if (!allSameSerialization(selectedTypes)) {
			getButtonGroup().setSelected(getCustomCheckItem().getModel(), true);
		} else if (isSdkSerialization(selectedTypes)) {
			getButtonGroup().setSelected(getSdkCheckItem().getModel(), true);
		}
		super.show(invoker, x, y);
	}
	
	
	private boolean allSameSerialization(SchemaElementType[] types) {
		String firstSer = types[0].getSerializer();
		String firstDeser = types[0].getDeserializer();
		for (int i = 1; i < types.length; i++) {
			if (firstSer != null) {
				if (!firstSer.equals(types[i].getSerializer())) {
					return false;
				}
			} else {
				if (types[i].getSerializer() != null) {
					return false;
				}
			}
			if (firstDeser != null) {
				if (!firstDeser.equals(types[i].getDeserializer())) {
					return false;
				}
			} else {
				if (types[i].getDeserializer() != null) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	private boolean isSdkSerialization(SchemaElementType[] types) {
		for (int i = 0; i < types.length; i++) {
			String ser = types[i].getSerializer();
			String des = types[i].getDeserializer();
			if (!(ser != null && des != null && 
				ser.equals(DataServiceConstants.SDK_SERIALIZER) && des.equals(DataServiceConstants.SDK_DESERIALIZER))) {
				return false;
			}
		}
		return true;
	}
	
	
	private JCheckBoxMenuItem getDefaultCheckItem() {
		if (defaultCheckItem == null) {
			defaultCheckItem = new JCheckBoxMenuItem();
			defaultCheckItem.setText("Default Serialization");
			defaultCheckItem.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (defaultCheckItem.isSelected()) {
						for (int i = 0; i < schemaTypes.length; i++) {
							schemaTypes[i].setSerializer("");
							schemaTypes[i].setDeserializer("");
							schemaTypes[i].setClassName("");
							typesTable.refreshSerialization(schemaTypes[i]);
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
			sdkCheckItem.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (sdkCheckItem.isSelected()) {
						for (int i = 0; i < schemaTypes.length; i++) {
							schemaTypes[i].setSerializer(DataServiceConstants.SDK_SERIALIZER);
							schemaTypes[i].setDeserializer(DataServiceConstants.SDK_DESERIALIZER);
							schemaTypes[i].setClassName(schemaTypes[i].getType());
							typesTable.refreshSerialization(schemaTypes[i]);
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
			customCheckItem.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (customCheckItem.isSelected()) {
						String serializer = JOptionPane.showInputDialog(typesTable, "Enter Serializer Class", "Serializer", JOptionPane.QUESTION_MESSAGE);
						String deserializer = JOptionPane.showInputDialog(typesTable, "Enter Deserializer Class", "Deserializer", JOptionPane.QUESTION_MESSAGE);
						for (int i = 0; i < schemaTypes.length; i++) {
							schemaTypes[i].setSerializer(serializer);
							schemaTypes[i].setDeserializer(deserializer);
							schemaTypes[i].setClassName(schemaTypes[i].getType());
							typesTable.refreshSerialization(schemaTypes[i]);
						}
					}
				}
			});
		}
		return customCheckItem;
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
