package gov.nih.nci.cagrid.fqp.processor;

import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;

/**
 * Listens for changes in FQP's (D)CQL broadcasting status
 * 
 * @author ervin
 */
public interface FQPProcessingStatusListener {

    /**
     * Indicates that the target service successfully processed the query
     * @param serviceURL
     */
    public void targetServiceOk(String serviceURL);
    
    
    /**
     * Indicates that the target service did not respond to connections
     * @param serviceURL
     */
    public void targetServiceConnectionRefused(String serviceURL);
    
    
    /**
     * Indicates that the target service threw some exception while processing
     * @param serviceURL
     * @param ex
     */
    public void targetServiceThrowsException(String serviceURL, Exception ex);
    
    
    /**
     * Indicates that the target service returned some invalid result
     * @param serviceURL
     * @param ex
     */
    public void targetServiceReturnedInvalidResult(String serviceURL, FederatedQueryProcessingException ex);
}
