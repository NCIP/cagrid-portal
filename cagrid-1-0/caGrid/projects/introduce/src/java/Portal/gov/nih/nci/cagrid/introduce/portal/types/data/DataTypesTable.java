package gov.nih.nci.cagrid.introduce.portal.types.data;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.projectmobius.common.MalformedNamespaceException;
import org.projectmobius.common.Namespace;

/** 
 *  DataTypesTable
 *  Table to display types accessable from a data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Mar 22, 2006 
 * @version $Id$ 
 */
public class DataTypesTable extends PortalBaseTable {
	private List types;
	
	public DataTypesTable() {
		super(createTableModel());
		types = new LinkedList();
	}
	
	
	private static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Package");
		model.addColumn("Class Name");
		model.addColumn("Type");
		return model;
	}
	
	
	public void addSchemaElement(NamespaceType namespace, SchemaElementType element) {
		if (!types.contains(element)) {
			Vector row = new Vector(3);
			try {
				row.add(CommonTools.getPackageName(new Namespace(namespace.getNamespace())));
			} catch (MalformedNamespaceException ex) {
				row.add("!! ERROR !!");
			}
			row.add(element.getClassName());
			row.add(element.getType());
			((DefaultTableModel) getModel()).addRow(row);
			types.add(element);
		}
	}
	
	
	public SchemaElementType getSelectedType() {
		if (getSelectedRow() != -1) {
			return (SchemaElementType) types.get(getSelectedRow());
		}
		return null;
	}
	
	
	public void removeSelectedType() {
		if (getSelectedRow() != -1) {
			types.remove(getSelectedRow());
			((DefaultTableModel) getModel()).removeRow(getSelectedRow());
		}
	}
	
	
	public SchemaElementType[] getAllTypes() {
		SchemaElementType[] allTypes = new SchemaElementType[types.size()];
		types.toArray(allTypes);
		return allTypes;
	}
	

	public void doubleClick() throws Exception {
		// TODO: can somebody make these optional instead of abstract
		// and therefire required???
	}


	public void singleClick() throws Exception {
		// TODO Auto-generated method stub

	}
}
