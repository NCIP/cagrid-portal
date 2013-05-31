/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
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
