package gov.nih.nci.cagrid.data.ui.auditors;

import gov.nih.nci.cagrid.data.auditing.AuditorConfigurationConfigurationProperties;
import gov.nih.nci.cagrid.data.auditing.ConfigurationProperty;

import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/** 
 *  AuditorConfigurationPropertiesTable
 *  Configures / displays configuration properties for data service auditors
 * 
 * @author David Ervin
 * 
 * @created May 21, 2007 10:58:45 AM
 * @version $Id: AuditorConfigurationPropertiesTable.java,v 1.1 2007-05-21 19:07:57 dervin Exp $ 
 */
public class AuditorConfigurationPropertiesTable extends JTable {

    private DefaultTableModel model;
    
    public AuditorConfigurationPropertiesTable() {
        super();
        setModel(getConfigurationPropertiesModel());
        initialize();
    }
    
    
    private void initialize() {
        this.setSize(new Dimension(348, 61));
        
    }
    
    
    public void setConfigurationProperties(AuditorConfigurationConfigurationProperties props, Properties defaults) {
        while (getRowCount() != 0) {
            getConfigurationPropertiesModel().removeRow(0);
        }
        Enumeration defaultKeys = defaults.keys();
        while (defaultKeys.hasMoreElements()) {
            String key = (String) defaultKeys.nextElement();
            String defaultValue = defaults.getProperty(key);
            String currentValue = defaultValue;
            if (props.getProperty() != null) {
                for (ConfigurationProperty prop : props.getProperty()) {
                    if (prop.getKey().equals(key)) {
                        currentValue = prop.getValue();
                    }
                }
            }
            Vector<String> row = new Vector(3);
            row.add(key);
            row.add(defaultValue);
            row.add(currentValue);
            getConfigurationPropertiesModel().addRow(row);
        }
    }
    
    
    public AuditorConfigurationConfigurationProperties getConfigurationProperties() {
        AuditorConfigurationConfigurationProperties props = 
            new AuditorConfigurationConfigurationProperties();
        ConfigurationProperty[] propArray = new ConfigurationProperty[getRowCount()];
        for (int i = 0; i < getRowCount(); i++) {
            propArray[i] = new ConfigurationProperty(
                getValueAt(i, 0).toString(), getValueAt(i, 2).toString());
        }
        props.setProperty(propArray);
        return props;
    }
    
    
    private DefaultTableModel getConfigurationPropertiesModel() {
        if (model == null) {
            model = new DefaultTableModel() {
                public boolean isCellEditable(int row, int col) {
                    return col == 2;
                }
            };
            model.addColumn("Key");
            model.addColumn("Default");
            model.addColumn("Value");
        }
        return model;
    }
}
