/**
 * 
 */
package org.cagrid.installer.steps;

import javax.swing.Icon;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DeployPropertiesFileEditorStep extends
		AbstractPropertiesFileEditorStep {

	protected String serviceName;

	/**
	 * 
	 */
	public DeployPropertiesFileEditorStep() {
	}

	/**
	 * @param name
	 * @param summary
	 * @param propertyNameColumnName
	 * @param propertyValueColumnValue
	 */
	public DeployPropertiesFileEditorStep(String serviceName, String name,
			String summary, String propertyNameColumnName,
			String propertyValueColumnValue) {
		super(name, summary, propertyNameColumnName, propertyValueColumnValue);
		this.serviceName = serviceName;
	}

	/**
	 * @param name
	 * @param summary
	 * @param propertyNameColumnName
	 * @param propertyValueColumnValue
	 * @param icon
	 */
	public DeployPropertiesFileEditorStep(String serviceName, String name,
			String summary, String propertyNameColumnName,
			String propertyValueColumnValue, Icon icon) {
		super(name, summary, propertyNameColumnName, propertyValueColumnValue,
				icon);
		this.serviceName = serviceName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.steps.AbstractPropertiesFileEditorStep#getPropertyFilePath()
	 */
	@Override
	protected String getPropertyFilePath() {
		return this.model.getServiceDestDir() + "/"
				+ this.serviceName + "/deploy.properties";
	}

}
