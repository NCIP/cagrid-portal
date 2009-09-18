package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import gov.nih.nci.cagrid.portal.domain.catalog.Rating;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class RatingDao extends AbstractDao<Rating> {

    public RatingDao() {
    }

    /* (non-Javadoc)
    * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
    */
    @Override
    public Class domainClass() {
        return Rating.class;
    }
}