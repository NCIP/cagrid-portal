package gov.nih.nci.cagrid.data.ui;

import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;

import java.util.Vector;

import javax.swing.JTable;
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
public class DataServiceTypesTable extends JTable {
	private DefaultTableModel model;
	
	public DataServiceTypesTable() {
		super();
		model = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return column >= 2;
			}
		};
		setModel(model);
		model.addColumn("Namespace");
		model.addColumn("Type");
		model.addColumn("Package");
		model.addColumn("Class");
		model.addColumn("Serializer");
		model.addColumn("Deserializer");
		model.addColumn("Encoding Style");
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
}
