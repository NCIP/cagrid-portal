package gov.nih.nci.cagrid.sdkquery4.processor;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.InitializationException;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.mapping.Mappings;
import gov.nih.nci.cagrid.data.service.ServiceConfigUtil;
import gov.nih.nci.cagrid.data.utilities.CQLResultsCreationUtil;
import gov.nih.nci.cagrid.data.utilities.ResultsCreationException;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.sdkquery4.beans.domaininfo.DomainTypesInformation;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.client.util.xml.caCOREMarshaller;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.io.FileReader;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.security.SecurityManager;

/** 
 *  SDK4QueryProcessor
 *  Processes CQL against a caCORE SDK 4.0 data source
 * 
 * @author David Ervin
 * 
 * @created Oct 3, 2007 10:34:55 AM
 * @version $Id: SDK4QueryProcessor.java,v 1.13 2008-01-18 18:19:32 dervin Exp $ 
 */
public class SDK4QueryProcessor extends CQLQueryProcessor {
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
    
    // default values for properties
    public static final String DEFAULT_USE_LOCAL_API = String.valueOf(false);
    public static final String DEFAULT_CASE_INSENSITIVE_QUERYING = String.valueOf(false);
    public static final String DEFAULT_USE_LOGIN = String.valueOf(false);
    public static final String DEFAULT_USE_GRID_IDENTITY_LOGIN = String.valueOf(false);
    
    // Log4J logger
    private static final Logger LOG = Logger.getLogger(SDK4QueryProcessor.class);
    
    private CQL2ParameterizedHQL cqlTranslator;
        
    public SDK4QueryProcessor() {
        super();
    }
    
    
    /**
     * Overriden to add initialization of the inheritance manager
     */
    public void initialize(Properties parameters, InputStream wsdd) throws InitializationException {
        super.initialize(parameters, wsdd);
        initializeCqlToHqlTranslator();
    }


    public CQLQueryResults processQuery(CQLQuery cqlQuery) throws MalformedQueryException, QueryProcessingException {
        List rawResults = queryCoreService(cqlQuery);
        if (LOG.isDebugEnabled()) {
            // print the SDK's output if debug is enabled
            for (Object o : rawResults) {
                LOG.debug(o.getClass().getName());
                caCOREMarshaller m = new caCOREMarshaller("unmarshaller-xml-mapping.xml", false);
                try {
                    LOG.debug(m.toXML(o));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        CQLQueryResults cqlResults = null;
        // determine which type of results to package up
        if (cqlQuery.getQueryModifier() != null) {
            QueryModifier mods = cqlQuery.getQueryModifier();
            if (mods.isCountOnly()) {
                long count = Long.parseLong(rawResults.get(0).toString());
                cqlResults = CQLResultsCreationUtil.createCountResults(count, cqlQuery.getTarget().getName());
            } else { // attributes
                String[] attributeNames = null;
                List<Object[]> resultsAsArrays = null;
                if (mods.getDistinctAttribute() != null) {
                    attributeNames = new String[] {mods.getDistinctAttribute()};
                    resultsAsArrays = new LinkedList<Object[]>();
                    for (Object o : rawResults) {
                        resultsAsArrays.add(new Object[] {o});
                    }
                } else { // multiple attributes
                    attributeNames = mods.getAttributeNames();
                    resultsAsArrays = new LinkedList<Object[]>();
                    for (Object o : rawResults) {
                        resultsAsArrays.add((Object[]) o);
                    }
                }
                cqlResults = CQLResultsCreationUtil.createAttributeResults(
                    resultsAsArrays, cqlQuery.getTarget().getName(), attributeNames);
            }
        } else {
            Mappings classToQname = null;
            try {
                classToQname = getClassToQnameMappings();
            } catch (Exception ex) {
                throw new QueryProcessingException("Error loading class to QName mappings: " + ex.getMessage(), ex);
            }
            try {
                cqlResults = CQLResultsCreationUtil.createObjectResults(
                    rawResults, cqlQuery.getTarget().getName(), classToQname);
            } catch (ResultsCreationException ex) {
                throw new QueryProcessingException("Error packaging query results: " + ex.getMessage(), ex);
            }
        }
        return cqlResults;
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
    
    
    protected List queryCoreService(CQLQuery query) 
        throws MalformedQueryException, QueryProcessingException {
        // get the caCORE application service
        ApplicationService service = getApplicationService();

        // generate the HQL to perform the query
        // new CQL2HQL process handles query modifiers at HQL level
        ParameterizedHqlQuery parameterizedHql = cqlTranslator.convertToHql(query);
        System.out.println("Executing HQL:\n" + parameterizedHql);
        LOG.debug("Executing HQL:\n" + parameterizedHql);

        // process the query
        HQLCriteria hqlCriteria = new HQLCriteria(parameterizedHql.getHql(), parameterizedHql.getParameters());
        List targetObjects = null;
        try {
            targetObjects = service.query(hqlCriteria);
        } catch (Exception ex) {
            throw new QueryProcessingException("Error querying caCORE Application Service: " + ex.getMessage(), ex);
        }
        return targetObjects;
    }
    
    
    private void initializeCqlToHqlTranslator() throws InitializationException {
        // get the domain types information document
        String domainTypesFilename = 
            getConfiguredParameters().getProperty(PROPERTY_DOMAIN_TYPES_INFO_FILENAME);
        DomainTypesInformation typesInfo = null;
        try {
            FileReader reader = new FileReader(domainTypesFilename);
            typesInfo = DomainTypesInformationUtil.deserializeDomainTypesInformation(reader);
            reader.close();
        } catch (Exception ex) {
            throw new InitializationException("Error deserializing domain types information from " 
                + domainTypesFilename + ": " + ex.getMessage(), ex);
        }
        // get the domain model
        DomainModel domainModel = null;
        try {
            domainModel = getDomainModel();
        } catch (Exception ex) {
            throw new InitializationException("Error obtaining domain model: " + ex.getMessage(), ex);
        }
        RoleNameResolver resolver = new RoleNameResolver(domainModel);
        // create the query translator instance
        try {
            cqlTranslator = new CQL2ParameterizedHQL(typesInfo, resolver, useCaseInsensitiveQueries());
        } catch (Exception ex) {
            throw new InitializationException("Error instantiating CQL to HQL translator: " 
                + ex.getMessage(), ex);
        }
        LOG.debug("CQL to HQL translator initialized");
    }
}
