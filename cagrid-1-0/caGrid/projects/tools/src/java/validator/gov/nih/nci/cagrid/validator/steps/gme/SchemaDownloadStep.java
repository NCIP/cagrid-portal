package gov.nih.nci.cagrid.validator.steps.gme;

import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.projectmobius.client.gme.ImportInfo;
import org.projectmobius.common.MobiusArgumentException;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.gme.SchemaReferenceException;

/** 
 *  SchemaDownloadStep
 *  Step to download schemas from the GME
 *  <b>NOTE</b> This step should run AFTER the Domains and Namespace step
 * 
 * @author David Ervin
 * 
 * @created Aug 27, 2007 4:37:36 PM
 * @version $Id: SchemaDownloadStep.java,v 1.1 2008-03-25 14:20:30 dervin Exp $ 
 */
public class SchemaDownloadStep extends BaseGmeTestStep {
    
    public SchemaDownloadStep(String serviceUrl, File tempDir, Properties configuration) {
        super(serviceUrl, tempDir, configuration);
    }


    public void runStep() throws Throwable {
        List<String> domains = getNamespaceDomains();
        for (String domain : domains) {
            List<Namespace> namespaces = getSchemasFromDomain(domain);
            for (Namespace namespace : namespaces) {
                File tempSchemaDir = new File(getTempDir(), "Schemas_" + System.currentTimeMillis());
                tempSchemaDir.mkdirs();
                List<Namespace> cachedSchemas = cacheSchemasToDirectory(namespace, tempSchemaDir);
                // verify each schema was cached correctly
                for (Namespace cached : cachedSchemas) {
                    ImportInfo ii = new ImportInfo(cached);
                    File schemaFile = new File(tempSchemaDir.getAbsolutePath() 
                        + File.separator + ii.getFileName());
                    assertTrue("Could not find schema file " + schemaFile.getName(), 
                        schemaFile.exists());
                    String targetNamespace = CommonTools.getTargetNamespace(schemaFile);
                    assertNotNull("Target namespace of schema was null", targetNamespace);
                    assertEquals(cached.getRaw(), targetNamespace);
                }
            }
        }
    }
    
    
    private List<String> getNamespaceDomains() {
        List<String> domains = null;
        try {
            domains = getGmeHandle().getNamespaceDomainList();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error listing namespace domains from GME: " + ex.getMessage());
        }
        return domains;
    }
    
    
    private List<Namespace> getSchemasFromDomain(String ns) {
        List<Namespace> namespaces = null;
        try {
            namespaces = getGmeHandle().getSchemaListForNamespaceDomain(ns);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error listing schemas in namespace: " + ex.getMessage());
        }
        return namespaces;
    }
    
    
    private List<Namespace> cacheSchemasToDirectory(Namespace baseSchema, File tempDir) {
        List<Namespace> cached = null;
        try {
            cached = getGmeHandle().cacheSchema(baseSchema, tempDir);
        } catch (SchemaReferenceException ex) {
            System.out.println("CAUGHT SCHEMA REFERENCE EXCEPTION");
            System.out.println("CAUGHT SCHEMA REFERENCE EXCEPTION");
            System.out.println("CAUGHT SCHEMA REFERENCE EXCEPTION");
            System.out.println("CAUGHT SCHEMA REFERENCE EXCEPTION");
            System.out.println("CAUGHT SCHEMA REFERENCE EXCEPTION");
        } catch (MobiusException ex) {
            System.out.println("CAUGHT MOBIUS EXCEPTION");
            System.out.println("CAUGHT MOBIUS EXCEPTION");
            System.out.println("CAUGHT MOBIUS EXCEPTION");
            System.out.println("CAUGHT MOBIUS EXCEPTION");
            System.out.println("CAUGHT MOBIUS EXCEPTION");
        } catch (Exception ex) {
            System.out.println("CAUGHT EXCEPTION");
            System.out.println("CAUGHT EXCEPTION");
            System.out.println("CAUGHT EXCEPTION");
            System.out.println("CAUGHT EXCEPTION");
            System.out.println("CAUGHT EXCEPTION");
            // ex.printStackTrace();
            fail("Error caching schemas: " + ex.getMessage());
        } 
        return cached;
    }
}
