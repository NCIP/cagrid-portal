package org.cagrid.installer.steps;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;


public class ObtainHostCredentialsStep extends PanelWizardStep {

    private JLabel hostLabel = null;


    /**
     * This method initializes
     */
    public ObtainHostCredentialsStep() {
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
        hostLabel.setText("Request Host Credentials for");
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(442, 204));

        this.add(hostLabel, gridBagConstraints);
    }


    public void init(WizardModel m) {
        CaGridInstallerModel model = (CaGridInstallerModel) m;
        super.init(m);

        hostLabel.setText(hostLabel.getText() + model.getProperty(Constants.SERVICE_HOSTNAME));
    }

} // @jve:decl-index=0:visual-constraint="16,21"
