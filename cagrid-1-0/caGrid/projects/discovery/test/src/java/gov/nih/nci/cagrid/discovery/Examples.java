package gov.nih.nci.cagrid.discovery;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;

import org.apache.axis.message.addressing.EndpointReferenceType;


public class Examples {

	public static void main(String[] args) {
		DiscoveryClient client = null;
		try {
			if (args.length == 1) {
				client = new DiscoveryClient(args[1]);
			} else {
				client = new DiscoveryClient();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		try {
			EndpointReferenceType[] types = client.discoverServicesBySearchString("Scott");
			if (types != null) {
				for (int i = 0; i < types.length; i++) {
					System.out.println(types[i]);
				}
			} else {
				System.out.println("no results.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
