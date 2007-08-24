/*
 * Created on Aug 24, 2007
 */
package gov.nih.nci.cagrid.carray.example;

import java.rmi.RemoteException;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;

import gov.nih.nci.cagrid.caarray.client.CaArraySvcClient;
import gov.nih.nci.cagrid.caarray.stubs.mageom.domain.experiment.Experiment;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.exceptions.InvalidResourcePropertyException;
import gov.nih.nci.cagrid.metadata.exceptions.QueryInvalidException;
import gov.nih.nci.cagrid.metadata.exceptions.RemoteResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

public class CaArrayExample
{
	public CaArrayExample()
	{
		super();
	}
	
	public static void runDiscoveryExample() throws MalformedURIException, RemoteResourcePropertyRetrievalException, QueryInvalidException, ResourcePropertyRetrievalException
	{
		String url = "http://cagrid01.bmi.ohio-state.edu:8080/wsrf/services/DefaultIndexService";
		DiscoveryClient dclient = new DiscoveryClient(url);
		for (EndpointReferenceType epr : dclient.getAllServices(false)) {
			System.out.println(epr);
		}
		
		EndpointReferenceType[] eprs = dclient.discoverDataServicesByDomainModel("caArray");
		System.out.println(eprs[0]);
	}
	
	public static void runMetadataExample() throws InvalidResourcePropertyException, RemoteResourcePropertyRetrievalException, ResourcePropertyRetrievalException, MalformedURIException
	{
		String url = "http://caarraydb-stage.nci.nih.gov:80/wsrf/services/caGrid/CaArraySvc";
		EndpointReferenceType epr = new EndpointReferenceType(new URI(url));
		
		ServiceMetadata metadata = MetadataUtils.getServiceMetadata(epr);
		System.out.println(metadata.getHostingResearchCenter().getResearchCenter().getDisplayName());
		
		DomainModel model = MetadataUtils.getDomainModel(epr);
		System.out.println(model.getProjectShortName());
	}
	
	public static void runInvokeExample() throws MalformedURIException, RemoteException
	{
		CQLQuery query = new CQLQuery();
		Object target = new Object();
		target.setName("gov.nih.nci.mageom.domain.Experiment.Experiment");
		query.setTarget(target);
		Attribute att = new Attribute();
		att.setName("identifier");
		att.setPredicate(Predicate.EQUAL_TO);
		att.setValue("gov.nih.nci.ncicb.caarray:Experiment:1015897558050098:1");
		target.setAttribute(att);
		
		String url = "http://caarraydb-stage.nci.nih.gov:80/wsrf/services/caGrid/CaArraySvc";
		CaArraySvcClient client = new CaArraySvcClient(url);
		CQLQueryResults results = client.query(query);
		results.setTargetClassname("gov.nih.nci.cagrid.caarray.stubs.mageom.domain.experiment.Experiment");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		while (iter.hasNext()) {
			Experiment experiment = (Experiment) iter.next();
			System.out.println(experiment.getName());
		}
	}
	
	public static void main(String[] args) throws MalformedURIException, RemoteResourcePropertyRetrievalException, QueryInvalidException, ResourcePropertyRetrievalException, RemoteException
	{
//		runDiscoveryExample();
//		runMetadataExample();
		runInvokeExample();
	}
}
