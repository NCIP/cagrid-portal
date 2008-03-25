package gov.nih.nci.cagrid.validator;

import gov.nih.nci.cagrid.testing.system.haste.StoryBook;
import gov.nih.nci.cagrid.tests.core.beans.validation.Schedule;

/** 
 *  ValidationPackage
 *  Container class for a deployment validation
 * 
 * @author David Ervin
 * 
 * @created Aug 27, 2007 3:05:03 PM
 * @version $Id: ValidationPackage.java,v 1.1 2008-03-25 14:20:30 dervin Exp $ 
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
