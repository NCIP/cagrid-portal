package gov.nih.nci.cagrid.metadata.xmi;

import gov.nih.nci.cagrid.metadata.common.SemanticMetadata;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLAssociationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLClassCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelUmlGeneralizationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLGeneralization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/** 
 *  BaseXMIHandler
 *  Base class to organize XMI handlers
 * 
 * @author David Ervin
 * 
 * @created Apr 22, 2008 3:44:03 PM
 * @version $Id: BaseXMIHandler.java,v 1.2 2008-04-22 20:35:19 dervin Exp $ 
 */
public abstract class BaseXMIHandler extends DefaultHandler {

    // parser contains configuration options and information for the handler
    private XMIParser parser;

    private StringBuffer chars;

    // lists of domain model components
    private List<UMLClass> classList;
    private List<UMLAttribute> attribList;
    private List<UMLAssociation> assocList;
    private List<UMLGeneralization> generalizationList;
    
    // maps from XMI name to domain model component
    private Map<String, UMLClass> classTable; // class ID to class instance
    private Map<String, UMLAttribute> attribTable; // attribute ID to attribute instance
    private Map<String, List<SemanticMetadata>> semanticMetadataTable; // element ID to semantic metadata list
    private Map<String, String> typeTable; // type ID to type name
    
    public BaseXMIHandler(XMIParser parser) {
        super();
        this.parser = parser;
        this.chars = new StringBuffer();
        // initialize lists
        this.classList = new ArrayList<UMLClass>();
        this.attribList = new ArrayList<UMLAttribute>();
        this.assocList = new ArrayList<UMLAssociation>();
        this.generalizationList = new ArrayList<UMLGeneralization>();
        // initialize tables
        this.classTable = new HashMap<String, UMLClass>();
        this.attribTable = new HashMap<String, UMLAttribute>();
        this.semanticMetadataTable = new HashMap<String, List<SemanticMetadata>>();
        this.typeTable = new HashMap<String, String>();
    }


    public List<UMLAssociation> getAssocList() {
        return assocList;
    }


    public List<UMLAttribute> getAttribList() {
        return attribList;
    }


    public Map<String, UMLAttribute> getAttribTable() {
        return attribTable;
    }


    public StringBuffer getChars() {
        return chars;
    }


    public List<UMLClass> getClassList() {
        return classList;
    }


    public Map<String, UMLClass> getClassTable() {
        return classTable;
    }


    public List<UMLGeneralization> getGeneralizationList() {
        return generalizationList;
    }


    public XMIParser getParser() {
        return parser;
    }


    public Map<String, List<SemanticMetadata>> getSemanticMetadataTable() {
        return semanticMetadataTable;
    }


    public Map<String, String> getTypeTable() {
        return typeTable;
    }


    public void characters(char[] ch, int start, int length) throws SAXException {
        chars.append(ch, start, length);
    }
    
    
    public void clearChars() {
        chars.delete(0, chars.length());
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
            new DomainModelUmlGeneralizationCollection(generalizationList.toArray(new UMLGeneralization[0])));
    }
    
    
//  -------------------------------
    // end of document cleanup tooling
    // -------------------------------
    
    
    private void applySemanticMetadata() {
        for (String id : semanticMetadataTable.keySet()) {
            if (classTable.containsKey(id)) {
                classTable.get(id).setSemanticMetadata(
                    semanticMetadataTable.get(id).toArray(new SemanticMetadata[0]));
            } else if (attribTable.containsKey(id)) {
                attribTable.get(id).setSemanticMetadata(
                    semanticMetadataTable.get(id).toArray(new SemanticMetadata[0]));
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
        for (UMLGeneralization gen : generalizationList) {
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
        // build set of class IDs to filter out
        // using IDs rather than names directory so they can then be used 
        // to remove associations involving the filtered classes
        HashSet<String> filterSet = new HashSet<String>();
        
        for (UMLClass clazz : classList) {
            String pack = clazz.getPackageName();
            if (this.parser.filterPrimitiveClasses && pack.startsWith("java")) {
                // filter primtives
                filterSet.add(clazz.getId());
            } else if (pack.startsWith("ValueDomain")) {
                // filter ValueDomain package
                filterSet.add(clazz.getId());
            } else if (pack.equals("")) {
                filterSet.add(clazz.getId());
            }
        }

        // filter class list
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
            new ArrayList<UMLGeneralization>(this.generalizationList.size());
        for (UMLGeneralization gen : this.generalizationList) {
            if (!filterSet.contains(gen.getSubClassReference().getRefid())
                && !filterSet.contains(gen.getSuperClassReference().getRefid())) {
                filteredGeneralizations.add(gen);
            }
        }
        this.generalizationList = filteredGeneralizations;
    }
}
