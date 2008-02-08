package gov.nih.nci.cagrid.cadsr.common;

/** 
 *  ModelProblems
 *  TODO:DOCUMENT ME
 * 
 * @author David Ervin
 * 
 * @created Feb 8, 2008 10:11:23 AM
 * @version $Id: ModelProblem.java,v 1.1 2008-02-08 16:36:48 dervin Exp $ 
 */
public abstract class ModelProblem {

    protected String packageName = null;
    protected String className = null;
    
    
    public ModelProblem(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }
    

    public String getClassName() {
        return className;
    }


    public String getPackageName() {
        return packageName;
    }
}
