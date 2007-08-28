/**
 * 
 */
package org.cagrid.installer.myservice;

import javax.swing.Icon;

import org.cagrid.installer.steps.AbstractPropertiesFileEditorStep;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class IntroduceServicePropertiesFileEditorStep extends
		AbstractPropertiesFileEditorStep {

	private String serviceDirProp;
	private String propFileName;
	/**
	 * 
	 */
	public IntroduceServicePropertiesFileEditorStep() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param summary
	 * @param propertyNameColumnName
	 * @param propertyValueColumnValue
	 */
	public IntroduceServicePropertiesFileEditorStep(String serviceDirProp, String propFileName, String name,
			String summary, String propertyNameColumnName,
			String propertyValueColumnValue) {
		this(serviceDirProp, propFileName, name, summary, propertyNameColumnName, propertyValueColumnValue, null);
	}

	/**
	 * @param name
	 * @param summary
	 * @param propertyNameColumnName
	 * @param propertyValueColumnValue
	 * @param icon
	 */
	public IntroduceServicePropertiesFileEditorStep(String serviceDirProp, String propFileName, String name,
			String summary, String propertyNameColumnName,
			String propertyValueColumnValue, Icon icon) {
		super(name, summary, propertyNameColumnName, propertyValueColumnValue,
				icon);
		this.serviceDirProp = serviceDirProp;
		this.propFileName = propFileName;
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.steps.AbstractPropertiesFileEditorStep#getPropertyFilePath()
	 */
	@Override
	protected String getPropertyFilePath() {
		return this.model.getProperty(this.serviceDirProp) + "/" + this.propFileName;
	}

}
