package gov.nih.nci.cagrid.metadata.xmi;

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

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
  *  XMIHandler
  *  SAX handler for XMI -> Domain Model
  * 
  * @author Patrick McConnell
  * @author David Ervin
  * 
  * @created Oct 22, 2007 10:26:25 AM
  * @version $Id: Sdk4EaXMIHandler.java,v 1.1 2008-04-04 15:57:41 dervin Exp $
 */
class Sdk4EaXMIHandler extends DefaultHandler {
    private static final Logger LOG = Logger.getLogger(Sdk4EaXMIHandler.class);   
    
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
    private Map<String, UMLAttribute> attribTable; //attribute ID to attribute instance
    private Map<String, List<SemanticMetadata>> smTable; // element ID to semantic metadata list
    private Map<String, String> typeTable; // type ID to type name

    // state variables
    private UMLAssociationEdge edge;
    private boolean sourceNavigable = false;
    private boolean targetNavigable = false;
    private String pkg = "";
    private boolean handlingAttribute = false;

    public Sdk4EaXMIHandler(XMIParser parser) {
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
    }


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        chars.append(ch, start, length);
    }


    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals(XMIConstants.XMI_UML_PACKAGE)) {
            int index = pkg.lastIndexOf('.');
            if (index == -1) {
                pkg = "";
            } else {
                pkg = pkg.substring(0, index);
            }
        } else if (qName.equals(XMIConstants.XMI_UML_CLASS)) {
            UMLClass cl = classList.get(classList.size() - 1);
            cl.setUmlAttributeCollection(new UMLClassUmlAttributeCollection(
                attribList.toArray(new UMLAttribute[0])));
            attribList.clear();
        } else if (qName.equals(XMIConstants.XMI_UML_ASSOCIATION)) {
            UMLAssociation assoc = assocList.get(assocList.size() - 1);
            if (sourceNavigable && !targetNavigable) {
                UMLAssociationEdge assocEdge = assoc.getSourceUMLAssociationEdge().getUMLAssociationEdge();
                assoc.getSourceUMLAssociationEdge().setUMLAssociationEdge(
                    assoc.getTargetUMLAssociationEdge().getUMLAssociationEdge());
                assoc.getTargetUMLAssociationEdge().setUMLAssociationEdge(assocEdge);
            }
            assoc.setBidirectional(sourceNavigable && targetNavigable);
        }

        chars.delete(0, chars.length());
    }


    @Override
    public void startElement(
        String uri, String localName, String qName, Attributes atts) throws SAXException {
        chars.delete(0, chars.length());

        if (qName.equals(XMIConstants.XMI_UML_PACKAGE)) {
            handlePackage(atts);
        } else if (qName.equals(XMIConstants.XMI_UML_CLASS)) {
            handleClass(atts);
        } else if (qName.equals(XMIConstants.XMI_UML_ATTRIBUTE)) {
            handleAttribute(atts);
        } else if (qName.equals(Sdk4EaXMIConstants.XMI_UML_CLASSIFIER)) {
            handleClassifier(atts);
        } else if (qName.equals(XMIConstants.XMI_UML_ASSOCIATION)) {
            // start the new association
            UMLAssociation ass = new UMLAssociation();
            assocList.add(ass);
        } else if (qName.equals(XMIConstants.XMI_UML_ASSOCIATION_END)) {
            handleAssociationEnd(atts);
        } else if (qName.equals(XMIConstants.XMI_UML_MULTIPLICITY_RANGE) && edge != null) {
            handleMultiplicity(atts);
        } else if (qName.equals(XMIConstants.XMI_UML_GENERALIZATION)) {
            handleGeneralization(atts);
        } else if (qName.equals(XMIConstants.XMI_UML_TAGGED_VALUE)) {
            handleTag(atts);
        } else if (qName.equals(XMIConstants.XMI_UML_DATA_TYPE)) {
            handleDataType(atts);
        } else if (qName.equals(XMIConstants.XMI_FOUNDATION_CORE_CLASSIFIER)) {
            if (attribList.size() == 0) {
                LOG.info("Ignoring " + XMIConstants.XMI_FOUNDATION_CORE_CLASSIFIER);
            } else {
                attribList.get(attribList.size() - 1)
                    .setDataTypeName(atts.getValue(XMIConstants.XMI_IDREF));
            }
        }
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
    
    
    // ------------------
    // XMI type handlers
    // ------------------
    
    
    private void handleDataType(Attributes atts) {
        typeTable.put(atts.getValue(XMIConstants.XMI_ID_ATTRIBUTE), 
            atts.getValue(XMIConstants.XMI_NAME_ATTRIBUTE));
    }
    
    
    private void handleGeneralization(Attributes atts) {
        UMLGeneralization gen = new UMLGeneralization();
        String subId = atts.getValue(XMIConstants.XMI_UML_GENERALIZATION_CHILD);
        String superId = atts.getValue(XMIConstants.XMI_UML_GENERALIZATION_PARENT);
        if (subId == null) {
            subId = atts.getValue(XMIConstants.XMI_UML_GENERALIZATION_SUBTYPE);
        }
        if (superId == null) {
            superId = atts.getValue(XMIConstants.XMI_UML_GENERALIZATION_SUPERTYPE);
        }
        gen.setSubClassReference(new UMLClassReference(subId));
        gen.setSuperClassReference(new UMLClassReference(superId));
        genList.add(gen);
    }
    
    
    private void handleMultiplicity(Attributes atts) {
        edge.setMinCardinality(Integer.parseInt(
            atts.getValue(XMIConstants.XMI_UML_MULTIPLICITY_LOWER)));
        edge.setMaxCardinality(Integer.parseInt(
            atts.getValue(XMIConstants.XMI_UML_MULTIPLICITY_UPPER)));
    }
    
    
    private void handleAssociationEnd(Attributes atts) {
        // get the most recently found association
        UMLAssociation assoc = assocList.get(assocList.size() - 1);
        boolean isNavigable = "true".equals(atts.getValue(XMIConstants.XMI_UML_ASSOCIATION_IS_NAVIGABLE));

        edge = new UMLAssociationEdge();
        if (assoc.getSourceUMLAssociationEdge() == null) {
            assoc.setSourceUMLAssociationEdge(new UMLAssociationSourceUMLAssociationEdge(edge));
            sourceNavigable = isNavigable;
        } else {
            assoc.setTargetUMLAssociationEdge(new UMLAssociationTargetUMLAssociationEdge(edge));
            targetNavigable = isNavigable;
        }
        edge.setRoleName(atts.getValue(XMIConstants.XMI_NAME_ATTRIBUTE));
        edge.setUMLClassReference(new UMLClassReference(atts.getValue(XMIConstants.XMI_TYPE_ATTRIBUTE)));
    }
    
    
    private void handleAttribute(Attributes atts) {
        handlingAttribute = true;
        UMLAttribute att = new UMLAttribute();
        att.setName(atts.getValue(XMIConstants.XMI_NAME_ATTRIBUTE));
        att.setVersion(this.parser.attributeVersion);
        attribList.add(att);
    }
    
    
    private void handleClassifier(Attributes atts) {
        if (handlingAttribute) {
            UMLAttribute lastAttribute = attribList.get(attribList.size() - 1);
            UMLClass lastClass = classList.get(classList.size() - 1);
            String localId = atts.getValue(XMIConstants.XMI_IDREF);
            String className = "" + lastClass.getPackageName() + "." + lastClass.getClassName();
            lastAttribute.setPublicID((className + localId).hashCode());
            attribTable.put(String.valueOf(lastAttribute.getPublicID()), lastAttribute);
            handlingAttribute = false;
        }
    }
    
    
    private void handleClass(Attributes atts) {
        UMLClass cl = new UMLClass();
        cl.setClassName(atts.getValue(XMIConstants.XMI_NAME_ATTRIBUTE));
        cl.setId(atts.getValue(XMIConstants.XMI_ID_ATTRIBUTE));
        cl.setPackageName(pkg);
        cl.setProjectName(this.parser.projectShortName);
        cl.setProjectVersion(this.parser.projectVersion);
        classList.add(cl);
        classTable.put(cl.getId(), cl);
    }
    
    
    private void handlePackage(Attributes atts) {
        String name = atts.getValue(XMIConstants.XMI_NAME_ATTRIBUTE);
        if (!name.equals(XMIConstants.XMI_LOGICAL_VIEW) 
            && !name.equals(XMIConstants.XMI_LOGICAL_MODEL)
            && !name.equals(XMIConstants.XMI_DATA_MODEL)) {
            if (!pkg.equals("")) {
                pkg += ".";
            }
            pkg += atts.getValue(XMIConstants.XMI_NAME_ATTRIBUTE);
        }
    }
    
    
    private void handleTag(Attributes atts) {
        String tag = atts.getValue(XMIConstants.XMI_UML_TAGGED_VALUE_TAG);
        String modelElement = atts.getValue(XMIConstants.XMI_UML_TAGGED_VALUE_MODEL_ELEMENT);
        String value = atts.getValue(XMIConstants.XMI_UML_TAGGED_VALUE_VALUE);

        LOG.debug(tag + " on " + modelElement);            
        if (tag.startsWith(XMIConstants.XMI_TAG_PROPERTY)) {
            modelElement = String.valueOf(modelElement.hashCode());
            LOG.debug(" (" + modelElement + ")");
        }
        LOG.debug(" = " + value);

        if (tag.equals(XMIConstants.XMI_TAG_DESCRIPTION)) {
            if (classTable.containsKey(modelElement)) {
                classTable.get(modelElement).setDescription(value);
            } else if (attribTable.containsKey(modelElement)) {
                attribTable.get(modelElement).setDescription(value);
            }
        } else if (tag.startsWith(XMIConstants.XMI_TAG_OBJECT_CLASS_CONCEPT_CODE)
            || tag.startsWith(XMIConstants.XMI_TAG_OBJECT_CLASS_QUALIFIER_CONCEPT_CODE) 
            || tag.startsWith(XMIConstants.XMI_TAG_PROPERTY_CONCEPT_CODE)
            || tag.startsWith(XMIConstants.XMI_TAG_PROPERTY_QUALIFIER_CONCEPT_CODE)) {
            addSemanticMetadata(tag, modelElement, value);
        } else if (tag.startsWith(XMIConstants.XMI_TAG_OBJECT_CLASS_CONCEPT_PREFERRED_NAME)
            || tag.startsWith(XMIConstants.XMI_TAG_OBJECT_CLASS_QUALIFIER_CONCEPT_PREFERRED_NAME)
            || tag.startsWith(XMIConstants.XMI_TAG_PROPERTY_CONCEPT_PREFERRED_NAME)
            || tag.startsWith(XMIConstants.XMI_TAG_PROPERTY_QUALIFIER_CONCEPT_PREFERRED_NAME)) {
            addSemanticMetadata(tag, modelElement, value);
        } else if ((tag.startsWith(XMIConstants.XMI_TAG_OBJECT_CLASS_CONCEPT_DEFINITION) 
                && !tag.startsWith(XMIConstants.XMI_TAG_OBJECT_CLASS_CONCEPT_DEFINITION_SOURCE))
            || (tag.startsWith(XMIConstants.XMI_TAG_OBJECT_CLASS_QUALIFIER_CONCEPT_DEFINITION) 
                && !tag.startsWith(XMIConstants.XMI_TAG_OBJECT_CLASS_QUALIFIER_CONCEPT_DEFINITION_SOURCE))
            || (tag.startsWith(XMIConstants.XMI_TAG_PROPERTY_CONCEPT_DEFINITION) 
                && !tag.startsWith(XMIConstants.XMI_TAG_PROPERTY_CONCEPT_DEFINITION_SOURCE))
            || (tag.startsWith(XMIConstants.XMI_TAG_PROPERTY_QUALIFIER_CONCEPT_DEFINITION) 
                && !tag.startsWith(XMIConstants.XMI_TAG_PROPERTY_QUALIFIER_CONCEPT_DEFINITION_SOURCE))) {
            addSemanticMetadata(tag, modelElement, value);
        }
    }
    
    
    //---------------------
    // general helpers
    //---------------------
    

    private int getSemanticMetadataOrder(String tag) {
        char c = tag.charAt(tag.length() - 1);
        if (Character.isDigit(c)) {
            return Integer.parseInt(String.valueOf(c));
        }
        return 0;
    }


    private void addSemanticMetadata(String tag, String modelElement, String value) {
        int order = getSemanticMetadataOrder(tag);

        List<SemanticMetadata> smList = smTable.get(modelElement);
        if (smList == null) {
            smTable.put(modelElement, smList = new ArrayList<SemanticMetadata>(9));
        }

        int size = smList.size();
        if (size <= order) {
            for (int i = smList.size(); i <= order; i++) {
                smList.add(new SemanticMetadata());
            }
        }

        SemanticMetadata sm = smList.get(order);
        if (tag.indexOf(XMIConstants.XMI_TAG_CONCEPT_CODE) != -1) {
            sm.setOrder(Integer.valueOf(order));
            sm.setConceptCode(value);
        } else if (tag.indexOf(XMIConstants.XMI_TAG_PREFERRED_NAME) != -1) {
            sm.setConceptName(value);
        } else if (tag.indexOf(XMIConstants.XMI_TAG_CONCEPT_DEFINITION) != -1) {
            sm.setConceptDefinition(value);
        }
    }


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

        // flatten each cl
        for (String clId : classTable.keySet()) {
            UMLClass cl = classTable.get(clId);
            List<UMLAttribute> flatAttributes = 
                flattenAttributes(parentTable, clId);
            cl.getUmlAttributeCollection().setUMLAttribute(
                flatAttributes.toArray(new UMLAttribute[0]));
        }
    }


    private List<UMLAttribute> flattenAttributes(
        Map<String, String> parentTable, String clId) {
        if (clId == null) {
            return new ArrayList<UMLAttribute>(0);
        }
        List<UMLAttribute> flat = new ArrayList<UMLAttribute>();

        // my atts
        UMLClass cl = classTable.get(clId);
        for (UMLAttribute att : cl.getUmlAttributeCollection().getUMLAttribute()) {
            flat.add(att);
        }
        // my parent's atts
        for (UMLAttribute att : flattenAttributes(parentTable, parentTable.get(clId))) {
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