/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.util.FileUtils;
import gov.nci.nih.cagrid.tests.core.util.SchemaInfo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.projectmobius.castor.xml.schema.reader.SchemaReader;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.gme.NamespaceExistsException;
import org.projectmobius.common.gme.NoSuchNamespaceException;
import org.projectmobius.common.gme.NotSubNamespaceException;
import org.projectmobius.common.gme.NotifyAuthorityException;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.atomicobject.haste.framework.Step;

/**
 * This step publishes a schema and any of its imports to a running GME grid service.
 * @author Patrick McConnell
 */
public class GMEPublishSchemaStep
	extends Step
{
	private EndpointReferenceType endpoint;
	private File schemaFile;
	
	public GMEPublishSchemaStep(int port, File schemaFile) 
		throws MalformedURIException
	{
		this(new EndpointReferenceType(new Address("http://localhost:" + port + "/wsrf/services/cagrid/GlobalModelExchange")), schemaFile);
	}

	public GMEPublishSchemaStep(EndpointReferenceType endpoint, File schemaFile)
	{
		super();
		
		this.endpoint = endpoint;
		this.schemaFile = schemaFile;
	}
	
	public void runStep() 
		throws Throwable
	{
		GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
		XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance().getGridService(endpoint.getAddress().toString());
		publishSchema(handle, schemaFile);
	}
	
	private void publishSchema(XMLDataModelService handle, File schemaFile) 
		throws IOException, ParserConfigurationException, SAXException, NotSubNamespaceException, NotifyAuthorityException, MobiusException
	{
		// read schema
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(schemaFile));
		SchemaReader sr = new SchemaReader(new InputSource(is));
		sr.setValidation(false);
		org.projectmobius.castor.xml.schema.Schema schema = sr.read();
		is.close();
		Namespace schemaTargetNamespace = new Namespace(schema.getTargetNamespace());

		// check for schema
		boolean found = false;
		List namespaces = null;
		try {
			namespaces = handle.getSchemaListForNamespaceDomain(schemaTargetNamespace.getDomain());
		} catch (NoSuchNamespaceException ex) {
			namespaces = new ArrayList(0);
		}
		for (int i = 0; i < namespaces.size(); i++) {
			Namespace namespace = (Namespace) namespaces.get(i);
			
			if (namespace.getDomain().equals(schemaTargetNamespace.getDomain()) &&
				namespace.getName().equals(schemaTargetNamespace.getName())
			) {
				found = true;
				break;
			}
		}
		if (found) return;

		// add imported schemas first
		SchemaInfo info = new SchemaInfo(schemaFile);
		Hashtable<String, String> importTable = info.getImports();
		for (String namespace : importTable.keySet()) {
			String location = importTable.get(namespace);
			File importFile = new File(schemaFile.getParent(), location); 
			publishSchema(handle, importFile);
		}
		
		// add namespace
		try {
			handle.addNamespaceDomain(schemaTargetNamespace.getDomain());
		} catch (NamespaceExistsException ex) {
			// should be ok here do nothing.........
			//ex.printStackTrace();
		}
		
		// add schema
		handle.publishSchema(FileUtils.readText(schemaFile));
	}
	
	public static void main(String[] args)
		throws Throwable
	{
		new GMEPublishSchemaStep(
			8080, 
			new File("test", "resources" + File.separator + "GMEServiceTest" + File.separator + "schema" + File.separator + "1.0_gov.nih.nci.cagrid.metadata.common.xsd")//"3.0_gov.nih.nci.cadsr.domain.xsd")
		).runStep(); 
	}
}
