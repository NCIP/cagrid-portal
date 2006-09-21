package gov.nih.nci.cagrid.fqp.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.fqp.processor.FederatedQueryEngine;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;
import gov.nih.nci.cagrid.fqp.results.service.globus.resource.FQPResultResource;
import gov.nih.nci.cagrid.fqp.results.service.globus.resource.FQPResultResourceHome;
import gov.nih.nci.cagrid.fqp.results.stubs.types.FederatedQueryResultsReference;
import gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault;

import java.rmi.RemoteException;

import org.apache.axis.MessageContext;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.impl.work.WorkManagerImpl;
import org.globus.wsrf.utils.AddressingUtils;

import commonj.work.Work;
import commonj.work.WorkManager;

/**
 * Federated Query Service
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class FederatedQueryProcessorImpl extends FederatedQueryProcessorImplBase {
	private static final int DEFAULT_POOL_SIZE = 10;
	private WorkManager workManager = null;

	protected static Log LOG = LogFactory.getLog(FederatedQueryProcessorImpl.class.getName());

	public FederatedQueryProcessorImpl() throws RemoteException {
		super();
	}

	public gov.nih.nci.cagrid.cqlresultset.CQLQueryResults executeAndAggregateResults(gov.nih.nci.cagrid.dcql.DCQLQuery query) throws RemoteException, gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault {
		FederatedQueryEngine engine = new FederatedQueryEngine();
		CQLQueryResults results = null;
		try {
			results = engine.executeAndAggregateResults(query);
		} catch (FederatedQueryProcessingException e) {
			FederatedQueryProcessingFault fault = new FederatedQueryProcessingFault();
			fault.setFaultString("Problem executing query: " + e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			throw helper.getFault();
		}
		return results;
	}

	public gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection execute(gov.nih.nci.cagrid.dcql.DCQLQuery query) throws RemoteException, gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault {
		FederatedQueryEngine engine = new FederatedQueryEngine();
		DCQLQueryResultsCollection results = null;
		try {
			results = engine.execute(query);
		} catch (FederatedQueryProcessingException e) {
			FederatedQueryProcessingFault fault = new FederatedQueryProcessingFault();
			fault.setFaultString("Problem executing query: " + e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			throw helper.getFault();
		}
		return results;
	}

	public gov.nih.nci.cagrid.fqp.results.stubs.types.FederatedQueryResultsReference executeAsynchronously(
		gov.nih.nci.cagrid.dcql.DCQLQuery query) throws RemoteException {

		// create a result resource
		FQPResultResourceHome resultHome = null;
		try {
			resultHome = (FQPResultResourceHome) getResourceHome("federatedQueryResultsHome");
		} catch (Exception e) {
			// TODO: change to throw "internal error"
			e.printStackTrace();
			throw new RemoteException("Problem locating result home.", e);
		}
		FQPResultResource fqpResultResource = null;
		ResourceKey key = null;
		try {
			key = resultHome.createFQPResultResource();
			fqpResultResource = (FQPResultResource) resultHome.find(key);
		} catch (Exception e) {
			// TODO: change to throw "internal error"
			throw new RemoteException("Creatng and accessing resource.", e);
		}

		// set initial status and complete=false
		fqpResultResource.setComplete(false);
		fqpResultResource.setStatusMessage("Queued for processing");

		// create a worker thread to execute query
		Work work = new QueryExecutionWork(fqpResultResource, query);

		// add worker to pool
		try {
			getWorkManager().schedule(work);
		} catch (Exception e1) {
			e1.printStackTrace();
			// TODO: change to throw "internal error"
			throw new RemoteException("Problem scheduling processing.", e1);
		}

		// return handle to resource
		MessageContext ctx = MessageContext.getCurrentContext();
		String transportURL = (String) ctx.getProperty(org.apache.axis.MessageContext.TRANS_URL);
		transportURL = transportURL.substring(0, transportURL.lastIndexOf('/') + 1);
		// TODO: fix this somehow (pull from property or constant)
		transportURL += "FederatedQueryResults";
		try {
			EndpointReferenceType epr = AddressingUtils.createEndpointReference(transportURL, key);
			return new FederatedQueryResultsReference(epr);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: change to throw "internal error"
			throw new RemoteException("Problem returning reference to result.", e);
		}
	}

	public synchronized WorkManager getWorkManager() {
		if (this.workManager == null) {
			int poolSize = DEFAULT_POOL_SIZE;
			try {
				String poolString = getConfiguration().getThreadPoolSize();
				LOG.debug("ThreadPoolSize property was:" + poolString);
				poolSize = Integer.parseInt(poolString);
			} catch (Exception e) {
				LOG.error("Problem determing pool size, using default(" + poolSize + ").", e);
			}
			this.workManager = new WorkManagerImpl(poolSize);
		}

		return this.workManager;
	}

	public synchronized void setWorkManager(WorkManager workManager) {
		this.workManager = workManager;
	}
}
class QueryExecutionWork implements Work {
	FQPResultResource resource;
	DCQLQuery query;
	

	public QueryExecutionWork(FQPResultResource resource, DCQLQuery query) {
		this.resource = resource;
		this.query = query;
	}

	public void run() {
		FederatedQueryEngine engine = new FederatedQueryEngine();

		try {
			this.resource.setStatusMessage("Processing");
			DCQLQueryResultsCollection collection = engine.execute(query);
			this.resource.setResults(collection);
			this.resource.setStatusMessage("Completed");
			this.resource.setComplete(true);
		} catch (Exception e) {
			this.resource.setResults(null);
			this.resource.setComplete(true);
			this.resource.setProcessingException(e);
		}

	}

	public boolean isDaemon() {
		return false;
	}

	public void release() {
		// Do nothing
	}
}
