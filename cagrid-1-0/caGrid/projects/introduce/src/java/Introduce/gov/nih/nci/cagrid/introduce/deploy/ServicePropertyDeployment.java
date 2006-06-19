package gov.nih.nci.cagrid.introduce.deploy;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.jdom.Document;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.XMLUtilities;

public class ServicePropertyDeployment extends Task {

	public static final String SERVICE_PROPERTY_KEYS = "SERVICE_PROPERTY_KEYS";
	public static final String BASEDIR_PROP = "basedir";
	public static final String JNDI_FILENAME = "jndi-config.xml" ;
	
	public void execute() throws BuildException {
		super.execute();

		Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());

		Properties properties = new Properties();
		properties.putAll(this.getProject().getProperties());

		File baseDirectory = new File(properties.getProperty(BASEDIR_PROP));

		String servicePropertyKeysString = properties.getProperty(SERVICE_PROPERTY_KEYS);
		StringTokenizer strtok = new StringTokenizer(servicePropertyKeysString,",",false);
		List servicePropertyKeys = new  ArrayList();
		while (strtok.hasMoreElements()){
			String key = strtok.nextToken();
			servicePropertyKeys.add(key);
		}
		
		Document doc = null;
		try {
			doc = XMLUtilities.fileNameToDocument(baseDirectory + File.separator + JNDI_FILENAME);
		} catch (MobiusException e) {
			throw new BuildException(e.getMessage(),e);
		}
		
		//add the properties to the JNDI now....
		
		
		try {
			FileWriter writer = new FileWriter(baseDirectory + File.separator + JNDI_FILENAME);
			writer.write(XMLUtilities.formatXML(XMLUtilities.documentToString(doc)));
		} catch (Exception e) {
			throw new BuildException(e.getMessage(),e);
		}

		
	}
}
