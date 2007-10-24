package gov.nih.nci.cagrid.metadata.xmi;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;

import com.atomicobject.haste.framework.Step;

/** 
 *  ModelConversionStep
 *  Converts XMI to a domain model
 * 
 * @author David Ervin
 * 
 * @created Oct 24, 2007 12:05:19 PM
 * @version $Id: ModelConversionStep.java,v 1.1 2007-10-24 16:43:23 dervin Exp $ 
 */
public class ModelConversionStep extends Step {
    
    private String modelsDir;
    
    public ModelConversionStep(String modelsDir) {
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
            System.out.println("Processing directory " + modelDir.getAbsolutePath());
            // locate the XMI
            File[] xmis = modelDir.listFiles(new FileFilter() {
                public boolean accept(File path) {
                    return path.getName().toLowerCase().endsWith(".xmi");
                }
            });
            assertTrue("No XMI was found in " + modelDir.getAbsolutePath(), xmis.length != 0);
            assertTrue("Multiple XMIs were found in " + modelDir.getAbsolutePath(), xmis.length == 1);
            // locate the model info file
            File infoFile = new File(modelDir, "info.txt");
            assertTrue("No info.txt file found in " + modelDir.getAbsolutePath(), infoFile.exists());
            String[] infoLines = Utils.fileToStringBuffer(infoFile).toString().split("\n");
            assertTrue("Not enough lines in info file", infoLines.length > 2);
            String projectShortName = infoLines[0];
            String projectVersion = infoLines[1];
            // convert the model
            XMIParser parser = new XMIParser(projectShortName, projectVersion);
            DomainModel model = parser.parse(xmis[0]);
            assertNotNull("Converted model was null!", model);
            // store the model to disk -- will be compared to gold later
            File modelFile = new File(modelDir, "convertedDomainModel.xml");
            FileWriter writer = new FileWriter(modelFile);
            MetadataUtils.serializeDomainModel(model, writer);
            writer.flush();
            writer.close();
        }
    }
}
