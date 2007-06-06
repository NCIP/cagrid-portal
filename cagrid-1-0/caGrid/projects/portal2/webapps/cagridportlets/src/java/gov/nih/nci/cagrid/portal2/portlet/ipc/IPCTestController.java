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
public class IPCTestController implements Controller {
	
	private static final String OUTPUT_PARAM = "ipcOutput";
	private static final String INPUT_PARAM = "ipcInput";

	private static final Log logger = LogFactory
			.getLog(IPCTestController.class);

	
	private String outputName;
	private String inputName;
	private String viewName;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.portlet.mvc.Controller#handleActionRequest(javax.portlet.ActionRequest,
	 *      javax.portlet.ActionResponse)
	 */
	public void handleActionRequest(ActionRequest request,
			ActionResponse response) throws Exception {
		String output = PortletRequestUtils.getStringParameter(request,
				OUTPUT_PARAM);
		logger.debug("output = " + output);
		if (output != null) {
			MessageHelper.loadPrefs(request);
			MessageHelper helper = new MessageHelper(request);
			logger.debug("publishing output = " + output);
			helper.send(getOutputName(), output);
		}
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
		String input = helper.getAsString(getInputName());
		ModelAndView mav = new ModelAndView(getViewName());
		if(input == null){
			input = "No Input";
		}
		logger.debug("input = " + input);
		mav.addObject(INPUT_PARAM, input);
		return mav;
	}

	public String getInputName() {
		return inputName;
	}

	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	public String getOutputName() {
		return outputName;
	}

	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

}
