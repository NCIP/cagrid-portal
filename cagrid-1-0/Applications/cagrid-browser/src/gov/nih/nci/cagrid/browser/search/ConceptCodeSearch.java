/**
 * $Id $
 */
package gov.nih.nci.cagrid.browser.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.metadata.exceptions.QueryInvalidException;
import gov.nih.nci.cagrid.metadata.exceptions.RemoteResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ConceptCodeSearch extends AbstractSearch {

	private static Logger logger = Logger.getLogger(ConceptCodeSearch.class);

	public static final String SERVICE = "service";

	public static final String OPERATION = "operation";

	public static final String DATA = "data";

	public static final String MODEL = "model";

	private String type;

	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.browser.beans.search.ServiceDiscoveryThread#reset()
	 * 
	 */
	public void reset() {
		super.reset();
		setCode(null);
		setType(null);
	}

	public EndpointReferenceType[] doRun(DiscoveryClient client) throws Exception {
		return search(client, getType(), getCode());
	}

	public static EndpointReferenceType[] search(DiscoveryClient client,
			String type, String code)
			throws RemoteResourcePropertyRetrievalException,
			QueryInvalidException, ResourcePropertyRetrievalException {
		
		EndpointReferenceType[] results = null;
		if (SERVICE.equals(type)) {
			results = client.discoverServicesByConceptCode(code);
		} else if (OPERATION.equals(type)) {
			results = client.discoverServicesByOperationConceptCode(code);
		} else if (DATA.equals(type)) {
			results = client.discoverServicesByDataConceptCode(code);
		} else if (MODEL.equals(type)) {
			results = client.discoverDataServicesByModelConceptCode(code);
		} else {
			throw new RuntimeException("Unknown category: " + type);
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
