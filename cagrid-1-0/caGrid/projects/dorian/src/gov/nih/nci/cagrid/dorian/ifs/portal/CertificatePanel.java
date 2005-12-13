package gov.nih.nci.cagrid.gums.ifs.portal;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.security.cert.X509Certificate;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: CertificatePanel.java,v 1.2 2005-12-13 19:53:22 langella Exp $
 */
public class CertificatePanel extends JPanel {

	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JLabel jLabel4 = null;
	private JLabel jLabel5 = null;
	private JLabel versionLabel = null;
	private JLabel jLabel7 = null;
	private JTextField certficateSignatureAlgorithm = null;
	private JTextField certificateType = null;
	private JTextField certificateVersion = null;
	private JTextField certificateSerialNumber = null;
	private JTextField certificateIssuer = null;
	private JTextField certificateExpires = null;
	private JTextField certificateCreated = null;
	private JTextField certificateSubject = null;


	/**
	 * This is the default constructor
	 */
	public CertificatePanel() {
		super();
		initialize();
	}


	public CertificatePanel(X509Certificate cert) {
		super();
		initialize();
		setCertificate(cert);
	}


	public void setCertificate(X509Certificate cert) {
		this.getCertificateCreated().setText(cert.getNotBefore().toString());
		this.getCertificateExpires().setText(cert.getNotAfter().toString());
		this.getCertificateIssuer().setText(cert.getIssuerDN().getName());
		this.getCertificateSerialNumber().setText(cert.getSerialNumber().toString());
		this.getCertificateSubject().setText(cert.getSubjectDN().getName());
		this.getCertificateSignatureAlgorithm().setText(cert.getSigAlgName());
		this.getCertificateType().setText(cert.getType());
		this.getCertificateVersion().setText(String.valueOf(cert.getVersion()));
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
		gridBagConstraints16.gridx = 0;
		gridBagConstraints16.anchor = java.awt.GridBagConstraints.NORTH;
		gridBagConstraints16.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints16.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints16.weightx = 1.0D;
		gridBagConstraints16.weighty = 1.0D;
		gridBagConstraints16.gridy = 0;
		this.setLayout(new GridBagLayout());
		this.setSize(300, 200);
		this.add(getJPanel(), gridBagConstraints16);
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.anchor = GridBagConstraints.WEST;
			gridBagConstraints15.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints15.gridx = 1;
			gridBagConstraints15.gridy = 0;
			gridBagConstraints15.weightx = 1.0D;
			gridBagConstraints15.weighty = 0.0D;
			gridBagConstraints15.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints14.gridx = 1;
			gridBagConstraints14.gridy = 4;
			gridBagConstraints14.weightx = 1.0D;
			gridBagConstraints14.weighty = 0.0D;
			gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints13.gridx = 1;
			gridBagConstraints13.gridy = 5;
			gridBagConstraints13.weightx = 1.0D;
			gridBagConstraints13.weighty = 0.0D;
			gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.gridy = 1;
			gridBagConstraints12.weightx = 1.0D;
			gridBagConstraints12.weighty = 0.0D;
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.gridy = 2;
			gridBagConstraints11.weightx = 1.0D;
			gridBagConstraints11.weighty = 0.0D;
			gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints10.gridx = 1;
			gridBagConstraints10.gridy = 8;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 7;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.gridy = 6;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 6;
			gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
			jLabel7 = new JLabel();
			jLabel7.setText("Signature Algorithm");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 8;
			gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
			versionLabel = new JLabel();
			versionLabel.setText("Version");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 7;
			gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
			jLabel5 = new JLabel();
			jLabel5.setText("Type");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 4;
			gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
			jLabel4 = new JLabel();
			jLabel4.setText("Created");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 5;
			gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
			jLabel3 = new JLabel();
			jLabel3.setText("Expires");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
			jLabel2 = new JLabel();
			jLabel2.setText("Issuer");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 2;
			gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
			jLabel1 = new JLabel();
			jLabel1.setText("Serial Number");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 0.0D;
			gridBagConstraints.weighty = 0.0D;
			gridBagConstraints.insets = new Insets(2, 2, 2, 2);
			jLabel = new JLabel();
			jLabel.setText("Subject");
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(jLabel, gridBagConstraints);
			jPanel.add(jLabel1, gridBagConstraints1);
			jPanel.add(jLabel2, gridBagConstraints2);
			jPanel.add(jLabel3, gridBagConstraints3);
			jPanel.add(jLabel4, gridBagConstraints4);
			jPanel.add(jLabel5, gridBagConstraints5);
			jPanel.add(versionLabel, gridBagConstraints6);
			jPanel.add(jLabel7, gridBagConstraints7);
			jPanel.add(getCertificateSignatureAlgorithm(), gridBagConstraints8);
			jPanel.add(getCertificateType(), gridBagConstraints9);
			jPanel.add(getCertificateVersion(), gridBagConstraints10);
			jPanel.add(getCertificateSerialNumber(), gridBagConstraints11);
			jPanel.add(getCertificateIssuer(), gridBagConstraints12);
			jPanel.add(getCertificateExpires(), gridBagConstraints13);
			jPanel.add(getCertificateCreated(), gridBagConstraints14);
			jPanel.add(getCertificateSubject(), gridBagConstraints15);
		}
		return jPanel;
	}


	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCertificateSignatureAlgorithm() {
		if (certficateSignatureAlgorithm == null) {
			certficateSignatureAlgorithm = new JTextField();
			certficateSignatureAlgorithm.setEditable(false);
		}
		return certficateSignatureAlgorithm;
	}


	/**
	 * This method initializes jTextField1
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCertificateType() {
		if (certificateType == null) {
			certificateType = new JTextField();
			certificateType.setEditable(false);
		}
		return certificateType;
	}


	/**
	 * This method initializes jTextField2
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCertificateVersion() {
		if (certificateVersion == null) {
			certificateVersion = new JTextField();
			certificateVersion.setEditable(false);
		}
		return certificateVersion;
	}


	/**
	 * This method initializes jTextField3
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCertificateSerialNumber() {
		if (certificateSerialNumber == null) {
			certificateSerialNumber = new JTextField();
			certificateSerialNumber.setEditable(false);
		}
		return certificateSerialNumber;
	}


	/**
	 * This method initializes jTextField4
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCertificateIssuer() {
		if (certificateIssuer == null) {
			certificateIssuer = new JTextField();
			certificateIssuer.setEditable(false);
		}
		return certificateIssuer;
	}


	/**
	 * This method initializes jTextField5
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCertificateExpires() {
		if (certificateExpires == null) {
			certificateExpires = new JTextField();
			certificateExpires.setEditable(false);
		}
		return certificateExpires;
	}


	/**
	 * This method initializes jTextField6
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCertificateCreated() {
		if (certificateCreated == null) {
			certificateCreated = new JTextField();
			certificateCreated.setEditable(false);
		}
		return certificateCreated;
	}


	/**
	 * This method initializes jTextField7
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCertificateSubject() {
		if (certificateSubject == null) {
			certificateSubject = new JTextField();
			certificateSubject.setEditable(false);
		}
		return certificateSubject;
	}

}
