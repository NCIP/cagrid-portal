package gov.nih.nci.cagrid.gridgrouper.ui.mygroups;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cagrid.grape.ApplicationComponent;
import org.cagrid.grape.MultiEventProgressBar;

import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MyGroupsWindow extends ApplicationComponent {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel metadataPanel = null;

	private JLabel jLabel = null;

	private JTextField gridIdentity = null;

	private JPanel progressPanel = null;

	private JPanel groupsPanel = null;

	private MultiEventProgressBar progress = null;

	private JScrollPane jScrollPane = null;

	private MyGroupsTable groups = null;

	/**
	 * This is the default constructor
	 */
	public MyGroupsWindow() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints21.fill = GridBagConstraints.BOTH;
			gridBagConstraints21.weightx = 1.0D;
			gridBagConstraints21.weighty = 1.0D;
			gridBagConstraints21.gridy = 2;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints11.weightx = 1.0D;
			gridBagConstraints11.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridheight = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.ipadx = 0;
			gridBagConstraints.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getMetadataPanel(), gridBagConstraints);
			jContentPane.add(getProgressPanel(), gridBagConstraints11);
			jContentPane.add(getGroupsPanel(), gridBagConstraints21);
		}
		return jContentPane;
	}

	/**
	 * This method initializes metadataPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMetadataPanel() {
		if (metadataPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints2.weightx = 1.0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
			jLabel = new JLabel();
			jLabel.setText("Grid Identity");
			metadataPanel = new JPanel();
			metadataPanel.setLayout(new GridBagLayout());
			metadataPanel.add(jLabel, gridBagConstraints1);
			metadataPanel.add(getGridIdentity(), gridBagConstraints2);
		}
		return metadataPanel;
	}

	/**
	 * This method initializes gridIdentity
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getGridIdentity() {
		if (gridIdentity == null) {
			gridIdentity = new JTextField();
			gridIdentity.setEditable(false);
		}
		return gridIdentity;
	}

	/**
	 * This method initializes progressPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getProgressPanel() {
		if (progressPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.insets = new Insets(0, 50, 0, 50);
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.gridy = 0;
			progressPanel = new JPanel();
			progressPanel.setLayout(new GridBagLayout());
			progressPanel.add(getProgress(), gridBagConstraints4);
		}
		return progressPanel;
	}

	/**
	 * This method initializes groupsPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getGroupsPanel() {
		if (groupsPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.weighty = 1.0;
			gridBagConstraints3.weightx = 1.0;
			groupsPanel = new JPanel();
			groupsPanel.setLayout(new GridBagLayout());
			groupsPanel.add(getJScrollPane(), gridBagConstraints3);
		}
		return groupsPanel;
	}

	/**
	 * This method initializes progress
	 * 
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getProgress() {
		if (progress == null) {
			progress = new MultiEventProgressBar(false);
		}
		return progress;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getGroups());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes groups	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getGroups() {
		if (groups == null) {
			groups = new MyGroupsTable();
		}
		return groups;
	}

}
