package gov.nih.nci.cagrid.gridca.ui;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.common.security.ProxyUtil;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

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

	private JPanel buttonPanel = null;

	private JButton jButton = null;

	private JPanel proxyPanel = null;

	private JComboBox proxy = null;

	private JButton viewCertificate = null;

	private JButton saveProxy = null;

	private JButton setDefaultProxy = null;

	private static final String DEFAULT_PROXY = "Globus Default Proxy";

	private JButton deleteProxy = null;

	private ProxyCaddy defaultProxy;

	private ProxyPanel proxyInformation = null;


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
		this.setContentPane(getJContentPane());
		this.setFrameIcon(GridCALookAndFeel.getProxyManagerIcon());
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




	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridy = 1;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints14.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints14.weightx = 1.0D;
			gridBagConstraints14.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 2;
			gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
			mainPanel.add(getButtonPanel(), gridBagConstraints4);
			mainPanel.add(getProxyPanel(), gridBagConstraints14);
			mainPanel.add(getProxyInformation(), gridBagConstraints);
		}
		return mainPanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */



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
			jButton.setIcon(GridCALookAndFeel.getCloseIcon());
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return jButton;
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
			proxyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Proxy",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null,GridCALookAndFeel.getPanelLabelColor()));
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
					ProxyCaddy caddy = (ProxyCaddy) getProxy().getSelectedItem();
					if (caddy != null) {
						if (caddy.getIdentity() == DEFAULT_PROXY) {
							try {
								proxyInformation.clearProxy();
								caddy.setProxy(ProxyUtil.getDefaultProxy());
							} catch (Exception ex) {
								return;
							}
						}
						proxyInformation.showProxy(caddy.getProxy());
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
			viewCertificate.setIcon(GridCALookAndFeel.getCertificateIcon());
			viewCertificate.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						proxyInformation.getCertificates().doubleClick();
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
			saveProxy.setIcon(GridCALookAndFeel.getSaveIcon());
		}
		return saveProxy;
	}


	private void saveProxyNow() {
		try {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnVal = fc.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				ProxyCaddy caddy = (ProxyCaddy) getProxy().getSelectedItem();
				ProxyUtil.saveProxy(caddy.getProxy(), fc.getSelectedFile().getAbsolutePath());
			}
		} catch (Exception e) {
			e.printStackTrace();
			PortalUtils.showErrorMessage("An unexpected error occurred in saving the currently selected proxy!!!");
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
			setDefaultProxy.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						ProxyCaddy caddy = (ProxyCaddy) getProxy().getSelectedItem();
						ProxyUtil.saveProxyAsDefault(caddy.getProxy());
					} catch (Exception ex) {
						ex.printStackTrace();
						PortalUtils.showErrorMessage("An unexpected error occurred in setting the default proxy!!!");
					}
				}
			});
			setDefaultProxy.setIcon(GridCALookAndFeel.getDefaultIcon());
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
			deleteProxy.setIcon(GridCALookAndFeel.getRemoveIcon());
			deleteProxy.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ProxyCaddy caddy = (ProxyCaddy) getProxy().getSelectedItem();
					proxyInformation.clearProxy();
					getProxy().removeItemAt(getProxy().getSelectedIndex());
					if (caddy.getIdentity() == DEFAULT_PROXY) {
						ProxyUtil.destroyDefaultProxy();
					} else {
						ProxyManager.getInstance().deleteProxy(caddy.getProxy());

					}
				}
			});
		}
		return deleteProxy;
	}


	/**
	 * This method initializes proxyInformation	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private ProxyPanel getProxyInformation() {
		if (proxyInformation == null) {
			proxyInformation = new ProxyPanel();
		}
		return proxyInformation;
	}
}
