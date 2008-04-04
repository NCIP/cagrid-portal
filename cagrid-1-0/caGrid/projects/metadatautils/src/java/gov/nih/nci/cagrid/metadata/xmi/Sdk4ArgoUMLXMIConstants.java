package gov.nih.nci.cagrid.metadata.xmi;


/** 
 *  XMIConstants specific to SDK 4 EA models
 * 
 * @author David Ervin
 * 
 * @created Oct 22, 2007 10:28:52 AM
 * @version $Id: Sdk4ArgoUMLXMIConstants.java,v 1.1 2008-04-04 15:57:41 dervin Exp $ 
 */
public class Sdk4ArgoUMLXMIConstants {
    // package naming constants
    public static final String LOGICAL_VIEW_PACKAGE_NAME = "Logical View";
    public static final String LOGICAL_MODEL_PACKAGE_NAME = "Logical Model";

    // generalization constants
    public static final String XMI_UML_GENERALIZATION_CHILD = XMIConstants.XMI_UML_GENERALIZATION + ".child";
    public static final String XMI_UML_GENERALIZATION_PARENT = XMIConstants.XMI_UML_GENERALIZATION + ".parent";
    
    // classifier constants
    public static final String XMI_UML_CLASSIFIER = "UML:Classifier";

    private Sdk4ArgoUMLXMIConstants() {
        // no instantiation
    }
}
