package gov.nih.nci.cagrid.data.ui.auditors;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/** 
 *  AuditorsTable
 *  Table of auditors used by the data service
 * 
 * @author David Ervin
 * 
 * @created May 21, 2007 10:41:23 AM
 * @version $Id: AuditorsTable.java,v 1.1 2007-05-21 19:07:57 dervin Exp $ 
 */
public class AuditorsTable extends JTable {
    
    private DefaultTableModel model = null;

    public AuditorsTable() {
        super();
        setModel(getAuditorsTableModel());
    }
    
    
    public void addAuditor(String className, String instanceName) {
        Vector<String> row = new Vector(2);
        row.add(className);
        row.add(instanceName);
        getAuditorsTableModel().addRow(row);
    }
    
    
    public void removeAuditor(String className, String instanceName) {
        for (int i = 0; i < getRowCount(); i++) {
            if (getValueAt(i, 0).equals(className)) {
                if (getValueAt(i, 1).equals(instanceName)) {
                    getAuditorsTableModel().removeRow(i);
                    return;
                }
            }
        }
    }
    
    
    public String getSelectedClassName() {
        return getValueAt(getSelectedRow(), 0).toString();
    }
    
    
    public String getSelectedInstanceName() {
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
}
