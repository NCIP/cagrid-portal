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
package gov.nih.nci.cagrid.portal.portlet;

import gov.nih.nci.cagrid.portal.util.filter.ServiceFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class FilteredContentGenerator extends org.springframework.web.portlet.handler.PortletContentGenerator {

    protected Log logger = LogFactory.getLog(getClass());

    private ServiceFilter filter;

    public ServiceFilter getFilter() {
        return filter;
    }

    public void setFilter(ServiceFilter filter) {
        this.filter = filter;
    }


}
