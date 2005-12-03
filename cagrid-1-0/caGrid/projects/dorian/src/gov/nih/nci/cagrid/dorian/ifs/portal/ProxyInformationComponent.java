package gov.nih.nci.cagrid.gums.ifs.portal;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.globus.gsi.GlobusCredential;
import org.projectmobius.portal.GridPortalComponent;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ProxyInformationComponent.java,v 1.3 2005-12-03 07:18:56 langella Exp $
 */
public class ProxyInformationComponent extends GridPortalComponent {

	private javax.swing.JPanel jContentPane = null;
	private JPanel mainPanel = null;
	private JPanel proxyInformation = null;
	private JLabel subjectLabel = null;
	private JTextField subjectField = null;
	private JPanel buttonPanel = null;
	private JButton jButton = null;
	private JLabel issuerLabel = null;
	private JTextField issuer = null;
	private JLabel identityLabel = null;
	private JTextField identity = null;
	private JLabel strengthLabel = null;
	private JTextField strength = null;
	private JLabel timeLeftLabel = null;
	private JTextField timeLeft = null;
	private GlobusCredential cred;
	private JPanel certificateChain = null;
	private JScrollPane jScrollPane = null;
	private CertificateTable certificates = null;
	/**
	 * This is the default constructor
	 */
	public ProxyInformationComponent(GlobusCredential cred) {
		super();
		this.cred = cred;
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300,300);
		this.setContentPane(getJContentPane());
		this.setFrameIcon(IFSLookAndFeel.getProxyManagerIcon());
		this.setTitle("Proxy Information");
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
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
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTH;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.weighty = 1.0D;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.insets = new java.awt.Insets(5,5,5,5);
			mainPanel.add(getProxyInformation(), gridBagConstraints1);
			mainPanel.add(getButtonPanel(), gridBagConstraints4);
		}
		return mainPanel;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getProxyInformation() {
		if (proxyInformation == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.gridy = 6;
			strengthLabel = new JLabel();
			timeLeftLabel = new JLabel();
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			issuerLabel = new JLabel();
			identityLabel = new JLabel();
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			subjectLabel = new JLabel();
			proxyInformation = new JPanel();
			proxyInformation.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
	 	    GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			
			subjectLabel.setText("Subject");
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.weightx = 0.0D;
			gridBagConstraints2.weighty = 0.0D;
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.weighty = 0.0D;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			proxyInformation.add(subjectLabel, gridBagConstraints2);
			
			issuerLabel.setText("Issuer");
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			identityLabel.setText("Identity");
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.gridy = 2;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints8.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.gridy = 2;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.insets = new java.awt.Insets(2,2,2,2);
			proxyInformation.add(identityLabel, gridBagConstraints8);
			gridBagConstraints7.weightx = 1.0D;
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			proxyInformation.add(issuerLabel, gridBagConstraints6);
			gridBagConstraints5.weightx = 1.0D;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			strengthLabel.setText("Strength");
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.gridy = 4;
			gridBagConstraints10.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 4;
			gridBagConstraints9.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			timeLeftLabel.setText("Expires");
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 5;
			gridBagConstraints11.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints12.weightx = 1.0D;
			gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints12.gridy = 5;
			gridBagConstraints12.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints12.weighty = 0.0D;
			gridBagConstraints9.weightx = 1.0D;
			gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.weighty = 0.0D;
			gridBagConstraints7.weighty = 0.0D;
			gridBagConstraints5.weighty = 0.0D;
			proxyInformation.add(timeLeftLabel, gridBagConstraints11);
			proxyInformation.add(strengthLabel, gridBagConstraints10);
			
			proxyInformation.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Proxy Information",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IFSLookAndFeel.getPanelLabelColor()));	
			proxyInformation.add(getIdentity(), gridBagConstraints7);
			proxyInformation.add(getIssuer(), gridBagConstraints5);
			proxyInformation.add(getTimeLeft(), gridBagConstraints12);
			proxyInformation.add(getStrength(), gridBagConstraints9);
			proxyInformation.add(getSubjectField(), gridBagConstraints3);
			proxyInformation.add(getCertificateChain(), gridBagConstraints);
		}
		return proxyInformation;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getSubjectField() {
		if (subjectField == null) {
			subjectField = new JTextField();
			subjectField.setText(cred.getSubject());
			subjectField.setEditable(false);
		}
		return subjectField;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getJButton(), null);
		}
		return buttonPanel;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Close");
			jButton.setIcon(IFSLookAndFeel.getCloseIcon());
			jButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					dispose();
				}
			});
		}
		return jButton;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getIssuer() {
		if (issuer == null) {
			issuer = new JTextField();
			issuer.setText(cred.getIssuer());
			issuer.setEditable(false);
		}
		return issuer;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getIdentity() {
		if (identity == null) {
			identity = new JTextField();
			identity.setText(cred.getIdentity());
			identity.setEditable(false);
		}
		return identity;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getStrength() {
		if (strength == null) {
			strength = new JTextField();
			strength.setText(cred.getStrength()+" bits");
			strength.setEditable(false);
		}
		return strength;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTimeLeft() {
		if (timeLeft == null) {
			timeLeft = new JTextField();
			cred.getTimeLeft();
			GregorianCalendar c = new GregorianCalendar();
			c.add(Calendar.SECOND,(int)cred.getTimeLeft());
			timeLeft.setText(c.getTime().toString());
			timeLeft.setEditable(false);
		}
		return timeLeft;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getCertificateChain() {
		if (certificateChain == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints13.weighty = 1.0;
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridy = 0;
			gridBagConstraints13.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints13.weightx = 1.0;
			certificateChain = new JPanel();
			certificateChain.setLayout(new GridBagLayout());
			certificateChain.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Certificate Chain",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IFSLookAndFeel.getPanelLabelColor()));
			certificateChain.add(getJScrollPane(), gridBagConstraints13);
		}
		return certificateChain;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getCertificates());
		}
		return jScrollPane;
	}
	/**
	 * This method initializes certificates	
	 * 	
	 * @return javax.swing.JTable	
	 */    
	private CertificateTable getCertificates() {
		if (certificates == null) {
			certificates = new CertificateTable();
			X509Certificate[] certs = cred.getCertificateChain();
			for(int i=0; i<certs.length; i++){
				certificates.addCertificate(certs[i]);
			}
		}
		return certificates;
	}
         }
