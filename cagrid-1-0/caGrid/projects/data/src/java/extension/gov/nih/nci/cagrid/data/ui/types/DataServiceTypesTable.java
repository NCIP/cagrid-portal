package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

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
	private List schemaElementTypes;
	
	public DataServiceTypesTable() {
		super(createTableModel());
		schemaElementTypes = new ArrayList();
	}
	
	
	public void addType(NamespaceType namespace, SchemaElementType type) {
		Vector v = new Vector(5);
		v.add(namespace.getNamespace());
		v.add(namespace.getPackageName());
		v.add(type.getType());
		v.add(type.getSerializer());
		v.add(type.getDeserializer());
		((DefaultTableModel) getModel()).addRow(v);
		schemaElementTypes.add(type);
	}
	
	
	public SchemaElementType getSelectedElementType() {
		if (getSelectedRow() != -1) {
			return (SchemaElementType) schemaElementTypes.get(getSelectedRow());
		}
		return null;
	}
	
	
	public List getSchemaElementTypes() {
		return schemaElementTypes;
	}
	
	
	public void removeSchemaElementType(int i) {
		((DefaultTableModel) getModel()).removeRow(i);
		schemaElementTypes.remove(i);
	}
	
	
	public void removeSchemaElementType(SchemaElementType type) {
		int index = schemaElementTypes.indexOf(type);
		if (index == -1) {
			throw new NoSuchElementException("No schema element type found");
		}
		removeSchemaElementType(index);
	}
	
	
	public void refreshSerialization(SchemaElementType elementType) {
		int index = schemaElementTypes.indexOf(elementType);
		schemaElementTypes.set(index, elementType);
		((DefaultTableModel) getModel()).setValueAt(elementType.getSerializer(), index, 3);
		((DefaultTableModel) getModel()).setValueAt(elementType.getDeserializer(), index, 4);
	}
	
	
	private static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		model.addColumn("Namespace");
		model.addColumn("Package");
		model.addColumn("Type");
		model.addColumn("Serializer");
		model.addColumn("Deserializer");
		return model;
	}
	
	
	public void doubleClick() {
		
	}
	
	
	public void singleClick() {
		
	}
}
