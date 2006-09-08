package gov.nih.nci.cagrid.gridca.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.security.cert.X509Certificate;

import javax.swing.JPanel;

import org.projectmobius.portal.GridPortalComponent;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: CertificateInformationComponent.java,v 1.1 2006-09-08 17:31:38 langella Exp $
 */
public class CertificateInformationComponent extends GridPortalComponent {

	private javax.swing.JPanel jContentPane = null;
	private JPanel mainPanel = null;
	private X509Certificate cert;
	private JPanel certificatePanel = null;
	
	public CertificateInformationComponent() {
		super();
		initialize();
	}
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
		this.setContentPane(getJContentPane());
		this.setFrameIcon(GridCALookAndFeel.getCertificateIcon());
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
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridy = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getCertificatePanel(), gridBagConstraints);
		}
		return mainPanel;
	}
	/**
	 * This method initializes certificatePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getCertificatePanel() {
		if (certificatePanel == null) {
			certificatePanel = new CertificatePanel(cert);
		}
		return certificatePanel;
	}
         }
