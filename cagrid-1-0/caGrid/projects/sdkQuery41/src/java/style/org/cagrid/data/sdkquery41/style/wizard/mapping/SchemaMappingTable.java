package org.cagrid.data.sdkquery41.style.wizard.mapping;

import gov.nih.nci.cagrid.data.extension.CadsrInformation;
import gov.nih.nci.cagrid.data.extension.CadsrPackage;
import gov.nih.nci.cagrid.data.extension.ClassMapping;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.cagrid.data.sdkquery41.style.wizard.SchemaMappingValidityListener;
import org.cagrid.data.sdkquery41.style.wizard.config.SchemaMappingConfigStep;

/**
 * Table which shows the mapping between domain model packages
 * and XML schemas, presenting the user with a means to see
 * what's been mapped, and a way to edit the mappings
 * 
 * @author David
 */
public class SchemaMappingTable extends JTable {
    
    private SchemaMappingTableModel tableModel = null;
    
    private ServiceInformation serviceInfo = null;
    private SchemaMappingConfigStep configuration = null;
    private SchemaMappingValidityListener validityListener = null;

    public SchemaMappingTable(ServiceInformation serviceInfo, 
        SchemaMappingConfigStep configuration, SchemaMappingValidityListener validityListener) {
        super();
        this.serviceInfo = serviceInfo;
        this.configuration = configuration;
        this.validityListener = validityListener;
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
        tableModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                SchemaMappingTable.this.validityListener
                    .updateSchemaMappingValidity(!hasErrors());
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
    
    
    public void reloadCadsrInformation() throws Exception {
        // empty the table
        while (tableModel.getRowCount() != 0) {
            tableModel.removeRow(0);
        }
        
        CadsrInformation cadsrInformation = configuration.getCurrentCadsrInformation();
        if (cadsrInformation.getPackages() != null) {
            // sort packages by name
            CadsrPackage[] packages = 
                new CadsrPackage[cadsrInformation.getPackages().length];
            for (int i = 0; i < cadsrInformation.getPackages().length; i++) {
                packages[i] = cadsrInformation.getPackages(i);
            }
            Arrays.sort(packages, new Comparator<CadsrPackage>() {
                public int compare(CadsrPackage p1, CadsrPackage p2) {
                    return p1.getName().compareTo(p2.getName());
                }
            });
            
            // create rows for the packages
            for (CadsrPackage pack : packages) {
                Vector<Object> row = new Vector<Object>();
                row.add(pack.getName());
                PackageMappingStatus status = determineMappingStatus(pack);
                row.add(status);
                SchemaResolutionButton resolutionButton = 
                    new SchemaResolutionButton(serviceInfo, pack, configuration, this);
                row.add(resolutionButton);
                tableModel.addRow(row);
            }
        } else {
            System.out.println("Packages in caDSR information are null!!!!!!!");
        }
    }
    
    
    private PackageMappingStatus determineMappingStatus(CadsrPackage pack) {
        // see if there's even a schema associated witht the package
        if (pack.getMappedNamespace() == null || pack.getMappedNamespace().length() == 0) {
            return PackageMappingStatus.NO_SCHEMA;
        }
        
        // see if the schema associated with this package exists in the service description
        NamespaceType[] serviceNamespaces = serviceInfo.getServiceDescriptor().getNamespaces().getNamespace();
        NamespaceType mappedSchema = null;
        for (NamespaceType namespace : serviceNamespaces) {
            if (namespace.getNamespace().equals(pack.getMappedNamespace())) {
                mappedSchema = namespace;
                break;
            }
        }
        if (mappedSchema == null) {
            return PackageMappingStatus.SCHEMA_NOT_FOUND;
        }
        
        // verify each class has a mapped element which exists in the schema
        for (ClassMapping mapping : pack.getCadsrClass()) {
            String elementName = mapping.getElementName();
            if (elementName == null || elementName.length() == 0) {
                return PackageMappingStatus.MISSING_ELEMENTS;
            }
            // try to find the element name
            boolean found = false;
            for (SchemaElementType type : mappedSchema.getSchemaElement()) {
                if (type.getType().equals(elementName)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return PackageMappingStatus.MISSING_ELEMENTS;
            }
        }
        
        return PackageMappingStatus.OK;
    }
    
    
    public static enum PackageMappingStatus {
        OK, NO_SCHEMA, SCHEMA_NOT_FOUND, MISSING_ELEMENTS;
        
        public String toString() {
            String value = null;
            switch (this) {
                case OK:
                    value = "OK";
                    break;
                case NO_SCHEMA:
                    value = "No Schema Assigned";
                    break;
                case SCHEMA_NOT_FOUND:
                    value = "Schema not found";
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
