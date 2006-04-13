package gov.nih.nci.cagrid.data.ui;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

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
		setDefaultEditor(Object.class, new DataServiceTypesTableCellEditor());
		setDefaultRenderer(Object.class, new DataServiceTypesTableCellRenderer());
		serializationMappings = new LinkedList();
	}
	
	
	// Allows editing of certain table cells
	public boolean isCellEditable(int row, int column) {
		return column >= 2;
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
	
	
	private static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return column >= 2;
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
		
	}
	
	
	public void singleClick() {
		
	}
}
