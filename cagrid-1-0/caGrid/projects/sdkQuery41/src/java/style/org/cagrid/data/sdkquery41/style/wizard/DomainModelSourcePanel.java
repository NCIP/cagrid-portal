package org.cagrid.data.sdkquery41.style.wizard;

import gov.nih.nci.cagrid.data.extension.CadsrInformation;

import javax.swing.JPanel;

/**
 * JPanel which is required to produce a caGrid Domain Model
 * 
 * @author David
 */
public abstract class DomainModelSourcePanel extends JPanel {
    
    public DomainModelSourcePanel() {
        super();
    }
    
    
    public abstract String getName();
    
    
    public abstract void populateFromConfiguration();
    
    
    public abstract CadsrInformation getCadsrDomainInformation() throws Exception;
}
