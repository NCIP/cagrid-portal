/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.projectmobius.castor.xml.schema.reader.SchemaReader;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.Namespace;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.xml.sax.InputSource;

import com.atomicobject.haste.framework.Step;

public class GMEGetSchemaListStep
	extends Step
{
	private EndpointReferenceType endpoint;
	private File schemaFile;
	
	public GMEGetSchemaListStep(int port, File schemaFile) 
		throws MalformedURIException
	{
		this(new EndpointReferenceType(new Address("http://localhost:" + port + "/wsrf/services/cagrid/GlobalModelExchange")), schemaFile);
	}

	public GMEGetSchemaListStep(EndpointReferenceType endpoint, File schemaFile)
	{
		super();
		
		this.endpoint = endpoint;
		this.schemaFile = schemaFile;
	}
	
	public void runStep() throws Throwable
	{
		System.out.println("Running GetSchemaListStep on " + schemaFile);
		
		// bind to service
		GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
		XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance().getGridService(endpoint.getAddress().toString());

		// read schema
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(schemaFile));
		SchemaReader sr = new SchemaReader(new InputSource(is));
		sr.setValidation(false);
		org.projectmobius.castor.xml.schema.Schema schema = sr.read();
		is.close();
		Namespace schemaTargetNamespace = new Namespace(schema.getTargetNamespace());

		// list
		boolean found = false;
		List namespaces = handle.getSchemaListForNamespaceDomain(schemaTargetNamespace.getDomain());
		for (Object namespace : namespaces) {
			if (schemaTargetNamespace.getDomain().equals(((Namespace) namespace).getDomain()) &&
				schemaTargetNamespace.getName().equals(((Namespace) namespace).getName())
			) {
				found = true;
				break;
			}
		}
		assertTrue(found);
	}
}
