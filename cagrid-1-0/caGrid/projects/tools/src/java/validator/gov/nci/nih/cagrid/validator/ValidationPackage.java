package gov.nci.nih.cagrid.validator;

import gov.nih.nci.cagrid.tests.core.beans.validation.Schedule;

import com.atomicobject.haste.framework.StoryBook;

/** 
 *  ValidationPackage
 *  Container class for a deployment validation
 * 
 * @author David Ervin
 * 
 * @created Aug 27, 2007 3:05:03 PM
 * @version $Id: ValidationPackage.java,v 1.1 2007-11-26 17:09:10 dervin Exp $ 
 */
public class ValidationPackage {

    private StoryBook validationStoryBook;
    private Schedule validationSchedule;
    
    public ValidationPackage(StoryBook validationStoryBook, Schedule validationSchedule) {
        this.validationStoryBook = validationStoryBook;
        this.validationSchedule = validationSchedule;
    }
    
    
    public StoryBook getValidationStoryBook() {
        return validationStoryBook;
    }
    
    
    public Schedule getValidationSchedule() {
        return validationSchedule;
    }
}
