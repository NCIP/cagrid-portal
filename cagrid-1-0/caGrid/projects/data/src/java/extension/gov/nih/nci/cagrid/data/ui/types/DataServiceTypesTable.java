package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cagrid.data.common.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
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
	private List schemaElementTypes;
	
	public DataServiceTypesTable() {
		super(createTableModel());
		DefaultListSelectionModel listSelection = new DefaultListSelectionModel();
		listSelection.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		setSelectionModel(listSelection);
		schemaElementTypes = new ArrayList();
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger() && getSelectedRows().length != 0) {
					SerializationPopupMenu pop = new SerializationPopupMenu(DataServiceTypesTable.this);
					SchemaElementType[] selection = getSelectedElementTypes();
					if (selection != null) {
						pop.show(DataServiceTypesTable.this, e.getX(), e.getY(), getSelectedElementTypes());
					}
				}
			}
			
			
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger() && getSelectedRows().length != 0) {
					SerializationPopupMenu pop = new SerializationPopupMenu(DataServiceTypesTable.this);
					SchemaElementType[] selection = getSelectedElementTypes();
					if (selection != null) {
						pop.show(DataServiceTypesTable.this, e.getX(), e.getY(), getSelectedElementTypes());
					}
				}
			}
		});
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
	
	
	public SchemaElementType[] getSelectedElementTypes() {
		int[] selectedRows = getSelectedRows();
		if (selectedRows.length != 0) {
			SchemaElementType[] selected = new SchemaElementType[selectedRows.length];
			for (int i = 0; i < selectedRows.length; i++) {
				selected[i] = (SchemaElementType) schemaElementTypes.get(selectedRows[i]);
			}
			return selected;
		}
		return null;
	}
	
	
	public List getAllSchemaElementTypes() {
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
}
