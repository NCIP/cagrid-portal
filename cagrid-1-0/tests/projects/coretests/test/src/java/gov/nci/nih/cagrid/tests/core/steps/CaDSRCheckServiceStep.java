/*
 * Created on Jun 11, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.util.CaDSRExtractUtils;
import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.cadsr.common.CaDSRServiceI;
import gov.nih.nci.cagrid.metadata.common.SemanticMetadata;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.io.File;
import java.rmi.RemoteException;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;

import com.atomicobject.haste.framework.Step;


/**
 * This step will pull caDSR metadata from a caDSR grid service and compare it
 * to a locally cached XML metadata extract. It accomplishes this by comparing a
 * number of fields in the project, classes, associations, and attributes.
 * 
 * @author Patrick McConnell
 */
public class CaDSRCheckServiceStep extends Step {
    public static final String SERVICE_DEPLOYMENT_PATH = "cagrid/CaDSRService";
    private EndpointReferenceType endpoint;
    private File extractFile;


    public CaDSRCheckServiceStep(EndpointReferenceType endpoint, File extractFile) {
        super();

        this.endpoint = endpoint;
        this.extractFile = extractFile;
    }


    @Override
    public void runStep() throws Exception {
        CaDSRServiceI cadsr = new CaDSRServiceClient(this.endpoint);

        DomainModel extract = null;
        if (this.extractFile == null) {
            extract = CaDSRExtractUtils.findExtract(cadsr, "Genomic Identifiers", "1");
            // extract = CaDSRExtractUtils.findExtract(cadsr, "caTIES");
        } else {
            extract = CaDSRExtractUtils.readExtract(this.extractFile);
        }

        // find project
        Project[] projects = cadsr.findAllProjects();
        Project project = null;
        for (Project myProject : projects) {
            if (myProject.longName.equals(extract.getProjectLongName())
                && myProject.getVersion().equals(extract.getProjectVersion())) {
                project = myProject;
                break;
            }
        }

        // assert everything looks good
        compareProject(extract, cadsr, project);
    }


    @SuppressWarnings("unchecked")
    private void compareProject(DomainModel extract, CaDSRServiceI cadsr, Project project) throws RemoteException {
        assertNotNull(project);
        assertEquals(extract.getProjectLongName(), project.longName);
        assertEquals(extract.getProjectShortName(), project.shortName);
        assertEquals(extract.getProjectDescription(), project.description);
        assertEquals(extract.getProjectVersion(), project.version);

        UMLClass[] extractCls = extract.getExposedUMLClassCollection().getUMLClass();
        UMLClassMetadata[] cls = cadsr.findClassesInProject(project);// (UMLClassMetadata[])
        // project.getUMLClassMetadataCollection().toArray(new
        // UMLClassMetadata[0]);
        assertEquals(extractCls.length, cls.length);
        compareClasses(cadsr, extractCls, project, cls);

        gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation[] extractAssocations = extract
            .getExposedUMLAssociationCollection().getUMLAssociation();
        gov.nih.nci.cagrid.cadsrservice.UMLAssociation[] assocations = cadsr.findAssociationsInProject(project);
        if (extractAssocations == null && assocations == null) {
            // some models don't have associations?
        } else {
            // assertEquals(extractAssocations.length, associations.length);
            compareAssociations(extractAssocations, assocations);
        }
    }


    private void compareClasses(CaDSRServiceI cadsr, UMLClass[] extractCls, Project project, UMLClassMetadata[] cls)
        throws RemoteException {
        for (UMLClass extractCl : extractCls) {
            UMLClassMetadata cl = findClass(extractCl, cls);
            assertNotNull(cl);
            assertEquals(extractCl.getPackageName() + "." + extractCl.getClassName(), cl.getFullyQualifiedName());
            assertEquals(extractCl.getDescription(), cl.getDescription());

            SemanticMetadata[] extractMetadata = extractCl.getSemanticMetadata();
            gov.nih.nci.cadsr.umlproject.domain.SemanticMetadata[] metadata = cadsr.findSemanticMetadataForClass(
                project, cl);
            assertEquals(extractMetadata.length, metadata.length);
            compareMetadata(cadsr, extractMetadata, metadata);

            UMLAttribute[] extractAtts = extractCl.getUmlAttributeCollection().getUMLAttribute();
            UMLAttributeMetadata[] atts = cadsr.findAttributesInClass(project, cl);
            assertEquals(extractAtts.length, atts.length);
            compareAttributes(cadsr, extractAtts, atts);
        }
    }


