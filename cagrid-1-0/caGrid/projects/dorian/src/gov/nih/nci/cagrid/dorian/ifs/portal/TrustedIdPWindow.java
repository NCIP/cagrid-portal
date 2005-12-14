package gov.nih.nci.cagrid.gums.ifs.portal;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.gums.client.IFSAdministrationClient;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserPolicy;
import gov.nih.nci.cagrid.gums.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.gums.portal.GumsLookAndFeel;
import gov.nih.nci.cagrid.gums.portal.ProxyCaddy;
import gov.nih.nci.cagrid.gums.portal.ProxyComboBox;
import gov.nih.nci.cagrid.security.commstyle.SecureConversationWithEncryption;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.globus.gsi.GlobusCredential;
import org.projectmobius.common.MobiusRunnable;
import org.projectmobius.portal.GridPortalBaseFrame;
import org.projectmobius.portal.PortalResourceManager;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: TrustedIdPWindow.java,v 1.3 2005-12-14 18:12:11 langella Exp $
 */
public class TrustedIdPWindow extends GridPortalBaseFrame {

	private final static String INFO_PANEL = "IdP Information";

	private final static String CERTIFICATE_PANEL = "Certificate";

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel buttonPanel = null;

	private JButton updateTrustedIdP = null;

	private JTabbedPane jTabbedPane = null;

	private JPanel jPanel1 = null;

	private JPanel jPanel2 = null;

	private JLabel jLabel14 = null;

	private String serviceId;

	private JTextField service = null;

	private JPanel infoPanel = null;

	private GlobusCredential cred;

	private JLabel credentialLabel = null;

	private JComboBox proxy = null;

	private TrustedIdP idp;

	private JPanel certificatePanel = null;

	private CertificatePanel credPanel = null;

	private boolean newTrustedIdP;

	private JLabel idLabel = null;

	private JTextField idpId = null;

	private JLabel nameLabel = null;

	private JTextField idpName = null;

	private JLabel statusLabel = null;

	private TrustedIdPStatusComboBox status = null;
	
	private IFSUserPolicy[] policies;

	private JLabel policyLabel = null;

	private JComboBox userPolicy = null;
	
	public TrustedIdPWindow(String serviceId, GlobusCredential proxy,IFSUserPolicy[] policies) {
		super();
		this.serviceId = serviceId;
		this.cred = proxy;
		this.idp = new TrustedIdP();
		this.newTrustedIdP = true;
		this.policies = policies;
		initialize();
	}


	/**
	 * This is the default constructor
	 */
	public TrustedIdPWindow(String serviceId, GlobusCredential proxy, TrustedIdP idp,IFSUserPolicy[] policies) throws Exception {
		super();
		this.serviceId = serviceId;
		this.cred = proxy;
		this.idp = idp;
		this.newTrustedIdP = false;
		this.policies = policies;
		initialize();
	}
	
	public class UserPolicyCaddy{
		private IFSUserPolicy policy;
		
		public UserPolicyCaddy(String className){
			this.policy = new IFSUserPolicy(className,"");
		}
		
		public UserPolicyCaddy(IFSUserPolicy policy){
			this.policy = policy;
		}
		
		public IFSUserPolicy getPolicy(){
			return policy;
		}
		
		public String toString(){
			return policy.getName();
		}
		
