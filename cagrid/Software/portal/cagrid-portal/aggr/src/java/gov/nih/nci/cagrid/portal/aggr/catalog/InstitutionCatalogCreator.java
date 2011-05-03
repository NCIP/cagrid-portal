package gov.nih.nci.cagrid.portal.aggr.catalog;

import gov.nih.nci.cagrid.portal.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal.dao.catalog.InstitutionCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.Participant;
import gov.nih.nci.cagrid.portal.domain.catalog.InstitutionCatalogEntry;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Transactional
public class InstitutionCatalogCreator extends AbstractCatalogCreator {

    InstitutionCatalogEntryDao institutionCatalogEntryDao;
    ParticipantDao participantDao;

    public void afterPropertiesSet() throws Exception {

        for (Participant p : participantDao.getAll()) {
        	InstitutionCatalogEntry entry = institutionCatalogEntryDao.isAbout(p);
            if (entry == null) {
                logger.debug("Instition catalog not found. Will create for id " + p.getId());
                try {
                    institutionCatalogEntryDao.createCatalogAbout(p);
                } catch (Exception e) {
                    logger.warn("Error cresting Institution catalog for Participant ID " + p.getId() + ". Will skip");
                }
            }else{
            	if(entry.getLocality()==null){
            		institutionCatalogEntryDao.createCatalogAbout(p);
            	}
            }
        }

    }

    public InstitutionCatalogEntryDao getInstitutionCatalogEntryDao() {
        return institutionCatalogEntryDao;
    }

    @Required
    public void setInstitutionCatalogEntryDao(InstitutionCatalogEntryDao institutionCatalogEntryDao) {
        this.institutionCatalogEntryDao = institutionCatalogEntryDao;
    }

    public ParticipantDao getParticipantDao() {
        return participantDao;
    }

    @Required
    public void setParticipantDao(ParticipantDao participantDao) {
        this.participantDao = participantDao;
    }
}
