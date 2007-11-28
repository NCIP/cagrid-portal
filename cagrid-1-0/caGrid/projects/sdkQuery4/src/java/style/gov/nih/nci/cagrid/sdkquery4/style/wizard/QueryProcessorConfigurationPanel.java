package gov.nih.nci.cagrid.sdkquery4.style.wizard;

import gov.nih.nci.cagrid.data.ui.wizard.AbstractWizardPanel;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JRadioButton;

/** 
 *  QueryProcessorConfigurationPanel
 *  Panel to configure the caCORE SDK Query Processor
 * 
 * @author David Ervin
 * 
 * @created Nov 27, 2007 4:50:32 PM
 * @version $Id: QueryProcessorConfigurationPanel.java,v 1.1 2007-11-28 17:26:57 dervin Exp $ 
 */
public class QueryProcessorConfigurationPanel extends AbstractWizardPanel {

    private JLabel applicationNameLabel = null;
    private JTextField applicationNameTextField = null;
    private JLabel beansJarLabel = null;
    private JTextField beansJarTextField = null;
    private JButton beansBrowseButton = null;
    private JLabel configDirLabel = null;
    private JTextField configDirTextField = null;
    private JButton configBrowseButton = null;
    private JPanel basicConfigPanel = null;
    private JRadioButton localApiRadioButton = null;
    private JRadioButton remoteApiRadioButton = null;
    private JLabel ormJarLabel = null;
    private JTextField ormJarTextField = null;
    private JButton ormJarBrowseButton = null;
    private JLabel hostNameLabel = null;
    private JTextField hostNameTextField = null;
    private JLabel portLabel = null;
    private JTextField portTextField = null;
    private JPanel localApiPanel = null;
    private JPanel remoteApiPanel = null;
    private JPanel apiConfigPanel = null;
    private JPanel mainPanel = null;  //  @jve:decl-index=0:visual-constraint="58,30"


    /**
     * @param extensionDescription
     * @param info
     */
    public QueryProcessorConfigurationPanel(ServiceExtensionDescriptionType extensionDescription,
        ServiceInformation info) {
        super(extensionDescription, info);        
    }


    public String getPanelShortName() {
        return "Configuration";
    }


    public String getPanelTitle() {
        return "caCORE Query Processor Configuration";
    }


    public void update() {
        // TODO Auto-generated method stub

    }


