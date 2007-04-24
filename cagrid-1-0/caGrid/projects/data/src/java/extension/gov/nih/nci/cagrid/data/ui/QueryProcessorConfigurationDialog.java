package gov.nih.nci.cagrid.data.ui;

import gov.nih.nci.cagrid.data.cql.ui.CQLQueryProcessorConfigUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.projectmobius.portal.PortalResourceManager;

/** 
 *  QueryProcessorConfigurationDialog
 *  Dialog to display and manage a CQL query processor
 *  configuration UI component
 * 
 * @author David Ervin
 * 
 * @created Apr 6, 2007 1:25:32 PM
 * @version $Id: QueryProcessorConfigurationDialog.java,v 1.2 2007-04-24 15:51:07 dervin Exp $ 
 */
public class QueryProcessorConfigurationDialog extends JDialog {
    private CQLQueryProcessorConfigUI configUi = null;
    private Properties configProperties = null;

    private QueryProcessorConfigurationDialog(
        CQLQueryProcessorConfigUI configUi, Properties configProperties) {
        super(PortalResourceManager.getInstance().getGridPortal(), 
            "CQL Processor Configuration", true);
        this.configUi = configUi;
        this.configProperties = configProperties;
        initialize();
    }
    
    
    private void initialize() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints configCons = new GridBagConstraints();
        configCons.gridx = 0;
        configCons.gridy = 0;
        configCons.fill = GridBagConstraints.BOTH;
        configCons.weightx = 1.0D;
        configCons.weighty = 1.0D;
        configCons.insets = new Insets(4, 4, 4, 4);
        configUi.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        mainPanel.add(configUi, configCons);
        JButton doneButton = new JButton();
        doneButton.setText("Done");
        doneButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               dispose();
           }
        });
        GridBagConstraints buttonCons = new GridBagConstraints();
        buttonCons.gridx = 0;
        buttonCons.gridy = 1;
        buttonCons.anchor = GridBagConstraints.EAST;
        buttonCons.insets = new Insets(2, 2, 2, 2);
        mainPanel.add(doneButton, buttonCons);
        configUi.setUpUi(configProperties);
        setContentPane(mainPanel);
    }
    
    
    public static Properties showConfigurationUi(
        CQLQueryProcessorConfigUI configUi, Properties configProperties) {
        JDialog dialog = new QueryProcessorConfigurationDialog(configUi, configProperties);
        dialog.pack();
        if (configUi.getPreferredDimension() != null) {
            dialog.setSize(configUi.getPreferredDimension());
        }
        dialog.setVisible(true);
        return configUi.getConfiguredProperties();
    }
}
