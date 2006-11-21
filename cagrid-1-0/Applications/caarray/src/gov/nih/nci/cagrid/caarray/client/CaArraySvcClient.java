package gov.nih.nci.cagrid.caarray.client;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.AxisClient;
import org.apache.axis.client.Stub;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.axis.utils.ClassUtils;

import org.oasis.wsrf.properties.GetResourcePropertyResponse;

import org.globus.gsi.GlobusCredential;

import gov.nih.nci.cagrid.caarray.stubs.CaArraySvcPortType;
import gov.nih.nci.cagrid.caarray.stubs.service.CaArraySvcServiceAddressingLocator;
import gov.nih.nci.cagrid.caarray.common.CaArraySvcI;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cagrid.introduce.security.client.ServiceSecurityClient;
import gov.nih.nci.mageom.domain.Description.Description;
import gov.nih.nci.mageom.domain.Experiment.Experiment;
import gov.nih.nci.mageom.domain.Experiment.impl.ExperimentImpl;

/**
 * This class is autogenerated, DO NOT EDIT GENERATED GRID SERVICE METHODS.
 * 
 * This client is generated automatically by Introduce to provide a clean
 * unwrapped API to the service.
 * 
 * On construction the class instance will contact the remote service and
 * retrieve it's security metadata description which it will use to configure
 * the Stub specifically for each method call.
 * 
 * @created by Introduce Toolkit version .99999
 */
public class CaArraySvcClient extends ServiceSecurityClient implements
		CaArraySvcI {
	protected CaArraySvcPortType portType;

	private Object portTypeMutex;

	public CaArraySvcClient(String url) throws MalformedURIException,
			RemoteException {
		this(url, null);
	}

	public CaArraySvcClient(String url, GlobusCredential proxy)
			throws MalformedURIException, RemoteException {
		super(url, proxy);
		initialize();
	}

	public CaArraySvcClient(EndpointReferenceType epr)
			throws MalformedURIException, RemoteException {
		this(epr, null);
	}

	public CaArraySvcClient(EndpointReferenceType epr, GlobusCredential proxy)
			throws MalformedURIException, RemoteException {
		super(epr, proxy);
		initialize();
	}

	private void initialize() throws RemoteException {
		this.portTypeMutex = new Object();
		this.portType = createPortType();
	}

	private CaArraySvcPortType createPortType() throws RemoteException {

		CaArraySvcServiceAddressingLocator locator = new CaArraySvcServiceAddressingLocator();
		// attempt to load our context sensitive wsdd file
		InputStream resourceAsStream = ClassUtils.getResourceAsStream(
				getClass(), "client-config.wsdd");
		if (resourceAsStream != null) {
			// we found it, so tell axis to configure an engine to use it
			EngineConfiguration engineConfig = new FileProvider(
					resourceAsStream);
			// set the engine of the locator
			locator.setEngine(new AxisClient(engineConfig));
		}
		CaArraySvcPortType port = null;
		try {
			port = locator.getCaArraySvcPortTypePort(getEndpointReference());
		} catch (Exception e) {
			throw new RemoteException("Unable to locate portType:"
					+ e.getMessage(), e);
		}

		return port;
	}

	public GetResourcePropertyResponse getResourceProperty(
			QName resourcePropertyQName) throws RemoteException {
		return portType.getResourceProperty(resourcePropertyQName);
	}

	public static void usage() {
		System.out.println(CaArraySvcClient.class.getName()
				+ " -url <service url>");
	}

	public static void main(String[] args) {
		System.out.println("Running the Grid Service Client");
		try {
			if (!(args.length < 2)) {
				if (args[0].equals("-url")) {
					CaArraySvcClient client = new CaArraySvcClient(args[1]);

					CQLQuery query = (CQLQuery) Utils.deserializeDocument(
							"test/resources/query_1.xml", CQLQuery.class);
					CQLQueryResults results = client.query(query);
					StringWriter w = new StringWriter();
					Utils
							.serializeObject(
									results,
									new QName(
											"http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet",
											"CQLResultSet"), w);
					System.out.println(w.getBuffer());

					results.setTargetClassname(ExperimentImpl.class.getName());
					CQLQueryResultsIterator iterator = new CQLQueryResultsIterator(
							results,
							new FileInputStream(
									"src/gov/nih/nci/cagrid/caarray/client/client-config.wsdd"));

					while (iterator.hasNext()) {
						Experiment exp = (Experiment)iterator.next();
						Description[] descs = exp.getDescriptions();
						System.out.println("id=" + exp.getIdentifier());
						for(int i = 0; i < descs.length; i++){
							System.out.println(descs.toString());
						}
					}

				} else {
					usage();
					System.exit(1);
				}
			} else {
				usage();
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadata getServiceSecurityMetadata() throws RemoteException {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"getServiceSecurityMetadata");
        gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataRequest params = new gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataRequest();
        gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse boxedResult = portType.getServiceSecurityMetadata(params);
        return boxedResult.getServiceSecurityMetadata();
      }
    }
	public gov.nih.nci.cagrid.cqlresultset.CQLQueryResults query(gov.nih.nci.cagrid.cqlquery.CQLQuery cqlQuery) throws RemoteException, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType {
      synchronized(portTypeMutex){
        configureStubSecurity((Stub)portType,"query");
        gov.nih.nci.cagrid.data.QueryRequest params = new gov.nih.nci.cagrid.data.QueryRequest();
        gov.nih.nci.cagrid.data.QueryRequestCqlQuery cqlQueryContainer = new gov.nih.nci.cagrid.data.QueryRequestCqlQuery();
        cqlQueryContainer.setCQLQuery(cqlQuery);
        params.setCqlQuery(cqlQueryContainer);
        gov.nih.nci.cagrid.data.QueryResponse boxedResult = portType.query(params);
        return boxedResult.getCQLQueryResultCollection();
      }
    }

}
