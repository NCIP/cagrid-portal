package org.cagrid.grape;

import javax.swing.ImageIcon;

import org.cagrid.grape.model.Configuration;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class ConfigurationTreeNode extends ConfigurationBaseTreeNode {

	public ConfigurationTreeNode(ConfigurationWindow window, ConfigurationTree tree, ConfigurationManager conf)
		throws Exception {
		super(window, tree, conf);
		processConfiguration(conf.getConfiguration());
		this.setDisplayPanel(new ConfigurationDisplayPanel("Preferences"));
	}


	private void processConfiguration(Configuration c) throws Exception {
		if (c != null) {
			this.processConfigurationGroups(c.getConfigurationGroups());
			this.processConfigurationDescriptors(c.getConfigurationDescriptors());
		}

	}


	public void applyChanges() throws Exception {
		for (int i = 0; i < this.getChildCount(); i++) {
			ConfigurationBaseTreeNode node = (ConfigurationBaseTreeNode) getChildAt(i);
			node.applyChanges();
		}
	}


	public ImageIcon getIcon() {
		return LookAndFeel.getPreferencesTreeIcon();
	}


	public String toString() {
		return "Preferences";
	}
}
