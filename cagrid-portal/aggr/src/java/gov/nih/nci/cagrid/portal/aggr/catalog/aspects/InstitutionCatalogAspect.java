package gov.nih.nci.cagrid.portal.aggr.catalog.aspects;

import gov.nih.nci.cagrid.portal.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal.dao.catalog.InstitutionCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.Participant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Aspect
public class InstitutionCatalogAspect {

    Log logger = LogFactory.getLog(getClass());
    ParticipantDao participantDao;
    InstitutionCatalogEntryDao institutionCatalogEntryDao;


    /**
     * Aspect will create catalog item when new service is regsitered
     *
     * @param participant
     */
    @AfterReturning("execution(* gov.nih.nci.cagrid.portal.dao.ParticipantDao.save*(gov.nih.nci.cagrid.portal.domain.Participant)) && args(participant)")
    public void onSave(Participant participant) {
        logger.debug("A Pariticpant is being saved. Will create catalog");
        institutionCatalogEntryDao.createCatalogAbout(participant);
    }

    public ParticipantDao getParticipantDao() {
        return participantDao;
    }

    public void setParticipantDao(ParticipantDao participantDao) {
        this.participantDao = participantDao;
    }

    public InstitutionCatalogEntryDao getInstitutionCatalogEntryDao() {
        return institutionCatalogEntryDao;
    }

    public void setInstitutionCatalogEntryDao(InstitutionCatalogEntryDao institutionCatalogEntryDao) {
        this.institutionCatalogEntryDao = institutionCatalogEntryDao;
    }
}
