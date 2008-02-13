/**
 * 
 */
package org.cagrid.installer.browser;

import org.cagrid.installer.portal.ConfigurePortalGridPropertiesStep;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.options.TextPropertyConfigurationOption;

import javax.swing.*;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ConfigureBrowserPropertiesStep extends
        ConfigurePortalGridPropertiesStep {

	/**
	 * 
	 */
	public ConfigureBrowserPropertiesStep() {

	}

	/**
	 * @param name
	 * @param description
	 */
	public ConfigureBrowserPropertiesStep(String name, String description) {
		super(name, description);

	}

	/**
	 * @param name
	 * @param description
	 * @param icon
	 */
	public ConfigureBrowserPropertiesStep(String name, String description,
			Icon icon) {
		super(name, description, icon);

	}

	protected String getIndexServiceURLsProperty() {
		return Constants.BROWSER_INDEX_SERVICE_URLS;
	}

	protected void addOptions() {
		getOptions()
		.add(
				new TextPropertyConfigurationOption(
						Constants.BROWSER_CONTEXT_NAME,
						this.model.getMessage("browser.context.name"),
						this.model
								.getProperty(
										Constants.BROWSER_CONTEXT_NAME,
										"cagrid-browser"),
						true));
		getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.BROWSER_EVS_SVC_URL,
								this.model.getMessage("browser.evs.svc.url"),
								this.model
										.getProperty(
												Constants.BROWSER_EVS_SVC_URL,
												"http://cagrid-service.nci.nih.gov:8080/wsrf/services/cagrid/EVSGridService"),
								true));

		getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.BROWSER_IDP_URL1,
								this.model.getMessage("browser.idp.url1"),
								this.model
										.getProperty(
												Constants.BROWSER_IDP_URL1,
												"https://cagrid-auth.nci.nih.gov:8443/wsrf/services/cagrid/AuthenticationService"),
								true));

		getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.BROWSER_IDP_URL2,
								this.model.getMessage("browser.idp.url2"),
								this.model
										.getProperty(
												Constants.BROWSER_IDP_URL2,
												"https://cagrid-auth.nci.nih.gov:8443/wsrf/services/cagrid/Dorian"),
								true));

		getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.BROWSER_IFS_URL,
								this.model.getMessage("browser.ifs.url"),
								this.model
										.getProperty(Constants.BROWSER_IFS_URL,
												"https://cagrid-auth.nci.nih.gov:8443/wsrf/services/cagrid/Dorian"),
								true));

	}

}
