package org.cagrid.grape;

import javax.swing.JPanel;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @created Oct 14, 2004
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public abstract class ConfigurationBasePanel extends JPanel {

	private String systemName;
	private Object configurationObject;
	private boolean changed;


	public ConfigurationBasePanel(String systemName, Object conf) {
		this.systemName = systemName;
		this.configurationObject = conf;
		this.changed = false;
	}


	public Object getConfigurationObject() {
		return configurationObject;
	}


	public String getSystemName() {
		return systemName;
	}


	public void setChanged(boolean changed) {
		this.changed = changed;
	}


	public boolean hasChanged() {
		return changed;
	}
}
