package gov.nih.nci.cagrid.cadsr.common;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/** 
 *  ModelProblemsUtil
 *  Utility to discover problems in caDSR models
 * 
 * @author Satish Patel
 * @author David Ervin
 * 
 * @created Feb 8, 2008 9:49:12 AM
 * @version $Id: ModelProblemsUtil.java,v 1.3 2008-04-17 19:47:44 dervin Exp $ 
 */
public class ModelProblemsUtil {
    
    // default URL of the cadsr application service
    public static final String DEFAULT_CADSR_APPLICATION_URL = "http://cabio.nci.nih.gov/cacore31/http/remoteService";

    private static final Log LOG = LogFactory.getLog(ModelProblemsUtil.class);

    private ApplicationService cadsrService = null;


    public ModelProblemsUtil(ApplicationService cadsrService) {
        this.cadsrService = cadsrService;
    }


    public List<ModelProblem> findProblems(String modelShortName, String modelVersion) throws ModelProblemException {
        List<ModelProblem> problems = new LinkedList<ModelProblem>();

        Project searchProject = new Project();
        searchProject.setShortName(modelShortName);
        searchProject.setVersion(modelVersion);
        LOG.debug("Creating domain model for project: " + searchProject.getShortName() + " (version:"
            + searchProject.getVersion() + ")");

        Collection projectCollection = null;
        try {
            projectCollection = cadsrService.search(Project.class, searchProject);
        } catch (Exception ex) {
            throw new ModelProblemException("Error searching application service: " + ex.getMessage(), ex);
        }

        // Proceed if only one Project is found in caDSR
        Iterator projectIter = projectCollection.iterator();
        if (!projectIter.hasNext()) {
            throw new ModelProblemException("No model (" + modelShortName + ", " + modelVersion + ")");
        }
        Project project = (Project) projectIter.next();
        if (projectIter.hasNext()) {
            throw new ModelProblemException("More than one model (" + modelShortName + ", " + modelVersion + ") found");
        }
        
        LOG.debug("Got domain model for project: " + project.getShortName() + " (version:"
            + project.getVersion() + ")");

        Collection umlClassCollection = project.getUMLClassMetadataCollection();

        for (Iterator iter = umlClassCollection.iterator(); iter.hasNext();) {
            UMLClassMetadata classMetadata = (UMLClassMetadata) iter.next();
            String classDesc = classMetadata.getDescription();

            // Process description of the class
            int[] classErrs = findNonUnicodeChars(classDesc);
            if (classErrs.length != 0) {
                // errors in the class description
                UMLClassProblem problem = new UMLClassProblem(
                    classMetadata.getUMLPackageMetadata().getName(),
                    classMetadata.getName(), classDesc, classErrs);
                problems.add(problem);
            }

            // Process attributes of the class
            Collection attrMetadataCollection = classMetadata.getUMLAttributeMetadataCollection();
            for (Iterator attrIterator = attrMetadataCollection.iterator(); attrIterator.hasNext();) {
                UMLAttributeMetadata attribMetadata = (UMLAttributeMetadata) attrIterator.next();
                String attribDesc = attribMetadata.getDescription();
                int[] attribErrs = findNonUnicodeChars(attribDesc);
                if (attribErrs.length != 0) {
                    // errors in the attribute description
                    UMLAttributeProblem problem = new UMLAttributeProblem(
                        classMetadata.getUMLPackageMetadata().getName(),
                        classMetadata.getName(), attribMetadata.getName(),
                        attribDesc, attribErrs);
                    problems.add(problem);
                }
            }
        }
        
        return problems;
    }
    
    
    private int[] findNonUnicodeChars(String str) {
        List<Integer> errs = new ArrayList<Integer>();
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                if (Character.isIdentifierIgnorable(str.charAt(i))) {
                    errs.add(Integer.valueOf(i));
                }
            }
        }
        int[] primativeArray = new int[errs.size()];
        for (int i = 0; i < errs.size(); i++) {
            primativeArray[i] = errs.get(i).intValue();
        }
        return primativeArray;
    }
    
    
    public static void main(String[] args) {
        String appUrl = DEFAULT_CADSR_APPLICATION_URL;
        String modelName = "NCIA_Model";
        String modelVersion = "3";
        if (args.length == 2) {
            modelName = args[0];
            modelVersion = args[1];
        }
        
        ModelProblemsUtil util = new ModelProblemsUtil(ApplicationService.getRemoteInstance(appUrl));
        try {
            List<ModelProblem> problems = util.findProblems(modelName, modelVersion);
            if (problems.size() == 0) {
                System.out.println("No errors found");
            } else {
                System.out.println("Problems in model:");
                for (ModelProblem problem : problems) {
                    System.out.println(problem.toString());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
