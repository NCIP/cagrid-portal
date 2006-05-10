package gov.nih.nci.cagrid.gme.service.globus.resource;

import gov.nih.nci.cagrid.common.Utils;

import java.io.File;
import java.util.Calendar;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.axis.MessageContext;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.mds.servicegroup.client.ServiceGroupRegistrationParameters;
import org.globus.wsrf.Constants;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceContextException;
import org.globus.wsrf.ResourceIdentifier;
import org.globus.wsrf.ResourceLifetime;
import org.globus.wsrf.ResourceProperties;
import org.globus.wsrf.ResourceProperty;
import org.globus.wsrf.ResourcePropertySet;
import org.globus.wsrf.config.ContainerConfig;
import org.globus.wsrf.impl.ReflectionResourceProperty;
import org.globus.wsrf.impl.SimpleResourceProperty;
import org.globus.wsrf.impl.SimpleResourcePropertyMetaData;
import org.globus.wsrf.impl.SimpleResourcePropertySet;
import org.globus.wsrf.impl.servicegroup.client.ServiceGroupRegistrationClient;
import org.globus.wsrf.utils.AddressingUtils;

import commonj.timers.Timer;


public class BaseResource implements Resource, ResourceProperties, ResourceLifetime, ResourceIdentifier {

	static final Log logger = LogFactory.getLog(BaseResource.class);

	/** the identifier of this resource */
	private Object id;

	/** Stores the ResourceProperties of this service */
	private ResourcePropertySet propSet;

	private Calendar terminationTime;

	// this can be used to cancel the registration renewal
	private Timer registrationTimer;

	private MetadataConfiguration configuration;

	// Define the metadata resource properties
	private ResourceProperty commonServiceMetadataRP;
	private gov.nih.nci.cagrid.metadata.CommonServiceMetadataType commonServiceMetadataMD;


	// initializes the resource
	public void initialize() throws Exception {
		// choose an ID
		this.id = new Integer(hashCode());

		// create the resource property set
		this.propSet = new SimpleResourcePropertySet(ResourceConstants.RESOURCE_PROPERY_SET);

		// these are the RPs necessary for resource lifetime management
		ResourceProperty prop = new ReflectionResourceProperty(SimpleResourcePropertyMetaData.TERMINATION_TIME, this);
		this.propSet.add(prop); // this property exposes the currenttime, as
		// believed by the local system
		prop = new ReflectionResourceProperty(SimpleResourcePropertyMetaData.CURRENT_TIME, this);
		this.propSet.add(prop);

		// this loads the metadata from XML files
		populateMetadata();

		// now add the metadata as resource properties //init the rp
		this.commonServiceMetadataRP = new SimpleResourceProperty(ResourceConstants.COMMONSERVICEMETADATA_MD_RP);
		// add the value to the rp
		this.commonServiceMetadataRP.add(this.commonServiceMetadataMD);
		// add the rp to the prop set
		this.propSet.add(this.commonServiceMetadataRP);

		// register the service to the index sevice
		performRegistration();

	}


	private void performRegistration() {
		if (getConfiguration().shouldPerformRegistration()) {
			logger.info("Attempting registration.");
			// register with the index service
			ResourceContext ctx;
			try {
				ctx = ResourceContext.getResourceContext();
			} catch (ResourceContextException e) {
				logger.error("Could not get ResourceContext: " + e);
				return;
			}

			EndpointReferenceType epr;
			try {
				epr = AddressingUtils.createEndpointReference(ctx, null);
			} catch (Exception e) {
				logger.error("Could not form EPR: " + e);
				return;
			}
			try {
				// This is how registration parameters are set (read from
				// template)
				File registrationFile = new File(ContainerConfig.getBaseDirectory() + File.separator
					+ getConfiguration().getRegistrationTemplateFile());

				if (registrationFile.exists() && registrationFile.canRead()) {
					logger.debug("Loading registration information from:" + registrationFile);

					ServiceGroupRegistrationParameters params = ServiceGroupRegistrationClient
						.readParams(registrationFile.getAbsolutePath());
					// set our service's EPR as the registrant
					params.setRegistrantEPR(epr);

					ServiceGroupRegistrationClient client = new ServiceGroupRegistrationClient();
					// apply the registration params to the client
					registrationTimer = client.register(params);
				} else {
					logger.error("Unable to read registration file:" + registrationFile);
				}
			} catch (Exception e) {
				logger.error("Exception when trying to register service (" + epr + "): " + e);
			}
		} else {
			logger.info("Skipping registration.");
		}
	}


	private void populateMetadata() {

		loadCommonServiceMetadataFromFile();

	}


	private void loadCommonServiceMetadataFromFile() {
		try {
			File dataFile = new File(ContainerConfig.getBaseDirectory() + File.separator
				+ getConfiguration().getCommonServiceMetadataFile());
			this.commonServiceMetadataMD = (gov.nih.nci.cagrid.metadata.CommonServiceMetadataType) Utils
				.deserializeDocument(dataFile.getAbsolutePath(),
					gov.nih.nci.cagrid.metadata.CommonServiceMetadataType.class);
		} catch (Exception e) {
			logger.error("ERROR: problem populating metadata from file: " + e.getMessage(), e);
		}
	}


	public MetadataConfiguration getConfiguration() {
		if (this.configuration != null) {
			return this.configuration;
		}
		MessageContext ctx = MessageContext.getCurrentContext();

		String servicePath = ctx.getTargetService();

		String jndiName = Constants.JNDI_SERVICES_BASE_NAME + servicePath + "/configuration";
		logger.debug("Will read configuration from jndi name: " + jndiName);
		try {
			Context initialContext = new InitialContext();
			this.configuration = (MetadataConfiguration) initialContext.lookup(jndiName);
		} catch (Exception e) {
			logger.error("when performing JNDI lookup for " + jndiName + ": " + e);
		}

		return this.configuration;
	}


	public ResourcePropertySet getResourcePropertySet() {
		return propSet;
	}


	public Object getID() {
		return id;
	}


	public void setTerminationTime(Calendar time) {
		this.terminationTime = time;
	}


	public Calendar getTerminationTime() {
		return this.terminationTime;
	}


	public Calendar getCurrentTime() {
		return Calendar.getInstance();
	}
}
