package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.BaseSpringDataAccessAbstractTestCase;
import gov.nih.nci.cagrid.portal.domain.Operation;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import org.apache.axis.types.URI;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 25, 2006
 * Time: 12:32:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegisteredServiceDAOLocalTest extends BaseSpringDataAccessAbstractTestCase {

    private GridServiceBaseDAO gridServiceBaseDAO;

    public void testDAO() {

        RegisteredService rService = null;
        String serviceName = "Test";
        Integer pk = new Integer(1);
        try {
            rService = new RegisteredService("http://test");

            rService.setPk(pk);
            rService.setName(serviceName);
        } catch (URI.MalformedURIException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        Operation oper = new Operation();
        oper.setName("Test");

        rService.addOperation(oper);
        gridServiceBaseDAO.saveOrUpdate(rService);

        RegisteredService rServiceNew = (RegisteredService) gridServiceBaseDAO.getObjectByPrimaryKey(RegisteredService.class, pk);
        assertEquals(rServiceNew.getName(), serviceName);

        //assertNotNull(rServiceNew.getOperationCollection());


    }

    public void setGridServiceBaseDAO(GridServiceBaseDAO gridServiceBaseDAO) {
        this.gridServiceBaseDAO = gridServiceBaseDAO;
    }

}
