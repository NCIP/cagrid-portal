package gov.nih.nci.cagrid.data.service;

import gov.nih.nci.cagrid.data.DataServiceConstants;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Properties;

import javax.naming.InitialContext;

import org.apache.axis.MessageContext;
import org.globus.wsrf.Constants;

/** 
 *  ServiceConfigUtil
 *  Hackware utility to load a service configuration from JNDI and
 *  walk through it with reflection to build up a HashMap
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jun 16, 2006 
 * @version $Id$ 
 */
public class ServiceConfigUtil {

	public static Properties getConfigurationParameters() throws Exception {
		String getterPrefix = "get" 
			+ Character.toUpperCase(DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX.charAt(0)) 
			+ DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX.substring(1);
		
		Properties configParams = new Properties();
		
		MessageContext context = MessageContext.getCurrentContext();
		String servicePath = context.getTargetService();
		String jndiName = Constants.JNDI_SERVICES_BASE_NAME + servicePath + "/serviceconfiguration";
		try {
			javax.naming.Context initialContext = new InitialContext();
			Object serviceConfig = initialContext.lookup(jndiName);
			Class configClass = serviceConfig.getClass();
			Method[] configMethods = configClass.getMethods();
			for (int i = 0; i < configMethods.length; i++) {
				Method current = configMethods[i];
				if (current.getName().startsWith(getterPrefix) 
					&& current.getReturnType().equals(String.class)
					&& Modifier.isPublic(current.getModifiers())) {
					String value = (String) current.invoke(serviceConfig, new Object[] {});
					String key = current.getName().substring(getterPrefix.length());
					// lowercase first character
					key = String.valueOf(Character.toLowerCase(key.charAt(0))) + key.substring(1); 
					configParams.setProperty(key, value);
				}
			}
		} catch (Exception e) {
			throw new Exception("Unable to convert service config to map: " + e.getMessage(), e);
		}
		return configParams;
	}
}
