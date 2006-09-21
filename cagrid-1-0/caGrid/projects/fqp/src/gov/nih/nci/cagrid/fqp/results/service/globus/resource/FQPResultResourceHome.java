package gov.nih.nci.cagrid.fqp.results.service.globus.resource;

import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.impl.ResourceHomeImpl;
import org.globus.wsrf.impl.SimpleResourceKey;


/**
 * This class implements a resource home
 */

public class FQPResultResourceHome extends ResourceHomeImpl {

	public ResourceKey createFQPResultResource() throws Exception {

		// Create a resource and initialize it
		FQPResultResource resultResource = (FQPResultResource) createNewInstance();
		resultResource.initialize();

		// Get key
		ResourceKey key = new SimpleResourceKey(getKeyTypeName(), resultResource.getID());

		// Add the resource to the list of resources in this home
		add(key, resultResource);
		return key;
	}
}