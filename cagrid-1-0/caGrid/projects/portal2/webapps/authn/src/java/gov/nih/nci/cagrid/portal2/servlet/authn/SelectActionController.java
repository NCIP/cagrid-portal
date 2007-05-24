/**
 * 
 */
package gov.nih.nci.cagrid.portal2.servlet.authn;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectActionController extends SimpleFormController {
	
	private String gridLoginView;
	private String localLoginView;
	private String registerView;
	
	
	protected ModelAndView onSubmit(Object commandObj, BindException errors) throws Exception {
		
		ModelAndView mav = null;
		SelectActionCommand command = (SelectActionCommand)commandObj;
		if(SelectActionCommand.GRID_LOGIN_ACTION.equals(command.getSelectedAction())){
			mav = new ModelAndView(getGridLoginView());
		}else if(SelectActionCommand.LOCAL_LOGIN_ACTION.equals(command.getSelectedAction())){
			mav = new ModelAndView(getLocalLoginView());
		}else if(SelectActionCommand.REGISTER_ACTION.equals(command.getSelectedAction())){
			mav = new ModelAndView(getRegisterView());
		}else{
			//This shouldn't happen
			throw new RuntimeException("Invalid action: " + command.getSelectedAction());
		}
		
		mav.addObject("targetUrl", command.getTargetUrl());
		
		return mav;
	}
	
	
	public String getGridLoginView() {
		return gridLoginView;
	}
	public void setGridLoginView(String gridLoginView) {
		this.gridLoginView = gridLoginView;
	}
	public String getLocalLoginView() {
		return localLoginView;
	}
	public void setLocalLoginView(String localLoginView) {
		this.localLoginView = localLoginView;
	}
	public String getRegisterView() {
		return registerView;
	}
	public void setRegisterView(String registerView) {
		this.registerView = registerView;
	}
	
	

}
