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
import gov.nih.nci.cagrid.portal.DaoTestBase;
import gov.nih.nci.cagrid.portal.domain.IdentityProvider;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class IdentityProviderDaoTest extends DaoTestBase<IdentityProviderDao> {

    @Test
    public void create() {
        IdentityProvider idp = new IdentityProvider();
        idp.setLabel("");
        idp.setUrl("");
        getDao().save(idp);

        assertNotNull(getDao().getAll());
    }


}
