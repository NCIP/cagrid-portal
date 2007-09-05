package gov.nci.nih.cagrid.validator;

import gov.nci.nih.cagrid.validator.steps.AbstractBaseServiceTestStep;
import gov.nci.nih.cagrid.validator.steps.base.DeleteTempDirStep;
import gov.nci.nih.cagrid.validator.steps.base.TestServiceMetaData;
import gov.nci.nih.cagrid.validator.steps.base.TestServiceUpStep;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.tests.core.beans.validation.ServiceDescription;
import gov.nih.nci.cagrid.tests.core.beans.validation.ServiceTestStep;
import gov.nih.nci.cagrid.tests.core.beans.validation.ServiceType;
import gov.nih.nci.cagrid.tests.core.beans.validation.ValidationDescription;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
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
 * @version $Id: GridDeploymentValidationLoader.java,v 1.5 2007-09-05 17:01:35 dervin Exp $ 
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
            Story serviceStory = createStoryForService(service, desc);
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
    
    
    private static Story createStoryForService(final ServiceDescription service, final ValidationDescription desc) throws Exception {
        String testName = service.getServiceName() + " validation tests";
        String testDescription = "Tests for " + service.getServiceName() 
            + " @ " + service.getServiceUrl().toString();
        
        Vector<Step> setUp = createSetupStepsForService(service);
        final Vector<Step> tests = createStepsForServiceType(service, desc);
        Vector<Step> tearDown = createTearDownStepsForService(service);
        
        ServiceValidationStory serviceStory = new ServiceValidationStory(
            testName, testDescription, setUp, tearDown) {
            // Haste Story's constructor calls steps() right away, and stores
            // the result internally, and iterates over THAT vector when running
            // steps.  Hence, passing the vector to a constructor of the ServiceValidationStory,
            // storing it in an instance variable, and returning it when steps() is called
            // DOESN'T WORK.
            public Vector steps() {
                return tests;
            }
        };
        return serviceStory;
    }
    
    
    private static Vector<Step> createSetupStepsForService(final ServiceDescription service) {
        Vector<Step> setup = new Vector();
        setup.add(new TestServiceUpStep(service.getServiceUrl().toString()));
        setup.add(new TestServiceMetaData(service.getServiceUrl().toString()));
        return setup;
    }
    
    
    private static Vector<Step> createStepsForServiceType(ServiceDescription service, ValidationDescription desc) 
        throws ValidationLoadingException {
        Vector<Step> steps = new Vector();
        Class[] constructorArgTypes = new Class[] {String.class, File.class};
        String serviceTypeName = service.getServiceType();
        for (ServiceType type : desc.getServiceType()) {
            if (type.getTypeName().equals(serviceTypeName)) {
                for (ServiceTestStep testStep : type.getTestStep()) {
                    String classname = testStep.getClassname();
                    Class stepClass = null;
                    try {
                        stepClass = Class.forName(classname);
                    } catch (ClassNotFoundException ex) {
                        throw new ValidationLoadingException(
                            "Could not service type " + serviceTypeName + " test class " 
                            + classname + ": " + ex.getMessage(), ex);
                    }
                    if (!AbstractBaseServiceTestStep.class.isAssignableFrom(stepClass)) {
                        throw new ValidationLoadingException(
                            "The service type " + serviceTypeName + " test class " +
                            classname + " does not extend " + AbstractBaseServiceTestStep.class.getName());
                    }
                    Object[] constructorArgs = {
                        service.getServiceUrl(),
                        getTempDirForService(service.getServiceName())
                    };
                    try {
                        Constructor stepConstructor = stepClass.getConstructor(constructorArgTypes);
                        AbstractBaseServiceTestStep step = (AbstractBaseServiceTestStep) 
                            stepConstructor.newInstance(constructorArgs);
                        steps.add(step);
                    } catch (Exception ex) {
                        throw new ValidationLoadingException(
                            "Service type " + serviceTypeName + " test class " + classname 
                            + " could not be created: " + ex.getMessage(), ex);
                    }
                }
            }
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
            dir = serviceTemp;
        }
        return dir;
    }
}
