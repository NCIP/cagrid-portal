package gov.nih.nci.cagrid.dorian.ifs.portal;

import gov.nih.nci.cagrid.dorian.portal.DorianLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.security.cert.X509Certificate;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.projectmobius.portal.GridPortalComponent;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: CertificateInformationComponent.java,v 1.6 2005-12-15 19:29:33 langella Exp $
 */
public class CertificateInformationComponent extends GridPortalComponent {

	private javax.swing.JPanel jContentPane = null;
	private JPanel mainPanel = null;
	private JPanel proxyInformation = null;
	private JLabel subjectLabel = null;
	private JTextField subjectField = null;
	private JPanel buttonPanel = null;
	private JButton jButton = null;
	private JLabel issuerLabel = null;
	private JTextField issuer = null;
	private JLabel serialLabel = null;
	private JTextField serialNumber = null;
	private JLabel createdLabel = null;
	private JTextField created = null;
	private JLabel timeLeftLabel = null;
	private JTextField timeLeft = null;
	private X509Certificate cert;
	private JLabel typeLabel = null;
	private JLabel versionLabel = null;
	private JLabel algorithLabel = null;
	private JTextField algorithm = null;
	private JTextField type = null;
	private JTextField version = null;
	/**
	 * This is the default constructor
	 */
	public CertificateInformationComponent(X509Certificate cert) {
		super();
		this.cert = cert;
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
		this.setFrameIcon(DorianLookAndFeel.getProxyIcon());
		this.setTitle("Certificate Viewer");
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
			createdLabel = new JLabel();
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.gridy = 8;
			gridBagConstraints21.weightx = 1.0;
			gridBagConstraints21.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints21.gridx = 1;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints16.gridy = 7;
			gridBagConstraints16.weightx = 1.0;
			gridBagConstraints16.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints16.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints16.gridx = 1;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints15.gridy = 6;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints15.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints15.gridx = 1;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints14.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints14.gridy = 6;
			algorithLabel = new JLabel();
			algorithLabel.setText("Signature Algorithm");
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints13.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints13.gridy = 8;
			versionLabel = new JLabel();
			versionLabel.setText("Version");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.gridy = 7;
			typeLabel = new JLabel();
			typeLabel.setText("Type");
			timeLeftLabel = new JLabel();
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			issuerLabel = new JLabel();
			serialLabel = new JLabel();
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
			serialLabel.setText("Serial Number");
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.gridy = 2;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints8.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.gridy = 2;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.insets = new java.awt.Insets(2,2,2,2);
			proxyInformation.add(serialLabel, gridBagConstraints8);
			gridBagConstraints7.weightx = 1.0D;
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			proxyInformation.add(issuerLabel, gridBagConstraints6);
			gridBagConstraints5.weightx = 1.0D;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			createdLabel.setText("Created");
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
			proxyInformation.add(createdLabel, gridBagConstraints10);
			proxyInformation.add(typeLabel, gridBagConstraints);
			proxyInformation.add(versionLabel, gridBagConstraints13);
			proxyInformation.add(algorithLabel, gridBagConstraints14);
			proxyInformation.add(getAlgorithm(), gridBagConstraints15);
			proxyInformation.add(getType(), gridBagConstraints16);
			proxyInformation.add(getVersion(), gridBagConstraints21);
			
			proxyInformation.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Certificate Information",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, DorianLookAndFeel.getPanelLabelColor()));	
			proxyInformation.add(getSerialNumber(), gridBagConstraints7);
			proxyInformation.add(getIssuer(), gridBagConstraints5);
			proxyInformation.add(getTimeLeft(), gridBagConstraints12);
			proxyInformation.add(getCreated(), gridBagConstraints9);
			proxyInformation.add(getSubjectField(), gridBagConstraints3);
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
			subjectField.setText(cert.getSubjectDN().getName());
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
			jButton.setIcon(DorianLookAndFeel.getCloseIcon());
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
			issuer.setText(cert.getIssuerDN().getName());
			issuer.setEditable(false);
		}
		return issuer;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getSerialNumber() {
		if (serialNumber == null) {
			serialNumber = new JTextField();
			serialNumber.setText(cert.getSerialNumber().toString());
			serialNumber.setEditable(false);
		}
		return serialNumber;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getCreated() {
		if (created == null) {
			created = new JTextField();
			created.setText(cert.getNotBefore().toString());
			created.setEditable(false);
		}
		return created;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTimeLeft() {
		if (timeLeft == null) {
			timeLeft = new JTextField();
			timeLeft.setText(cert.getNotAfter().toString());
			timeLeft.setEditable(false);
		}
		return timeLeft;
	}
	/**
	 * This method initializes algorithm	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getAlgorithm() {
		if (algorithm == null) {
			algorithm = new JTextField();
			algorithm.setEditable(false);
			algorithm.setText(cert.getSigAlgName());
		}
		return algorithm;
	}
	/**
	 * This method initializes type	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getType() {
		if (type == null) {
			type = new JTextField();
			type.setEditable(false);
			type.setText(cert.getType());
		}
		return type;
	}
	/**
	 * This method initializes version	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getVersion() {
		if (version == null) {
			version = new JTextField();
			version.setEditable(false);
			version.setText(String.valueOf(cert.getVersion()));
		}
		return version;
	}
         }
