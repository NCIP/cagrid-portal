package gov.nih.nci.cagrid.data.ui.table;

import gov.nih.nci.cagrid.data.extension.ClassMapping;
import gov.nih.nci.cagrid.data.ui.types.NamespaceUtils;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;

import java.awt.Component;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

/** 
 *  ClassElementSerializationTable
 *  Table for showing and configuring class, namespace, element, and serialization
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Oct 6, 2006 
 * @version $Id$ 
 */
public class ClassElementSerializationTable extends JTable {
	private List classInformationChangeListeners = null;

	public ClassElementSerializationTable() {
		super(createTableModel());
		setDefaultRenderer(Object.class, new ComponentCellRenderer());
		setDefaultEditor(Component.class, new ComponentCellEditor());
		this.classInformationChangeListeners = new LinkedList();
		getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					switch (e.getColumn()) {
						case 3:
							fireElementNameChanged(e.getFirstRow());
							break;
						case 4:
						case 5:
							fireSerializationChanged(e.getFirstRow());
							break;
						case 6:
							fireTargetabilityChanged(e.getFirstRow());
							break;
					}
				}
			}
		});
	}
	
	
	public void addClass(String pack, ClassMapping mapping, NamespaceType nsType) {
		Vector row = new Vector(7);
		row.add(pack);
		row.add(mapping.getClassName());
		row.add(nsType.getNamespace());
		row.add(mapping.getElementName());
		SchemaElementType schemaType = NamespaceUtils.getElementByName(nsType, mapping.getElementName());
		row.add(schemaType == null ? null : schemaType.getSerializer());
		row.add(schemaType == null ? null : schemaType.getDeserializer());
		JCheckBox check = new JCheckBox();
		check.setSelected(mapping.isTargetable());
		row.add(check);
		((DefaultTableModel) getModel()).addRow(row);
	}
	
	
	public void removeRow(String packName, String className) {
		// find the row needed
		int row = 0;
		while (row < getRowCount()) {
			if (packName.equals(getValueAt(row, 0)) && className.equals(getValueAt(row, 1))) {
				((DefaultTableModel) getModel()).removeRow(row);
				break;
			}
			row++;
		}
	}
	
	
	public boolean isCellEditable(int row, int column) {
		return column >= 3;
	}
	
	
	public void addClassInformatonChangeListener(ClassInformatonChangeListener l) {
		classInformationChangeListeners.add(l);
	}
	
	
	public boolean removeClassInformatonChangeListener(ClassInformatonChangeListener l) {
		return classInformationChangeListeners.remove(l);
	}
	
	
	public ClassInformatonChangeListener[] getClassInformationChangeListeners() {
		ClassInformatonChangeListener[] listeners = 
			new ClassInformatonChangeListener[classInformationChangeListeners.size()];
		classInformationChangeListeners.toArray(listeners);
		return listeners;
	}
	
	
	public void clearTable() {
		while (getRowCount() != 0) {
			((DefaultTableModel) getModel()).removeRow(0);
		}
	}
	
	
	protected void fireElementNameChanged(int row) {
		ClassChangeEvent e = getChangeForRow(row);
		Iterator i = classInformationChangeListeners.iterator();
		while (i.hasNext()) {
			((ClassInformatonChangeListener) i.next()).elementNameChanged(e);
		}
	}
	
	
	protected void fireSerializationChanged(int row) {
		ClassChangeEvent e = getChangeForRow(row);
		Iterator i = classInformationChangeListeners.iterator();
		while (i.hasNext()) {
			((ClassInformatonChangeListener) i.next()).serializationChanged(e);
		}
	}
	
	
	protected void fireTargetabilityChanged(int row) {
		ClassChangeEvent e = getChangeForRow(row);
		Iterator i = classInformationChangeListeners.iterator();
		while (i.hasNext()) {
			((ClassInformatonChangeListener) i.next()).targetabilityChanged(e);
		}
	}
	
	
	private ClassChangeEvent getChangeForRow(int row) {
		String packName = (String) getValueAt(row, 0);
		String className = (String) getValueAt(row, 1);
		String namespace = (String) getValueAt(row, 2);
		String elemName = (String) getValueAt(row, 3);
		String serializer = (String) getValueAt(row, 4);
		String deserializer = (String) getValueAt(row, 5);
		boolean targetable = ((JCheckBox) getValueAt(row, 6)).isSelected();
		
		ClassChangeEvent event = new ClassChangeEvent(this, packName,
			className, namespace, elemName, serializer, deserializer, targetable);
		return event;
	}
	
	
	private static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel() {
			public Class getColumnClass(int column) {
				return column == 6 ? Component.class : Object.class;
			}
		};
		model.addColumn("Package Name");
		model.addColumn("Class Name");
		model.addColumn("Namespace");
		model.addColumn("Element Name");
		model.addColumn("Serializer");
		model.addColumn("Deserializer");
		model.addColumn("Targetable");
		return model;
	}
	
	
	private static class ComponentCellRenderer extends DefaultTableCellRenderer {
		
		public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
			if (value instanceof Component) {
				return (Component) value;
			}
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}
	
	
	private static class ComponentCellEditor extends AbstractCellEditor implements TableCellEditor {
		
		private Object editorValue = null;
		
		public ComponentCellEditor() {
			editorValue = null;
		}
		
		
		public Object getCellEditorValue() {
			return editorValue;
		}
		
		
		public Component getTableCellEditorComponent(JTable table, Object value, 
			boolean isSelected, int row, int column) {
			editorValue = value;
			return (Component) value;
		}
	}
}
