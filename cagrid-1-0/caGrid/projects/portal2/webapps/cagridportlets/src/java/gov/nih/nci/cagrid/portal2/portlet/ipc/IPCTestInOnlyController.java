/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.ipc;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import message.MessageHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.PortletRequestUtils;
import org.springframework.web.portlet.mvc.Controller;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class IPCTestInOnlyController implements Controller {
	
	private static final String INPUT1_PARAM = "ipcInput1";
	private static final String INPUT2_PARAM = "ipcInput2";

	private static final Log logger = LogFactory
			.getLog(IPCTestInOnlyController.class);

	
	private String input1Name;
	private String input2Name;
	private String viewName;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.portlet.mvc.Controller#handleActionRequest(javax.portlet.ActionRequest,
	 *      javax.portlet.ActionResponse)
	 */
	public void handleActionRequest(ActionRequest request,
			ActionResponse response) throws Exception {
		MessageHelper.loadPrefs(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.portlet.mvc.Controller#handleRenderRequest(javax.portlet.RenderRequest,
	 *      javax.portlet.RenderResponse)
	 */
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {

		MessageHelper.loadPrefs(request);
		MessageHelper helper = new MessageHelper(request);
		String input1 = helper.getAsString(getInput1Name());
		String input2 = helper.getAsString(getInput2Name());
		ModelAndView mav = new ModelAndView(getViewName());
		if(input1 == null){
			input1 = "No Input 1";
		}
		if(input2 == null){
			input2 = "No Input 2";
		}
		logger.debug("input1 = " + input1 + ", input2 = " + input2);
		mav.addObject(INPUT1_PARAM, input1);
		mav.addObject(INPUT2_PARAM, input2);
		return mav;
	}

	public String getInput1Name() {
		return input1Name;
	}

	public void setInput1Name(String input1Name) {
		this.input1Name = input1Name;
	}
	
	public String getInput2Name() {
		return input2Name;
	}

	public void setInput2Name(String input2Name) {
		this.input2Name = input2Name;
	}	

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

}
