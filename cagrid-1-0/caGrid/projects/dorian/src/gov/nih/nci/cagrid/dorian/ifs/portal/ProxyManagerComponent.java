package gov.nih.nci.cagrid.gums.ifs.portal;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.gums.common.ProxyUtil;
import gov.nih.nci.cagrid.gums.portal.GumsLookAndFeel;
import gov.nih.nci.cagrid.gums.portal.ProxyCaddy;
import gov.nih.nci.cagrid.gums.portal.ProxyManager;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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
 * @version $Id: ProxyInformationComponent.java,v 1.3 2005/12/03 07:18:56
 *          langella Exp $
 */
public class ProxyManagerComponent extends GridPortalComponent {

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

	private JPanel certificateChain = null;

	private JScrollPane jScrollPane = null;

	private CertificateTable certificates = null;

	private JPanel proxyPanel = null;

	private JComboBox proxy = null;

	private JButton viewCertificate = null;

	private JButton saveProxy = null;

	private JButton setDefaultProxy = null;

	private static final String DEFAULT_PROXY = "Globus Default Proxy";

	private JButton deleteProxy = null;

	private ProxyCaddy defaultProxy;
	/**
	 * This is the default constructor
	 */
	public ProxyManagerComponent() {
		super();
		initialize();
		List creds = ProxyManager.getInstance().getProxies();
		defaultProxy = new ProxyCaddy(DEFAULT_PROXY, null);
		getProxy().addItem(defaultProxy);
		for (int i = 0; i < creds.size(); i++) {
			getProxy().addItem(new ProxyCaddy((GlobusCredential) creds.get(i)));
		}
	}

	public ProxyManagerComponent(GlobusCredential cred) {
		this();
		getProxy().setSelectedItem(new ProxyCaddy(cred));
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(500, 400);
		this.setContentPane(getJContentPane());
		this.setFrameIcon(GumsLookAndFeel.getProxyManagerIcon());
		this.setTitle("Proxy Manager");
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

	private void clearProxy() {
		subjectField.setText("");
		issuer.setText("");
		identity.setText("");
		strength.setText("");
		timeLeft.setText("");
		certificates.clearTable();
	}

	private void showProxy(GlobusCredential cred) {
		clearProxy();
		subjectField.setText(cred.getSubject());
		issuer.setText(cred.getIssuer());
		identity.setText(cred.getIdentity());
		strength.setText(cred.getStrength() + " bits");
		cred.getTimeLeft();
		GregorianCalendar c = new GregorianCalendar();
		c.add(Calendar.SECOND, (int) cred.getTimeLeft());
		timeLeft.setText(c.getTime().toString());
		X509Certificate[] certs = cred.getCertificateChain();
		for (int i = 0; i < certs.length; i++) {
			certificates.addCertificate(certs[i]);
		}
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints14.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints14.weightx = 1.0D;
			gridBagConstraints14.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTH;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.weighty = 1.0D;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 2;
			gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
			mainPanel.add(getProxyInformation(), gridBagConstraints1);
			mainPanel.add(getButtonPanel(), gridBagConstraints4);
			mainPanel.add(getProxyPanel(), gridBagConstraints14);
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
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.weightx = 0.0D;
			gridBagConstraints2.weighty = 0.0D;
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.weighty = 0.0D;
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			proxyInformation.add(subjectLabel, gridBagConstraints2);

			issuerLabel.setText("Issuer");
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			identityLabel.setText("Identity");
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.gridy = 2;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints8.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.gridy = 2;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
			proxyInformation.add(identityLabel, gridBagConstraints8);
			gridBagConstraints7.weightx = 1.0D;
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			proxyInformation.add(issuerLabel, gridBagConstraints6);
			gridBagConstraints5.weightx = 1.0D;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			strengthLabel.setText("Strength");
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.gridy = 4;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 4;
			gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			timeLeftLabel.setText("Expires");
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 5;
			gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints12.weightx = 1.0D;
			gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints12.gridy = 5;
			gridBagConstraints12.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints12.weighty = 0.0D;
			gridBagConstraints9.weightx = 1.0D;
			gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.weighty = 0.0D;
			gridBagConstraints7.weighty = 0.0D;
			gridBagConstraints5.weighty = 0.0D;
			proxyInformation.add(timeLeftLabel, gridBagConstraints11);
			proxyInformation.add(strengthLabel, gridBagConstraints10);

			proxyInformation
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Proxy Information",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									null, GumsLookAndFeel.getPanelLabelColor()));
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
			buttonPanel.add(getViewCertificate(), null);
			buttonPanel.add(getSaveProxy(), null);
			buttonPanel.add(getDeleteProxy(), null);
			buttonPanel.add(getSetDefaultProxy(), null);
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
			jButton.setIcon(GumsLookAndFeel.getCloseIcon());
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
			gridBagConstraints13.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints13.weightx = 1.0;
			certificateChain = new JPanel();
			certificateChain.setLayout(new GridBagLayout());
			certificateChain
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Certificate Chain",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									null, GumsLookAndFeel.getPanelLabelColor()));
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
		}
		return certificates;
	}

