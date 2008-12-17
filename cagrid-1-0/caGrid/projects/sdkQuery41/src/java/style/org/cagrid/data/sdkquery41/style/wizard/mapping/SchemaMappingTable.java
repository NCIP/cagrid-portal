package org.cagrid.data.sdkquery41.style.wizard.mapping;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Table which shows the mapping between domain model packages
 * and XML schemas, presenting the user with a means to see
 * what's been mapped, and a way to edit the mappings
 * 
 * @author David
 */
public class SchemaMappingTable extends JTable {
    
    private SchemaMappingTableModel tableModel = null;

    public SchemaMappingTable() {
        super();
        tableModel = new SchemaMappingTableModel();
        setModel(tableModel);
        setDefaultRenderer(Object.class, new ValidatingTableCellRenderer() {
            protected void validateCell(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                // validate status of the 2nd cell
                if (column == 1) {
                    if (value.equals(PackageMappingStatus.MISSING_ELEMENTS) || 
                        value.equals(PackageMappingStatus.NO_SCHEMA)) {
                        setErrorBackground();
                    }
                }
            }
        });
        setDefaultEditor(Object.class, new ComponentTableCellEditor());
    }
    
    
    public boolean hasErrors() {
        boolean errors = false;
        for (int i = 0; i < tableModel.getRowCount() && !errors; i++) {
            Object status = tableModel.getValueAt(i, 1);
            if (!PackageMappingStatus.OK.equals(status)) {
                errors = true;
            }
        }
        return errors;
    }
    
    
    public static enum PackageMappingStatus {
        OK, NO_SCHEMA, MISSING_ELEMENTS;
        
        public String toString() {
            String value = null;
            switch (this) {
                case OK:
                    value = "OK";
                    break;
                case NO_SCHEMA:
                    value = "No Schema Assigned";
                    break;
                case MISSING_ELEMENTS:
                    value = "Missing Elements";
                    break;
                default:
                    throw new IllegalArgumentException("Unknown value " + this.name());
            }
            return value;
        }
    }
    
    
    private static class SchemaMappingTableModel extends DefaultTableModel {

        public SchemaMappingTableModel() {
            addColumn("Package Name");
            addColumn("Status");
            addColumn("Manual Resolution");
        }


        public boolean isCellEditable(int row, int column) {
            return column == 2;
        }
    }
}
