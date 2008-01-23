package gov.nih.nci.cagrid.sdkquery4.style.wizard;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.extension.CadsrPackage;
import gov.nih.nci.cagrid.data.ui.wizard.CacoreWizardUtils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import com.jgoodies.validation.view.ValidationComponentUtils;

/** 
 *  PackageToNamespaceTable
 *  Table for mapping cadsr packages to schema
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Sep 26, 2006 
 * @version $Id: PackageToNamespaceTable.java,v 1.1 2008-01-23 19:59:19 dervin Exp $ 
 */
public class PackageToNamespaceTable extends JTable {
    
    private SchemaResolutionHandler resolutionHandler = null;
    
    public PackageToNamespaceTable() {
        setModel(new PackageToNamespaceTableModel());
        setDefaultRenderer(Object.class, new PackageToNamespaceTableRenderer());
        setDefaultEditor(Object.class, new PackageToNamespaceTableEditor());
    }
    
    
    public void setSchemaResolutionHandler(SchemaResolutionHandler handler) {
        this.resolutionHandler = handler;
    }
    
    
    public SchemaResolutionHandler getSchemaResolutionHandler() {
        return this.resolutionHandler;
    }


    public boolean isPackageInTable(CadsrPackage info) {
        for (int i = 0; i < getRowCount(); i++) {
            if (info.getName().equals(getValueAt(i, 0)) 
                && info.getMappedNamespace().equals(getValueAt(i, 1))) {
                return true;
            }
        }
        return false;
    }


    public void addNewCadsrPackage(ServiceInformation serviceInfo, CadsrPackage pack) {
        Vector<Object> row = new Vector<Object>(4);
        row.add(pack.getName());
        row.add(pack.getMappedNamespace());
        row.add(SchemaResolutionStatus.NEVER_TRIED);
        row.add(getResolveButton(serviceInfo, pack));

        ((DefaultTableModel) getModel()).addRow(row);
    }


    public void removeCadsrPackage(String packName) {
        for (int i = 0; i < getRowCount(); i++) {
            if (getValueAt(i, 0).equals(packName)) {
                ((DefaultTableModel) getModel()).removeRow(i);
                break;
            }
        }
    }


    /**
     * Creates a new JButton to handle schema resolution
     * @param pack
     * @return
     *      A JButton to resolve schemas
     */
    private JButton getResolveButton(
        final ServiceInformation serviceInfo, final CadsrPackage pack) {
        JButton resolveButton = new JButton("Resolve");
        resolveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (resolutionHandler != null) {
                    // find the table row for this button / package
                    int row = 0;
                    while (getValueAt(row, 3) != e.getSource()) {
                        row++;
                    } 

                    // figure out what the current status is
                    SchemaResolutionStatus currentStatus = 
                        (SchemaResolutionStatus) getValueAt(row, 2);
                    if (currentStatus == SchemaResolutionStatus.SCHEMA_FOUND) {
                        String[] message = {
                            "This package already has a schema associated with it.",
                            "Replace the schema with a different one?"
                        };
                        int choice = JOptionPane.showConfirmDialog(
                            (JButton) e.getSource(), message, "Replace?", JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            // remove associated schema and namespace types
                            removeAssociatedSchema(serviceInfo, pack);
                        } else {
                            return;
                        }
                    }

                    // invoke the resolution handler
                    SchemaResolutionStatus resolutionStatus = 
                        resolutionHandler.resolveSchemaForPackage(serviceInfo, pack.getName());
                    
                    // change status
                    setValueAt(resolutionStatus, row, 2);
                } else {
                    throw new IllegalStateException("No schema resolution handler available");
                }
            }
        });
        return resolveButton;
    }


    private void removeAssociatedSchema(ServiceInformation info, CadsrPackage pack) {
        // get the schema directory for the service
        String serviceName = info.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
        String schemaDir = CacoreWizardUtils.getServiceBaseDir(info) + File.separator + "schema" + File.separator + serviceName;
        // get the namespace type from the service information
        NamespaceType[] namespaces = info.getNamespaces().getNamespace();
        for (int i = 0; i < namespaces.length; i++) {
            if (namespaces[i].getNamespace().equals(pack.getMappedNamespace())) {
                NamespaceType delme = namespaces[i];
                File schemaFile = new File(schemaDir + File.separator + delme.getLocation());
                schemaFile.delete();
                namespaces = (NamespaceType[]) Utils.removeFromArray(namespaces, delme);
                break;
            }
        }
        info.getNamespaces().setNamespace(namespaces);
    }


    private static class PackageToNamespaceTableModel extends DefaultTableModel {

        public PackageToNamespaceTableModel() {
            addColumn("Package Name");
            addColumn("Namespace");
            addColumn("Status");
            addColumn("Manual Resolution");
        }


        public boolean isCellEditable(int row, int column) {
            return column == 3;
        }
    }


    private static class PackageToNamespaceTableRenderer extends DefaultTableCellRenderer {
        
        private Color defaultBackground = null;
        
        public PackageToNamespaceTableRenderer() {
            super();
            defaultBackground = getBackground();
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
            // return swing components as they are
            if (value instanceof Component) {
                return (Component) value;
            }
            
            // restore the default background
            setBackground(defaultBackground);

            // validation coloring on status column
            if (column == 2) {
                if (value.equals(SchemaResolutionStatus.NEVER_TRIED)) {
                    setBackground(ValidationComponentUtils.getErrorBackground());
                } else if (value.equals(SchemaResolutionStatus.MAPPING_ERROR)) {
                    setBackground(ValidationComponentUtils.getWarningBackground());
                }
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }


    private static class PackageToNamespaceTableEditor extends AbstractCellEditor implements TableCellEditor {

        private Object editorValue = null;

        public PackageToNamespaceTableEditor() {
            editorValue = null;
        }


        public Object getCellEditorValue() {
            return editorValue;
        }


        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            editorValue = value;
            return (Component) value;
        }
    }
}
