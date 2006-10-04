/*-----------------------------------------------------------------------------
 * Copyright (c) 2003-2004, The Ohio State University,
 * Department of Biomedical Informatics, Multiscale Computing Laboratory
 * All rights reserved.
 * 
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3  All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement: This product includes
 *    material developed by the Mobius Project (http://www.projectmobius.org/).
 * 
 * 4. Neither the name of the Ohio State University, Department of Biomedical
 *    Informatics, Multiscale Computing Laboratory nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 
 * 5. Products derived from this Software may not be called "Mobius"
 *    nor may "Mobius" appear in their names without prior written
 *    permission of Ohio State University, Department of Biomedical
 *    Informatics, Multiscale Computing Laboratory
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *---------------------------------------------------------------------------*/

package gov.nih.nci.cagrid.introduce.portal.modification.services;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.modification.services.methods.MethodsPopUpMenu;
import gov.nih.nci.cagrid.introduce.portal.modification.services.methods.MethodsTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties.ResourcePropertiesPopUpMenu;
import gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties.ResourcePropertiesTypeTreeNode;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.tree.DefaultMutableTreeNode;


public class ServiceButtonPanel extends ServiceContextsOptionsPanel {

	private JButton modifyServiceButton = null;
	private JButton addMethodButton = null;
	private JButton modifyResourcesButton = null;


	public ServiceButtonPanel(ServicesJTree tree) {
		super(tree);
		initialize();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints2.gridy = 2;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		this.setLayout(new GridBagLayout());
		this.add(getModifyServiceButton(), gridBagConstraints);
		this.add(getAddMethodButton(), gridBagConstraints1);
		this.add(getModifyResourcesButton(), gridBagConstraints2);
	}


	/**
	 * This method initializes modifyServiceButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getModifyServiceButton() {
		if (modifyServiceButton == null) {
			modifyServiceButton = new JButton();
			modifyServiceButton.setText("Modify Service");
			modifyServiceButton.setIcon(IntroduceLookAndFeel.getModifyServiceIcon());
			modifyServiceButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					DefaultMutableTreeNode tnode = ServiceButtonPanel.this.getTree().getCurrentNode();
					if (tnode instanceof ServiceTypeTreeNode) {
						ServiceTypeTreeNode node = (ServiceTypeTreeNode) tnode;
						ModifyService comp = new ModifyService(node, new SpecificServiceInformation(node.getInfo(),
							node.getServiceType()), false);
						comp.pack();
						PortalUtils.centerWindow(comp);
						comp.setVisible(true);
					}
				}

			});
		}
		return modifyServiceButton;
	}


	/**
	 * This method initializes addMethodButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddMethodButton() {
		if (addMethodButton == null) {
			addMethodButton = new JButton();
			addMethodButton.setText("Add Method");
			addMethodButton.setIcon(IntroduceLookAndFeel.getMethodIcon());
			addMethodButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					DefaultMutableTreeNode tnode = ServiceButtonPanel.this.getTree().getCurrentNode();
					if (tnode instanceof MethodsTypeTreeNode) {
						MethodsPopUpMenu.addMethod((MethodsTypeTreeNode) tnode);
					}
				}

			});
		}
		return addMethodButton;
	}


	/**
	 * This method initializes modifyResourcesButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getModifyResourcesButton() {
		if (modifyResourcesButton == null) {
			modifyResourcesButton = new JButton();
			modifyResourcesButton.setText("Modify Resources");
			modifyResourcesButton.setIcon(IntroduceLookAndFeel.getResourcePropertiesIcon());
			modifyResourcesButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					DefaultMutableTreeNode tnode = ServiceButtonPanel.this.getTree().getCurrentNode();
					if (tnode instanceof ResourcePropertiesTypeTreeNode) {
						ResourcePropertiesPopUpMenu.modifyResourceProperties((ResourcePropertiesTypeTreeNode) tnode);
					}
				}

			});
		}
		return modifyResourcesButton;
	}

}
