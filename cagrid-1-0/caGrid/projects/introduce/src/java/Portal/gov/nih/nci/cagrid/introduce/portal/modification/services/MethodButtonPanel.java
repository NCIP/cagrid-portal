package gov.nih.nci.cagrid.introduce.portal.modification.services;

import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.modification.services.methods.MethodPopUpMenu;
import gov.nih.nci.cagrid.introduce.portal.modification.services.methods.MethodTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.services.methods.MethodViewer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.tree.DefaultMutableTreeNode;

import org.projectmobius.portal.PortalResourceManager;


public class MethodButtonPanel extends ServiceContextsOptionsPanel {

	private JButton addServiceButton = null;


	/**
	 * This method initializes
	 */
	public MethodButtonPanel(ServicesJTree tree) {
		super(tree);
		initialize();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.add(getAddServiceButton(), gridBagConstraints);

	}


	/**
	 * This method initializes addServiceButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddServiceButton() {
		if (addServiceButton == null) {
			addServiceButton = new JButton();
			addServiceButton.setText("Modify Method");
			addServiceButton.setIcon(IntroduceLookAndFeel.getAddIcon());
			addServiceButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					DefaultMutableTreeNode tnode = MethodButtonPanel.this.getTree().getCurrentNode();
					if (tnode instanceof MethodTypeTreeNode) {
						PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(
							new MethodViewer(((MethodTypeTreeNode) tnode).getMethod(), ((MethodTypeTreeNode) tnode)
								.getInfo()));
					}

				}

			});
		}
		return addServiceButton;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
