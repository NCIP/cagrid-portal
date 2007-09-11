package gov.nci.nih.cagrid.validator.steps.cadsr;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.common.Utils;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.jdom.Element;
import org.projectmobius.common.XMLUtilities;

/** 
 *  ProjectListTestStep
 *  Step lists out project contents and compares to expected values
 * 
 * @author David Ervin
 * 
 * @created Sep 11, 2007 10:21:40 AM
 * @version $Id: ProjectListStep.java,v 1.1 2007-09-11 18:25:46 dervin Exp $ 
 */
public class ProjectListStep extends BaseCadsrTestStep {
    public static final String GOLD_RESULTS_FILE = "goldResultsFile";
    
    public ProjectListStep() {
        super();
    }
    

    public ProjectListStep(String serviceUrl, File tempDir, Properties configuration) {
        super(serviceUrl, tempDir, configuration);
    }


    public void runStep() throws Throwable {
        Project[] goldProjects = getGoldProjects();
        // list out all projects
        Project[] projects = getCadsrClient().findAllProjects();
        // start comparing each project with the expected ones
        for (Project goldProj : goldProjects) {
            Project foundProject = getMatchingProject(projects, goldProj);
            assertNotNull("Project " + goldProj.getShortName() + " " + goldProj.getVersion() + " not found", foundProject);
            // make sure all packages exist
            UMLPackageMetadata[] packages = getCadsrClient().findPackagesInProject(foundProject);
            Iterator packageIter = goldProj.getUMLPackageMetadataCollection().iterator();
            while (packageIter.hasNext()) {
                UMLPackageMetadata goldPack = (UMLPackageMetadata) packageIter.next();
                UMLPackageMetadata foundPack = getMatchingPackage(packages, goldPack);
                assertNotNull("Package " + goldPack.getName() + " not found", foundPack);
                // make sure all classes exist
                UMLClassMetadata[] classes = getCadsrClient().findClassesInPackage(foundProject, foundPack.getName());
                Iterator classIter = goldPack.getUMLClassMetadataCollection().iterator();
                while (classIter.hasNext()) {
                    UMLClassMetadata goldClass = (UMLClassMetadata) classIter.next();
                    UMLClassMetadata foundClass = getMatchingClass(classes, goldClass);
                    assertNotNull("Class " + goldClass.getFullyQualifiedName() + " not found", foundClass);
                }
            }
        }
    }
    
    
    private Project getMatchingProject(Project[] projects, Project proj) {
        for (Project p : projects) {
            if (p.getShortName().equals(proj.getShortName()) && p.getVersion().equals(proj.getVersion())) {
                return p;
            }
        }
        return null;
    }
    
    
    private UMLPackageMetadata getMatchingPackage(UMLPackageMetadata[] packages, UMLPackageMetadata pack) {
        for (UMLPackageMetadata p : packages) {
            if (p.getName().equals(pack.getName())) {
                return p;
            }
        }
        return null;
    }
    
    
    private UMLClassMetadata getMatchingClass(UMLClassMetadata[] classes, UMLClassMetadata clazz) {
        for (UMLClassMetadata m : classes) {
            if (m.getFullyQualifiedName().equals(clazz.getFullyQualifiedName())) {
                return m;
            }
        }
        return null;
    }
    
    
    private Project[] getGoldProjects() {
        String goldFilename = getConfiguration().getProperty(GOLD_RESULTS_FILE);
        Project[] projects = null;
        try {
            List<Project> projList = new ArrayList();
            Element topLevelElement = XMLUtilities.fileNameToDocument(goldFilename).getRootElement();
            Iterator projElementIter = topLevelElement.getChildren().iterator();
            while (projElementIter.hasNext()) {
                Element projectElement = (Element) projElementIter.next();
                String xmlText = XMLUtilities.elementToString(projectElement);
                StringReader reader = new StringReader(xmlText);
                Project proj = (Project) Utils.deserializeObject(reader, Project.class);
                projList.add(proj);
            }
            projects = projList.toArray(new Project[0]);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error loading gold projects file: " + ex.getMessage());
        }
        return projects;
    }
    
    
    public Set<String> getRequiredConfigurationProperties() {
        Set<String> props = super.getRequiredConfigurationProperties();
        props.add(GOLD_RESULTS_FILE);
        return props;
    }
}
