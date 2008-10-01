package gov.nih.nci.cagrid.fqp.results.service.globus.resource;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.fqp.common.FQPConstants;
import gov.nih.nci.cagrid.fqp.results.stubs.types.InternalErrorFault;
import gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault;

import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.fqp.results.metadata.FederatedQueryExecutionStatus;
import org.cagrid.fqp.results.metadata.ProcessingStatus;
import org.cagrid.fqp.results.metadata.ResultsRange;
import org.cagrid.fqp.results.metadata.ServiceConnectionStatus;
import org.cagrid.fqp.results.metadata.TargetServiceStatus;
import org.globus.wsrf.ResourceProperty;
import org.globus.wsrf.ResourcePropertySet;

/**
 * Utility for managing the status resource property of the
 * Federated Query Processor Results Resource
 * 
 * TODO: This class needs to call the store() method on the resource to
 * persist the resource property changes
 * 
 * @author ervin
 */
public class FederatedQueryResultsResourcePropertyManager {

    private ResourcePropertySet resourceProperties = null;
    private FederatedQueryExecutionStatus executionStatus = null;
    
    public FederatedQueryResultsResourcePropertyManager(ResourcePropertySet resourceProperties) {
        this.resourceProperties = resourceProperties;
        this.executionStatus = new FederatedQueryExecutionStatus();
        storeExecutionStatus();
    }
    
    
    public void setExecutionDetailMessage(String message) {
        executionStatus.setExecutionDetails(message);
        storeExecutionStatus();
    }
    
    
    public void setProcessingStatus(ProcessingStatus status) {
        executionStatus.setCurrentStatus(status);
        storeExecutionStatus();
    }
    
    
    public void setTargetServiceConnectionStatusOk(String serviceURL) throws InternalErrorFault {
        TargetServiceStatus status = getTargetServiceStatus(serviceURL);
        status.setConnectionStatus(ServiceConnectionStatus.OK);
        storeExecutionStatus();
    }
    
    
    public void setTargetServiceConnectionStatusRefused(String serviceURL) throws InternalErrorFault {
        TargetServiceStatus status = getTargetServiceStatus(serviceURL);
        status.setConnectionStatus(ServiceConnectionStatus.Could_Not_Connect);
        storeExecutionStatus();
    }
    
    
    public void setTargetServiceConnectionStatusException(String serviceURL, Exception ex) throws InternalErrorFault {
        TargetServiceStatus status = getTargetServiceStatus(serviceURL);
        status.setConnectionStatus(ServiceConnectionStatus.Exception);
        FaultHelper helper = new FaultHelper(new FederatedQueryProcessingFault());
        helper.addDescription("Error parsing data service URL");
        helper.addDescription(ex.getMessage());
        helper.addFaultCause(ex);
        FederatedQueryProcessingFault fqpFault = (FederatedQueryProcessingFault) helper.getFault();
        status.setBaseFault(fqpFault);
        storeExecutionStatus();
    }
    
    
    public void setTargetServiceResultsRange(String serviceURL, int lowerIndex, int count) throws InternalErrorFault {
        TargetServiceStatus status = getTargetServiceStatus(serviceURL);
        ResultsRange range = new ResultsRange(lowerIndex, lowerIndex + count);
        status.setResultsRange(range);
        storeExecutionStatus();
    }
    
    
    private TargetServiceStatus getTargetServiceStatus(String serviceURL) throws InternalErrorFault {
        TargetServiceStatus status = null;
        if (executionStatus.getTargetServiceStatus() == null) {
            executionStatus.setTargetServiceStatus(new TargetServiceStatus[0]);
        }
        for (TargetServiceStatus storedStatus : executionStatus.getTargetServiceStatus()) {
            if (storedStatus.getServiceURL().toString().equals(serviceURL)) {
                status = storedStatus;
                break;
            }
        }
        if (status == null) {
            status = new TargetServiceStatus();
            try {
                status.setServiceURL(new URI(serviceURL));
            } catch (MalformedURIException ex) {
                FaultHelper helper = new FaultHelper(new InternalErrorFault());
                helper.addDescription("Error parsing data service URL");
                helper.addDescription(ex.getMessage());
                helper.addFaultCause(ex);
                throw (InternalErrorFault) helper.getFault();
            }
            TargetServiceStatus[] statusArray = 
                (TargetServiceStatus[]) Utils.appendToArray(executionStatus.getTargetServiceStatus(), status);
            executionStatus.setTargetServiceStatus(statusArray);
            
        }
        return status;
    }
    
    
    private void storeExecutionStatus() {
        ResourceProperty property = resourceProperties.get(FQPConstants.RESULTS_METADATA_QNAME);
        property.set(0, executionStatus);
        
        // store()?
    }
}