		public boolean equals(Object o){
			UserPolicyCaddy up = (UserPolicyCaddy)o;
			if(this.getPolicy().getClassName().equals(up.getPolicy().getClassName())){
				return true;
			}else{
				return false;
			}
		}
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(500, 500);
		this.setContentPane(getJContentPane());
		if(this.newTrustedIdP){
			this.setTitle("Add Trusted IdP");
		}else{
		this.setTitle("Trusted IdP [" + idp.getName() + "]");
		}
		this.setFrameIcon(GumsLookAndFeel.getTrustedIdPIcon());

	}


	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getMainPanel(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.weighty = 1.0D;
			gridBagConstraints4.gridx = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTH;
			gridBagConstraints1.gridx = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 2;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			mainPanel.add(getButtonPanel(), gridBagConstraints2);
			mainPanel.add(getJPanel2(), gridBagConstraints1);
			mainPanel.add(getJTabbedPane(), gridBagConstraints4);
		}
		return mainPanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getUpdateTrustedIdP(), null);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes manageUser
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getUpdateTrustedIdP() {
		if (updateTrustedIdP == null) {
			updateTrustedIdP = new JButton();
			
			if(this.newTrustedIdP){
				updateTrustedIdP.setText("Add");
			}else{
				updateTrustedIdP.setText("Update");
			}
			
			updateTrustedIdP.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							updateTrustedIdP();
						}
					};
					try {
						PortalResourceManager.getInstance().getThreadManager().executeInBackground(runner);
					} catch (Exception t) {
						t.getMessage();
					}

				}
			});
			updateTrustedIdP.setIcon(GumsLookAndFeel.getTrustedIdPIcon());
		}
		return updateTrustedIdP;
	}


	private synchronized void updateTrustedIdP() {

		try {
			String service = getService().getText();
			GlobusCredential c = ((ProxyCaddy) getProxy().getSelectedItem()).getProxy();
			IFSAdministrationClient client = new IFSAdministrationClient(service, new SecureConversationWithEncryption(
				c));
			client.updateUser(null);
			PortalUtils.showMessage("The Trusted IdP was updated successfully.");

		} catch (PermissionDeniedFault pdf) {
			PortalUtils.showErrorMessage(pdf);
		} catch (Exception e) {
			e.printStackTrace();
			PortalUtils.showErrorMessage(e);
		}

	}


	/**
	 * This method initializes jTabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setBorder(BorderFactory.createTitledBorder(null, "Trusted Identity Provider",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, GumsLookAndFeel
					.getPanelLabelColor()));
			jTabbedPane.addTab(INFO_PANEL, GumsLookAndFeel.getTrustedIdPIcon(), getInfoPanel(), null);
			jTabbedPane.addTab(CERTIFICATE_PANEL, GumsLookAndFeel.getProxyIcon(), getCertificatePanel(), null);
		}
		return jTabbedPane;
	}


	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.gridy = 3;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints12.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints12.gridx = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints11.gridy = 3;
			policyLabel = new JLabel();
			policyLabel.setText("User Policy");
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.gridy = 2;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.gridx = 1;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints9.gridy = 2;
			statusLabel = new JLabel();
			statusLabel.setText("Status");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridy = 1;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints8.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints7.gridx = 0;
			nameLabel = new JLabel();
			nameLabel.setText("Name");
			nameLabel.setName("Name");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints6.weightx = 1.0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints5.gridx = 0;
			idLabel = new JLabel();
			idLabel.setText("IdP Id");
			if(newTrustedIdP){
				
			}
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.add(getStatus(), gridBagConstraints10);
			jPanel1.setName(INFO_PANEL);
			jPanel1.add(statusLabel, gridBagConstraints9);
			jPanel1.add(idLabel, gridBagConstraints5);
			jPanel1.add(getIdpId(), gridBagConstraints6);
			jPanel1.add(nameLabel, gridBagConstraints7);
			jPanel1.add(getIdPName(), gridBagConstraints8);
			jPanel1.add(policyLabel, gridBagConstraints11);
			jPanel1.add(getUserPolicy(), gridBagConstraints12);
			
		}
		return jPanel1;
	}


	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints.gridy = 1;
			credentialLabel = new JLabel();
			credentialLabel.setText("Proxy");
			GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
			gridBagConstraints27.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints27.weightx = 1.0;
			GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
			gridBagConstraints28.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints28.gridx = 1;
			gridBagConstraints28.gridy = 0;
			gridBagConstraints28.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints28.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints28.weightx = 1.0;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.anchor = GridBagConstraints.WEST;
			gridBagConstraints31.gridwidth = 1;
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.gridy = 0;
			gridBagConstraints31.insets = new Insets(2, 2, 2, 2);
			jLabel14 = new JLabel();
			jLabel14.setText("Service");
			jPanel2 = new JPanel();
			jPanel2.setLayout(new GridBagLayout());
			jPanel2.setBorder(BorderFactory.createTitledBorder(null, "Login Information",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, GumsLookAndFeel
					.getPanelLabelColor()));
			jPanel2.add(jLabel14, gridBagConstraints31);
			jPanel2.add(getService(), gridBagConstraints27);
			jPanel2.add(credentialLabel, gridBagConstraints);
			jPanel2.add(getProxy(), gridBagConstraints3);
		}
		return jPanel2;
	}


	/**
	 * This method initializes service1
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getService() {
		if (service == null) {
			service = new JTextField();
			service.setText(serviceId);
			service.setEditable(false);
		}
		return service;
	}


	/**
	 * This method initializes infoPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getInfoPanel() {
		if (infoPanel == null) {
			infoPanel = new JPanel();
			infoPanel.setLayout(new BorderLayout());
			infoPanel.add(getJPanel1(), java.awt.BorderLayout.NORTH);
		}
		return infoPanel;
	}


	/**
	 * This method initializes proxy1
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getProxy() {
		if (proxy == null) {
			proxy = new ProxyComboBox(cred);
		}
		return proxy;
	}


	/**
	 * This method initializes certificatePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCertificatePanel() {
		if (certificatePanel == null) {
			GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
			gridBagConstraints36.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints36.gridy = 0;
			gridBagConstraints36.weightx = 1.0D;
			gridBagConstraints36.weighty = 1.0D;
			gridBagConstraints36.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints36.anchor = java.awt.GridBagConstraints.NORTH;
			gridBagConstraints36.gridx = 0;
			certificatePanel = new JPanel();
			certificatePanel.setLayout(new BorderLayout());
			certificatePanel.add(getCredPanel(), java.awt.BorderLayout.NORTH);
		}
		return certificatePanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private CertificatePanel getCredPanel() {
		if (credPanel == null) {
			try {
				credPanel = new CertificatePanel();
				if (idp.getIdPCertificate() != null) {
					credPanel.setCertificate(CertUtil.loadCertificateFromString(idp.getIdPCertificate()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return credPanel;
	}


	/**
	 * This method initializes idpId	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getIdpId() {
		if (idpId == null) {
			idpId = new JTextField();
			idpId.setEditable(false);
			if(!newTrustedIdP){
				idpId.setText(String.valueOf(idp.getId()));
			}
		}
		return idpId;
	}


	/**
	 * This method initializes idPName	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getIdPName() {
		if (idpName == null) {
			idpName = new JTextField();
			if(!newTrustedIdP){
				idpName.setText(idp.getName());
			}
		}
		return idpName;
	}


	/**
	 * This method initializes status	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private TrustedIdPStatusComboBox getStatus() {
		if (status == null) {
			status = new TrustedIdPStatusComboBox();
			if(!newTrustedIdP){
				status.setSelectedItem(idp.getStatus());
			}
		}
		return status;
	}


	/**
	 * This method initializes userPolicy	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getUserPolicy() {
		if (userPolicy == null) {
			userPolicy = new JComboBox();
			for(int i=0; i<policies.length; i++){
				userPolicy.addItem(new UserPolicyCaddy(policies[i]));
				
				if(!newTrustedIdP){
					
					if(idp.getUserPolicyClass().equals(policies[i].getClassName())){
						int count = userPolicy.getItemCount();
						userPolicy.setSelectedIndex((count-1));
					}
				}
			}
			
		}
		return userPolicy;
	}

}
