package gov.nih.nci.cagrid.introduce.portal.discoverytools.core;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.discoverytools.NamespaceTypeToolsComponent;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.projectmobius.portal.GridPortalComponent;


public class TypesToolsComponent extends GridPortalComponent {

	private JPanel mainPanel = null;
	private JTabbedPane contentTabbedPane = null;


	/**
	 * This method initializes
	 */
	public TypesToolsComponent() {
		super();
		initialize();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setContentPane(getMainPanel());
		this.setTitle("Discovery Tools");
		this.setFrameIcon(IntroduceLookAndFeel.getDiscoveryToolsIcon());
	}


	/**
	 * This method initializes mainPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getContentTabbedPane(), gridBagConstraints);
		}
		return mainPanel;
	}


	/**
	 * This method initializes contentTabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getContentTabbedPane() {
		if (contentTabbedPane == null) {
			contentTabbedPane = new JTabbedPane();
			List tools = ExtensionsLoader.getInstance().getDiscoveryExtensions();
			for (int i = 0; i < tools.size(); i++) {
				DiscoveryExtensionDescriptionType desc = (DiscoveryExtensionDescriptionType) tools.get(i);
				try {
					NamespaceTypeToolsComponent comp = gov.nih.nci.cagrid.introduce.portal.extension.ExtensionTools
						.getNamespaceTypeToolsComponent(desc.getName());
					if (comp != null) {
						contentTabbedPane.addTab(desc.getDisplayName(), comp);
					}
				} catch (Exception e) {
					ErrorDialog.showErrorDialog("Could not load types tool: "
						+ desc.getDisplayName());
					// JOptionPane.showMessageDialog(TypesToolsComponent.this, "Could not load types tool: "
					//	+ desc.getDisplayName());
				}
			}
		}

		return contentTabbedPane;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
