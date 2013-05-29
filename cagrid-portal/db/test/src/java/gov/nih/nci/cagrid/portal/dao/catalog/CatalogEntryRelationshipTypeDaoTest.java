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

import static org.junit.Assert.*;
import gov.nih.nci.cagrid.portal.DaoTestBase;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRelationshipType;
import gov.nih.nci.cagrid.portal.util.DefaultCatalogEntryRelationshipTypesFactory;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class CatalogEntryRelationshipTypeDaoTest extends
		DaoTestBase<CatalogEntryRelationshipTypeDao> {

	@Before
	public void init() {
		DefaultCatalogEntryRelationshipTypesFactory b = (DefaultCatalogEntryRelationshipTypesFactory) getApplicationContext()
				.getBean("defaultCatalogEntryRelationshipTypesFactory");
		b.init();
	}

	@Test
	public void testDefaultRelationshipTypes() {

		List<CatalogEntryRelationshipType> relTypes = getDao().getAll();
		assertTrue(relTypes.size() > 0);
	}

}
