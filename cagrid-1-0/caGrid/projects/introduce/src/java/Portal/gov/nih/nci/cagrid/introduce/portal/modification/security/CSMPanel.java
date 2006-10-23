package gov.nih.nci.cagrid.introduce.portal.modification.security;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.security.CSMAuthorization;
import gov.nih.nci.cagrid.introduce.beans.security.ProtectionMethod;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @created Jun 22, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */

public class CSMPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel jLabel = null;
	private JComboBox protectionType = null;
	private JLabel jLabel1 = null;
	private JTextField protectionElement = null;
	private JLabel jLabel2 = null;
	private JTextField privilege = null;
	private String serviceType;
	private String methodName;


	/**
	 * This is the default constructor
	 */
	public CSMPanel(String serviceType) {
		this(serviceType, null);
	}


	public CSMPanel(String serviceType, String methodName) {
		super();
		this.serviceType = serviceType;
		this.methodName = methodName;
		initialize();
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints5.gridy = 2;
		gridBagConstraints5.weightx = 1.0;
		gridBagConstraints5.anchor = GridBagConstraints.WEST;
		gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints5.gridx = 1;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.anchor = GridBagConstraints.WEST;
		gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints4.gridy = 2;
		jLabel2 = new JLabel();
		jLabel2.setText("Privilege");
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints3.gridy = 1;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.anchor = GridBagConstraints.WEST;
		gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints3.gridx = 1;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints2.gridy = 1;
		jLabel1 = new JLabel();
		jLabel1.setText("Protection Element");
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		jLabel = new JLabel();
		jLabel.setText("Protection Method");
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder(null, "Common Security Module (CSM)",
			TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12),
			new Color(62, 109, 181)));
		this.add(jLabel, gridBagConstraints1);
		this.add(getProtectionType(), gridBagConstraints);
		this.add(jLabel1, gridBagConstraints2);
		this.add(getProtectionElement(), gridBagConstraints3);
		this.add(jLabel2, gridBagConstraints4);
		this.add(getPrivilege(), gridBagConstraints5);

	}


	/**
	 * This method initializes protectionType
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getProtectionType() {
		if (protectionType == null) {
			protectionType = new JComboBox();
			protectionType.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					StringBuffer pe = new StringBuffer();
					if (protectionType.getSelectedItem().equals(ProtectionMethod.ServiceType)) {
						pe.append(serviceType);

					} else {
						pe.append("[DEPLOYMENT_SERVICE_URL]");
					}
					if (methodName != null) {
						pe.append(":" + methodName);
					}
					getProtectionElement().setText(pe.toString());
				}
			});
			protectionType.addItem(ProtectionMethod.ServiceType);
			protectionType.addItem(ProtectionMethod.ServiceURI);
		}
		return protectionType;
	}


	/**
	 * This method initializes protectionElement
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getProtectionElement() {
		if (protectionElement == null) {
			protectionElement = new JTextField();
			protectionElement.setEditable(false);
		}
		return protectionElement;
	}


	/**
	 * This method initializes privilege
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getPrivilege() {
		if (privilege == null) {
			privilege = new JTextField();
		}
		return privilege;
	}

	public void setAuthorization(CSMAuthorization csm){
		this.getProtectionType().setSelectedItem(csm.getProtectionMethod());
		this.getPrivilege().setText(csm.getPrivilege());		
	}

	public CSMAuthorization getAuthorization() throws Exception {
		CSMAuthorization csm = new CSMAuthorization();
		csm.setProtectionMethod((ProtectionMethod) getProtectionType().getSelectedItem());
		String priv = Utils.clean(getPrivilege().getText());
		if (priv == null) {
			StringBuffer sb = new StringBuffer();

			if (methodName != null) {
				sb.append("You must specify a privilege to protect the method, " + methodName + " with CSM!!!");
			} else {
				sb.append("You must specify a privilege to protect the service, " + serviceType + " with CSM!!!");
			}
			throw new Exception(sb.toString());
		}
		csm.setPrivilege(priv);
		return csm;
	}

}
