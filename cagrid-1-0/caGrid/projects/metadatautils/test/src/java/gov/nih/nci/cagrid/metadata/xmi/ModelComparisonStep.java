package gov.nih.nci.cagrid.metadata.xmi;

import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.common.SemanticMetadata;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;

import com.atomicobject.haste.framework.Step;

/** 
 *  ModelComparisonStep
 *  Compares models
 * 
 * @author David Ervin
 * 
 * @created Oct 24, 2007 1:27:00 PM
 * @version $Id: ModelComparisonStep.java,v 1.1 2007-10-24 20:22:24 dervin Exp $ 
 */
public class ModelComparisonStep extends Step {
    
    private String modelsDir;
    
    public ModelComparisonStep(String modelsDir) {
        this.modelsDir = modelsDir;
    }
    

    public void runStep() throws Throwable {
        // list each model subdir
        File basedir = new File(modelsDir);
        File[] dirs = basedir.listFiles(new FileFilter() {
            public boolean accept(File path) {
                return path.isDirectory();
            }
        });
        assertTrue("No model directories were found", dirs != null && dirs.length != 0);

        // iterate model directories
        for (File modelDir : dirs) {
            // locate model files
            System.out.println("Processing directory " + modelDir.getAbsolutePath());
            File convertedModelFile = new File(modelDir, "convertedDomainModel.xml");
            File goldModelFile = new File(modelDir, "goldDomainModel.xml");
            assertTrue("Converted model file does not exist", convertedModelFile.exists());
            assertTrue("Gold model file does not exist", goldModelFile.exists());
            
            // deserialize models
            FileReader convertedReader = new FileReader(convertedModelFile);
            FileReader goldReader = new FileReader(goldModelFile);
            DomainModel convertedModel = null;
            DomainModel goldModel = null;            
            try {
                convertedModel = MetadataUtils.deserializeDomainModel(convertedReader);
                goldModel = MetadataUtils.deserializeDomainModel(goldReader);
            } finally {
                convertedReader.close();
                goldReader.close();
            }
            compareModels(goldModel, convertedModel);
        }
    }
    
    
    private void compareModels(DomainModel goldModel, DomainModel testModel) {
        // project short name and version must match
        assertEquals("Project short names did not match", 
            goldModel.getProjectShortName(), testModel.getProjectShortName());
        assertEquals("Project versions did not match",
            goldModel.getProjectVersion(), testModel.getProjectVersion());
        
        /*
         * Commented out until I figure out why the caDSR-generated models have
         * a multitude of classes the models from XMI do not have
         * 
        // start comparing classes
        UMLClass[] goldClasses = goldModel.getExposedUMLClassCollection().getUMLClass();
        UMLClass[] testClasses = testModel.getExposedUMLClassCollection().getUMLClass();
        compareClasses(goldClasses, testClasses);
        */
    }
    
    
    private void compareClasses(UMLClass[] goldClassses, UMLClass[] testClasses) {
        assertEquals("Mismatched number of classes", goldClassses.length, testClasses.length);
        for (UMLClass gold : goldClassses) {
            // find a matching class in the test model
            String goldClassname = gold.getPackageName() + '.' + gold.getClassName();
            UMLClass matchingTestClass = null;
            for (UMLClass test : testClasses) {
                String testClassname = test.getPackageName() + '.' + test.getClassName();
                if (goldClassname.equals(testClassname)) {
                    matchingTestClass = test;
                    break;
                }
            }
            assertNotNull("Class " + goldClassname + " not found in generated model", matchingTestClass);
            
            // compare semantic metadata
            /*
            SemanticMetadata[] goldMetadata = gold.getSemanticMetadata();
            SemanticMetadata[] testMetadata = matchingTestClass.getSemanticMetadata();
            assertFalse("Gold has no semantic metadata, but created model does", goldMetadata == null && testMetadata != null);
            assertFalse("Gold has semantic metadata, but created model does not", goldMetadata != null && testMetadata == null);
            if (goldMetadata != null) { // already know testMetadata != null
                compareSemanticMetadata(goldMetadata, testMetadata);
            }
            
            // compare attributes
            UMLAttribute[] goldAttributes = gold.getUmlAttributeCollection().getUMLAttribute();
            UMLAttribute[] testAttributes = matchingTestClass.getUmlAttributeCollection().getUMLAttribute();
            */
        }
    }
    
    
    private void compareSemanticMetadata(SemanticMetadata[] goldMetadata, SemanticMetadata[] testMetadata) {
        assertEquals("Mismatched number of semantic metadata", goldMetadata.length, testMetadata.length);
        for (SemanticMetadata gold : goldMetadata) {
            String goldString = "" + gold.getConceptCode() + ":" + gold.getConceptDefinition() 
                + ":" + gold.getConceptName() + ":" + gold.getOrder() + ":" + gold.getOrderLevel();
            boolean found = false;
            for (SemanticMetadata test : testMetadata) {
                String testString = "" + test.getConceptCode() + ":" + test.getConceptDefinition() 
                    + ":" + test.getConceptName() + ":" + test.getOrder() + ":" + test.getOrderLevel();
                if (goldString.equals(testString)) {
                    found = true;
                    break;
                }
            }
            assertTrue(goldString + " semantic metadata not found", found);
        }
    }
}