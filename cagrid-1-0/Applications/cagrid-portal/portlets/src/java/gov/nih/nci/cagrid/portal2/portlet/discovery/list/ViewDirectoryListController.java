/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.list;

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
public class ViewDirectoryListController extends
		BaseDiscoveryViewController {

	/**
	 * 
	 */
	public ViewDirectoryListController() {

	}

	
	protected AbstractDirectoryBean newDirectoryBean() {
		return (AbstractDirectoryBean)getApplicationContext().getBean("listBeanPrototype");
	}

	@Override
	protected void doHandleDirectory(RenderRequest request,
			RenderResponse response, DiscoveryDirectory dir, AbstractDirectoryBean dirBean)
			throws Exception {

		ListBean listBean = (ListBean)dirBean;
		listBean.setType(dir.getType().toString());
		listBean.getScroller().setObjects(dir.getObjects());
		listBean.refresh();		
	}


	@Override
	protected void doHandleResults(RenderRequest request,
			RenderResponse response, DiscoveryResults results, AbstractDirectoryBean dirBean)
			throws Exception {
		ListBean listBean = (ListBean)dirBean;
		listBean.setType(results.getType().toString());
		listBean.getScroller().setObjects(results.getObjects());
		listBean.refresh();
	}

}