    /**
     * This method initializes applicationNameLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getApplicationNameLabel() {
        if (applicationNameLabel == null) {
            applicationNameLabel = new JLabel();
            applicationNameLabel.setText("Application Name:");
        }
        return applicationNameLabel;
    }


    /**
     * This method initializes applicationNameTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getApplicationNameTextField() {
        if (applicationNameTextField == null) {
            applicationNameTextField = new JTextField();
        }
        return applicationNameTextField;
    }


    /**
     * This method initializes beansJarLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getBeansJarLabel() {
        if (beansJarLabel == null) {
            beansJarLabel = new JLabel();
            beansJarLabel.setText("Beans Jar:");
        }
        return beansJarLabel;
    }


    /**
     * This method initializes beansJarTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getBeansJarTextField() {
        if (beansJarTextField == null) {
            beansJarTextField = new JTextField();
            beansJarTextField.setEditable(false);
        }
        return beansJarTextField;
    }


    /**
     * This method initializes beansBrowseButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getBeansBrowseButton() {
        if (beansBrowseButton == null) {
            beansBrowseButton = new JButton();
            beansBrowseButton.setText("Browse");
            beansBrowseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return beansBrowseButton;
    }


    /**
     * This method initializes configDirLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getConfigDirLabel() {
        if (configDirLabel == null) {
            configDirLabel = new JLabel();
            configDirLabel.setText("Config Directory:");
        }
        return configDirLabel;
    }


    /**
     * This method initializes configDirTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getConfigDirTextField() {
        if (configDirTextField == null) {
            configDirTextField = new JTextField();
            configDirTextField.setEditable(false);
        }
        return configDirTextField;
    }


    /**
     * This method initializes configBrowseButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getConfigBrowseButton() {
        if (configBrowseButton == null) {
            configBrowseButton = new JButton();
            configBrowseButton.setText("Browse");
            configBrowseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return configBrowseButton;
    }


    /**
     * This method initializes basicConfigPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getBasicConfigPanel() {
        if (basicConfigPanel == null) {
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.gridx = 2;
            gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints7.gridy = 2;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints6.gridy = 2;
            gridBagConstraints6.weightx = 1.0;
            gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints6.gridx = 1;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints5.gridy = 2;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 2;
            gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints4.gridy = 1;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.gridy = 1;
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints3.gridx = 1;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints2.gridy = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints1.gridwidth = 2;
            gridBagConstraints1.gridx = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridy = 0;
            basicConfigPanel = new JPanel();
            basicConfigPanel.setLayout(new GridBagLayout());
            basicConfigPanel.add(getApplicationNameLabel(), gridBagConstraints);
            basicConfigPanel.add(getApplicationNameTextField(), gridBagConstraints1);
            basicConfigPanel.add(getBeansJarLabel(), gridBagConstraints2);
            basicConfigPanel.add(getBeansJarTextField(), gridBagConstraints3);
            basicConfigPanel.add(getBeansBrowseButton(), gridBagConstraints4);
            basicConfigPanel.add(getConfigDirLabel(), gridBagConstraints5);
            basicConfigPanel.add(getConfigDirTextField(), gridBagConstraints6);
            basicConfigPanel.add(getConfigBrowseButton(), gridBagConstraints7);
        }
        return basicConfigPanel;
    }


    /**
     * This method initializes localApiRadioButton	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getLocalApiRadioButton() {
        if (localApiRadioButton == null) {
            localApiRadioButton = new JRadioButton();
            localApiRadioButton.setText("Local API");
            localApiRadioButton.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    System.out.println("itemStateChanged()"); // TODO Auto-generated Event stub itemStateChanged()
                }
            });
        }
        return localApiRadioButton;
    }


    /**
     * This method initializes remoteApiRadioButton	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getRemoteApiRadioButton() {
        if (remoteApiRadioButton == null) {
            remoteApiRadioButton = new JRadioButton();
            remoteApiRadioButton.setText("Remote API");
            remoteApiRadioButton.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    System.out.println("itemStateChanged()"); // TODO Auto-generated Event stub itemStateChanged()
                }
            });
        }
        return remoteApiRadioButton;
    }


    /**
     * This method initializes ormJarLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getOrmJarLabel() {
        if (ormJarLabel == null) {
            ormJarLabel = new JLabel();
            ormJarLabel.setText("ORM Jar:");
        }
        return ormJarLabel;
    }


    /**
     * This method initializes ormJarTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getOrmJarTextField() {
        if (ormJarTextField == null) {
            ormJarTextField = new JTextField();
            ormJarTextField.setEditable(false);
        }
        return ormJarTextField;
    }


    /**
     * This method initializes ormJarBrowseButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getOrmJarBrowseButton() {
        if (ormJarBrowseButton == null) {
            ormJarBrowseButton = new JButton();
            ormJarBrowseButton.setText("Browse");
            ormJarBrowseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return ormJarBrowseButton;
    }


    /**
     * This method initializes hostNameLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getHostNameLabel() {
        if (hostNameLabel == null) {
            hostNameLabel = new JLabel();
            hostNameLabel.setText("Host Name:");
        }
        return hostNameLabel;
    }


    /**
     * This method initializes hostNameTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getHostNameTextField() {
        if (hostNameTextField == null) {
            hostNameTextField = new JTextField();
        }
        return hostNameTextField;
    }


    /**
     * This method initializes portLabel	
     * 	
     * @return javax.swing.JLabel	
     */
    private JLabel getPortLabel() {
        if (portLabel == null) {
            portLabel = new JLabel();
            portLabel.setText("Port:");
        }
        return portLabel;
    }


