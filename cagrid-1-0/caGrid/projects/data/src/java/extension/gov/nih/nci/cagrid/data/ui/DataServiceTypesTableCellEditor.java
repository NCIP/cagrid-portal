package gov.nih.nci.cagrid.data.ui;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

import org.projectmobius.portal.PortalResourceManager;


/**
 * DataServiceTypesTableCellEditor Cell editor for the data service types table
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @created Apr 7, 2006
 * @version $Id$
 */
public class DataServiceTypesTableCellEditor implements TableCellEditor {
	private List editorListeners = null;
	private TypeSerializationConfigDialog dialog = null;
	private ChangeEvent changeEvent = null;


	public DataServiceTypesTableCellEditor() {
		changeEvent = new ChangeEvent(this);
		editorListeners = new LinkedList();
	}


	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		// TODO: make the constructor take the right serialization mapping
		DataServiceTypesTable typesTable = (DataServiceTypesTable) table;
		SerializationMapping mapping = (SerializationMapping) typesTable.getSerializationMappings().get(
			typesTable.getSelectedRow());
		PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(
			new TypeSerializationConfigDialog(typesTable, mapping));
		return (Component) value;
	}


	public void cancelCellEditing() {
		if (dialog != null) {
			dialog.hide();
		}
		fireEditingCanceled();
	}


	public boolean stopCellEditing() {
		if (dialog != null) {
			dialog.dispose();
			dialog = null;
		}
		fireEditingStopped();
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
		if (anEvent instanceof MouseEvent) {
			MouseEvent mouseEvent = (MouseEvent) anEvent;
			if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
				Component potentialButton = SwingUtilities.getDeepestComponentAt((Component) anEvent.getSource(),
					mouseEvent.getX(), mouseEvent.getY());
				if (potentialButton != null && potentialButton instanceof JButton) {
					DataServiceTypesTable table = (DataServiceTypesTable) anEvent.getSource();
					dialog = new TypeSerializationConfigDialog(table, (SerializationMapping) table
						.getSerializationMappings().get(table.getSelectedRow()));
				}
			}
		}
		return false;
	}


	public void addCellEditorListener(CellEditorListener l) {
		editorListeners.add(l);
	}


	public void removeCellEditorListener(CellEditorListener l) {
		editorListeners.remove(l);
	}


	protected synchronized void fireEditingCanceled() {
		Iterator iter = editorListeners.iterator();
		while (iter.hasNext()) {
			((CellEditorListener) iter.next()).editingCanceled(changeEvent);
		}
	}


	protected synchronized void fireEditingStopped() {
		Iterator iter = editorListeners.iterator();
		while (iter.hasNext()) {
			((CellEditorListener) iter.next()).editingStopped(changeEvent);
		}
	}
}
