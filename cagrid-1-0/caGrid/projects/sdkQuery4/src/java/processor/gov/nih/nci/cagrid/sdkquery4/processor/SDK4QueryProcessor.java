package gov.nih.nci.cagrid.sdkquery4.processor;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
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

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/** 
 *  SDK4QueryProcessor
 *  Processes CQL against a caCORE SDK 4.0 data source
 * 
 * @author David Ervin
 * 
 * @created Oct 3, 2007 10:34:55 AM
 * @version $Id: SDK4QueryProcessor.java,v 1.1 2007-10-03 19:01:14 dervin Exp $ 
 */
public class SDK4QueryProcessor extends CQLQueryProcessor {
    // configuration property keys
    public static final String USE_LOCAL_CLIENT = "useLocalApi";
    public static final String REMOTE_URL = "remoteServiceUrl";
    public static final String CASE_INSENSITIVE_QUERYING = "queryCaseInsensitive";
    
    // default values for properties
    public static final String DEFAULT_USE_LOCAL_CLIENT = String.valueOf(false);
    public static final String DEFAULT_CASE_INSENSITIVE_QUERYING = String.valueOf(false);
    
    private static final Logger LOG = Logger.getLogger(SDK4QueryProcessor.class);
        
    public SDK4QueryProcessor() {
        // can't do any initialization in constructor!
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
        props.setProperty(USE_LOCAL_CLIENT, DEFAULT_USE_LOCAL_CLIENT);
        props.setProperty(CASE_INSENSITIVE_QUERYING, DEFAULT_CASE_INSENSITIVE_QUERYING);
        props.setProperty(REMOTE_URL, "");
        return props;
    }
    
    
    public Set<String> getPropertiesFromEtc() {
        return super.getPropertiesFromEtc();
    }
    
    
    public String getConfigurationUiClassname() {
        // TODO: do I even need this?
        return null;
    }
    
    
    private ApplicationService getApplicationService() throws QueryProcessingException {
        ApplicationService service = null;
        
        String useLocalValue = getConfiguredParameters().getProperty(USE_LOCAL_CLIENT);
        boolean useLocal = Boolean.parseBoolean(useLocalValue);
        try {
            if (useLocal) {
                service = ApplicationServiceProvider.getApplicationService();
            } else {
                String url = getConfiguredParameters().getProperty(REMOTE_URL);
                service = ApplicationServiceProvider.getApplicationServiceFromUrl(url);
            }
        } catch (Exception ex) {
            throw new QueryProcessingException("Error obtaining application service instance: " + ex.getMessage(), ex);
        }
        
        return service;
    }
    
    
    private boolean useCaseInsensitiveQueries() throws QueryProcessingException {
        String caseInsensitiveValue = getConfiguredParameters().getProperty(CASE_INSENSITIVE_QUERYING);
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
        boolean subclassesDetected = SubclassCheckCache.hasClassProperty(query.getTarget().getName(), service);

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
}
