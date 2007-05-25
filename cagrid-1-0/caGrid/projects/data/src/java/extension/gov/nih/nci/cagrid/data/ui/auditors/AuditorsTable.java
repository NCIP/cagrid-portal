package gov.nih.nci.cagrid.data.ui.auditors;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/** 
 *  AuditorsTable
 *  Table of auditors used by the data service
 * 
 * @author David Ervin
 * 
 * @created May 21, 2007 10:41:23 AM
 * @version $Id: AuditorsTable.java,v 1.3 2007-05-25 14:34:47 dervin Exp $ 
 */
public class AuditorsTable extends JTable {
    
    private DefaultTableModel model = null;
    private List<AuditorChangeListener> auditorChangeListeners = null;

    public AuditorsTable() {
        super();
        auditorChangeListeners = new LinkedList();
        setModel(getAuditorsTableModel());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getFirstIndex() != -1) {
                    fireAuditorSelectionChanged();
                }
            }
        });
    }
    
    
    public void addAuditorChangeListener(AuditorChangeListener listener) {
        auditorChangeListeners.add(listener);
    }
    
    
    public boolean removeAuditorChangeListener(AuditorChangeListener listener) {
        return auditorChangeListeners.remove(listener);
    }
    
    
    public AuditorChangeListener[] getAuditorChangeListeners() {
        AuditorChangeListener[] listeners = new AuditorChangeListener[auditorChangeListeners.size()];
        auditorChangeListeners.toArray(listeners);
        return listeners;
    }
    
    
    public void addAuditor(String className, String instanceName) {
        Vector<String> row = new Vector(2);
        row.add(className);
        row.add(instanceName);
        getAuditorsTableModel().addRow(row);
    }
    
    
    public void removeAuditor(String className, String instanceName) {
        for (int i = 0; i < getRowCount(); i++) {
            if (getValueAt(i, 0).equals(className) 
                && getValueAt(i, 1).equals(instanceName)) {
                System.out.println("Removing auditor at row " + i);
                getAuditorsTableModel().removeRow(i);
                return;
            }
        }
    }
    
    
    public String getSelectedClassName() {
        if (getSelectedRow() == -1) {
            return null;
        }
        return getValueAt(getSelectedRow(), 0).toString();
    }
    
    
    public String getSelectedInstanceName() {
        if (getSelectedRow() == -1) {
            return null;
        }
        return getValueAt(getSelectedRow(), 1).toString();
    }
    
    
    private DefaultTableModel getAuditorsTableModel() {
        if (model == null) {
            model = new DefaultTableModel();
            model.addColumn("Auditor Class");
            model.addColumn("Instance Name");
            
        }
        return model;
    }
    
    
    protected void fireAuditorSelectionChanged() {
        String className = getSelectedClassName();
        String instanceName = getSelectedInstanceName();
        for (AuditorChangeListener listener : auditorChangeListeners) {
            listener.auditorSelectionChanged(className, instanceName);
        }
    }
}
