package gov.nih.nci.cagrid.portal2.portlet.dataservice;


import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.mvc.SimpleFormController;
/**
 * @author <A HREF="MAILTO:parmarv@mail.nih.gov">Vijay Parmar</A>
 *
 */
public class QueryController extends SimpleFormController implements InitializingBean {

	
	
	  /** Logger for this class and subclasses */
    protected final static Log logger = LogFactory.getLog(QueryController.class);
    
    
    private DataServiceManager dataServiceManager;
    
    public void afterPropertiesSet() throws Exception {
        if (this.dataServiceManager == null)
            throw new IllegalArgumentException("A DataServiceManager is required");
    }

	public void onSubmitAction(ActionRequest request, ActionResponse response,
			Object command,	BindException errors) throws Exception {
		logger.info("Query2Controller.onSubmitAction ");
		
		URLQueryObject uqo= (URLQueryObject) command;
		
		String url = request.getParameter("url");
		String cqlQuery = request.getParameter("cqlQuery");
		logger.info("Request Parameter URL = "+url);
		logger.info("Request Parameter cqlQuery = "+cqlQuery);
		
		
		try{
			
			String results = dataServiceManager.executeQuery(uqo);
			uqo.setResults(results);
		
		}catch(Exception e){
			logger.info("Query2Controller.onSubmitAction(). Exception :"+e.getMessage());
			uqo.setResults(e.getMessage());	
			e.printStackTrace();
		}
		
		dataServiceManager.setUrlQueryObject(uqo);
		response.setRenderParameter("action","dataservicehome");
	}
	
    protected Object formBackingObject(PortletRequest request)
    		throws Exception {
    	
    	String tempurl = (String) request.getPortletSession().getAttribute("gridserviceurl");
    	String tempcqlquery = (String) request.getPortletSession().getAttribute("gridservicecqlquery");
    	    	
    	return this.dataServiceManager.getUrlQueryObject();
	}
    
	protected void initBinder(PortletRequest request, PortletRequestDataBinder binder)
			throws Exception {
	    
	    binder.setAllowedFields(new String[] {"url","cqlQuery"});
	}

	protected ModelAndView renderInvalidSubmit(RenderRequest request, RenderResponse response)
			throws Exception {
		
	    return null;
	}
	
	protected void handleInvalidSubmit(ActionRequest request, ActionResponse response)
			throws Exception {
		response.setRenderParameter("action","dataservicehome");
	}

	public void setDataServiceManager(DataServiceManager dataServiceManager) {
	    this.dataServiceManager = dataServiceManager;
	}

	public DataServiceManager getDataServiceManager() {
		return dataServiceManager;
	}
}
