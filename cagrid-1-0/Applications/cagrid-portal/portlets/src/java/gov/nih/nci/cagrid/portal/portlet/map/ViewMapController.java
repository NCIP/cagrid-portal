/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.map;

import gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.DiscoveryDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.map.MapBean;

import javax.portlet.RenderRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 *
 */
public class ViewMapController extends AbstractViewObjectController {

    private DiscoveryModel discoveryModel;

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


    public DiscoveryModel getDiscoveryModel() {
        return discoveryModel;
    }

    public void setDiscoveryModel(DiscoveryModel discoveryModel) {
        this.discoveryModel = discoveryModel;
    }
}
