package org.cagrid.data.test.system;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

import org.cagrid.data.test.creation.DataTestCaseInfo;

/** 
 *  AddTestingJarToServiceStep
 *  Adds the testing jar to a grid service's libraries
 * 
 * @author David Ervin
 * 
 * @created May 21, 2008 4:07:21 PM
 * @version $Id: AddTestingJarToServiceStep.java,v 1.1 2008-05-22 16:30:02 dervin Exp $ 
 */
public class AddTestingJarToServiceStep extends Step {
    
    public static final String DATA_TESTS_JAR_PREFIX = "DataTests-";

    private DataTestCaseInfo info;
    
    public AddTestingJarToServiceStep(DataTestCaseInfo info) {
        this.info = info;
    }
    
    
    public void runStep() throws Throwable {
        File libDir = new File(info.getDir(), "lib");
        // find the testing jar
        File dataTestsJar = null;
        assertTrue("Class loader was not a URLClassLoader", getClass().getClassLoader() instanceof URLClassLoader);
        URLClassLoader currentClassLoader = (URLClassLoader) getClass().getClassLoader();
        URL[] urls = currentClassLoader.getURLs();
        for (URL u : urls) {
            URI uri = u.toURI();
            if (uri.getScheme().equalsIgnoreCase("file")) {
                // is a file
                File classpathFile = new File(uri);
                String name = classpathFile.getName();
                if (name.endsWith(".jar") && name.startsWith(DATA_TESTS_JAR_PREFIX)) {
                    // found the data tests jar file
                    dataTestsJar = classpathFile;
                    break;
                }
            }
        }
        assertNotNull("Could not locate DataTests jar on classpath", dataTestsJar);
        // copy the jar to the service's lib dir
        File libOut = new File(libDir, dataTestsJar.getName());
        Utils.copyFile(dataTestsJar, libOut);
    }
}
