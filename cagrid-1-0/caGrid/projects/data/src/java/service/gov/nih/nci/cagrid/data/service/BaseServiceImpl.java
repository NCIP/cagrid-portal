package gov.nih.nci.cagrid.data.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.auditing.AuditorConfiguration;
import gov.nih.nci.cagrid.data.auditing.DataServiceAuditors;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.cql.validation.CqlDomainValidator;
import gov.nih.nci.cagrid.data.cql.validation.CqlStructureValidator;
import gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;
import gov.nih.nci.cagrid.data.service.auditing.DataServiceAuditor;
import gov.nih.nci.cagrid.data.service.auditing.QueryBeginAuditingEvent;
import gov.nih.nci.cagrid.data.service.auditing.QueryProcessingFailedAuditingEvent;
import gov.nih.nci.cagrid.data.service.auditing.QueryResultsAuditingEvent;
import gov.nih.nci.cagrid.data.service.auditing.ValidationAuditingEvent;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.security.SecurityManager;
import org.oasis.wsrf.faults.BaseFaultType;

/** 
 *  BaseServiceImpl
 *  Base class for data service and data service like service implementations
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Sep 5, 2006 
 * @version $Id$ 
 */
public abstract class BaseServiceImpl {
	private Properties dataServiceConfig = null;
	private Properties cqlQueryProcessorConfig = null;
	private Properties resourceProperties = null;
	
	private CqlStructureValidator cqlStructureValidator = null;
	private CqlDomainValidator cqlDomainValidator = null;
	
	private DomainModel domainModel = null;
	private boolean domainModelSearchedFor;
	
	private Class cqlQueryProcessorClass = null;
    
    private List<DataServiceAuditor> auditors = null;

