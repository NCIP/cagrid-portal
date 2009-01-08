package org.cagrid.data.styles.cacore41.test;

import org.cagrid.data.test.creation.DataTestCaseInfo;

/** 
 *  SDK41ServiceStyleSystemTestConstants
 *  Constants for the SDK 4.1 Data Service style system tests
 * 
 * @author David Ervin
 * 
 * @created Feb 1, 2008 7:58:01 AM
 * @version $Id: SDK41ServiceStyleSystemTestConstants.java,v 1.1 2009-01-08 21:31:20 dervin Exp $ 
 */
public class SDK41ServiceStyleSystemTestConstants {
    // the service style's internal name
    public static final String STYLE_NAME = "caCORE SDK v 4.1";
    
    // system property to locate the Introduce base directory
    public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";

    // test service naming
    public static final String SERVICE_PACKAGE = "org.cagrid.sdkquery41.test";
    public static final String SERVICE_NAME = "TestSDK41StyleDataService";
    public static final String SERVICE_NAMESPACE = "http://" + SERVICE_PACKAGE + "/" + SERVICE_NAME;
    
    public static DataTestCaseInfo SERVICE_TEST_CASE_INFO = 
        new SDK41TestServiceInfo();
    
    private static class SDK41TestServiceInfo extends DataTestCaseInfo {
        public String getServiceDirName() {
            return SDK41ServiceStyleSystemTestConstants.SERVICE_NAME;
        }


        public String getName() {
            return SDK41ServiceStyleSystemTestConstants.SERVICE_NAME;
        }


        public String getNamespace() {
            return SDK41ServiceStyleSystemTestConstants.SERVICE_NAMESPACE;
        }


        public String getPackageName() {
            return SDK41ServiceStyleSystemTestConstants.SERVICE_PACKAGE;
        }


        public String getExtensions() {
            return "cagrid_metadata," + super.getExtensions();
        }
    }
}