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
package gov.nih.nci.cagrid.portal.dao.catalog;

import org.junit.Test;
import static org.junit.Assert.*;
import gov.nih.nci.cagrid.portal.DaoTestBase;
import gov.nih.nci.cagrid.portal.domain.catalog.CommunityCatalogEntry;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CommunityCatalogEntryDaoTest extends DaoTestBase<CommunityCatalogEntryDao> {

    @Test
    public void save(){
        CommunityCatalogEntry community = new CommunityCatalogEntry();
        community.setCommunityUrl("http://");
        getDao().save(community);

        interruptSession();

        assertNotNull(getDao().getAll());
        assertNotNull(getDao().getAll().get(0).getCommunityUrl());



    }
}
