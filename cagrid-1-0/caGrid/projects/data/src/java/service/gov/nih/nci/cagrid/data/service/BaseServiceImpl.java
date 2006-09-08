package gov.nih.nci.cagrid.data.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.cql.validation.CqlDomainValidator;
import gov.nih.nci.cagrid.data.cql.validation.CqlStructureValidator;
import gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.lang.reflect.Method;
import java.util.Properties;

import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceContext;
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

	public BaseServiceImpl() {
		domainModelSearchedFor = false;
	}
	
	
	protected void preProcess(CQLQuery cqlQuery) throws QueryProcessingExceptionType, MalformedQueryExceptionType {
		// validation for cql structure		
		if (shouldValidateCqlStructure()) {
			CqlStructureValidator validator = getCqlStructureValidator();  
			try {
				validator.validateCqlStructure(cqlQuery);
			} catch (gov.nih.nci.cagrid.data.MalformedQueryException ex) {
				throw (MalformedQueryExceptionType) getTypedException(ex, new MalformedQueryExceptionType());
			}
		}
		
		// validation for domain model
		if (shouldValidateDomainModel()) {
			CqlDomainValidator validator = getCqlDomainValidator();			
			try {
				// get the domain model from resource properties
				/*
				String domainModelFileName = getResourceProperties().getProperty("DomainModelFile");
				if (domainModelFileName == null) {
					FileNotFoundException ex = new FileNotFoundException("Serialized domain model file " + domainModelFileName + " not found for validation!");
					throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
				}
				FileReader domainModelReader = new FileReader(domainModelFileName);
				DomainModel model = MetadataUtils.deserializeDomainModel(domainModelReader);
				domainModelReader.close();
				*/
				DomainModel model = getDomainModel();
				validator.validateDomainModel(cqlQuery, model);
			} catch (gov.nih.nci.cagrid.data.MalformedQueryException ex) {
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
		return cqlQueryProcessorConfig;
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
	
	
	protected CQLQueryProcessor getCqlQueryProcessorInstance() throws QueryProcessingExceptionType {
		if (cqlQueryProcessorClass == null) {
			try {
				String qpClassName = ServiceConfigUtil.getCqlQueryProcessorClassName();
				cqlQueryProcessorClass = Class.forName(qpClassName);
			} catch (Exception ex) {
				throw (QueryProcessingExceptionType) getTypedException(ex, new QueryProcessingExceptionType());
			}
		}
		try {
			CQLQueryProcessor processor = (gov.nih.nci.cagrid.data.cql.CQLQueryProcessor) 
				cqlQueryProcessorClass.newInstance();
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
}
