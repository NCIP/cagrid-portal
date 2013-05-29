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
package gov.nih.nci.cagrid.portal.portlet.discovery.list;

import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryResults;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.AbstractDirectoryBean;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.BaseDiscoveryViewController;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.DiscoveryDirectory;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
public class ViewDirectoryListController extends BaseDiscoveryViewController {

    private String listBeanSessionAttributeName;

    /**
     *
     */
    public ViewDirectoryListController() {

    }

    protected AbstractDirectoryBean newDirectoryBean() {
        return (AbstractDirectoryBean) getApplicationContext().getBean(
                "listBeanPrototype");
    }

    @Override
    protected AbstractDirectoryBean doHandleDirectory(RenderRequest request,
                                                      RenderResponse response, DiscoveryDirectory dir) throws Exception {
        return getListBean(request, dir.getType().toString(), dir.getId(), dir.getObjects());
    }

    private ListBean getListBean(RenderRequest request, String type, String id, List objects) {
        ListBean listBean = (ListBean) request.getPortletSession()
                .getAttribute(getListBeanSessionAttributeName());

        boolean createNewListBean = false;

        if (listBean != null) {
            String oldId = listBean.getSelectedResults();
            if (oldId == null) {
                oldId = listBean.getSelectedDirectory();
            }
            if (!oldId.equals(id)) {
                createNewListBean = true;
            }
        }
        if (listBean == null || createNewListBean) {

            listBean = (ListBean) newDirectoryBean();
            listBean.setType(type);
            listBean.getScroller().setObjects(objects);


            request.getPortletSession().setAttribute(
                    getListBeanSessionAttributeName(), listBean);
        }
        listBean.refresh();
        return listBean;
    }

    @Override
    protected AbstractDirectoryBean doHandleResults(RenderRequest request,
                                                    RenderResponse response, DiscoveryResults results) throws Exception {
        return getListBean(request, results.getType().toString(), results.getId(), results.getObjects());
    }

    public String getListBeanSessionAttributeName() {
        return listBeanSessionAttributeName;
    }

    public void setListBeanSessionAttributeName(
            String listBeanSessionAttributeName) {
        this.listBeanSessionAttributeName = listBeanSessionAttributeName;
    }

}
