package gov.nih.nci.cagrid.data.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.cql.validation.CqlDomainValidator;
import gov.nih.nci.cagrid.data.cql.validation.CqlStructureValidator;
import gov.nih.nci.cagrid.data.stubs.MalformedQueryException;
import gov.nih.nci.cagrid.data.stubs.QueryProcessingException;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Properties;

import org.apache.axis.utils.ClassUtils;
import org.oasis.wsrf.faults.BaseFaultType;

/** 
 *  gov.nih.nci.cagrid.dataI
 *  TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class DataServiceImpl {
	
	public DataServiceImpl() throws RemoteException {
		
	}
	
	
	public gov.nih.nci.cagrid.cqlresultset.CQLQueryResults query(gov.nih.nci.cagrid.cqlquery.CQLQuery cqlQuery) 
		throws RemoteException, gov.nih.nci.cagrid.data.stubs.QueryProcessingException, gov.nih.nci.cagrid.data.stubs.MalformedQueryException {
		// see if we have any validation to do
		Properties dsConfig = null;
		Properties resourceProperties = null;
		try {
			resourceProperties = ResourcePropertiesUtil.getResourceProperties();
			dsConfig = ServiceConfigUtil.getDataServiceParams();
		} catch (Exception ex) {
			throw (QueryProcessingException) getTypedException(ex, new QueryProcessingException());
		}
		boolean validateCql = dsConfig.getProperty(DataServiceConstants.VALIDATE_CQL_FLAG) != null 
			&& Boolean.valueOf(dsConfig.getProperty(DataServiceConstants.VALIDATE_CQL_FLAG)).booleanValue(); 
		if (validateCql) {
			CqlStructureValidator structureValidator = null;
			try {
				String validatorClassName = dsConfig.getProperty(DataServiceConstants.CQL_VALIDATOR_CLASS);
				Class validatorClass = Class.forName(validatorClassName);
				structureValidator = (CqlStructureValidator) validatorClass.newInstance();
			} catch (Exception ex) {
				throw (QueryProcessingException) getTypedException(ex, new QueryProcessingException());
			}
			try {
				structureValidator.validateCqlStructure(cqlQuery);
			} catch (gov.nih.nci.cagrid.data.MalformedQueryException ex) {
				throw (MalformedQueryException) getTypedException(ex, new MalformedQueryException());
			}
		}
		boolean validateDomain = dsConfig.getProperty(DataServiceConstants.VALIDATE_DOMAIN_MODEL_FLAG) != null
			&& Boolean.valueOf(dsConfig.getProperty(DataServiceConstants.VALIDATE_DOMAIN_MODEL_FLAG)).booleanValue();
		if (validateDomain) {
			CqlDomainValidator domainValidator = null;
			try {
				String validatorClassName = dsConfig.getProperty(DataServiceConstants.DOMAIN_MODEL_VALIDATOR_CLASS);
				Class validatorClass = Class.forName(validatorClassName);
				domainValidator = (CqlDomainValidator) validatorClass.newInstance();
			} catch (Exception ex) {
				throw (QueryProcessingException) getTypedException(ex, new QueryProcessingException());
			}
			try {
				// get the domain model from resource properties
				String domainModelFileName = resourceProperties.getProperty("DomainModelFile");
				DomainModel model = (DomainModel) Utils.deserializeDocument(domainModelFileName, DomainModel.class);
				domainValidator.validateDomainModel(cqlQuery, model);
			} catch (gov.nih.nci.cagrid.data.MalformedQueryException ex) {
				throw (MalformedQueryException) getTypedException(ex, new MalformedQueryException());
			} catch (Exception ex) {
				throw (QueryProcessingException) getTypedException(ex, new QueryProcessingException());
			}
		}
		
		gov.nih.nci.cagrid.data.cql.CQLQueryProcessor processor = null;
		try {
			Properties configParameters = ServiceConfigUtil.getQueryProcessorConfigurationParameters();
			String qpClassName = ServiceConfigUtil.getCqlQueryProcessorClassName();
			Class qpClass = Class.forName(qpClassName);
			processor = (gov.nih.nci.cagrid.data.cql.CQLQueryProcessor) qpClass.newInstance();
			InputStream configStream = ClassUtils.getResourceAsStream(
				getClass(), "server-config.wsdd");
			processor.initialize(configParameters, configStream);
		} catch (Exception ex) {
			throw (QueryProcessingException) getTypedException(ex, new QueryProcessingException());
		}
		try {
			return processor.processQuery(cqlQuery);
		} catch (gov.nih.nci.cagrid.data.QueryProcessingException ex) {
			throw (QueryProcessingException) getTypedException(ex, new QueryProcessingException());
		} catch (gov.nih.nci.cagrid.data.MalformedQueryException ex) {
			throw (MalformedQueryException) getTypedException(ex, new MalformedQueryException());
		}
	}
	
	
	private Exception getTypedException(Exception cause, BaseFaultType fault) {
		FaultHelper helper = new FaultHelper(fault);
		helper.addFaultCause(cause);
		helper.setDescription(cause.getClass().getSimpleName() + " -- " + cause.getMessage());
		return helper.getFault();
	}
}

