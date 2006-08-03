package gov.nih.nci.cagrid.portal.domain;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 2, 2006
 * Time: 9:28:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class DomainTestSuite {
    public static Test suite() {
        TestSuite domain = new TestSuite();
        domain.addTestSuite(IndexServiceTestCase.class);

        return domain;
    }
}
