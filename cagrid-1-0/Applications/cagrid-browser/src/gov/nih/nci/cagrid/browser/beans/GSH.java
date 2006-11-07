package gov.nih.nci.cagrid.browser.beans;

//~--- non-JDK imports --------------------------------------------------------

import gov.nih.nci.cagrid.analytical.client.AnalyticalServiceDataUtils;
import gov.nih.nci.cagrid.analytical.servicedata.AnalyticalType;
import gov.nih.nci.cagrid.analytical.servicedata.AnalyticalType_operation;
import gov.nih.nci.cagrid.browser.exception.GridServiceNotAvailableException;
import gov.nih.nci.cagrid.browser.util.ApplicationCtx;
import gov.nih.nci.cagrid.common.client.ServiceDataUtils;
import gov.nih.nci.cagrid.common.servicedata.DomainModelType;
import gov.nih.nci.cagrid.common.servicedata.DomainObjectType;
import gov.nih.nci.cagrid.common.servicedata.ResearchCenterInfoType;
import gov.nih.nci.cagrid.data.client.CaBIGXMLQueryActivity;
import gov.nih.nci.cagrid.data.client.DataServiceDataUtils;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.ogsa.GridServiceException;
import org.gridforum.ogsi.FaultType;
import org.gridforum.ogsi.GridService;
import org.gridforum.ogsi.HandleType;
import org.gridforum.ogsi.OGSIServiceGridLocator;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.jdom.input.DOMBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Element;
import uk.org.ogsadai.client.toolkit.*;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.xml.namespace.QName;

//~--- classes ----------------------------------------------------------------

