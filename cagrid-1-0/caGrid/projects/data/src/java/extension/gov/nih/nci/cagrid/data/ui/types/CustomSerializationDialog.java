package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.projectmobius.portal.PortalResourceManager;

/** 
 *  CustomSerializationDialog
 *  Dialog for custom configuration of schema element types
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jun 28, 2006 
 * @version $Id$ 
 */
public class CustomSerializationDialog extends JDialog {
	private SchemaElementType[] types;
	
	private JLabel serializerLabel = null;
	private JLabel deserialierLabel = null;
	private JTextField serialzierTextField = null;
	private JTextField deserializerTextField = null;
	private JButton okButton = null;
	private JButton cancelButton = null;
	private JPanel serializationPanel = null;
	private JPanel buttonPanel = null;
	private JPanel mainPanel = null;

	public CustomSerializationDialog(SchemaElementType[] types) {
		super(PortalResourceManager.getInstance().getGridPortal(), "Custom Serialization", true);
		this.types = types;
		this.initialize();		
	}
	
	
	private void initialize() {		
		this.setSize(new java.awt.Dimension(350, 112));
		this.setContentPane(getMainPanel());
		PortalUtils.centerWindow(this);
		setVisible(true);
	}


	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getSerializerLabel() {
		if (serializerLabel == null) {
			serializerLabel = new JLabel();
			serializerLabel.setText("Serializer:");
		}
		return serializerLabel;
	}


	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getDeserialierLabel() {
		if (deserialierLabel == null) {
			deserialierLabel = new JLabel();
			deserialierLabel.setText("Deserializer:");
		}
		return deserialierLabel;
	}


	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSerialzierTextField() {
		if (serialzierTextField == null) {
			serialzierTextField = new JTextField();
		}
		return serialzierTextField;
	}


	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getDeserializerTextField() {
		if (deserializerTextField == null) {
			deserializerTextField = new JTextField();
		}
		return deserializerTextField;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("OK");
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String serializer = getSerialzierTextField().getText();
					String deserializer = getDeserializerTextField().getText();
					for (int i = 0; i < types.length; i++) {
						types[i].setSerializer(serializer);
						types[i].setDeserializer(deserializer);
						types[i].setClassName(types[i].getType());
					}
					dispose();
				}
			});
		}
		return okButton;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return cancelButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSerializationPanel() {
		if (serializationPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			serializationPanel = new JPanel();
			serializationPanel.setLayout(new GridBagLayout());
			serializationPanel.add(getSerializerLabel(), gridBagConstraints);
			serializationPanel.add(getDeserialierLabel(), gridBagConstraints1);
			serializationPanel.add(getSerialzierTextField(), gridBagConstraints2);
			serializationPanel.add(getDeserializerTextField(), gridBagConstraints3);
		}
		return serializationPanel;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints5.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints4.gridy = 0;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getOkButton(), gridBagConstraints4);
			buttonPanel.add(getCancelButton(), gridBagConstraints5);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints7.gridy = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.weightx = 1.0D;
			gridBagConstraints6.gridy = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getSerializationPanel(), gridBagConstraints6);
			mainPanel.add(getButtonPanel(), gridBagConstraints7);
		}
		return mainPanel;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
