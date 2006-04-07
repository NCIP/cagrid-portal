package gov.nih.nci.cagrid.data.ui;

import java.awt.Component;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

/** 
 *  DataServiceTypesTableCellEditor
 *  TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 7, 2006 
 * @version $Id$ 
 */
public class DataServiceTypesTableCellEditor implements TableCellEditor {
	private List editorListeners = null;
	private TypeSerializationConfigDialog dialog = null;
	
	public DataServiceTypesTableCellEditor() {
		editorListeners = new LinkedList();
	}
	

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		dialog = new TypeSerializationConfigDialog();
		return dialog;
	}


	public void cancelCellEditing() {
		if (dialog != null) {
			dialog.hide();
		}
	}


	public boolean stopCellEditing() {
		if (dialog != null) {
			dialog.dispose();
			dialog = null;
		}
		return true;
	}


	public Object getCellEditorValue() {
		return dialog;
	}


	public boolean isCellEditable(EventObject anEvent) {
		JTable sourceTable = (JTable) anEvent.getSource();
		return sourceTable.getSelectedColumn() >= 2;
	}


	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}


	public void addCellEditorListener(CellEditorListener l) {
		editorListeners.add(l);
	}


	public void removeCellEditorListener(CellEditorListener l) {
		editorListeners.remove(l);
	}
	
	
	protected void fireEditingCanceled() {
		ChangeEvent e = new ChangeEvent(this);
		Iterator iter = editorListeners.iterator();
		while (iter.hasNext()) {
			((CellEditorListener) iter.next()).editingCanceled(e);
		}
	}
	
	
	protected void fireEditingStopped() {
		ChangeEvent e = new ChangeEvent(this);
		Iterator iter = editorListeners.iterator();
		while (iter.hasNext()) {
			((CellEditorListener) iter.next()).editingStopped(e);
		}
	}
}
