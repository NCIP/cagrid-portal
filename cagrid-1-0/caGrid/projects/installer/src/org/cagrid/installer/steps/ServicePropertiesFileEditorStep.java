/**
 * 
 */
package org.cagrid.installer.steps;

import javax.swing.Icon;


/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ServicePropertiesFileEditorStep extends
		DeployPropertiesFileEditorStep {

	/**
	 * 
	 */
	public ServicePropertiesFileEditorStep() {

	}

	/**
	 * @param serviceName
	 * @param name
	 * @param summary
	 * @param propertyNameColumnName
	 * @param propertyValueColumnValue
	 */
	public ServicePropertiesFileEditorStep(String serviceName, String name,
			String summary, String propertyNameColumnName,
			String propertyValueColumnValue) {
		super(serviceName, name, summary, propertyNameColumnName,
				propertyValueColumnValue);

	}

	/**
	 * @param serviceName
	 * @param name
	 * @param summary
	 * @param propertyNameColumnName
	 * @param propertyValueColumnValue
	 * @param icon
	 */
	public ServicePropertiesFileEditorStep(String serviceName, String name,
			String summary, String propertyNameColumnName,
			String propertyValueColumnValue, Icon icon) {
		super(serviceName, name, summary, propertyNameColumnName,
				propertyValueColumnValue, icon);

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.steps.AbstractPropertiesFileEditorStep#getPropertyFilePath()
	 */
	@Override
	protected String getPropertyFilePath() {
		return this.model.getServiceDestDir() + "/"
				+ this.serviceName + "/service.properties";
	}

}
