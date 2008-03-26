package gov.nih.nci.cagrid.validator.steps.gme;

import gov.nih.nci.cagrid.common.XMLUtilities;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.castor.xml.schema.Schema;
import org.projectmobius.castor.xml.schema.reader.SchemaReader;
import org.projectmobius.client.gme.ImportInfo;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.gme.SchemaParsingException;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.protocol.gme.SchemaNode;
import org.xml.sax.InputSource;

/** 
 *  SchemaDownloadStep
 *  Step to download schemas from the GME
 *  <b>NOTE</b> This step should run AFTER the Domains and Namespace step
 * 
 * @author David Ervin
 * 
 * @created Aug 27, 2007 4:37:36 PM
 * @version $Id: SchemaDownloadStep.java,v 1.3 2008-03-26 14:32:45 dervin Exp $ 
 */
public class SchemaDownloadStep extends BaseGmeTestStep {
    
    public SchemaDownloadStep(String serviceUrl, File tempDir, Properties configuration) {
        super(serviceUrl, tempDir, configuration);
    }


    public void runStep() throws Throwable {
        List<String> domains = getNamespaceDomains();
        for (String domain : domains) {
            System.out.println("Inspecting domain " + domain);
            List<Namespace> namespaces = getSchemasFromDomain(domain);
            for (Namespace namespace : namespaces) {
                System.out.println("\tInspecting namespace " + namespace.getRaw());
                File tempSchemaDir = new File(getTempDir(), "Schemas_" + System.currentTimeMillis());
                tempSchemaDir.mkdirs();
                List<Namespace> cachedSchemas = cacheSchemasToDirectory(namespace, tempSchemaDir);
                // verify each schema was cached correctly
                for (Namespace cached : cachedSchemas) {
                    System.out.println("\t\tVerifying namespace of schema " + cached.getRaw());
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
            GmeClientHelper helper = new GmeClientHelper(getGmeHandle());
            cached = helper.cacheSchema(baseSchema, tempDir);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error caching schemas: " + ex.getMessage());
        } 
        return cached;
    }
    
    
    /**
     * This inner class duplicates some functionality of the Mobius GME client class
     * so as not to simply print exceptions and move on without rethrowing them
     */
    private class GmeClientHelper {
        
        private XMLDataModelService gmeClient = null;
        
        public GmeClientHelper(XMLDataModelService client) {
            this.gmeClient = client;
        }
        
        
        /* CODE FROM org.projectmobius.client.gme.XMLDataModelServiceClient */
        public List cacheSchema(Namespace namespace, File directory) throws MobiusException {
            directory.mkdirs();
            
            // list holds the namespaces cached
            List namespaces = new ArrayList();
            
            // get the base namespace and its imports
            System.out.println("\t\tTrying to download " + namespace.getRaw());
            SchemaNode node = gmeClient.getSchema(namespace, true);
            
            // recursively parse the schema and its imports
            this.processSchemaNode(node, directory.getAbsolutePath(), namespaces);

            return namespaces;
        }
        
        
        /* CODE FROM org.projectmobius.client.gme.XMLDataModelServiceClient */
        private ImportInfo processSchemaNode(SchemaNode node, String directory, List writtenNamespaces) throws MobiusException {
            List fileImports = new ArrayList();
            List referencedSchemas = node.getReferenced();
            // recursively parse imports
            for (int i = 0; i < referencedSchemas.size(); i++) {
                fileImports.add(processSchemaNode(
                    (SchemaNode) referencedSchemas.get(i), directory, writtenNamespaces));
            }

            // read the schema itself
            String schemaString = node.getSchemaContents();
            Schema schema = null;
            ByteArrayInputStream bis = new ByteArrayInputStream(schemaString.getBytes());
            InputSource is = new InputSource(bis);
            try {
                SchemaReader sr = new SchemaReader(is);
                sr.setValidation(false);
                schema = sr.read();
            } catch (IOException ex) {
                throw new SchemaParsingException("Error parsing schema: " + ex.getMessage(), ex);
            }
            
            // create the namespace and local file name for the schema
            Namespace thisnamespace = new Namespace(schema.getTargetNamespace());
            ImportInfo ii = new ImportInfo(thisnamespace);
            String fileName = directory + File.separator + ii.getFileName();            

            // convert the schema to a JDom element for further processing
            Document schemaDoc = null;
            try {
                schemaDoc = XMLUtilities.stringToDocument(schemaString);
            } catch (Exception ex) {
                throw new SchemaParsingException("Error converting schema to JDOM object: " + ex.getMessage(), ex);
            }
            Element root = schemaDoc.getRootElement();
            //remove the imports if the schema text was submitted with some.....
            root.removeChildren("import", root.getNamespace());
            //add the new imports based on fetching them from GME's
            for (int i = 0; i < fileImports.size(); i++) {
                Element im = new Element("import", root.getNamespace());
                ImportInfo fim = (ImportInfo) fileImports.get(i);
                im.setAttribute("namespace", fim.getNamespace().getRaw());
                im.setAttribute("schemaLocation", fim.getFileName());
                root.addContent(0, im);
            }

            // write the edited schema back out
            try {
                FileWriter fw = new FileWriter(new File(fileName));
                fw.write(XMLUtilities.formatXML(XMLUtilities.documentToString(schemaDoc)));
                fw.flush();
                fw.close();
            } catch (Exception ex) {
                throw new SchemaParsingException("Error writing schema to disk: " + ex.getMessage(), ex);
            }
            
            // include the base schema's target namespace
            Namespace ns = new Namespace(schema.getTargetNamespace());
            writtenNamespaces.add(ns);
            
            return new ImportInfo(ns);
        }
    }
}
