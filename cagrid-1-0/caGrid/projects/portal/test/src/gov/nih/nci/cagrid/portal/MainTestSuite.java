package gov.nih.nci.cagrid.portal;

import gov.nih.nci.cagrid.portal.domain.DomainTestSuite;
import gov.nih.nci.cagrid.portal.manager.ManagerTestSuite;
import gov.nih.nci.cagrid.portal.utils.UtilsTestSuite;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 2, 2006
 * Time: 9:07:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class MainTestSuite {
    public static Test suite() {
        TestSuite main = new TestSuite();

        main.addTest(UtilsTestSuite.suite());
        main.addTest(DomainTestSuite.suite());
        main.addTest(ManagerTestSuite.suite());

        return main;
    }
}
