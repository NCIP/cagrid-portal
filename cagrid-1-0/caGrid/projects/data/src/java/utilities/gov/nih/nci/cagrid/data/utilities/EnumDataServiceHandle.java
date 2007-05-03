package gov.nih.nci.cagrid.data.utilities;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.data.enumeration.common.EnumerationDataServiceI;
import gov.nih.nci.cagrid.data.enumeration.stubs.response.EnumerationResponseContainer;
import gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;

import java.rmi.RemoteException;
import java.util.Iterator;

import org.globus.ws.enumeration.ClientEnumIterator;
import org.globus.ws.enumeration.IterationConstraints;

/** 
 *  EnumDataServiceHandle
 *  Data Service with Enumeration 'Handle' class to wrap complexity
 *  of WS-Enumeration interface with a reasonable API
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 8, 2006 
 * @version $Id: EnumDataServiceHandle.java,v 1.3 2007-05-03 18:17:39 dervin Exp $ 
 */
public class EnumDataServiceHandle implements DataServiceIterator {

	private EnumerationDataServiceI queryService;
	private IterationConstraints constraints;
	
	public EnumDataServiceHandle(EnumerationDataServiceI enumQueryService) {
		this(enumQueryService, new IterationConstraints());
	}
	
	
	public EnumDataServiceHandle(EnumerationDataServiceI enumQueryService, IterationConstraints iterationConstraints) {
		this.queryService = enumQueryService;
		this.constraints = iterationConstraints;
	}
	
	
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
        
        ClientEnumIterator iter = new ClientEnumIterator(
			queryService, container.getContext());
		iter.setIterationConstraints(constraints);
		iter.setItemType(targetClass);
		return new IterationWraper(iter);
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
