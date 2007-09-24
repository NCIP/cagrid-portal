package org.cagrid.gaards.ui.gts;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.gts.bean.Permission;
import gov.nih.nci.cagrid.gts.bean.PermissionFilter;
import gov.nih.nci.cagrid.gts.bean.Role;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter;
import gov.nih.nci.cagrid.gts.client.GTSPublicClient;
import gov.nih.nci.cagrid.gts.common.Constants;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: TrustedAuthoritiesWindow.java,v 1.2 2006/03/27 19:05:40
 *          langella Exp $
 */
public class PermissionPanel extends JPanel {

	public final static String ANY = "Any";

	private JLabel jLabel = null;

	private JTextField gid = null;

	private JLabel jLabel1 = null;

	private JComboBox trustedAuthorities = null;

	private JLabel jLabel2 = null;

	private JComboBox role = null;

	private String currentService = null;

	private int currentLength = 0;

	private boolean wildcards = false;


	/**
	 * This is the default constructor
	 */
	public PermissionPanel(boolean useWildcards) {
		super();
		this.wildcards = useWildcards;
		initialize();
	}


	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints5.gridy = 2;
		gridBagConstraints5.weightx = 1.0;
		gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints5.gridx = 1;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.gridy = 2;
		gridBagConstraints4.insets = new java.awt.Insets(2, 2, 2, 2);
		jLabel2 = new JLabel();
		jLabel2.setText("Role");
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints3.gridy = 1;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints3.gridx = 1;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints1.gridy = 1;
		jLabel1 = new JLabel();
		jLabel1.setText("Trusted Authority");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints.weightx = 1.0;
		jLabel = new JLabel();
		jLabel.setText("Grid Identity");
		this.setLayout(new GridBagLayout());
		this.setSize(300, 200);
		this.add(jLabel, gridBagConstraints2);
		this.add(getGid(), gridBagConstraints);
		this.add(jLabel1, gridBagConstraints1);
		this.add(getTrustedAuthorities(), gridBagConstraints3);
		this.add(jLabel2, gridBagConstraints4);
		this.add(getRole(), gridBagConstraints5);
	}


	/**
	 * This method initializes gridIdentity
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getGid() {
		if (gid == null) {
			gid = new JTextField();
			gid.setText("");
		}
		return gid;
	}


	/**
	 * This method initializes trustedAuthority
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getTrustedAuthorities() {
		if (trustedAuthorities == null) {
			trustedAuthorities = new JComboBox();
			trustedAuthorities.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					syncRoles();
				}
			});
		}
		return trustedAuthorities;
	}


	public PermissionFilter getPermissionFilter() {
		PermissionFilter f = new PermissionFilter();
		f.setGridIdentity(Utils.clean(this.gid.getText()));
		String ta = (String) trustedAuthorities.getSelectedItem();
		if (!ta.equals(ANY)) {
			f.setTrustedAuthorityName(ta);
		}

		if (!role.getSelectedItem().equals(ANY)) {
			f.setRole((Role) role.getSelectedItem());
		}
		return f;
	}


	public Permission getPermission() {
		Permission p = new Permission();
		p.setGridIdentity(Utils.clean(this.gid.getText()));
		String ta = (String) trustedAuthorities.getSelectedItem();
		if (!ta.equals(ANY)) {
			p.setTrustedAuthorityName(ta);
		}

		if (!role.getSelectedItem().equals(ANY)) {
			p.setRole((Role) role.getSelectedItem());
		}
		return p;
	}


	/**
	 * This method initializes role
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getRole() {
		if (role == null) {
			role = new JComboBox();
		}
		return role;
	}


	public synchronized int syncWithService(String service) throws Exception {
		try {

			if ((currentService == null) || (!currentService.equals(service))) {
				this.currentService = service;
				disableAll();
				this.trustedAuthorities.removeAllItems();
				if (wildcards) {
					this.trustedAuthorities.addItem(ANY);
				}
				this.trustedAuthorities.addItem(Constants.ALL_TRUST_AUTHORITIES);
				currentLength = 0;
				GTSPublicClient client = new GTSPublicClient(service);
				TrustedAuthority[] tas = client.findTrustedAuthorities(new TrustedAuthorityFilter());

				if (tas != null) {
					currentLength = tas.length;
					for (int i = 0; i < tas.length; i++) {
						this.trustedAuthorities.addItem(tas[i].getName());
					}
				}

				syncRoles();
			}
			return currentLength;
		} catch (Exception e) {
			throw e;
		} finally {
			enableAll();
		}

	}


	private void disableAll() {
		this.gid.setEnabled(false);
		this.trustedAuthorities.setEnabled(false);
		this.role.setEnabled(false);
	}


	private void enableAll() {
		this.gid.setEnabled(true);
		this.trustedAuthorities.setEnabled(true);
		this.role.setEnabled(true);
	}


	public synchronized void syncRoles() {
		this.role.removeAllItems();
		String ta = (String) this.trustedAuthorities.getSelectedItem();
		if (ta != null) {
			if (ta.equals(ANY)) {
				if (wildcards) {
					role.addItem(ANY);
				}
				role.addItem(Role.TrustServiceAdmin);
				role.addItem(Role.TrustAuthorityManager);
			} else if (ta.equals(Constants.ALL_TRUST_AUTHORITIES)) {
				if (wildcards) {
					role.addItem(ANY);
				}
				role.addItem(Role.TrustServiceAdmin);
			} else {
				if (wildcards) {
					role.addItem(ANY);
				}
				role.addItem(Role.TrustAuthorityManager);
			}
		}
	}

}
