/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.compare.XmlComparator;
import gov.nci.nih.cagrid.tests.core.util.FileUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.projectmobius.castor.xml.schema.reader.SchemaReader;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.atomicobject.haste.framework.Step;


/**
 * This step gets a schemas from a running GME grid service and then compares it
 * to a local schema to determine that the schemas are the same.
 * 
 * @author Patrick McConnell
 */
public class GMEGetSchemaStep extends Step {
    private EndpointReferenceType endpoint;
    private File schemaFile;


    public GMEGetSchemaStep(EndpointReferenceType endpoint, File schemaFile) {
        super();

        this.endpoint = endpoint;
        this.schemaFile = schemaFile;
    }


    @Override
    public void runStep() throws Throwable {
        // bind to service
        GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
        XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance().getGridService(
            this.endpoint.getAddress().toString());

        File tmpDir = FileUtils.createTempDir("GetSchemaStep", "dir");
        try {
            // download schema
            downloadSchema(handle, tmpDir, this.schemaFile);

            // compare schemas
            assertTrue(new XmlComparator().isEqual(new File[]{this.schemaFile,
                    new File(tmpDir, this.schemaFile.getName())}));
        } finally {
            // cleanup
            FileUtils.deleteRecursive(tmpDir);
        }
    }


    private void downloadSchema(XMLDataModelService handle, File dir, File schemaFile) throws IOException,
        MobiusException, ParserConfigurationException, SAXException {
        // read schema
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(schemaFile));
        SchemaReader sr = new SchemaReader(new InputSource(is));
        sr.setValidation(false);
        org.projectmobius.castor.xml.schema.Schema schema = sr.read();
        is.close();
        Namespace schemaTargetNamespace = new Namespace(schema.getTargetNamespace());

        // download schema
        handle.cacheSchema(schemaTargetNamespace, dir);

    }

}
