package gov.nih.nci.cagrid.browser.beans;

//~--- non-JDK imports --------------------------------------------------------

import gov.nih.nci.cagrid.browser.exception.GridServiceNotAvailableException;
import gov.nih.nci.cagrid.browser.util.ApplicationCtx;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.log4j.Logger;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.oasis.wsrf.properties.GetResourceProperty;
import org.oasis.wsrf.properties.WSResourcePropertiesServiceAddressingLocator;

//~--- classes ----------------------------------------------------------------

/**
 * Created by the caGrid Team User: kherm Date: Jun 15, 2005 Time: 11:10:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class GSH {

    private static Logger logger = Logger.getLogger(GSH.class);

    public static final String DATA_SERVICE_INTERFACE = "caGridDataServiceFactoryPortType";

    // Not sure about this.
    public static final String GENERIC_GRID_SERVICE_INTERFACE = "GridService";

    public static final int DATA_SERVICE_TYPE = 0;

    public static final int GENERIC_GRID_SERVICE_TYPE = 1;

    // ~--- fields
    // -------------------------------------------------------------

    private String URL;

    private String caBIGXMLQuery;

    private DomainModel domainModel;

    private String queryResult;

    private ResearchCenter rcInfo;

    private EndpointReferenceType service;

    private UMLClass navigatedObject;

    // default value for service type
    private int serviceType;

    // ~--- constructors
    // -------------------------------------------------------

    /**
         * This will create a GSH bean for any service URL but will not verify
         * if there is a valid grid service at the URL. For that use the
         * isValidService() method
         * 
         * @param service
         */
    public GSH(EndpointReferenceType service) {
	this.URL = service.getAddress().toString();
	this.service = service;

	/* Set it to be a Generic grid service */
	this.serviceType = GSH.GENERIC_GRID_SERVICE_TYPE;

	/* Set Common metadata */
	ResearchCenter rcInfo = null;
	try {
	    rcInfo = MetadataUtils.getServiceMetadata(this.service)
		    .getHostingResearchCenter().getResearchCenter();
	} catch (Exception ex) {
	    logger.error("Error retrieving research info: " + ex.getMessage(),
		    ex);
	    throw new RuntimeException(ex);
	}

	this.setRcInfo(rcInfo);
    }

    // ~--- methods
    // ------------------------------------------------------------

    public boolean isValidService() {
	boolean valid = false;
	GetResourceProperty port;
	WSResourcePropertiesServiceAddressingLocator locator = new WSResourcePropertiesServiceAddressingLocator();
	try {
	    port = locator.getGetResourcePropertyPort(this.service);
	    valid = port != null;
	} catch (ServiceException e) {

	}
	return valid;
    }

    public String doActivity() {

	/* Default action */
	String activityName = ApplicationCtx.getParameter("activity")
		.toString();

	/* Set the query textbox empty */
	this.caBIGXMLQuery = "";
	/**
         * if
         * (activityName.equals(ApplicationCtx.CABIG_XML_QUERY_ACTIVITY.trim()))
         * forwardTo = "query";
         * 
         */

	return "success";
    }

    public String doAddToServiceCart() {
	FacesContext ctx = FacesContext.getCurrentInstance();
	Application app = ctx.getApplication();
	ServiceCart cart = (ServiceCart) app.createValueBinding(
		"#{serviceCart}").getValue(ctx);

	cart.addGSH(this);

	return "serviceAddedToCart";
    }

    public String doQueryGDSF() {

	// TODO: Generate this query
	CQLQuery cqlQuery = null;

	CQLQueryResults cqlResults = null;
	try {
	    DataServiceClient client = new DataServiceClient(this.URL);
	    cqlResults = client.query(cqlQuery);
	} catch (Exception ex) {
	    logger.error("Problem querying remote service:" + this.URL, ex);
	    throw new RuntimeException("Problem query data service at URL:"
		    + this.URL, ex);
	}
	return "success";
    }

    public String doRemoveFromserviceCart() {

	ServiceCart cart = (ServiceCart) ApplicationCtx
		.getBean("#{serviceCart}");

	cart.removeGSH(this);

	return "serviceRemovedFromCart";
    }

    public String doSetNavigatedService() {

	DiscoveredServices disc = (DiscoveredServices) ApplicationCtx
		.getBean("#{discoveredServices}");

	disc.setNavigatedService(this);

	if (this.serviceType == GSH.DATA_SERVICE_TYPE) {
	    return "navigateToGDSF";
	}

	return "navigateToGSH";
    }

    public UMLClass getNavigatedObject() {
	return navigatedObject;
    }

    public String doSetNavigatedObject() {

	navigatedObject = (UMLClass) ApplicationCtx
		.getParameter("domainObject");

	return "navigateToObject";
    }

    /**
         * Fills in Service specific metadata
         */
    public void fillInMetadata() throws GridServiceNotAvailableException {

	/* Set Type of service based on portypes implemented */
	try {
	    this.serviceType = GSH.DATA_SERVICE_TYPE;

	    /* Data Service specific metadata */
	    if (this.serviceType == GSH.DATA_SERVICE_TYPE) {

		domainModel = MetadataUtils.getDomainModel(service);

	    } else {
		// TODO: handle analytical services
	    }
	} catch (Exception ex) {
	    logger.error("Error populating metadata: " + ex.getMessage(), ex);
	    throw new GridServiceNotAvailableException(
		    "Error populating metadata: " + ex.getMessage());
	}
    }

    public void getPrettyPrintXML(CQLQueryResults res) {

	XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
	StringWriter w = new StringWriter();
	try {
	    Utils.serializeObject(res, new QName(
		    "http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet",
		    "CQLResultSet"), w);
	    org.jdom.Element rootEl = (new SAXBuilder(false)).build(
		    new ByteArrayInputStream(w.getBuffer().toString()
			    .getBytes())).getRootElement();
	    queryResult = xmlOut.outputString(rootEl);
	} catch (Exception ex) {
	    logger.debug("Error getting pretty xml: " + ex.getMessage(), ex);
	    throw new RuntimeException("Error getting pretty xml: "
		    + ex.getMessage(), ex);
	}

    }

    // ~--- get methods
    // --------------------------------------------------------

    public String getCaBIGXMLQuery() {
	return caBIGXMLQuery;
    }

    public DomainModel getDomainModel() {
	return domainModel;
    }

    public String getQueryResult() {
	return queryResult;
    }

    public ResearchCenter getRcInfo() {
	return rcInfo;
    }

    public int getServiceType() {
	return serviceType;
    }

    public String getURL() {
	return URL;
    }

    // ~--- set methods
    // --------------------------------------------------------

    public void setCaBIGXMLQuery(String caBIGXMLQuery) {
	this.caBIGXMLQuery = caBIGXMLQuery;
    }

    public void setDomainModel(DomainModel domainModel) {
	this.domainModel = domainModel;
    }

    public void setQueryResult(String queryResult) {
	this.queryResult = queryResult;
    }

    public void setRcInfo(ResearchCenter rcInfo) {
	this.rcInfo = rcInfo;
    }

    public void setServiceType(int type) {
	this.serviceType = type;
    }

    public void setURL(String URL) {
	this.URL = URL;
    }
}

// ~ Formatted by Jindent --- http://www.jindent.com
