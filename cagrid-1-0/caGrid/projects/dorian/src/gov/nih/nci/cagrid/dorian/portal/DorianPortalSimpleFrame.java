package gov.nih.nci.cagrid.dorian.portal;



import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.projectmobius.portal.GridPortalBaseFrame;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public abstract class DorianPortalSimpleFrame extends GridPortalBaseFrame {

	private javax.swing.JPanel jContentPane = null;
	private JPanel mainPanel = null;
	private JPanel contentPanel = null;
	private JPanel buttonPanel = null;
	private JButton action = null;
	private JButton cancel = null;


	/**
	 * This is the default constructor
	 */
	public DorianPortalSimpleFrame(String actionText, String contentPanelLabel) {
		super();
		initialize();
		this.getAction().setText(actionText);
		contentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, contentPanelLabel,
			javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
			null, DorianLookAndFeel.getPanelLabelColor()));
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());

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
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.ipadx = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.weighty = 1.0D;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			mainPanel.add(getContentPanel(), gridBagConstraints1);
			mainPanel.add(getButtonPanel(), gridBagConstraints2);
		}
		return mainPanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setLayout(new GridBagLayout());
		}
		return contentPanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getAction(), null);
			buttonPanel.add(getCancel(), null);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAction() {
		if (action == null) {
			action = new JButton();
			action.setText("Perform");
			action.setIcon(DorianLookAndFeel.getSelectIcon());
			action.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					perform();
				}
			});
		}
		return action;
	}


	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancel() {
		if (cancel == null) {
			cancel = new JButton();
			cancel.setText("Cancel");
			cancel.setIcon(DorianLookAndFeel.getCloseIcon());
			cancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return cancel;
	}


	protected void addContent(String label, String value, int index, boolean enabled) {
		this.addTextField(this.contentPanel, label, value, index, enabled);
	}


	protected String getContentErrorIfEmpty(String name) throws Exception {
		String val = this.getTextFieldValue(name);
		if ((val == null) || (val.trim().length() <= 0)) {
			throw new Exception("No Value specified for " + name);
		}
		return val;
	}


	protected void setContent(String name, String value) {
		this.setTextFieldValue(name, value);
	}


	protected abstract void perform();

}
