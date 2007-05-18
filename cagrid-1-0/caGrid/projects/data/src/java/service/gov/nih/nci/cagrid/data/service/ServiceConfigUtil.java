package gov.nih.nci.cagrid.data.service;

import gov.nih.nci.cagrid.data.DataServiceConstants;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

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

	public static Properties getQueryProcessorConfigurationParameters() throws Exception {
		String getterPrefix = "get" 
			+ Character.toUpperCase(DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX.charAt(0)) 
			+ DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX.substring(1);
		
		Properties configParams = new Properties();
		try {
			Object serviceConfig = getServiceConfigObject();
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
					// key = String.valueOf(Character.toLowerCase(key.charAt(0))) + key.substring(1); 
					configParams.setProperty(key, value);
				}
			}
		} catch (Exception e) {
			throw new Exception("Unable to convert service config to map: " + e.getMessage(), e);
		}
		return configParams;
	}
	
	
	public static String getCqlQueryProcessorClassName() throws Exception {
		String getterName = "get" + Character.toUpperCase(DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY.charAt(0)) 
			+ DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY.substring(1);
		
		try {
			Object serviceConfig = getServiceConfigObject();
			Class configClass = serviceConfig.getClass();
			Method[] configMethods = configClass.getMethods();
			for (int i = 0; i < configMethods.length; i++) {
				Method current = configMethods[i];
				if (current.getName().equals(getterName) 
					&& current.getReturnType().equals(String.class)
					&& Modifier.isPublic(current.getModifiers())) {
					String value = (String) current.invoke(serviceConfig, new Object[] {});
					return value;
				}
			}
		} catch (Exception e) {
			throw new Exception("Unable to extract query processor class name from config: " + e.getMessage(), e);
		}
		return null;
	}
	
	
	public static Properties getDataServiceParams() throws Exception {
		String getterName = "get" + Character.toUpperCase(DataServiceConstants.DATA_SERVICE_PARAMS_PREFIX.charAt(0)) 
			+ DataServiceConstants.DATA_SERVICE_PARAMS_PREFIX.substring(1);
		Properties props = new Properties();
		try {
			Object serviceConfig = getServiceConfigObject();
			Class configClass = serviceConfig.getClass();
			Method[] configMethods = configClass.getMethods();
			for (int i = 0; i < configMethods.length; i++) {
				Method current = configMethods[i];
				if (current.getName().startsWith(getterName)
					&& current.getReturnType().equals(String.class)
					&& Modifier.isPublic(current.getModifiers())) {
					String name = current.getName().substring(3);
					name = String.valueOf(Character.toLowerCase(name.charAt(0))) + name.substring(1);
					String value = (String) current.invoke(serviceConfig, new Object[] {});
					props.setProperty(name, value);
				}
			}
		} catch (Exception ex) {
			throw new Exception("Unable to extract data service config parameters: " + ex.getMessage(), ex);
		}
		return props;
	}
	
	
	public static String getClassToQnameMappingsFile() throws Exception {
		String getterName = "get" + Character.toUpperCase(DataServiceConstants.CLASS_MAPPINGS_FILENAME.charAt(0))
			+ DataServiceConstants.CLASS_MAPPINGS_FILENAME.substring(1);
		try {
			Object serviceConfig = getServiceConfigObject();
			Class configClass = serviceConfig.getClass();
			Method[] configMethods = configClass.getMethods();
			for (int i = 0; i < configMethods.length; i++) {
				Method current = configMethods[i];
				if (current.getName().startsWith(getterName)
					&& current.getReturnType().equals(String.class)
					&& Modifier.isPublic(current.getModifiers())) {
					String name = current.getName().substring(3);
					name = String.valueOf(Character.toLowerCase(name.charAt(0))) + name.substring(1);
					String value = (String) current.invoke(serviceConfig, new Object[] {});
					return value;
				}
			}
			throw new NoSuchMethodException("Method " + getterName + " not found on " + configClass.getName());
		} catch (Exception ex) {
			throw new Exception("Unable to get class mappings filename: " + ex.getMessage(), ex);
		}
	}
	
	
	public static String getConfigProperty(String propertyName) throws Exception {
		String getterName = "get" + Character.toUpperCase(propertyName.charAt(0)) 
			+ propertyName.substring(1);
		try {
			Object serviceConfig = getServiceConfigObject();
			Class configClass = serviceConfig.getClass();
			Method getter = configClass.getMethod(getterName, new Class[] {});
			return (String) getter.invoke(serviceConfig, new Object[] {});
		} catch (Exception ex) {
			throw new Exception("Unable to resolve property " + propertyName + ": " + ex.getMessage(), ex);
		}
	}
	
	
	private static Object getServiceConfigObject() throws NamingException {
		MessageContext context = MessageContext.getCurrentContext();
		String servicePath = context.getTargetService();
		String jndiName = Constants.JNDI_SERVICES_BASE_NAME + servicePath + "/serviceconfiguration";
		javax.naming.Context initialContext = new InitialContext();
		Object serviceConfig = initialContext.lookup(jndiName);
		return serviceConfig;
	}
}