/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Jun 15, 2005
 * Time: 11:10:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class GSH {
    public static final String DATA_SERVICE_INTERFACE =
            "caGridDataServiceFactoryPortType";

    // Not sure about this.
    public static final String GENERIC_GRID_SERVICE_INTERFACE = "GridService";
    public static final int    DATA_SERVICE_TYPE              = 0;
    public static final int    GENERIC_GRID_SERVICE_TYPE      = 1;

    //~--- fields -------------------------------------------------------------

    private String                     URL;
    private ActivityMetaData[]         activityInfo;
    private String                     caBIGXMLQuery;
    private DomainModelType            domainModel;
    private AnalyticalType_operation[] operationsInfo;
    private ProductInfoMetaData        productInfo;
    private String                     queryResult;
    private ResearchCenterInfoType     rcInfo;
    private HandleType                 service;
    private DomainObjectType           navigatedObject;

    // default value for service type
    private int serviceType;

    //~--- constructors -------------------------------------------------------

    /** This will create a GSH bean for any service URL
     * but will not verify if there is a valid grid service
     * at the URL. For that use the isValidService() method
     * @param service
     */
    public GSH(HandleType service) {
        this.URL     = service.getValue().toString();
        this.service = service;


        /* Set it to be a Generic grid service */
        this.serviceType = GSH.GENERIC_GRID_SERVICE_TYPE;

        /* Set Common metadata */
        ResearchCenterInfoType rcInfo =
                ServiceDataUtils.getResearchCenterInfoType(this.service);

        this.setRcInfo(rcInfo);
    }

    //~--- methods ------------------------------------------------------------

    public boolean isValidService(){

     OGSIServiceGridLocator locator = new OGSIServiceGridLocator();
        try {
            GridService gridService = locator.getGridServicePort(this.service);
        } catch (FaultType faultType) {
            /** We don't care for the exception, except the service is not valid
             *
             */
            return false;
        } catch (GridServiceException e) {
            return false;
        }

        return true;
    }
    public String doActivity() {

        /* Default action */
        String activityName =
                ApplicationCtx.getParameter("activity").toString();

        /* Set the query textbox empty */
        this.caBIGXMLQuery = "";
        /**
         *  if (activityName.equals(ApplicationCtx.CABIG_XML_QUERY_ACTIVITY.trim()))
         *      forwardTo = "query";
         *
         */

        return "success";
    }

    public String doAddToServiceCart() {
        FacesContext ctx  = FacesContext.getCurrentInstance();
        Application  app  = ctx.getApplication();
        ServiceCart  cart = (ServiceCart) app.createValueBinding(
                "#{serviceCart}").getValue(ctx);

        cart.addGSH(this);

        return "serviceAddedToCart";
    }

    public String doQueryGDSF() {
        GridDataService gsh = null;

        try {
            GridDataServiceFactory factory =
                    ServiceFetcher.getFactory(this.getURL());

            gsh = factory.createGridDataService();
        } catch (MalformedHandleException e) {
            ApplicationCtx.logError(e.getMessage());
        } catch (ServiceCommsException e) {
            ApplicationCtx.logError(e.getMessage());
        }

        CaBIGXMLQueryActivity query =
                new CaBIGXMLQueryActivity(this.getCaBIGXMLQuery());
        ActivityRequest DAIrequest = new ActivityRequest();

        DAIrequest.addActivity(query);

        try {
            Response res = gsh.perform(DAIrequest);

            this.getPrettyPrintXML(res);
        } catch(uk.org.ogsadai.client.toolkit.InternalException e){
            queryResult = e.getMessage();
            return "failed";
        } catch (RequestException e) {
            ApplicationCtx.logError(e.getMessage());
            return "failed";
        } catch (ActivityException e) {
            queryResult = e.getErrorMessage();
            return "failed";
        } catch (ServiceCommsException e) {

            /**
             * Throwing this exception can imply that service requires message level
             * security and grid crednetials. SO we try one more time to invoke the service
             * with users credentials
             */
            try {

                /** Try and retreive the users Proxy certificate from cagrid-browser login bean */
                GridLoginServices appLogin =
                        (GridLoginServices) ApplicationCtx.getBean(
                                "#{loginBean}");
                GlobusCredential credential = appLogin.getGridCredential();

                /** Get GSSCredential from GlobusCredential object */
                GSSCredential gssCred = new GlobusGSSCredentialImpl(
                        credential,
                        GSSCredential.INITIATE_AND_ACCEPT);

                /** Set message leverl security and insert credentials into the ogsa-dai client */
                MessageLevelSecurityProperty property =
                        new MessageLevelSecurityProperty();

                property.setAuthentication(
                        Authentication.SECURE_CONVERSATION_INTEGRITY);
                property.setCredential(gssCred);
                gsh.configure(property);

                /** Perform query */
                Response res = gsh.perform(DAIrequest);
                this.getPrettyPrintXML(res);

            } catch (GSSException e1) {
                ApplicationCtx.logError(e1.getMessage());
                return "failed";
            } catch (RequestException e1) {
                ApplicationCtx.logError(e1.getMessage());
                return "failed";
            } catch (ActivityException e1) {
                queryResult = e1.getErrorMessage();
                return "failed";
            } catch (ServiceCommsException e1) {
                ApplicationCtx.logError(e1.getMessage());
                return "failed";
            }
        }

        return "success";
    }

    public String doRemoveFromserviceCart() {

        ServiceCart    cart = (ServiceCart) ApplicationCtx.getBean("#{serviceCart}");

        cart.removeGSH(this);

        return "serviceRemovedFromCart";
    }

    public String doSetNavigatedService() {



        DiscoveredServices disc =
                (DiscoveredServices) ApplicationCtx.getBean("#{discoveredServices}");

        disc.setNavigatedService(this);

        if (this.serviceType == GSH.DATA_SERVICE_TYPE) {
            return "navigateToGDSF";
        }

        return "navigateToGSH";
    }

    public DomainObjectType getNavigatedObject() {
        return navigatedObject;
    }

    public String doSetNavigatedObject(){

    navigatedObject =       (DomainObjectType)ApplicationCtx.getParameter("domainObject");

        return "navigateToObject";
    }
    /**
     * Fills in Service specific metadata
     */
    public void fillInMetadata() throws GridServiceNotAvailableException {

        /* Set Type of service based on portypes implemented */
        try {
            QName[] interfaces =
                    ServiceDataUtils.getServiceInterfaces(this.service);

            for (int i = 0; i < interfaces.length; i++) {
                if (interfaces[i].getLocalPart().equals(
                        GSH.DATA_SERVICE_INTERFACE)) {
                    this.setServiceType(GSH.DATA_SERVICE_TYPE);
                }
            }

            /* Data Service specific metadata */
            if (this.serviceType == GSH.DATA_SERVICE_TYPE) {
                ProductInfoMetaData pimd    = null;
                ActivityMetaData    actmd[] =
                        new uk.org.ogsadai.client.toolkit.ActivityMetaData[0];

                try {
                    GridDataServiceFactory factory =
                            ServiceFetcher.getFactory(this.URL);

                    this.productInfo  = factory.getProductInfoMetaData();
                    this.activityInfo = factory.getActivityMetaData();
                } catch (MalformedHandleException e) {
                    ApplicationCtx.logError(e.getMessage());
                } catch (ServiceCommsException e) {
                    ApplicationCtx.logError(e.getMessage());
                }

                domainModel          =
                        DataServiceDataUtils.getDomainModel(service);

            } else {
                AnalyticalType anType =
                        AnalyticalServiceDataUtils.getAnalyticalType(service);

                /* HACK: Method doesn't throw exception */
                if (anType != null) {
                    this.operationsInfo = anType.getOperation();
                }
            }
        } catch (java.lang.RuntimeException rEx) {

            /** Throw an exception that will be caught by the container */
            throw new GridServiceNotAvailableException();
        }
    }

    public void getPrettyPrintXML(Response res){

          XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());

        // Get the DOM Element from the response object
        Element responseElement = res.getDocument().getDocumentElement();

        queryResult = xmlOut.outputString((new DOMBuilder()).build(res.getDocument()));


    }


    //~--- get methods --------------------------------------------------------

    public ActivityMetaData[] getActivityInfo() {
        return activityInfo;
    }

    public String getCaBIGXMLQuery() {
        return caBIGXMLQuery;
    }

    public DomainModelType getDomainModel() {
        return domainModel;
    }

    public AnalyticalType_operation[] getOperationsInfo() {
        return operationsInfo;
    }

    public ProductInfoMetaData getProductInfo() {
        return productInfo;
    }

    public String getQueryResult() {
        return queryResult;
    }

    public ResearchCenterInfoType getRcInfo() {
        return rcInfo;
    }

    public int getServiceType() {
        return serviceType;
    }

    public String getURL() {
        return URL;
    }

    //~--- set methods --------------------------------------------------------

    public void setActivityInfo(ActivityMetaData[] activityInfo) {
        this.activityInfo = activityInfo;
    }

    public void setCaBIGXMLQuery(String caBIGXMLQuery) {
        this.caBIGXMLQuery = caBIGXMLQuery;
    }

    public void setDomainModel(DomainModelType domainModel) {
        this.domainModel = domainModel;
    }

    public void setOperationsInfo(AnalyticalType_operation[] operationsInfo) {
        this.operationsInfo = operationsInfo;
    }

    public void setProductInfo(ProductInfoMetaData productInfo) {
        this.productInfo = productInfo;
    }

    public void setQueryResult(String queryResult) {
        this.queryResult = queryResult;
    }

    public void setRcInfo(ResearchCenterInfoType rcInfo) {
        this.rcInfo = rcInfo;
    }

    public void setServiceType(int type) {
        this.serviceType = type;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
