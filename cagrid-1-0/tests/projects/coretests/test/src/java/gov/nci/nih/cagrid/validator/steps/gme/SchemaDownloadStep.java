package gov.nci.nih.cagrid.validator.steps.gme;

import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;
import java.util.List;

import org.projectmobius.client.gme.ImportInfo;
import org.projectmobius.common.Namespace;
import org.projectmobius.gme.XMLDataModelService;

/** 
 *  SchemaDownloadStep
 *  Step to download schemas from the GME
 *  <b>NOTE</b> This step should run AFTER the Domains and Namespace step
 * 
 * @author David Ervin
 * 
 * @created Aug 27, 2007 4:37:36 PM
 * @version $Id: SchemaDownloadStep.java,v 1.1 2007-08-28 14:03:14 dervin Exp $ 
 */
public class SchemaDownloadStep extends BaseGmeTestStep {
    
    private File tempDir;

    public SchemaDownloadStep(String serviceUrl, File tempDir) {
        super(serviceUrl);
        this.tempDir = tempDir;
    }


    public void runStep() throws Throwable {
        XMLDataModelService gmeHandle = getGmeHandle();
        
        List<String> domains = gmeHandle.getNamespaceDomainList();
        for (String domain : domains) {
            List<String> namespaces = gmeHandle.getSchemaListForNamespaceDomain(domain);
            for (String namespace : namespaces) {
                File tempSchemaDir = new File(tempDir.getAbsolutePath() + File.separator + "Schemas_" + System.currentTimeMillis());
                tempSchemaDir.mkdirs();
                Namespace schemaNamespace = new Namespace(namespace);
                List<Namespace> cachedSchemas = gmeHandle.cacheSchema(schemaNamespace, tempSchemaDir);
                // verify each schema was cached correctly
                for (Namespace cached : cachedSchemas) {
                    ImportInfo ii = new ImportInfo(cached);
                    File schemaFile = new File(tempSchemaDir.getAbsolutePath() + File.separator + ii.getFileName());
                    assertTrue("Could not find schea file " + schemaFile.getName(), schemaFile.exists());
                    String targetNamespace = CommonTools.getTargetNamespace(schemaFile);
                    assertNotNull(targetNamespace);
                    assertEquals(cached, targetNamespace);
                }
            }
        }
    }
}