	public BaseServiceImpl() throws RemoteException {
		domainModelSearchedFor = false;
        initializeAuditors();
	}
    
	
	protected void preProcess(CQLQuery cqlQuery) throws QueryProcessingExceptionType, MalformedQueryExceptionType {
		// validation for cql structure		
		if (shouldValidateCqlStructure()) {
			CqlStructureValidator validator = getCqlStructureValidator();  
			try {
				validator.validateCqlStructure(cqlQuery);
			} catch (gov.nih.nci.cagrid.data.MalformedQueryException ex) {
                fireAuditValidationFailure(cqlQuery, ex, null);
				throw (MalformedQueryExceptionType) getTypedException(ex, new MalformedQueryExceptionType());
			}
		}
		
		// validation for domain model
		if (shouldValidateDomainModel()) {
			CqlDomainValidator validator = getCqlDomainValidator();			
			try {
				DomainModel model = getDomainModel();
				validator.validateDomainModel(cqlQuery, model);
			} catch (gov.nih.nci.cagrid.data.MalformedQueryException ex) {
                fireAuditValidationFailure(cqlQuery, null, ex);
				throw (MalformedQueryExceptionType) getTypedException(ex, new MalformedQueryExceptionType());
			} catch (Exception ex) {
				throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
			}
		}
	}
	
	
	protected boolean shouldValidateCqlStructure() throws QueryProcessingExceptionType {
		return getDataServiceConfig().getProperty(DataServiceConstants.VALIDATE_CQL_FLAG) != null 
			&& Boolean.valueOf(getDataServiceConfig().getProperty(
				DataServiceConstants.VALIDATE_CQL_FLAG)).booleanValue();
	}
	
	
	protected boolean shouldValidateDomainModel() throws QueryProcessingExceptionType {
		return getDataServiceConfig().getProperty(DataServiceConstants.VALIDATE_DOMAIN_MODEL_FLAG) != null
			&& Boolean.valueOf(getDataServiceConfig().getProperty(
				DataServiceConstants.VALIDATE_DOMAIN_MODEL_FLAG)).booleanValue();
	}
	
	
	protected CqlStructureValidator getCqlStructureValidator() throws QueryProcessingExceptionType {
		if (cqlStructureValidator == null) {
			try {
				String validatorClassName = getDataServiceConfig().getProperty(DataServiceConstants.CQL_VALIDATOR_CLASS);
				Class validatorClass = Class.forName(validatorClassName);
				cqlStructureValidator = (CqlStructureValidator) validatorClass.newInstance();
			} catch (Exception ex) {
				throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
			}
		}
		return cqlStructureValidator;
	}
	
	
	protected CqlDomainValidator getCqlDomainValidator() throws QueryProcessingExceptionType {
		if (cqlDomainValidator == null) {
			try {
				String validatorClassName = getDataServiceConfig().getProperty(DataServiceConstants.DOMAIN_MODEL_VALIDATOR_CLASS);
				Class validatorClass = Class.forName(validatorClassName);
				cqlDomainValidator = (CqlDomainValidator) validatorClass.newInstance();
			} catch (Exception ex) {
				throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
			}
		}
		return cqlDomainValidator;
	}
	
	
	protected Properties getDataServiceConfig() throws QueryProcessingExceptionType {
		if (dataServiceConfig == null) {
			try {
				dataServiceConfig = ServiceConfigUtil.getDataServiceParams();
			} catch (Exception ex) {
				throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
			}
		}
		return dataServiceConfig;
	}
	
	
	protected Properties getCqlQueryProcessorConfig() throws QueryProcessingExceptionType {
		if (cqlQueryProcessorConfig == null) {
			try {
                cqlQueryProcessorConfig = ServiceConfigUtil.getQueryProcessorConfigurationParameters();
			} catch (Exception ex) {
				throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
			}
		}
        // clone the query processor config instance 
        // (in case they get modified by the Query Processor implementation)
        Properties clone = new Properties();
        Enumeration keyEnumeration = cqlQueryProcessorConfig.keys();
        while (keyEnumeration.hasMoreElements()) {
            String key = (String) keyEnumeration.nextElement();
            String value = cqlQueryProcessorConfig.getProperty(key);
            clone.setProperty(key, value);
        }
		return clone;
	}
	
	
	protected Properties getResourceProperties() throws QueryProcessingExceptionType {
		if (resourceProperties == null) {
			try {
				resourceProperties = ResourcePropertiesUtil.getResourceProperties();
			} catch (Exception ex) {
				throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
			}
		}
		return resourceProperties;
	}
	
	
	private Class getCqlQueryProcessorClass() throws QueryProcessingExceptionType {
		if (cqlQueryProcessorClass == null) {
			try {
				String qpClassName = ServiceConfigUtil.getCqlQueryProcessorClassName();
				cqlQueryProcessorClass = Class.forName(qpClassName);
			} catch (Exception ex) {
				throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
			}
		}
		return cqlQueryProcessorClass;
	}
	
	
	protected CQLQueryProcessor getCqlQueryProcessorInstance() throws QueryProcessingExceptionType {
		try {
			CQLQueryProcessor processor = (gov.nih.nci.cagrid.data.cql.CQLQueryProcessor) 
				getCqlQueryProcessorClass().newInstance();
			String serverConfigLocation = ServiceConfigUtil.getConfigProperty(
				DataServiceConstants.SERVER_CONFIG_LOCATION);
			InputStream configStream = new FileInputStream(serverConfigLocation);
			processor.initialize(getCqlQueryProcessorConfig(), configStream);
			return processor;
		} catch (Exception ex) {
			throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
		}
	}
	
	
	protected DomainModel getDomainModel() throws Exception {
		if (domainModel == null && !domainModelSearchedFor) {
			Resource serviceBaseResource = ResourceContext.getResourceContext().getResource();
			Method[] resourceMethods = serviceBaseResource.getClass().getMethods();
			for (int i = 0; i < resourceMethods.length; i++) {
				if (resourceMethods[i].getReturnType() != null 
					&& resourceMethods[i].getReturnType().equals(DomainModel.class)) {
					domainModel = (DomainModel) resourceMethods[i].invoke(serviceBaseResource, new Object[] {});
					break;
				}
			}
			domainModelSearchedFor = true;
		}
		return domainModel;
	}
	
	
	protected Exception getTypedException(Exception cause, BaseFaultType fault) {
		FaultHelper helper = new FaultHelper(fault);
		helper.addFaultCause(cause);
		helper.setDescription(cause.getClass().getSimpleName() + " -- " + cause.getMessage());
		return helper.getFault();
	}
    
    
    // ----------
    // Auditor support
    // ----------
    
    
    private synchronized void initializeAuditors() throws RemoteException {
        if (auditors == null) {
            auditors = new LinkedList();
            try {
                String configFileName = getDataServiceConfig().getProperty(
                    DataServiceConstants.DATA_SERVICE_AUDITORS_CONFIG_FILE_PROPERTY);
                if (configFileName != null) {
                    DataServiceAuditors auditorConfig = (DataServiceAuditors) 
                        Utils.deserializeDocument(configFileName, DataServiceAuditors.class);
                    if (auditorConfig.getAuditorConfiguration() != null) {
                        for (AuditorConfiguration config : auditorConfig.getAuditorConfiguration()) {
                            DataServiceAuditor auditor = createAuditor(config);
                            auditors.add(auditor);
                        }
                    }
                }
            } catch (Exception ex) {
                throw new RemoteException(ex.getMessage(), ex);
            }
        }
    }
    
    
    private DataServiceAuditor createAuditor(AuditorConfiguration config) throws Exception {
        String auditorClassName = config.getClassName();
        Class auditorClass = Class.forName(auditorClassName);
        Constructor auditorConstructor = auditorClass.getConstructor(
            new Class[] {AuditorConfiguration.class});
        return (DataServiceAuditor) auditorConstructor.newInstance(new Object[] {config});
    }
    
    
    /**
     * Fires a query begins auditing event
     * 
     * @param query
     * @throws RemoteException
     */
    protected void fireAuditQueryBegins(CQLQuery query) throws RemoteException {
        initializeAuditors();
        if (auditors.size() != 0) {
            String callerIdentity = SecurityManager.getManager().getCaller();
            QueryBeginAuditingEvent event = new QueryBeginAuditingEvent(query, callerIdentity);
            for (DataServiceAuditor auditor : auditors) {
                if (auditor.getAuditorConfiguration()
                    .getMonitoredEvents().isQueryBegin()) {
                    auditor.auditQueryBegin(event);
                }
            }
        }
    }
    
    
    /**
     * Fires a validation failure auditing event
     * 
     * @param query
     * @param structureException
     * @param domainException
     * @throws RemoteException
     */
    protected void fireAuditValidationFailure(CQLQuery query, 
        MalformedQueryException structureException, MalformedQueryException domainException) {
        if (auditors.size() != 0) {
            String callerIdentity = SecurityManager.getManager().getCaller();
            ValidationAuditingEvent event = 
                new ValidationAuditingEvent(query, callerIdentity, structureException, domainException);
            for (DataServiceAuditor auditor : auditors) {
                if (auditor.getAuditorConfiguration()
                    .getMonitoredEvents().isValidationFailure()) {
                    auditor.auditValidation(event);
                }
            }
        }
    }
    
    
    /**
     * Fires a query processing failure auditing event
     * 
     * @param query
     * @param qpException
     * @throws RemoteException
     */
    protected void fireAuditQueryProcessingFailure(CQLQuery query,
        QueryProcessingException qpException) {
        if (auditors.size() != 0) {
            String callerIdentity = SecurityManager.getManager().getCaller();
            QueryProcessingFailedAuditingEvent event = 
                new QueryProcessingFailedAuditingEvent(query, callerIdentity, qpException);
            for (DataServiceAuditor auditor : auditors) {
                if (auditor.getAuditorConfiguration()
                    .getMonitoredEvents().isQueryProcessingFailure()) {
                    auditor.auditQueryProcessingFailed(event);
                }
            }
        }
    }
    
    
    /**
     * Fires a query results auditing event
     * 
     * @param query
     * @param results
     * @throws RemoteException
     */
    protected void fireAuditQueryResults(CQLQuery query, CQLQueryResults results) {
        if (auditors.size() != 0) {
            String callerIdentity = SecurityManager.getManager().getCaller();
            QueryResultsAuditingEvent event = new QueryResultsAuditingEvent(query, callerIdentity, results);
            for (DataServiceAuditor auditor : auditors) {
                if (auditor.getAuditorConfiguration()
                    .getMonitoredEvents().isQueryResults()) {
                    auditor.auditQueryResults(event);
                }
            }
        }
    }
}
