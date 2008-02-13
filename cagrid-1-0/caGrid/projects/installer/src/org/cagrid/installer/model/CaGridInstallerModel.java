/**
 * 
 */
package org.cagrid.installer.model;

import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.WizardStep;
import org.pietschy.wizard.models.Condition;

import java.util.Map;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface CaGridInstallerModel extends WizardModel {

//	Map getState();
	
	void add(WizardStep step);
	
	void add(WizardStep step, Condition condition);
	
	Map<String,String> getStateMap();
	
	void unsetProperty(String propName);
	
	void setProperty(String propName, String propValue);
	
	String getProperty(String propName);
	
	String getProperty(String propName, String defaultValue);	
	
	String getMessage(String key);

	boolean isTomcatConfigurationRequired();

	boolean isSecurityConfigurationRequired();

	boolean isTrue(String propName);

	boolean isTomcatContainer();
	
	boolean isGlobusContainer();

	boolean isSet(String propName);

	boolean isCAGenerationRequired();

	boolean isServiceCertGenerationRequired();

	boolean isEqual(String value, String propName2);

	boolean isConfigureGlobusRequired();

	boolean isDeployGlobusRequired();

	void setDeactivatePrevious(boolean b);
	
	String getServiceDestDir();

	boolean isSecureContainerRequired();

	boolean isConfigureContainerSelected();

	boolean isSyncGTSInstalled();
	
	boolean isAntInstalled();

	boolean isTomcatInstalled();

	boolean isGlobusInstalled();

	boolean isCaGridInstalled();

	boolean isActiveBPELInstalled();
	
	boolean isGlobusConfigured();
	
	boolean isGlobusDeployed();

	boolean isBrowserInstalled();

    boolean isPortalInstalled();

    boolean isAuthnSvcServiceCredentialsPresent();
}
