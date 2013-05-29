/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.liferay.utils;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupServiceUtil;
import com.liferay.portal.service.ServiceContext;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Will create a community in Liferay, based on a templated community
 * <p/>
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CommunityCreator {
    private LiferayLoginUtil liferayLoginUtil;

    private Log logger = LogFactory.getLog(getClass());

    public Group create(CatalogEntry ce) throws SystemException, PortalException {

        logger.info("Will create a community for Catalog Entry");
        String name = ce.getName();
        String description = ce.getDescription();
        int type = 1;
        String friendlyURL = "";
        boolean active = true;

        try {
            liferayLoginUtil.masqueradeOmniUser();
        } catch (Exception e) {
            throw new SystemException("Could not masquerade as super user. WIll not create community", e);
        }
        // Add group
        return GroupServiceUtil.addGroup(
                name, description, type, friendlyURL, active, new ServiceContext());

    }


    public LiferayLoginUtil getLiferayLoginUtil() {
        return liferayLoginUtil;
    }

    public void setLiferayLoginUtil(LiferayLoginUtil liferayLoginUtil) {
        this.liferayLoginUtil = liferayLoginUtil;
    }
}


 
