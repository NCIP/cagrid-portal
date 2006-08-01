package gov.nih.nci.cagrid.gridgrouper.stem.service.globus.resource;

import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.impl.ResourceHomeImpl;
import org.globus.wsrf.impl.SimpleResourceKey;


/**
 * This class implements a resource home
 */

public class StemResourceHome extends ResourceHomeImpl {

	public ResourceKey createStemResource() throws Exception {
		
		// Create a resource and initialize it
		StemResource stem = (StemResource) createNewInstance();
		stem.initialize();

		// Get key
		ResourceKey key = new SimpleResourceKey(keyTypeName, stem.getID());
		
		// Add the resource to the list of resources in this home
		add(key, stem);
		return key;
	}
}