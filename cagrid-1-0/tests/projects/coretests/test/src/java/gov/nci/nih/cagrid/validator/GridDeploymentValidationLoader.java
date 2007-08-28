package gov.nci.nih.cagrid.validator;

import gov.nci.nih.cagrid.validator.steps.base.DeleteTempDirStep;
import gov.nci.nih.cagrid.validator.steps.base.TestServiceMetaData;
import gov.nci.nih.cagrid.validator.steps.base.TestServiceUpStep;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.tests.core.beans.validation.ServiceDescription;
import gov.nih.nci.cagrid.tests.core.beans.validation.ServiceType;
import gov.nih.nci.cagrid.tests.core.beans.validation.ValidationDescription;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.namespace.QName;

import com.atomicobject.haste.framework.Step;
import com.atomicobject.haste.framework.Story;
import com.atomicobject.haste.framework.StoryBook;

/** 
 *  GridDeploymentValidationLoader
 *  Loader utility for grid deploymentm validation tests
 * 
 * @author David Ervin
 * 
 * @created Aug 27, 2007 3:04:08 PM
 * @version $Id: GridDeploymentValidationLoader.java,v 1.2 2007-08-28 16:03:13 dervin Exp $ 
 */
public class GridDeploymentValidationLoader {
    
    public static final String VALIDATION_NAMESPACE = "http://gov.nih.nci.cagrid.tests.core.validation/ValidationDescription";
    public static final String VALIDATION_DESCRIPTION_ELEMENT = "ValidationDescription";
    public static final QName VALIDATION_DESCRIPTION_QNAME = 
        new QName(VALIDATION_NAMESPACE, VALIDATION_DESCRIPTION_ELEMENT);
    
    private static Map<String, File> tempDirsForServices = new HashMap();

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
    
    
    private static Story createStoryForService(final ServiceDescription service) throws Exception {
        String testName = service.getServiceName() + " validation tests";
        String testDescription = "Tests for " + service.getServiceName() 
            + " @ " + service.getServiceUrl().toString();
        
        Vector<Step> setUp = createSetupStepsForService(service);
        Vector<Step> tests = createStepsForServiceType(service);
        Vector<Step> tearDown = createTearDownStepsForService(service);
        
        ServiceValidationStory serviceStory = new ServiceValidationStory(testName, testDescription,
            setUp, tests, tearDown);
        return serviceStory;
    }
    
    
    private static Vector<Step> createSetupStepsForService(final ServiceDescription service) {
        Vector<Step> setup = new Vector();
        setup.add(new TestServiceUpStep(service.getServiceUrl().toString()));
        setup.add(new TestServiceMetaData(service.getServiceUrl().toString()));
        return setup;
    }
    
    
    private static Vector<Step> createStepsForServiceType(ServiceDescription service) {
        Vector<Step> steps = new Vector();        
        // would be nice if I could use a switch here
        if (service.getServiceType().equals(ServiceType.GME)) {
            
        }
        return steps;
    }
    
    
    private static Vector<Step> createTearDownStepsForService(final ServiceDescription service) {
        Vector<Step> teardown = new Vector();
        teardown.add(new DeleteTempDirStep(getTempDirForService(service.getServiceName())));
        return teardown;
    }
    
    
    private static File getTempDirForService(String serviceName) {
        File dir = tempDirsForServices.get(serviceName);
        if (dir == null) {
            File tempDir = new File("tmp").getAbsoluteFile();
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }
            File serviceTemp = new File(tempDir.getAbsolutePath() + File.separator + serviceName);
            serviceTemp.mkdirs();
            tempDirsForServices.put(serviceName, serviceTemp);
        }
        return dir;
    }
}
