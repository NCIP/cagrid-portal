package gov.nih.nci.cagrid.data.ui;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;

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
	private DefaultTableModel model;
	
	public DataServiceTypesTable() {
		super(createTableModel());
	}
	
	
	// Allows editing of certain table cells
	public boolean isCellEditable(int row, int column) {
		return column >= 2;
	}
	
	
	public void addType(NamespaceType namespace, SchemaElementType type) {
		Vector row = new Vector(7);
		row.add(namespace.getNamespace());
		row.add(type.getType());
		row.add(type.getPackageName());
		row.add(type.getClassName());
		row.add("");
		row.add("");
		row.add("");
		model.addRow(row);
	}
	
	
	private static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Namespace");
		model.addColumn("Type");
		model.addColumn("Package");
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
