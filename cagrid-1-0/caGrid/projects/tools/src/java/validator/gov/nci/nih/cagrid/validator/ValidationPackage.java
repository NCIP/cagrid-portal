package gov.nci.nih.cagrid.validator;

import gov.nih.nci.cagrid.testing.system.haste.StoryBook;
import gov.nih.nci.cagrid.tests.core.beans.validation.Schedule;

/** 
 *  ValidationPackage
 *  Container class for a deployment validation
 * 
 * @author David Ervin
 * 
 * @created Aug 27, 2007 3:05:03 PM
 * @version $Id: ValidationPackage.java,v 1.2 2007-12-03 16:27:18 hastings Exp $ 
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