	/**
	 * This method initializes proxyPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getProxyPanel() {
		if (proxyPanel == null) {
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.gridy = 0;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.insets = new java.awt.Insets(2, 2, 2, 2);
			proxyPanel = new JPanel();
			proxyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, "Select Proxy",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					GumsLookAndFeel.getPanelLabelColor()));
			proxyPanel.setLayout(new GridBagLayout());
			proxyPanel.add(getProxy(), gridBagConstraints15);
		}
		return proxyPanel;
	}

	/**
	 * This method initializes proxy
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getProxy() {
		if (proxy == null) {
			proxy = new JComboBox();
			/*
			 * proxy.addItemListener(new java.awt.event.ItemListener() { public
			 * void itemStateChanged(java.awt.event.ItemEvent e) { } });
			 */
			proxy.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ProxyCaddy caddy = (ProxyCaddy) getProxy()
							.getSelectedItem();
					if (caddy != null) {
						if (caddy.getIdentity() == DEFAULT_PROXY) {
							try {
								clearProxy();
								caddy.setProxy(ProxyUtil.getDefaultProxy());	
							} catch (Exception ex) {
								return;
							}
						}
						showProxy(caddy.getProxy());
					}
				}
			});
		}
		return proxy;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getViewCertificate() {
		if (viewCertificate == null) {
			viewCertificate = new JButton();
			viewCertificate.setText("View Certificate");
			viewCertificate.setIcon(GumsLookAndFeel.getProxyIcon());
			viewCertificate
					.addActionListener(new java.awt.event.ActionListener() {

						public void actionPerformed(java.awt.event.ActionEvent e) {
							try {
								getCertificates().doubleClick();
							} catch (Exception ex) {
								PortalUtils.showErrorMessage(ex);
							}
						}
					});
		}
		return viewCertificate;
	}

	/**
	 * This method initializes saveProxy
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSaveProxy() {
		if (saveProxy == null) {
			saveProxy = new JButton();
			saveProxy.setText("Save Proxy");
			saveProxy.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					saveProxyNow();

				}
			});
			saveProxy.setIcon(GumsLookAndFeel.getSaveIcon());
		}
		return saveProxy;
	}

	private void saveProxyNow() {
		try {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.showSaveDialog(this);
			ProxyCaddy caddy = (ProxyCaddy) getProxy().getSelectedItem();
			ProxyUtil.saveProxy(caddy.getProxy(), fc.getSelectedFile()
					.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
			PortalUtils
					.showErrorMessage("An unexpected error occurred in saving the currently selected proxy!!!");
		}
	}

	/**
	 * This method initializes setDefaultProxy
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSetDefaultProxy() {
		if (setDefaultProxy == null) {
			setDefaultProxy = new JButton();
			setDefaultProxy.setText("Set Default");
			setDefaultProxy
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							try {
								ProxyCaddy caddy = (ProxyCaddy) getProxy()
										.getSelectedItem();
								ProxyUtil.saveProxyAsDefault(caddy.getProxy());
							} catch (Exception ex) {
								ex.printStackTrace();
								PortalUtils
										.showErrorMessage("An unexpected error occurred in setting the default proxy!!!");
							}
						}
					});
			setDefaultProxy.setIcon(GumsLookAndFeel.getGreenFlagIcon());
		}
		return setDefaultProxy;
	}

	/**
	 * This method initializes deleteProxy
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDeleteProxy() {
		if (deleteProxy == null) {
			deleteProxy = new JButton();
			deleteProxy.setText("Delete Proxy");
			deleteProxy.setIcon(GumsLookAndFeel.getDeleteProxyIcon());
			deleteProxy.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ProxyCaddy caddy = (ProxyCaddy) getProxy()
							.getSelectedItem();
					clearProxy();
					getProxy().removeItemAt(getProxy().getSelectedIndex());
					if (caddy.getIdentity() == DEFAULT_PROXY) {
						ProxyUtil.destroyDefaultProxy();
					}else{
						ProxyManager.getInstance()
						.deleteProxy(caddy.getProxy());
						
					}
				}
			});
		}
		return deleteProxy;
	}
}
