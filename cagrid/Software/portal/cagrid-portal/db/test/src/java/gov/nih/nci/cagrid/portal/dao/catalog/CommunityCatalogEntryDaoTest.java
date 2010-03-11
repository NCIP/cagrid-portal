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
