package org.cagrid.grape;

import org.cagrid.grape.model.Component;

public class ApplicationContext {

	private GridApplication application;

	private Component component;
	
	public ApplicationContext(GridApplication application) {
		this(application, null);
	}


	public ApplicationContext(GridApplication application, Component component) {
		this.application = application;
		this.component = component;
	}

	public Component getComponent() {
		return component;
	}

	public void addApplicationComponent(ApplicationComponent comp) {
		this.application.addApplicationComponent(comp);
	}

	public void addApplicationComponent(ApplicationComponent comp, int width, int height) {
		this.application.addApplicationComponent(comp, width, height);
	}

}
