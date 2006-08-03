package gov.nih.nci.cagrid.portal.utils;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 2, 2006
 * Time: 10:34:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class UtilsTestSuite {
    public static Test suite() {
        TestSuite manager = new TestSuite();

        manager.addTestSuite(InitBeanTestCase.class);
        manager.addTestSuite(GridUtilsTestCase.class);

        return manager;
    }
}
