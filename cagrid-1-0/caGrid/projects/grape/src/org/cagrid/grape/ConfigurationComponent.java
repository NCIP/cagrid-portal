package org.cagrid.grape;


public abstract class ConfigurationComponent extends ApplicationComponent{
	
	private Object configurationObject;
	
	public ConfigurationComponent(ApplicationContext context, Object conf) {
		super(context);
		this.configurationObject = conf;
	}

	public Object getConfigurationObject() {
		return configurationObject;
	}
		
	
}
