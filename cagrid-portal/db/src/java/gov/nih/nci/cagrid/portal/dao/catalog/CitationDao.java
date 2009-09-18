package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import gov.nih.nci.cagrid.portal.domain.catalog.Citation;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CitationDao extends AbstractDao<Citation> {

    public CitationDao() {
    }

    /* (non-Javadoc)
    * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
    */
    @Override
    public Class domainClass() {
        return Citation.class;
    }
}