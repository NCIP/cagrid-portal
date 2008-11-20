package org.cagrid.data.sdkquery41.processor;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.mapping.Mappings;
import gov.nih.nci.cagrid.data.service.ServiceConfigUtil;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.security.SecurityManager;

/**
 * SDK41QueryProcessor
 * Leverages the caCORE SDK v4.1's native CQL processing functionality
 * 
 * @author David
 */
public class SDK41QueryProcessor extends CQLQueryProcessor {
    
    public static final Log logger = LogFactory.getLog(SDK41QueryProcessor.class);
    
    // configuration property keys
    public static final String PROPERTY_APPLICATION_NAME = "applicationName";
    public static final String PROPERTY_USE_LOCAL_API = "useLocalApiFlag";
    public static final String PROPERTY_ORM_JAR_NAME = "ormJarName"; // only for local
    public static final String PROPERTY_HOST_NAME = "applicationHostName"; // only for remote
    public static final String PROPERTY_HOST_PORT = "applicationHostPort"; // only for remote
    public static final String PROPERTY_CASE_INSENSITIVE_QUERYING = "queryCaseInsensitive";
    public static final String PROPERTY_DOMAIN_TYPES_INFO_FILENAME = "domainTypesInfoFilename";
    public static final String PROPERTY_USE_LOGIN = "useServiceLogin";
    public static final String PROPERTY_USE_GRID_IDENTITY_LOGIN = "useGridIdentityLogin";
    public static final String PROPERTY_STATIC_LOGIN_USERNAME = "staticLoginUsername";
    public static final String PROPERTY_STATIC_LOGIN_PASSWORD = "staticLoginPassword";
    public static final String PROPERTY_STRICT_CQL_PROCESSING = "strictCQLProcessing";
    
    // default values for properties
    public static final String DEFAULT_USE_LOCAL_API = String.valueOf(false);
    public static final String DEFAULT_CASE_INSENSITIVE_QUERYING = String.valueOf(false);
    public static final String DEFAULT_USE_LOGIN = String.valueOf(false);
    public static final String DEFAULT_USE_GRID_IDENTITY_LOGIN = String.valueOf(false);
    public static final String DEFAULT_STRICT_CQL_PROCESSING = String.valueOf(true);

    public SDK41QueryProcessor() {
        super();
    }


