/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
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
