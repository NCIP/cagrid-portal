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
/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.domain.catalog.SharedQueryCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.dataservice.Query;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SharedQueryCatalogEntryDao extends AboutCatalogEntryDao<SharedQueryCatalogEntry, Query> {

	@Override
	public SharedQueryCatalogEntry createCatalogAbout(Query domainObject) {
		SharedQueryCatalogEntry entry = isAbout(domainObject);
		if(entry == null){
			entry = new SharedQueryCatalogEntry();
			entry.setAbout(domainObject);
		}
		getHibernateTemplate().save(entry);
		return entry;
	}

	@Override
	public Class domainClass() {
		return SharedQueryCatalogEntry.class;
	}

}
