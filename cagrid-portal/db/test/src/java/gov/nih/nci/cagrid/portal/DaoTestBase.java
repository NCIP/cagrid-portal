package gov.nih.nci.cagrid.portal;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;

/**
 * Base class for testing DAO. Will create a Test database
 * but not insert any data.
 *
 * For inserting seed data, use subclass DBTestBase
 *
 * User: kherm
 *
 * @see DBTestBase
 * @author kherm manav.kher@semanticbits.com
 */
public class DaoTestBase<T extends AbstractDao> extends AbstractDBTestBase {
    public T getDao() {
        return (T) getApplicationContext().getBean(getNamingStrategy().substring(0, 1).toLowerCase() + getNamingStrategy().substring(1, getNamingStrategy().indexOf("Test")));
    }

    public String getNamingStrategy() {
        return getClass().getSimpleName();
    }
}
