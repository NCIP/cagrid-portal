package gov.nih.nci.cagrid.portal.dao;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 2, 2006
 * Time: 9:11:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class DAOTestSuite {
    public static Test suite() {
        TestSuite dao = new TestSuite();

        dao.addTest(new IndexServiceDAOLocalTestCaseCase());

        return dao;
    }
}
