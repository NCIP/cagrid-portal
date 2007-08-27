package gov.nci.nih.cagrid.validator;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.tests.core.beans.validation.ServiceDescription;
import gov.nih.nci.cagrid.tests.core.beans.validation.ValidationDescription;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import com.atomicobject.haste.framework.Story;
import com.atomicobject.haste.framework.StoryBook;

/** 
 *  GridDeploymentValidationLoader
 *  Loader utility for grid deploymentm validation tests
 * 
 * @author David Ervin
 * 
 * @created Aug 27, 2007 3:04:08 PM
 * @version $Id: GridDeploymentValidationLoader.java,v 1.1 2007-08-27 19:43:06 dervin Exp $ 
 */
public class GridDeploymentValidationLoader {
    
    public static final String VALIDATION_NAMESPACE = "http://gov.nih.nci.cagrid.tests.core.validation/ValidationDescription";
    public static final String VALIDATION_DESCRIPTION_ELEMENT = "ValidationDescription";
    public static final QName VALIDATION_DESCRIPTION_QNAME = 
        new QName(VALIDATION_NAMESPACE, VALIDATION_DESCRIPTION_ELEMENT);

    public static ValidationPackage loadValidationPackage(InputStream validationDescriptionStream) throws Exception {
        // load the validation description
        InputStreamReader descReader = new InputStreamReader(validationDescriptionStream);
        ValidationDescription desc = (ValidationDescription) 
            Utils.deserializeObject(descReader, ValidationDescription.class);
        descReader.close();
        
        final List<Story> serviceStories = new ArrayList(desc.getServiceDescription().length);
        for (ServiceDescription service : desc.getServiceDescription()) {
            Story serviceStory = createStoryForService(service);
            serviceStories.add(serviceStory);
        }
        
        StoryBook validationStoryBook = new StoryBook() {
            protected void stories() {
                for (Story story : serviceStories) {
                    addStory(story);
                }
            }
        };
        
        return new ValidationPackage(validationStoryBook, desc.getSchedule());
    }
    
    
    private static Story createStoryForService(ServiceDescription service) throws Exception {
        // TODO: implement me
        return null;
    }
}
