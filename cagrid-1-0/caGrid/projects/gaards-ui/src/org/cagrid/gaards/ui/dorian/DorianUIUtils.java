package org.cagrid.gaards.ui.dorian;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.configuration.GeneralConfiguration;
import org.cagrid.grape.configuration.Properties;
import org.cagrid.grape.configuration.Property;
import org.cagrid.grape.configuration.Values;


public class DorianUIUtils {

	private static Logger log = Logger.getLogger(DorianUIUtils.class);;


	public static List getAuthenticationServices() {
		List services = new ArrayList();
		services = getValues(DorianUIConstants.AUTH_SERVICES_CONF);
		services.addAll(getDorianServices());
		return services;
	}


	public static List getDorianServices() {
		return getValues(DorianUIConstants.DORIAN_SERVICES_CONF);
	}


	public static List getValues(String property) {
		List values = new ArrayList();
		try {
			GeneralConfiguration conf = (GeneralConfiguration) GridApplication.getContext().getConfigurationManager()
				.getConfigurationObject(DorianUIConstants.UI_CONF);

			Properties props = conf.getProperties();
			if (props != null) {
				Property[] prop = props.getProperty();
				if (prop != null) {
					for (int i = 0; i < prop.length; i++) {
						if (prop[i].getName().equals(property)) {
							Values vals = prop[i].getValues();
							if (vals != null) {
								String[] val = vals.getValue();
								if (val != null) {
									for (int j = 0; j < val.length; j++) {
										values.add(val[j]);
									}
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			log.error("Error loading the property " + property + " from the configuration.");
			log.error(e);
		}
		return values;
	}
}
