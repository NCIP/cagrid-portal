package org.cagrid.grape;


public abstract class ConfigurationComponent extends ApplicationComponent{
	
	private String systemName;
	private Object configurationObject;
	
	public ConfigurationComponent(ApplicationContext context, String systemName, Object conf) {
		super(context);
		this.systemName = systemName;
		this.configurationObject = conf;
	}

	public Object getConfigurationObject() {
		return configurationObject;
	}

	public String getSystemName() {
		return systemName;
	}
		
	public void save() throws Exception{
		getContext().getConfigurationManager().save(getSystemName(), getConfigurationObject());
	}
	
	
}
