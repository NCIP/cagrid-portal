package org.cagrid.installer.steps;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;


public class DorianObtainHostCredentialsStep extends PanelWizardStep {
    private CaGridInstallerModel model  = null;  //  @jve:decl-index=0:
    private JLabel hostLabel = null;


    /**
     * This method initializes
     */
    public DorianObtainHostCredentialsStep() {
        super();
        initialize();
    }


    /**
     * This method initializes this
     */
    private void initialize() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        hostLabel = new JLabel();
        hostLabel.setText("Request host credentials for");
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(442, 204));

        this.add(hostLabel, gridBagConstraints);
    }


    public void init(WizardModel m) {
        model= (CaGridInstallerModel) m;
        super.init(m);

        
    }


    @Override
    public void prepare() {
        super.prepare();
        
        hostLabel.setText(hostLabel.getText() + " " + model.getProperty(Constants.SERVICE_HOSTNAME));
        
        
        
        
        setComplete(true);
        
        
    }
    
    

} // @jve:decl-index=0:visual-constraint="16,21"
