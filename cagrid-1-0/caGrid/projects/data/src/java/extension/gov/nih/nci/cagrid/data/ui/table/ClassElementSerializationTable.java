package gov.nih.nci.cagrid.data.ui.table;

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
