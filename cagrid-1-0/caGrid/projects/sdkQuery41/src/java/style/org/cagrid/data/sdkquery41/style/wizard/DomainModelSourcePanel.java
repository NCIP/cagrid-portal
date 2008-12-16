package org.cagrid.data.sdkquery41.style.wizard;

import gov.nih.nci.cagrid.data.extension.CadsrInformation;

import javax.swing.JPanel;

/**
 * JPanel which is required to produce a caGrid Domain Model
 * 
 * @author David
 */
public abstract class DomainModelSourcePanel extends JPanel {
    
    private DomainModelSourceValidityListener validityListener = null;
    
    public DomainModelSourcePanel(DomainModelSourceValidityListener validityListener) {
        super();
        this.validityListener = validityListener;
    }
    
    
    public abstract String getName();
    
    
    public abstract void populateFromConfiguration();
    
    
    public abstract CadsrInformation getCadsrDomainInformation() throws Exception;
    
    
    protected void setModelValidity(boolean valid) {
        validityListener.domainModelSourceValid(this, valid);
    }
}
