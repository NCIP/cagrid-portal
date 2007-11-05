/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.list;

import java.util.List;

import gov.nih.nci.cagrid.portal2.portlet.discovery.DiscoveryResults;
import gov.nih.nci.cagrid.portal2.portlet.discovery.dir.AbstractDirectoryBean;
import gov.nih.nci.cagrid.portal2.portlet.discovery.dir.BaseDiscoveryViewController;
import gov.nih.nci.cagrid.portal2.portlet.discovery.dir.DiscoveryDirectory;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
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
		ListBean listBean = getListBean(request, dir.getType().toString(), dir.getId(), dir.getObjects());
		listBean.setSelectedDirectory(dir.getId());
		return listBean;
	}

	private ListBean getListBean(RenderRequest request, String type, String id, List objects) {
		ListBean listBean = (ListBean) request.getPortletSession()
				.getAttribute(getListBeanSessionAttributeName());
		
		boolean createNewListBean = false;
		
		if (listBean != null){
			String oldId = listBean.getSelectedResults();
			if(oldId == null){
				oldId = listBean.getSelectedDirectory();
			}
			if(!oldId.equals(id)){
				createNewListBean = true;
			}
		}
		if(listBean == null || createNewListBean){

			listBean = (ListBean) newDirectoryBean();
			listBean.setType(type);
			listBean.getScroller().setObjects(objects);
			listBean.refresh();
			
			request.getPortletSession().setAttribute(
					getListBeanSessionAttributeName(), listBean);
		}
		return listBean;
	}

	@Override
	protected AbstractDirectoryBean doHandleResults(RenderRequest request,
			RenderResponse response, DiscoveryResults results) throws Exception {
		ListBean listBean = getListBean(request, results.getType().toString(), results.getId(), results.getObjects());
		listBean.setSelectedResults(results.getId());
		return listBean;
	}

	public String getListBeanSessionAttributeName() {
		return listBeanSessionAttributeName;
	}

	public void setListBeanSessionAttributeName(
			String listBeanSessionAttributeName) {
		this.listBeanSessionAttributeName = listBeanSessionAttributeName;
	}

}
