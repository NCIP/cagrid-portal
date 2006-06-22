package gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties;

import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class ModifyResourcePropertiesComponent extends JDialog {

	private JPanel mainPanel = null;
	private JPanel resourcesPanel = null;
	private JPanel buttonPanel = null;
	private ResourcePropertiesListType properties;
	private NamespacesType namespaces;
	private JButton doneButton = null;
	private boolean showW3Cnamespaces;
	
	/**
	 * This method initializes 
	 * 
	 */
	public ModifyResourcePropertiesComponent(ResourcePropertiesListType properties, NamespacesType namespaces, boolean showW3Cnamespaces) {
		super();
		this.properties = properties;
		this.namespaces = namespaces;
		this.showW3Cnamespaces = showW3Cnamespaces;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new java.awt.Dimension(401,244));
        this.setContentPane(getMainPanel());
			
	}

	/**
	 * This method initializes mainPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.weighty = 0.0D;
			gridBagConstraints1.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridx = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getResourcesPanel(), gridBagConstraints);
			mainPanel.add(getButtonPanel(), gridBagConstraints1);
		}
		return mainPanel;
	}

	/**
	 * This method initializes resourcesPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResourcesPanel() {
		if (resourcesPanel == null) {
			resourcesPanel = new ModifyResourcePropertiesPanel(properties,namespaces,showW3Cnamespaces);
		}
		return resourcesPanel;
	}

	/**
	 * This method initializes buttonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getDoneButton(), gridBagConstraints2);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes doneButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDoneButton() {
		if (doneButton == null) {
			doneButton = new JButton();
			doneButton.setText("Done");
			doneButton.setIcon(IntroduceLookAndFeel.getDoneIcon());
			doneButton.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					dispose();
				}
			});
		}
		return doneButton;
	}
	

}  //  @jve:decl-index=0:visual-constraint="10,10"
