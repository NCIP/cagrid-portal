package gov.nih.nci.cagrid.portal.util;

import liquibase.exception.LiquibaseException;
import liquibase.spring.SpringLiquibase;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DBUpdater extends SpringLiquibase {

    boolean enable;

    @Override
    public void afterPropertiesSet() throws LiquibaseException {
        if (enable)
            super.afterPropertiesSet();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
