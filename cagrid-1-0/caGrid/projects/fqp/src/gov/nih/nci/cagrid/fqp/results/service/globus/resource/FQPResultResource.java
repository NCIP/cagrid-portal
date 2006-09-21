package gov.nih.nci.cagrid.fqp.results.service.globus.resource;

import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;

import java.util.Calendar;

import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceIdentifier;
import org.globus.wsrf.ResourceLifetime;
import org.globus.wsrf.ResourceProperties;
import org.globus.wsrf.ResourceProperty;
import org.globus.wsrf.ResourcePropertySet;
import org.globus.wsrf.impl.ReflectionResourceProperty;
import org.globus.wsrf.impl.SimpleResourcePropertyMetaData;
import org.globus.wsrf.impl.SimpleResourcePropertySet;
import org.globus.wsrf.jndi.Initializable;


public class FQPResultResource
	implements
		Resource,
		ResourceIdentifier,
		ResourceLifetime,
		ResourceProperties,
		Initializable {

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
		// TODO: do any init here if you need to (post creation)

		// TODO: pick some way to get a unique id for this resource (maybe some
		// db id)
		this.id = new Integer(hashCode());

		this.propSet = new SimpleResourcePropertySet(ResourceConstants.RESOURCE_PROPERY_SET);

		// these are the RPs necessary for resource lifetime management
		ResourceProperty prop = new ReflectionResourceProperty(SimpleResourcePropertyMetaData.TERMINATION_TIME, this);
		this.propSet.add(prop);
		// this property exposes the currenttime, as believed by the local
		// system
		prop = new ReflectionResourceProperty(SimpleResourcePropertyMetaData.CURRENT_TIME, this);
		this.propSet.add(prop);
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

}