package gov.nih.nci.cagrid.cadsr.common;


/** 
 *  UMLClassProblem
 *  Class for problems discovered in caDSR model attributes
 * 
 * @author David Ervin
 * 
 * @created Feb 8, 2008 9:54:09 AM
 * @version $Id: UMLClassProblem.java,v 1.1 2008-02-08 16:36:49 dervin Exp $ 
 */
public class UMLClassProblem extends ModelProblem {
    
    protected String classDescription = null;
    protected int[] errorIndices = null;
    
    public UMLClassProblem(String packageName, String className, String classDesc, int[] errorIndices) {
        super(packageName, className);
        this.classDescription = classDesc;
        this.errorIndices = errorIndices;
    }
    
    
    public String getClassDescription() {
        return classDescription;
    }

    
    public int[] getErrorIndices() {
        return errorIndices;
    }
    
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("UMLClassProblem:\n");
        buf.append("\tPackage: ").append(packageName).append("\n");
        buf.append("\tClass: ").append(className).append("\n");
        buf.append("\tDescription: ").append(classDescription).append("\n");
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
