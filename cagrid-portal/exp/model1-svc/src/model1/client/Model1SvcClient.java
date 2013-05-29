/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package model1.client;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.StringWriter;
import java.rmi.RemoteException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import model1.common.Model1SvcI;

import org.apache.axis.client.Stub;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.globus.gsi.GlobusCredential;

/**
 * This class is autogenerated, DO NOT EDIT GENERATED GRID SERVICE ACCESS
 * METHODS.
 * 
 * This client is generated automatically by Introduce to provide a clean
 * unwrapped API to the service.
 * 
 * On construction the class instance will contact the remote service and
 * retrieve it's security metadata description which it will use to configure
 * the Stub specifically for each method call.
 * 
 * @created by Introduce Toolkit version 1.2
 */
public class Model1SvcClient extends Model1SvcClientBase implements Model1SvcI {

	public Model1SvcClient(String url) throws MalformedURIException,
			RemoteException {
		this(url, null);
	}

	public Model1SvcClient(String url, GlobusCredential proxy)
			throws MalformedURIException, RemoteException {
		super(url, proxy);
	}

	public Model1SvcClient(EndpointReferenceType epr)
			throws MalformedURIException, RemoteException {
		this(epr, null);
	}

	public Model1SvcClient(EndpointReferenceType epr, GlobusCredential proxy)
			throws MalformedURIException, RemoteException {
		super(epr, proxy);
	}

	public static void usage() {
		System.out.println(Model1SvcClient.class.getName()
				+ " -url <service url>");
	}

	public static void main(String[] args) {
		System.out.println("Running the Grid Service Client");
		try {
			if (!(args.length < 2)) {
				if (args[0].equals("-url")) {
					Model1SvcClient client = new Model1SvcClient(args[1]);

					CQLQuery query = null;
					try {
						query = (CQLQuery) Utils.deserializeObject(
								new FileReader("test/resources/geneQuery.xml"),
								CQLQuery.class);
					} catch (Exception ex) {
						throw new RuntimeException(
								"Error deserializing query: " + ex.getMessage(),
								ex);
					}
					CQLQueryResults results = client.query(query);
					try {

						Mapping mapping = new Mapping();
						try {
							mapping.loadMapping("file:src/castor-mapping.xml");
						} catch (Exception ex) {
							throw new RuntimeException(
									"Error loading mapping: " + ex.getMessage(),
									ex);
						}

						CQLQueryResultsIterator iterator = new CQLQueryResultsIterator(
								results, new FileInputStream(
										"src/model1/client/client-config.wsdd"));

						while (iterator.hasNext()) {

							StringWriter w = new StringWriter();
							try {

								Marshaller m = new Marshaller(w);
								m.setMapping(mapping);
								m.marshal(iterator.next());

							} catch (Exception ex) {
								throw new RuntimeException(
										"Error marshalling: " + ex.getMessage());
							}

							String xml = w.getBuffer().toString();
							try {
								SchemaFactory schemaFactory = SchemaFactory
										.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
								Source schemaSource = new StreamSource(
										new File("schema/Model1Svc/model1.xsd"));
								Schema schema = schemaFactory
										.newSchema(schemaSource);

								Validator validator = schema.newValidator();
								validator
										.validate(new StreamSource(
												new ByteArrayInputStream(xml
														.getBytes())));
							} catch (Exception ex) {
								throw new RuntimeException(
										"Failed to validate: "
												+ ex.getMessage());
							}

							System.out.println(xml);

						}
					} catch (Exception ex) {
						throw new RuntimeException(
								"Error serializing results: " + ex.getMessage(),
								ex);
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

	public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(
			org.oasis.wsrf.properties.GetMultipleResourceProperties_Element params)
			throws RemoteException {
		synchronized (portTypeMutex) {
			configureStubSecurity((Stub) portType,
					"getMultipleResourceProperties");
			return portType.getMultipleResourceProperties(params);
		}
	}

	public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(
			javax.xml.namespace.QName params) throws RemoteException {
		synchronized (portTypeMutex) {
			configureStubSecurity((Stub) portType, "getResourceProperty");
			return portType.getResourceProperty(params);
		}
	}

	public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(
			org.oasis.wsrf.properties.QueryResourceProperties_Element params)
			throws RemoteException {
		synchronized (portTypeMutex) {
			configureStubSecurity((Stub) portType, "queryResourceProperties");
			return portType.queryResourceProperties(params);
		}
	}

	public gov.nih.nci.cagrid.cqlresultset.CQLQueryResults query(
			gov.nih.nci.cagrid.cqlquery.CQLQuery cqlQuery)
			throws RemoteException,
			gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType,
			gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType {
		synchronized (portTypeMutex) {
			configureStubSecurity((Stub) portType, "query");
			gov.nih.nci.cagrid.data.QueryRequest params = new gov.nih.nci.cagrid.data.QueryRequest();
			gov.nih.nci.cagrid.data.QueryRequestCqlQuery cqlQueryContainer = new gov.nih.nci.cagrid.data.QueryRequestCqlQuery();
			cqlQueryContainer.setCQLQuery(cqlQuery);
			params.setCqlQuery(cqlQueryContainer);
			gov.nih.nci.cagrid.data.QueryResponse boxedResult = portType
					.query(params);
			return boxedResult.getCQLQueryResultCollection();
		}
	}

}
