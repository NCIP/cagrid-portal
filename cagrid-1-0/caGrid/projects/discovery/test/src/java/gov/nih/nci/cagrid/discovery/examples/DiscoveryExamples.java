package gov.nih.nci.cagrid.discovery.examples;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.metadata.common.PointOfContact;

import org.apache.axis.message.addressing.EndpointReferenceType;


public class DiscoveryExamples {

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

		String searchString = "Scott";
		String center = "OSU";

		try {
			EndpointReferenceType[] services = null;

			printHeader("All Services");
			services = client.getAllServices();
			printResults(services);

			printHeader("Search String [" + searchString + "]");
			services = client.discoverServicesBySearchString(searchString);
			printResults(services);

			printHeader("Research Center Name [" + center + "]");
			services = client.discoverServicesByResearchCenter(center);
			printResults(services);

			PointOfContact poc = new PointOfContact();
			poc.setFirstName("Scott");
			poc.setLastName("Oster");
			printHeader("POC [" + poc + "]");
			services = client.discoverServicesByPointOfContact(poc);
			printResults(services);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private static void printHeader(String title) {
		System.out.println("==================================================");
		System.out.println("Querying by " + title);
		System.out.println("==================================================");
	}


	private static void printResults(EndpointReferenceType[] types) {
		if (types != null) {
			for (int i = 0; i < types.length; i++) {
				System.out.println("\t" + i + ")  " + types[i].toString().trim());
			}
		} else {
			System.out.println("no results.");
		}
		System.out.println("--------------------------------------------------\n\n");
	}
}
