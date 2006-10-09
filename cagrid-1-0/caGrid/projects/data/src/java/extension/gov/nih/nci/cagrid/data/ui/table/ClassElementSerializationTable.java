package gov.nih.nci.cagrid.data.ui.table;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

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
		this.classInformationChangeListeners = new LinkedList();
		getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					if (e.getColumn() == 3) {
						fireElementNameChanged(e.getFirstRow());
					} else if (e.getColumn() == 4 || e.getColumn() == 5) {
						fireSerializationChanged(e.getFirstRow());
					}
				}
			}
		});
	}
	
	
	public boolean isCellEditable(int row, int column) {
		return column == 3;
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
	
	
	private ClassChangeEvent getChangeForRow(int row) {
		String packName = (String) getValueAt(row, 0);
		String className = (String) getValueAt(row, 1);
		String namespace = (String) getValueAt(row, 2);
		String elemName = (String) getValueAt(row, 3);
		String serializer = (String) getValueAt(row, 4);
		String deserializer = (String) getValueAt(row, 5);
		
		ClassChangeEvent event = new ClassChangeEvent(this, packName,
			className, namespace, elemName, serializer, deserializer);
		return event;
	}
	
	
	private static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Package Name");
		model.addColumn("Class Name");
		model.addColumn("Namespace");
		model.addColumn("Element Name");
		model.addColumn("Serializer");
		model.addColumn("Deserializer");
		return model;
	}
}
