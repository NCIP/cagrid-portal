/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.PortalTestUtils;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Date;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CQLQueryInstanceDaoTest extends QueryInstanceDaoTestBase<CQLQueryInstanceDao> {

     @Test
    public void error(){
        CQLQueryInstance instance = new CQLQueryInstance();

        instance.setError(PortalTestUtils.createReallyLongString(4000));
        getDao().save(instance);
        assertNotNull(instance);

    }

    @Test
    public void save(){
        CQLQueryInstance instance = new CQLQueryInstance();
        instance.setCreateTime(new Date());

        getDao().save(instance);

        assertEquals(instance.getCreateTime(),((CQLQueryInstance)(getDao().getByExample(instance))).getCreateTime());


    }

    @Test
    public void search(){
        CQLQueryInstance loaded = getDao().getById(-1);
        GridDataService dataService = loaded.getDataService();

        assertTrue(getDao().getByDataService(dataService).contains(loaded));

    }

    @Override
    public String getNamingStrategy() {
        return "cqlQueryInstanceDaoTest";
    }
}



