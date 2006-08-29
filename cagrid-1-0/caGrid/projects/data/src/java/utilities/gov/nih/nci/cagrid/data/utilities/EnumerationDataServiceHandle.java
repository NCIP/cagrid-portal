package gov.nih.nci.cagrid.data.utilities;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.client.EnumerationQueryClient;

import java.rmi.RemoteException;
import java.util.Iterator;

import org.globus.ws.enumeration.ClientEnumIterator;
import org.globus.ws.enumeration.IterationConstraints;
import org.xmlsoap.schemas.ws._2004._09.enumeration.DataSource;
import org.xmlsoap.schemas.ws._2004._09.enumeration.EnumerateResponse;

/** 
 *  EnumerationDataServiceHandle
 *  Handle class for enumeration supporting data services to simplify handeling of query results
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 24, 2006 
 * @version $Id$ 
 */
public class EnumerationDataServiceHandle {

	private EnumerationQueryClient enumQueryService;
	private DataSource enumService; 
	private IterationConstraints iterConstraints;
	
	public EnumerationDataServiceHandle(final EnumerationQueryClient client) {
		this(client, (DataSource) client, new IterationConstraints());
	}
	
	
	public EnumerationDataServiceHandle(final EnumerationQueryClient client, IterationConstraints constraints) {
		this(client, (DataSource) client, constraints);
	}
	
	
	public EnumerationDataServiceHandle(final EnumerationQueryClient client, DataSource enumerationSource) {
		this(client, enumerationSource, new IterationConstraints());
	}
	
	
	public EnumerationDataServiceHandle(final EnumerationQueryClient client, DataSource enumerationSource, IterationConstraints constraints) {
		this.enumQueryService = client;
		this.enumService = enumerationSource;
		this.iterConstraints = constraints;
	}
	
	
	public Iterator query(CQLQuery query) throws QueryProcessingException, MalformedQueryException {
		Class targetClass = null;
		try {
			targetClass = Class.forName(query.getTarget().getName());
		} catch (Exception ex) {
			throw new QueryProcessingException(ex);
		}
		EnumerateResponse response = null;
		try {
			response = enumQueryService.enumerationQuery(query);
		} catch (RemoteException ex) {
			throw new QueryProcessingException(ex.getMessage(), ex);
		}
		ClientEnumIterator iter = new ClientEnumIterator(enumService, response.getEnumerationContext());
		iter.setIterationConstraints(iterConstraints);
		iter.setItemType(targetClass);
		return iter;
	}
}
