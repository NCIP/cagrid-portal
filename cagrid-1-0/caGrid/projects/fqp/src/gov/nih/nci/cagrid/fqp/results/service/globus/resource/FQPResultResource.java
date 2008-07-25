package gov.nih.nci.cagrid.fqp.results.service.globus.resource;

import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;
import gov.nih.nci.cagrid.fqp.results.common.FederatedQueryResultsConstants;

import java.util.Calendar;

import org.apache.axis.components.uuid.UUIDGen;
import org.apache.axis.components.uuid.UUIDGenFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.RemoveCallback;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceIdentifier;
import org.globus.wsrf.ResourceLifetime;
import org.globus.wsrf.ResourceProperties;
import org.globus.wsrf.ResourceProperty;
import org.globus.wsrf.ResourcePropertySet;
import org.globus.wsrf.impl.ReflectionResourceProperty;
import org.globus.wsrf.impl.SimpleResourcePropertyMetaData;
import org.globus.wsrf.impl.SimpleResourcePropertySet;
import org.globus.wsrf.impl.security.descriptor.ResourceSecurityDescriptor;
import org.globus.wsrf.jndi.Initializable;
import org.globus.wsrf.security.SecureResource;


public class FQPResultResource
	implements
		Resource,
		RemoveCallback,
		ResourceIdentifier,
		ResourceLifetime,
		ResourceProperties,
		SecureResource,
		Initializable {

	protected static Log LOG = LogFactory.getLog(FQPResultResource.class.getName());
	private static final UUIDGen UUIDGEN = UUIDGenFactory.getUUIDGen();

	// Resource security descriptor for this resouce
	private ResourceSecurityDescriptor resourceSecDesc;

	/** the identifier of this resource... should be unique in the service */
	private Object id;
	private ResourcePropertySet propSet;
	private Calendar terminationTime;

	// one of the service specific "values" of your resource
	private String statusMessage;
	private boolean isComplete = false;
	private DCQLQueryResultsCollection results;
	private Exception processingException;


	/**
	 * 
	 * @see org.globus.wsrf.ResourceIdentifier#getID()
	 */
	public Object getID() {
		return this.id;
	}


	/**
	 * 
	 * @see org.globus.wsrf.jndi.Initializable#initialize()
	 */
	public void initialize() throws Exception {
		// use a new UUID as the unique identifier for this resource
		this.id = UUIDGEN.nextUUID();

		this.propSet = new SimpleResourcePropertySet(FederatedQueryResultsConstants.RESOURCE_PROPERTY_SET);

		// these are the RPs necessary for resource lifetime management
		ResourceProperty prop = new ReflectionResourceProperty(SimpleResourcePropertyMetaData.TERMINATION_TIME, this);
		this.propSet.add(prop);
		// this property exposes the currenttime, as believed by the local
		// system
		prop = new ReflectionResourceProperty(SimpleResourcePropertyMetaData.CURRENT_TIME, this);
		this.propSet.add(prop);

		LOG.info("Resource (" + getID() + ") being created.");
	}


	/**
	 * 
	 * @see org.globus.wsrf.ResourceLifetime#setTerminationTime(java.util.Calendar)
	 */
	public void setTerminationTime(Calendar time) {
		this.terminationTime = time;
	}


	/**
	 * 
	 * 
	 * @see org.globus.wsrf.ResourceLifetime#getTerminationTime()
	 */
	public Calendar getTerminationTime() {
		return this.terminationTime;
	}


	/**
	 * 
	 * @see org.globus.wsrf.ResourceLifetime#getCurrentTime()
	 */
	public Calendar getCurrentTime() {
		return Calendar.getInstance();
	}


	/**
	 * 
	 * @see org.globus.wsrf.ResourceProperties#getResourcePropertySet()
	 */
	public ResourcePropertySet getResourcePropertySet() {
		return propSet;
	}


	public ResourceSecurityDescriptor getSecurityDescriptor() {
		return this.resourceSecDesc;
	}


	public void setSecurityDescriptor(ResourceSecurityDescriptor desc) {
		this.resourceSecDesc = desc;
	}


	public String getStatusMessage() {
		return this.statusMessage;
	}


	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}


	public boolean isComplete() {
		return this.isComplete;
	}


	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}


	public DCQLQueryResultsCollection getResults() {
		return this.results;
	}


	public void setResults(DCQLQueryResultsCollection results) {
		this.results = results;
	}


	public Exception getProcessingException() {
		return this.processingException;
	}


	public void setProcessingException(Exception e) {
		this.processingException = e;
	}


	public void remove() throws ResourceException {
		LOG.info("Resource (" + getID() + ") being removed.");
	}
}