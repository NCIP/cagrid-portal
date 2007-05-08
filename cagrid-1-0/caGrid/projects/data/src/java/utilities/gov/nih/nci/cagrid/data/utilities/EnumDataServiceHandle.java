package gov.nih.nci.cagrid.data.utilities;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.data.enumeration.common.EnumerationDataServiceI;
import gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;
import gov.nih.nci.cagrid.enumeration.stubs.response.EnumerationResponseContainer;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Iterator;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.AxisClient;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.utils.ClassUtils;
import org.globus.ws.enumeration.ClientEnumIterator;
import org.globus.ws.enumeration.IterationConstraints;
import org.xmlsoap.schemas.ws._2004._09.enumeration.DataSource;
import org.xmlsoap.schemas.ws._2004._09.enumeration.service.EnumerationServiceAddressingLocator;

/** 
 *  EnumDataServiceHandle
 *  Data Service with Enumeration 'Handle' class to wrap complexity
 *  of WS-Enumeration interface with a reasonable API
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 8, 2006 
 * @version $Id: EnumDataServiceHandle.java,v 1.7 2007-05-08 20:52:43 dervin Exp $ 
 */
public class EnumDataServiceHandle implements DataServiceIterator {

	private EnumerationDataServiceI queryService;
	private IterationConstraints constraints;
	
    /**
     * Creates a simplified enumeration data service handle using
     * the default iteration constraints for WS-Enumeration
     * 
     * @param enumQueryService
     */
	public EnumDataServiceHandle(EnumerationDataServiceI enumQueryService) {
		this(enumQueryService, new IterationConstraints());
	}
	
	
    /**
     * Creates a simplified enumeration data service handle
     * using the supplied iteration constraints for
     * WS-Enumeration
     * 
     * @param enumQueryService
     * @param iterationConstraints
     */
	public EnumDataServiceHandle(EnumerationDataServiceI enumQueryService, IterationConstraints iterationConstraints) {
		this.queryService = enumQueryService;
		this.constraints = iterationConstraints;
	}
	
	
    /**
     * Performs a CQL query against the data source and returns an Iterator
     * implementation, which hides the complexity of initializing a
     * WS-Enumeration client session
     */
	public Iterator query(CQLQuery query) 
		throws MalformedQueryExceptionType, QueryProcessingExceptionType, RemoteException {
		Class targetClass = null;
		try {
			targetClass = Class.forName(query.getTarget().getName());
		} catch (ClassNotFoundException ex) {
			FaultHelper helper = new FaultHelper(new QueryProcessingExceptionType());
			helper.addFaultCause(ex);
			throw (QueryProcessingExceptionType) helper.getFault();
		}
		EnumerationResponseContainer container = queryService.enumerationQuery(query);
        
        DataSource dataSource = createDataSource(container.getEPR());
        
        ClientEnumIterator iter = new ClientEnumIterator(
			dataSource, container.getContext());
		iter.setIterationConstraints(constraints);
		iter.setItemType(targetClass);
		return new IterationWraper(iter);
	}
	
    
    private DataSource createDataSource(EndpointReferenceType epr) throws RemoteException {

        EnumerationServiceAddressingLocator locator = new EnumerationServiceAddressingLocator();
        
        // attempt to load our context sensitive wsdd file
        InputStream resourceAsStream = ClassUtils.getResourceAsStream(getClass(), "client-config.wsdd");
        if (resourceAsStream != null) {
            // we found it, so tell axis to configure an engine to use it
            EngineConfiguration engineConfig = new FileProvider(resourceAsStream);
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
    
	
	private static class IterationWraper implements Iterator {
		private ClientEnumIterator clientIter;
		
		public IterationWraper(ClientEnumIterator clientIter) {
			this.clientIter = clientIter;
		}
		
		
		public boolean hasNext() {
			return clientIter.hasNext();
		}
		
		
		public Object next() {
			return clientIter.next();
		}
		
		
		public void remove() {
			throw new UnsupportedOperationException("remove() is not implemented");
		}
	}
}
