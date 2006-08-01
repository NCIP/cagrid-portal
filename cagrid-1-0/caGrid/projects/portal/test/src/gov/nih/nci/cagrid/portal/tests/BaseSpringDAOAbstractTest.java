package gov.nih.nci.cagrid.portal.tests;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;


/**
 * Mock tests base class DAO
 * testing.
 *
 * 
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 31, 2006
 * Time: 10:31:16 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseSpringDAOAbstractTest
        extends AbstractTransactionalDataSourceSpringContextTests {
    protected String[] getConfigLocations() {
        return new String[] {
                "classpath*:/**/applicationContext-data-access-mock.xml",
                "classpath*:/**/applicationContext-data-access.xml",
        };
    }


}


