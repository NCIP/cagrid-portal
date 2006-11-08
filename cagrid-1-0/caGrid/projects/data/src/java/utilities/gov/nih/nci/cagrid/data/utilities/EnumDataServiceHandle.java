package gov.nih.nci.cagrid.data.utilities;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.data.common.EnumerationQueryI;
import gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;

import java.rmi.RemoteException;
import java.util.Iterator;

import org.globus.ws.enumeration.ClientEnumIterator;
import org.globus.ws.enumeration.IterationConstraints;
import org.xmlsoap.schemas.ws._2004._09.enumeration.DataSource;
import org.xmlsoap.schemas.ws._2004._09.enumeration.EnumerateResponse;

/** 
 *  EnumDataServiceHandle
 *  Data Service with Enumeration 'Handle' class to wrap complexity
 *  of WS-Enumeration interface with a reasonable API
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 8, 2006 
 * @version $Id: EnumDataServiceHandle.java,v 1.1 2006-11-08 19:08:14 dervin Exp $ 
 */
public class EnumDataServiceHandle implements DataServiceIterator {

	private EnumerationQueryI queryService;
	private DataSource dataSource;
	private IterationConstraints constraints;
	
	public EnumDataServiceHandle(EnumerationQueryI enumQueryService, DataSource dataSource) {
		this(enumQueryService, dataSource, new IterationConstraints());
	}
	
	
	public EnumDataServiceHandle(EnumerationQueryI enumQueryService, DataSource dataSource, IterationConstraints iterationConstraints) {
		this.queryService = enumQueryService;
		this.dataSource = dataSource;
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
		EnumerateResponse response = queryService.enumerationQuery(query);
		ClientEnumIterator iter = new ClientEnumIterator(
			dataSource, response.getEnumerationContext());
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
