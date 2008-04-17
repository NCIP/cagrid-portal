package gov.nih.nci.cagrid.metadata.xmi;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.common.SemanticMetadata;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.common.UMLClassUmlAttributeCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLAssociationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLClassCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelUmlGeneralizationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationSourceUMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationTargetUMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference;
import gov.nih.nci.cagrid.metadata.dataservice.UMLGeneralization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/** 
 *  ArgoXmiHandler
 *  SAX handler for converting Argo UML's XMI to a domain model
 * 
 * @author David Ervin
 * 
 * @created Mar 19, 2008 3:16:47 PM
 * @version $Id: ArgoXmiHandler.java,v 1.2 2008-04-17 19:18:23 dervin Exp $ 
 */
public class ArgoXmiHandler extends DefaultHandler {
    
    private static final Log LOG = LogFactory.getLog(ArgoXmiHandler.class);

    // parser contains configuration options and information for the handler
    private final XMIParser parser;

    private StringBuffer chars;

    // lists of domain model components
    private List<UMLClass> classList;
    private List<UMLAttribute> attribList;
    private List<UMLAssociation> assocList;
    private List<UMLGeneralization> genList;

    // maps from XMI name to domain model component
    private Map<String, UMLClass> classTable; // class ID to class instance
    private Map<String, UMLAttribute> attribTable; // attribute ID to attribute instance
    private Map<String, List<SemanticMetadata>> smTable; // element ID to semantic metadata list
    private Map<String, String> typeTable; // type ID to type name
    
    // some state variables
    private int currentNodeDepth;
    private String currentPackageName;
    private boolean handlingGeneralization;
    private boolean handlingChildGeneralization;
    private boolean handlingParentGeneralization;
    private int currentGeneralizationNodeDepth;
    private int currentClassNodeDepth;
    private boolean handlingClass;
    private boolean handlingAssociation;
    private boolean associationSourceIsNavigable;
    private boolean associationTargetIsNavigable;
    private boolean associationSourceMultiplicitySet;
    private boolean associationSourceParticipantSet;
    
    public ArgoXmiHandler(XMIParser parser) {
        super();
        this.parser = parser;
        this.chars = new StringBuffer();
        // initialize lists
        this.classList = new ArrayList<UMLClass>();
        this.attribList = new ArrayList<UMLAttribute>();
        this.assocList = new ArrayList<UMLAssociation>();
        this.genList = new ArrayList<UMLGeneralization>();
        // initialize tables
        this.classTable = new HashMap<String, UMLClass>();
        this.attribTable = new HashMap<String, UMLAttribute>();
        this.smTable = new HashMap<String, List<SemanticMetadata>>();
        this.typeTable = new HashMap<String, String>();
        // initialize state
        currentNodeDepth = 0;
        currentPackageName = "";
        handlingGeneralization = false;
        handlingChildGeneralization = false;
        handlingParentGeneralization = false;
        currentGeneralizationNodeDepth = 0;
        currentClassNodeDepth = 0;
        handlingClass = false;
        handlingAssociation = false;
    }
    
    
    public void characters(char[] ch, int start, int length) throws SAXException {
        chars.append(ch, start, length);
    }
    
    
    public void startElement(
        String uri, String localName, String qName, Attributes atts) throws SAXException {
        currentNodeDepth++;
        // clean out the character buffer
        chars.delete(0, chars.length());
        
        // start handling elements by name
        if (qName.equals(XMIConstants.XMI_UML_PACKAGE)) {
            handlePackage(atts);
        } else if (insideValidPackage()) {
            if (qName.equals(XMIConstants.XMI_UML_CLASS)) {
                handleClass(atts);
            } else if (qName.startsWith(XMIConstants.XMI_UML_GENERALIZATION)) {
                handleGeneralization(atts);
                if (qName.endsWith(XMIConstants.XMI_UML_GENERALIZATION_CHILD)) {
                    handlingChildGeneralization = true;
                } else if (qName.endsWith(XMIConstants.XMI_UML_GENERALIZATION_PARENT)) {
                    handlingParentGeneralization = true;
                }
            } else if (qName.equals(XMIConstants.XMI_UML_ATTRIBUTE)) {
                handleAttribute(atts);
            } else if (qName.equals(XMIConstants.XMI_UML_ASSOCIATION)) {
                handleAssociation();
            } else if (qName.equals(XMIConstants.XMI_UML_ASSOCIATION_END) && handlingAssociation) {
                handleAssociationEnd(atts);
            } else if (qName.equals(XMIConstants.XMI_UML_MULTIPLICITY_RANGE) && handlingAssociation) {
                handleMultiplicityRange(atts);
            }
        }
    }
    
    
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // handle special element closures
        if (qName.equals(XMIConstants.XMI_UML_PACKAGE)) {
            handlePackageEnd();
        } else if (insideValidPackage()) {
            if (handlingGeneralization && 
                qName.equals(Sdk4ArgoUMLXMIConstants.XMI_UML_GENERALIZATION_CHILD)) {
                handlingChildGeneralization = false;
            } else if (handlingGeneralization &&
                qName.equals(Sdk4ArgoUMLXMIConstants.XMI_UML_GENERALIZATION_PARENT)) {
                handlingParentGeneralization = false;
            } else if (handlingGeneralization &&
                qName.equals(XMIConstants.XMI_UML_GENERALIZATION)) {
                if (currentNodeDepth == currentGeneralizationNodeDepth) {
                    LOG.debug("Done with a generalization element");
                    handlingGeneralization = false;
                }
            } else if (qName.equals(XMIConstants.XMI_UML_CLASS)) {
                if (currentNodeDepth == currentClassNodeDepth) {
                    handlingClass = false;
                    LOG.debug("CLASS ENDED");
                }
            } else if (qName.equals(XMIConstants.XMI_UML_ASSOCIATION)) {
                LOG.debug("ASSOCIATION ENDED");
                UMLAssociation currentAssociation = assocList.get(assocList.size() - 1);
                currentAssociation.setBidirectional(associationSourceIsNavigable && associationTargetIsNavigable);
                handlingAssociation = false;
            }
        }
        
