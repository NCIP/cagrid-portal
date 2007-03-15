package org.cagrid.grape;

import javax.swing.ImageIcon;

import org.cagrid.grape.model.ConfigurationDescriptor;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class ConfigurationDescriptorTreeNode extends ConfigurationBaseTreeNode {

	private ConfigurationDescriptor des;


	public ConfigurationDescriptorTreeNode(ConfigurationWindow window, ConfigurationTree tree,
		ConfigurationManager conf, ConfigurationDescriptor des) throws Exception {
		super(window, tree, conf);
		this.des = des;
		this.setDisplayPanel(new ConfigurationDisplayPanel(this.des.getDisplayName()));
	}


	public ImageIcon getIcon() {
		return LookAndFeel.getConfigurationPropertyIcon();
	}


	public String toString() {
		return des.getDisplayName();
	}

}
