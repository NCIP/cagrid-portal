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

import gov.nih.nci.cagrid.portal.DBTestBase;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import static org.junit.Assert.*;
import org.junit.Test;


/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class IndexServiceDaoTest extends DBTestBase<IndexServiceDao> {


    @Test
    public void get() {
        IndexService idxById = getDao().getById(-1);

        IndexService idxByUrl = getDao().getIndexServiceByUrl("http://index1");

        assertEquals(idxById, idxByUrl);

        IndexService idxByUrl2 = getDao().getIndexServiceByUrl("http://index2");
        assertNotNull(idxByUrl2.getServices());


    }


}
