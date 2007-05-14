package org.cagrid.rav;


import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.CreationExtensionUIDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JDialog;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.axis.message.MessageElement;
import org.ggf.schemas.jsdl._2005._11.jsdl.Application_Type;
import java.io.File;
import java.awt.Dimension;
import javax.swing.JCheckBox;
import javax.swing.JTable;

public class ApplicationServicePanel extends CreationExtensionUIDialog  {
//public class ApplicationServicePanel extends JDialog {

	private static final long serialVersionUID = 1L;
	public static final String RAV_EXTENSION = "rav";  //  @jve:decl-index=0:
	private JPanel browserPanel = null;
	
	private JLabel jLabel = null;
	private JTextField jTextField = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	
	private JLabel jLabel1 = null;
	private JTextField jTextField1 = null;
	private JLabel jLabel2 = null;
	private JTextField jTextField2 = null;
	private JLabel jLabel3 = null;
	private JTextField jTextField3 = null;
	
	private File appName = null;
	
	private JFileChooser fileChooser = null;
	
	private Application_Type appType = null;  //  @jve:decl-index=0:
	private JCheckBox jCheckBox = null;
	private JLabel jLabel4 = null;
	
	
	// Args Panel
	private JPanel argsPanel = null;
    private JPanel commandArgsTableContainerPanel = null;
    private JScrollPane commandArgsTableScrollPane = null;
    private CommandArgsTable commandArgsTable = null;
    private JButton addCommandArgsButton = null;
    private JButton removeCommandArgsButton = null;
    private JTextField commandArgsKeyTextField = null;
    private JTextField commandArgsValueTextField = null;
    private JLabel commandArgsKeyLabel = null;
    private JLabel commandArgsValueLabel = null;
    private JPanel commandArgsButtonPanel = null;
    private JPanel commandArgsControlPanel = null;
	/**
	 * This is the default constructor
	 */
	public ApplicationServicePanel(Frame f, 
			ServiceExtensionDescriptionType desc, ServiceInformation info) {
		
		super(f, desc, info);//super(f, true);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints1.weightx = 1.0D;
		gridBagConstraints1.weighty = 1.0D;
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints.gridy = 0;
		this.setSize(536, 245);
		this.setLayout(new GridBagLayout());
		this.setName("ApplicationServicePanel");
		
		JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Application Information", null,
        				  getBrowserPanel(),
                          "desc"); //tooltip text
        tabbedPane.addTab("Arguments", null,
        				  getArgsPanel(),
                          "test"); //tooltip text

		
		//this.add(getBrowserPanel(), gridBagConstraints);
		//this.add(getArgsPanel(), gridBagConstraints1);
		this.setTitle("Remote Application Virtualization Environment");
		appType = new Application_Type();
		this.setContentPane(tabbedPane);
	}

	
	
	/**
	 * This method initializes browserPanel	
	 * BrowserPanel is the first tab
	 * 	
	 * @return javax.swing.JPanel	
	 * 
	 */
	private JPanel getBrowserPanel() {
		if (browserPanel == null) {
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 1;
			gridBagConstraints14.gridy = 5;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.gridy = 4;
			jLabel4 = new JLabel();
			jLabel4.setText("Deploy as GRAM Job");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.gridy = 4;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 2;
			gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints4.gridwidth = 1;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.weightx = 1.0;
			
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints5.gridwidth = 1;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 1;
			
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
			
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.weightx = 1.0;
			
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.gridy = 2;
			gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
			
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 3;
			gridBagConstraints9.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.weightx = 1.0;
			
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.gridy = 3;
			gridBagConstraints10.insets = new Insets(2, 2, 2, 2);
			
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.gridy = 2;
			gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.weightx = 1.0;
			
			jLabel = new JLabel();
			jLabel.setText("Application/Executable");
			jLabel1 = new JLabel();
			jLabel1.setText("Application Description");
			
			jLabel2 = new JLabel();
			jLabel2.setText("Application Version");
			
			jLabel3 = new JLabel();
			jLabel3.setText("Extensions");
			browserPanel = new JPanel();
			browserPanel.setLayout(new GridBagLayout());
			browserPanel.add(jLabel, gridBagConstraints3);
			browserPanel.add(getJTextField(), gridBagConstraints2);
			
			browserPanel.add(jLabel1, gridBagConstraints6);
			browserPanel.add(getJTextField1(), gridBagConstraints7);
			
			browserPanel.add(jLabel2, gridBagConstraints8);
			browserPanel.add(getJTextField2(), gridBagConstraints9);
			
			browserPanel.add(jLabel3, gridBagConstraints10);
			browserPanel.add(getJTextField3(), gridBagConstraints11);
			
			browserPanel.add(getJButton(), gridBagConstraints4);
			browserPanel.add(getJCheckBox(), gridBagConstraints12);
			browserPanel.add(jLabel4, gridBagConstraints13);
			browserPanel.add(getJButton1(), gridBagConstraints14);
		}
		return browserPanel;
	}

	/**
	 * This method initializes argsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getArgsPanel() {
		if (argsPanel == null) {
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = GridBagConstraints.BOTH;
			gridBagConstraints15.gridy = 0;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.weighty = 1.0;
			gridBagConstraints15.gridx = 0;
			

            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.gridx = 0;
            gridBagConstraints16.fill = GridBagConstraints.BOTH;
            gridBagConstraints16.gridy = 1;
            
            
			argsPanel = new JPanel();
			argsPanel.setLayout(new GridBagLayout());
			argsPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			argsPanel.add(getCommandArgsTableContainerPanel(), gridBagConstraints15);
			argsPanel.add(getCommandArgsControlPanel(), gridBagConstraints16);
			
			
		}
		return argsPanel;
	}



    /**
     * This method initializes servicePropertiesTableContainerPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getCommandArgsTableContainerPanel() {
        if (commandArgsTableContainerPanel == null) {
            GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
            gridBagConstraints28.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints28.gridx = 0;
            gridBagConstraints28.gridy = 0;
            gridBagConstraints28.weightx = 1.0;
            gridBagConstraints28.weighty = 1.0;
            gridBagConstraints28.insets = new java.awt.Insets(5, 5, 5, 5);
            commandArgsTableContainerPanel = new JPanel();
            commandArgsTableContainerPanel.setLayout(new GridBagLayout());
            commandArgsTableContainerPanel.add(getCommandArgsTableScrollPane(), gridBagConstraints28);
        }
        return commandArgsTableContainerPanel;
    }


    /**
     * This method initializes servicePropertiesTableScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getCommandArgsTableScrollPane() {
        if (commandArgsTableScrollPane == null) {
        	commandArgsTableScrollPane = new JScrollPane();
        	commandArgsTableScrollPane.setViewportView(getCommandArgsTable());
        }
        return commandArgsTableScrollPane;
    }


    /**
     * This method initializes servicePropertiesTable
     * 
     * @return javax.swing.JTable
     */
    private CommandArgsTable getCommandArgsTable() {
        if (commandArgsTable == null) {
        	commandArgsTable = new CommandArgsTable(/*info*/);
        }
        return commandArgsTable;
    }

    
    
    private JPanel getCommandArgsControlPanel() {
        if (commandArgsControlPanel == null) {

             GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
             gridBagConstraints42.gridx = 1;
             gridBagConstraints42.fill = java.awt.GridBagConstraints.BOTH;
             gridBagConstraints42.gridwidth = 1;
             gridBagConstraints42.gridheight = 4;
             gridBagConstraints42.gridy = 0;
             GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
             gridBagConstraints41.gridx = 0;
             gridBagConstraints41.anchor = java.awt.GridBagConstraints.SOUTHWEST;
             gridBagConstraints41.insets = new java.awt.Insets(2, 2, 2, 2);
             gridBagConstraints41.gridy = 2;
            
             GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
             gridBagConstraints40.gridx = 0;
             gridBagConstraints40.anchor = java.awt.GridBagConstraints.SOUTHWEST;
             gridBagConstraints40.insets = new java.awt.Insets(2, 2, 2, 2);
             gridBagConstraints40.gridy = 0;
             
             GridBagConstraints gridBagConstraints39 = new GridBagConstraints();
             gridBagConstraints39.fill = java.awt.GridBagConstraints.HORIZONTAL;
             gridBagConstraints39.gridy = 3;
             gridBagConstraints39.weightx = 1.0;
             gridBagConstraints39.insets = new java.awt.Insets(2, 2, 10, 10);
             gridBagConstraints39.gridx = 0;
             GridBagConstraints gridBagConstraints38 = new GridBagConstraints();
             gridBagConstraints38.fill = java.awt.GridBagConstraints.HORIZONTAL;
             gridBagConstraints38.gridy = 1;
             gridBagConstraints38.weightx = 1.0;
             gridBagConstraints38.insets = new java.awt.Insets(2, 2, 10, 10);
             gridBagConstraints38.gridx = 0;
             
             commandArgsValueLabel = new JLabel();
             commandArgsValueLabel.setText("Value:");
             commandArgsKeyLabel = new JLabel();
             commandArgsKeyLabel.setText("Key:");
             
             commandArgsControlPanel = new JPanel();
             commandArgsControlPanel.setLayout(new GridBagLayout());
             commandArgsControlPanel.add(getCommandArgsKeyTextField(), gridBagConstraints38);
             commandArgsControlPanel.add(getCommandArgsValueTextField(), gridBagConstraints39);
             commandArgsControlPanel.add(commandArgsKeyLabel, gridBagConstraints40);
             commandArgsControlPanel.add(commandArgsValueLabel, gridBagConstraints41);
             commandArgsControlPanel.add(getCommandArgsButtonPanel(), gridBagConstraints42);
             
        }
        return commandArgsControlPanel;
    }
	
    /**
     * This method initializes servicePropertiesButtonPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getCommandArgsButtonPanel() {
        if (commandArgsButtonPanel == null) {
            GridBagConstraints gridBagConstraints37 = new GridBagConstraints();
            gridBagConstraints37.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints37.gridy = 0;
            gridBagConstraints37.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints37.gridx = 0;
            GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
            gridBagConstraints32.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints32.gridy = 1;
            gridBagConstraints32.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints32.gridx = 0;
            commandArgsButtonPanel = new JPanel();
            commandArgsButtonPanel.setLayout(new GridBagLayout());
            commandArgsButtonPanel.add(getRemoveCommandArgsButton(), gridBagConstraints32);
            commandArgsButtonPanel.add(getAddCommandArgsButton(), gridBagConstraints37);
        }
        return commandArgsButtonPanel;
    }
    
    /**
     * This method initializes addServiceProperyButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getAddCommandArgsButton() {
        if (addCommandArgsButton == null) {
        	addCommandArgsButton = new JButton();
        	addCommandArgsButton.setText("Add");
        	addCommandArgsButton.setIcon(PortalLookAndFeel.getAddIcon());
        	addCommandArgsButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    // TODO - might want to do some extra checks here 
                	if ((getCommandArgsKeyTextField().getText().length() != 0) && CommonTools.isValidJavaField(getCommandArgsKeyTextField().getText())) {
                        String key = getCommandArgsKeyTextField().getText();
                        String value = getCommandArgsValueTextField().getText();
                       
                        getCommandArgsTable().addRow(key, value);
                    } else {
                    	System.out.println("ERROR specifying command args - key null");
                        //JOptionPane
                        //    .showMessageDialog(ModificationViewer.this,
                        //        "Service Property key must be a valid java identifier, beginning with a lowercase character.");
                    }
                }
            });
        }
        return addCommandArgsButton;
    }


    /**
     * This method initializes removeServicePropertyButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getRemoveCommandArgsButton() {
        if (removeCommandArgsButton == null) {
        	removeCommandArgsButton = new JButton();
        	removeCommandArgsButton.setText("Remove");
        	removeCommandArgsButton.setIcon(PortalLookAndFeel.getRemoveIcon());
        	removeCommandArgsButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        getCommandArgsTable().removeSelectedRow();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        //ErrorDialog.showErrorDialog(e1);
                    }
                }
            });
        }
        return removeCommandArgsButton;
    }
	
    /**
     * This method initializes servicePropertyKeyTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getCommandArgsKeyTextField() {
        if (commandArgsKeyTextField == null) {
        	commandArgsKeyTextField = new JTextField();
        }
        return commandArgsKeyTextField;
    }
    
    /**
     * This method initializes servicePropertyKeyTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getCommandArgsValueTextField() {
        if (commandArgsValueTextField == null) {
        	commandArgsValueTextField = new JTextField();
        }
        return commandArgsValueTextField;
    }
    
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
		}
		jTextField.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (getJTextField().getText() != null) {
					getJButton().setEnabled(true);
				}
				
			}
			
		});
		return jTextField;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Browse");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					fileChooser = new JFileChooser(".");
			           int status = fileChooser.showOpenDialog(null);
			           if (status == JFileChooser.APPROVE_OPTION) {
			           appName = 
			                  fileChooser.getSelectedFile();
			             getJTextField().setText(appName.getAbsolutePath());
			           }
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("OK");
			jButton1.setEnabled(true);
			jButton1.setHorizontalAlignment(SwingConstants.RIGHT);
			jButton1.addActionListener(new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(!(getJTextField().getText().trim()).equals("")) {
					
						appType.setApplicationName(getJTextField().getText().trim());
						appType.setApplicationVersion(getJTextField2().getText().trim());
						appType.setDescription(getJTextField1().getText().trim());
						//TODO: Handle the extension elements to JSDL Application_Type properly
						MessageElement[] any_element = new MessageElement[1];
						any_element[0] = new MessageElement(getJTextField3().getText().trim(), "foo");
						appType.set_any(any_element);
						try {
							setExtensionData();
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(ApplicationServicePanel.this, 
							"Error: setting the extensions ");
							e1.printStackTrace();
						}
						dispose();	
					} else {
						JOptionPane.showMessageDialog(ApplicationServicePanel.this, 
								"Error: Empty Application/Executable path ");
					}
					
				}
			});
		}
		return jButton1;
	}

	/**
	 * @return the jTextField1
	 */
	public JTextField getJTextField1() {
		if (jTextField1 == null) {
			jTextField1 = new JTextField();
		}
		return jTextField1;
	}

	

	/**
	 * @return the jTextField2
	 */
	public JTextField getJTextField2() {
		if (jTextField2 == null) {
			jTextField2 = new JTextField();
		}
		return jTextField2;
	}

	
	/**
	 * @return the jTextField3
	 */
	public JTextField getJTextField3() {
		if (jTextField3 == null) {
			jTextField3 = new JTextField();
		}
		return jTextField3;
	}
	
	private void setExtensionData() throws Exception {
		ExtensionTypeExtensionData data = getExtensionTypeExtensionData();
		Application_Type appType1 = ExtensionDataUtils.getExtensionData(data);
		appType1.setApplicationName(appType.getApplicationName());
		appType1.setApplicationVersion(appType.getApplicationVersion());
		appType1.setDescription(appType.getDescription());
		appType1.set_any(appType.get_any());
		System.out.println(appType.getApplicationName() + appType.getApplicationVersion() +
				appType.getDescription() );
		MessageElement element = new MessageElement(Application_Type.getTypeDesc().getXmlType(), appType1);
		ExtensionTools.updateExtensionDataElement(data, element);
	}

	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox() {
		if (jCheckBox == null) {
			jCheckBox = new JCheckBox();
		}
		return jCheckBox;
	}


	

}  //  @jve:decl-index=0:visual-constraint="27,29"
