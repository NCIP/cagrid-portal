/*
 * Created on Jun 12, 2006
 */
package gov.nci.nih.cagrid.tests.core.util;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.cadsr.common.CaDSRServiceI;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.namespace.QName;

import org.globus.wsrf.encoding.DeserializationException;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.xml.sax.InputSource;

public class CaDSRExtractUtils
{
	public static DomainModel findExtract(CaDSRServiceI cadsr, String projectName) 
		throws Exception
	{
		Project[] projects = cadsr.findAllProjects();
		Project project = null;
		for (Project myProject : projects) {
			if (myProject.shortName.equals(projectName)) {
				project = myProject;
				break;
			}
		}
		if (project == null) {
			throw new IllegalArgumentException("could not find project " + projectName);
		}
		
		return cadsr.generateDomainModelForProject(project);
	}
	
	public static DomainModel findExtract(String url, String projectName) 
		throws Exception
	{
		CaDSRServiceI cadsr = new CaDSRServiceClient(url);
		return findExtract(cadsr, projectName);
	}
	
	public static void writeExtract(DomainModel extract, File file)
		throws Exception
	{
		//BufferedWriter out = new BufferedWriter(new FileWriter(file));
		//ObjectSerializer.serialize(out, extract, new QName("extract"));
		//out.flush();
		//out.close();
		
		Utils.serializeDocument(file.toString(), extract, new QName("extract"));
		//gov.nih.nci.cagrid.encoding.SDKSerializerFactory.createFactory(null, null, null).
	}
	
	public static DomainModel readExtract(File file)
		throws IOException, DeserializationException
	{
		BufferedReader in = new BufferedReader(new FileReader(file));
		DomainModel extract = (DomainModel) ObjectDeserializer.deserialize(new InputSource(in), DomainModel.class);
		in.close();
		
		return extract;
	}
	
	public static void main(String[] args) 
		throws Exception
	{
		DomainModel model = findExtract(args[0], args[1]);
		writeExtract(model, new File(args[2]));
		readExtract(new File(args[2]));
	}
}
