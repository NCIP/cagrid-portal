package org.cagrid.grape;

import java.lang.reflect.Constructor;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
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

	private Logger log;


	public ConfigurationDescriptorTreeNode(ConfigurationWindow window, ConfigurationTree tree,
		ConfigurationManager conf, ConfigurationDescriptor des) throws Exception {
		super(window, tree, conf);
		this.des = des;
		log = Logger.getLogger(this.getClass().getName());
		if (des.getConfigurationPanel() == null) {
			this.setDisplayPanel(new ConfigurationDisplayPanel(this.des.getDisplayName()));
		} else {
			try {
				Class[] types = new Class[2];
				types[0] = String.class;
				types[1] = Object.class;
				Constructor c = Class.forName(des.getConfigurationPanel()).getConstructor(types);
				Object[] args = new Object[2];
				args[0] = des.getSystemName();
				args[1] = this.getConfigurationManager().getConfigurationObject(des.getSystemName());
				this.setDisplayPanel((ConfigurationBasePanel) c.newInstance(args));

			} catch (Exception e) {
				this.setDisplayPanel(new ConfigurationDisplayPanel(this.des.getDisplayName()));
				log.error("An error occurred using the panel " + des.getConfigurationPanel()
					+ " for editing the preference " + des.getDisplayName());
				log.error(e.getMessage(), e);
			}
		}
	}


	public ImageIcon getIcon() {
		return LookAndFeel.getConfigurationPropertyIcon();
	}


	public String toString() {
		return des.getDisplayName();
	}

}