        // clean out the char buffer
        chars.delete(0, chars.length());
        currentNodeDepth--;
    }
    
    
    public void endDocument() throws SAXException {
        applySemanticMetadata();
        applyDataTypes();
        flattenAttributes();
        applyFilters();

        this.parser.model = new DomainModel();

        this.parser.model.setProjectShortName(this.parser.projectShortName);
        this.parser.model.setProjectLongName(this.parser.projectLongName);
        this.parser.model.setProjectVersion(this.parser.projectVersion);
        this.parser.model.setProjectDescription(this.parser.projectDescription);

        // convert base UML classes to data UML classes
        gov.nih.nci.cagrid.metadata.dataservice.UMLClass[] dataClasses = 
            new gov.nih.nci.cagrid.metadata.dataservice.UMLClass[classList.size()];
        int i = 0;
        for (UMLClass commonClass : classList) {
            gov.nih.nci.cagrid.metadata.dataservice.UMLClass dataClass = 
                new gov.nih.nci.cagrid.metadata.dataservice.UMLClass();
            dataClass.setClassName(commonClass.getClassName());
            dataClass.setDescription(commonClass.getDescription());
            dataClass.setId(commonClass.getId());
            dataClass.setPackageName(commonClass.getPackageName());
            dataClass.setProjectName(commonClass.getProjectName());
            dataClass.setProjectVersion(commonClass.getProjectVersion());
            dataClass.setSemanticMetadata(commonClass.getSemanticMetadata());
            dataClass.setUmlAttributeCollection(commonClass.getUmlAttributeCollection());
            dataClass.setAllowableAsTarget(true); // NEW attribute for data classes
            dataClasses[i++] = dataClass;
        }
        this.parser.model.setExposedUMLClassCollection(
            new DomainModelExposedUMLClassCollection(dataClasses));
        this.parser.model.setExposedUMLAssociationCollection(
            new DomainModelExposedUMLAssociationCollection(assocList.toArray(new UMLAssociation[0])));
        this.parser.model.setUmlGeneralizationCollection(
            new DomainModelUmlGeneralizationCollection(genList.toArray(new UMLGeneralization[0])));
    }
    
    
    private boolean insideValidPackage() {
        // ignore everything that isn't part of the logical model
        String expectedPrefix = 
            Sdk4ArgoUMLXMIConstants.LOGICAL_VIEW_PACKAGE_NAME + "." +
            Sdk4ArgoUMLXMIConstants.LOGICAL_MODEL_PACKAGE_NAME;
        return currentPackageName.startsWith(expectedPrefix);
    }
    
    
    private String getTrimmedPackageName() {
        String expectedPrefix = 
            Sdk4ArgoUMLXMIConstants.LOGICAL_VIEW_PACKAGE_NAME + "." +
            Sdk4ArgoUMLXMIConstants.LOGICAL_MODEL_PACKAGE_NAME;
        String trimmedPackageName = currentPackageName.substring(expectedPrefix.length());
        if (trimmedPackageName.startsWith(".")) {
            trimmedPackageName = trimmedPackageName.substring(1);
        }
        return trimmedPackageName;
    }

    
    private void handlePackage(Attributes attribs) {
        String namePart = attribs.getValue(XMIConstants.XMI_NAME_ATTRIBUTE);
        if (!currentPackageName.equals("")) {
            currentPackageName += ".";
        }
        currentPackageName += namePart;
    }
    
    
    private void handlePackageEnd() {
        int index = currentPackageName.lastIndexOf('.');
        if (index == -1) {
            currentPackageName = "";
        } else {
            currentPackageName = currentPackageName.substring(0, index);
        }
    }
    
    
    private void handleClass(Attributes atts) {
        // clean up the package name
        String trimmedPackageName = getTrimmedPackageName();

        if (!handlingClass && !handlingAssociation && !handlingGeneralization) {
            // if we're not actually handling a class, start a new one
            handleNewClass(atts, trimmedPackageName);
        } else if (!handlingAssociation && handlingGeneralization) {
            // class elements appear inside generalization.child and generalization.parent
            handleGeneralizationClassRef(atts);
        } else if (handlingAssociation) {
            handleAssociationParticipantClass(atts);
        }
    }
    
    
    private void handleNewClass(Attributes atts, String packageName) {
        // create a new class
        handlingClass = true;
        currentClassNodeDepth = currentNodeDepth;
        UMLClass clazz = new UMLClass();
        clazz.setClassName(atts.getValue(XMIConstants.XMI_NAME_ATTRIBUTE));
        clazz.setId(atts.getValue(XMIConstants.XMI_ID_ATTRIBUTE));
        clazz.setPackageName(packageName);
        // project name and version from parser
        clazz.setProjectName(this.parser.getProjectShortName());
        clazz.setProjectVersion(this.parser.getProjectVersion());
        clazz.setUmlAttributeCollection(new UMLClassUmlAttributeCollection());
        LOG.debug("Created new class " + clazz.getPackageName() + "." + clazz.getClassName());
        // add the class to the list
        classList.add(clazz);
        // and the table of xmi.id to class
        classTable.put(clazz.getId(), clazz);
    }
    
    
    private void handleGeneralizationClassRef(Attributes atts) {
        UMLGeneralization currentGeneralization = genList.get(genList.size() - 1);
        if (handlingChildGeneralization) {
            LOG.debug("Handling subclass generalization");
            currentGeneralization.setSubClassReference(
                new UMLClassReference(atts.getValue(XMIConstants.XMI_IDREF)));
        } else if (handlingParentGeneralization) {
            LOG.debug("Handling superclass generalization");
            currentGeneralization.setSuperClassReference(
                new UMLClassReference(atts.getValue(XMIConstants.XMI_IDREF)));            
        }
    }
    
    
    private void handleAttribute(Attributes atts) {
        if (handlingClass) {
            UMLAttribute attrib = new UMLAttribute();
            attrib.setName(atts.getValue(XMIConstants.XMI_NAME_ATTRIBUTE));
            attrib.setVersion(this.parser.getAttributeVersion());
            String idValue = atts.getValue(XMIConstants.XMI_ID_ATTRIBUTE);
            attrib.setPublicID(idValue.hashCode());
            attribList.add(attrib);
            attribTable.put(String.valueOf(attrib.getPublicID()), attrib);
            // TODO: ???
            // attach the attribute to the most recent class
            UMLClass lastClass = classList.get(classList.size() - 1);
            UMLClassUmlAttributeCollection attribCollection = lastClass.getUmlAttributeCollection();
            UMLAttribute[] currentAttributes = attribCollection.getUMLAttribute();
            if (currentAttributes == null) {
                currentAttributes = new UMLAttribute[0];
            }
            currentAttributes = (UMLAttribute[]) Utils.appendToArray(currentAttributes, attrib);
            attribCollection.setUMLAttribute(currentAttributes);
            LOG.debug("Handled attribute " + attrib.getName() + " of class " + lastClass.getPackageName() + "." + lastClass.getClassName());
        }
    }
    
    
    private void handleGeneralization(Attributes atts) {
        // verify this is a generalization we need to handle
        if (atts.getValue(XMIConstants.XMI_ID_ATTRIBUTE) != null) {
            handlingGeneralization = true;
            currentGeneralizationNodeDepth = currentNodeDepth;
            UMLGeneralization gen = new UMLGeneralization();
            genList.add(gen);
            LOG.debug("Started new generalization");
        }
    }
    
    
    private void handleAssociation() {
        LOG.debug("HANDLING ASSOCIATION");
        UMLAssociation association = new UMLAssociation();
        assocList.add(association);
        handlingAssociation = true;
        associationSourceMultiplicitySet = false;
        associationSourceParticipantSet = false;
    }
    
    
    private void handleAssociationEnd(Attributes atts) {
        LOG.debug("Handling association edge");
        UMLAssociation currentAssociation = assocList.get(assocList.size() - 1);
        // create the new edge
        UMLAssociationEdge edge = new UMLAssociationEdge();
        String roleName = atts.getValue(XMIConstants.XMI_NAME_ATTRIBUTE);
        boolean isNavigable = Boolean.valueOf(
            atts.getValue(XMIConstants.XMI_UML_ASSOCIATION_IS_NAVIGABLE)).booleanValue();
        edge.setRoleName(roleName);
        
        // determine if its source or target
        if (currentAssociation.getSourceUMLAssociationEdge() == null) {
            UMLAssociationSourceUMLAssociationEdge sourceEdge = 
                new UMLAssociationSourceUMLAssociationEdge();
            sourceEdge.setUMLAssociationEdge(edge);
            currentAssociation.setSourceUMLAssociationEdge(sourceEdge);
            associationSourceIsNavigable = isNavigable;
            LOG.debug("Added source edge");
        } else {
            UMLAssociationTargetUMLAssociationEdge targetEdge = 
                new UMLAssociationTargetUMLAssociationEdge();
            targetEdge.setUMLAssociationEdge(edge);
            currentAssociation.setTargetUMLAssociationEdge(targetEdge);
            associationTargetIsNavigable = isNavigable;
            LOG.debug("Added target edge");
        }
    }
    
    
    private void handleMultiplicityRange(Attributes atts) {
        LOG.debug("Handling association multiplicity");
        UMLAssociation currentAssociation = assocList.get(assocList.size() - 1);
        UMLAssociationEdge edge = null;
        if (!associationSourceMultiplicitySet) {
            // source edge
            edge = currentAssociation.getSourceUMLAssociationEdge().getUMLAssociationEdge();
            LOG.debug("\t... to source edge");
            associationSourceMultiplicitySet = true;
        } else {
            edge = currentAssociation.getTargetUMLAssociationEdge().getUMLAssociationEdge();
            LOG.debug("\t... to target edge");
        }
        int min = Integer.parseInt(atts.getValue(XMIConstants.XMI_UML_MULTIPLICITY_LOWER));
        int max = Integer.parseInt(atts.getValue(XMIConstants.XMI_UML_MULTIPLICITY_UPPER));
        edge.setMinCardinality(min);
        edge.setMaxCardinality(max);
    }
    
    
    private void handleAssociationParticipantClass(Attributes atts) {
        LOG.debug("Handling association participant");
        UMLAssociation currentAssociation = assocList.get(assocList.size() - 1);
        UMLAssociationEdge edge = null;
        if (!associationSourceParticipantSet) {
            // source edge
            edge = currentAssociation.getSourceUMLAssociationEdge().getUMLAssociationEdge();
            LOG.debug("\t... to source edge");
            associationSourceParticipantSet = true;
        } else {
            edge = currentAssociation.getTargetUMLAssociationEdge().getUMLAssociationEdge();
            LOG.debug("\t... to target edge");
        }
        String refid = atts.getValue(XMIConstants.XMI_IDREF);
        edge.setUMLClassReference(new UMLClassReference(refid));
    }
    
    
    // -------------------------------
    // end of document cleanup tooling
    // -------------------------------
    
    
    private void applySemanticMetadata() {
        for (String id : smTable.keySet()) {
            if (classTable.containsKey(id)) {
                classTable.get(id).setSemanticMetadata(
                    smTable.get(id).toArray(new SemanticMetadata[0]));
            } else if (attribTable.containsKey(id)) {
                attribTable.get(id).setSemanticMetadata(
                    smTable.get(id).toArray(new SemanticMetadata[0]));
            }
        }
    }


    private void applyDataTypes() {
        for (String id : attribTable.keySet()) {
            UMLAttribute att = attribTable.get(id);
            String typeRef = att.getDataTypeName();

            String dataType = null;

            // check for class
            if (dataType == null) {
                UMLClass typeCl = classTable.get(typeRef);
                if (typeCl != null)
                    dataType = typeCl.getClassName();
            }

            // check type table
            if (dataType == null) {
                dataType = typeTable.get(typeRef);
            }

            // perform mapping
            if (dataType != null && XMIParser.DATATYPE_MAP.containsKey(dataType)) {
                dataType = XMIParser.DATATYPE_MAP.get(dataType);
            }

            // set data type
            att.setDataTypeName(dataType);
        }
    }


    private void flattenAttributes() {
        // build parent table
        Map<String, String> parentTable = new HashMap<String, String>();
        for (UMLGeneralization gen : genList) {
            parentTable.put(gen.getSubClassReference().getRefid(), 
                gen.getSuperClassReference().getRefid());
        }

        // flatten each class by ID
        for (String classId : classTable.keySet()) {
            UMLClass clazz = classTable.get(classId);
            List<UMLAttribute> flatAttributes = 
                flattenAttributesOfClass(parentTable, classId);
            clazz.getUmlAttributeCollection().setUMLAttribute(
                flatAttributes.toArray(new UMLAttribute[0]));
        }
    }


    private List<UMLAttribute> flattenAttributesOfClass(
        Map<String, String> parentTable, String classId) {
        if (classId == null) {
            return new ArrayList<UMLAttribute>(0);
        }
        List<UMLAttribute> flat = new ArrayList<UMLAttribute>();

        // my atts
        UMLClass cl = classTable.get(classId);
        if (cl.getUmlAttributeCollection() != null && cl.getUmlAttributeCollection().getUMLAttribute() != null) {
            for (UMLAttribute att : cl.getUmlAttributeCollection().getUMLAttribute()) {
                flat.add(att);
            }
        }
        // my parent's atts
        for (UMLAttribute att : flattenAttributesOfClass(parentTable, parentTable.get(classId))) {
            flat.add(att);
        }

        return flat;
    }


    private void applyFilters() {
        // build filter set
        HashSet<String> filterSet = new HashSet<String>();
        // filter primtives
        if (this.parser.filterPrimitiveClasses) {
            for (UMLClass cl : classList) {
                if (cl.getPackageName().startsWith("java")) {
                    filterSet.add(cl.getId());
                }
            }
        }
        // filter root class
        for (UMLClass cl : classList) {
            if (cl.getPackageName().equals("")) {
                filterSet.add(cl.getId());
            }
        }

        // filter classes
        List<UMLClass> filteredClasses = new ArrayList<UMLClass>(this.classList.size());
        for (UMLClass cl : this.classList) {
            if (!filterSet.contains(cl.getId())) {
                filteredClasses.add(cl);
            }
        }
        this.classList = filteredClasses;

        // filter assocations
        List<UMLAssociation> filteredAssociations = 
            new ArrayList<UMLAssociation>(this.assocList.size());
        for (UMLAssociation assoc : this.assocList) {
            if (!filterSet.contains(assoc.getSourceUMLAssociationEdge()
                .getUMLAssociationEdge().getUMLClassReference().getRefid())
                && !filterSet.contains(assoc.getTargetUMLAssociationEdge()
                    .getUMLAssociationEdge().getUMLClassReference().getRefid())) {
                filteredAssociations.add(assoc);
            }
        }
        this.assocList = filteredAssociations;

        // filter generalizations
        List<UMLGeneralization> filteredGeneralizations = 
            new ArrayList<UMLGeneralization>(this.genList.size());
        for (UMLGeneralization gen : this.genList) {
            if (!filterSet.contains(gen.getSubClassReference().getRefid())
                && !filterSet.contains(gen.getSuperClassReference().getRefid())) {
                filteredGeneralizations.add(gen);
            }
        }
        this.genList = filteredGeneralizations;
    }
}
