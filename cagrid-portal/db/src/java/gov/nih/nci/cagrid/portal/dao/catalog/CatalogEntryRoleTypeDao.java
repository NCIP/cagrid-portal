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

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRoleType;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class CatalogEntryRoleTypeDao extends AbstractDao<CatalogEntryRoleType> {

	@Override
	public Class domainClass() {
		return CatalogEntryRoleType.class;
	}

}
