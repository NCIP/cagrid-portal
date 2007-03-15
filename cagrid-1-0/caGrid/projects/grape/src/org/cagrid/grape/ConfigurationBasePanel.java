package org.cagrid.grape;

import javax.swing.JPanel;


public abstract class ConfigurationBasePanel extends JPanel{
	
	private String systemName;
	private Object configurationObject;
	
	public ConfigurationBasePanel(String systemName, Object conf) {
		this.systemName = systemName;
		this.configurationObject = conf;
	}

	public Object getConfigurationObject() {
		return configurationObject;
	}

	public String getSystemName() {
		return systemName;
	}	
}