    public CQLQueryResults processQuery(CQLQuery cqlQuery) throws MalformedQueryException, QueryProcessingException {
        ApplicationService applicationService = getApplicationService();
        List results = null;
        try {
            results = applicationService.query(cqlQuery);
        } catch (ApplicationException ex) {
            String message = "Error processing CQL query in the caCORE ApplicationService: " + ex.getMessage();
            logger.error(message, ex);
            throw new QueryProcessingException(message, ex);
        }
        
        return null;
    }
    
    
    public Properties getRequiredParameters() {
        Properties props = new Properties();
        props.setProperty(PROPERTY_APPLICATION_NAME, "");
        props.setProperty(PROPERTY_CASE_INSENSITIVE_QUERYING, DEFAULT_CASE_INSENSITIVE_QUERYING);
        props.setProperty(PROPERTY_DOMAIN_TYPES_INFO_FILENAME , "");
        props.setProperty(PROPERTY_HOST_NAME, "");
        props.setProperty(PROPERTY_HOST_PORT, "");
        props.setProperty(PROPERTY_ORM_JAR_NAME, "");
        props.setProperty(PROPERTY_USE_LOCAL_API, DEFAULT_USE_LOCAL_API);
        props.setProperty(PROPERTY_USE_LOGIN, DEFAULT_USE_LOGIN);
        props.setProperty(PROPERTY_USE_GRID_IDENTITY_LOGIN, DEFAULT_USE_GRID_IDENTITY_LOGIN);
        props.setProperty(PROPERTY_STATIC_LOGIN_USERNAME, "");
        props.setProperty(PROPERTY_STATIC_LOGIN_PASSWORD, "");
        props.setProperty(PROPERTY_STRICT_CQL_PROCESSING, DEFAULT_STRICT_CQL_PROCESSING);
        return props;
    }
    
    
    public Set<String> getPropertiesFromEtc() {
        Set<String> required = super.getPropertiesFromEtc();
        required.add(PROPERTY_DOMAIN_TYPES_INFO_FILENAME);
        return required;
    }
    
    
    public String getConfigurationUiClassname() {
        // TODO: return the UI classname once the class is written
        return null;
    }
    
    
    private ApplicationService getApplicationService() throws QueryProcessingException {
        ApplicationService service = null;
        
        boolean useLocal = useLocalApplicationService();
        boolean useLogin = useServiceLogin();
        boolean useStaticLogin = useStaticLogin();
        try {
            String username = null;
            String passwd = null;
            if (useLogin) {
                if (useStaticLogin) {
                    username = getConfiguredParameters().getProperty(PROPERTY_STATIC_LOGIN_USERNAME);
                    passwd = username = getConfiguredParameters().getProperty(PROPERTY_STATIC_LOGIN_PASSWORD);
                } else {
                    SecurityManager securityManager = SecurityManager.getManager();
                    username = securityManager.getCaller();
                    // TODO: password?
                }
            }
            
            if (useLocal) {
                if (useLogin) {
                    service = ApplicationServiceProvider.getApplicationService(username, passwd);
                } else {
                    service = ApplicationServiceProvider.getApplicationService();   
                }
            } else {
                String url = getRemoteApplicationUrl();
                if (useLogin) {
                    service = ApplicationServiceProvider.getApplicationServiceFromUrl(url, username, passwd);
                } else {
                    service = ApplicationServiceProvider.getApplicationServiceFromUrl(url);   
                }
            }
        } catch (Exception ex) {
            throw new QueryProcessingException("Error obtaining application service instance: " + ex.getMessage(), ex);
        }
        
        return service;
    }
    
    
    private String getRemoteApplicationUrl() {
        String hostname = getConfiguredParameters().getProperty(PROPERTY_HOST_NAME);
        /*
        if (!hostname.startsWith("http://") || !hostname.startsWith("https://")) {
            hostname = "http://" + hostname;
        }
        */
        String port = getConfiguredParameters().getProperty(PROPERTY_HOST_PORT);
        while (hostname.endsWith("/")) {
            hostname = hostname.substring(0, hostname.length() - 1);
        }
        String urlPart = hostname + ":" + port;
        urlPart += "/";
        urlPart += getConfiguredParameters().getProperty(PROPERTY_APPLICATION_NAME);
        return urlPart;
    }
    
    
    private boolean useCaseInsensitiveQueries() throws QueryProcessingException {
        String caseInsensitiveValue = getConfiguredParameters().getProperty(PROPERTY_CASE_INSENSITIVE_QUERYING);
        try {
            return Boolean.parseBoolean(caseInsensitiveValue);
        } catch (Exception ex) {
            throw new QueryProcessingException("Error determining case insensitivity: " + ex.getMessage(), ex);
        }
    }
    
    
    private boolean useLocalApplicationService() throws QueryProcessingException {
        String useLocalValue = getConfiguredParameters().getProperty(PROPERTY_USE_LOCAL_API);
        try {
            return Boolean.parseBoolean(useLocalValue);
        } catch (Exception ex) {
            throw new QueryProcessingException("Error determining local application service use: " + ex.getMessage(), ex);
        }
    }
    
    
    private boolean useServiceLogin() throws QueryProcessingException {
        String useLoginValue = getConfiguredParameters().getProperty(PROPERTY_USE_LOGIN);
        try {
            return Boolean.parseBoolean(useLoginValue);
        } catch (Exception ex) {
            throw new QueryProcessingException("Error determining login use flag: " + ex.getMessage(), ex);
        }
    }
    
    
    private boolean useStaticLogin() throws QueryProcessingException {
        String useGridIdentLogin = getConfiguredParameters().getProperty(PROPERTY_USE_GRID_IDENTITY_LOGIN);
        try {
            return !Boolean.parseBoolean(useGridIdentLogin);
        } catch (Exception ex) {
            throw new QueryProcessingException("Error determining use of static login: " + ex.getMessage(), ex);
        }
    }
    
    
    private boolean useStrictCQLProcessing() throws QueryProcessingException {
        String useStrictValue = getConfiguredParameters().getProperty(PROPERTY_STRICT_CQL_PROCESSING);
        try {
            return Boolean.parseBoolean(useStrictValue);
        } catch (Exception ex) {
            throw new QueryProcessingException("Error determining use of strict CQL processing: " + ex.getMessage(), ex);
        }
    }
    
    
    private Mappings getClassToQnameMappings() throws Exception {
        // get the mapping file name
        String filename = ServiceConfigUtil.getClassToQnameMappingsFile();
        // String filename = "mapping.xml";
        Mappings mappings = (Mappings) Utils.deserializeDocument(filename, Mappings.class);
        return mappings;
    }
    
    
    private DomainModel getDomainModel() throws Exception {
        DomainModel domainModel = null;
        Resource serviceBaseResource = ResourceContext.getResourceContext().getResource();
        Method[] resourceMethods = serviceBaseResource.getClass().getMethods();
        for (int i = 0; i < resourceMethods.length; i++) {
            if (resourceMethods[i].getReturnType() != null 
                && resourceMethods[i].getReturnType().equals(DomainModel.class)) {
                domainModel = (DomainModel) resourceMethods[i].invoke(serviceBaseResource, new Object[] {});
                break;
            }
        }
        return domainModel;
    }
}
