package gov.nih.nci.cagrid.fqp.results.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.enumeration.stubs.response.EnumerationResponseContainer;
import gov.nih.nci.cagrid.fqp.processor.DCQLAggregator;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;
import gov.nih.nci.cagrid.fqp.results.common.FederatedQueryResultsConstants;
import gov.nih.nci.cagrid.fqp.results.service.globus.resource.FederatedQueryResultsResource;
import gov.nih.nci.cagrid.fqp.results.stubs.types.InternalErrorFault;
import gov.nih.nci.cagrid.fqp.results.stubs.types.ProcessingNotCompleteFault;
import gov.nih.nci.cagrid.fqp.results.utils.FQPEnumerationExecutionUtil;
import gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPElement;

import org.apache.axis.message.MessageElement;
import org.cagrid.fqp.results.metadata.FederatedQueryExecutionStatus;
import org.cagrid.transfer.context.service.helper.TransferServiceHelper;
import org.cagrid.transfer.context.stubs.types.TransferServiceContextReference;
import org.cagrid.transfer.descriptor.DataDescriptor;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceContextException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.encoding.SerializationException;
import org.globus.wsrf.utils.AnyHelper;
import org.globus.wsrf.utils.XmlUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * TODO:I am the service side implementation class. IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class FederatedQueryResultsImpl extends FederatedQueryResultsImplBase {

    public FederatedQueryResultsImpl() throws RemoteException {
        super();
    }


    public gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection getResults() throws RemoteException,
        gov.nih.nci.cagrid.fqp.results.stubs.types.ProcessingNotCompleteFault,
        gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault,
        gov.nih.nci.cagrid.fqp.results.stubs.types.InternalErrorFault {
        FederatedQueryResultsResource resource = getResource();
        if (!resource.isComplete()) {
            ProcessingNotCompleteFault fault = new ProcessingNotCompleteFault();
            fault.setFaultString("The query processing is not yet complete; current status is: "
                + resource.getStatusMessage());
            throw fault;
        } else if (resource.getProcessingException() != null) {
            FederatedQueryProcessingFault fault = new FederatedQueryProcessingFault();
            fault.setFaultString("Problem executing query: " + resource.getProcessingException());
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(resource.getProcessingException());
            throw helper.getFault();
        }
        return resource.getResults();
    }


    public boolean isProcessingComplete() throws RemoteException {
        FederatedQueryResultsResource resource = getResource();
        return resource.isComplete();
    }


    public gov.nih.nci.cagrid.cqlresultset.CQLQueryResults getAggregateResults() throws RemoteException,
        gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault,
        gov.nih.nci.cagrid.fqp.results.stubs.types.ProcessingNotCompleteFault,
        gov.nih.nci.cagrid.fqp.results.stubs.types.InternalErrorFault {
        FederatedQueryResultsResource resource = getResource();
        if (!resource.isComplete()) {
            FaultHelper helper = new FaultHelper(new ProcessingNotCompleteFault());
            helper.addDescription("Query processing not complete!");
            throw (ProcessingNotCompleteFault) helper.getFault();
        }
        DCQLQueryResultsCollection dcqlResults = resource.getResults();
        CQLQueryResults cqlResults = DCQLAggregator.aggregateDCQLResults(
            dcqlResults, resource.getQuery().getTargetObject().getName());
        return cqlResults;
    }


    public gov.nih.nci.cagrid.enumeration.stubs.response.EnumerationResponseContainer enumerate()
        throws RemoteException, gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault,
        gov.nih.nci.cagrid.fqp.results.stubs.types.ProcessingNotCompleteFault,
        gov.nih.nci.cagrid.fqp.results.stubs.types.InternalErrorFault {
        FederatedQueryResultsResource resource = getResource();
        DCQLQueryResultsCollection dcqlResults = resource.getResults();
        CQLQueryResults cqlResults = DCQLAggregator.aggregateDCQLResults(
            dcqlResults, resource.getQuery().getTargetObject().getName());
        EnumerationResponseContainer response = null;
        try {
            response = FQPEnumerationExecutionUtil.setUpEnumeration(cqlResults);
        } catch (FederatedQueryProcessingException ex) {
            FaultHelper helper = new FaultHelper(new FederatedQueryProcessingFault());
            helper.addDescription("Error setting up WS-Enumeration of results");
            helper.addDescription(ex.getMessage());
            helper.addFaultCause(ex);
            throw (FederatedQueryProcessingFault) helper.getFault();
        }
        return response;
    }


    public org.cagrid.transfer.context.stubs.types.TransferServiceContextReference transfer() throws RemoteException,
        gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault,
        gov.nih.nci.cagrid.fqp.results.stubs.types.ProcessingNotCompleteFault,
        gov.nih.nci.cagrid.fqp.results.stubs.types.InternalErrorFault {
        // get the resource and its results
        FederatedQueryResultsResource resource = getResource();
        DCQLQueryResultsCollection dcqlResults = resource.getResults();
        CQLQueryResults cqlResults = DCQLAggregator.aggregateDCQLResults(
            dcqlResults, resource.getQuery().getTargetObject().getName());
        
        // serialize the results
        StringWriter writer = new StringWriter();
        try {
            Utils.serializeObject(cqlResults, DataServiceConstants.CQL_RESULT_SET_QNAME, writer);
        } catch (Exception ex) {
            FaultHelper helper = new FaultHelper(new InternalErrorFault());
            helper.addFaultCause(ex);
            helper.addDescription("Unable to serialize CQL query results for transfer");
            helper.addDescription(ex.getMessage());
            throw (InternalErrorFault) helper.getFault();
        }
        
        // convert the results to a byte stream for transfer
        // TODO: implement InputStream subclass to queue up bytes as they're available from the
        // serialization process without storing large XML document in memory
        ByteArrayInputStream byteInput = new ByteArrayInputStream(writer.getBuffer().toString().getBytes());
        
        // create a data descriptor for the results
        DataDescriptor descriptor = new DataDescriptor();
        descriptor.setName(DataServiceConstants.CQL_RESULT_SET_QNAME.toString());
        // This causes no deserializer found exception on the client?!?! 
        // descriptor.setMetadata(getResource().getFederatedQueryExecutionStatus());
        /* and THIS causes no deserializer found for anyType (WTF?!?!?)
        try {
            MessageElement metadataElement = getMetadataAsElement();
            descriptor.setMetadata(metadataElement);
        } catch (Exception ex) {
            FaultHelper helper = new FaultHelper(new InternalErrorFault());
            helper.addFaultCause(ex);
            helper.addDescription("Error serializing metadata for data descriptor");
            helper.addDescription(ex.getMessage());
            throw (InternalErrorFault) helper.getFault();
        }
        */
        
        TransferServiceContextReference transferReference = null;
        try {
            transferReference = TransferServiceHelper.createTransferContext(byteInput, descriptor);            
        } catch (RemoteException ex) {
            FaultHelper helper = new FaultHelper(new InternalErrorFault());
            helper.addDescription("Unable to create transfer contex");
            helper.addDescription(ex.getMessage());
            helper.addFaultCause(ex);
            throw (InternalErrorFault) helper.getFault();
        }
        
        return transferReference;
    }

    
    private static FederatedQueryResultsResource getResource() throws ResourceException, ResourceContextException {
        FederatedQueryResultsResource resource = 
            (FederatedQueryResultsResource) ResourceContext.getResourceContext().getResource();
        return resource;
    }
    
    
    private MessageElement getMetadataAsElement() throws Exception {
        FederatedQueryExecutionStatus status = getResource().getFederatedQueryExecutionStatus();
        StringWriter writer = new StringWriter();
        Utils.serializeObject(status, FederatedQueryResultsConstants.FEDERATEDQUERYEXECUTIONSTATUS, writer);
        MessageElement element = null;
        try {
            Document doc = XmlUtils.newDocument(new InputSource(new StringReader(writer.getBuffer().toString())));
            element = new MessageElement(doc.getDocumentElement());
            element.setQName(FederatedQueryResultsConstants.FEDERATEDQUERYEXECUTIONSTATUS);
        } catch (ParserConfigurationException ex) {
            throw new SerializationException("Error in XML parser: " + ex.getMessage(), ex);
        } catch (SAXException ex) {
            throw new SerializationException("Error parsing XML document into an element: " + ex.getMessage(), ex);
        } catch (IOException ex) {
            throw new SerializationException("Error handling XML: " + ex.getMessage(), ex);
        }
        return element;
    }
}
