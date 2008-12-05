package org.cagrid.gaards.dorian.client;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.ResourcePropertyHelper;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.exceptions.InvalidResourcePropertyException;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

import java.io.InputStream;
import java.io.StringReader;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.axis.types.URI.MalformedURIException;
import org.globus.gsi.GlobusCredential;
import org.globus.wsrf.impl.security.authorization.Authorization;
import org.globus.wsrf.utils.XmlUtils;
import org.w3c.dom.Element;


public abstract class DorianBaseClient {

    public static final String VERSION_UNKOWN = "UNKNOWN";
    public static final String VERSION_1_0 = "1.0";
    public static final String VERSION_1_1 = "1.1";
    public static final String VERSION_1_2 = "1.2";
    public static final String VERSION_1_3 = "1.3";

    public static final QName SERVICE_METADATA = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata",
        "ServiceMetadata");

    private DorianClient client;
    private ServiceMetadata serviceMetadata;
    private String serviceURL;


    public DorianBaseClient(String serviceURL) throws MalformedURIException, RemoteException {
        this(serviceURL, null);
    }


    public DorianBaseClient(String serviceURI, GlobusCredential cred) throws MalformedURIException, RemoteException {
        this.serviceURL = serviceURI;
        client = new DorianClient(serviceURI, cred);
    }


    protected DorianClient getClient() {
        return this.client;
    }


    /**
     * This method specifies an authorization policy that the client should use
     * for authorizing the server that it connects to.
     * 
     * @param authorization
     *            The authorization policy to enforce
     */

    public void setAuthorization(Authorization authorization) {
        client.setAuthorization(authorization);
    }


    /**
     * This method obtains the service metadata for the service.
     * 
     * @return The service metadata.
     * @throws ResourcePropertyRetrievalException
     */

    public ServiceMetadata getServiceMetadata() throws InvalidResourcePropertyException,
        ResourcePropertyRetrievalException {
        if (serviceMetadata == null) {
            Element resourceProperty = null;

            InputStream wsdd = getClass().getResourceAsStream("client-config.wsdd");
            resourceProperty = ResourcePropertyHelper.getResourceProperty(client.getEndpointReference(),
                SERVICE_METADATA, wsdd);

            try {
                this.serviceMetadata = (ServiceMetadata) Utils.deserializeObject(new StringReader(XmlUtils
                    .toString(resourceProperty)), ServiceMetadata.class);
            } catch (Exception e) {
                throw new ResourcePropertyRetrievalException("Unable to deserailize: " + e.getMessage(), e);
            }
        }
        return this.serviceMetadata;
    }


    public String getServiceVersion() throws InvalidResourcePropertyException, ResourcePropertyRetrievalException {
        ServiceMetadata sm = getServiceMetadata();
        if (sm == null) {
            return VERSION_UNKOWN;
        } else {
            return sm.getServiceDescription().getService().getVersion();
        }
    }


    public String getServiceURL() {
        return serviceURL;
    }

}
