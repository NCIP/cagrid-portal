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
package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CommunityCatalogEntry;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CommunityCatalogEntryDao extends AbstractDao<CommunityCatalogEntry> {

    @Override
    public Class domainClass() {
        return CommunityCatalogEntry.class;
    }

    public boolean isUnique(String name) {
        CommunityCatalogEntry entry = new CommunityCatalogEntry();
        entry.setName(name);

        return getByExample(entry) == null;

    }
}
