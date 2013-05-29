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
/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.dir;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryType;
import gov.nih.nci.cagrid.portal.util.filter.ServiceFilter;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class ServiceDirectory extends DiscoveryDirectory {


    private ServiceDirectoryType serviceDirectoryType;
    private GridServiceDao gridServiceDao;
    private ServiceFilter servicefilter;

    /**
     *
     */
    public ServiceDirectory() {
        setType(DiscoveryType.SERVICE);
    }

    /* (non-Javadoc)
      * @see gov.nih.nci.cagrid.portal.portlet.discovery.dir.DiscoveryDirectory#getObjects()
      */
    @Override
    public List getObjects() {
        List<GridService> objects = null;

        if (ServiceDirectoryType.ALL.equals(getServiceDirectoryType())) {
            objects = getGridServiceDao().getAll();
        } else if (ServiceDirectoryType.DATA.equals(getServiceDirectoryType())) {
            objects = getGridServiceDao().getAllDataServices();
        } else if (ServiceDirectoryType.ANALYTICAL.equals(getServiceDirectoryType())) {
            objects = getGridServiceDao().getAllAnalyticalServices();
        } else {
            throw new CaGridPortletApplicationException("Unknown ServiceDirectoryType: " + getServiceDirectoryType());
        }

        objects = servicefilter.filter(objects);

        return objects;
    }

    @Required
    public ServiceDirectoryType getServiceDirectoryType() {
        return serviceDirectoryType;
    }

    public void setServiceDirectoryType(ServiceDirectoryType serviceDirectoryType) {
        this.serviceDirectoryType = serviceDirectoryType;
    }

    @Required
    public GridServiceDao getGridServiceDao() {
        return gridServiceDao;
    }

    public void setGridServiceDao(GridServiceDao gridServiceDao) {
        this.gridServiceDao = gridServiceDao;
    }

    public ServiceFilter getServicefilter() {
        return servicefilter;
    }

    public void setServicefilter(ServiceFilter servicefilter) {
        this.servicefilter = servicefilter;
    }
}
