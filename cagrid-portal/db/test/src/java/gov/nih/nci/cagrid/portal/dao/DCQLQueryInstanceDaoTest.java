package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQueryInstance;
import static org.junit.Assert.*;
import org.junit.Test;


/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DCQLQueryInstanceDaoTest  extends QueryInstanceDaoTestBase<DCQLQueryInstanceDao> {

    @Test
    public void  get(){
        GridService fqpService = new GridService();
        fqpService.setUrl("http://fqpservice");

        DCQLQueryInstance loaded = getDao().getById(-2);
        assertNotNull(loaded);
        assertTrue(loaded.getFqpService().getUrl().equals(fqpService.getUrl()));

    }
     @Override
    public String getNamingStrategy() {
        return "dcqlQueryInstanceDaoTest";
    }
}
