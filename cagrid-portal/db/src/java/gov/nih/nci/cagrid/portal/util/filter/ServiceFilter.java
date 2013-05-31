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
package gov.nih.nci.cagrid.portal.util.filter;

import gov.nih.nci.cagrid.portal.domain.GridService;

import java.util.List;

/**
 * Interface for filtering services.
 * Implementation classes will filter services
 * based on some criteria
 * <p/>
 * Used to filter services in the View
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface ServiceFilter {

    /**
     * Filter a List of services based on
     * some pre-defined criteria.
     * Implementations can define their filter criterias
     *
     * @param services
     * @return
     */
    public List<GridService> filter(List<GridService> services);

    /**
     * Will return boolean flag if a service will be
     * filtered by implementing class
     *
     * @param service
     * @return
     */
    public boolean willBeFiltered(GridService service);
}
