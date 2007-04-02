package gov.nih.nci.cagrid.data.ui;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.data.utilities.dmviz.DomainModelVisualizationPanel;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.projectmobius.portal.PortalResourceManager;

/** 
 *  DomainModelVisualizationDialog
 *  Dialog to visually display a Data Service's domain model
 * 
 * @author David Ervin
 * 
 * @created Apr 2, 2007 2:02:12 PM
 * @version $Id: DomainModelVisualizationDialog.java,v 1.1 2007-04-02 18:31:35 dervin Exp $ 
 */
public class DomainModelVisualizationDialog extends JDialog {

    private DomainModelVisualizationPanel dmVizPanel = null;
    private JButton okButton = null;
    private JPanel mainPanel = null;
    
    public DomainModelVisualizationDialog(DomainModel model) {
        super(PortalResourceManager.getInstance().getGridPortal(), "Domain Model", false);
        getDmVizPanel().setDomainModel(model);
        initialize();
    }
    
    
    private void initialize() {
        setContentPane(getMainPanel());
        pack();
        setSize(500, 500);
        PortalUtils.centerWindow(this);
        setVisible(true);
    }
    
    
    private DomainModelVisualizationPanel getDmVizPanel() {
        if (dmVizPanel == null) {
            dmVizPanel = new DomainModelVisualizationPanel();
        }
        return dmVizPanel;
    }


    /**
     * This method initializes okButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton();
            okButton.setText("OK");
            okButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setVisible(false);
                    dispose();
                }
            });
        }
        return okButton;
    }


    /**
     * This method initializes mainPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.insets = new Insets(0, 2, 2, 2);
            gridBagConstraints1.gridy = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new Insets(6, 6, 6, 6);
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0D;
            gridBagConstraints.weighty = 1.0D;
            gridBagConstraints.gridy = 0;
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.add(getDmVizPanel(), gridBagConstraints);
            mainPanel.add(getOkButton(), gridBagConstraints1);
        }
        return mainPanel;
    }
}
