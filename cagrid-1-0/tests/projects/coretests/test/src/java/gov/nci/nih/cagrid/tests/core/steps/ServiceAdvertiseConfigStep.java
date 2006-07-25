/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.util.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Step;

/**
 * This step modifies the etc/registration.xml file to register with a specific index service
 * (likely http://localhost:8080/wsrf/services/DefaultIndexService).
 * @author Patrick McConnell
 */
public class ServiceAdvertiseConfigStep
	extends Step
{
	private File serviceDir;
	private String indexServiceURL;
	
	public ServiceAdvertiseConfigStep(int port, File serviceDir) 
		throws MalformedURIException
	{
		this("http://localhost:" + port + "/wsrf/services/DefaultIndexService", serviceDir);
	}

	public ServiceAdvertiseConfigStep(String indexServiceURL, File serviceDir)
	{
		super();
		
		this.serviceDir = serviceDir;
		this.indexServiceURL = indexServiceURL;
	}
	
	public void runStep() throws Throwable
	{
		File tmpFile = File.createTempFile("AdvertiseServiceStep", ".xml");
		tmpFile.deleteOnExit();
		File registrationFile = new File(serviceDir, "etc" + File.separator + "registration.xml");
		
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(tmpFile)));
		BufferedReader br = new BufferedReader(new FileReader(registrationFile));
		try {
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.trim().startsWith("<wsa:Address>")) {
					int index1 = line.indexOf(">");
					int index2 = line.indexOf("<", index1+1);
					line = line.substring(0, index1+1) + indexServiceURL + line.substring(index2);
				}
				out.println(line);
			}
		} finally {
			out.close();
			out.flush();
			br.close();
		}
		
		FileUtils.copy(tmpFile, registrationFile);
		tmpFile.delete();
	}
}
