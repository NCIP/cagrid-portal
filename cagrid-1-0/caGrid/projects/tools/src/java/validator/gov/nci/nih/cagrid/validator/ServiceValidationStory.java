package gov.nci.nih.cagrid.validator;

import java.util.Vector;

import com.atomicobject.haste.framework.Step;
import com.atomicobject.haste.framework.Story;

/** 
 *  ServiceValidationStory
 *  Story for service validation
 * 
 * @author David Ervin
 * 
 * @created Aug 28, 2007 10:33:18 AM
 * @version $Id: ServiceValidationStory.java,v 1.1 2007-11-26 17:09:10 dervin Exp $ 
 */
public abstract class ServiceValidationStory extends Story {
    
    private String name;
    private String description;
    private Vector<Step> setUpSteps;
    private Vector<Step> tearDownSteps;

    public ServiceValidationStory(String name, String desc, 
        final Vector<Step> setUp, final Vector<Step> tearDown) {
        this.name = name;
        this.description = desc;        
        this.setUpSteps = setUp;
        this.tearDownSteps = tearDown;
    }
    
    
    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }
    
    
    protected boolean storySetUp() throws Throwable {
        if (setUpSteps != null) {
            for (Step step : setUpSteps) {
                step.runStep();
            }
        }
        return true;
    }
    
    
    protected void storyTearDown() throws Throwable {
        super.storyTearDown();
        if (tearDownSteps != null) {
            for (Step step : tearDownSteps) {
                step.runStep();
            }
        }
    }
    
    
    // used to make sure that if we are going to use a junit testsuite to 
    // test this that the test suite will not error out 
    // looking for a single test......
    public void testDummy() throws Throwable {
    }
}
