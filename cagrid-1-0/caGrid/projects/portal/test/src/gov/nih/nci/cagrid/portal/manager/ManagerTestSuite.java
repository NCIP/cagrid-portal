package gov.nih.nci.cagrid.portal.manager;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 2, 2006
 * Time: 9:11:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class ManagerTestSuite {
    public static Test suite() {
        TestSuite manager = new TestSuite();

        manager.addTestSuite(IndexServiceManagerLocalTestCase.class);
        manager.addTestSuite(RegisteredServiceLocalTestCase.class);


        return manager;
    }
}
