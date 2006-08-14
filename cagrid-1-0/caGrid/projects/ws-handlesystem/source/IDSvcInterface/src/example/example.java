package example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import net.handle.server.*;

/**
 * An example application that uses the {@link IDSvcInterface} class to
 * register, and retrieve, three resources.
 *
 * <p>Resource 1 shows how complicated data can be stored using this
 * system.  A lookup table of 3 items is created and encoded in base 64.
 * (This would allow arbitrary binary data to be stored.)</p>
 *
 * <p>Resource 2 stores a simpler string value in the same context.
 * Notice that the URI created for this resource is under the same URI
 * authority as resource 1, thus allowing the same administration info
 * to be used for both resources in case of data migration.</p>
 *
 * <p>Resource 3 stores the same string value in a different context.
 * While the URI authority is different, the URI path is the same as
 * resource 2.  This may not hold in general.</p>
 */
public class example {

/**
 * Sole constructor.  Since all methods are abstract, there is
 * no need to instantiate an {@link example} object.
 */
private example() { }

/**
 * The example application code.  No parameters are used.
 */
public static void main(String args[]) throws Exception {
	String configPath;
	if (args.length == 1) {
		configPath = args[0];
	} else {
		System.out.print("Enter full path to handle server directory:  ");
		configPath = (new BufferedReader(new InputStreamReader(System.in))).readLine();
	}
	HandleRepositoryIDInterface idSvc = new HandleRepositoryIDInterface(configPath);
	//IDSvcInterface idSvc = new net.handle.server.HandleServerIDInterface(configPath);
	//IDSvcInterface idSvc = IDSvcInterfaceFactory.getInterface(configPath);
	Thread.sleep(100);	// not necessary, but let's get the output from the other threads all flushed first

	// example 1:  create an identifier containing key:value pairs
	System.out.println("***example 1***");
	ResourceIdInfo test = new ResourceIdInfo(
		new URI("hdl://123/context=Hutch-Center"),
		Base32.encode("name".getBytes()) + ":" + Base32.encode("sam".getBytes()) + ";" +
			Base32.encode("org".getBytes()) + ":" + Base32.encode("cnri".getBytes()) + ";" +
			Base32.encode("phone".getBytes()) + ":" + Base32.encode("555-1212".getBytes()));
	URI bigid = idSvc.createOrGetGlobalID(test);
	System.out.println("bigid = " + bigid);
	if (bigid != null) {
		ResourceIdInfo info = idSvc.getBigIDInfo(bigid);
		System.out.println("info = " + info);
		// decode the string (base64:base64;...)
		// this is specific to example 1
		String[] maps = info.resourceIdentification.split(";");
		for (int i=0; i<maps.length; i++) {
			//System.out.println("map " + i + ":  " + maps[i]);
			String[] values = maps[i].split(":");
			System.out.println("\t" + new String(Base32.decode(values[0])) + ":" + new String(Base32.decode(values[1])));
		}
	}

	// example 2:  create an identifier in the same context
	System.out.println("\n***example 2***");
	test = new ResourceIdInfo(
		new URI("hdl://123/context=Hutch-Center"),
		"this is a different identifier containing just a string");
	bigid = idSvc.createOrGetGlobalID(test);
	System.out.println("bigid = " + bigid);
	if (bigid != null) {
		ResourceIdInfo info = idSvc.getBigIDInfo(bigid);
		System.out.println("info = " + info);
	}

	// example 3:  create an identifier in a different context
	System.out.println("\n***example 3***");
	test = new ResourceIdInfo(
		new URI("urn:different.context"),
		"this is a different identifier containing just a string");
	bigid = idSvc.createOrGetGlobalID(test);
	System.out.println("bigid = " + bigid);
	if (bigid != null) {
		ResourceIdInfo info = idSvc.getBigIDInfo(bigid);
		System.out.println("info = " + info);
	}

	System.out.println("\n***attempting to fill up hash buckets and testing manual commit***");
	int numIDs = 100;
	long startTime = System.currentTimeMillis();
	idSvc.beginTransaction();
	for (int i=0; i<numIDs; i++) {
		ResourceIdInfo test1 = new ResourceIdInfo(
			new URI("urn:context" + i),
			"the identifier");
		URI bigid1 = idSvc.createOrGetGlobalID(test1);
	}
	idSvc.commitTransaction();
	long endTime = System.currentTimeMillis();
	System.out.println("elapsed time:  " + (endTime-startTime)/1000.0 + " seconds");
	System.out.println(idSvc.numCollisions + " collisions, " + numIDs + " IDs, hash length " + idSvc.minHashLength);

	System.out.println("\n***testing removing by ResourceIdInfo***");
	ResourceIdInfo test2 = new ResourceIdInfo(
		new URI("hdl://123/context=Hutch-Center"),
		Base32.encode("name".getBytes()) + ":" + Base32.encode("sam".getBytes()) + ";" +
			Base32.encode("org".getBytes()) + ":" + Base32.encode("cnri".getBytes()) + ";" +
			Base32.encode("phone".getBytes()) + ":" + Base32.encode("555-1212".getBytes()));
	idSvc.removeGlobalID(test2);
	
	System.out.println("\n***testing removing by (Global ID) URI***");
	idSvc.removeGlobalID(bigid);

	System.out.println("\n***testing removing by context***");
	idSvc.removeContext(new URI("hdl://123/context=Hutch-Center"));

	System.out.println("Ctrl-C to finish...");
	while (true) { Thread.sleep(1000); }
}

}

