package gov.nih.nci.cagrid.metadata.xmi;

/** 
 *  XMIConstants
 * 
 * @author David Ervin
 * 
 * @created Oct 22, 2007 10:28:52 AM
 * @version $Id: XMIConstants.java,v 1.1 2007-10-22 14:46:20 dervin Exp $ 
 */
public class XMIConstants {
    // common? constants
    public static final String XMI_NAME_ATTRIBUTE = "name"; // for names of elements
    public static final String XMI_TYPE_ATTRIBUTE = "type"; // types of elements
    public static final String XMI_ID_ATTRIBUTE = "xmi.id"; // for xmi component ids
    
    // association constants
    public static final String XMI_UML_ASSOCIATION = "UML:Association";
    public static final String XMI_UML_ASSOCIATION_END = "UML:AssociationEnd";
    public static final String XMI_UML_ASSOCIATION_IS_NAVIGABLE = "isNavigable";    
    
    // class constants
    public static final String XMI_UML_CLASS = "UML:Class";
    
    // package constants
    public static final String XMI_UML_PACKAGE = "UML:Package";    
    public static final String XMI_LOGICAL_MODEL = "Logical Model";
    public static final String XMI_LOGICAL_VIEW = "Logical View";
    
    // attribute constants
    public static final String XMI_UML_ATTRIBUTE = "UML:Attribute";
    
    // multiplicity (cardinality) constants
    public static final String XMI_UML_MULTIPLICITY_RANGE = "UML:MultiplicityRange";
    public static final String XMI_UML_MULTIPLICITY_LOWER = "lower";
    public static final String XMI_UML_MULTIPLICITY_UPPER = "upper";
    
    // generalization (inheritance) constants
    public static final String XMI_UML_GENERALIZATION = "UML:Generalization";
    public static final String XMI_UML_GENERALIZATION_CHILD = "child";
    public static final String XMI_UML_GENERALIZATION_PARENT = "parent";


    private XMIConstants() {
        // no instantiation
    }
}
