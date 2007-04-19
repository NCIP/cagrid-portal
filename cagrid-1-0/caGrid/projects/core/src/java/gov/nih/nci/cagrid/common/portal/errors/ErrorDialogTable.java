package gov.nih.nci.cagrid.common.portal.errors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.projectmobius.portal.JComponentCellEditor;

/** 
 *  ErrorDialogTable
 *  Table to display and manage errors
 * 
 * @author David Ervin
 * 
 * @created Apr 13, 2007 9:20:12 AM
 * @version $Id: ErrorDialogTable.java,v 1.3 2007-04-19 04:23:10 dervin Exp $ 
 */
public class ErrorDialogTable extends JTable {
    private DefaultTableModel model = null;
    private Map<Integer, ErrorContainer> rowData = null;
    private List<ErrorDialogTableListener> tableListeners = null;
    
    private ActionListener detailButtonListener = null;
    private ActionListener exceptionButtonListener = null;

    public ErrorDialogTable() {
        super();
        model = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                return col != 0;
            }
        };
        rowData = new HashMap();
        tableListeners = new LinkedList();
        setModel(model);
        setDefaultRenderer(Object.class, new ErrorDialogTableCellRenderer());
        setDefaultEditor(Component.class, new JComponentCellEditor());
        model.addColumn("Message");
        model.addColumn("Details");
        model.addColumn("Exception");
        Component detailsComponent = getCellRenderer(0, 1).getTableCellRendererComponent(
            this, new JButton("Details"), false, false, 0, 1);
        Component exceptionComponent = getCellRenderer(0, 2).getTableCellRendererComponent(
            this, new JButton("Exception"), false, false, 0, 2);
        int detailsWidth = (int) detailsComponent.getPreferredSize().getWidth() + (2 * getIntercellSpacing().width);
        int exceptionWidth = (int) exceptionComponent.getPreferredSize().getWidth() + (2 * getIntercellSpacing().width);
        // set the width on button columns smaller
        TableColumn detailsColumn = getColumn("Details");
        detailsColumn.setPreferredWidth(detailsWidth);
        detailsColumn.setWidth(detailsWidth);
        detailsColumn.setMaxWidth(detailsWidth);
        TableColumn exceptionColumn = getColumn("Exception");
        exceptionColumn.setPreferredWidth(exceptionWidth);
        exceptionColumn.setWidth(exceptionWidth);
        exceptionColumn.setMaxWidth(exceptionWidth);
    }
    
    
    public Class getColumnClass(int col) {
        return col == 0 ? String.class : Component.class;
    }
    
    
    public void addError(ErrorContainer container) {
        Vector row = new Vector();
        row.add(container.getMessage());
        JButton detailButton = new JButton();
        detailButton.setText("Details");
        detailButton.setEnabled(container.getDetail() != null);
        detailButton.addActionListener(getDetailButtonListener());
        row.add(detailButton);
        JButton exceptButton = new JButton();
        exceptButton.setText("Exception");
        exceptButton.setEnabled(container.getError() != null);
        exceptButton.addActionListener(getExceptionButtonListener());
        row.add(exceptButton);
        
        model.addRow(row);
        rowData.put(Integer.valueOf(getRowCount() - 1), container);
    }
    
    
    public void clearTable() {
        while (getRowCount() != 0) {
            model.removeRow(0);
        }
        rowData.clear();
    }
    
    
    public void addErrorTableListener(ErrorDialogTableListener listener) {
        tableListeners.add(listener);
    }
    
    
    public boolean removeErrorTableListener(ErrorDialogTableListener listener) {
        return tableListeners.remove(listener);
    }
    
    
    protected void fireDetailsClicked() {
        Integer row = Integer.valueOf(getEditingRow());
        ErrorContainer container = rowData.get(row);
        for (ErrorDialogTableListener listener : tableListeners) {
            listener.showDetailsClicked(container);
        }
    }
    
    
    protected void fireExceptionClicked() {
        Integer row = Integer.valueOf(getEditingRow());
        ErrorContainer container = rowData.get(row);
        for (ErrorDialogTableListener listener : tableListeners) {
            listener.showErrorClicked(container);
        }
    }
    
    
    private ActionListener getDetailButtonListener() {
        if (detailButtonListener == null) {
            detailButtonListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireDetailsClicked();
                }
            };
        }
        return detailButtonListener;
    }
    
    
    private ActionListener getExceptionButtonListener() {
        if (exceptionButtonListener == null) {
            exceptionButtonListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireExceptionClicked();
                }
            };
        }
        return exceptionButtonListener;
    }
    
    
    private static class ErrorDialogTableCellRenderer extends DefaultTableCellRenderer {
        
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof Component) {
                return (Component) value;
            }
            return this;
        }
    }
}
