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
import java.util.HashSet;
import java.util.Hashtable;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class XMIHandler extends DefaultHandler {
    /**
     * Comment for <code>parser</code>
     */
    private final XMIParser parser;

    private StringBuffer chars = new StringBuffer();

    private ArrayList<UMLClass> clList = new ArrayList<UMLClass>();
    private ArrayList<UMLAttribute> attList = new ArrayList<UMLAttribute>();
    private ArrayList<UMLAssociation> assList = new ArrayList<UMLAssociation>();
    private ArrayList<UMLGeneralization> genList = new ArrayList<UMLGeneralization>();

    private Hashtable<String, UMLClass> clTable = new Hashtable<String, UMLClass>();
    private Hashtable<String, UMLAttribute> attTable = new Hashtable<String, UMLAttribute>();
    private Hashtable<String, ArrayList<SemanticMetadata>> smTable = new Hashtable<String, ArrayList<SemanticMetadata>>();
    private Hashtable<String, String> typeTable = new Hashtable<String, String>();

    private UMLAssociationEdge edge;
    private boolean sourceNavigable = false;
    private boolean targetNavigable = false;
    private String pkg = "";


    public XMIHandler(XMIParser parser) {
        super();
        this.parser = parser;
    }


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        chars.append(ch, start, length);
    }


    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("UML:Package")) {
            int index = pkg.lastIndexOf('.');
            if (index == -1)
                pkg = "";
            else
                pkg = pkg.substring(0, index);
        } else if (qName.equals("UML:Class")) {
            UMLClass cl = clList.get(clList.size() - 1);
            cl.setUmlAttributeCollection(new UMLClassUmlAttributeCollection(attList.toArray(new UMLAttribute[0])));
            attList.clear();
        } else if (qName.equals("UML:Association")) {
            UMLAssociation assoc = assList.get(assList.size() - 1);
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
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        chars.delete(0, chars.length());

        if (qName.equals("UML:Package")) {
            String name = atts.getValue("name");
            if (!name.equals("Logical View") && !name.equals("Logical Model")) {
                if (!pkg.equals(""))
                    pkg += ".";
                pkg += atts.getValue("name");
            }
        } else if (qName.equals("UML:Class")) {
            UMLClass cl = new UMLClass();
            cl.setClassName(atts.getValue("name"));
            cl.setId(atts.getValue("xmi.id"));
            cl.setPackageName(pkg);
            cl.setProjectName(this.parser.projectShortName);
            cl.setProjectVersion(this.parser.projectVersion);
            clList.add(cl);
            clTable.put(cl.getId(), cl);
        } else if (qName.equals("UML:Attribute")) {
            UMLAttribute att = new UMLAttribute();
            att.setName(atts.getValue("name"));
            att.setPublicID(atts.getValue("xmi.id").hashCode());
            att.setVersion(this.parser.attributeVersion);
            attList.add(att);
            attTable.put(String.valueOf(att.getPublicID()), att);
        } else if (qName.equals("UML:Association")) {
            UMLAssociation ass = new UMLAssociation();
            assList.add(ass);
        } else if (qName.equals("UML:AssociationEnd")) {
            UMLAssociation ass = assList.get(assList.size() - 1);
            String type = atts.getValue("type");
            boolean isNavigable = "true".equals(atts.getValue("isNavigable"));

            edge = new UMLAssociationEdge();
            if (ass.getSourceUMLAssociationEdge() == null) {
                ass.setSourceUMLAssociationEdge(new UMLAssociationSourceUMLAssociationEdge(edge));
                sourceNavigable = isNavigable;
            } else {
                ass.setTargetUMLAssociationEdge(new UMLAssociationTargetUMLAssociationEdge(edge));
                targetNavigable = isNavigable;
            }
            edge.setRoleName(atts.getValue("name"));
            edge.setUMLClassReference(new UMLClassReference(atts.getValue("type")));
        } else if (qName.equals("UML:MultiplicityRange") && edge != null) {
            edge.setMinCardinality(Integer.parseInt(atts.getValue("lower")));
            edge.setMaxCardinality(Integer.parseInt(atts.getValue("upper")));
        } else if (qName.equals("UML:Generalization")) {
            UMLGeneralization gen = new UMLGeneralization();
            gen.setSubClassReference(new UMLClassReference(atts.getValue("child")));
            gen.setSuperClassReference(new UMLClassReference(atts.getValue("parent")));
            genList.add(gen);
        } else if (qName.equals("UML:TaggedValue")) {
            String tag = atts.getValue("tag");
            String modelElement = atts.getValue("modelElement");
            String value = atts.getValue("value");

            if (this.parser.debug)
                System.out.print(tag + " on " + modelElement);
            if (tag.startsWith("Property")) {
                modelElement = String.valueOf(modelElement.hashCode());
                if (this.parser.debug)
                    System.out.print(" (" + modelElement + ")");
            }
            if (this.parser.debug)
                System.out.println(" = " + value);

            if (tag.equals("description")) {
                if (clTable.containsKey(modelElement)) {
                    clTable.get(modelElement).setDescription(value);
                } else if (attTable.containsKey(modelElement)) {
                    attTable.get(modelElement).setDescription(value);
                }
            } else if (tag.startsWith("ObjectClassConceptCode")
                || tag.startsWith("ObjectClassQualifierConceptCode") || tag.startsWith("PropertyConceptCode")
                || tag.startsWith("PropertyQualifierConceptCode")) {
                addSemanticMetadata(tag, modelElement, value);
            } else if (tag.startsWith("ObjectClassConceptPreferredName")
                || tag.startsWith("ObjectClassQualifierConceptPreferredName")
                || tag.startsWith("PropertyConceptPreferredName")
                || tag.startsWith("PropertyQualifierConceptPreferredName")) {
                addSemanticMetadata(tag, modelElement, value);
            } else if ((tag.startsWith("ObjectClassConceptDefinition") && !tag
                .startsWith("ObjectClassConceptDefinitionSource"))
                || (tag.startsWith("ObjectClassQualifierConceptDefinition") && !tag
                    .startsWith("ObjectClassQualifierConceptDefinitionSource"))
                || (tag.startsWith("PropertyConceptDefinition") && !tag
                    .startsWith("PropertyConceptDefinitionSource"))
                || (tag.startsWith("PropertyQualifierConceptDefinition") && !tag
                    .startsWith("PropertyQualifierConceptDefinitionSource"))) {
                addSemanticMetadata(tag, modelElement, value);
            }
        } else if (qName.equals("UML:DataType")) {
            typeTable.put(atts.getValue("xmi.id"), atts.getValue("name"));
        } else if (qName.equals("Foundation.Core.Classifier")) {
            attList.get(attList.size() - 1).setDataTypeName(atts.getValue("xmi.idref"));
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

        gov.nih.nci.cagrid.metadata.dataservice.UMLClass[] dataClasses = new gov.nih.nci.cagrid.metadata.dataservice.UMLClass[clList
            .size()];
        int i = 0;
        for (UMLClass commonClass : clList) {
            gov.nih.nci.cagrid.metadata.dataservice.UMLClass dataClass = new gov.nih.nci.cagrid.metadata.dataservice.UMLClass();
            dataClass.setClassName(commonClass.getClassName());
            dataClass.setDescription(commonClass.getDescription());
            dataClass.setId(commonClass.getId());
            dataClass.setPackageName(commonClass.getPackageName());
            dataClass.setProjectName(commonClass.getProjectName());
            dataClass.setProjectVersion(commonClass.getProjectVersion());
            dataClass.setSemanticMetadata(commonClass.getSemanticMetadata());
            dataClass.setUmlAttributeCollection(commonClass.getUmlAttributeCollection());
            dataClass.setAllowableAsTarget(true); // NEW attribute for
                                                    // data classes
            dataClasses[i++] = dataClass;
        }
        this.parser.model.setExposedUMLClassCollection(new DomainModelExposedUMLClassCollection(dataClasses));
        this.parser.model.setExposedUMLAssociationCollection(new DomainModelExposedUMLAssociationCollection(assList
            .toArray(new UMLAssociation[0])));
        this.parser.model.setUmlGeneralizationCollection(new DomainModelUmlGeneralizationCollection(genList
            .toArray(new UMLGeneralization[0])));
    }


    private int getSemanticMetadataOrder(String tag) {
        char c = tag.charAt(tag.length() - 1);
        if (Character.isDigit(c))
            return Integer.parseInt(String.valueOf(c));
        return 0;
    }


    private void addSemanticMetadata(String tag, String modelElement, String value) {
        int order = getSemanticMetadataOrder(tag);

        ArrayList<SemanticMetadata> smList = smTable.get(modelElement);
        if (smList == null)
            smTable.put(modelElement, smList = new ArrayList<SemanticMetadata>(9));

        int size = smList.size();
        if (size <= order) {
            for (int i = smList.size(); i <= order; i++) {
                smList.add(new SemanticMetadata());
            }
        }

        SemanticMetadata sm = smList.get(order);
        if (tag.indexOf("ConceptCode") != -1) {
            sm.setOrder(Integer.valueOf(order));
            sm.setConceptCode(value);
        } else if (tag.indexOf("PreferredName") != -1) {
            sm.setConceptName(value);
        } else if (tag.indexOf("ConceptDefinition") != -1) {
            sm.setConceptDefinition(value);
        }
    }


    private void applySemanticMetadata() {
        for (String id : smTable.keySet()) {
            if (clTable.containsKey(id)) {
                clTable.get(id).setSemanticMetadata(smTable.get(id).toArray(new SemanticMetadata[0]));
            } else if (attTable.containsKey(id)) {
                attTable.get(id).setSemanticMetadata(smTable.get(id).toArray(new SemanticMetadata[0]));
            }
        }
    }


    private void applyDataTypes() {
        for (String id : attTable.keySet()) {
            UMLAttribute att = attTable.get(id);
            String typeRef = att.getDataTypeName();

            String dataType = null;

            // check for class
            if (dataType == null) {
                UMLClass typeCl = clTable.get(typeRef);
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
        Hashtable<String, String> parentTable = new Hashtable<String, String>();
        for (UMLGeneralization gen : genList) {
            parentTable.put(gen.getSubClassReference().getRefid(), gen.getSuperClassReference().getRefid());
        }

        // flatten each cl
        for (String clId : clTable.keySet()) {
            UMLClass cl = clTable.get(clId);
            ArrayList<UMLAttribute> flatAttributes = flattenAttributes(parentTable, clId);
            cl.getUmlAttributeCollection().setUMLAttribute(flatAttributes.toArray(new UMLAttribute[0]));
        }
    }


    private ArrayList<UMLAttribute> flattenAttributes(Hashtable<String, String> parentTable, String clId) {
        if (clId == null)
            return new ArrayList<UMLAttribute>(0);
        ArrayList<UMLAttribute> flat = new ArrayList<UMLAttribute>();

        // my atts
        UMLClass cl = clTable.get(clId);
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
            for (UMLClass cl : clList) {
                if (cl.getPackageName().startsWith("java"))
                    filterSet.add(cl.getId());
            }
        }
        // filter root class
        for (UMLClass cl : clList) {
            if (cl.getPackageName().equals(""))
                filterSet.add(cl.getId());
        }

        // filter classes
        ArrayList<UMLClass> filteredClasses = new ArrayList<UMLClass>(this.clList.size());
        for (UMLClass cl : this.clList) {
            if (!filterSet.contains(cl.getId())) {
                filteredClasses.add(cl);
            }
        }
        this.clList = filteredClasses;

        // filter assocations
        ArrayList<UMLAssociation> filteredAssociations = new ArrayList<UMLAssociation>(this.assList.size());
        for (UMLAssociation ass : this.assList) {
            if (!filterSet.contains(ass.getSourceUMLAssociationEdge().getUMLAssociationEdge()
                .getUMLClassReference().getRefid())
                && !filterSet.contains(ass.getTargetUMLAssociationEdge().getUMLAssociationEdge()
                    .getUMLClassReference().getRefid())) {
                filteredAssociations.add(ass);
            }
        }
        this.assList = filteredAssociations;

        // filter generalizations
        ArrayList<UMLGeneralization> filteredGeneralizations = new ArrayList<UMLGeneralization>(this.genList.size());
        for (UMLGeneralization gen : this.genList) {
            if (!filterSet.contains(gen.getSubClassReference().getRefid())
                && !filterSet.contains(gen.getSuperClassReference().getRefid())) {
                filteredGeneralizations.add(gen);
            }
        }
        this.genList = filteredGeneralizations;
    }
}