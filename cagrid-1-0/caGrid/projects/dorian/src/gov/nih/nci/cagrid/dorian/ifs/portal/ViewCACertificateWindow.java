package gov.nih.nci.cagrid.dorian.ifs.portal;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.dorian.client.DorianCertifcateAuthorityClient;
import gov.nih.nci.cagrid.dorian.portal.DorianLookAndFeel;
import gov.nih.nci.cagrid.dorian.portal.DorianServiceListComboBox;
import gov.nih.nci.cagrid.gridca.portal.CertificateInformationComponent;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.security.cert.X509Certificate;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.projectmobius.common.MobiusRunnable;
import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.portal.PortalResourceManager;


public class ViewCACertificateWindow extends GridPortalComponent {

	private JPanel jContentPane = null;
	private JPanel mainPanel = null;
	private JPanel servicePanel = null;
	private JPanel buttonPanel = null;
	private JLabel ifsLabel = null;
	private JComboBox ifs = null;
	private JButton viewCAButton = null;
	private JButton close = null;


	/**
	 * This is the default constructor
	 */
	public ViewCACertificateWindow() {
		super();
		initialize();
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setFrameIcon(DorianLookAndFeel.getCertificateIcon());
		this.setTitle("View Dorian CA Certifcate");
	}


	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getMainPanel(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}


	/**
	 * This method initializes mainPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridy = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getServicePanel(), gridBagConstraints);
			mainPanel.add(getButtonPanel(), gridBagConstraints3);
		}
		return mainPanel;
	}


	/**
	 * This method initializes idpPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getServicePanel() {
		if (servicePanel == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints5.gridx = 0;
			ifsLabel = new JLabel();
			ifsLabel.setText("Dorian Service");
			servicePanel = new JPanel();
			servicePanel.setLayout(new GridBagLayout());
			servicePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "View Dorian CA Certifcate",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, DorianLookAndFeel.getPanelLabelColor()));
			servicePanel.add(ifsLabel, gridBagConstraints5);
			servicePanel.add(getIfs(), gridBagConstraints6);
		}
		return servicePanel;
	}


	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getViewCAButton(), null);
			buttonPanel.add(getClose(), null);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes ifs
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getIfs() {
		if (ifs == null) {
			ifs = new DorianServiceListComboBox();
		}
		return ifs;
	}


	/**
	 * This method initializes authenticateButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getViewCAButton() {
		if (viewCAButton == null) {
			viewCAButton = new JButton();
			viewCAButton.setText("View CA Certificate");
			viewCAButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							getCACertificate();
						}
					};
					try {
						PortalResourceManager.getInstance().getThreadManager().executeInBackground(runner);
					} catch (Exception t) {
						t.getMessage();
					}
				}
			});
			viewCAButton.setIcon(DorianLookAndFeel.getCertificateIcon());
		}
		return viewCAButton;
	}


	private void getCACertificate() {
		try {
			getViewCAButton().setEnabled(false);
			DorianCertifcateAuthorityClient client = new DorianCertifcateAuthorityClient((String) ifs.getSelectedItem());
			X509Certificate cert = client.getCACertificate();
			dispose();
			CertificateInformationComponent cic = new CertificateInformationComponent(cert);
			PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(cic, 600, 425);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			PortalUtils.showErrorMessage(e);
		}
		getViewCAButton().setEnabled(true);
	}


	private JButton getClose() {
		if (close == null) {
			close = new JButton();
			close.setText("Close");
			close.setIcon(DorianLookAndFeel.getCloseIcon());
			close.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return close;
	}

}
