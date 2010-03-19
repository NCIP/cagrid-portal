/**
 * 
 */
package gov.nih.nci.cagrid.portal.service;

import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryDao;
import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryRelationshipInstanceDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRelationshipInstance;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRoleInstance;

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
		deleteRelationships(ce);
		getCatalogEntryDao().delete(ce);
	}
	
	public void deleteRelationships(CatalogEntry catalogEntry) {
		if(catalogEntry == null){
			throw new IllegalArgumentException("CatalogEntry is null.");
		}
		for (CatalogEntryRoleInstance roleInst : catalogEntry.getRoles()) {
			CatalogEntryRelationshipInstance relInst = roleInst
					.getRelationship();
			getCatalogEntryRelationshipInstanceDao().delete(relInst);
		}
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
