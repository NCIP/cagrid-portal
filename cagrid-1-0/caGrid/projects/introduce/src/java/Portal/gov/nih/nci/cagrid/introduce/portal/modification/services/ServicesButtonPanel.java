package gov.nih.nci.cagrid.introduce.portal.modification.services;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.modification.common.ServiceContextsOptionsPanel;
import gov.nih.nci.cagrid.introduce.portal.modification.services.methods.MethodTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.services.methods.MethodsTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties.ResourcePropertiesTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties.ResourcePropertyTypeTreeNode;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;


public class ServicesButtonPanel extends ServiceContextsOptionsPanel {

	private JButton addServiceButton = null;


	/**
	 * This method initializes
	 */
	public ServicesButtonPanel(ServicesJTree tree) {
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
			addServiceButton.setText("Add Service");
			addServiceButton.setIcon(IntroduceLookAndFeel.getAddIcon());
			addServiceButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					DefaultMutableTreeNode node = ServicesButtonPanel.this.getTree().getCurrentNode();
						if (node instanceof ServicesTypeTreeNode) {

							ServiceType service = new ServiceType();
							service.setMethods(new MethodsType());
							service.setResourcePropertiesList(new ResourcePropertiesListType());
							service.setResourceFrameworkType(IntroduceConstants.INTRODUCE_BASE_RESOURCE);
							// service.setServiceSecurity(new
							// ServiceSecurity());
							service.setMethods(new MethodsType());
							ServiceTypeTreeNode newNode = ServicesButtonPanel.this.getTree().getRoot().addService(
								service);
							ModifyService comp = new ModifyService(newNode, new SpecificServiceInformation(
								ServicesButtonPanel.this.getTree().getRoot().getInfo(), service));
							// PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(
							// new ModifyService(newNode, new
							// SpecificServiceInformation(node.getInfo(),service)));
							comp.pack();
							PortalUtils.centerWindow(comp);
							comp.setVisible(true);
						}
				}
			});
		}
		return addServiceButton;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
