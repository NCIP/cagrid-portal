package org.cagrid.gaards.ui.dorian;

import gov.nih.nci.cagrid.dorian.client.IFSAdministrationClient;
import gov.nih.nci.cagrid.dorian.client.IFSUserClient;
import gov.nih.nci.cagrid.dorian.client.IdPAdministrationClient;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.cagrid.gaards.ui.common.ProxyComboBox;
import org.cagrid.grape.LookAndFeel;
import org.globus.gsi.GlobusCredential;

public class SessionPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel jLabel = null;

	private DorianServiceListComboBox service = null;

	private JLabel jLabel1 = null;

	private ProxyComboBox cred = null;

	/**
	 * This is the default constructor
	 */
	public SessionPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints3.gridy = 1;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.anchor = GridBagConstraints.WEST;
		gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints3.gridx = 1;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.gridy = 1;
		jLabel1 = new JLabel();
		jLabel1.setText("Credential");
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
		jLabel.setText("Service URI");
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder(null,
				"Session Information", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, null, LookAndFeel
						.getPanelLabelColor()));
		this.add(jLabel, gridBagConstraints1);
		this.add(getService(), gridBagConstraints);
		this.add(jLabel1, gridBagConstraints2);
		this.add(getCred(), gridBagConstraints3);
	}

	/**
	 * This method initializes service
	 * 
	 * @return javax.swing.JComboBox
	 */
	private DorianServiceListComboBox getService() {
		if (service == null) {
			service = new DorianServiceListComboBox();
		}
		return service;
	}

	/**
	 * This method initializes cred
	 * 
	 * @return javax.swing.JComboBox
	 */
	private ProxyComboBox getCred() {
		if (cred == null) {
			cred = new ProxyComboBox();
		}
		return cred;
	}

	public IFSAdministrationClient getAdminClient() throws Exception {
		String serviceUrl = getService().getSelectedService();
		GlobusCredential proxyCred = getCred().getSelectedProxy();
		IFSAdministrationClient client = new IFSAdministrationClient(
				serviceUrl, proxyCred);
		return client;
	}

	public IFSUserClient getUserClientWithCredentials() throws Exception {
		String serviceUrl = getService().getSelectedService();
		GlobusCredential proxyCred = getCred().getSelectedProxy();
		IFSUserClient client = new IFSUserClient(serviceUrl, proxyCred);
		return client;
	}

	public IdPAdministrationClient getLocalAdminClient() throws Exception {
		String serviceUrl = getService().getSelectedService();
		GlobusCredential proxyCred = getCred().getSelectedProxy();
		IdPAdministrationClient client = new IdPAdministrationClient(
				serviceUrl, proxyCred);
		return client;
	}

	public String getServiceURI() {
		return getService().getSelectedService();
	}

	public GlobusCredential getCredential() throws Exception {
		return getCred().getSelectedProxy();
	}

}