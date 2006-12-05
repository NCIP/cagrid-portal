/**
 * $Id $
 */
package gov.nih.nci.cagrid.browser.search;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.metadata.exceptions.QueryInvalidException;
import gov.nih.nci.cagrid.metadata.exceptions.RemoteResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ServiceMetadataSearch extends AbstractSearch {

	private static Logger logger = Logger.getLogger(ServiceMetadataSearch.class);

	public static final String TEXT = "text";
	public static final String RESEARCH_CENTER = "researchCenter";
	public static final String POINT_OF_CONTACT = "pointOfContact";
	public static final String SERVICE_NAME = "serviceName";
	public static final String OPERATION_NAME = "operationName";
	public static final String OPERATION_INPUT = "operationInput";
	public static final String OPERATION_OUTPUT = "operationOutput";
	public static final String PERMISSIBLE_VALUE = "permissibleValue";
	public static final String DOMAIN_MODEL_NAME = "domainModelName";
	public static final String CLASS_NAME = "className";


	private String type;

	private String term;

	public String getTerm() {
		return term;
	}

	public void setTerm(String code) {
		this.term = code;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.browser.beans.search.ServiceDiscoveryThread#reset()
	 * 
	 */
	public void reset() {
		super.reset();
		setTerm(null);
		setType(null);
	}

	public EndpointReferenceType[] doRun(DiscoveryClient client) throws Exception {
		return search(client, getType(), getTerm());
	}

	public static EndpointReferenceType[] search(DiscoveryClient client,
			String category, String term)
			throws RemoteResourcePropertyRetrievalException,
			QueryInvalidException, ResourcePropertyRetrievalException {
		
		EndpointReferenceType[] results = null;
		if (TEXT.equals(category)) {
			results = client.discoverServicesBySearchString(term);
		} else if (RESEARCH_CENTER.equals(category)) {
			results = client.discoverServicesByResearchCenter(term);
		} else if (POINT_OF_CONTACT.equals(category)) {
			results = client.discoverServicesByDataConceptCode(term);
		} else if (SERVICE_NAME.equals(category)) {
			results = client.discoverServicesByName(term);
		} else if (OPERATION_NAME.equals(category)) {
			results = client.discoverServicesByOperationName(term);
		} else if (OPERATION_INPUT.equals(category)) {
			UMLClass klass = new UMLClass();
			klass.setClassName(term);
			klass.setAllowableAsTarget(true);
			results = client.discoverServicesByOperationInput(klass);
		} else if (OPERATION_OUTPUT.equals(category)) {
			UMLClass klass = new UMLClass();
			klass.setClassName(term);
			klass.setAllowableAsTarget(true);
			results = client.discoverServicesByOperationOutput(klass);
		} else if (PERMISSIBLE_VALUE.equals(category)) {
			results = client.discoverDataServicesByPermissibleValue(term);
		} else if (DOMAIN_MODEL_NAME.equals(category)) {
			results = client.discoverDataServicesByDomainModel(term);
		} else if (CLASS_NAME.equals(category)) {
			UMLClass klass = new UMLClass();
			klass.setClassName(term);
			klass.setAllowableAsTarget(true);
			results = client.discoverDataServicesByExposedClass(klass);
		} else {
			throw new RuntimeException("Unknown category: " + category);
		}
		return results;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
