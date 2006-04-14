package gov.nih.nci.cagrid.data.ui;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;

import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.projectmobius.portal.PortalResourceManager;

/** 
 *  DataServiceTypesTable
 *  Table to maintain what types are available targets for a data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 5, 2006 
 * @version $Id$ 
 */
public class DataServiceTypesTable extends PortalBaseTable {
	private List serializationMappings;
	
	public DataServiceTypesTable() {
		super(createTableModel());
		serializationMappings = new ArrayList();
	}
	
	
	// Allows editing of certain table cells
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
	
	public void addType(NamespaceType namespace, SchemaElementType type) {
		SerializationMapping mapping = new SerializationMapping(namespace, type);
		addMapping(mapping);
	}
	
	
	public void addMapping(SerializationMapping mapping) {
		serializationMappings.add(mapping);
		((DefaultTableModel) getModel()).addRow(mapping.toVector());
	}
	
	
	public List getSerializationMappings() {
		return serializationMappings;
	}
	
	
	public void removeSerializationMapping(int i) {
		((DefaultTableModel) getModel()).removeRow(i);
		serializationMappings.remove(i);
	}
	
	
	public void refreshSerializationMapping(SerializationMapping mapping) {
		int index = serializationMappings.indexOf(mapping);
		serializationMappings.set(index, mapping);
		((DefaultTableModel) getModel()).removeRow(index);
		((DefaultTableModel) getModel()).insertRow(index, mapping.toVector());
	}
	
	
	private static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false; // not really...
			}
		};
		model.addColumn("Namespace");
		model.addColumn("Type");
		model.addColumn("Class");
		model.addColumn("Serializer");
		model.addColumn("Deserializer");
		model.addColumn("Encoding Style");
		return model;
	}
	
	
	public void doubleClick() {
		if (getSelectedColumn() >= 3) {
			TypeSerializationConfigDialog dialog = new TypeSerializationConfigDialog(
				this, (SerializationMapping) serializationMappings.get(getSelectedRow()));
			PortalResourceManager.getInstance().getGridPortal()
				.addGridPortalComponent(dialog);
			try {
				dialog.setSelected(true);
			} catch (PropertyVetoException ex) {
				// thats unfortunate
				ex.printStackTrace();
			}
			dialog.setSize(new java.awt.Dimension(400,153));
		}
	}
	
	
	public void singleClick() {
		
	}
}
