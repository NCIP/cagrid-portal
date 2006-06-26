package gov.nih.nci.cagrid.data.utilities;


import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.common.DataServiceI;
import gov.nih.nci.cagrid.data.stubs.MalformedQueryException;
import gov.nih.nci.cagrid.data.stubs.QueryProcessingException;

import java.rmi.RemoteException;
import java.util.Iterator;

/** 
 *  DataServiceHandle
 *  Wrapper implementation over a data service instance to return iterators
 *  over the CQL Result Set's objects
 *  
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jun 15, 2006 
 * @version $Id$ 
 */
public class DataServiceHandle {

	private DataServiceI service;
	private String wsddFilename;
	
	public DataServiceHandle(DataServiceI dataService) {
		this(dataService, null);
	}
	
	
	public DataServiceHandle(DataServiceI dataService, String wsddFilename) {
		this.service = dataService;
		this.wsddFilename = wsddFilename;
	}
	
	
	public Iterator query(CQLQuery cqlQuery) 
		throws MalformedQueryException, QueryProcessingException, RemoteException {
		CQLQueryResults results = service.query(cqlQuery);
		if (wsddFilename == null) {
			return new CQLQueryResultsIterator(results);
		} else {
			return new CQLQueryResultsIterator(results, wsddFilename);
		}
	}
}
