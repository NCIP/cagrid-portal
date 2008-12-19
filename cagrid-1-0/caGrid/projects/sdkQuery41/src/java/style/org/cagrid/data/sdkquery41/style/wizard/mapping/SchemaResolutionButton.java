package org.cagrid.data.sdkquery41.style.wizard.mapping;

import gov.nih.nci.cagrid.data.extension.CadsrPackage;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.cagrid.data.sdkquery41.style.wizard.config.SchemaMappingConfigStep;

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
        // TODO: map schema to package
        System.out.println("Button clicked");
    }
}