    private void compareMetadata(CaDSRServiceI cadsr, SemanticMetadata[] extractMetadata,
        gov.nih.nci.cadsr.umlproject.domain.SemanticMetadata[] metadata) {
        for (SemanticMetadata em : extractMetadata) {
            assertNotNull(em);
            gov.nih.nci.cadsr.umlproject.domain.SemanticMetadata m = findSemanticMetadata(em, metadata);
            assertNotNull(m);

            assertEquals(m.getConceptCode(), em.getConceptCode());
            assertEquals(m.getConceptDefinition(), em.getConceptDefinition());
            assertEquals(m.getConceptName(), em.getConceptName());
            // assertEquals(m.id, em.id);
            // assertEquals(m.isPrimaryConcept, em.isPrimaryConcept);
            assertEquals(m.getOrder(), em.getOrder());
            assertEquals(m.getOrderLevel(), em.getOrderLevel());
        }
    }


    private void compareAttributes(CaDSRServiceI cadsr, UMLAttribute[] extractAtts, UMLAttributeMetadata[] atts)
        throws RemoteException {
        for (UMLAttribute extractAtt : extractAtts) {
            assertNotNull(extractAtt);
            UMLAttributeMetadata att = findAttribute(extractAtt, atts);
            assertNotNull(att);

            assertTrue((extractAtt.getDescription() == null && att.description == null)
                || (extractAtt.getDescription() == null && "".equals(att.description))
                || ("".equals(extractAtt.getDescription()) && att.description == null)
                || extractAtt.getDescription().equals(att.description));
            // assertEquals(extractAtt.getDescription(), att.description);
            assertEquals(getExtractAttributeName(extractAtt), att.name);
        }
    }


    private void compareAssociations(gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation[] extractAssociations,
        gov.nih.nci.cagrid.cadsrservice.UMLAssociation[] associations) {
        for (gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation extractAssociation : extractAssociations) {
            assertNotNull(extractAssociation);
            gov.nih.nci.cagrid.cadsrservice.UMLAssociation association = findAssociation(extractAssociation,
                associations);
            assertNotNull(association);
        }
    }


    private gov.nih.nci.cagrid.cadsrservice.UMLAssociation findAssociation(
        gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation extractAssociation,
        gov.nih.nci.cagrid.cadsrservice.UMLAssociation[] associations) {
        for (gov.nih.nci.cagrid.cadsrservice.UMLAssociation association : associations) {
            // String extractSourceClass =
            // extractAssociation.getSourceUMLAssociationEdge().getUMLAssociationEdge().getUmlClass().getUMLClass().getClassName();
            // String sourceClass =
            // association.getSourceUMLClassMetadata().getUMLClassMetadata().fullyQualifiedName;
            // assertEquals(extractSourceClass, sourceClass);

            return association;
        }
        return null;
    }


    private UMLAttributeMetadata findAttribute(UMLAttribute extractAtt, UMLAttributeMetadata[] atts) {
        // extractAtt.getName()=Gene:ensemblgeneID
        // att.getName()=ensemblgeneID
        for (UMLAttributeMetadata att : atts) {
            if (getExtractAttributeName(extractAtt).equals(att.name)) {
                return att;
            }
        }
        return null;
    }


    private String getExtractAttributeName(UMLAttribute extractAtt) {
        String extractName = extractAtt.getName();
        int index = extractName.indexOf(':');
        if (index != -1) {
            extractName = extractName.substring(index + 1);
        }
        return extractName;
    }


    private gov.nih.nci.cadsr.umlproject.domain.SemanticMetadata findSemanticMetadata(SemanticMetadata em,
        gov.nih.nci.cadsr.umlproject.domain.SemanticMetadata[] metadata) {
        for (gov.nih.nci.cadsr.umlproject.domain.SemanticMetadata m : metadata) {
            if (em.getConceptCode().equals(m.getConceptCode())) {
                return m;
            }
        }
        return null;
    }


    private UMLClassMetadata findClass(UMLClass extractCl, UMLClassMetadata[] cls) {
        String className = extractCl.getPackageName() + "." + extractCl.getClassName();
        for (UMLClassMetadata cl : cls) {
            if (className.equals(cl.getFullyQualifiedName())) {
                return cl;
            }
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
        CaDSRExtractUtils.setAxisConfig(new File("etc", "cadsr" + File.separator + "client-config.wsdd"));
        new CaDSRCheckServiceStep(new EndpointReferenceType(new Address(
            "http://localhost:8080/wsrf/services/cagrid/CaDSRService")), new File("test", "resources" + File.separator
            + "CheckCaDSRServiceStep" + File.separator + "caTIES_extract.xml")).runStep();
    }
}
