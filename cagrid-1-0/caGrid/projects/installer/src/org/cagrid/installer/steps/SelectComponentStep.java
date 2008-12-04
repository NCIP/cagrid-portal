/**
 * 
 */
package org.cagrid.installer.steps;

import javax.swing.Icon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pietschy.wizard.InvalidStateException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectComponentStep extends PropertyConfigurationStep {

	private static final Log logger = LogFactory.getLog(SelectComponentStep.class);
	
	/**
	 * 
	 */
	public SelectComponentStep() {

	}

	/**
	 * @param name
	 * @param description
	 */
	public SelectComponentStep(String name, String description) {
		super(name, description);

	}

	/**
	 * @param name
	 * @param description
	 * @param icon
	 */
	public SelectComponentStep(String name, String description, Icon icon) {
		super(name, description, icon);
	}
	
	protected void checkComplete(){
		boolean oneChecked = false;
		for(String key : this.requiredFields.keySet()){
			if(this.requiredFields.get(key)){
				oneChecked = true;
				break;
			}
		}
		setComplete(oneChecked);
	}
	
	public void applyState() throws InvalidStateException {
		super.applyState();
		if(this.model.isSecureContainerRequired()){
			this.model.setProperty(Constants.USE_SECURE_CONTAINER, Constants.TRUE);
		}
	}

}
