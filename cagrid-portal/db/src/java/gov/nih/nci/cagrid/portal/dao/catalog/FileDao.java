package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import gov.nih.nci.cagrid.portal.domain.catalog.File;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class FileDao extends AbstractDao<File> {

    public FileDao() {
    }

    /* (non-Javadoc)
    * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
    */
    @Override
    public Class domainClass() {
        return File.class;
    }
}
