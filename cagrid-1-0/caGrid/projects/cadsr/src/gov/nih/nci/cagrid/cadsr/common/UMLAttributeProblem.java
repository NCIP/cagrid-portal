package gov.nih.nci.cagrid.cadsr.common;


/** 
 *  UMLAttributeProblem
 *  Class for problems discovered in caDSR model attributes
 * 
 * @author David Ervin
 * 
 * @created Feb 8, 2008 9:54:09 AM
 * @version $Id: UMLAttributeProblem.java,v 1.1 2008-02-08 16:36:49 dervin Exp $ 
 */
public class UMLAttributeProblem extends ModelProblem {

    private String attributeName = null;
    private String attributeDescription = null;
    private int[] errorIndices = null;
    
    
    public UMLAttributeProblem(String packageName, String className, String attributeName, 
        String attributeDesc, int[] errorIndices) {
        super(packageName, className);
        this.attributeName = attributeName;
        this.attributeDescription = attributeDesc;
        this.errorIndices = errorIndices;
    }
    
    
    public String getAttributeDescription() {
        return attributeDescription;
    }
    
    
    public String getAttributeName() {
        return attributeName;
    }
    

    public int[] getErrorIndices() {
        return errorIndices;
    }
    
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("UMLClassProblem:\n");
        buf.append("\tPackage: ").append(packageName).append("\n");
        buf.append("\tClass: ").append(className).append("\n");
        buf.append("\tAttribute: ").append(attributeName).append("\n");
        buf.append("\tDescription: ").append(attributeDescription).append("\n");
        buf.append("\tErrors at chars: ");
        for (int i = 0; i < errorIndices.length; i++) {
            buf.append(errorIndices[i]);
            if (i + 1 < errorIndices.length) {
                buf.append(", ");
            }
        }
        return buf.toString();
    }
}
