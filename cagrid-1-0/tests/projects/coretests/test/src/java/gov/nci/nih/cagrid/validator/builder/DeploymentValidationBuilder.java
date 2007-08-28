package gov.nci.nih.cagrid.validator.builder;

import gov.nih.nci.cagrid.introduce.common.FileFilters;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

/** 
 *  DeploymentValidationBuilder
 *  Utility to build a grid deployment validation test
 * 
 * @author David Ervin
 * 
 * @created Aug 28, 2007 12:14:58 PM
 * @version $Id: DeploymentValidationBuilder.java,v 1.1 2007-08-28 20:38:55 dervin Exp $ 
 */
public class DeploymentValidationBuilder extends JFrame {
    
    private JMenuBar mainMenuBar = null;
    private JMenu fileMenu = null;
    private JMenuItem fileLoadMenuItem = null;
    private JMenuItem fileSaveMenuItem = null;
    private JMenuItem fileSaveAsMenuItem = null;
    private JMenuItem fileExitMenuItem = null;
    private ServiceTable serviceTable = null;
    private JScrollPane serviceTableScrollPane = null;
    private JPanel buttonPanel = null;
    private JButton addServicesButton = null;
    private JButton removeServicesButton = null;
    private JPanel mainPanel = null;
    private SchedulePanel schedulePanel = null;
    
    private File currentDeploymentDescriptionFile = null;

    public DeploymentValidationBuilder() {
        super();
        setTitle("Deployment Validation Builder");
        initialize();
    }
    
    
    private void initialize() {
        this.setContentPane(getMainPanel());
        this.setJMenuBar(getMainMenuBar());
        this.pack();
        this.setSize(new Dimension(525, 375));
        setVisible(true);
    }
    

    /**
     * This method initializes mainMenuBar	
     * 	
     * @return javax.swing.JMenuBar	
     */
    private JMenuBar getMainMenuBar() {
        if (mainMenuBar == null) {
            mainMenuBar = new JMenuBar();
            mainMenuBar.add(getFileMenu());
        }
        return mainMenuBar;
    }


    /**
     * This method initializes fileMenu	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getFileMenu() {
        if (fileMenu == null) {
            fileMenu = new JMenu();
            fileMenu.setText("File");
            fileMenu.add(getFileLoadMenuItem());
            fileMenu.add(getFileSaveMenuItem());
            fileMenu.add(getFileSaveAsMenuItem());
            fileMenu.addSeparator();
            fileMenu.add(getFileExitMenuItem());
        }
        return fileMenu;
    }


    /**
     * This method initializes fileLoadMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getFileLoadMenuItem() {
        if (fileLoadMenuItem == null) {
            fileLoadMenuItem = new JMenuItem();
            fileLoadMenuItem.setText("Load");
            fileLoadMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    JFileChooser chooser = new JFileChooser(currentDeploymentDescriptionFile);
                    chooser.setFileFilter(new FileFilters.XMLFileFilter());
                    int choice = chooser.showOpenDialog(DeploymentValidationBuilder.this);
                    if (choice == JFileChooser.APPROVE_OPTION) {
                        
                    }
                }
            });
        }
        return fileLoadMenuItem;
    }


    /**
     * This method initializes fileSaveMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getFileSaveMenuItem() {
        if (fileSaveMenuItem == null) {
            fileSaveMenuItem = new JMenuItem();
            fileSaveMenuItem.setText("Save");
            fileSaveMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return fileSaveMenuItem;
    }


    /**
     * This method initializes fileSaveAsMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getFileSaveAsMenuItem() {
        if (fileSaveAsMenuItem == null) {
            fileSaveAsMenuItem = new JMenuItem();
            fileSaveAsMenuItem.setText("Save As...");
            fileSaveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return fileSaveAsMenuItem;
    }


    /**
     * This method initializes fileExitMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getFileExitMenuItem() {
        if (fileExitMenuItem == null) {
            fileExitMenuItem = new JMenuItem();
            fileExitMenuItem.setText("Exit");
            fileExitMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return fileExitMenuItem;
    }
    
    
    private ServiceTable getServiceTable() {
        if (serviceTable == null) {
            serviceTable = new ServiceTable();
        }
        return serviceTable;
    }


    /**
     * This method initializes serviceTableScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getServiceTableScrollPane() {
        if (serviceTableScrollPane == null) {
            serviceTableScrollPane = new JScrollPane();
            serviceTableScrollPane.setBorder(BorderFactory.createTitledBorder(null, "Tested Services", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            serviceTableScrollPane.setViewportView(getServiceTable());
        }
        return serviceTableScrollPane;
    }


    /**
     * This method initializes buttonPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints1.gridy = 0;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints.gridy = 0;
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel.add(getAddServicesButton(), gridBagConstraints);
            buttonPanel.add(getRemoveServicesButton(), gridBagConstraints1);
        }
        return buttonPanel;
    }


    /**
     * This method initializes addServicesButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getAddServicesButton() {
        if (addServicesButton == null) {
            addServicesButton = new JButton();
            addServicesButton.setText("Add Service");
            addServicesButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return addServicesButton;
    }


    /**
     * This method initializes removeServicesButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getRemoveServicesButton() {
        if (removeServicesButton == null) {
            removeServicesButton = new JButton();
            removeServicesButton.setText("Remove Selected");
            removeServicesButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return removeServicesButton;
    }


    /**
     * This method initializes mainPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints4.weightx = 1.0D;
            gridBagConstraints4.gridy = 2;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.anchor = GridBagConstraints.EAST;
            gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints3.gridy = 1;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = GridBagConstraints.BOTH;
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.weightx = 1.0;
            gridBagConstraints2.weighty = 1.0D;
            gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints2.gridx = 0;
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.add(getServiceTableScrollPane(), gridBagConstraints2);
            mainPanel.add(getButtonPanel(), gridBagConstraints3);
            mainPanel.add(getSchedulePanel(), gridBagConstraints4);
        }
        return mainPanel;
    }
    
    
    private SchedulePanel getSchedulePanel() {
        if (schedulePanel == null) {
            schedulePanel = new SchedulePanel();
        }
        return schedulePanel;
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Error setting system look and feel");
        }
        JFrame frame = new DeploymentValidationBuilder();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
