package gov.nih.nci.cagrid.gridgrouper.ui;

import gov.nih.nci.cagrid.gridgrouper.grouper.Stem;

import java.awt.GridBagLayout;

import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Insets;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class StemBrowser extends JPanel {

	private static final long serialVersionUID = 1L;

	private StemTreeNode node;

	private Stem stem;

	private JPanel stemProperties = null;

	private JLabel jLabel = null;

	private JTextField serviceURI = null;

	private JLabel jLabel1 = null;

	private JTextField stemName = null;

	private JLabel jLabel2 = null;

	private JTextField credentials = null;

	/**
	 * This is the default constructor
	 */
	public StemBrowser(StemTreeNode node) {
		super();
		this.node = node;
		this.stem = node.getStem();
		initialize();
		this.setStem();
	}

	private void setStem() {
		this.serviceURI.setText(this.node.getGridGrouper().getName());
		this.stemName.setText(stem.getDisplayExtension());
		if (node.getGridGrouper().getProxyIdentity() == null) {
			this.credentials.setText("None");
		} else {
			this.credentials.setText(node.getGridGrouper().getProxyIdentity());
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints.gridy = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getStemProperties(), gridBagConstraints);
	}

	/**
	 * This method initializes stemProperties
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getStemProperties() {
		if (stemProperties == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridy = 2;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints5.gridy = 2;
			jLabel2 = new JLabel();
			jLabel2.setText("Credentials");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints4.gridx = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints3.gridy = 1;
			jLabel1 = new JLabel();
			jLabel1.setText("Name");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints1.weightx = 1.0;
			jLabel = new JLabel();
			jLabel.setText("Grid Grouper");
			stemProperties = new JPanel();
			stemProperties.setLayout(new GridBagLayout());
			stemProperties.add(jLabel, gridBagConstraints2);
			stemProperties.add(getServiceURI(), gridBagConstraints1);
			stemProperties.add(jLabel1, gridBagConstraints3);
			stemProperties.add(getStemName(), gridBagConstraints4);
			stemProperties.add(jLabel2, gridBagConstraints5);
			stemProperties.add(getCredentials(), gridBagConstraints6);
		}
		return stemProperties;
	}

	/**
	 * This method initializes serviceURI
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getServiceURI() {
		if (serviceURI == null) {
			serviceURI = new JTextField();
			this.serviceURI.setEditable(false);
		}
		return serviceURI;
	}

	/**
	 * This method initializes stemName
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getStemName() {
		if (stemName == null) {
			stemName = new JTextField();
			stemName.setEditable(false);
		}
		return stemName;
	}

	/**
	 * This method initializes credentials
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCredentials() {
		if (credentials == null) {
			credentials = new JTextField();
			credentials.setEditable(false);
		}
		return credentials;
	}

}
