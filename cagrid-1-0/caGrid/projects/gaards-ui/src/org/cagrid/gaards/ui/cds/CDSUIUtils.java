package org.cagrid.gaards.ui.cds;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.cagrid.gaards.cds.common.DelegationPolicy;
import org.cagrid.gaards.cds.common.GroupDelegationPolicy;
import org.cagrid.gaards.cds.common.IdentityDelegationPolicy;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.configuration.GeneralConfiguration;
import org.cagrid.grape.configuration.Properties;
import org.cagrid.grape.configuration.Property;
import org.cagrid.grape.configuration.Values;

public class CDSUIUtils {

	private static Logger log = Logger.getLogger(CDSUIUtils.class);;

	public static List<String> getCDSServices() {
		return getValues(CDSUIConstants.CDS_SERVICES_CONF);
	}

	public static List<String> getValues(String property) {
		List<String> values = new ArrayList<String>();
		try {
			GeneralConfiguration conf = (GeneralConfiguration) GridApplication
					.getContext().getConfigurationManager()
					.getConfigurationObject(CDSUIConstants.UI_CONF);

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
			log.error("Error loading the property " + property
					+ " from the configuration.");
			log.error(e);
		}
		return values;
	}

	public static DelegationPolicyPanel getPolicyPanel(String policyType,
			boolean editable) {
		if (policyType.equals(CDSUIConstants.IDENTITY_POLICY_TYPE)) {
			return new IdentityDelegationPolicyPanel(editable);
		} else if (policyType.equals(CDSUIConstants.GROUP_POLICY_TYPE)) {
			return new GroupDelegationPolicyPanel(editable);
		} else {
			return null;
		}
	}

	public static String getDelegationPolicyType(DelegationPolicy policy) {
		if (policy == null) {
			return "None";
		} else if (policy instanceof IdentityDelegationPolicy) {
			return CDSUIConstants.IDENTITY_POLICY_TYPE;
		} else if (policy instanceof GroupDelegationPolicy) {
			return CDSUIConstants.GROUP_POLICY_TYPE;
		} else {
			return policy.getClass().getName();
		}
	}
}