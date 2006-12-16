/**
 * $Id $
 */
package gov.nih.nci.cagrid.browser.search;

import java.util.HashSet;
import java.util.Set;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.log4j.Logger;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.metadata.exceptions.QueryInvalidException;
import gov.nih.nci.cagrid.metadata.exceptions.RemoteResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ServiceTypeSearch extends AbstractSearch {

	public static Logger logger = Logger.getLogger(ServiceTypeSearch.class);

	public static final String DATA = "data";

	public static final String ANALYTICAL = "analytical";

	private String type;

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.browser.beans.search.ServiceDiscoveryThread#reset()
	 */
	public void reset() {
		super.reset();
		setType(null);
	}

	public EndpointReferenceType[] doRun(DiscoveryClient client)
			throws Exception {
		return search(client, getType());
	}

	public static EndpointReferenceType[] search(DiscoveryClient client,
			String type) throws RemoteResourcePropertyRetrievalException,
			QueryInvalidException, ResourcePropertyRetrievalException {
		EndpointReferenceType[] results = null;

		if (DATA.equals(type)) {
			results = client.getAllDataServices();
		} else {
			EndpointReferenceType[] all = client.getAllServices(true);
			if (all != null) {
				if (ANALYTICAL.equals(type)) {
					Set allSet = new HashSet();
					for (int i = 0; i < all.length; i++) {
						allSet.add(all[i]);
					}
					EndpointReferenceType[] data = client.getAllDataServices();
					if (data == null) {
						logger.error("No data services retrieved!!!!");
					} else {
						for (int i = 0; i < data.length; i++) {
							allSet.remove(data[i]);
						}
					}
					results = (EndpointReferenceType[]) allSet
							.toArray(new EndpointReferenceType[allSet.size()]);

				} else {
					results = all;
				}

			}
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
