package gov.nih.nci.cagrid.introduce.portal.discoverytools.core;

import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.portal.PortalResourceManager;

import gov.nih.nci.cagrid.introduce.portal.IntroducePortalConf;
import gov.nih.nci.cagrid.introduce.portal.NamespaceTypeToolDescriptor;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JTabbedPane;
import java.awt.GridBagConstraints;
import java.util.List;

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
        this.setSize(new java.awt.Dimension(284,182));
        this.setContentPane(getMainPanel());
        this.setTitle("Discovery Tools");
			
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
			IntroducePortalConf conf = (IntroducePortalConf) PortalResourceManager.getInstance().getResource(
				IntroducePortalConf.RESOURCE);
			List tools = conf.getNamespaceToolsComponents();
			if(tools!=null){
				for(int i = 0; i < tools.size(); i++){
					NamespaceTypeToolDescriptor desc = (NamespaceTypeToolDescriptor)tools.get(i);
					try {
						contentTabbedPane.addTab(desc.getType(),desc.getNamespaceTypeToolComponent());
					} catch (Exception e) {
						JOptionPane.showMessageDialog(TypesToolsComponent.this,"Could not load types tool: " + desc.getType());
					}
				}
			}
		}
		return contentTabbedPane;
	}

}  // @jve:decl-index=0:visual-constraint="10,10"
