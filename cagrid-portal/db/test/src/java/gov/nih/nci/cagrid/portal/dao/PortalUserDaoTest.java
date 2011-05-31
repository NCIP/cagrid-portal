package gov.nih.nci.cagrid.portal.dao;

import static org.junit.Assert.assertNotNull;
import gov.nih.nci.cagrid.portal.DaoTestBase;
import gov.nih.nci.cagrid.portal.domain.PortalUser;

import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalUserDaoTest extends DaoTestBase<PortalUserDao> {

    @Test
    public void create() {
    	String portalId = "123:456";
        PortalUser pu = new PortalUser();
        pu.setPortalId(portalId);
        getDao().save(pu);
        assertNotNull(getDao().getByPortalId(portalId));
    }
    
    @Test
    public void delete(){
    	
    	PortalUser pu = new PortalUser();
    	
    	getDao().save(pu);
    	
    	pu = getDao().getById(pu.getId());
    
    }


}
