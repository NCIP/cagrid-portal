package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import gov.nih.nci.cagrid.portal.domain.catalog.Hyperlink;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class HyperlinkDao extends AbstractDao<Hyperlink> {

    public HyperlinkDao() {
    }

    /* (non-Javadoc)
    * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
    */
    @Override
    public Class domainClass() {
        return Hyperlink.class;
    }
}