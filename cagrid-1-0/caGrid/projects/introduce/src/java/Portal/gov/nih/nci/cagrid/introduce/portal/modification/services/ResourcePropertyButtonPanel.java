package gov.nih.nci.cagrid.introduce.portal.modification.services;

import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.modification.services.methods.MethodTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.services.methods.MethodViewer;
import gov.nih.nci.cagrid.introduce.portal.modification.services.methods.MethodsTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties.ResourcePropertiesTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties.ResourcePropertyTypeTreeNode;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


public class ResourcePropertyButtonPanel extends ServiceContextsOptionsPanel {

	private JButton removeResourcePropertyButton = null;
	/**
	 * This method initializes
	 */
	public ResourcePropertyButtonPanel(ServicesJTree tree) {
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
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.add(getRemoveResourcePropertyButton(), gridBagConstraints);
	}


	public void setCanModify(boolean canModify) {
		this.getRemoveResourcePropertyButton().setEnabled(canModify);
	}


	/**
	 * This method initializes addServiceButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRemoveResourcePropertyButton() {
		if (removeResourcePropertyButton == null) {
		    removeResourcePropertyButton = new JButton();
		    removeResourcePropertyButton.setText("Remove Resource Property");
		    removeResourcePropertyButton.setIcon(IntroduceLookAndFeel.getRemoveResourcePropertyIcon());
		    removeResourcePropertyButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					DefaultMutableTreeNode tnode = ResourcePropertyButtonPanel.this.getTree().getCurrentNode();
					if (tnode instanceof ResourcePropertyTypeTreeNode) {
					    ResourcePropertiesTypeTreeNode parent = ((ResourcePropertiesTypeTreeNode) tnode.getParent());
					    CommonTools.removeResourceProperty(parent.getService(), ((ResourcePropertyType) tnode.getUserObject()).getQName());
                        parent.remove(tnode);  
                        ServicesJTree.getInstance().setServices(parent.getInfo());
					}

				}

			});
		}
		return removeResourcePropertyButton;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
