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
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.bcel.classfile.JavaClass;
import org.apache.log4j.Logger;

/** 
 *  SDK4QueryProcessor
 *  Processes CQL against a caCORE SDK 4.0 data source
 * 
 * @author David Ervin
 * 
 * @created Oct 3, 2007 10:34:55 AM
 * @version $Id: SDK4QueryProcessor.java,v 1.4 2007-12-05 21:27:47 dervin Exp $ 
 */
public class SDK4QueryProcessor extends CQLQueryProcessor {
    // configuration property keys
    public static final String PROPERTY_APPLICATION_NAME = "applicationName";
    public static final String PROPERTY_BEANS_JAR_NAME = "beansJarName";
    public static final String PROPERTY_USE_LOCAL_API = "useLocalApiFlag";
    public static final String PROPERTY_ORM_JAR_NAME = "ormJarName"; // only for local
    public static final String PROPERTY_HOST_NAME = "applicationHostName"; // only for remote
    public static final String PROPERTY_HOST_PORT = "applicationHostPort"; // only for remote
    public static final String PROPERTY_CASE_INSENSITIVE_QUERYING = "queryCaseInsensitive";
    
    // default values for properties
    public static final String DEFAULT_USE_LOCAL_API = String.valueOf(false);
    public static final String DEFAULT_CASE_INSENSITIVE_QUERYING = String.valueOf(false);
    
    // Log4J logger
    private static final Logger LOG = Logger.getLogger(SDK4QueryProcessor.class);
    
    private Map<String, Boolean> classHasSubclasses;
    private InheritanceManager inheritanceManager;
        
    public SDK4QueryProcessor() {
        super();
    }
    
    
    /**
     * Overriden to add initialization of the inheritance manager
     */
    public void initialize(Properties parameters, InputStream wsdd) throws InitializationException {
        super.initialize(parameters, wsdd);
        initializeInheritanceManager();
    }


    public CQLQueryResults processQuery(CQLQuery cqlQuery) throws MalformedQueryException, QueryProcessingException {
        List rawResults = queryCoreService(cqlQuery);
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
                    resultsAsArrays = rawResults;
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
        props.setProperty(PROPERTY_BEANS_JAR_NAME, "");
        props.setProperty(PROPERTY_CASE_INSENSITIVE_QUERYING, DEFAULT_CASE_INSENSITIVE_QUERYING);
        props.setProperty(PROPERTY_HOST_NAME, "");
        props.setProperty(PROPERTY_HOST_PORT, "");
        props.setProperty(PROPERTY_ORM_JAR_NAME, "");
        props.setProperty(PROPERTY_USE_LOCAL_API, DEFAULT_USE_LOCAL_API);
        return props;
    }
    
    
    public Set<String> getPropertiesFromEtc() {
        return super.getPropertiesFromEtc();
    }
    
    
    public String getConfigurationUiClassname() {
        // TODO: return the UI classname once the class is written
        return null;
    }
    
    
    private ApplicationService getApplicationService() throws QueryProcessingException {
        ApplicationService service = null;
        
        String useLocalValue = getConfiguredParameters().getProperty(PROPERTY_USE_LOCAL_API);
        boolean useLocal = Boolean.parseBoolean(useLocalValue);
        try {
            if (useLocal) {
                service = ApplicationServiceProvider.getApplicationService();
            } else {
                String url = getRemoteApplicationUrl();
                service = ApplicationServiceProvider.getApplicationServiceFromUrl(url);
            }
        } catch (Exception ex) {
            throw new QueryProcessingException("Error obtaining application service instance: " + ex.getMessage(), ex);
        }
        
        return service;
    }
    
    
    private String getRemoteApplicationUrl() {
        String hostname = getConfiguredParameters().getProperty(PROPERTY_HOST_NAME);
        if (!hostname.startsWith("http://") || !hostname.startsWith("https://")) {
            hostname = "http://" + hostname;
        }
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
    
    
    private Mappings getClassToQnameMappings() throws Exception {
        // get the mapping file name
        String filename = ServiceConfigUtil.getClassToQnameMappingsFile();
        Mappings mappings = (Mappings) Utils.deserializeDocument(filename, Mappings.class);
        return mappings;
    }
    
    
    protected List queryCoreService(CQLQuery query) 
        throws MalformedQueryException, QueryProcessingException {
        // get the caCORE application service
        ApplicationService service = getApplicationService();

        // see if the target has subclasses
        boolean subclassesDetected = classHasSubclasses(query.getTarget().getName());

        // see if queries should be made case insensitive
        boolean caseInsensitive = useCaseInsensitiveQueries();

        // generate the HQL to perform the query
        // new CQL2HQL process handles query modifiers at HQL level
        String hql = CQL2HQL.convertToHql(query, subclassesDetected, caseInsensitive);
        System.out.println("Executing HQL: " + hql);
        LOG.debug("Executing HQL:" + hql);

        // process the query
        HQLCriteria hqlCriteria = new HQLCriteria(hql);
        List targetObjects = null;
        try {
            targetObjects = service.query(hqlCriteria);
        } catch (Exception ex) {
            throw new QueryProcessingException("Error querying caCORE Application Service: " + ex.getMessage(), ex);
        }
        return targetObjects;
    }
    
    
    private void initializeInheritanceManager() throws InitializationException {
        // no inheritance manager yet
        String beansJarName = getConfiguredParameters().getProperty(PROPERTY_BEANS_JAR_NAME);
        // examine the classpath for the jar
        String classPath = System.getProperty("java.class.path");
        String beansJarFilename = null;
        StringTokenizer tokenizer = new StringTokenizer(classPath, File.pathSeparator);
        while (beansJarFilename == null && tokenizer.hasMoreElements()) {
            String pathElement = tokenizer.nextToken();
            if (pathElement.endsWith(beansJarName)) {
                beansJarFilename = pathElement;
            }
        }
        if (beansJarFilename == null) {
            throw new InitializationException("Unable to locate beans jar (" 
                + beansJarName + ") in the classpath!");
        }
        File beansJar = new File(beansJarFilename);
        LOG.debug("Beans jar found: " + beansJar.getAbsolutePath());
        try {
            inheritanceManager = InheritanceManager.getManager(beansJar, false);
        } catch (Exception ex) {
            throw new InitializationException("Error instantiating class inheritance manager: " 
                + ex.getMessage(), ex);
        }
        LOG.debug("Inheritance manager initialized");
    }
    
    
    private synchronized boolean classHasSubclasses(String className) throws QueryProcessingException {
        // ensure the map exists
        if (classHasSubclasses == null) {
            classHasSubclasses = new HashMap<String, Boolean>();
        }
        Boolean hasSubclasses = classHasSubclasses.get(className);
        if (hasSubclasses == null) {
            // never been checked
            try {
                List<JavaClass> subs = inheritanceManager.getSubclasses(className);
                hasSubclasses = Boolean.valueOf(subs == null || subs.size() == 0);
                classHasSubclasses.put(className, hasSubclasses);
            } catch (ClassNotFoundException ex) {
                throw new QueryProcessingException("No class " + className + " found in the beans jar file: " 
                    + ex.getMessage(), ex);
            }
        }
        return hasSubclasses.booleanValue();
    }
}
