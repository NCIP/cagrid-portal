package org.cagrid.rav;


import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.ComponentOrientation;
import javax.swing.SwingConstants;
import java.io.File;

public class ApplicationServicePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel browserPanel = null;
	private JPanel argsPanel = null;
	private JLabel jLabel = null;
	private JTextField jTextField = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	
	private File appName = null;
	
	private JFileChooser fileChooser = null;
	/**
	 * This is the default constructor
	 */
	public ApplicationServicePanel() {
		super();
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
		this.setSize(466, 98);
		this.setLayout(new GridBagLayout());
		this.setName("ApplicationServicePanel");
		this.add(getBrowserPanel(), gridBagConstraints);
		this.add(getArgsPanel(), gridBagConstraints1);
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
			jLabel = new JLabel();
			jLabel.setText("Application/Executable");
			browserPanel = new JPanel();
			browserPanel.setLayout(new GridBagLayout());
			browserPanel.add(jLabel, gridBagConstraints3);
			browserPanel.add(getJTextField(), gridBagConstraints2);
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
			             if(appName != null) {
			            	 getJButton1().setEnabled(true);
			             }
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
			jButton1.setEnabled(false);
			jButton1.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return jButton1;
	}

}  //  @jve:decl-index=0:visual-constraint="8,8"
