package gov.nih.nci.cagrid.wsenum.utils;

import gov.nih.nci.cagrid.enumeration.stubs.response.EnumerationResponseContainer;

import java.io.InputStream;
import java.rmi.RemoteException;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.AxisClient;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.ws.enumeration.ClientEnumIterator;
import org.xmlsoap.schemas.ws._2004._09.enumeration.DataSource;
import org.xmlsoap.schemas.ws._2004._09.enumeration.service.EnumerationServiceAddressingLocator;

/** 
 *  EnumerationResponseHelper
 *  A collection of methods to simplify use of the WS-Enumeration
 *  client side tools, as well as create Iterations from
 *  CaGridEnumeration service responses to create enumerations
 * 
 * @author David Ervin
 * 
 * @created May 16, 2007 2:09:06 PM
 * @version $Id: EnumerationResponseHelper.java,v 1.1 2007-05-16 18:46:48 dervin Exp $ 
 */
public class EnumerationResponseHelper {
    
    /**
     * Creates a client enumeration iterator to iterate over
     * a remove enumeration resource
     * 
     * @param container
     *      The enumeration response container
     * @return
     *      The client side iterator
     * @throws RemoteException
     */
    public static ClientEnumIterator createClientIterator(EnumerationResponseContainer container) throws RemoteException {
        return createClientIterator(container, null);
    }
    
    
    /**
     * Creates a client enumeration iterator to iterate over
     * a remove enumeration resource
     * 
     * @param container
     *      The enumeration response container
     * @param wsddInputStream
     *      The WSDD configuration stream for the service.  May be <code>null</code>
     * @return
     *      The client side iterator
     * @throws RemoteException
     */
    public static ClientEnumIterator createClientIterator(EnumerationResponseContainer container, 
        InputStream wsddInputStream) throws RemoteException {
        DataSource dataSource = createDataSource(container.getEPR(), wsddInputStream);
        ClientEnumIterator iter = new ClientEnumIterator(dataSource, container.getContext());
        return iter;
    }


    /**
     * Creates a DataSource implementation which connects to a remote
     * WS-Enumeration implementation
     * 
     * @param epr
     *      The endpoint of the remote enumeration
     * @return
     *      The data source
     * @throws RemoteException
     */
    public static DataSource createDataSource(EndpointReferenceType epr) throws RemoteException {
        return createDataSource(epr, null);
    }
    
    
    /**
     * Creates a DataSource implementation which connects to a remote
     * WS-Enumeration implementation
     * 
     * @param epr
     *      The endpoint of the remote enumeration
     * @param wsddInputStream
     *      The WSDD configuration stream for the service.  May be <code>null</code>
     * @return
     *      The data source
     * @throws RemoteException
     */
    public static DataSource createDataSource(EndpointReferenceType epr, InputStream wsddInputStream) throws RemoteException {
        EnumerationServiceAddressingLocator locator = new EnumerationServiceAddressingLocator();
        
        // attempt to load our context sensitive wsdd file
        if (wsddInputStream != null) {
            // we found it, so tell axis to configure an engine to use it
            EngineConfiguration engineConfig = new FileProvider(wsddInputStream);
            // set the engine of the locator
            locator.setEngine(new AxisClient(engineConfig));
        }
        DataSource port = null;
        try {
            port = locator.getDataSourcePort(epr);
        } catch (Exception e) {
            throw new RemoteException("Unable to locate portType:" + e.getMessage(), e);
        }

        return port;
    }
}
