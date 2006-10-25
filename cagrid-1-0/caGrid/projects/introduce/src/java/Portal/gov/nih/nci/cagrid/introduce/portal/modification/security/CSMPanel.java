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

	public final static String CSM_CONFIGURATION_FILE = "csmConfiguration";

	private static final long serialVersionUID = 1L;
	private JLabel jLabel = null;
	private JComboBox protectionType = null;
	private JLabel jLabel1 = null;
	private JTextField protectionElement = null;
	private JLabel jLabel2 = null;
	private JComboBox privilege = null;
	private String serviceType;
	private String methodName;

	private JLabel jLabel3 = null;

	private JTextField applicationContext = null;


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
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints21.gridy = 0;
		gridBagConstraints21.weightx = 1.0;
		gridBagConstraints21.anchor = GridBagConstraints.WEST;
		gridBagConstraints21.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints21.gridx = 1;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.anchor = GridBagConstraints.WEST;
		gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints11.gridy = 0;
		jLabel3 = new JLabel();
		jLabel3.setText("CSM Application Context");
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints5.gridy = 3;
		gridBagConstraints5.weightx = 1.0;
		gridBagConstraints5.anchor = GridBagConstraints.WEST;
		gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints5.gridx = 1;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.anchor = GridBagConstraints.WEST;
		gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints4.gridy = 3;
		jLabel2 = new JLabel();
		jLabel2.setText("Privilege");
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints3.gridy = 2;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.anchor = GridBagConstraints.WEST;
		gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints3.gridx = 1;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints2.gridy = 2;
		jLabel1 = new JLabel();
		jLabel1.setText("Protection Element");
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 1;
		gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
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
		this.add(jLabel3, gridBagConstraints11);
		this.add(getApplicationContext(), gridBagConstraints21);
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
						if (methodName != null) {
							pe.append(":" + methodName);
						}
						getProtectionElement().setEditable(false);
						getProtectionElement().setText(pe.toString());
					} else if (protectionType.getSelectedItem().equals(ProtectionMethod.ServiceURI)) {
						pe.append("http://[SERVICE_URL_DETERMINED_AT_RUNTIME]");
						if (methodName != null) {
							pe.append(":" + methodName);
						}
						getProtectionElement().setEditable(false);
						getProtectionElement().setText(pe.toString());
					} else {
						getProtectionElement().setEditable(true);
					}

				}
			});
			protectionType.addItem(ProtectionMethod.ServiceType);
			protectionType.addItem(ProtectionMethod.ServiceURI);
			protectionType.addItem(ProtectionMethod.Custom);
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
	private JComboBox getPrivilege() {
		if (privilege == null) {
			privilege = new JComboBox();
			privilege.setEditable(true);
			privilege.addItem("EXECUTE");
			privilege.addItem("READ");
			privilege.addItem("WRITE");
			privilege.addItem("CREATE");
			privilege.addItem("ACCESS");
			privilege.addItem("UPDATE");
			privilege.addItem("DELETE");
		}
		return privilege;
	}


	public void setAuthorization(CSMAuthorization csm) {
		this.getProtectionType().setSelectedItem(csm.getProtectionMethod());
		if (csm.getProtectionMethod().equals(ProtectionMethod.Custom) && csm.getCustomProtectionMethod() != null) {
			this.getProtectionElement().setText(csm.getCustomProtectionMethod());
		}
		boolean hasPrivilege = false;
		for (int i = 0; i < getPrivilege().getItemCount(); i++) {
			if (getPrivilege().getItemAt(i).equals(csm.getPrivilege())) {
				hasPrivilege = true;
			}
		}
		if (!hasPrivilege) {
			getPrivilege().addItem(csm.getPrivilege());
		}
		this.getPrivilege().setSelectedItem(csm.getPrivilege());
		this.getApplicationContext().setText(csm.getApplicationContext());
	}


	public CSMAuthorization getAuthorization() throws Exception {
		CSMAuthorization csm = new CSMAuthorization();
		csm.setProtectionMethod((ProtectionMethod) getProtectionType().getSelectedItem());

		if (csm.getProtectionMethod().equals(ProtectionMethod.Custom)) {
			String custom = Utils.clean(this.getProtectionElement().getText());
			if (custom == null) {
				StringBuffer sb = new StringBuffer();

				if (methodName != null) {
					sb.append("You must specify a protection element to protect the method, " + methodName
						+ " with CSM!!!");
				} else {
					sb.append("You must specify a protection element to protect the service, " + serviceType
						+ " with CSM!!!");
				}
				throw new Exception(sb.toString());
			}else{
				csm.setCustomProtectionMethod(custom);
			}

		}

		String priv = Utils.clean((String) getPrivilege().getSelectedItem());
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

		String application = Utils.clean(getApplicationContext().getText());
		if (application == null) {
			StringBuffer sb = new StringBuffer();
			if (methodName != null) {
				sb.append("You must specify a CSM application context to protect the method, " + methodName
					+ " with CSM!!!");
			} else {
				sb.append("You must specify a CSM application context to protect the service, " + serviceType
					+ " with CSM!!!");
			}
			throw new Exception(sb.toString());
		}
		csm.setApplicationContext(application);
		return csm;
	}


	/**
	 * This method initializes applicationContext
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getApplicationContext() {
		if (applicationContext == null) {
			applicationContext = new JTextField();
		}
		return applicationContext;
	}

}
