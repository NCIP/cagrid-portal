package org.cagrid.rav;


import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.CreationExtensionUIDialog;

import java.awt.GridBagLayout;
import javax.swing.JPanel;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ggf.schemas.jsdl._2005._11.jsdl.Application_Type;

import java.io.File;

public class ApplicationServicePanel extends CreationExtensionUIDialog {

	private static final long serialVersionUID = 1L;
	private JPanel browserPanel = null;
	private JPanel argsPanel = null;
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
	
	private Application_Type appType = null;
	
	/**
	 * This is the default constructor
	 */
	public ApplicationServicePanel(Frame f, 
			ServiceExtensionDescriptionType desc, ServiceInformation info) {
		super(f, desc, info);
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
		this.setSize(536, 191);
		this.setLayout(new GridBagLayout());
		this.setName("ApplicationServicePanel");
		this.add(getBrowserPanel(), gridBagConstraints);
		this.add(getArgsPanel(), gridBagConstraints1);
		this.setTitle("Remote Application Virtualization Environment");
	}

	/**
	 * This method initializes browserPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getBrowserPanel() {
		if (browserPanel == null) {
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
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = GridBagConstraints.SOUTHWEST;
			gridBagConstraints6.gridx = 3;
			gridBagConstraints6.gridheight = 4;
			gridBagConstraints6.gridwidth = 41;
			gridBagConstraints6.gridy = 6;
			argsPanel = new JPanel();
			argsPanel.setLayout(new GridBagLayout());
			argsPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			argsPanel.add(getJButton1(), gridBagConstraints6);
		}
		return argsPanel;
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
		if (jTextField1 != null) {
			jTextField1 = new JTextField();
		}
		return jTextField1;
	}

	

	/**
	 * @return the jTextField2
	 */
	public JTextField getJTextField2() {
		if (jTextField2 != null) {
			jTextField2 = new JTextField();
		}
		return jTextField2;
	}

	
	/**
	 * @return the jTextField3
	 */
	public JTextField getJTextField3() {
		if (jTextField3 != null) {
			jTextField3 = new JTextField();
		}
		return jTextField3;
	}

}  //  @jve:decl-index=0:visual-constraint="8,8"
