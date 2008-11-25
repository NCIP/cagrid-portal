package gov.nih.nci.cagrid.data.ui.domain;

import gov.nih.nci.cagrid.data.utilities.dmviz.DomainModelVisualizationPanel;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.FileReader;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

/** 
 *  DomainModelVisualizer
 *  TODO:DOCUMENT ME
 * 
 * @author David Ervin
 * 
 * @created Mar 19, 2008 11:13:47 AM
 * @version $Id: DomainModelVisualizer.java,v 1.2 2008-11-25 15:55:20 dervin Exp $ 
 */
public class DomainModelVisualizer extends JFrame {
    
    private DomainModelVisualizationPanel dmVizPanel = null;
    private JButton loadButton = null;
    private JButton closeButton = null;
    private JPanel buttonPanel = null;
    private JPanel mainPanel = null;

    public DomainModelVisualizer() {
        super();
        setTitle("Domain Model Visualizer");
        initialize();
    }
    
    
    private void initialize() {
        this.setContentPane(getMainPanel());
        setSize(600,600);
    }
    
    
    private DomainModelVisualizationPanel getDmVizPanel() {
        if (this.dmVizPanel == null) {
            this.dmVizPanel = new DomainModelVisualizationPanel();
            dmVizPanel.setBorder(BorderFactory.createTitledBorder(
                null, "Domain Model", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
        }
        return this.dmVizPanel;
    }
    
    
    /**
     * This method initializes loadButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getLoadButton() {
        if (loadButton == null) {
            loadButton = new JButton();
            loadButton.setText("Load File");
            loadButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileFilter(FileFilters.XML_FILTER);
                    int choice = chooser.showOpenDialog(DomainModelVisualizer.this);
                    if (choice == JFileChooser.APPROVE_OPTION) {
                        try {
                            FileReader reader = new FileReader(chooser.getSelectedFile());
                            DomainModel model = MetadataUtils.deserializeDomainModel(reader);
                            reader.close();
                            getDmVizPanel().setDomainModel(model);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        }
        return loadButton;
    }


    /**
     * This method initializes closeButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getCloseButton() {
        if (closeButton == null) {
            closeButton = new JButton();
            closeButton.setText("Close");
            closeButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    dispose();
                }
            });
        }
        return closeButton;
    }


    /**
     * This method initializes buttonPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(1);
            gridLayout.setHgap(4);
            gridLayout.setColumns(2);
            buttonPanel = new JPanel();
            buttonPanel.setLayout(gridLayout);
            buttonPanel.add(getLoadButton(), null);
            buttonPanel.add(getCloseButton(), null);
        }
        return buttonPanel;
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
            gridBagConstraints1.anchor = GridBagConstraints.EAST;
            gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints1.gridy = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0D;
            gridBagConstraints.weighty = 1.0D;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints.gridy = 0;
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.add(getDmVizPanel(), gridBagConstraints);
            mainPanel.add(getButtonPanel(), gridBagConstraints1);
        }
        return mainPanel;
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Error setting system look and feel: " + ex.getMessage());
        }
        DomainModelVisualizer viz = new DomainModelVisualizer();
        viz.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viz.setVisible(true);
    }
}
