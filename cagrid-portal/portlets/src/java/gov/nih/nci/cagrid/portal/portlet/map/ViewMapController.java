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
package gov.nih.nci.cagrid.portal.portlet.map;

import gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.DiscoveryDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.map.MapBean;

import javax.portlet.RenderRequest;

import org.springframework.web.portlet.ModelAndView;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 *
 */
public class ViewMapController extends AbstractViewObjectController {

    private DiscoveryModel discoveryModel;
    private String solrServiceUrl;

    /**
     *
     */
    public ViewMapController() {

    }

    /* (non-Javadoc)
      * @see gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController#getObject(javax.portlet.RenderRequest)
      */
    @Override
    protected Object getObject(RenderRequest request) {
        MapBean mapBean = (MapBean) getApplicationContext().getBean("mapBeanPrototype");
        DiscoveryDirectory selectedDirectory = getDiscoveryModel().getSelectedDirectory();
        // Set the directory. Nodes are loaded asynchronously
        if(selectedDirectory != null)
            mapBean.setSelectedDirectory(selectedDirectory.getId());

        return mapBean;
    }


    @Override
    protected void addData(RenderRequest request, ModelAndView mav) {
        super.addData(request, mav);    //To change body of overridden methods use File | Settings | File Templates.
        mav.addObject("solrServiceUrl",getSolrServiceUrl());
    }

    public String getSolrServiceUrl() {
        return solrServiceUrl;
    }

    public void setSolrServiceUrl(String solrServiceUrl) {
        this.solrServiceUrl = solrServiceUrl;
    }

    public DiscoveryModel getDiscoveryModel() {
        return discoveryModel;
    }

    public void setDiscoveryModel(DiscoveryModel discoveryModel) {
        this.discoveryModel = discoveryModel;
    }
}
