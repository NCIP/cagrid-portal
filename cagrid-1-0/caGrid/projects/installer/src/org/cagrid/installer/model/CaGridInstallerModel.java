/**
 * 
 */
package org.cagrid.installer.model;

import java.util.Map;

import org.pietschy.wizard.WizardModel;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface CaGridInstallerModel extends WizardModel {

	Map getState();
	void setState(Map state);
	
	String getMessage(String key);
	
}
