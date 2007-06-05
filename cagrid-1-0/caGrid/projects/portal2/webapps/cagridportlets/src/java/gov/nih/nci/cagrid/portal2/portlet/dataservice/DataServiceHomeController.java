package gov.nih.nci.cagrid.portal2.portlet.dataservice;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
/**
 * @author <A HREF="MAILTO:parmarv@mail.nih.gov">Vijay Parmar</A>
 *
 */
public class DataServiceHomeController extends AbstractController {

    
	private DataServiceManager dataServiceManager;

    protected ModelAndView handleRenderRequestInternal(RenderRequest request, RenderResponse response) throws Exception {
    	return new ModelAndView("dataservicehome", "urlqueryobject", dataServiceManager.getUrlQueryObject());
    }


	public DataServiceManager getDataServiceManager() {
		return dataServiceManager;
	}


	public void setDataServiceManager(DataServiceManager dataServiceManager) {
		this.dataServiceManager = dataServiceManager;
	}
}