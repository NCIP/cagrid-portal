package gov.nci.nih.cagrid.validator.steps.cadsr;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/** 
 *  ProjectListTestStep
 *  Step lists out project contents and compares to expected values
 * 
 * @author David Ervin
 * 
 * @created Sep 11, 2007 10:21:40 AM
 * @version $Id: ProjectListStep.java,v 1.1 2007-11-26 17:09:10 dervin Exp $ 
 */
public class ProjectListStep extends BaseCadsrTestStep {
    public static final String EXPECTED_PROJECTS_FILE = "expectedProjectsFile";
    
    public ProjectListStep() {
        super();
    }
    

    public ProjectListStep(String serviceUrl, File tempDir, Properties configuration) {
        super(serviceUrl, tempDir, configuration);
    }


    public void runStep() throws Throwable {
        Element goldProjects = getGoldProjects();
        // <GoldProjects>
        // list out all projects
        Project[] projects = getCadsrClient().findAllProjects();
        // start comparing each project with the expected ones
        Iterator projectElemIter = goldProjects.getChildren("Project", goldProjects.getNamespace()).iterator();
        // <Project name="foo" version="x.y">
        while (projectElemIter.hasNext()) {
            Element projectElement = (Element) projectElemIter.next();
            String projectShortName = projectElement.getAttributeValue("name");
            String projectVersion = projectElement.getAttributeValue("version");
            Project foundProject = getMatchingProject(projects, projectShortName, projectVersion);
            assertNotNull("Project " + projectShortName + " ver " + projectVersion + " not found", foundProject);
            // make sure all packages exist
            UMLPackageMetadata[] packages = getCadsrClient().findPackagesInProject(foundProject);
            Iterator packageElemIter = projectElement.getChildren("Package", projectElement.getNamespace()).iterator();
            // <Package name="foo">
            while (packageElemIter.hasNext()) {
                Element packageElement = (Element) packageElemIter.next();
                String packageName = packageElement.getAttributeValue("name");
                UMLPackageMetadata foundPack = getMatchingPackage(packages, packageName);
                assertNotNull("Package " + packageName + " not found", foundPack);
                // make sure all classes exist
                UMLClassMetadata[] classes = getCadsrClient().findClassesInPackage(foundProject, foundPack.getName());
                Iterator classElemIter = packageElement.getChildren("Class", packageElement.getNamespace()).iterator();
                // <Class name="foo">
                while (classElemIter.hasNext()) {
                    Element classElement = (Element) classElemIter.next();
                    String className = classElement.getAttributeValue("name");
                    UMLClassMetadata foundClass = getMatchingClass(classes, className);
                    assertNotNull("Class " + className + " not found", foundClass);
                }
            }
        }
    }
    
    
    private Project getMatchingProject(Project[] projects, String shortName, String version) {
        for (Project p : projects) {
            if (p.getShortName().equals(shortName) && p.getVersion().equals(version)) {
                return p;
            }
        }
        return null;
    }
    
    
    private UMLPackageMetadata getMatchingPackage(UMLPackageMetadata[] packages, String packName) {
        for (UMLPackageMetadata p : packages) {
            if (p.getName().equals(packName)) {
                return p;
            }
        }
        return null;
    }
    
    
    private UMLClassMetadata getMatchingClass(UMLClassMetadata[] classes, String className) {
        for (UMLClassMetadata m : classes) {
            if (m.getName().equals(className)) {
                return m;
            }
        }
        return null;
    }
    
    
    private Element getGoldProjects() {
        String goldFilename = getConfiguration().getProperty(EXPECTED_PROJECTS_FILE);
        File goldFile = new File(goldFilename);
        if (!goldFile.exists() && goldFile.canRead()) {
            fail("Could not read file " + goldFilename);
        }
        Element root = null;
        try {
            FileInputStream fis = new FileInputStream(goldFile);
            SAXBuilder builder = new SAXBuilder(false);
            root = builder.build(fis).getRootElement();
            fis.close();
            return root;
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error reading gold project file: " + ex.getMessage());
        }
        return root;
    }
    
    
    public Set<String> getRequiredConfigurationProperties() {
        Set<String> props = super.getRequiredConfigurationProperties();
        props.add(EXPECTED_PROJECTS_FILE);
        return props;
    }
}
