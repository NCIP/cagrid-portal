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
package gov.nih.nci.cagrid.portal.service;

import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryDao;
import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryRelationshipInstanceDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Transactional
public class CatalogEntryService {
	
	
	private long executeTimeout = 5000L;
	
	private CatalogEntryRelationshipInstanceDao catalogEntryRelationshipInstanceDao;
	private CatalogEntryDao catalogEntryDao;
	
	public CatalogEntry getById(int id){
		return getCatalogEntryDao().getById(id);
	}
	
	public Object execute(DoAsUserThread t) throws Exception {
		t.start();
		try {
			t.join(getExecuteTimeout());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (t.getEx() != null) {
			throw t.getEx();
		}
		return t.getReturnValue();
	}
	
	public void deleteCatalogEntry(CatalogEntry catalogEntry){
		CatalogEntry ce = getCatalogEntryDao().getById(catalogEntry.getId());
		getCatalogEntryDao().delete(ce);
	}

    public long getExecuteTimeout() {
		return executeTimeout;
	}

	public void setExecuteTimeout(long executeTimeout) {
		this.executeTimeout = executeTimeout;
	}

	public CatalogEntryRelationshipInstanceDao getCatalogEntryRelationshipInstanceDao() {
		return catalogEntryRelationshipInstanceDao;
	}

	public void setCatalogEntryRelationshipInstanceDao(
			CatalogEntryRelationshipInstanceDao catalogEntryRelationshipInstanceDao) {
		this.catalogEntryRelationshipInstanceDao = catalogEntryRelationshipInstanceDao;
	}

	public CatalogEntryDao getCatalogEntryDao() {
		return catalogEntryDao;
	}

	public void setCatalogEntryDao(CatalogEntryDao catalogEntryDao) {
		this.catalogEntryDao = catalogEntryDao;
	}

}
