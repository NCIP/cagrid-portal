/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.projectmobius.castor.xml.schema.reader.SchemaReader;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.Namespace;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.xml.sax.InputSource;


/**
 * This step gets a list of schemas from a running GME grid service and then
 * checks that a schema file is represented in the list.
 * 
 * @author Patrick McConnell
 */
public class GMEGetSchemaListStep extends Step {
    private EndpointReferenceType endpoint;
    private File schemaFile;


    public GMEGetSchemaListStep(EndpointReferenceType endpoint, File schemaFile) {
        super();

        this.endpoint = endpoint;
        this.schemaFile = schemaFile;
    }


    @Override
    public void runStep() throws Throwable {
        System.out.println("Running GetSchemaListStep on " + this.schemaFile);

        // bind to service
        GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
        XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance().getGridService(
            this.endpoint.getAddress().toString());

        // read schema
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(this.schemaFile));
        SchemaReader sr = new SchemaReader(new InputSource(is));
        sr.setValidation(false);
        org.projectmobius.castor.xml.schema.Schema schema = sr.read();
        is.close();
        Namespace schemaTargetNamespace = new Namespace(schema.getTargetNamespace());

        // list
        boolean found = false;
        List namespaces = handle.getSchemaListForNamespaceDomain(schemaTargetNamespace.getDomain());
        for (Object namespace : namespaces) {
            if (schemaTargetNamespace.getDomain().equals(((Namespace) namespace).getDomain())
                && schemaTargetNamespace.getName().equals(((Namespace) namespace).getName())) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
}
