package gov.nci.nih.cagrid.validator;

import java.io.FileInputStream;

import junit.framework.TestResult;
import junit.textui.TestRunner;

import com.atomicobject.haste.framework.StoryBook;

/** 
 *  ValidationRunner
 *  Runs a validation package of tests
 * 
 * @author David Ervin
 * 
 * @created Aug 29, 2007 1:17:37 PM
 * @version $Id: ValidationRunner.java,v 1.1 2007-08-29 18:43:29 dervin Exp $ 
 */
public class ValidationRunner {
    
    private ValidationPackage pack;
    
    public ValidationRunner(ValidationPackage pack) {
        this.pack = pack;
    }
    
    
    public TestResult testNow() {
        StoryBook validationStory = pack.getValidationStoryBook();
        TestRunner runner = new TestRunner();
        TestResult results = runner.doRun(validationStory);
        System.out.flush();
        return results;
    }
    
    
    public void startDaemon() {
        
    }
    

    public static void main(String[] args) {
        try {
            FileInputStream in = new FileInputStream(args[0]);
            ValidationPackage pack = GridDeploymentValidationLoader.loadValidationPackage(in);
            in.close();
            ValidationRunner runner = new ValidationRunner(pack);
            runner.testNow();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
