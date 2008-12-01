package org.cagrid.data.sdkquery41.style.wizard;

import gov.nih.nci.cagrid.common.portal.DocumentChangeAdapter;
import gov.nih.nci.cagrid.data.ui.wizard.AbstractWizardPanel;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import javax.swing.event.DocumentEvent;

import org.cagrid.data.sdkquery41.style.wizard.config.GeneralConfigurationStep;
import org.cagrid.grape.utils.CompositeErrorDialog;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * GeneralConfigurationPanel
 * Prompts the service developer to supply some configuration information
 * which will be used to set up the query processor
 * 
 * @author David
 */
public class GeneralConfigurationPanel extends AbstractWizardPanel {
    
    private ValidationResultModel validationModel = null;
    private GeneralConfigurationStep configuration = null;
    private DocumentChangeAdapter documentChangeListener = null;

    public GeneralConfigurationPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
        super(extensionDescription, info);
        this.validationModel = new DefaultValidationResultModel();
        this.configuration = new GeneralConfigurationStep(info);
        this.documentChangeListener = new DocumentChangeAdapter() {
            public void documentEdited(DocumentEvent e) {
                validateInput();
            }
        };
        initialize();
    }


    public String getPanelShortName() {
        return "Configuration";
    }


    public String getPanelTitle() {
        return "caCORE System Configuration";
    }


    public void update() {
        // This method is (mostly) blank, since it's the first panel to run, 
        // and no other panel changes the values used by this panel
        validateInput();
    }
    
    
    public void movingNext() {
        // called when the 'next' button is clicked
        // causes the configuration to be applied, which involves copying around jars,
        // creating new ones for the config files, etc.
        try {
            configuration.applyConfiguration();
        } catch (Exception ex) {
            ex.printStackTrace();
            CompositeErrorDialog.showErrorDialog("Error applying general configuration", ex.getMessage(), ex);
        }
    }
    
    
    private void initialize() {
        
    }
    
    
    // -----------
    // validation
    // -----------
    
    
    private void configureValidation() {
        // ValidationComponentUtils.setMessageKey(getGuiField(), KEY);
        
        validateInput();
        updateComponentTreeSeverity();
    }
    
    
    private void validateInput() {
        ValidationResult result = new ValidationResult();
        
        // TODO: validation
        
        validationModel.setResult(result);
        
        updateComponentTreeSeverity();
        // update next button enabled
        setNextEnabled(!validationModel.hasErrors());
    }
    
    
    private void updateComponentTreeSeverity() {
        ValidationComponentUtils.updateComponentTreeMandatoryAndBlankBackground(this);
        ValidationComponentUtils.updateComponentTreeSeverityBackground(this, validationModel.getResult());
    }
}
