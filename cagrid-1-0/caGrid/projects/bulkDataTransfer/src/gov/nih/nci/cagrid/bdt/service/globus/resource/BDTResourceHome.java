package gov.nih.nci.cagrid.bdt.service.globus.resource;

import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.impl.ResourceHomeImpl;
import org.globus.wsrf.impl.SimpleResourceKey;

public class BDTResourceHome extends ResourceHomeImpl {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
	public ResourceKey createBDTResource() throws Exception {
		
		// Create a resource and initialize it
		BDTResource bdtr = (BDTResource) createNewInstance();
		bdtr.initialize();

		// Get key
		ResourceKey key = new SimpleResourceKey(keyTypeName, bdtr.getID());
		
		// Add the resource to the list of resources in this home
		add(key, bdtr);
		return key;
	}
}
