/*
 * Created on Jan 25, 2007
 */
package gov.nih.nci.cagrid.annualdemo.util;

import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;

import gov.nih.nci.cagrid.caarray.client.CaArraySvcClient;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;

public class MageHelper
{
	public MageHelper()
	{
		super();
	}
	
	public String fetchBioassay(String id) 
		throws MalformedURIException, RemoteException
	{
		 String url = "http://cagrid-portal.nci.nih.gov:8080/wsrf/services/cagrid/CaArraySvc";

 		 CaArraySvcClient client = new CaArraySvcClient(url);

 		 //CQLQuery query = (CQLQuery) Utils.deserializeDocument("demo.xml", CQLQuery.class);
 		 CQLQuery query = new CQLQuery();
 		 Object target = new Object();
 		 target.setName("gov.nih.nci.mageom.domain.BioAssayData.MeasuredBioAssayData");
 		 Attribute att = new Attribute("identifier", Predicate.EQUAL_TO, id);
 		 target.setAttribute(att);
 		 query.setTarget(target);
 		 
 		 CQLQueryResults results = client.query(query);

 		 results.setTargetClassname("java.lang.Object");
 		 CQLQueryResultsIterator iterator = new CQLQueryResultsIterator(results, true);

 		 if (! iterator.hasNext()) return null;
		 String bioassay = (String) iterator.next();
		 System.out.println(bioassay);
 		 return bioassay;
	}
}
