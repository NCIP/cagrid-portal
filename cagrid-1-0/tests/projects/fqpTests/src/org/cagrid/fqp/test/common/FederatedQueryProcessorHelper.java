package org.cagrid.fqp.test.common;

import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.fqp.client.FederatedQueryProcessorClient;
import gov.nih.nci.cagrid.fqp.processor.FederatedQueryEngine;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;
import gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault;
import gov.nih.nci.cagrid.testing.system.deployment.SecureContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;

import java.io.File;
import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;

/** 
 *  FederatedQueryProcessorHelper
 *  Wraps an FQP implementation (Engine or Client) with a consistent interface
 * 
 * @author David Ervin
 * 
 * @created Jul 10, 2008 1:33:38 PM
 * @version $Id: FederatedQueryProcessorHelper.java,v 1.3 2008-11-07 20:34:56 dervin Exp $ 
 */
public class FederatedQueryProcessorHelper {

    private FederatedQueryProcessorClient fqpClient;
    private FederatedQueryEngine fqpEngine;
    private ServiceContainerSource fqpContainerSource;
    
    public FederatedQueryProcessorHelper(FederatedQueryProcessorClient client) {
        this.fqpClient = client;
    }
    
    
    public FederatedQueryProcessorHelper(FederatedQueryEngine engine) {
        this.fqpEngine = engine;
    }
    
    
    public FederatedQueryProcessorHelper(ServiceContainerSource containerSource) {
        this.fqpContainerSource = containerSource;
    }
    
    
    public synchronized DCQLQueryResultsCollection execute(DCQLQuery query) throws RemoteException, 
        FederatedQueryProcessingFault, FederatedQueryProcessingException {
        if (fqpClient == null && fqpContainerSource != null) {
            createClientFromContainer();
        }
        
        if (fqpContainerSource != null && fqpContainerSource.getServiceContainer() instanceof SecureContainer) {
            configureCaDirectory();
        }
        
        DCQLQueryResultsCollection results = null;
        if (fqpClient != null) {
            results = fqpClient.execute(query);
        } else if (fqpEngine != null) {
            results = fqpEngine.execute(query);
        } else {
            throw new IllegalStateException("NO CLIENT OR ENGINE!");
        }
        
        return results;
    }
    
    
    public synchronized CQLQueryResults executeAndAggregateResults(DCQLQuery query) throws RemoteException,
        FederatedQueryProcessingFault, FederatedQueryProcessingException {
        if (fqpClient == null && fqpContainerSource != null) {
            createClientFromContainer();
        }
        
        if (fqpContainerSource != null && fqpContainerSource.getServiceContainer() instanceof SecureContainer) {
            configureCaDirectory();
        }
        
        CQLQueryResults results = null;
        if (fqpClient != null) {
            results = fqpClient.executeAndAggregateResults(query);
        } else if (fqpEngine != null) {
            results = fqpEngine.executeAndAggregateResults(query);
        } else {
            throw new IllegalStateException("NO CLIENT OR ENGINE!");
        }
        
        return results;
    }
    
    
    private void createClientFromContainer() throws RemoteException {
        ServiceContainer container = fqpContainerSource.getServiceContainer();
        String url = null;
        try {
            url = container.getContainerBaseURI().toString() + "cagrid/FederatedQueryProcessor";
            fqpClient = new FederatedQueryProcessorClient(url);
        } catch (MalformedURIException ex) {
            throw new RemoteException("Error creating FQP client URL: " + ex.getMessage(), ex);
        }
    }
    
    
    private synchronized void configureCaDirectory() throws RemoteException {
        File caCertsDir = null;
        try {
            File certsDir = ((SecureContainer) fqpContainerSource.getServiceContainer()).getCertificatesDirectory();
            caCertsDir = new File(certsDir, "ca");
        } catch (Exception ex) {
            throw new RemoteException("Error obtaining CA certificates directory from service container: " + ex.getMessage(), ex);
        }
        org.globus.common.CoGProperties properties = org.globus.common.CoGProperties.getDefault();
        properties.setCaCertLocations(caCertsDir.getAbsolutePath());
        org.globus.common.CoGProperties.setDefault(properties);
        System.out.println("SET CERTS DIRECTORY TO " + caCertsDir.getAbsolutePath());
    }
}
