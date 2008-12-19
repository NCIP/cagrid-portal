package org.cagrid.data.sdkquery41.style.wizard.mapping;

import gov.nih.nci.cagrid.data.extension.CadsrPackage;
import gov.nih.nci.cagrid.data.ui.SchemaResolutionDialog;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.cagrid.data.sdkquery41.style.wizard.config.SchemaMappingConfigStep;
import org.cagrid.grape.utils.CompositeErrorDialog;

public class SchemaResolutionButton extends JButton implements ActionListener {
    
    private ServiceInformation serviceInfo = null;
    private CadsrPackage cadsrPack = null;
    private SchemaMappingConfigStep configuration = null;
    
    public SchemaResolutionButton(ServiceInformation serviceInfo, CadsrPackage cadsrPack, 
        SchemaMappingConfigStep configuration) {
        super();
        this.serviceInfo = serviceInfo;
        this.cadsrPack = cadsrPack;
        this.configuration = configuration;
        this.setName("Map Schema");
        addActionListener(this);
    }
    
    
    public void actionPerformed(ActionEvent e) {
        // use the schema resolution dialog to load an XSD
        NamespaceType[] namespaces = SchemaResolutionDialog.resolveSchemas(serviceInfo);
        if (namespaces != null && namespaces.length != 0) {
            // only the first namespace is the one selected; the rest are imports
            MappingCustomizationDialog.customizeElementMapping(namespaces[0], cadsrPack, configuration);
            try {
                configuration.storeCustomMapping(cadsrPack);
            } catch (Exception ex) {
                ex.printStackTrace();
                CompositeErrorDialog.showErrorDialog("Error storing mapping", ex.getMessage(), ex);
            }
        }
    }
}