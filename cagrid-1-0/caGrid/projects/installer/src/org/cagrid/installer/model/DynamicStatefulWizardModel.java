/**
 * 
 */
package org.cagrid.installer.model;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.cagrid.installer.steps.Constants;
import org.pietschy.wizard.models.DynamicModel;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DynamicStatefulWizardModel extends DynamicModel implements CaGridInstallerModel {

	private Map state;
	private ResourceBundle messages;
	
	/**
	 * 
	 */
	public DynamicStatefulWizardModel() {
		this(null, null);
	}
	
	public DynamicStatefulWizardModel(Map state){
		this(state, null);
	}
	
	public DynamicStatefulWizardModel(Map state, ResourceBundle messages){
		this.state = state;
		if(this.state == null){
			this.state = new HashMap();
		}
		this.messages = messages;
		if(this.messages == null){
			// Load messages
			try {
				this.messages = ResourceBundle.getBundle(Constants.MESSAGES,
						Locale.US);
			} catch (Exception ex) {
				throw new RuntimeException("Error loading messages: "
						+ ex.getMessage());
			}
		}
	}

	public Map<String, String> getState() {
		return state;
	}

	public void setState(Map state) {
		if(state == null){
			throw new IllegalArgumentException("State must not be set to null");
		}
		this.state = state;
	}

	public String getMessage(String key) {
		String message = null;
		if(this.messages != null){
			message = this.messages.getString(key);
		}
		return message;
	}

}
