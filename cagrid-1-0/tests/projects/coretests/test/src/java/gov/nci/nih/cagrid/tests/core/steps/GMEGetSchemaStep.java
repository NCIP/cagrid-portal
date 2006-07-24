/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.compare.XmlComparator;
import gov.nci.nih.cagrid.tests.core.util.FileUtils;
import gov.nci.nih.cagrid.tests.core.util.SchemaInfo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.projectmobius.castor.xml.schema.reader.SchemaReader;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.atomicobject.haste.framework.Step;

public class GMEGetSchemaStep
	extends Step
{
	private EndpointReferenceType endpoint;
	private File schemaFile;
	
	public GMEGetSchemaStep(int port, File schemaFile) 
		throws MalformedURIException
	{
		this(new EndpointReferenceType(new Address("http://localhost:" + port + "/wsrf/services/cagrid/GlobalModelExchange")), schemaFile);
	}

	public GMEGetSchemaStep(EndpointReferenceType endpoint, File schemaFile)
	{
		super();
		
		this.endpoint = endpoint;
		this.schemaFile = schemaFile;
	}
	
	public void runStep() throws Throwable
	{
		// bind to service
		GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
		XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance().getGridService(endpoint.getAddress().toString());
		
		File tmpDir = FileUtils.createTempDir("GetSchemaStep", "dir");
		try {
			// download schema
			downloadSchema(handle, tmpDir, schemaFile);

			// compare schemas
			assertTrue(new XmlComparator().isEqual(new File[] { 
				schemaFile, new File(tmpDir, schemaFile.getName()) 
			}));
		} finally {
			// cleanup
			FileUtils.deleteRecursive(tmpDir);
		}
	}
	
	private void downloadSchema(XMLDataModelService handle, File dir, File schemaFile) 
		throws IOException, MobiusException, ParserConfigurationException, SAXException
	{
		// read schema
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(schemaFile));
		SchemaReader sr = new SchemaReader(new InputSource(is));
		sr.setValidation(false);
		org.projectmobius.castor.xml.schema.Schema schema = sr.read();
		is.close();
		Namespace schemaTargetNamespace = new Namespace(schema.getTargetNamespace());
		
		// download schema
		handle.cacheSchema(schemaTargetNamespace, dir);
		
		// download imports
		SchemaInfo info = new SchemaInfo(new File(dir, schemaFile.getName()));
		Hashtable<String, String> importTable = info.getImports();
		for (String namespace : importTable.keySet()) {
			String location = importTable.get(namespace);
			File importFile = new File(schemaFile.getParent(), location); 
			downloadSchema(handle, dir, importFile);
		}
	}
	
	public static void main(String[] args)
		throws Throwable
	{
		new GMEGetSchemaStep(
			8080, 
			new File("test", "resources" + File.separator + "GMEServiceTest" + File.separator + "schema" + File.separator + "1.0_gov.nih.nci.cagrid.metadata.common.xsd")//"3.0_gov.nih.nci.cadsr.domain.xsd")
		).runStep(); 
	}

}
