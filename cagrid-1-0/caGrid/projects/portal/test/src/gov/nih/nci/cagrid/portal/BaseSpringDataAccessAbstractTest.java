package gov.nih.nci.cagrid.portal;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import java.util.HashSet;


/**
 * Mock tests base class DAO
 * testing.
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 31, 2006
 * Time: 10:31:16 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseSpringDataAccessAbstractTest
        extends AbstractTransactionalDataSourceSpringContextTests {
    protected HashSet rootIndexSet = new HashSet();
    public EndpointReferenceType validServiceEPR;


    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction(); //To change body of overridden methods use File | Settings | File Templates.
        setAutowireMode(AUTOWIRE_BY_NAME);
        //rootIndexSet.add("http://cagrid01.bmi.ohio-state.edu:8080/wsrf/services/DefaultIndexService");
        rootIndexSet.add(
                "http://cagrid04.bmi.ohio-state.edu:7080/wsrf/services/DefaultIndexService");
        validServiceEPR = new EndpointReferenceType(new URI("http://cagrid04.bmi.ohio-state.edu:7080/wsrf/services/cagrid/CaDSRService"));
    }

    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:/**/applicationContext-utils.xml",
                "classpath*:/**/applicationContext-data-access-mock.xml",
                "classpath*:/**/applicationContext-data-access.xml",
        };
    }
}