    /**
     * This method initializes portTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getPortTextField() {
        if (portTextField == null) {
            portTextField = new JTextField();
        }
        return portTextField;
    }


    /**
     * This method initializes localApiPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getLocalApiPanel() {
        if (localApiPanel == null) {
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 2;
            gridBagConstraints10.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints10.gridy = 0;
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints9.gridy = 0;
            gridBagConstraints9.weightx = 1.0;
            gridBagConstraints9.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints9.gridx = 1;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 0;
            gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints8.gridy = 0;
            localApiPanel = new JPanel();
            localApiPanel.setLayout(new GridBagLayout());
            localApiPanel.add(getOrmJarLabel(), gridBagConstraints8);
            localApiPanel.add(getOrmJarTextField(), gridBagConstraints9);
            localApiPanel.add(getOrmJarBrowseButton(), gridBagConstraints10);
        }
        return localApiPanel;
    }


    /**
     * This method initializes remoteApiPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getRemoteApiPanel() {
        if (remoteApiPanel == null) {
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints14.gridy = 1;
            gridBagConstraints14.weightx = 1.0;
            gridBagConstraints14.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints14.gridx = 1;
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.gridx = 0;
            gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints13.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints13.gridy = 1;
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints12.gridy = 0;
            gridBagConstraints12.weightx = 1.0;
            gridBagConstraints12.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints12.gridx = 1;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints11.gridy = 0;
            remoteApiPanel = new JPanel();
            remoteApiPanel.setLayout(new GridBagLayout());
            remoteApiPanel.add(getHostNameLabel(), gridBagConstraints11);
            remoteApiPanel.add(getHostNameTextField(), gridBagConstraints12);
            remoteApiPanel.add(getPortLabel(), gridBagConstraints13);
            remoteApiPanel.add(getPortTextField(), gridBagConstraints14);
        }
        return remoteApiPanel;
    }


    /**
     * This method initializes apiConfigPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getApiConfigPanel() {
        if (apiConfigPanel == null) {
            GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
            gridBagConstraints18.gridx = 1;
            gridBagConstraints18.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints18.weightx = 1.0D;
            gridBagConstraints18.gridy = 1;
            GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
            gridBagConstraints17.gridx = 1;
            gridBagConstraints17.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints17.weightx = 1.0D;
            gridBagConstraints17.gridy = 0;
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.gridx = 0;
            gridBagConstraints16.anchor = GridBagConstraints.NORTH;
            gridBagConstraints16.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints16.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints16.gridy = 1;
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.gridx = 0;
            gridBagConstraints15.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints15.anchor = GridBagConstraints.NORTH;
            gridBagConstraints15.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints15.gridy = 0;
            apiConfigPanel = new JPanel();
            apiConfigPanel.setLayout(new GridBagLayout());
            apiConfigPanel.add(getLocalApiRadioButton(), gridBagConstraints15);
            apiConfigPanel.add(getRemoteApiRadioButton(), gridBagConstraints16);
            apiConfigPanel.add(getLocalApiPanel(), gridBagConstraints17);
            apiConfigPanel.add(getRemoteApiPanel(), gridBagConstraints18);
        }
        return apiConfigPanel;
    }


    /**
     * This method initializes mainPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
            gridBagConstraints20.gridx = 0;
            gridBagConstraints20.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints20.weightx = 1.0D;
            gridBagConstraints20.gridy = 1;
            GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
            gridBagConstraints19.gridx = 0;
            gridBagConstraints19.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints19.weightx = 1.0D;
            gridBagConstraints19.gridy = 0;
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.setSize(new Dimension(452, 217));
            mainPanel.add(getBasicConfigPanel(), gridBagConstraints19);
            mainPanel.add(getApiConfigPanel(), gridBagConstraints20);
        }
        return mainPanel;
    }
}
